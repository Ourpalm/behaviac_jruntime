package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.htn.Task;
import org.gof.behaviac.utils.Func1;

public class CAgentStaticMethod1<R, P1> extends CAgentMethodBase {
	Func1<R, P1> _fp;
	IInstanceMember _p1;

	public CAgentStaticMethod1(Func1<R, P1> f, ClassInfo rclazz, ClassInfo[] pclazzs) {
		super(rclazz, pclazzs);
		_fp = f;
	}

	public CAgentStaticMethod1(CAgentStaticMethod1<R, P1> rhs) {
		super(rhs);
		_fp = rhs._fp;
		_p1 = rhs._p1;
	}

	@Override
	public IMethod Clone() {
		return new CAgentStaticMethod1<R, P1>(this);
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
		_returnValue.value = _fp.run((P1) _p1.GetValueObject(self));
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		String paramName = String.format("%s%d", Task.LOCAL_TASK_PARAM_PRE, 0);
		treeTask.SetVariable(paramName, _p1.GetValueObject(self));
	}
}
