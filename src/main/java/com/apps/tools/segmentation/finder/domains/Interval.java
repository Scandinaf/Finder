package com.apps.tools.segmentation.finder.domains;

public class Interval implements Comparable<Interval> {
	
	protected final long minIp;
	protected final long maxIp;
	protected final String segment;
	
	public Interval(long minIp, long maxIp, String segment) {
		this.segment = segment;
		this.minIp = minIp;
		this.maxIp = maxIp;
	}
	
	public boolean contains(long userIp) {
		return userIp < maxIp && userIp > minIp;
	}
	
	public int compareTo(Interval other) {	
		if (minIp < other.getMinIp()) return -1;
		else if (minIp > other.getMinIp()) return 1;
		else if (maxIp < other.getMaxIp()) return -1;
		else if (maxIp > other.getMaxIp()) return 1;
		else return 0;
	}

	public long getMinIp() {
		return minIp;
	}

	public long getMaxIp() {
		return maxIp;
	}

	public String getSegment() {
		return segment;
	}
}