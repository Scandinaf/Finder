package com.apps.tools.segmentation.finder.handlers;

import com.apps.tools.segmentation.finder.domains.Constants;

public abstract class BaseAggregator {

	protected long ipToLong(String ipAddress) {
		String[] ipAddressInArray = ipAddress.split("\\.");
		long result = 0;
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}
		return result;
	}
	
	protected boolean isFinallMessage(String message) {
		if (Constants.POISON_MESSAGE_HASHCODE != message.hashCode()) return false;
		return Constants.POISON_MESSAGE.equals(message);
	}
}
