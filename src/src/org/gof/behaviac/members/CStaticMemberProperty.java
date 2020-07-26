package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;

public class CStaticMemberProperty extends CProperty {
	@FunctionalInterface
	public static interface Getter {
		Object get();
	}

	@FunctionalInterface
	public static interface Setter {
		void set(Object value);
	}

	Getter _getter;
	Setter _setter;
	public CStaticMemberProperty(String name, ClassInfo clazz, Getter getter,
			Setter setter) {
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
