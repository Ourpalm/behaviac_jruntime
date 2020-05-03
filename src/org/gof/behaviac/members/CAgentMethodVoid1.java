package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.utils.Proc2;

public class CAgentMethodVoid1<P1> extends CAgentMethodVoidBase {
	Proc2<Agent, P1> _fp;
	IInstanceMember _p1;

	public CAgentMethodVoid1(Proc2<Agent, P1> f, ClassInfo[] pclazzs) {
		super(pclazzs);
		_fp = f;
	}

	public CAgentMethodVoid1(CAgentMethodVoid1<P1> rhs) {
		super(rhs);
		_fp = rhs._fp;
		_p1 = rhs._p1;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethodVoid1<P1>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 1);
		_instance = instance;
		_p1 = AgentMeta.ParseProperty(paramStrs[0], _pclazzs[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Run(Agent self) {
		_fp.run(self, (P1) _p1.GetValueObject(self));
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		String paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 0);
		treeTask.SetVariable(paramName, _p1.GetValueObject(self));
	}
}
