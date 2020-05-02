package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Proc1;

public class CAgentMethodVoid0 extends CAgentMethodVoidBase {
	Proc1<Agent> _fp;

	public CAgentMethodVoid0(Proc1<Agent> f) {
		_fp = f;
	}

	public CAgentMethodVoid0(CAgentMethodVoid0 rhs) {
		super(rhs);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethodVoid0(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 0);

		_instance = instance;
	}

	@Override
	public void Run(Agent self) {
		_fp.run(self);
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
	}
}
