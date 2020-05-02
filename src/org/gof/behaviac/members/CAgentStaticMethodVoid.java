package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Proc0;

public class CAgentStaticMethodVoid extends CAgentMethodVoidBase {
	Proc0 _fp;

	public CAgentStaticMethodVoid(Proc0 f) {
		_fp = f;
	}

	public CAgentStaticMethodVoid(CAgentStaticMethodVoid rhs) {
		super(rhs);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentStaticMethodVoid(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 0);
		_instance = instance;
	}

	@Override
	public void Run(Agent self) {
		_fp.run();
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
	}
}
