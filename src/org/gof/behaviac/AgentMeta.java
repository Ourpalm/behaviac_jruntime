package org.gof.behaviac;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.gof.behaviac.members.ICustomizedProperty;
import org.gof.behaviac.members.IInstanceMember;
import org.gof.behaviac.members.IInstantiatedVariable;
import org.gof.behaviac.members.IMethod;
import org.gof.behaviac.members.IProperty;
import org.gof.behaviac.utils.StringUtils;
import org.gof.behaviac.utils.Tuple2;
import org.gof.behaviac.utils.Tuple4;
import org.gof.behaviac.utils.Utils;

public class AgentMeta {
	private static HashMap<Long, AgentMeta> _agentMetas = new HashMap<Long, AgentMeta>();
	private static HashMap<String, TypeCreator> _Creators = new HashMap<String, TypeCreator>();
	private static HashMap<String, Class<?>> _typesRegistered = new HashMap<String, Class<?>>();
	private static long _totalSignature = 0;
	private static BehaviorLoader _behaviorLoader;

	public static void SetBehaviorLoader(BehaviorLoader loader) {
		_behaviorLoader = loader;
	}

	public static void SetTotalSignature(long signature) {
		_totalSignature = signature;
	}

	// -----------------------------------------------------------------------
	private HashMap<Long, IProperty> _memberProperties = new HashMap<Long, IProperty>();
	private HashMap<Long, ICustomizedProperty> _customizedProperties = new HashMap<Long, ICustomizedProperty>();
	private HashMap<Long, ICustomizedProperty> _customizedStaticProperties = new HashMap<Long, ICustomizedProperty>();
	private HashMap<Long, IInstantiatedVariable> _customizedStaticVars = null;
	private HashMap<Long, IMethod> _methods = new HashMap<Long, IMethod>();

	private long _signature = 0;

	public long GetSignature() {
		return this._signature;
	}

	public AgentMeta() {
		this(0);
	}

	public AgentMeta(long _signature) {
		this._signature = _signature;
	}

	public static void RegisterMeta(long signature, AgentMeta meta) {
		_agentMetas.put(signature, meta);
	}

