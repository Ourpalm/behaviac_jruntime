package org.gof.behaviac;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.gof.behaviac.htn.Task;

public abstract class Agent implements Closeable {
	private static AtomicLong idAlloc = new AtomicLong(0);

	public static long allocId() {
		return idAlloc.getAndAdd(1);
	}

	public static class State_t {
		protected Variables m_vars = new Variables();

		public Variables GetVars() {
			return this.m_vars;
		}

		protected BehaviorTreeTask m_bt;

		public BehaviorTreeTask GetBT() {
			return this.m_bt;
		}

		public void SetBT(BehaviorTreeTask bt) {
			this.m_bt = bt;
		}

		public State_t(State_t c) {
			c.m_vars.CopyTo(null, this.m_vars);

			if (c.m_bt != null) {
				BehaviorNode pNode = c.m_bt.GetNode();

				if (pNode != null) {
					this.m_bt = (BehaviorTreeTask) pNode.CreateAndInitTask();

					c.m_bt.CopyTo(this.m_bt);
				}
			}
		}

		public boolean SaveToFile(String fileName) {
			return false;
		}

		public boolean LoadFromFile(String fileName) {
			return false;
		}
	}

	private long m_id = -1;
	private boolean m_bActive = true;
	private boolean m_referencetree = false;
	public int m_priority;
	public int m_contextId;
	private String name;

	private List<BehaviorTreeTask> m_behaviorTreeTasks;

	private List<BehaviorTreeTask> GetBehaviorTreeTasks() {
		if (m_behaviorTreeTasks == null) {
			m_behaviorTreeTasks = new ArrayList<BehaviorTreeTask>();
		}
		return m_behaviorTreeTasks;
	}

	protected Agent() {
		Init();
	}

	protected void Init() {
		Awake();
	}

	void Awake() {
		Init_(this.m_contextId, this, this.m_priority);
	}

	void OnDestroy() {
		Context.RemoveAgent(this);

		if (this.m_behaviorTreeTasks != null) {
			for (int i = 0; i < this.m_behaviorTreeTasks.size(); ++i) {
				BehaviorTreeTask bt = this.m_behaviorTreeTasks.get(i);
				Workspace.Instance.DestroyBehaviorTreeTask(bt, this);
			}

			this.m_behaviorTreeTasks.clear();
			this.m_behaviorTreeTasks = null;
		}
	}

	private static class BehaviorTreeStackItem_t {
		public BehaviorTreeTask bt;
		public TriggerMode triggerMode;
		public boolean triggerByEvent;

		public BehaviorTreeStackItem_t(BehaviorTreeTask bt_, TriggerMode tm, boolean bByEvent) {
			bt = bt_;
			triggerMode = tm;
			triggerByEvent = bByEvent;
		}
	}

	private List<BehaviorTreeStackItem_t> m_btStack;

	private List<BehaviorTreeStackItem_t> GetBTStack() {
		if (m_btStack == null) {
			m_btStack = new ArrayList<BehaviorTreeStackItem_t>();
		}
		return m_btStack;
	}

	private BehaviorTreeTask m_currentBT;

	public BehaviorTreeTask GetCurrentTreeTask() {
		return m_currentBT;
	}

	public void SetCurrentTreeTask(BehaviorTreeTask value) {
		m_currentBT = value;
	}

	private BehaviorTreeTask m_excutingTreeTask;

	public BehaviorTreeTask GetExcutingTreeTask() {
		return m_excutingTreeTask;
	}

	public void SetExcutingTreeTask(BehaviorTreeTask value) {
		m_excutingTreeTask = value;
	}

	public long GetId() {
		return this.m_id;
	}

	public int GetPriority() {
		return (int) this.m_priority;
	}

	public String GetClassTypeName() {
		return this.getClass().getName();
	}

	private static long ms_idMask = 0xffffffff;
	private long m_idFlag = 0xffffffff;

	public boolean IsMasked() {
		return (this.m_idFlag & Agent.IdMask()) != 0;
	}

	public void SetIdFlag(long idMask) {
		this.m_idFlag = idMask;
	}

	public static void SetIdMask(long idMask) {
		ms_idMask = idMask;
	}

	public static long IdMask() {
		return ms_idMask;
	}

