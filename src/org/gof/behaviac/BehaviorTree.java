package org.gof.behaviac;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class BehaviorTree extends BehaviorNode {
	private final int SupportedVersion = 5;
	private String m_name = "";
	private boolean m_bIsFSM = false;
	private Map<Long, ICustomizedProperty> m_localProps;

	public Map<Long, ICustomizedProperty> getLocalProps() {
		return this.m_localProps;
	}

	public void AddPar(String agentType, String typeName, String name, String valueStr) {
		this.AddLocal(agentType, typeName, name, valueStr);
	}

	public void AddLocal(String agentType, String typeName, String name, String valueStr) {
		if (this.m_localProps == null) {
			this.m_localProps = new HashMap<>();
		}

		var varId = Utils.MakeVariableId(name);
		ICustomizedProperty prop = AgentMeta.CreateProperty(typeName, varId, name, valueStr);
		this.m_localProps.put(varId, prop);

		var type = Utils.GetElementTypeFromName(typeName);

		if (type != null) {
			prop = AgentMeta.CreateArrayItemProperty(typeName, varId, name);
			varId = Utils.MakeVariableId(name + "[]");
			this.m_localProps.put(varId, prop);
		}
	}

	public void InstantiatePars(Map<Long, IInstantiatedVariable> vars) {
		if (this.m_localProps != null) {
			for (var it : m_localProps.entrySet()) {
				vars.put(it.getKey(), it.getValue().Instantiate());
			}
		}
	}

	public void UnInstantiatePars(Map<Long, IInstantiatedVariable> vars) {
		if (this.m_localProps != null) {
			for (var it : m_localProps.entrySet()) {
				vars.remove(it.getKey());
			}
		}
	}

	@Override
	protected void load_local(int version, String agentType, Element node) {
		if (node.getName() != "par") {
			Debug.Check(false);
			return;
		}
		var name = node.attribute("name").getValue();
		var type = node.attribute("type").getValue().replace("::", ".");
		var value = node.attribute("value").getValue();

		this.AddLocal(agentType, type, name, value);
	}

	public boolean load_xml(byte[] pBuffer) {
		try {
			Debug.Check(pBuffer != null);
			var xml = new String(pBuffer, StandardCharsets.UTF_8);
			var doc = DocumentHelper.parseText(xml);
			var behaviorNode = doc.getRootElement();

			m_name = behaviorNode.attributeValue("name");
			var agentType = behaviorNode.attributeValue("agenttype");
			var fsm = (behaviorNode.attributeValue("fsm") != null) ? behaviorNode.attributeValue("fsm") : null;
			var versionStr = behaviorNode.attributeValue("version");

			var version = Integer.parseInt(versionStr);
			if (version != SupportedVersion) {
				Debug.LogError(String.format(
						"'%s' Version(%d), while Version(%d) is supported, please update runtime or rexport data using the latest designer",
						this.m_name, version, SupportedVersion));
			}
			this.setClassName("BehaviorTree");
			this.setId(-1);
			if (fsm != null && fsm == "true") {
				this.m_bIsFSM = true;
			}
			this.load_properties_pars_attachments_children(true, version, agentType, behaviorNode);

		} catch (Exception e) {
			Debug.Check(false, e.getMessage());
			return false;
		}
		return true;
	}

	public String GetName() {
		return this.m_name;
	}

	public void SetName(String name) {
		this.m_name = name;
	}

	public boolean isFSM() {
		return this.m_bIsFSM;
	}

	public void setFSM(boolean value) {
		this.m_bIsFSM = value;
	}

	@Override
	protected org.gof.behaviac.BehaviorTask createTask() {
		BehaviorTreeTask pTask = new BehaviorTreeTask();
		return pTask;
	}
}
