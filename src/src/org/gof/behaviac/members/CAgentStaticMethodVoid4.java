package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.members.CAgentMethodVoidBase;
import org.gof.behaviac.members.IInstanceMember;
import org.gof.behaviac.members.IMethod;
import org.gof.behaviac.utils.Proc4;

public class CAgentStaticMethodVoid4<P1, P2, P3, P4> extends CAgentMethodVoidBase {
	Proc4<P1, P2, P3, P4> _fp;
	IInstanceMember _p1;
	IInstanceMember _p2;
	IInstanceMember _p3;
	IInstanceMember _p4;

	public CAgentStaticMethodVoid4(Proc4<P1, P2, P3, P4> f, ClassInfo[] pclazzs) {
		super(pclazzs);_fp = f;
	}

	public CAgentStaticMethodVoid4(CAgentStaticMethodVoid4<P1, P2, P3, P4> rhs) {
		super(rhs);
		_fp = rhs._fp;
		_p1 = rhs._p1;
		_p2 = rhs._p2;
		_p3 = rhs._p3;
		_p4 = rhs._p4;
	}

	@Override
	public IMethod Clone() {
		return new CAgentStaticMethodVoid4<P1, P2, P3, P4>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 3);

		_instance = instance;
		_p1 = AgentMeta.ParseProperty(paramStrs[0], _pclazzs[0]);
		_p2 = AgentMeta.ParseProperty(paramStrs[1], _pclazzs[1]);
		_p3 = AgentMeta.ParseProperty(paramStrs[2], _pclazzs[2]);
		_p4 = AgentMeta.ParseProperty(paramStrs[3], _pclazzs[3]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Run(Agent self) {
		_fp.run((P1) _p1.GetValueObject(self), (P2) _p2.GetValueObject(self), (P3) _p3.GetValueObject(self),
				(P4) _p4.GetValueObject(self));
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
	}
}
