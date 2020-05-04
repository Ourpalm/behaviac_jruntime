package org.gof.behaviac.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.Debug;
import org.gof.behaviac.members.IInstanceMember;

public class StringUtils {

	public static boolean IsValidString(String str) {
		if (Utils.IsNullOrEmpty(str) || (str.charAt(0) == '\"' && str.charAt(1) == '\"')) {
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

	public static Tuple2<Integer, String> FirstToken(String params_, char sep, String t) {
		// const int 5
		int end = params_.indexOf(sep);
		if (end != -1) {
			var token = params_.substring(0, end);
			return new Tuple2<>(end, token);
		}

		return new Tuple2<>(-1, t);
	}

	private static final String kQuotStr = "&quot;";

	public static String RemoveQuot(String str) {
		String ret = str;

		if (ret.startsWith(kQuotStr)) {
			// Debug.Check(ret.EndsWith(kQuotStr));
			ret = ret.replace(kQuotStr, "\"");
		}

		return ret;
	}

	public static Tuple2<List<String>, String> SplitTokens(String str) {
		List<String> ret = new ArrayList<String>();

		// "const String \"test String\""
		// "const int 10"
		// "int Self.AgentArrayAccessTest::ListInts[int
		// Self.AgentArrayAccessTest::l_index]"

		str = RemoveQuot(str);

		if (str.startsWith("\"") && str.endsWith("\"")) {
			ret.add(str);

			return new Tuple2<>(ret, str);
		}

		if (str.startsWith("const String ")) {
			ret.add("const");
			ret.add("String");

			String strValue = str.substring(13);
			strValue = RemoveQuot(strValue);

			ret.add(strValue);

			str = "const String " + strValue;

			return new Tuple2<>(ret, str);
		}

		int pB = 0;
		int i = 0;

		boolean bBeginIndex = false;

		while (i < str.length()) {
			boolean bFound = false;
			char c = str.charAt(i);

			if (c == ' ' && !bBeginIndex) {
				bFound = true;
			} else if (c == '[') {
				bBeginIndex = true;
				bFound = true;
			} else if (c == ']') {
				bBeginIndex = false;
				bFound = true;
			}

			if (bFound) {
				String strT = ReadToken(str, pB, i);
				Debug.Check(strT.length() > 0);
				ret.add(strT);

				pB = i + 1;
			}

			i++;
		}

		String t = ReadToken(str, pB, i);

		if (t.length() > 0) {
			ret.add(t);
		}

		return new Tuple2<>(ret, str);
	}

	private static String ReadToken(String str, int pB, int end) {
		String strT = "";
		int p = pB;

		while (p < end) {
			strT += str.charAt(p++);
		}

		return strT;
	}

	public static Tuple2<Boolean, String> ParseForStruct(Class<?> type, String str, /* ref */ String strT,
			Map<String, IInstanceMember> props) {
		int pB = 0;
		int i = 0;

		while (i < str.length()) {
			char c = str.charAt(i);

			if (c == ';' || c == '{' || c == '}') {
				int p = pB;

				while (p <= i) {
					strT += str.charAt(p++);
				}

				pB = i + 1;
			} else if (c == ' ') {
				// par or property
				String propName = "";
				int p = pB;

				while (str.charAt(p) != '=') {
					propName += str.charAt(p++);
				}

				// skip '='
				Debug.Check(str.charAt(p) == '=');
				p++;

				String valueStr = str.substring(p);

				String typeStr = "";

				while (str.charAt(p) != ' ') {
					typeStr += str.charAt(p++);
				}

				// boolean bStatic = false;

				if (typeStr.equals("static")) {
					// skip ' '
					Debug.Check(str.charAt(p) == ' ');
					p++;

					while (str.charAt(p) != ' ') {
						typeStr += str.charAt(p++);
					}

					// bStatic = true;
				}

				String parName = "";

				// skip ' '
				Debug.Check(str.charAt(i) == ' ');
				i++;

				while (str.charAt(i) != ';') {
					parName += str.charAt(i++);
				}

				props.put(propName, AgentMeta.ParseProperty(valueStr));

				// skip ';'
				Debug.Check(str.charAt(i) == ';');

				pB = i + 1;
			}

			i++;
		}

		return new Tuple2<>(true, strT);
	}

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	static Tuple2<Boolean, Integer> IsArrayString(String str, int posStart, /* ref */int posEnd) {
		// begin of the count of an array?
		// int posStartOld = posStart;

		boolean bIsDigit = false;

		int strLen = (int) str.length();
		;

		while (posStart < strLen) {
			char c = str.charAt(posStart++);

			if (isDigit(c)) {
				bIsDigit = true;
			} else if (c == ':' && bIsDigit) {
				// transit_points = 3:{coordX = 0; coordY = 0; } | {coordX = 0; coordY = 0; } |
				// {coordX = 0; coordY = 0; };
				// skip array item which is possible a struct
				int depth = 0;

				for (int posStart2 = posStart; posStart2 < strLen; posStart2++) {
					char c1 = str.charAt(posStart2);

					if (c1 == ';' && depth == 0) {
						// the last ';'
						posEnd = posStart2;
						break;
					} else if (c1 == '{') {
						Debug.Check(depth < 10);
						depth++;
					} else if (c1 == '}') {
						Debug.Check(depth > 0);
						depth--;
					}
				}

				return new Tuple2<>(true, posEnd);
			} else {
				break;
			}
		}

		return new Tuple2<>(false, posEnd);
	}

	public static List<String> SplitTokensForStruct(String src) {
		List<String> ret = new ArrayList<String>();

		if (Utils.IsNullOrEmpty(src)) {
			return ret;
		}

		// {color=0;id=;type={bLive=false;name=0;weight=0;};}
		// the first char is '{'
		// the last char is '}'
		int posCloseBrackets = Utils.SkipPairedBrackets(src, 0);
		Debug.Check(posCloseBrackets != -1);

		// {color=0;id=;type={bLive=false;name=0;weight=0;};}
		// {color=0;id=;type={bLive=false;name=0;weight=0;};transit_points=3:{coordX=0;coordY=0;}|{coordX=0;coordY=0;}|{coordX=0;coordY=0;};}
		int posBegin = 1;
		int posEnd = src.indexOf(';', posBegin);

		while (posEnd != -1) {
			Debug.Check(src.charAt(posEnd) == ';');

			// the last one might be empty
			if (posEnd > posBegin) {
				int posEqual = src.indexOf('=', posBegin);
				Debug.Check(posEqual > posBegin);

				int length = posEqual - posBegin;
				String memmberName = src.substring(posBegin, posEqual);
				String memberValueStr;
				char c = src.charAt(posEqual + 1);

				if (c != '{') {
					length = posEnd - posEqual - 1;

					// to check if it is an array
					var rr = IsArrayString(src, posEqual + 1, posEnd);
					posEnd = rr.value2;

					memberValueStr = src.substring(posEqual + 1, posEnd - 1);
				} else {
					int pStructBegin = 0;
					pStructBegin += posEqual + 1;
					int posCloseBrackets_ = Utils.SkipPairedBrackets(src, pStructBegin);
					memberValueStr = src.substring(posEqual + 1, posCloseBrackets_ + 1);

					posEnd = posEqual + 1 + length;
				}

				ret.add(memberValueStr);
			}

			// skip ';'
			posBegin = posEnd + 1;

			// {color=0;id=;type={bLive=false;name=0;weight=0;};transit_points=3:{coordX=0;coordY=0;}|{coordX=0;coordY=0;}|{coordX=0;coordY=0;};}
			posEnd = src.indexOf(';', posBegin);

			if (posEnd > posCloseBrackets) {
				break;
			}
		}

		return ret;
	}
}
