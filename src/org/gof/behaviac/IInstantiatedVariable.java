package org.gof.behaviac;

public interface IInstantiatedVariable {
	Object GetValueObject(Agent self);

	Object GetValueObject(Agent self, int index);

	void SetValueFromString(Agent self, String valueStr);

	void SetValue(Agent self, Object value);

	void SetValue(Agent self, Object value, int index);

	String getName();

	void Log(Agent self);

	IInstantiatedVariable clone();

	void CopyTo(Agent pAgent);
}
