package org.gof.behaviac;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

public class Utils {
	public static boolean IsNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static long MakeVariableId(String idstring) {
		var c = new CRC32();
		c.update(idstring.getBytes());
		return c.getValue();
	}

	public static Class<?> GetPrimitiveTypeFromName(String typeName) {
		if (IsNullOrEmpty(typeName)) {
			return null;
		}
		switch (typeName) {
		case "bool":
		case "Boolean":
			return boolean.class;

		case "int":
		case "Int32":
			return int.class;

		case "uint":
		case "UInt32":
			return int.class;

		case "short":
		case "Int16":
			return short.class;

		case "ushort":
		case "UInt16":
			return short.class;

		case "char":
		case "Char":
			return char.class;

		case "sbyte":
		case "SByte":
			return byte.class;

		case "ubyte":
		case "Ubyte":
		case "byte":
		case "Byte":
			return byte.class;

		case "long":
		case "llong":
		case "Int64":
			return long.class;

		case "ulong":
		case "ullong":
		case "UInt64":
			return long.class;

		case "float":
		case "Single":
			return float.class;

		case "double":
		case "Double":
			return double.class;

		case "string":
		case "String":
			return String.class;
		}

		return Utils.GetType(typeName);
	}

	public static Class<?> GetType(String typeName) {
		try {
			return Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static Class<?> GetElementTypeFromName(String typeName) {
		boolean bArrayType = false;

		// array type
		if (typeName.startsWith("vector<")) {
			bArrayType = true;
		}

		if (bArrayType) {
			int bracket0 = typeName.indexOf('<');
			int bracket1 = typeName.indexOf('>');
			// int len = bracket1 - bracket0 - 1;

			var elementTypeName = typeName.substring(bracket0 + 1, bracket1);
			var elementType = Utils.GetTypeFromName(elementTypeName);

			return elementType;
		}

		return null;
	}

	public static Class<?> GetTypeFromName(String typeName) {
		if (typeName == "void*") {
			return Void.class;
		}

		Class<?> type = AgentMeta.GetTypeFromName(typeName);

		if (type == null) {
			type = Utils.GetPrimitiveTypeFromName(typeName);

			if (type == null) {
				Class<?> elementType = Utils.GetElementTypeFromName(typeName);
				if (elementType != null) {

					Class<?> vectorType = ArrayList.class;// typeof(List<>).MakeGenericType(elementType);
					return vectorType;
				} else {
					typeName = typeName.replace("::", ".");
					type = Utils.GetType(typeName);
				}
			}
		}

		return type;
	}

	public static String Combine(String path, String name) {
		if (path.endsWith("\\") || path.endsWith("/"))
			return path + name;
		return path + "/" + name;
	}

	public static boolean IsIntegerClass(Class<?> clazz) {
		return clazz == int.class || clazz == byte.class || clazz == short.class || clazz == long.class;
	}

	public static boolean IsFloatClass(Class<?> clazz) {
		return clazz == float.class || clazz == double.class;
	}

	public static Object Clone(Class<?> clazz, boolean isList, Object value) {
		return ConvertFromObject(clazz, isList, value);
	}

	public static Object ConvertFromString(Class<?> clazz, boolean isList, String valueStr) {
		try {
			if (!isList) {
				if (IsIntegerClass(clazz)) {
					return ConvertFromObject(clazz, false, Long.parseLong(valueStr));
				}
				if (IsFloatClass(clazz)) {
					return ConvertFromObject(clazz, false, Double.parseDouble(valueStr));
				}
				if (clazz == String.class) {
					return valueStr;
				}
				if (clazz == Boolean.class) {
					return Boolean.parseBoolean(valueStr);
				}
				Debug.Check(false, String.format("unsupported convert from '%s' to <%s>", valueStr, clazz.toString()));
			} else {
				return FromStringVector(clazz, valueStr);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object ConvertFromObject(Class<?> targetClazz, boolean isList, Object value) {
		var clazz = targetClazz;
		if (!isList) {
			if (IsIntegerClass(clazz)) {
				var longValue = ((Number) value).longValue();
				if (clazz == int.class)
					return (int) longValue;
				else if (clazz == byte.class)
					return (byte) longValue;
				else if (clazz == short.class)
					return (short) longValue;
				else if (clazz == long.class)
					return (long) longValue;
			} else if (IsFloatClass(clazz)) {
				var doubleValue = ((Number) value).doubleValue();
				if (clazz == float.class)
					return (float) doubleValue;
				else if (clazz == double.class)
					return (double) doubleValue;
			} else if (clazz == Boolean.class) {
				if (value.getClass() == Boolean.class)
					return ((Boolean) value).booleanValue();
				else if (value.getClass() == String.class)
					return Boolean.parseBoolean((String) value);
			} else if (clazz == String.class) {
				if (value.getClass() == String.class)
					return ((String) value);
				return value.toString();
			}
		} else {
			if (value instanceof List) {
				var list = (List) value;
				var result = new ArrayList<>(list.size());
				for (int i = 0; i < list.size(); ++i) {
					result.add(ConvertFromObject(targetClazz, false, list.get(i)));
				}
				return result;
			}
			Debug.Check(false, "不支持将值转化为vector!");
		}
		return null;
	}

	public static String GetNativeTypeName(ClassInfo clazz) {
		return clazz.getName();
	}

	public static String ChangeExtension(String file, String newExt) {
		var ext = StringUtils.FindExtension(file);
		if (!IsNullOrEmpty(ext)) {
			file.replaceAll(ext, newExt);
		}
		return file;
	}

	public static TValue GetDefaultValue(ClassInfo _clazz) {
		if (_clazz.isList()) {
			return new TValue(_clazz, new ArrayList<Object>());
		}
		try {
			return new TValue(_clazz, _clazz.getElemClass().newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Agent GetParentAgent(Agent self, String _instance) {
		return self;

		// TODO:CBH 命名agent
	}

	private static int SkipPairedBrackets(String src, int indexBracketBegin) {
		if (!IsNullOrEmpty(src) && src.charAt(indexBracketBegin) == '{') {
			int depth = 0;
			int pos = indexBracketBegin;

			while (pos < src.length()) {
				if (src.charAt(pos) == '{') {
					depth++;
				} else if (src.charAt(pos) == '}') {
					if (--depth == 0) {
						return pos;
					}
				}

				pos++;
			}
		}
		return -1;
	}

	private static Object FromStringVector(Class<?> elemClass, String src) {
		var objVector = new ArrayList<Object>();

		if (IsNullOrEmpty(src)) {
			return objVector;
		}

		int semiColon = src.indexOf(':');
		Debug.Check(semiColon != -1);
		String countStr = src.substring(0, semiColon);
		int count = Integer.parseInt(countStr);

		int b = semiColon + 1;
		int sep = b;

		if (b < src.length() && src.charAt(b) == '{') {
			sep = SkipPairedBrackets(src, b);
			Debug.Check(sep != -1);
		}

		sep = src.indexOf('|', sep);

		while (sep != -1) {
			String elemStr = src.substring(b, sep);
			Object elemObject = ConvertFromString(elemClass, false, elemStr);

			objVector.add(elemObject);

			b = sep + 1;

			if (b < src.length() && src.charAt(b) == '{') {
				sep = SkipPairedBrackets(src, b);
				Debug.Check(b != -1);
			} else {
				sep = b;
			}

			sep = src.indexOf('|', sep);
		}

		if (b < src.length()) {
			String elemStr = src.substring(b, src.length());
			Object elemObject = ConvertFromString(elemClass, false, elemStr);

			objVector.add(elemObject);
		}

		Debug.Check(objVector.size() == count);

		return objVector;
	}

	public static final String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return OS.startsWith("windows");
	}

	public static boolean isLinux() {
		return OS.startsWith("linux");
	}
}
