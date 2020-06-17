package org.gof.behaviac;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.gof.behaviac.utils.StringUtils;
import org.gof.behaviac.utils.Utils;

/**
 * 全局唯一工作区，加载部分线程安全
 * @author caobihuan/ctemple@21cn.com
 *
 */
public class Workspace implements Closeable {

	public static Workspace Instance = null;

	public final int EFF_XML = 1;
	public final int EFF_BSON = 2;

	private EFileFormat m_fileFormat = EFileFormat.EFF_xml;
	private String m_filePath = null;
	private String m_metaFile = null;
	private ReadWriteLock m_behaviortreesLock = new ReentrantReadWriteLock();
	private Map<String, BehaviorTree> m_behaviortrees = new HashMap<>();
	private Map<String, Class<?>> m_behaviorNodeTypes = new HashMap<>();
	private boolean m_bRegistered = false;

	static {
		Instance = new Workspace();
	}

	private static String GetDefaultFilePath() {
		return "";
	}

	public Workspace() {
		this.LoadBuiltinNodes();
	}

	private Map<String, BehaviorTree> GetBehaviorTrees() {
		m_behaviortreesLock.readLock().lock();
		try {
			return new HashMap<>(m_behaviortrees);
		} finally {
			m_behaviortreesLock.readLock().unlock();
		}
	}

	public EFileFormat GetFileFormat() {
		return this.m_fileFormat;
	}

	public void SetFileFormat(EFileFormat f) {
		this.m_fileFormat = f;
	}

	public String GetFilePath() {
		if (Utils.IsNullOrEmpty(m_filePath)) {
			m_filePath = GetDefaultFilePath();
		}

		return this.m_filePath;
	}

	public void SetFilePath(String v) {
		this.m_filePath = v;
	}

	public String GetMetaFile() {
		return this.m_metaFile;
	}

	public void SetMetaFile(String value) {
		this.m_metaFile = value;
	}

	private boolean m_bInited = false;

	public boolean IsInited() {
		return m_bInited;
	}

	public synchronized boolean TryInit() {
		if (this.m_bInited) {
			return true;
		}

		this.m_bInited = true;

		Workspace.Instance.RegisterStuff();

		if (Utils.IsNullOrEmpty(this.GetFilePath())) {
			Debug.LogError("No FilePath file is specified!");
			Debug.Check(false);

			return false;
		}

//		Debug.Log(String.format("FilePath: %s\n", this.GetFilePath()));
		Debug.Check(!this.GetFilePath().endsWith("\\"), "use '/' instead of '\\'");
		return true;
	}

	public void Cleanup() {
		this.UnLoadAll();

		Debug.Check(this.m_bRegistered);

		this.UnRegisterStuff();

		this.m_bInited = false;
	}

	void RegisterStuff() {
		// only register metas and others at the 1st time
		if (!this.m_bRegistered) {
			this.m_bRegistered = true;

			AgentMeta.Register();
		}
	}

	private void UnRegisterStuff() {
		Debug.Check(this.m_bRegistered);

		this.UnRegisterBehaviorNode();

		AgentMeta.UnRegister();

		this.m_bRegistered = false;
	}

	public void LogWorkspaceInfo() {

	}

	protected void LogFrames() {

	}

	public void RecordBTAgentMapping(String relativePath, Agent agent) {

	}

