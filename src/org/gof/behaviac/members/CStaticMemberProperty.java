package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;

public class CStaticMemberProperty extends CProperty {
	@FunctionalInterface
	public static interface StaticPropertyGetter {
		Object get();
	}

	@FunctionalInterface
	public static interface StaticPropertySetter {
		void set(Object value);
	}

	StaticPropertyGetter _getter;
	StaticPropertySetter _setter;
	public CStaticMemberProperty(String name, ClassInfo clazz, StaticPropertyGetter getter,
			StaticPropertySetter setter) {
		super(name, clazz);
		_getter = getter;
		_setter = setter;
	}

	@Override
	public Object GetValue(Agent self) {
		return _getter.get();
	}
	
	@Override
	public void SetValue(Agent self, Object value) {
		_setter.set(value);
	}
}
