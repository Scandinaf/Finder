package com.apps.tools.segmentation.finder.handlers;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.apps.tools.segmentation.finder.domains.Constants;
import com.apps.tools.segmentation.finder.domains.Interval;

public class RangesAggregator extends BaseAggregator implements Runnable {
	protected final BlockingQueue<String> queue;
	protected final List<Interval> list;
	protected final CountDownLatch latch;
	
	public RangesAggregator(BlockingQueue<String> queue, List<Interval> list, CountDownLatch latch) {
		this.queue = queue;
		this.list = list;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		while(true) {
			String value = null;
			try {
				value = queue.take();
			} catch (InterruptedException e) {
				System.out.println(e);
				Thread.currentThread().interrupt();
				break;
			}
			if (isFinallMessage(value)) break;
			String[] parseStrings = Constants.RANGES_PATTERN.split(value);
			list.add(new Interval(ipToLong(parseStrings[0]), ipToLong(parseStrings[1]), parseStrings[2]));
		}
		latch.countDown();
	}
}