	public String GetName() {
		return this.name;
	}

//    public static boolean IsDerived(Agent pAgent, String agentType)
//    {
//        boolean bIsDerived = false;
//        Class<?> type = pAgent.getClass();
//
//        while (type != null)
//        {
//            if (type.getSimpleName() == agentType)
//            {
//                bIsDerived = true;
//                break;
//            }
//            type = type.getSuperclass();
//        }
//
//        return bIsDerived;
//    }

	public void SetName(String instanceName) {
		this.name = instanceName + "-" + Long.toString(this.GetId());
	}

	public int GetContextId() {
		return this.m_contextId;
	}

	public boolean IsActive() {
		return this.m_bActive;
	}

	/**
	 * set the agent active or inactive
	 */

	public void SetActive(boolean bActive) {
		this.m_bActive = bActive;
	}

	private Variables m_variables = null;

	public Variables GetVariables() {
		if (m_variables == null) {
			Map<Long, IInstantiatedVariable> vars = this.GetCustomizedVariables();

			this.m_variables = new Variables(vars);
		}
		return m_variables;
	}

	private Map<Long, IInstantiatedVariable> GetCustomizedVariables() {
		var agentClassName = this.GetClassTypeName();
		var agentClassId = Utils.MakeVariableId(agentClassName);
		AgentMeta meta = AgentMeta.GetMeta(agentClassId);

		if (meta != null) {
			Map<Long, IInstantiatedVariable> vars = meta.InstantiateCustomizedProperties();

			return vars;
		}

		return null;
	}

	IInstantiatedVariable GetInstantiatedVariable(long varId) {
		// local var
		var task = this.GetExcutingTreeTask();
		if (task != null && task.GetLocalVars().containsKey(varId)) {
			return task.GetLocalVars().get(varId);
		}
		// customized var
		IInstantiatedVariable pVar = this.GetVariables().GetVariable(varId);

		return pVar;
	}

