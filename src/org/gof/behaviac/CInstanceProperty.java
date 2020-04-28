package org.gof.behaviac;

public class CInstanceProperty extends CInstanceMember {
	CProperty _property;

	public CInstanceProperty(String instance, IInstanceMember indexMember, CProperty prop) {
		super(instance, indexMember);
		_property = prop;
	}

	@Override
	public Object GetValueObject(Agent self) {
//		Agent agent = Utils.GetParentAgent(self, _instance);
//
//        if (_indexMember != null)
//        {
//            int indexValue = ((CInstanceMember<int>)_indexMember).GetValue(self);
//            return _property.GetValue(agent, indexValue);
//        }
//
//        return _property.GetValue(agent);
		Debug.Check(false, "cbh TODO");
		return null;
	}

	@Override
	public void SetValue(Agent self, Object right) {
//		Agent agent = Utils.GetParentAgent(self, _instance);
//
//        if (_indexMember != null)
//        {
//            int indexValue = ((CInstanceMember<int>)_indexMember).GetValue(self);
//            _property.SetValue(agent, value, indexValue);
//        }
//        else
//        {
//            _property.SetValue(agent, value);
//        }
		Debug.Check(false, "cbh TODO");
	}

}
