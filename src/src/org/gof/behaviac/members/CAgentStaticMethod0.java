package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Func0;

public class CAgentStaticMethod0<R> extends CAgentMethodBase {
	Func0<R> _fp;

	public CAgentStaticMethod0(Func0<R> f, ClassInfo rclazz, ClassInfo[] pclazzs) {
		super(rclazz, pclazzs);
		_fp = f;
	}

	public CAgentStaticMethod0(CAgentStaticMethod0<R> rhs) {
		super(rhs);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentStaticMethod0<R>(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 0);
		_instance = instance;
	}

	@Override
	public void Run(Agent self) {
		_returnValue.value = _fp.run();
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
	}
}