	public void UnLoad(String relativePath) {
		Debug.Check(Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)), "no extention to specify");
		Debug.Check(this.IsValidPath(relativePath));

		m_behaviortreesLock.writeLock().lock();
		try {
			m_behaviortrees.remove(relativePath);
		} finally {
			m_behaviortreesLock.writeLock().unlock();
		}

	}

	public void UnLoadAll() {
		m_behaviortreesLock.writeLock().lock();
		try {
			m_behaviortrees.clear();
		} finally {
			m_behaviortreesLock.writeLock().unlock();
		}
	}

	public byte[] ReadFileToBuffer(String file, String ext) {
		try {
			byte[] pBuffer = Files.readAllBytes(Paths.get(file + ext));
			return pBuffer;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void PopFileFromBuffer(String file, String ext, byte[] pBuffer) {
//		FileManager.Instance.FileClose(file, ext, pBuffer);
	}

	private String getValidFilename(String filename) {
		filename = filename.replace("/", "_");
		filename = filename.replace("-", "_");
		return filename;
	}

	public BehaviorTree Load(String relativePath, boolean bForce) {
		Debug.Check(Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)), "no extention to specify");
		Debug.Check(this.IsValidPath(relativePath));

		if (!m_bInited) {
			TryInit();
		}

		BehaviorTree pBT = null;
		m_behaviortreesLock.readLock().lock();
		try {
			pBT = m_behaviortrees.get(relativePath);
			if (pBT != null && !bForce)
				return pBT;
		} finally {
			m_behaviortreesLock.readLock().unlock();
		}

		var fullPath = Utils.Combine(this.GetFilePath(), relativePath);
		fullPath = fullPath.replace('\\', '/');

		var ext = ".xml";
		EFileFormat f = this.GetFileFormat();

		var bLoadResult = false;
		var bCleared = false;
		var bNewly = false;

		m_behaviortreesLock.writeLock().lock();
		try {
			if (pBT == null) {
				bNewly = true;
				pBT = new BehaviorTree();
				// in case of circular referencebehavior
				this.m_behaviortrees.put(relativePath, pBT);
			}

			Debug.Check(pBT != null);

			if (f == EFileFormat.EFF_xml) {
				byte[] pBuffer = ReadFileToBuffer(fullPath, ext);

				if (pBuffer != null) {
					// if forced to reload
					if (!bNewly) {
						bCleared = true;
						pBT.Clear();
					}
					bLoadResult = pBT.load_xml(pBuffer);
					PopFileFromBuffer(fullPath, ext, pBuffer);
				} else {
					Debug.LogError(String.format("'%s' doesn't exist!, Please set Workspace.FilePath", fullPath));
					Debug.Check(false);
				}
			} else {
				Debug.Check(false);
			}

			if (bLoadResult) {
				Debug.Check(pBT.GetName().equals(relativePath));

				if (!bNewly) {
					Debug.Check(m_behaviortrees.get(pBT.GetName()) == pBT);
				}
			} else {
				if (bNewly) {
					var removed = m_behaviortrees.remove(relativePath);
					Debug.Check(removed != null);
				} else if (bCleared) {
					// it has been cleared but failed to load, to remove it
					m_behaviortrees.remove(relativePath);
				}

				Debug.LogError(String.format("%s is not loaded!", fullPath));
			}
			return pBT;
		} finally {
			m_behaviortreesLock.writeLock().unlock();
		}
	}

	public BehaviorTree Load(String relativePath) {
		return this.Load(relativePath, false);
	}

	public BehaviorTree LoadBehaviorTree(String relativePath) {
		m_behaviortreesLock.readLock().lock();
		try {
			var r = m_behaviortrees.get(relativePath);
			if (r != null)
				return r;
		} finally {
			m_behaviortreesLock.readLock().unlock();
		}
		return this.Load(relativePath, false);
	}

	public boolean IsValidPath(String relativePath) {
		Debug.Check(!Utils.IsNullOrEmpty(relativePath));

		if (relativePath.charAt(0) == '.' && (relativePath.charAt(1) == '/' || relativePath.charAt(1) == '\\')) {
			// ./dummy_bt
			return false;
		} else if (relativePath.charAt(0) == '/' || relativePath.charAt(0) == '\\') {
			// /dummy_bt
			return false;
		}

		return true;
	}

	public BehaviorTreeTask CreateBehaviorTreeTask(String relativePath) {
		Debug.Check(Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)), "no extention to specify");
		Debug.Check(this.IsValidPath(relativePath));

		BehaviorTree bt = this.Load(relativePath);
		if (bt != null) {
			BehaviorTask task = bt.CreateAndInitTask();
			Debug.Check(task instanceof BehaviorTreeTask);
			BehaviorTreeTask behaviorTreeTask = (BehaviorTreeTask) task;
			return behaviorTreeTask;
		}

		return null;
	}

	public void DestroyBehaviorTreeTask(BehaviorTreeTask behaviorTreeTask, Agent agent) {
		if (behaviorTreeTask != null) {
			BehaviorTask.DestroyTask(behaviorTreeTask);
		}
	}

	private void UnRegisterBehaviorNode() {
		Debug.Check(m_behaviorNodeTypes != null);
		m_behaviorNodeTypes.clear();
	}

	private void LoadBuiltinNodes() {
		Class<?>[] nodeClasses = new Class<?>[] {
			org.gof.behaviac.composites.SelectorProbability.class,
			org.gof.behaviac.actions.WaitFrames.class,
			org.gof.behaviac.decorators.DecoratorWeight.class,
			org.gof.behaviac.composites.SelectorLoop.class,
			org.gof.behaviac.htn.Task.class,
			org.gof.behaviac.composites.ReferencedBehavior.class,
			org.gof.behaviac.decorators.DecoratorCount.class,
			org.gof.behaviac.conditions.And.class,
			org.gof.behaviac.fsm.WaitTransition.class,
			org.gof.behaviac.conditions.Condition.class,
			org.gof.behaviac.htn.Variables.class,
			org.gof.behaviac.decorators.DecoratorNot.class,
			org.gof.behaviac.decorators.DecoratorFrames.class,
			org.gof.behaviac.composites.Sequence.class,
			org.gof.behaviac.actions.End.class,
			org.gof.behaviac.decorators.DecoratorAlwaysSuccess.class,
			org.gof.behaviac.htn.Method.class,
			org.gof.behaviac.composites.WithPrecondition.class,
			org.gof.behaviac.fsm.WaitFramesState.class,
			org.gof.behaviac.composites.IfElse.class,
			org.gof.behaviac.conditions.True.class,
			org.gof.behaviac.composites.Parallel.class,
			org.gof.behaviac.decorators.DecoratorTime.class,
			org.gof.behaviac.fsm.FSM.class,
			org.gof.behaviac.decorators.DecoratorAlwaysRunning.class,
			org.gof.behaviac.actions.WaitforSignal.class,
			org.gof.behaviac.fsm.AlwaysTransition.class,
			org.gof.behaviac.decorators.DecoratorLoopUntil.class,
			org.gof.behaviac.actions.Noop.class,
			org.gof.behaviac.composites.SelectorStochastic.class,
			org.gof.behaviac.actions.Action.class,
			org.gof.behaviac.decorators.DecoratorAlwaysFailure.class,
			org.gof.behaviac.decorators.DecoratorCountLimit.class,
			org.gof.behaviac.conditions.False.class,
			org.gof.behaviac.fsm.StartCondition.class,
			org.gof.behaviac.decorators.DecoratorIterator.class,
			org.gof.behaviac.decorators.DecoratorLoop.class,
			org.gof.behaviac.conditions.Or.class,
			org.gof.behaviac.composites.CompositeStochastic.class,
			org.gof.behaviac.actions.Assignment.class,
			org.gof.behaviac.actions.Wait.class,
			org.gof.behaviac.composites.Selector.class,
			org.gof.behaviac.decorators.DecoratorSuccessUntil.class,
			org.gof.behaviac.decorators.DecoratorLog.class,
			org.gof.behaviac.Event.class,
			org.gof.behaviac.decorators.DecoratorFailureUntil.class,
			org.gof.behaviac.fsm.TransitionCondition.class,
			org.gof.behaviac.Precondition.class,
			org.gof.behaviac.fsm.WaitState.class,
			org.gof.behaviac.decorators.DecoratorRepeat.class,
			org.gof.behaviac.actions.Compute.class,
			org.gof.behaviac.fsm.State.class,
			org.gof.behaviac.fsm.Transition.class,
		};
		
		for(Class<?> clazz : nodeClasses) {
			var anno = clazz.getAnnotation(RegisterableNode.class);
			if (anno != null) {
				String name;
				if (!Utils.IsNullOrEmpty(anno.value())) {
					name = anno.value();
				} else {
					name = clazz.getSimpleName();
				}
				name = "behaviac." + name;
				m_behaviorNodeTypes.put(name, clazz);
			}
		}
		
//		for (var clazz : PackageClass.getPackageClasses("org.gof.behaviac")) {
//			var anno = clazz.getAnnotation(RegisterableNode.class);
//			if (anno != null) {
//				String name;
//				if (!Utils.IsNullOrEmpty(anno.value())) {
//					name = anno.value();
//				} else {
//					name = clazz.getSimpleName();
//				}
//				System.out.println(clazz.getName() + ".class,");
//				name = "behaviac." + name;
//				m_behaviorNodeTypes.put(name, clazz);
//			}
//		}
	}

	public BehaviorNode CreateBehaviorNode(String className) {
		try {
			Class<?> type = null;
			if (m_behaviorNodeTypes.containsKey(className)) {
				type = m_behaviorNodeTypes.get(className);
			} else {
				var fullClassName = "behaviac." + className.replace("::", ".");
				type = m_behaviorNodeTypes.get(fullClassName);
			}

			if (type != null) {
				Object p = type.newInstance();
				return (BehaviorNode) p;
			}
			
//			System.err.println("registered nodes============begin============");
//			for(Map.Entry<String, Class<?>> entry : m_behaviorNodeTypes.entrySet()) {
//				System.err.println("\t" + entry.getKey() + "=" + entry.getClass().getName());	
//			}
//			System.err.println("registered nodes============ended============");

			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public void OnBehaviorNodeLoaded(String nodeType, List<property_t> properties) {
		// TODO Auto-generated method stub

	}

	public long GetFrameSinceStartup() {
		// TODO Auto-generated method stub
		Debug.Check(false, "unimpl");
		return 0;
	}

}
