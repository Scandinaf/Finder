package com.apps.tools.segmentation.finder.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import com.apps.tools.segmentation.finder.App;
import com.apps.tools.segmentation.finder.domains.Constants;

public class FileReader implements Runnable {
	protected final BlockingQueue<String> queue;
	protected final String path;
	protected final int threadCount;
	
	public FileReader(BlockingQueue<String> queue, String path, int threadCount) {
		this.queue = queue;
		this.path = path;
		this.threadCount = threadCount;
	}
	
	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(App.class.getResourceAsStream(path), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				queue.put(line);
			}
			for (int i = 0; i < threadCount; i++) {
				queue.put(Constants.POISON_MESSAGE);
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}