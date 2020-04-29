package org.gof.behaviac.composites;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;

@RegisterableNode
public class Parallel extends BehaviorNode {
	// options when a parallel node is considered to be failed.
	// FAIL_ON_ONE: the node fails as soon as one of its children fails.
	// FAIL_ON_ALL: the node failes when all of the node's children must fail
	// If FAIL_ON_ONE and SUCEED_ON_ONE are both active and are both trigerred, it
	// fails
	public enum FAILURE_POLICY {
		FAIL_ON_ONE, FAIL_ON_ALL
	}

	// options when a parallel node is considered to be succeeded.
	// SUCCEED_ON_ONE: the node will return success as soon as one of its children
	// succeeds.
	// SUCCEED_ON_ALL: the node will return success when all the node's children
	// must succeed.
	public enum SUCCESS_POLICY {
		SUCCEED_ON_ONE, SUCCEED_ON_ALL
	}

	// options when a parallel node is exited
	// EXIT_NONE: the parallel node just exit.
	// EXIT_ABORT_RUNNINGSIBLINGS: the parallel node abort all other running
	// siblings.
	public enum EXIT_POLICY {
		EXIT_NONE, EXIT_ABORT_RUNNINGSIBLINGS
	}

	// the options of what to do when a child finishes
	// CHILDFINISH_ONCE: the child node just executes once.
	// CHILDFINISH_LOOP: the child node runs again and again.
	public enum CHILDFINISH_POLICY {
		CHILDFINISH_ONCE, CHILDFINISH_LOOP
	}

	protected FAILURE_POLICY m_failPolicy;
	protected SUCCESS_POLICY m_succeedPolicy;
	protected EXIT_POLICY m_exitPolicy;
	protected CHILDFINISH_POLICY m_childFinishPolicy;

	public Parallel() {
		m_failPolicy = FAILURE_POLICY.FAIL_ON_ONE;
		m_succeedPolicy = SUCCESS_POLICY.SUCCEED_ON_ALL;
		m_exitPolicy = EXIT_POLICY.EXIT_NONE;
		m_childFinishPolicy = CHILDFINISH_POLICY.CHILDFINISH_LOOP;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "FailurePolicy") {
				if (p.value == "FAIL_ON_ONE") {
					this.m_failPolicy = FAILURE_POLICY.FAIL_ON_ONE;
				} else if (p.value == "FAIL_ON_ALL") {
					this.m_failPolicy = FAILURE_POLICY.FAIL_ON_ALL;
				} else {
					Debug.Check(false);
				}
			} else if (p.name == "SuccessPolicy") {
				if (p.value == "SUCCEED_ON_ONE") {
					this.m_succeedPolicy = SUCCESS_POLICY.SUCCEED_ON_ONE;
				} else if (p.value == "SUCCEED_ON_ALL") {
					this.m_succeedPolicy = SUCCESS_POLICY.SUCCEED_ON_ALL;
				} else {
					Debug.Check(false);
				}
			} else if (p.name == "ExitPolicy") {
				if (p.value == "EXIT_NONE") {
					this.m_exitPolicy = EXIT_POLICY.EXIT_NONE;
				} else if (p.value == "EXIT_ABORT_RUNNINGSIBLINGS") {
					this.m_exitPolicy = EXIT_POLICY.EXIT_ABORT_RUNNINGSIBLINGS;
				} else {
					Debug.Check(false);
				}
			} else if (p.name == "ChildFinishPolicy") {
				if (p.value == "CHILDFINISH_ONCE") {
					this.m_childFinishPolicy = CHILDFINISH_POLICY.CHILDFINISH_ONCE;
				} else if (p.value == "CHILDFINISH_LOOP") {
					this.m_childFinishPolicy = CHILDFINISH_POLICY.CHILDFINISH_LOOP;
				} else {
					Debug.Check(false);
				}
			} else {
				// todo: enter exit action failed here by mistake
				// Debug.Check(false);
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Parallel)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	public boolean IsManagingChildrenAsSubTrees() {
		// TODO Auto-generated method stub
		return true;
	}

	private void SetPolicy(FAILURE_POLICY failPolicy /* = FAILURE_POLICY.FAIL_ON_ALL */,
			SUCCESS_POLICY successPolicy /* = SUCCESS_POLICY.SUCCEED_ON_ALL */,
			EXIT_POLICY exitPolicty /* = EXIT_POLICY.EXIT_NONE */) {
		m_failPolicy = failPolicy;
		m_succeedPolicy = successPolicy;
		m_exitPolicy = exitPolicty;
	}

