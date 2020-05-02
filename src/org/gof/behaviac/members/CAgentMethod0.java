package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Func1;

public class CAgentMethod0<R> extends CAgentMethodBase {
	Func1<R, Agent> _fp;

	public CAgentMethod0(Func1<R, Agent> f, ClassInfo rclazz) {
		super(rclazz);
		_fp = f;
	}

	public CAgentMethod0(CAgentMethod0<R> rhs) {
		super(rhs._returnValue._clazz);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethod0<R>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 0);
		_instance = instance;
	}

	@Override
	public void Run(Agent self) {

		_returnValue.value = _fp.run(self);
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
	}
}
