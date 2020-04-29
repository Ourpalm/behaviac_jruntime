package org.gof.behaviac;

public class TValue implements IValue {

	public Object value;
	private ClassInfo _clazz;

	public TValue(ClassInfo clazz, Object obj) {
		value = Utils.Clone(clazz.getElemClass(), clazz.isList(), obj);
		_clazz = clazz;
	}

	public TValue(TValue rhs) {
		_clazz = rhs._clazz;
		value = Utils.Clone(_clazz.getElemClass(), _clazz.isList(), rhs.value);
	}

	@Override
	public void Log(Agent agent, String name, boolean bForce) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	public <T> T getT() {
		return (T) value;
	}

}
