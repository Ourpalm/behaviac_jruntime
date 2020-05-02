package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.TValue;
import org.gof.behaviac.utils.Utils;

public class CAgentMethodBase extends CInstanceMember implements IMethod {

	protected TValue _returnValue;

	protected CAgentMethodBase(ClassInfo rclazz)
    {
		super(rclazz);
        _returnValue = new TValue(rclazz, Utils.GetDefaultValue2(rclazz.getElemClass(), rclazz.isList()));
    }

	@Override
	public IMethod Clone() {
		Debug.Check(false);
		return null;
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(false);
	}

	@Override
	public IValue GetIValue(Agent self) {
		if (self != null) {
			Run(self);
		}
		return _returnValue;
	}

	@Override
	public IValue GetIValue(Agent self, IInstanceMember firstParam) {
		Agent agent = Utils.GetParentAgent(self, _instance);
		firstParam.Run(agent);

		return GetIValue(self);
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		Debug.Check(false);
	}

}
