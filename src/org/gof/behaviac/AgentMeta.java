package org.gof.behaviac;

import java.util.HashMap;

public class AgentMeta {
	private HashMap<Long, IProperty> _memberProperties = new HashMap<Long, IProperty>();
	private HashMap<Long, ICustomizedProperty> _customizedProperties = new HashMap<Long, ICustomizedProperty>();
	private HashMap<Long, ICustomizedProperty> _customizedStaticProperties = new HashMap<Long, ICustomizedProperty>();
	private HashMap<Long, IInstantiatedVariable> _customizedStaticVars = null;
	private HashMap<Long, IMethod> _methods = new HashMap<Long, IMethod>();

	private static HashMap<Long, AgentMeta> _agentMetas = new HashMap<Long, AgentMeta>();
	private static HashMap<String, TypeCreator> _Creators = new HashMap<String, TypeCreator>();
	private static HashMap<String, Class<?>> _typesRegistered = new HashMap<String, Class<?>>();

	public static ICustomizedProperty CreateProperty(String typeName, long varId, String name, String valueStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Class<?> GetTypeFromName(String typeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ICustomizedProperty CreateArrayItemProperty(String typeName, long varId, String name) {
		return null;
	}

	public static IInstanceMember ParseProperty(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public static IInstanceMember ParseMethod(String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
