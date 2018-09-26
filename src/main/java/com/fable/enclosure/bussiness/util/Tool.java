package com.fable.enclosure.bussiness.util;

import com.fable.enclosure.bussiness.entity.MethodPropertiesCachedEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Tool {

private  static	DataSourceTransactionManager txManager = SpringContextUtil.getBean(DataSourceTransactionManager.class);

private static DefaultTransactionDefinition def = new DefaultTransactionDefinition();

private static TransactionStatus transactionStatus;

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

	public static void  startTransaction(MethodPropertiesCachedEntity entity){

		def.setPropagationBehavior(entity.getPropagation().value());// 事物隔离级别，开启新事务..
		def.setIsolationLevel(entity.getIsolation().value());
		transactionStatus=txManager.getTransaction(def); // 获得事务状态
	}

	public static void endTransaction(){
		txManager.commit(transactionStatus);
	}

	public static void rollBack(){
		txManager.rollback(transactionStatus);
	}

	public static String reverse(String originStr) {
		if(originStr == null || originStr.length() <= 1)
			return originStr;
		return reverse(originStr.substring(1)) + originStr.charAt(0);
	}

}