	public static void Register() {
		RegisterBasicTypes();

		if (_behaviorLoader == null) {
			final String kLoaderClass = "behaviac.BehaviorLoaderImplement";
			try {
				var loaderType = Class.forName(kLoaderClass);
				_behaviorLoader = (BehaviorLoader) loaderType.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		_behaviorLoader.Load();
		LoadAllMetaFiles();
	}

	public static void UnRegister() {
		UnRegisterBasicTypes();

		if (_behaviorLoader != null) {
			_behaviorLoader.UnLoad();
		}

		_agentMetas.clear();
		_Creators.clear();
	}

	public static ICustomizedProperty CreateProperty(String typeName, long varId, String name, String valueStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Class<?> GetTypeFromName(String typeName) {
		return _typesRegistered.get(typeName);
	}

	public static ICustomizedProperty CreateArrayItemProperty(String typeName, long varId, String name) {
		return null;
	}

	public static IInstanceMember ParseProperty(String value, ClassInfo clazz) {
		try {
			if (Utils.IsNullOrEmpty(value)) {
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
			Debug.Check(false, e);
		}

		return null;
	}

	public static IInstanceMember ParseProperty(String value) {
		return ParseProperty(value, (List<String>) null);
	}

	public static IInstanceMember ParseProperty(String value, List<String> tokens) {
		try {
			if (Utils.IsNullOrEmpty(value)) {
				return null;
			}

			if (tokens == null) {
				var r = StringUtils.SplitTokens(value);

				tokens = r.value1;
				value = r.value2;
			}

			String typeName = "";

			if (tokens.get(0).equals("const")) {
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

				if (tokens.get(0).equals("static")) {
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

				if (!Utils.IsNullOrEmpty(indexPropStr)) {
					arrayItem = "[]";
					indexMember = ParseProperty(indexPropStr, new ClassInfo(int.class));
				}

				typeName = typeName.replace("::", ".");
				propStr = propStr.replace("::", ".");

				String[] props = propStr.split("\\.");
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
			Debug.Check(false, e);
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
		agentClassName = convertAgentName(agentClassName);
		return new Tuple4<>(pBeginP, agentIntanceName, agentClassName, methodName);
	}

	public static Tuple2<IMethod, String> ParseMethod(String valueStr, /* ref */ String methodName) {
		// Self.test_ns::AgentActionTest::Action2(0)
		if (Utils.IsNullOrEmpty(valueStr) || (valueStr.charAt(0) == '\"' && valueStr.charAt(1) == '\"')) {
			return null;
		}

		String agentIntanceName = null;
		String agentClassName = null;
		var r0 = ParseMethodNames(valueStr, agentIntanceName, agentClassName, methodName);

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

	private static void LoadAllMetaFiles() {
		String metaFolder = "";
		try {
			String ext = ".xml";

			metaFolder = Utils.Combine(Workspace.Instance.GetFilePath(), "meta");
			metaFolder = metaFolder.replace('\\', '/');

			if (!Utils.IsNullOrEmpty(Workspace.Instance.GetMetaFile())) {
				String metaFile = Utils.Combine(metaFolder, Workspace.Instance.GetMetaFile());
				metaFile = Utils.ChangeExtension(metaFile, ".meta");

				byte[] fileBuffer = Files.readAllBytes(Paths.get(metaFile, ext));

				load_xml(fileBuffer);
			} else {
				var stream = Files.find(Paths.get(metaFolder), 1, (Path p, BasicFileAttributes attr) -> {
					return p.toFile().getPath().endsWith(ext);
				});

				for (var iter = stream.iterator(); iter.hasNext();) {
					var path = iter.next();
					byte[] fileBuffer = Files.readAllBytes(path);
					load_xml(fileBuffer);

				}
			}
		} catch (Exception ex) {
			Debug.LogWarning(ex.getMessage());
		}
	}

	private static boolean checkSignature(String signatureStr) {
		if (!signatureStr.equals(Long.toString(AgentMeta._totalSignature))) {
			String errorInfo = "[meta] The types/AgentProperties.cs should be exported from the behaviac designer, and then integrated into your project!\n";

			Debug.LogWarning(errorInfo);
			Debug.Check(false, errorInfo);

			return false;
		}

		return true;
	}

	private static void registerCustomizedProperty(AgentMeta meta, String propName, String typeName, String valueStr,
			boolean isStatic) {
		typeName = typeName.replace("::", ".");

		var nameId = Utils.MakeVariableId(propName);
		IProperty prop = meta.GetProperty(nameId);
		ICustomizedProperty newProp = AgentMeta.CreateCustomizedProperty(typeName, nameId, propName, valueStr);

		if (prop != null && newProp != null) {
			Object newValue = newProp.GetValueObject(null);
			Object value = prop.GetValueObject(null);

			if (newValue != null && value != null && newValue.getClass() == value.getClass()) {
				return;
			}

			String errorInfo = String.format(
					"The type of '%s' has been modified to %s, which would bring the unpredictable consequences.",
					propName, typeName);
			Debug.LogWarning(errorInfo);
			Debug.Check(false, errorInfo);
		}

		if (isStatic) {
			meta.RegisterStaticCustomizedProperty(nameId, newProp);
		} else {
			meta.RegisterCustomizedProperty(nameId, newProp);
		}

		Class<?> type = AgentMeta.GetTypeFromName(typeName);

		if (type == List.class) {
			// Get item type, i.e. vector<int>
			var strVector = "vector<";
			int kStartIndex = strVector.length();
			typeName = typeName.substring(kStartIndex, typeName.length() - 1); // item type
			ICustomizedProperty arrayItemProp = AgentMeta.CreateCustomizedArrayItemProperty(typeName, nameId, propName);
			nameId = Utils.MakeVariableId(propName + "[]");

			if (isStatic) {
				meta.RegisterStaticCustomizedProperty(nameId, arrayItemProp);
			} else {
				meta.RegisterCustomizedProperty(nameId, arrayItemProp);
			}
		}
	}

	private static ICustomizedProperty CreateCustomizedArrayItemProperty(String typeName, long id, String name) {
		typeName = GetTypeName(typeName);
		if (_Creators.containsKey(typeName)) {
			TypeCreator creator = _Creators.get(typeName);
			return creator.CreateCustomizedArrayItemProperty(id, name);
		}
		Debug.Check(false);
		return null;
	}

	private static ICustomizedProperty CreateCustomizedProperty(String typeName, long id, String name,
			String valueStr) {
		typeName = GetTypeName(typeName);
		if (_Creators.containsKey(typeName)) {
			TypeCreator creator = _Creators.get(typeName);
			return creator.CreateCustomizedProperty(id, name, valueStr);
		}
		Debug.Check(false);
		return null;
	}

	private static boolean load_xml(byte[] pBuffer) {
		try {
			Debug.Check(pBuffer != null);
			var xml = new String(pBuffer, StandardCharsets.UTF_8);
			var doc = DocumentHelper.parseText(xml);
			var rootNode = doc.getRootElement();

			if (rootNode.elements().size() <= 0 || rootNode.getName() != "agents") {
				return false;
			}

			String versionStr = rootNode.attribute("version").getValue();
			Debug.Check(!Utils.IsNullOrEmpty(versionStr));

			String signatureStr = rootNode.attribute("signature").getValue();
			checkSignature(signatureStr);

			for (var bbNode : rootNode.elements()) {
				if (bbNode.getName().equals("agent") && bbNode.elements() != null) {
					String agentType = bbNode.attribute("type").getValue().replace("::", ".");
					var classId = Utils.MakeVariableId(agentType);
					AgentMeta meta = AgentMeta.GetMeta(classId);

					if (meta == null) {
						meta = new AgentMeta();
						_agentMetas.put(classId, meta);
					}

					String agentSignature = bbNode.attribute("signature").getValue();
					if (agentSignature.equals(Long.toString(meta.GetSignature()))) {
						continue;
					}

					for (var propertiesNode : bbNode.elements()) {
						if (propertiesNode.getName().equals("properties") && propertiesNode.elements() != null) {
							for (var propertyNode : propertiesNode.elements()) {
								if (propertyNode.getName().equals("property")) {
									String memberStr = propertyNode.attribute("member").getValue();
									boolean bIsMember = (!Utils.IsNullOrEmpty(memberStr) && memberStr.equals("true"));

									if (!bIsMember) {
										String propName = propertyNode.attribute("name").getValue();
										String propType = propertyNode.attribute("type").getValue().replace("::", ".");
										String valueStr = propertyNode.attributeValue("defaultvalue", "");
										String isStatic = propertyNode.attribute("static").getValue();
										boolean bIsStatic = (!Utils.IsNullOrEmpty(isStatic) && isStatic.equals("true"));

										registerCustomizedProperty(meta, propName, propType, valueStr, bIsStatic);
									}
								}
							}
						}
					} // end of for propertiesNode
				}
			} // end of for bbNode

			return true;
		} catch (Exception e) {
			Debug.Check(false, e);
		}

		Debug.Check(false);
		return false;
	}

	static AgentMeta GetMeta(long agentClassId) {
		return _agentMetas.get(agentClassId);
	}

	private static IInstanceMember CreateInstanceProperty(String typeName, String instance, IInstanceMember indexMember,
			long propId) {
		typeName = GetTypeName(typeName);
		if (_Creators.containsKey(typeName)) {
			TypeCreator creator = _Creators.get(typeName);
			return creator.CreateInstanceProperty(instance, indexMember, propId);
		}

		Debug.Check(false);
		return null;
	}

	private static IInstanceMember CreateInstanceConst(String typeName, String valueStr) {
		typeName = GetTypeName(typeName);
		if (_Creators.containsKey(typeName)) {
			TypeCreator creator = _Creators.get(typeName);
			return creator.CreateInstanceConst(typeName, valueStr);
		}

		Debug.Check(false);
		return null;
	}

	public IProperty GetProperty(long propId) {
		IProperty r = _customizedStaticProperties.get(propId);
		if (r == null)
			r = _customizedProperties.get(propId);
		if (r == null)
			r = _memberProperties.get(propId);

		return r;
	}

	public IProperty GetMemberProperty(long propId) {
		return _memberProperties.get(propId);
	}

	public Map<Long, IProperty> GetMemberProperties() {
		return Collections.unmodifiableMap(_memberProperties);
	}

	public IMethod GetMethod(long methodId) {
		return _methods.get(methodId);
	}

	public Map<Long, IInstantiatedVariable> InstantiateCustomizedProperties() {
		Map<Long, IInstantiatedVariable> vars = new HashMap<Long, IInstantiatedVariable>();
		if (_customizedStaticVars == null)
			_customizedStaticVars = new HashMap<Long, IInstantiatedVariable>();

		// instance customzied properties
		for (var it : _customizedProperties.entrySet()) {
			vars.put(it.getKey(), it.getValue().Instantiate());
		}

		for (var it : _customizedStaticProperties.entrySet()) {
			vars.put(it.getKey(), it.getValue().Instantiate());
		}
		for (var it : _customizedStaticVars.entrySet()) {
			vars.put(it.getKey(), it.getValue());
		}
		return vars;
	}

	public void RegisterMemberProperty(long propId, IProperty property) {
		_memberProperties.put(propId, property);
	}

	public void RegisterStaticProperty(long propId, IProperty property) {
		_memberProperties.put(propId, property);
	}

	public void RegisterCustomizedProperty(long propId, ICustomizedProperty property) {
		_customizedProperties.put(propId, property);
	}

	public void RegisterStaticCustomizedProperty(long propId, ICustomizedProperty property) {
		_customizedStaticProperties.put(propId, property);
	}

	public void RegisterMethod(long methodId, IMethod method) {
		_methods.put(methodId, method);
	}

	public void RegisterMemberMethod(long methodId, IMethod method) {
		_methods.put(methodId, method);
	}

	public void RegisterStaticMethod(long methodId, IMethod method) {
		_methods.put(methodId, method);
	}

	public static boolean Register(String typeName, Class<?> clazz) {
		typeName = typeName.replace("::", ".");

		TypeCreator tc = TypeCreator.Create(clazz);
		_Creators.put(typeName, tc);
		_Creators.put(clazz.getName(), tc);

		String vectorTypeName1 = String.format("vector<%s>", typeName);
		String vectorTypeName2 = String.format("vector<%s>", clazz.getName());
		TypeCreator tcl = TypeCreator.CreateList(clazz);
		_Creators.put(vectorTypeName1, tcl);
		_Creators.put(vectorTypeName2, tcl);

		_typesRegistered.put(typeName, clazz);
		_typesRegistered.put(vectorTypeName1, ArrayList.class);
		_typesRegistered.put(vectorTypeName2, ArrayList.class);

		return true;
	}

	public static void UnRegister(String typeName) {
		typeName = typeName.replace("::", ".");
		String vectorTypeName = String.format("vector<%s>", typeName);

		_typesRegistered.remove(typeName);
		_typesRegistered.remove(vectorTypeName);

		_Creators.remove(typeName);
		_Creators.remove(vectorTypeName);
	}

	private static void RegisterBasicTypes() {

		Register("bool", Boolean.class);
		Register("Boolean", Boolean.class);
		Register("byte", Byte.class);
		Register("ubyte", Byte.class);
		Register("Byte", Byte.class);
		Register("char", Byte.class);
		Register("Char", Byte.class);
		Register("decimal", BigDecimal.class);
		Register("Decimal", BigDecimal.class);
		Register("double", Double.class);
		Register("Double", Double.class);
		Register("float", Float.class);
		Register("int", Integer.class);
		Register("Int16", Short.class);
		Register("Int32", Integer.class);
		Register("Int64", Long.class);
		Register("long", Long.class);
		Register("llong", Long.class);

		Register("sbyte", Byte.class);
		Register("SByte", Byte.class);
		Register("short", Short.class);
		Register("ushort", Short.class);

		Register("uint", Integer.class);
		Register("UInt16", Integer.class);
		Register("UInt32", Integer.class);
		Register("UInt64", Long.class);
		Register("ulong", Long.class);
		Register("ullong", Long.class);
		Register("Single", Float.class);
		Register("string", String.class);
		Register("String", String.class);
		Register("object", Object.class);
		Register("behaviac.Agent", Agent.class);
		Register("behaviac.EBTStatus", EBTStatus.class);

	}

	private static void UnRegisterBasicTypes() {
		UnRegister("bool");
		UnRegister("Boolean");
		UnRegister("byte");
		UnRegister("ubyte");
		UnRegister("Byte");
		UnRegister("char");
		UnRegister("Char");
		UnRegister("decimal");
		UnRegister("Decimal");
		UnRegister("double");
		UnRegister("Double");
		UnRegister("float");
		UnRegister("Single");
		UnRegister("int");
		UnRegister("Int16");
		UnRegister("Int32");
		UnRegister("Int64");
		UnRegister("long");
		UnRegister("llong");
		UnRegister("sbyte");
		UnRegister("SByte");
		UnRegister("short");
		UnRegister("ushort");

		UnRegister("uint");
		UnRegister("UInt16");
		UnRegister("UInt32");
		UnRegister("UInt64");
		UnRegister("ulong");
		UnRegister("ullong");

		UnRegister("string");
		UnRegister("String");
		UnRegister("object");
		UnRegister("behaviac.Agent");
		UnRegister("behaviac.EBTStatus");
	}

	public static String GetTypeName(String typeName) {
		typeName = typeName.replace("*", "");
		// typeName = typeName.Replace("&lt;", "<");
		// typeName = typeName.Replace("&gt;", ">");
		return typeName;
	}

	public static Object ParseTypeValue(String typeName, String valueStr) {
		var info = Utils.GetTypeFromName2(typeName);
		Debug.Check(info != null);

		if (info.isList() || info.getElemClass() == String.class) {
			if (!Utils.IsNullOrEmpty(valueStr)) {
				return Utils.ConvertFromString(info.getElemClass(), info.isList(), valueStr);
			} else if (info.getElemClass() == String.class) {
				return "";
			}
		}
		return null;
	}

	public static String convertAgentName(String name) {
		name = name.replace("::", ".");
		if (name.equals("behaviac.Agent"))
			name = "org.gof.behaviac.Agent";
		return name;

	}
}
