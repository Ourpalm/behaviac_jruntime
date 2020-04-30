package org.gof.behaviac;

import org.gof.behaviac.utils.TValue;
import org.gof.behaviac.utils.Utils;

public class CProperty implements IProperty {

	String _name;
	ClassInfo _clazz;

	public CProperty(String name, ClassInfo clazz) {
		_name = name;
		_clazz = clazz;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return _name;
	}

	@Override
	public void SetValue(Agent self, IInstanceMember right) {
		Object rightValue = right.GetValueObject(self);
		SetValue(self, rightValue);
	}

	@Override
	public void SetValueFromString(Agent self, String valueStr) {
		var value = Utils.ConvertFromString(_clazz.getElemClass(), _clazz.isList(), valueStr);
		SetValue(self, value);
	}

	@Override
	public Object GetValueObject(Agent self) {
		return GetValue(self);
	}

	@Override
	public Object GetValueObject(Agent self, int index) {
		return GetValue(self, index);
	}

	@Override
	public IInstanceMember CreateInstance(String instance, IInstanceMember indexMember) {
		return new CInstanceProperty(_clazz, instance, indexMember, this);
	}

	@Override
	public IValue CreateIValue() {
		return new TValue(Utils.GetDefaultValue(this._clazz));
	}

	public void SetValue(Agent self, Object value, int index) {
		Debug.Check(false);
	}

	public void SetValue(Agent self, Object value) {
		Debug.Check(false);
	}

	public Object GetValue(Agent self) {
		Debug.Check(false);
		return Utils.GetDefaultValue(this._clazz);
	}

	public Object GetValue(Agent self, int index) {
		Debug.Check(false);
		return Utils.GetDefaultValue(this._clazz);
	}
}
