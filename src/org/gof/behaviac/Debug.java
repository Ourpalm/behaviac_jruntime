package org.gof.behaviac;

public class Debug {
	public static void Check(boolean cond) {
		if (!cond)
			throw new RuntimeException("check failure!");
	}

	public static void Check(boolean cond, String msg) {
		if (!cond)
			throw new RuntimeException("check failure!" + msg);
	}

	public static void LogError(String text) {

	}

	public static void Log(String text) {

	}
}
