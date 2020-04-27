package org.gof.behaviac;

public interface IMethod extends IInstanceMember {
	IMethod Clone();

	void Load(String instance, String[] paramStrs);

	IValue GetIValue(Agent self);

	IValue GetIValue(Agent self, IInstanceMember firstParam);

	void SetTaskParams(Agent self, BehaviorTreeTask treeTask);
}
