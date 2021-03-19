package com.github.grubsic.rcapi;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDUtils{

	public static String trimmedToFull(String uuid){
		Pattern pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
		return pattern.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
	}

	public static String fullToTrimmed(String uuid){
		return uuid.replaceAll("-", "");
	}
	public static String fullToTrimmed(UUID uuid){
		return uuid.toString().replaceAll("-", "");
	}

}
