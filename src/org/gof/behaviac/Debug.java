package org.gof.behaviac;

public class Debug {
	public static void check(boolean cond) {
		if(!cond)
			throw new RuntimeException("check failure!");
	}
	
	public static void check(boolean cond, String msg) {
		if(!cond)
			throw new RuntimeException("check failure!" + msg);
	}

	public static void LogError(String text) {
		
	}
}
