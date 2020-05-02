package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;

public class CMemberProperty extends CProperty {
	@FunctionalInterface
	public static interface Getter {
		Object get(Agent self);
	}

	@FunctionalInterface
	public static interface Setter {
		void set(Agent self, Object value);
	}

	Getter _getter;
	Setter _setter;

	public CMemberProperty(String name, ClassInfo clazz, Getter getter, Setter setter) {
		super(name, clazz);
		_getter = getter;
		_setter = setter;
	}

	@Override
	public Object GetValue(Agent self) {
		return _getter.get(self);
	}

	@Override
	public void SetValue(Agent self, Object value) {
		_setter.set(self, value);
	}
}
