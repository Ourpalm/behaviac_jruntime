package org.gof.behaviac;

public class TypeCreator {
	@FunctionalInterface
	public static interface PropertyCreator {
		ICustomizedProperty run(long propId, String propName, String valueStr);
	}

	@FunctionalInterface
	public static interface ArrayItemPropertyCreator {
		ICustomizedProperty run(long parentId, String parentName);
	}

	@FunctionalInterface
	public static interface InstancePropertyCreator {
		IInstanceMember run(String instance, IInstanceMember indexMember, long id);
	}

	@FunctionalInterface
	public static interface InstanceConstCreator {
		IInstanceMember run(String typeName, String valueStr);
	}

	@FunctionalInterface
	public static interface CustomizedPropertyCreator {
		ICustomizedProperty run(long id, String name, String valueStr);
	}

	@FunctionalInterface
	public static interface CustomizedArrayItemPropertyCreator {
		ICustomizedProperty run(long id, String name);
	}

	protected PropertyCreator _propertyCreator;
	protected ArrayItemPropertyCreator _arrayItemPropertyCreator;
	protected InstancePropertyCreator _instancePropertyCreator;
	protected InstanceConstCreator _instanceConstCreator;
	protected CustomizedPropertyCreator _customizedPropertyCreator;
	protected CustomizedArrayItemPropertyCreator _customizedArrayItemPropertyCreator;

	public TypeCreator() {

	}

	public TypeCreator(PropertyCreator propCreator, ArrayItemPropertyCreator arrayItemPropCreator,
			InstancePropertyCreator instancePropertyCreator, InstanceConstCreator instanceConstCreator,
			CustomizedPropertyCreator customizedPropertyCreator,
			CustomizedArrayItemPropertyCreator customizedArrayItemPropertyCreator) {
		_propertyCreator = propCreator;
		_arrayItemPropertyCreator = arrayItemPropCreator;
		_instancePropertyCreator = instancePropertyCreator;
		_instanceConstCreator = instanceConstCreator;
		_customizedPropertyCreator = customizedPropertyCreator;
		_customizedArrayItemPropertyCreator = customizedArrayItemPropertyCreator;
	}

	public ICustomizedProperty CreateProperty(long propId, String propName, String valueStr) {
		return _propertyCreator.run(propId, propName, valueStr);
	}

	public ICustomizedProperty CreateArrayItemProperty(long parentId, String parentName) {
		return _arrayItemPropertyCreator.run(parentId, parentName);
	}

	public IInstanceMember CreateInstanceProperty(String instance, IInstanceMember indexMember, long id) {
		return _instancePropertyCreator.run(instance, indexMember, id);
	}

	public IInstanceMember CreateInstanceConst(String typeName, String valueStr) {
		return _instanceConstCreator.run(typeName, valueStr);
	}

	public ICustomizedProperty CreateCustomizedProperty(long id, String name, String valueStr) {
		return _customizedPropertyCreator.run(id, name, valueStr);
	}

	public ICustomizedProperty CreateCustomizedArrayItemProperty(long id, String name) {
		return _customizedArrayItemPropertyCreator.run(id, name);
	}

	public static TypeCreator Create(Class<?> clazz) {
		return new CommonTypeCreator(new ClassInfo(clazz));
	}

	public static TypeCreator CreateList(Class<?> clazz) {
		return new CommonTypeCreator(new ClassInfo(true, clazz));
	}
}
