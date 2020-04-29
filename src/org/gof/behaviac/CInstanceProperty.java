package org.gof.behaviac;

public class CInstanceProperty extends CInstanceMember {
	CProperty _property;

	public CInstanceProperty(Class<?> clazz, String instance, IInstanceMember indexMember, CProperty prop) {
		super(instance, indexMember, clazz);
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
