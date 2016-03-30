package com.apps.tools.segmentation.finder.handlers;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.apps.tools.segmentation.finder.domains.Constants;
import com.apps.tools.segmentation.finder.domains.Interval;
import com.apps.tools.segmentation.finder.intervalTree.IntervalNode;

public class TransactionsAggregator extends BaseAggregator implements Runnable {
	protected final BlockingQueue<String> rangesQueue;
	protected final BlockingQueue<String> resultQueue;
	protected final IntervalNode intervalTree;
	protected final CountDownLatch latch;
	
	public TransactionsAggregator(BlockingQueue<String> rangesQueue,
			BlockingQueue<String> resultQueue, IntervalNode intervalTree, CountDownLatch transactionsLatch) {
		this.rangesQueue = rangesQueue;
		this.resultQueue = resultQueue;
		this.intervalTree = intervalTree;
		this.latch = transactionsLatch;
	}
	
	@Override
	public void run() {
		while(true) {
			String value = null;
			try {
				value = rangesQueue.take();
			} catch (InterruptedException e) {
				System.out.println(e);
				Thread.currentThread().interrupt();
				break;
			}
			if (isFinallMessage(value)) break;
			String[] parseStrings = Constants.TRANSATIONS_PATTERN.split(value);
			findIpSegment(ipToLong(parseStrings[1]), parseStrings[0]);
		}
		latch.countDown();
	}
	
	protected void findIpSegment(long userIp, String userId) {
		List<Interval> results = intervalTree.stab(userIp);
		for (Interval interval : results) {
			try {
				resultQueue.put(getResult(userId, interval.getSegment()));
			} catch (InterruptedException e) {
				System.out.println(e);
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
	
	protected String getResult(String userId, String segment) {
		StringBuilder sb = new StringBuilder();
		sb.append(userId).append(" ").append(segment);
		return sb.toString();
	}
}