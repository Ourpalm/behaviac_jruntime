package org.gof.behaviac;

public interface IInstanceMember {
	int GetCount(Agent self);

	void SetValue(Agent self, IInstanceMember right, int index);

	Object GetValueObject(Agent self);

	void SetValue(Agent self, Object value);

	void SetValue(Agent self, IInstanceMember right);

	void SetValueAs(Agent self, IInstanceMember right);

	boolean Compare(Agent self, IInstanceMember right, EOperatorType comparisonType);

	void Compute(Agent self, IInstanceMember right1, IInstanceMember right2, EOperatorType computeType);

	void Run(Agent self);
}
