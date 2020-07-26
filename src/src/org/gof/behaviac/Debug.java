package org.gof.behaviac;

public class Debug {
	public static void Check(boolean cond) {
		if (!cond)
			throw new RuntimeException("check failure!");
	}

	public static void Check(boolean cond, Exception ex) {
		if (!cond) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public static void Check(boolean cond, String msg) {
		if (!cond) {
			var e = new RuntimeException("check failure!" + msg);
			e.printStackTrace();
			throw e;
		}
	}

	public static void LogError(String text) {
		System.out.println("Debug.LogError" + text);
	}

	public static void Log(String text) {
		System.out.println("Debug.Log" + text);
	}

	public static void LogWarning(String text) {
		System.out.println("Debug.LogWarning" + text);
	}
}
