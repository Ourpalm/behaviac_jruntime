package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.utils.Proc8;

public class CAgentMethodVoid7<P1, P2, P3, P4, P5, P6, P7> extends CAgentMethodVoidBase {
	Proc8<Agent, P1, P2, P3, P4, P5, P6, P7> _fp;
	IInstanceMember _p1;
	IInstanceMember _p2;
	IInstanceMember _p3;
	IInstanceMember _p4;
	IInstanceMember _p5;
	IInstanceMember _p6;
	IInstanceMember _p7;

	public CAgentMethodVoid7(Proc8<Agent, P1, P2, P3, P4, P5, P6, P7> f, ClassInfo[] pclazzs) {
		super(pclazzs);_fp = f;
	}

	public CAgentMethodVoid7(CAgentMethodVoid7<P1, P2, P3, P4, P5, P6, P7> rhs) {
		super(rhs);
		_fp = rhs._fp;
		_p1 = rhs._p1;
		_p2 = rhs._p2;
		_p3 = rhs._p3;
		_p4 = rhs._p4;
		_p5 = rhs._p5;
		_p6 = rhs._p6;
		_p7 = rhs._p7;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethodVoid7<P1, P2, P3, P4, P5, P6, P7>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 7);
		_instance = instance;
		_p1 = AgentMeta.ParseProperty(paramStrs[0], _pclazzs[0]);
		_p2 = AgentMeta.ParseProperty(paramStrs[1], _pclazzs[1]);
		_p3 = AgentMeta.ParseProperty(paramStrs[2], _pclazzs[2]);
		_p4 = AgentMeta.ParseProperty(paramStrs[3], _pclazzs[3]);
		_p5 = AgentMeta.ParseProperty(paramStrs[4], _pclazzs[4]);
		_p6 = AgentMeta.ParseProperty(paramStrs[5], _pclazzs[5]);
		_p7 = AgentMeta.ParseProperty(paramStrs[6], _pclazzs[6]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Run(Agent self) {
		_fp.run(self, (P1) _p1.GetValueObject(self), (P2) _p2.GetValueObject(self), (P3) _p3.GetValueObject(self),
				(P4) _p4.GetValueObject(self), (P5) _p5.GetValueObject(self), (P6) _p6.GetValueObject(self),
				(P7) _p7.GetValueObject(self));
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		String paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 0);
		treeTask.SetVariable(paramName, _p1.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 1);
		treeTask.SetVariable(paramName, _p2.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 2);
		treeTask.SetVariable(paramName, _p3.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 3);
		treeTask.SetVariable(paramName, _p4.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 4);
		treeTask.SetVariable(paramName, _p5.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 5);
		treeTask.SetVariable(paramName, _p6.GetValueObject(self));

		paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 6);
		treeTask.SetVariable(paramName, _p7.GetValueObject(self));
	}
}
