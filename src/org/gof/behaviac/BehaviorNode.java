package org.gof.behaviac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public abstract class BehaviorNode {
	private int m_id;
	private String m_className = "";

	protected List<BehaviorNode> m_events;
	private List<Precondition> m_preconditions;
	private List<Effector> m_effectors;
	protected boolean m_loadAttachment = false;
	private byte m_enter_precond;
	private byte m_update_precond;
	private byte m_both_precond;
	private byte m_success_effectors;
	private byte m_failure_effectors;
	private byte m_both_effectors;

	protected BehaviorNode m_parent;
	protected List<BehaviorNode> m_children;
	protected BehaviorNode m_customCondition;
	protected boolean m_bHasEvents;

	public int getId() {
		return m_id;
	}

	public void setId(int m_id) {
		this.m_id = m_id;
	}

	public String getClassName() {
		return m_className;
	}

	public void setClassName(String m_className) {
		this.m_className = m_className;
	}

	public boolean HasEvents() {
		return this.m_bHasEvents;
	}

	public void setHasEvents(boolean hasEvents) {
		this.m_bHasEvents = hasEvents;
	}

	public void addChild(BehaviorNode pChild) {
		pChild.m_parent = this;
		if (this.m_children == null) {
			this.m_children = new ArrayList<BehaviorNode>();
		}
		this.m_children.add(pChild);
	}

	public BehaviorTask CreateAndInitTask() {
		BehaviorTask pTask = this.createTask();

		Debug.Check(pTask != null);
		pTask.Init(this);

		return pTask;
	}

	public int GetChildrenCount() {
		if (this.m_children != null) {
			return this.m_children.size();
		}

		return 0;
	}

	public BehaviorNode GetChild(int index) {
		if (this.m_children != null && index < this.m_children.size()) {
			return this.m_children.get(index);
		}

		return null;
	}

	public BehaviorNode getChildById(int nodeId) {
		if (this.m_children != null && this.m_children.size() > 0) {
			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorNode c = this.m_children.get(i);

				if (c.getId() == nodeId) {
					return c;
				}
			}
		}

		return null;
	}

	protected BehaviorNode() {
	}

	public void Clear() {
		if (this.m_events != null) {
			this.m_events.clear();
			this.m_events = null;
		}

		if (this.m_preconditions != null) {
			this.m_preconditions.clear();
			this.m_preconditions = null;
		}

		if (this.m_effectors != null) {
			this.m_effectors.clear();
			this.m_effectors = null;
		}

		if (this.m_children != null) {
			this.m_children.clear();
			this.m_children = null;
		}
	}

	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		return true;
	}

	public boolean IsManagingChildrenAsSubTrees() {
		return false;
	}

	protected static BehaviorNode Create(String className) {
		return Workspace.Instance.CreateBehaviorNode(className);
	}

	protected void load(int version, String agentType, List<property_t> properties) {
		var nodeType = this.getClassName().replace(".", "::");
		Workspace.Instance.OnBehaviorNodeLoaded(nodeType, properties);
	}

	protected void load_local(int version, String agentType, Element node) {
		Debug.Check(false);
	}

	protected void load_properties_pars_attachments_children(boolean bNode, int version, String agentType,
			Element node) {
		var bHasEvents = this.HasEvents();
		List<property_t> properties = new ArrayList<property_t>();

		for (var c : node.elements()) {
			if (bNode) {
				if (c.getName() == "attachment") {
					bHasEvents = this.load_attachment(version, agentType, bHasEvents, c);
				} else if (c.getName() == "custom") {
					Debug.Check(c.elements().size() == 1);
					var customNode = c.elements().get(0);
					BehaviorNode pChildNode = BehaviorNode.load(agentType, customNode, version);
					this.m_customCondition = pChildNode;
				} else if (c.getName() == "node") {
					BehaviorNode pChildNode = BehaviorNode.load(agentType, c, version);
					bHasEvents |= pChildNode.m_bHasEvents;

					this.addChild(pChildNode);
				}
			} else {
				if (c.getName() == "attachment") {
					bHasEvents = this.load_attachment(version, agentType, bHasEvents, c);
				}
			}
		}
		if (properties.size() > 0) {
			this.load(version, agentType, properties);
		}
		this.m_bHasEvents |= bHasEvents;
	}

	private void load_attachment_transition_effectors(int version, String agentType, boolean bHasEvents, Element c) {
		this.m_loadAttachment = true;

		this.load_properties_pars_attachments_children(false, version, agentType, c);

		this.m_loadAttachment = false;
	}

	private boolean load_attachment(int version, String agentType, boolean bHasEvents, Element c) {
		try {
			var pAttachClassName = c.attribute("class").getValue();

			if (pAttachClassName == null) {
				this.load_attachment_transition_effectors(version, agentType, bHasEvents, c);
				return true;
			}

			BehaviorNode pAttachment = BehaviorNode.Create(pAttachClassName);
			Debug.Check(pAttachment != null);

			if (pAttachment != null) {
				pAttachment.setClassName(pAttachClassName);
				var idStr = c.attribute("id").getValue();
				pAttachment.setId(Integer.parseInt(idStr));

				var bIsPrecondition = false;
				var bIsEffector = false;
				var bIsTransition = false;
				var flagStr = c.attribute("flag").getValue();

				if (flagStr == "precondition") {
					bIsPrecondition = true;
				} else if (flagStr == "effector") {
					bIsEffector = true;
				} else if (flagStr == "transition") {
					bIsTransition = true;
				}

				pAttachment.load_properties_pars_attachments_children(false, version, agentType, c);

				this.Attach(pAttachment, bIsPrecondition, bIsEffector, bIsTransition);

				bHasEvents |= (pAttachment instanceof Event);
			}

			return bHasEvents;
		} catch (Exception ex) {
			Debug.Check(false, ex.getMessage());
		}

		return bHasEvents;
	}

	private boolean load_property_pars(List<property_t> properties, Element c, int version, String agentType) {
		try {
			if (c.getName() == "property") {
				Debug.Check(c.attributeCount() == 1);

				for (var prop : c.attributes()) {
					property_t p = new property_t(prop.getName(), prop.getValue());
					properties.add(p);
					break;
				}

				return true;
			} else if (c.getName() == "pars") {
				if (c.elements() != null) {
					for (var parNode : c.elements()) {
						if (parNode.getName() == "par") {
							this.load_local(version, agentType, parNode);
						}
					}
				}

				return true;
			}
		} catch (Exception ex) {
			Debug.Check(false, ex.getMessage());
		}

		return false;
	}

	protected static BehaviorNode load(String agentType, Element node, int version) {
		Debug.Check(node.getName() == "node");
		var pClassName = node.attribute("class").getValue();
		BehaviorNode pNode = BehaviorNode.Create(pClassName);
		Debug.Check(pNode != null, "unsupported class " + pClassName);
		if (pNode != null) {
			pNode.setClassName(pClassName);
			var idStr = node.attribute("id").getValue();
			pNode.setId(Integer.parseInt(idStr));
			pNode.load_properties_pars_attachments_children(true, version, agentType, node);
		}

		return pNode;
	}

	public void Attach(BehaviorNode pAttachment, boolean bIsPrecondition, boolean bIsEffector) {
		this.Attach(pAttachment, bIsPrecondition, bIsEffector, false);
	}

	public void Attach(BehaviorNode pAttachment, boolean bIsPrecondition, boolean bIsEffector, boolean bIsTransition) {
		Debug.Check(bIsTransition == false);

		if (bIsPrecondition) {
			Debug.Check(!bIsEffector);

			if (this.m_preconditions == null) {
				this.m_preconditions = new ArrayList<Precondition>();
			}

			Precondition predicate = (Precondition) pAttachment;
			Debug.Check(predicate != null);
			this.m_preconditions.add(predicate);

			var phase = predicate.GetPhase();

			if (phase == Precondition.EPhase.E_ENTER) {
				this.m_enter_precond++;
			} else if (phase == Precondition.EPhase.E_UPDATE) {
				this.m_update_precond++;
			} else if (phase == Precondition.EPhase.E_BOTH) {
				this.m_both_precond++;
			} else {
				Debug.Check(false);
			}
		} else if (bIsEffector) {
			Debug.Check(!bIsPrecondition);

			if (this.m_effectors == null) {
				this.m_effectors = new ArrayList<Effector>();
			}

			Effector effector = (Effector) pAttachment;
			Debug.Check(effector != null);
			this.m_effectors.add(effector);

			var phase = effector.GetPhase();

			if (phase == Effector.EPhase.E_SUCCESS) {
				this.m_success_effectors++;
			} else if (phase == Effector.EPhase.E_FAILURE) {
				this.m_failure_effectors++;
			} else if (phase == Effector.EPhase.E_BOTH) {
				this.m_both_effectors++;
			} else {
				Debug.Check(false);
			}
		} else {
			if (this.m_events == null) {
				this.m_events = new ArrayList<BehaviorNode>();
			}

			this.m_events.add(pAttachment);
		}
	}

	protected EBTStatus update_impl(Agent pAgent, EBTStatus childStatus) {
		return EBTStatus.BT_FAILURE;
	}

	public BehaviorNode getParent() {
		return this.m_parent;
	}

	public int getPreconditionsCount() {
		if (this.m_preconditions != null) {
			return this.m_preconditions.size();
		}
		return 0;
	}

	public boolean CheckPreconditions(Agent pAgent, boolean bIsAlive) {
		Precondition.EPhase phase = bIsAlive ? Precondition.EPhase.E_UPDATE : Precondition.EPhase.E_ENTER;

		// satisfied if there is no preconditions
		if (this.m_preconditions == null || this.m_preconditions.size() == 0) {
			return true;
		}

		if (this.m_both_precond == 0) {
			if (phase == Precondition.EPhase.E_ENTER && this.m_enter_precond == 0) {
				return true;
			}

			if (phase == Precondition.EPhase.E_UPDATE && this.m_update_precond == 0) {
				return true;
			}
		}

		boolean firstValidPrecond = true;
		boolean lastCombineValue = false;

		for (int i = 0; i < this.m_preconditions.size(); ++i) {
			Precondition pPrecond = this.m_preconditions.get(i);

			if (pPrecond != null) {
				Precondition.EPhase ph = pPrecond.GetPhase();

				if (phase == Precondition.EPhase.E_BOTH || ph == Precondition.EPhase.E_BOTH || ph == phase) {
					boolean taskBoolean = pPrecond.Evaluate(pAgent);

//                    CombineResults(ref firstValidPrecond, ref lastCombineValue, pPrecond, taskBoolean);
					{
						if (firstValidPrecond) {
							firstValidPrecond = false;
							lastCombineValue = taskBoolean;
						} else {
							boolean andOp = pPrecond.IsAnd();

							if (andOp) {
								lastCombineValue = lastCombineValue && taskBoolean;
							}

							else {
								lastCombineValue = lastCombineValue || taskBoolean;
							}
						}
					}
				}
			}
		}
		return lastCombineValue;
	}

	public void ApplyEffects(Agent pAgent, Effector.EPhase phase) {
		if (this.m_effectors == null || this.m_effectors.size() == 0) {
			return;
		}

		if (this.m_both_effectors == 0) {
			if (phase == Effector.EPhase.E_SUCCESS && this.m_success_effectors == 0) {
				return;
			}

			if (phase == Effector.EPhase.E_FAILURE && this.m_failure_effectors == 0) {
				return;
			}
		}

		for (int i = 0; i < this.m_effectors.size(); ++i) {
			Effector pEffector = this.m_effectors.get(i);

			if (pEffector != null) {
				Effector.EPhase ph = pEffector.GetPhase();

				if (phase == Effector.EPhase.E_BOTH || ph == Effector.EPhase.E_BOTH || ph == phase) {
					pEffector.Evaluate(pAgent);
				}
			}
		}

		return;
	}

	public boolean CheckEvents(String eventName, Agent pAgent, Map<Long, IInstantiatedVariable> eventParams) {
		if (this.m_events != null) {
			// bool bTriggered = false;
			for (int i = 0; i < this.m_events.size(); ++i) {
				BehaviorNode pA = this.m_events.get(i);
				Event pE = (Event) pA;

				// check events only
				if (pE != null && !Utils.isNullOrEmpty(eventName)) {
					var pEventName = pE.GetEventName();

					if (!Utils.isNullOrEmpty(pEventName) && pEventName == eventName) {
						pE.switchTo(pAgent, eventParams);

						if (pE.TriggeredOnce()) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public boolean Evaluate(Agent pAgent) {
		Debug.Check(false, "only Condition/Sequence/And/Or allowed");
		return false;
	}

	protected boolean EvaluteCustomCondition(Agent pAgent) {
		if (this.m_customCondition != null) {
			return m_customCondition.Evaluate(pAgent);
		}

		return false;
	}

	public void SetCustomCondition(BehaviorNode node) {
		this.m_customCondition = node;
	}

	protected abstract BehaviorTask createTask();

	public boolean enteraction_impl(Agent pAgent) {
		return false;
	}

	public boolean exitaction_impl(Agent pAgent) {
		return false;
	}
}
