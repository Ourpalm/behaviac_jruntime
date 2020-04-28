package org.gof.behaviac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static IInstanceMember ParseProperty(String value, Class<?> clazz) {
		try {
			if (Utils.isNullOrEmpty(value)) {
				return null;
			}

			var r = StringUtils.SplitTokens(value);
			List<String> tokens = r.value1;
			value = r.value2;

			// const
			if (tokens.size() == 1) {
				String typeName = Utils.GetNativeTypeName(clazz);

				return AgentMeta.CreateInstanceConst(typeName, tokens.get(0));
			}

			return ParseProperty(value);
		} catch (Exception e) {
			Debug.Check(false, e.getMessage());
		}

		return null;
	}

	public static IInstanceMember ParseProperty(String value) {
		return ParseProperty(value, (List<String>) null);
	}

	public static IInstanceMember ParseProperty(String value, List<String> tokens) {
		try {
			if (Utils.isNullOrEmpty(value)) {
				return null;
			}

			if (tokens == null) {
				var r = StringUtils.SplitTokens(value);

				tokens = r.value1;
				value = r.value2;
			}

			String typeName = "";

			if (tokens.get(0) == "const") {
				// const Int32 0
				Debug.Check(tokens.size() == 3);

				final int kConstLength = 5;
				String strRemaining = value.substring(kConstLength + 1);
				var r = StringUtils.FirstToken(strRemaining, ' ', typeName);
				int p = r.value1;
				typeName = r.value2;

				typeName = typeName.replace("::", ".");

				String strVale = strRemaining.substring(p + 1);

				// const
				return AgentMeta.CreateInstanceConst(typeName, strVale);
			} else {
				String propStr = "";
				String indexPropStr = "";

				if (tokens.get(0) == "static") {
					// static float Self.AgentNodeTest::s_float_type_0
					// static float Self.AgentNodeTest::s_float_type_0[int
					// Self.AgentNodeTest::par_int_type_2]
					Debug.Check(tokens.size() == 3 || tokens.size() == 4);

					typeName = tokens.get(1);
					propStr = tokens.get(2);

					if (tokens.size() == 4) // array index
					{
						indexPropStr = tokens.get(3);
					}
				} else {
					// float Self.AgentNodeTest::par_float_type_1
					// float Self.AgentNodeTest::par_float_type_1[int
					// Self.AgentNodeTest::par_int_type_2]
					Debug.Check(tokens.size() == 2 || tokens.size() == 3);

					typeName = tokens.get(0);
					propStr = tokens.get(1);

					if (tokens.size() == 3) // array index
					{
						indexPropStr = tokens.get(2);
					}
				}

				String arrayItem = "";
				IInstanceMember indexMember = null;

				if (!Utils.isNullOrEmpty(indexPropStr)) {
					arrayItem = "[]";
					indexMember = ParseProperty(indexPropStr, int.class);
				}

				typeName = typeName.replace("::", ".");
				propStr = propStr.replace("::", ".");

				String[] props = propStr.split('.');
				Debug.Check(props.length >= 3);

				String instantceName = props[0];
				String propName = props[props.length - 1];
				String className = props[1];

				for (int i = 2; i < props.length - 1; ++i) {
					className += "." + props[i];
				}

				var classId = Utils.MakeVariableId(className);
				AgentMeta meta = AgentMeta.GetMeta(classId);
				Debug.Check(meta != null,
						"please add the exported 'AgentProperties.cs' and 'customizedtypes.cs' into the project!");

				var propId = Utils.MakeVariableId(propName + arrayItem);

				// property
				if (meta != null) {
					IProperty p = meta.GetProperty(propId);

					if (p != null) {
						return p.CreateInstance(instantceName, indexMember);
					}
				}

				// local var
				return AgentMeta.CreateInstanceProperty(typeName, instantceName, indexMember, propId);
			}
		} catch (Exception e) {
			Debug.Check(false, e.getMessage());
		}

		return null;
	}

	public static IMethod ParseMethod(String valueStr) {
		String methodName = "";
		return ParseMethod(valueStr, methodName).value1;
	}

	private static Tuple4<Integer, String, String, String> ParseMethodNames(String fullName,
			/* ref */String agentIntanceName, /* ref */ String agentClassName, /* ref */ String methodName) {
		// Self.test_ns::AgentActionTest::Action2(0)
		int pClassBegin = fullName.indexOf('.');
		Debug.Check(pClassBegin != -1);

		agentIntanceName = fullName.substring(0, pClassBegin);

		int pBeginAgentClass = pClassBegin + 1;

		int pBeginP = fullName.indexOf('(', pBeginAgentClass);
		Debug.Check(pBeginP != -1);

		// test_ns::AgentActionTest::Action2(0)
		int pBeginMethod = fullName.lastIndexOf(':', pBeginP);
		Debug.Check(pBeginMethod != -1);
		// skip '::'
		Debug.Check(fullName.charAt(pBeginMethod) == ':' && fullName.charAt(pBeginMethod - 1) == ':');
		pBeginMethod += 1;

		methodName = fullName.substring(pBeginMethod, pBeginP);

		int pos = pBeginMethod - 2 - pBeginAgentClass;

		agentClassName = fullName.substring(pBeginAgentClass, pos + pBeginAgentClass).replace("::", ".");

		return new Tuple4<>(pBeginP, agentIntanceName, agentClassName, methodName);
	}

	public static Tuple2<IMethod, String> ParseMethod(String valueStr, /* ref */ String methodName) {
		// Self.test_ns::AgentActionTest::Action2(0)
		if (Utils.isNullOrEmpty(valueStr) || (valueStr.charAt(0) == '\"' && valueStr.charAt(1) == '\"')) {
			return null;
		}

		String agentIntanceName = null;
		String agentClassName = null;
		var r0 = ParseMethodNames(valueStr, agentIntanceName, agentClassName, methodName);
		;

		int pBeginP = r0.value1;
		agentIntanceName = r0.value2;
		agentClassName = r0.value3;
		methodName = r0.value4;

		var agentClassId = Utils.MakeVariableId(agentClassName);
		var methodId = Utils.MakeVariableId(methodName);

		AgentMeta meta = AgentMeta.GetMeta(agentClassId);
		Debug.Check(meta != null);

		if (meta != null) {
			IMethod method = meta.GetMethod(methodId);

			if (method == null) {
				Debug.Check(false, String.format("Method of %s::%s is not registered!\n", agentClassName, methodName));
			} else {
				method = (IMethod) (method.Clone());

				String paramsStr = valueStr.substring(pBeginP);
				Debug.Check(paramsStr.charAt(0) == '(');

				List<String> paramsTokens = new ArrayList<String>();
				int len = paramsStr.length();
				Debug.Check(paramsStr.charAt(len - 1) == ')');

				String text = paramsStr.substring(1, len - 2 + 1);
				paramsTokens = ParseForParams(text);

				var array = new String[paramsTokens.size()];
				for (var k = 0; k < paramsTokens.size(); ++k) {
					array[k] = paramsTokens.get(k);
				}
				method.Load(agentIntanceName, array);
			}

			return new Tuple2<>(method, methodName);
		}

		return new Tuple2<>(null, null);
	}

	// suppose params are seprated by ','
	private static List<String> ParseForParams(String tsrc) {
		tsrc = StringUtils.RemoveQuot(tsrc);

		int tsrcLen = tsrc.length();
		int startIndex = 0;
		int index = 0;
		int quoteDepth = 0;

		List<String> params_ = new ArrayList<String>();

		for (; index < tsrcLen; ++index) {
			if (tsrc.charAt(index) == '"') {
				quoteDepth++;

				if ((quoteDepth & 0x1) == 0) {
					// closing quote
					quoteDepth -= 2;
					Debug.Check(quoteDepth >= 0);
				}
			} else if (quoteDepth == 0 && tsrc.charAt(index) == ',') {
				// skip ',' inside quotes, like "count, count"
				String strTemp = tsrc.substring(startIndex, index);
				params_.add(strTemp);
				startIndex = index + 1;
			}
		} // end for

		// the last param
		int lengthTemp0 = index - startIndex;

		if (lengthTemp0 > 0) {
			String strTemp = tsrc.substring(startIndex, index);
			params_.add(strTemp);
		}

		return params_;
	}

	private static AgentMeta GetMeta(long agentClassId) {
		// TODO Auto-generated method stub
		return null;
	}

	private static IInstanceMember CreateInstanceProperty(String typeName, String instantceName,
			IInstanceMember indexMember, long propId) {
		// TODO Auto-generated method stub
		return null;
	}

	private static IInstanceMember CreateInstanceConst(String typeName, String strVale) {
		// TODO Auto-generated method stub
		return null;
	}

	public IProperty GetProperty(long propId) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod GetMethod(long eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Long, IInstantiatedVariable> InstantiateCustomizedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void Register() {
		// TODO Auto-generated method stub

	}

	public static void UnRegister() {
		// TODO Auto-generated method stub

	}

}
