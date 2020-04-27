package org.gof.behaviac;

public interface IInstantiatedVariable {
	Object GetValueObject(Agent self);

	Object GetValueObject(Agent self, int index);

	void SetValueFromString(Agent self, String valueStr);

	void SetValue(Agent self, Object value);

	void SetValue(Agent self, Object value, int index);

	String GetName();

	void Log(Agent self);

	IInstantiatedVariable Clone();

	void CopyTo(Agent pAgent);
}
