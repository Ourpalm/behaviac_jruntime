package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EOperatorType;

public class CAgentMethodVoidBase implements IMethod {
	protected String _instance = "Self";
	protected ClassInfo[] _pclazzs;

	public CAgentMethodVoidBase(ClassInfo[] pclazzs) {
		_pclazzs = pclazzs;
	}

	public CAgentMethodVoidBase(CAgentMethodVoidBase rhs) {
		_instance = rhs._instance;
		_pclazzs = rhs._pclazzs;
	}

	@Override
	public int GetCount(Agent self) {
		Debug.Check(false);
		return 0;
	}

	@Override
	public void SetValue(Agent self, IInstanceMember right, int index) {
		Debug.Check(false);
	}

	@Override
	public Object GetValueObject(Agent self) {
		Debug.Check(false);
		return null;
	}

	@Override
	public void SetValue(Agent self, Object value) {
		Debug.Check(false);
	}

	@Override
	public void SetValue(Agent self, IInstanceMember right) {
		Debug.Check(false);
	}

	@Override
	public void SetValueAs(Agent self, IInstanceMember right) {
		Debug.Check(false);
	}

	@Override
	public boolean Compare(Agent self, IInstanceMember right, EOperatorType comparisonType) {
		Debug.Check(false);
		return false;
	}

	@Override
	public void Compute(Agent self, IInstanceMember right1, IInstanceMember right2, EOperatorType computeType) {
		Debug.Check(false);
	}

	@Override
	public void Run(Agent self) {
		// TODO Auto-generated method stub

	}

	@Override
	public IMethod Clone() {
		Debug.Check(false);
		return null;
	}

	@Override
	public void Load(String instance, String[] paramStrs) {
		Debug.Check(false);
		_instance = instance;
	}

	@Override
	public IValue GetIValue(Agent self) {
		Debug.Check(false);
		return null;
	}

	@Override
	public IValue GetIValue(Agent self, IInstanceMember firstParam) {
		return GetIValue(self);
	}

	@Override
	public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
		Debug.Check(false);
	}

}
