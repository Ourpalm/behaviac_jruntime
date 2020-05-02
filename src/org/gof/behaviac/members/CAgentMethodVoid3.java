package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.utils.Proc4;

public class CAgentMethodVoid3<P1, P2, P3> extends CAgentMethodVoidBase {
	Proc4<Agent, P1, P2, P3> _fp;
	IInstanceMember _p1;
	IInstanceMember _p2;
	IInstanceMember _p3;

	public CAgentMethodVoid3(Proc4<Agent, P1, P2, P3> f) {
		_fp = f;
	}

	public CAgentMethodVoid3(CAgentMethodVoid3<P1, P2, P3> rhs) {
		super(rhs);
		_fp = rhs._fp;
		_p1 = rhs._p1;
		_p2 = rhs._p2;
		_p3 = rhs._p3;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethodVoid3<P1, P2, P3>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 3);
		_instance = instance;
		_p1 = AgentMeta.ParseProperty(paramStrs[0]);
		_p2 = AgentMeta.ParseProperty(paramStrs[1]);
		_p3 = AgentMeta.ParseProperty(paramStrs[2]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Run(Agent self) {
		_fp.run(self, (P1) _p1.GetValueObject(self), (P2) _p2.GetValueObject(self), (P3) _p3.GetValueObject(self));
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		String paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 0);
		treeTask.SetVariable(paramName, _p1.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 1);
		treeTask.SetVariable(paramName, _p2.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 2);
		treeTask.SetVariable(paramName, _p3.GetValueObject(self));
	}
}
