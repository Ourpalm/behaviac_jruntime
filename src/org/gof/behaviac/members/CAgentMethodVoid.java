package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Proc1;
import org.gof.behaviac.utils.Utils;

public class CAgentMethodVoid extends CAgentMethodVoidBase {

	Proc1<Agent> _fp;

	public CAgentMethodVoid(Proc1<Agent> f) {
		_fp = f;
	}

	public CAgentMethodVoid(CAgentMethodVoid rhs) {
		super(rhs);
		_fp = rhs._fp;
	}

	@Override
	public IMethod Clone() {
		return new CAgentMethodVoid(this);
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(paramStrs.length == 0);
		_instance = instance;
	}

	@Override
	public void Run(Agent self) {
		Agent agent = Utils.GetParentAgent(self, _instance);
		_fp.run(agent);
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		super.SetTaskParams(self, treeTask);
	}
}
