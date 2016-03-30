package com.apps.tools.segmentation.finder.intervalTree;

import java.util.List;

import com.apps.tools.segmentation.finder.domains.Interval;

public class IntervalTreeBuilder {
	public static IntervalNode buildIntervalTree(List<Interval> intervals) {
		return new IntervalNode(intervals);
	}
}