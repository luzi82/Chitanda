package com.luzi82.chitanda.server.test;

import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

import com.luzi82.java.Java;

public class Log extends LinkedList<Log.LogUnit> {
	public static class LogUnit extends TreeMap<String, Object> {
		private static final long serialVersionUID = 7928554331245794491L;
		long mTime;
		Log.Logger mLogger;
		StackTraceElement[] mStackTrace;
	
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(BlockBreakServiceTest.LOG_SIMPLEDATEFORMAT.format(new Date(mTime)));
			sb.append(": ");
			sb.append(mLogger.getClass().getName());
			return sb.toString();
		}
	}

	public static class Logger extends Log {
		private static final long serialVersionUID = -2886716288797349215L;
	
		public Log mLog;
		public final String mName;
	
		public Logger(String aName, Log aLog) {
			mLog = aLog;
			mName = aName;
		}
	
		public LogUnit createLog() {
			LogUnit ret = new LogUnit();
			ret.mTime = System.currentTimeMillis();
			ret.mLogger = this;
			ret.mStackTrace = Java.getStackTrace(1);
			addLast(ret);
			if (mLog != null)
				mLog.addLast(ret);
			return ret;
		}
	}

	private static final long serialVersionUID = 7993092370196095160L;
}