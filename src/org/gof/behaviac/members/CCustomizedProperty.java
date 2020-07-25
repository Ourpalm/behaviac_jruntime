package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Utils;

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
		if (self != null) {
			return self.GetVarValue(_id);
		}
		return _defaultValue;
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
