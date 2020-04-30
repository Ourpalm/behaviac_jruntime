package org.gof.behaviac;

import java.util.HashMap;
import java.util.Map;

import org.gof.behaviac.utils.Utils;

public class Variables {
	protected Map<Long, IInstantiatedVariable> m_variables;

	public Variables(Map<Long, IInstantiatedVariable> vars) {
		this.m_variables = vars;
	}

	public Variables() {
		this.m_variables = new HashMap<Long, IInstantiatedVariable>();
	}

	public boolean IsExisting(long varId) {
		return this.m_variables.containsKey(varId);
	}

	public IInstantiatedVariable GetVariable(long varId) {
		if (this.m_variables != null) {
			return this.m_variables.get(varId);
		}
		return null;
	}

	public void Log(Agent agent) {
	}

	public void UnLoad(String variableName) {
		Debug.Check(!Utils.IsNullOrEmpty(variableName));

		var varId = Utils.MakeVariableId(variableName);

		this.m_variables.remove(varId);
	}

	public void Unload() {
	}

	public void CopyTo(Agent pAgent, Variables target) {
		target.m_variables.clear();

		for (var it : m_variables.entrySet()) {
			target.m_variables.put(it.getKey(), it.getValue().Clone());
			if (pAgent != null) {
				it.getValue().CopyTo(pAgent);
			}
		}
	}

	public Map<Long, IInstantiatedVariable> GetVars() {
		return this.m_variables;
	}
}
