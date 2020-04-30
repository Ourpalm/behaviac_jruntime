package org.gof.behaviac;

public class CInstanceProperty extends CInstanceMember {
	CProperty _property;

	public CInstanceProperty(ClassInfo clazz, String instance, IInstanceMember indexMember, CProperty prop) {
		super(clazz, instance, indexMember);
		_property = prop;
	}

	@Override
	public Object GetValueObject(Agent self) {
		Agent agent = Utils.GetParentAgent(self, _instance);

		if (_indexMember != null) {
			int indexValue = ((Number) _indexMember.GetValueObject(self)).intValue();
			return _property.GetValue(agent, indexValue);
		}

		return _property.GetValue(agent);
	}

	@Override
	public void SetValue(Agent self, Object value) {
		Agent agent = Utils.GetParentAgent(self, _instance);

		if (_indexMember != null) {
			int indexValue = ((Number) _indexMember.GetValueObject(self)).intValue();
			_property.SetValue(agent, value, indexValue);
		} else {
			_property.SetValue(agent, value);
		}
	}

}
