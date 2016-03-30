package com.apps.tools.segmentation.finder.intervalTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.apps.tools.segmentation.finder.domains.Interval;

public class IntervalNode {

	protected final long center;
	protected SortedMap<Interval, List<Interval>> intervals;
	protected IntervalNode leftNode;
	protected IntervalNode rightNode;
	
	public IntervalNode(List<Interval> intervalList) {
		
		intervals = new ConcurrentSkipListMap<Interval, List<Interval>>();
		
		center = getMedian(getEndpoints(intervalList));
		
		List<Interval> left = new ArrayList<Interval>();
		List<Interval> right = new ArrayList<Interval>();
		
		for(Interval interval : intervalList) {
			if(interval.getMaxIp() < center)
				left.add(interval);
			else if(interval.getMinIp() > center)
				right.add(interval);
			else {
				List<Interval> posting = intervals.get(interval);
				if(posting == null) {
					posting = new CopyOnWriteArrayList<Interval>();
					intervals.put(interval, posting);
				}
				posting.add(interval);
			}
		}

		if(left.size() > 0)
			leftNode = new IntervalNode(left);
		if(right.size() > 0)
			rightNode = new IntervalNode(right);
	}

	public List<Interval> stab(long userIp) {		
		List<Interval> result = new ArrayList<Interval>();

		for(Entry<Interval, List<Interval>> entry : intervals.entrySet()) {
			if(entry.getKey().contains(userIp))
				for(Interval interval : entry.getValue())
					result.add(interval);
			else if(entry.getKey().getMinIp() > userIp)
				break;
		}
		
		if(userIp < center && leftNode != null)
			result.addAll(leftNode.stab(userIp));
		else if(userIp > center && rightNode != null)
			result.addAll(rightNode.stab(userIp));
		return result;
	}
	
	protected SortedSet<Long> getEndpoints(List<Interval> intervalList) {
		SortedSet<Long> endpoints = new TreeSet<Long>();
		for(Interval interval: intervalList) {
			endpoints.add(interval.getMinIp());
			endpoints.add(interval.getMaxIp());
		}
		return endpoints;
	}
	
	protected Long getMedian(SortedSet<Long> set) {
		int middle = set.size() / 2;
		int i = 0;
		for(Long point : set) {
			if(i == middle)
				return point;
			i++;
		}
		return null;
	}
}