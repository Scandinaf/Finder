package com.apps.tools.segmentation.finder;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.apps.tools.segmentation.finder.domains.Constants;
import com.apps.tools.segmentation.finder.domains.Interval;
import com.apps.tools.segmentation.finder.handlers.FileReader;
import com.apps.tools.segmentation.finder.handlers.FileWriter;
import com.apps.tools.segmentation.finder.handlers.LatchFileReader;
import com.apps.tools.segmentation.finder.handlers.RangesAggregator;
import com.apps.tools.segmentation.finder.handlers.TransactionsAggregator;
import com.apps.tools.segmentation.finder.intervalTree.IntervalNode;
import com.apps.tools.segmentation.finder.intervalTree.IntervalTreeBuilder;
 
public class App {
 
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(Constants.MAX_THREAD);
		BlockingQueue<String> rangesQueue = new ArrayBlockingQueue<>(Constants.QUEUE_CAMPACITY);
		ExecutorService cachedPool = Executors.newCachedThreadPool();
		cachedPool.submit(new LatchFileReader(rangesQueue, latch, Constants.RANGES_PATH, Constants.MAX_THREAD - 1));
		List<Interval> intervals = new CopyOnWriteArrayList<>();
		for (int i = 0; i < Constants.MAX_THREAD - 1; i++) cachedPool.submit(new RangesAggregator(rangesQueue, intervals, latch));
		latch.await();
		IntervalNode intervalTree = IntervalTreeBuilder.buildIntervalTree(intervals);
		cachedPool.submit(new FileReader(rangesQueue, Constants.TRANSATIONS_PATH, Constants.MAX_THREAD - 2));
		BlockingQueue<String> resultQueue = new ArrayBlockingQueue<>(Constants.QUEUE_CAMPACITY);
		CountDownLatch transactionsLatch = new CountDownLatch(Constants.MAX_THREAD - 2);
		for (int i = 0; i < Constants.MAX_THREAD - 2; i++) cachedPool.submit(new TransactionsAggregator(rangesQueue,
				resultQueue, intervalTree, transactionsLatch)); 
		cachedPool.submit(new FileWriter(resultQueue, Constants.OUTPUT_PATH));
		cachedPool.shutdown();
		transactionsLatch.await();
		resultQueue.put(Constants.POISON_MESSAGE);
	}
}