package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.utils.Func2;

public class CAgentMethod1<R, P1> extends CAgentMethodBase {
	Func2<R, Agent, P1> _fp;
	IInstanceMember _p1;

	public CAgentMethod1(Func2<R, Agent, P1> f, ClassInfo rclazz) {
		super(rclazz);
		_fp = f;
	}

	public CAgentMethod1(CAgentMethod1<R, P1> rhs) {
		super(rhs._returnValue._clazz);
		_fp = rhs._fp;
		_p1 = rhs._p1;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethod1<R, P1>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 1);
		_instance = instance;
		_p1 = AgentMeta.ParseProperty(paramStrs[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Run(Agent self) {

		_returnValue.value = _fp.run(self, (P1) _p1.GetValueObject(self));
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		String paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 0);
		treeTask.SetVariable(paramName, _p1.GetValueObject(self));
	}
}
