package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;

public class CStaticArrayItemProperty extends CProperty {
	@FunctionalInterface
	public static interface PropertyGetter {
		Object get(int index);
	}

	@FunctionalInterface
	public static interface PropertySetter {
		void set(Object value, int index);
	}

	PropertyGetter _getter;
	PropertySetter _setter;

	public CStaticArrayItemProperty(String name, ClassInfo clazz, PropertyGetter getter, PropertySetter setter) {
		super(name, clazz);
		_getter = getter;
		_setter = setter;
	}

	@Override
	public Object GetValue(Agent self, int index) {
		return _getter.get(index);
	}

	@Override
	public void SetValue(Agent self, Object value, int index) {
		_setter.set(value, index);
	}
}
