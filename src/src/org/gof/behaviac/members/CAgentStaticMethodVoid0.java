package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Proc0;

public class CAgentStaticMethodVoid0<AGENT> extends CAgentMethodVoidBase {
	Proc0 _fp;

	public CAgentStaticMethodVoid0(Proc0 f, ClassInfo[] pclazzs) {
		super(pclazzs);_fp = f;
	}

	public CAgentStaticMethodVoid0(CAgentStaticMethodVoid0<AGENT> rhs) {
		super(rhs);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentStaticMethodVoid0<AGENT>(this);
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
