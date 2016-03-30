package com.apps.tools.segmentation.finder.handlers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class LatchFileReader extends FileReader {
	protected final CountDownLatch latch;
	
	public LatchFileReader(BlockingQueue<String> queue, CountDownLatch latch,
			String path, int threadCount) {
		super(queue, path, threadCount);
		this.latch = latch;
	}
	
	@Override
	public void run() {
		super.run();
		latch.countDown();
	}
}