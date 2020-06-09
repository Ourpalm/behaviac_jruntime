package org.gof.behaviac;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gof.behaviac.members.IInstantiatedVariable;
import org.gof.behaviac.utils.Utils;

public class BehaviorTreeTask extends SingeChildTask {
	private Map<Long, IInstantiatedVariable> m_localVars = new HashMap<Long, IInstantiatedVariable>();
	private BehaviorTreeTask m_excutingTreeTask = null;
	private Set<String> m_ignoreEvents = new HashSet<>();
	private EBTStatus m_endStatus = EBTStatus.BT_INVALID;
	private long latestActionNodeBatchId = 0;

	public BehaviorTreeTask() {

	}

	public Map<Long, IInstantiatedVariable> GetLocalVars() {
		return this.m_localVars;
	}

	public void SetVariable(String variableName, Object value) {
		var variableId = Utils.MakeVariableId(variableName);

		IInstantiatedVariable v = this.m_localVars.get(variableId);
		if (v != null) {
			v.SetValue(null, value);
			return;
		}

		Debug.Check(false, String.format("The variable \"%s\" an not be found!", variableName));
	}

	void AddVariables(Map<Long, IInstantiatedVariable> vars) {
		if (vars != null) {
			for (var it : vars.entrySet()) {
				this.m_localVars.put(it.getKey(), it.getValue());
			}
		}
	}

	@Override
	public void Init(BehaviorNode node) {
		super.Init(node);
		if (this.m_node != null) {
			Debug.Check(this.m_node instanceof BehaviorTree);
			((BehaviorTree) this.m_node).InstantiatePars(this.m_localVars);
		}
	}

	@Override
	public void Clear() {
		this.m_root = null;

		if (this.m_node != null) {
			Debug.Check(this.m_node instanceof BehaviorTree);
			((BehaviorTree) this.m_node).UnInstantiatePars(this.m_localVars);
		}

		super.Clear();
	}

	public void SetRootTask(BehaviorTask pRoot) {
		this.addChild(pRoot);
	}

	public void CopyTo(BehaviorTreeTask target) {
		this.copyto(target);
	}

	public String GetName() {
		Debug.Check(this.m_node instanceof BehaviorTree);
		BehaviorTree bt = (BehaviorTree) this.m_node;
		Debug.Check(bt != null);
		return bt.GetName();
	}

	public void setEndStatus(EBTStatus status) {
		this.m_endStatus = status;
	}

	public EBTStatus resume(Agent pAgent, EBTStatus status) {
		EBTStatus s = super.resume_branch(pAgent, status);

		return s;
	}

	@Override
	protected boolean onenter(Agent pAgent) {
		pAgent.LogJumpTree(this.GetName());
		return true;
	}

	@Override
	protected void onexit(Agent pAgent, EBTStatus status) {
		pAgent.SetExcutingTreeTask(this.m_excutingTreeTask);
		pAgent.LogReturnTree(this.GetName());

		super.onexit(pAgent, status);
	}

	@Override
	protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
		Debug.Check(this.m_node != null);
		Debug.Check(this.m_node instanceof BehaviorTree);

		this.m_excutingTreeTask = pAgent.GetExcutingTreeTask();
		pAgent.SetExcutingTreeTask(this);

		BehaviorTree tree = (BehaviorTree) this.m_node;
		EBTStatus status = EBTStatus.BT_RUNNING;

		if (tree.isFSM()) {
			status = this.update(pAgent, childStatus);
		} else {
			status = super.update_current(pAgent, childStatus);
		}

		return status;
	}

	private void end(Agent pAgent, EBTStatus status) {
		this.traverse(true, end_handler_, pAgent, status);
	}

	@Override
	protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
		Debug.Check(this.m_node != null);
		Debug.Check(this.m_root != null);

		if (childStatus != EBTStatus.BT_RUNNING) {
			return childStatus;
		}

		EBTStatus status = EBTStatus.BT_INVALID;

		status = super.update(pAgent, childStatus);

		Debug.Check(status != EBTStatus.BT_INVALID);

		// When the End node takes effect, it always returns BT_RUNNING
		// and m_endStatus should always be BT_SUCCESS or BT_FAILURE
		if ((status == EBTStatus.BT_RUNNING) && (this.m_endStatus != EBTStatus.BT_INVALID)) {
			this.end(pAgent, this.m_endStatus);
			return this.m_endStatus;
		}

		return status;
	}

	public void addIgnoreEvent(String btEvent) {
		m_ignoreEvents.add(btEvent);
	}

	public boolean isIgnoreEvent(String btEvent) {
		return m_ignoreEvents.contains(btEvent);
	}

	public void onActionTaskEnter() {
		latestActionNodeBatchId = Math.max(0, ++latestActionNodeBatchId);
	}

	public void onActionTaskExit() {

	}

	public long getActionNodeBatchId() {
		return latestActionNodeBatchId;
	}
}
