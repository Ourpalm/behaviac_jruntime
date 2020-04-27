package org.gof.behaviac;

public interface IProperty {
	String getName();

    void SetValue(Agent self, IInstanceMember right);

    void SetValueFromString(Agent self, String valueStr);

    Object GetValueObject(Agent self);

    Object GetValueObject(Agent self, int index);

    IInstanceMember CreateInstance(String instance, IInstanceMember indexMember);

    IValue CreateIValue();
}
