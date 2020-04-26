package org.gof.behaviac.node;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.gof.behaviac.Debug;

public class BehaviorTree extends BehaviorNode {
	private final int SupportedVersion = 5;
	private String m_name = "";
	private boolean m_bIsFSM = false;

	public boolean load_xml(byte[] pBuffer) {
		try {
			Debug.check(pBuffer != null);
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
			Debug.check(false, e.getMessage());
			return false;
		}
		return true;
	}

	
	private void load(int version, String agentType, List<property_t> properties) {

	}

	private boolean load_attachment(int version, String agentType, boolean bHasEvents, Element c) {
		// TODO Auto-generated method stub
		return false;
	}
}
