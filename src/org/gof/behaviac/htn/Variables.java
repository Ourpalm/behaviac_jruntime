package org.gof.behaviac.htn;

import java.util.HashMap;
import java.util.Map;

import org.gof.behaviac.Agent;
import org.gof.behaviac.Debug;
import org.gof.behaviac.IInstantiatedVariable;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.Utils;

@RegisterableNode
public class Variables {
	public Variables(Map<Long, IInstantiatedVariable> vars) {
		this.m_variables = vars;
	}

	public Variables() {
		this.m_variables = new HashMap<Long, IInstantiatedVariable>();
	}

	public boolean IsExisting(Long varId) {
		return this.m_variables.containsKey(varId);
	}

	public IInstantiatedVariable GetVariable(long varId) {
		if (this.m_variables != null && this.m_variables.containsKey(varId)) {
			return this.m_variables.get(varId);
		}

		return null;
	}

	public void AddVariable(Long varId, IInstantiatedVariable pVar, int stackIndex) {
		Debug.Check(!this.m_variables.containsKey(varId));

		this.m_variables.put(varId, pVar);
	}

	public void Log(Agent agent) {
	}

	public void UnLoad(String variableName) {
		Debug.Check(!Utils.IsNullOrEmpty(variableName));

		Long varId = Utils.MakeVariableId(variableName);

		if (this.m_variables.containsKey(varId)) {
			this.m_variables.remove(varId);
		}
	}

	public void Unload() {
	}

	public void CopyTo(Agent pAgent, Variables target) {
		target.m_variables.clear();

		for (var it : this.m_variables.entrySet()) {
			target.m_variables.put(it.getKey(), it.getValue().Clone());
		}

		if (pAgent != null) {
			for (var it : target.m_variables.entrySet()) {
				it.getValue().CopyTo(pAgent);
			}
		}
	}

	protected Map<Long, IInstantiatedVariable> m_variables = new HashMap<Long, IInstantiatedVariable>();

	public Map<Long, IInstantiatedVariable> GetVars() {
		return this.m_variables;
	}

}
