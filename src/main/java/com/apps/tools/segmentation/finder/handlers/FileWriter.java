package com.apps.tools.segmentation.finder.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

public class FileWriter extends BaseAggregator implements Runnable {
	protected final BlockingQueue<String> queue;
	protected final String path;
	
	public FileWriter(BlockingQueue<String> queue, String path) {
		this.queue = queue;
		this.path = path;
	}
	
	@Override
	public void run() {
		createFileIsNotExsist(path);
		try (PrintWriter pw = new PrintWriter(path)) {
			String value = null;
			while(true) {
				value = queue.take();
				if (isFinallMessage(value)) break;
				pw.println(value);
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	protected void createFileIsNotExsist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
				System.exit(1);
			}
		}
	}
}