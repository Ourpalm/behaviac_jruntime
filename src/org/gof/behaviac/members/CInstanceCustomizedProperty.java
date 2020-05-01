package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.utils.Utils;

public class CInstanceCustomizedProperty extends CInstanceMember {
	long _id;

	public CInstanceCustomizedProperty(ClassInfo clazz, String instance, IInstanceMember indexMember, long id) {
		super(clazz, instance, indexMember);
		_id = id;
	}

	@Override
	public Object GetValueObject(Agent self) {
		if (self != null) {
			Agent agent = Utils.GetParentAgent(self, _instance);

			if (_indexMember != null) {
				int indexValue = (int) _indexMember.GetValueObject(self);
				return agent.GetVariable(_id, indexValue);
			} else {
				return agent.GetVariable(_id);
			}
		}
		return Utils.GetDefaultValue(_clazz);
	}

	@Override
	public void SetValue(Agent self, Object value) {
		Agent agent = Utils.GetParentAgent(self, _instance);

		if (_indexMember != null) {
			int indexValue = (int) _indexMember.GetValueObject(self);
			agent.SetVariable("", _id, value, indexValue);
		} else {
			agent.SetVariable("", _id, value);
		}
	}
}
