package org.gof.behaviac;

import java.util.List;

public class CVariable<T> implements IInstantiatedVariable {
	T _value;
	String _name;
	Class<?> _clazz;

	@SuppressWarnings("unchecked")
	public CVariable(String name, T value, Class<?> clazz) {
		_name = name;
		_value = (T) Utils.Clone(value);
		_clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public CVariable(String name, String valueStr, Class<?> clazz) {
		_name = name;
		_clazz = clazz;
		_value = (T) Utils.ConvertFromString(clazz, valueStr);
		_name = name;
	}

	public T GetValue(Agent self) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void SetValueFromString(Agent self, String valueStr) {
		_value = (T) Utils.ConvertFromString(_clazz, valueStr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void SetValue(Agent self, Object value) {
		_value = (T) value;
	}

	@Override
	public void SetValue(Agent self, Object value, int index) {
		Debug.Check(false);
	}

	@Override
	public String GetName() {
		// TODO Auto-generated method stub
		return _name;
	}

	@Override
	public void Log(Agent self) {
		// TODO Auto-generated method stub

	}

	@Override
	public IInstantiatedVariable Clone() {
		CVariable<T> p = new CVariable<T>(this._name, this._value, this._clazz);

		return p;
	}

	@Override
	public void CopyTo(Agent pAgent) {
		Debug.Check(false);
	}

}