	@Override
	protected BehaviorTask createTask() {
		ParallelTask pTask = new ParallelTask();

		return pTask;
	}

	public EBTStatus ParallelUpdate(Agent pAgent, List<BehaviorTask> children) {
		boolean sawSuccess = false;
		boolean sawFail = false;
		boolean sawRunning = false;
		boolean sawAllFails = true;
		boolean sawAllSuccess = true;

		boolean bLoop = (this.m_childFinishPolicy == CHILDFINISH_POLICY.CHILDFINISH_LOOP);

		// go through all m_children
		for (int i = 0; i < children.size(); ++i) {
			BehaviorTask pChild = children.get(i);

			EBTStatus treeStatus = pChild.GetStatus();

			if (bLoop || (treeStatus == EBTStatus.BT_RUNNING || treeStatus == EBTStatus.BT_INVALID)) {
				EBTStatus status = pChild.exec(pAgent);

				if (status == EBTStatus.BT_FAILURE) {
					sawFail = true;
					sawAllSuccess = false;
				} else if (status == EBTStatus.BT_SUCCESS) {
					sawSuccess = true;
					sawAllFails = false;
				} else if (status == EBTStatus.BT_RUNNING) {
					sawRunning = true;
					sawAllFails = false;
					sawAllSuccess = false;
				}
			} else if (treeStatus == EBTStatus.BT_SUCCESS) {
				sawSuccess = true;
				sawAllFails = false;
			} else {
				Debug.Check(treeStatus == EBTStatus.BT_FAILURE);

				sawFail = true;
				sawAllSuccess = false;
			}
		}

		EBTStatus result = sawRunning ? EBTStatus.BT_RUNNING : EBTStatus.BT_FAILURE;

		if ((this.m_failPolicy == FAILURE_POLICY.FAIL_ON_ALL && sawAllFails)
				|| (this.m_failPolicy == FAILURE_POLICY.FAIL_ON_ONE && sawFail)) {
			result = EBTStatus.BT_FAILURE;
		} else if ((this.m_succeedPolicy == SUCCESS_POLICY.SUCCEED_ON_ALL && sawAllSuccess)
				|| (this.m_succeedPolicy == SUCCESS_POLICY.SUCCEED_ON_ONE && sawSuccess)) {
			result = EBTStatus.BT_SUCCESS;
		}

		// else if (m_failPolicy == FAIL_ON_ALL && m_succeedPolicy == SUCCEED_ON_ALL &&
		// sawRunning)
		// {
		// return EBTStatus.BT_RUNNING;
		// }

		if (this.m_exitPolicy == EXIT_POLICY.EXIT_ABORT_RUNNINGSIBLINGS
				&& (result == EBTStatus.BT_FAILURE || result == EBTStatus.BT_SUCCESS)) {
			for (int i = 0; i < children.size(); ++i) {
				BehaviorTask pChild = children.get(i);
				// Debug.Check(BehaviorTreeTask.DynamicCast(pChild));
				EBTStatus treeStatus = pChild.GetStatus();

				if (treeStatus == EBTStatus.BT_RUNNING) {
					pChild.abort(pAgent);
				}
			}
		}

		return result;
	}

	/// Execute behaviors in parallel
	// There are two policies that control the flow of execution:
	// the policy for failure, and the policy for success.
	private static class ParallelTask extends CompositeTask {
		// ~ParallelTask()
		// {
		// this.m_children.Clear();
		// }

		@Override
		protected boolean onenter(Agent pAgent) {
			Debug.Check(this.m_activeChildIndex == CompositeTask.InvalidChildIndex);

			// reset the status cache of the children
			// for (int i = 0; i < this.m_children.Count; ++i)
			// {
			// BehaviorTask pChild = this.m_children[i];

			// pChild.reset(pAgent);
			// }

			return true;
		}

		// no current task, as it needs to update every child for every update
		@Override
		protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
			EBTStatus s = this.update(pAgent, childStatus);

			return s;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			Debug.Check(this.GetNode() instanceof Parallel);
			Parallel pParallelNode = (Parallel) (this.GetNode());

			return pParallelNode.ParallelUpdate(pAgent, this.m_children);
		}
	}

}
