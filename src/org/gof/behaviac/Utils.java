package org.gof.behaviac;

import java.util.ArrayList;
import java.util.zip.CRC32;

public class Utils {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static long MakeVariableId(String idstring) {
		var c = new CRC32();
		c.update(idstring.getBytes());
		return c.getValue();
	}

	public static Class<?> GetPrimitiveTypeFromName(String typeName) {
		if (isNullOrEmpty(typeName)) {
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
			//int len = bracket1 - bracket0 - 1;

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
	
	public static Object Clone(Object value) {
		return value;
	}

	public static Object ConvertFromString(Class<?> clazz, String valueStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String GetNativeTypeName(Class<?> clazz) {
		return clazz.getName();
	}
}
