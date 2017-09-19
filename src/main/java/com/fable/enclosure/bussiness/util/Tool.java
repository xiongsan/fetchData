package com.fable.enclosure.bussiness.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Tool {

	public static String newGuid() {
		return UUID.randomUUID().toString();
	}

	public static String nowToString() {
		Date dt = new Date();
		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return from.format(dt);
	}

	public static String convertForUpdate(String s){
		String[] strings=s.split(",");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<strings.length;i++){
			if(i<strings.length-1){
				sb.append(strings[i]+"="+"#" + "{" + strings[i] + "}"+","+"\n");
			}
			else{
				sb.append(strings[i]+"="+"#" + "{" + strings[i] + "}");
			}

		}
		return sb.toString();
	}

	public static String convertForInsert(String s){
		String[] strings=s.split(",");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<strings.length;i++){
			if(i<strings.length-1){
				sb.append("#" + "{" + strings[i] + "}"+",");
			}
			else{
				sb.append("#" + "{" + strings[i] + "}");
			}

		}
		return sb.toString();
	}

	public static String reverse(String originStr) {
		if(originStr == null || originStr.length() <= 1)
			return originStr;
		return reverse(originStr.substring(1)) + originStr.charAt(0);
	}
}
