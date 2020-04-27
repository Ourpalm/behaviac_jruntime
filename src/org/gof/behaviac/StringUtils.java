package org.gof.behaviac;

public class StringUtils {

	public static boolean IsValidString(String str) {
		if (Utils.isNullOrEmpty(str) || (str.charAt(0) == '\"' && str.charAt(1) == '\"')) {
			return false;
		}

		return true;
	}

}
