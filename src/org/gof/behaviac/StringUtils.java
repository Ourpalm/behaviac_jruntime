package org.gof.behaviac;

public class StringUtils {

	public static boolean IsValidString(String str) {
		if (Utils.isNullOrEmpty(str) || (str.charAt(0) == '\"' && str.charAt(1) == '\"')) {
			return false;
		}

		return true;
	}

	public static String FindExtension(String relativePath) {
		String extension = "";
		int i = relativePath.lastIndexOf('.');
		if (i > 0) {
			extension = relativePath.substring(i + 1);
		}
		return extension;
	}

}
