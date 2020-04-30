package org.gof.behaviac;

public class CCustomizedProperty extends CProperty implements ICustomizedProperty {
	long _id;
	Object _defaultValue;

	public CCustomizedProperty(ClassInfo clazz, long id, String name, String valueStr) {
		super(name, clazz);
		_id = id;
		_defaultValue = Utils.ConvertFromString(clazz.getElemClass(), clazz.isList(), valueStr);
	}

	
	@Override
	public Object GetValueObject(Agent self) {
		return self.GetVarValue(_id);
	}
	
	@Override
	public void SetValue(Agent self, Object value) {
		var bOk = self.SetVarValue(_id, value);
		Debug.Check(bOk);
	}

	@Override
	public IInstantiatedVariable Instantiate() {
		Object value = Utils.GetDefaultValue(this._clazz);
		return new CVariable(this._name, value, this._clazz);
	}

}
