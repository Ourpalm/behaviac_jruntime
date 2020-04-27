package org.gof.behaviac;

public class CProperty implements IProperty {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetValue(Agent self, IInstanceMember right) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetValueFromString(Agent self, String valueStr) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object GetValueObject(Agent self) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object GetValueObject(Agent self, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInstanceMember CreateInstance(String instance, IInstanceMember indexMember) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue CreateIValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void SetValue(Agent self, Object value, int index) {
		Debug.Check(false);
	}

	public void SetValue(Agent self, Object value) {
		Debug.Check(false);
	}
}
