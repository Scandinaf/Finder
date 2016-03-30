package com.apps.tools.segmentation.finder.domains;

import java.util.regex.Pattern;

public final class Constants {
	public static final int QUEUE_CAMPACITY = 100;
	public static final int MAX_THREAD = 4;
	public static final String POISON_MESSAGE = "DONE";
	public static final int POISON_MESSAGE_HASHCODE = POISON_MESSAGE.hashCode();
	public static final Pattern RANGES_PATTERN = Pattern.compile("-|\\s");
	public static final Pattern TRANSATIONS_PATTERN = Pattern.compile("\\s");
	public static final String RANGES_PATH = "/files/ranges.tsv";
	public static final String TRANSATIONS_PATH = "/files/transactions.tsv";
	public static final String OUTPUT_PATH = "D:/output.tsv";
}
