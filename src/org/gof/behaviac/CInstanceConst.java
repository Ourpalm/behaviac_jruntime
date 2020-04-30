package org.gof.behaviac;

public class CInstanceConst extends CInstanceMember {
	protected Object _value;

	public CInstanceConst(ClassInfo clazz, String typeName, String valueStr) {
		super(clazz);
		this._value = Utils.ConvertFromString(clazz.getElemClass(), clazz.isList(), valueStr);
	}

	@Override
	public Object GetValueObject(Agent self) {
		return _value;
	}

	@Override
	public void SetValue(Agent self, Object value) {
		_value = value;
	}
}
