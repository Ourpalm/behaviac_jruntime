package org.gof.behaviac;

public class TValue implements IValue {

	public Object value;

	public TValue(Object v) {
		value = Utils.Clone(v);
	}

	public TValue(TValue rhs) {
		value = Utils.Clone(rhs.value);
	}

	@Override
	public void Log(Agent agent, String name, boolean bForce) {
		// TODO Auto-generated method stub
	}
	
	@SuppressWarnings("unchecked")
	public <T> T  getT() {
		return (T)value;
	}

}
