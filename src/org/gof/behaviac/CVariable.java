package org.gof.behaviac;

import java.util.List;

public class CVariable implements IInstantiatedVariable {
	Object _value;
	String _name;
	ClassInfo _clazz;

	public CVariable(String name, Object value, ClassInfo clazz) {
		_name = name;
		_value = Utils.Clone(clazz.getElemClass(), clazz.isList(), value);
		_clazz = clazz;
	}

	public CVariable(String name, String valueStr, ClassInfo clazz) {
		_name = name;
		_clazz = clazz;
		_value = Utils.ConvertFromString(clazz.getElemClass(), clazz.isList(), valueStr);
		_name = name;
	}

	public Object GetValue(Agent self) {
		return _value;
	}

	@Override
	public Object GetValueObject(Agent self) {
		// TODO Auto-generated method stub
		return _value;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object GetValueObject(Agent self, int index) {
		if (_value instanceof List) {
			return ((List) _value).get(index);
		}
		return _value;
	}

	@Override
	public void SetValueFromString(Agent self, String valueStr) {
		_value = Utils.ConvertFromString(_clazz.getElemClass(), _clazz.isList(), valueStr);
	}

	@Override
	public void SetValue(Agent self, Object value) {
		_value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void SetValue(Agent self, Object value, int index) {
		if (_value instanceof List) {
			((List) _value).set(index, value);
			return;
		}
		Debug.Check(false);
	}

	@Override
	public String GetName() {
		return _name;
	}

	@Override
	public void Log(Agent self) {
	}

	@Override
	public IInstantiatedVariable Clone() {
		CVariable p = new CVariable(this._name, this._value, this._clazz);

		return p;
	}

	@Override
	public void CopyTo(Agent pAgent) {
		Debug.Check(false);
	}

}