	private IProperty GetProperty(long propId) {
		var className = this.GetClassTypeName();
		var classId = Utils.MakeVariableId(className);
		AgentMeta meta = AgentMeta.GetMeta(classId);

		if (meta != null) {
			IProperty prop = meta.GetProperty(propId);

			if (prop != null) {
				return prop;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	<VariableType> VariableType GetVarValue(long varId) {
		IInstantiatedVariable v = this.GetInstantiatedVariable(varId);

		if (v != null) {
			if (v instanceof CVariable) {
				return (VariableType) v.GetValueObject(this);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <VariableType> VariableType GetVarValue(long varId, int index) {
		IInstantiatedVariable v = this.GetInstantiatedVariable(varId);

		if (v != null) {
			return (VariableType) v.GetValueObject(this, index);
		}
		return null;
	}

	public boolean SetVarValue(long varId, Object value) {
		IInstantiatedVariable v = this.GetInstantiatedVariable(varId);

		if (v != null) {
			v.SetValue(this, value);
			return true;
		}
		return false;
	}

	public boolean SetVarValue(long varId, int index, Object value) {
		IInstantiatedVariable v = this.GetInstantiatedVariable(varId);

		if (v != null) {
			Debug.Check(v instanceof CArrayItemVariable);
			CArrayItemVariable arrayItemVar = (CArrayItemVariable) v;

			if (arrayItemVar != null) {
				arrayItemVar.SetValue(this, value, index);
				return true;
			}
		}

		return false;
	}

	public boolean IsValidVariable(String variableName) {
		var variableId = Utils.MakeVariableId(variableName);

		IInstantiatedVariable v = this.GetInstantiatedVariable(variableId);

		if (v != null) {
			return true;
		}

		IProperty prop = this.GetProperty(variableId);
		return (prop != null);
	}

	public <VariableType> VariableType GetVariable(String variableName) {
		var variableId = Utils.MakeVariableId(variableName);

		return GetVariable(variableId);
	}

	@SuppressWarnings("unchecked")
	<VariableType> VariableType GetVariable(long variableId) {
		VariableType value = this.GetVarValue(variableId);
		if (value != null)
			return value;

		// property
		IProperty prop = this.GetProperty(variableId);
		if (prop != null) {
			return (VariableType) prop.GetValueObject(this);
		}

		Debug.Check(false, String.format("The variable \"%s\" can not be found!", Long.toString(variableId)));
		return null;
	}

	@SuppressWarnings("unchecked")
	<VariableType> VariableType GetVariable(long variableId, int index) {
		VariableType value = this.GetVarValue(variableId, index);
		if (value != null)
			return value;

		// property
		IProperty prop = this.GetProperty(variableId);

		if (prop != null) {
			return (VariableType) prop.GetValueObject(this, index);
		}

		Debug.Check(false,
				String.format("The variable \"%s\" with type \"{1}\" can not be found!", Long.toString(variableId)));
		return null;
	}

	public <VariableType> void SetVariable(String variableName, VariableType value) {
		var variableId = Utils.MakeVariableId(variableName);

		SetVariable(variableName, variableId, value);
	}

	public <VariableType> void SetVariable(String variableName, long variableId, VariableType value) {
		if (variableId == 0) {
			variableId = Utils.MakeVariableId(variableName);
		}

		// var
		if (this.SetVarValue(variableId, value)) {
			return;
		}

		// property
		IProperty prop = this.GetProperty(variableId);
		if (prop != null) {
			var p = (CProperty) prop;
			p.SetValue(this, value);
			return;
		}

		Debug.Check(false, String.format(
				"The variable \"%s\" can not be found! please check the variable name or be after loading type info(btload)!",
				variableName));
	}

	public <VariableType> void SetVariable(String variableName, long variableId, VariableType value, int index) {
		if (variableId == 0) {
			variableId = Utils.MakeVariableId(variableName);
		}

		// var
		if (this.SetVarValue(variableId, index, value)) {
			return;
		}

		// property
		IProperty prop = this.GetProperty(variableId);

		if (prop != null) {
			CProperty p = (CProperty) prop;
			if (p != null) {
				p.SetValue(this, value, index);
				return;
			}
		}

		Debug.Check(false, String.format("The variable \"%s\" with type \"{1}\" can not be found!", variableName));
	}

	void SetVariableFromString(String variableName, String valueStr) {
		var variableId = Utils.MakeVariableId(variableName);

		IInstantiatedVariable v = this.GetInstantiatedVariable(variableId);

		if (v != null) {
			v.SetValueFromString(this, valueStr);
			return;
		}

		IProperty prop = this.GetProperty(variableId);
		if (prop != null) {
			prop.SetValueFromString(this, valueStr);
		}
	}

	public void LogVariables(boolean bForce) {

	}

	public void LogJumpTree(String getName) {
		// TODO Auto-generated method stub

	}

	public void LogReturnTree(String getName) {
		// TODO Auto-generated method stub

	}

	public void LogRunningNodes() {

	}

	protected static void Init_(int contextId, Agent pAgent, int priority) {
		Debug.Check(contextId >= 0, "invalid context id");

		pAgent.m_contextId = contextId;
		pAgent.m_id = allocId();
		pAgent.m_priority = priority;
		pAgent.SetName(pAgent.name);

		Context.AddAgent(pAgent);
	}

	public void btresetcurrrent() {
		if (this.m_currentBT != null) {
			this.m_currentBT.reset(this);
		}
	}

	public void btsetcurrent(String relativePath) {
		_btsetcurrent(relativePath, TriggerMode.TM_Transfer, false);
	}

	public void btreferencetree(String relativePath) {
		this._btsetcurrent(relativePath, TriggerMode.TM_Return, false);
		this.m_referencetree = true;
	}

	public void bteventtree(Agent pAgent, String relativePath, TriggerMode triggerMode) {
		this._btsetcurrent(relativePath, triggerMode, true);
	}

	private void _btsetcurrent(String relativePath, TriggerMode triggerMode, boolean bByEvent) {
		boolean bEmptyPath = Utils.IsNullOrEmpty(relativePath);
		Debug.Check(!bEmptyPath && Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)));
		Debug.Check(Workspace.Instance.IsValidPath(relativePath));

		if (!bEmptyPath) {
			// if (this.m_currentBT != null && this.m_currentBT.GetName() == relativePath) {
			// //the same bt is set again return; }

			boolean bLoaded = Workspace.Instance.Load(relativePath);

			if (!bLoaded) {
				String agentName = this.getClass().getName();
				agentName += "::";
				agentName += this.name;

				Debug.Check(false,
						String.format("%s is not a valid loaded behavior tree of %s", relativePath, agentName));
			} else {
				Workspace.Instance.RecordBTAgentMapping(relativePath, this);

				if (this.m_currentBT != null) {
					// if trigger mode is 'return', just push the current bt 'oldBt' on the stack
					// and do nothing more
					// 'oldBt' will be restored when the new triggered one ends
					if (triggerMode == TriggerMode.TM_Return) {
						BehaviorTreeStackItem_t item = new BehaviorTreeStackItem_t(this.m_currentBT, triggerMode,
								bByEvent);
						Debug.Check(this.GetBTStack().size() < 200, "recursive?");

						this.GetBTStack().add(item);
					} else if (triggerMode == TriggerMode.TM_Transfer) {
						// don't use the bt stack to restore, we just abort the current one.
						// as the bt node has onenter/onexit, the abort can make them paired
						// Debug.Check (this.m_currentBT.GetName() != relativePath);

						if (this.m_currentBT.GetName() != relativePath) {
							this.m_currentBT.abort(this);
						} else {
							Debug.Check(true);
						}
					}
				}

				// BehaviorTreeTask pTask = this.BehaviorTreeTasks.Find(delegate
				// (BehaviorTreeTask task) {return task.GetName() == relativePath;});
				BehaviorTreeTask pTask = null;

				for (int i = 0; i < this.GetBehaviorTreeTasks().size(); ++i) {
					BehaviorTreeTask t = this.GetBehaviorTreeTasks().get(i);

					if (t.GetName() == relativePath) {
						pTask = t;
						break;
					}
				}

				boolean bRecursive = false;

				if (pTask != null && this.GetBTStack().size() > 0) {
					// bRecursive = this.BTStack.FindIndex(delegate (BehaviorTreeStackItem_t
					// item){return item.bt.GetName() == relativePath;}) > -1;
					for (int i = 0; i < this.GetBTStack().size(); ++i) {
						BehaviorTreeStackItem_t item = this.GetBTStack().get(i);

						if (item.bt.GetName() == relativePath) {
							bRecursive = true;
							break;
						}
					}

					if (pTask.GetStatus() != EBTStatus.BT_INVALID) {
						pTask.reset(this);
					}
				}

				if (pTask == null || bRecursive) {
					pTask = Workspace.Instance.CreateBehaviorTreeTask(relativePath);
					Debug.Check(pTask != null);
					this.GetBehaviorTreeTasks().add(pTask);
				}

				this.SetCurrentTreeTask(pTask);

				// string pThisTree = this.m_currentBT.GetName();
				// this.LogJumpTree(pThisTree);
			}
		}
	}

	private EBTStatus btexec_() {
		if (this.m_currentBT != null) {
			// the following might modify this.m_currentBT if the invoked function called
			// btsetcurrent/FireEvent
			BehaviorTreeTask pCurrentBT = this.m_currentBT;
			EBTStatus s = this.m_currentBT.exec(this);
			// Debug.Check(s == EBTStatus.BT_RUNNING || pCurrentBT == this.m_currentBT,
			// "btsetcurrent/FireEvent is not allowed in the invoked function.");

			while (s != EBTStatus.BT_RUNNING) {
				if (this.GetBTStack().size() > 0) {
					// get the last one
					BehaviorTreeStackItem_t lastOne = this.GetBTStack().get(this.GetBTStack().size() - 1);

					this.SetCurrentTreeTask(lastOne.bt);
					this.GetBTStack().remove(this.GetBTStack().size() - 1);

					boolean bExecCurrent = false;

					if (lastOne.triggerMode == TriggerMode.TM_Return) {
						if (!lastOne.triggerByEvent) {
							if (this.m_currentBT != pCurrentBT) {
								s = this.m_currentBT.resume(this, s);
							} else {
								// pCurrentBT ends and while pCurrentBT is exec, it internally calls
								// 'btsetcurrent'
								// to modify m_currentBT as the new one, and after pop from the stack,
								// m_currentBT would be pCurrentBT
								Debug.Check(true);
							}
						} else {
							bExecCurrent = true;
						}
					} else {
						bExecCurrent = true;
					}

					if (bExecCurrent) {
						pCurrentBT = this.m_currentBT;
						s = this.m_currentBT.exec(this);
						break;
					}
				} else {
					// this.CurrentBT = null;
					break;
				}
			}

			if (s != EBTStatus.BT_RUNNING) {
				this.SetExcutingTreeTask(null);
			}

			return s;
		} else {
			// behaviac.Debug.LogWarning("NO ACTIVE BT!\n");
		}

		return EBTStatus.BT_INVALID;
	}

	public EBTStatus btexec() {
		if (this.m_bActive) {

			EBTStatus s = this.btexec_();

			while (this.m_referencetree && s == EBTStatus.BT_RUNNING) {
				this.m_referencetree = false;
				s = this.btexec_();
			}

			if (this.IsMasked()) {
				this.LogVariables(false);
			}

			return s;
		}

		return EBTStatus.BT_INVALID;
	}

	public boolean btload(String relativePath, boolean bForce /* = false */) {
		boolean bOk = Workspace.Instance.Load(relativePath, bForce);

		if (bOk) {
			Workspace.Instance.RecordBTAgentMapping(relativePath, this);
		}

		return bOk;
	}

	public boolean btload(String relativePath) {
		boolean bOk = this.btload(relativePath, false);

		return bOk;
	}

	@Override
	public void close() throws IOException {
		OnDestroy();
	}

	public void btunload(String relativePath) {
		Debug.Check(Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)), "no extention to specify");
		Debug.Check(Workspace.Instance.IsValidPath(relativePath));

		// clear the current bt if it is the current bt
		if (this.m_currentBT != null && this.m_currentBT.GetName() == relativePath) {
			BehaviorNode btNode = this.m_currentBT.GetNode();
			Debug.Check(btNode instanceof BehaviorTree);
			BehaviorTree bt = (BehaviorTree) btNode;
			this.btunload_pars(bt);

			this.SetCurrentTreeTask(null);
		}

		// remove it from stack
		for (int i = 0; i < this.GetBTStack().size(); ++i) {
			BehaviorTreeStackItem_t item = this.GetBTStack().get(i);

			if (item.bt.GetName() == relativePath) {
				this.GetBTStack().remove(item);
				break;
			}
		}

		for (int i = 0; i < this.GetBehaviorTreeTasks().size(); ++i) {
			BehaviorTreeTask task = this.GetBehaviorTreeTasks().get(i);

			if (task.GetName() == relativePath) {
				Workspace.Instance.DestroyBehaviorTreeTask(task, this);

				this.GetBehaviorTreeTasks().remove(task);
				break;
			}
		}

		Workspace.Instance.UnLoad(relativePath);
	}

	public void bthotreloaded(BehaviorTree bt) {
		this.btunload_pars(bt);
	}

	public void btunloadall() {
		List<BehaviorTree> bts = new ArrayList<BehaviorTree>();

		for (int i = 0; i < this.GetBehaviorTreeTasks().size(); ++i) {
			BehaviorTreeTask btTask = this.GetBehaviorTreeTasks().get(i);
			BehaviorNode btNode = btTask.GetNode();
			Debug.Check(btNode instanceof BehaviorTree);
			BehaviorTree bt = (BehaviorTree) btNode;

			boolean bFound = false;

			for (int t = 0; t < bts.size(); ++t) {
				BehaviorTree it = bts.get(t);

				if (it == bt) {
					bFound = true;
					break;
				}
			}

			if (!bFound) {
				bts.add(bt);
			}

			Workspace.Instance.DestroyBehaviorTreeTask(btTask, this);
		}

		for (int t = 0; t < bts.size(); ++t) {
			BehaviorTree it = bts.get(t);
			this.btunload_pars(it);

			Workspace.Instance.UnLoad(it.GetName());
		}

		this.GetBehaviorTreeTasks().clear();

		// just clear the name vector, don't unload it from cache
		this.SetCurrentTreeTask(null);
		this.GetBTStack().clear();

		this.GetVariables().Unload();
	}

	public void btreloadall() {
		this.SetCurrentTreeTask(null);
		this.GetBTStack().clear();

		if (this.m_behaviorTreeTasks != null) {
			List<String> bts = new ArrayList<String>();

			// collect the bts
			for (int i = 0; i < this.m_behaviorTreeTasks.size(); ++i) {
				BehaviorTreeTask bt = this.m_behaviorTreeTasks.get(i);
				String btName = bt.GetName();

				if (bts.indexOf(btName) == -1) {
					bts.add(btName);
				}
			}

			for (int i = 0; i < bts.size(); ++i) {
				var btName = bts.get(i);
				Workspace.Instance.Load(btName, true);
			}

			this.GetBehaviorTreeTasks().clear();
		}

		this.GetVariables().Unload();
	}

	public boolean btsave(State_t state) {
		this.GetVariables().CopyTo(null, state.GetVars());

		if (this.m_currentBT != null) {
			Workspace.Instance.DestroyBehaviorTreeTask(state.GetBT(), this);

			BehaviorNode pNode = this.m_currentBT.GetNode();

			if (pNode != null) {
				state.SetBT((BehaviorTreeTask) pNode.CreateAndInitTask());
				this.m_currentBT.CopyTo(state.GetBT());

				return true;
			}
		}

		return false;
	}

	public boolean btload(State_t state) {
		state.GetVars().CopyTo(this, this.m_variables);

		if (state.GetBT() != null) {
			if (this.m_currentBT != null) {
				for (int i = 0; i < this.m_behaviorTreeTasks.size(); ++i) {
					BehaviorTreeTask task = this.m_behaviorTreeTasks.get(i);

					if (task == this.m_currentBT) {
						Workspace.Instance.DestroyBehaviorTreeTask(task, this);

						this.m_behaviorTreeTasks.remove(task);
						break;
					}
				}
			}

			BehaviorNode pNode = state.GetBT().GetNode();

			if (pNode != null) {
				this.m_currentBT = (BehaviorTreeTask) pNode.CreateAndInitTask();
				state.GetBT().CopyTo(this.m_currentBT);

				return true;
			}
		}

		return false;
	}

	private void btunload_pars(BehaviorTree bt) {
		if (bt.getLocalProps() != null) {
			bt.getLocalProps().clear();
		}
	}

	public void btonevent(String btEvent, Map<Long, IInstantiatedVariable> eventParams) {
		if (this.m_currentBT != null) {
			String agentClassName = this.GetClassTypeName();
			var agentClassId = Utils.MakeVariableId(agentClassName);
			AgentMeta meta = AgentMeta.GetMeta(agentClassId);

			if (meta != null) {
				var eventId = Utils.MakeVariableId(btEvent);
				IMethod e = meta.GetMethod(eventId);

				if (e != null) {
					this.m_currentBT.onevent(this, btEvent, eventParams);
				} else {
					Debug.Check(false, String.format("unregistered event %s", btEvent));
				}
			}
		}
	}

	public void FireEvent(String eventName) {
		this.btonevent(eventName, null);
	}

	public void FireEvent(String eventName, Object[] parameters, ClassInfo[] classes) {
		Map<Long, IInstantiatedVariable> eventParams = new HashMap<Long, IInstantiatedVariable>();

		for (int i = 0; i < parameters.length; ++i) {
			var paramName = String.format("{0}{1}", Task.LOCAL_TASK_PARAM_PRE, i);
			var paramId = Utils.MakeVariableId(paramName);
			eventParams.put(paramId, new CVariable(paramName, parameters[i], classes[i]));
		}
		this.btonevent(eventName, eventParams);
	}

	public static void LogMessage(String message) {
		System.out.println(message);
//        int frames = behaviac.Workspace.Instance.FrameSinceStartup;
//
//        behaviac.Debug.Log(string.Format("[{0}]{1}\n", frames, message));
	}

	public static int VectorLength(List vector) {
		if (vector != null) {
			return vector.size();
		}

		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void VectorAdd(List vector, Object element) {
		if (vector != null) {
			vector.add(element);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void VectorRemove(List vector, Object element) {
		if (vector != null) {
			vector.remove(element);
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean VectorContains(List vector, Object element) {
		if (vector != null) {
			boolean bOk = vector.indexOf(element) > -1;

			return bOk;
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	public static void VectorClear(List vector) {
		if (vector != null) {
			vector.clear();
		}
	}

	public abstract Object GetProperty();

	public abstract void SetProperty(String property, Object value);

	public abstract Object ExecuteMethod(String method, Object[] args);

	public abstract long GetCurrentTime();

	public abstract long GetFrameSinceStartup();
}
