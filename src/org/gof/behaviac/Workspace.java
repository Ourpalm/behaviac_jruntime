package org.gof.behaviac;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gof.behaviac.utils.StringUtils;
import org.gof.behaviac.utils.Utils;

public class Workspace implements Closeable {

	public static Workspace Instance = null;

	public final int EFF_XML = 1;
	public final int EFF_BSON = 2;

	private EFileFormat m_fileFormat = EFileFormat.EFF_xml;
	private String m_filePath = null;
	private String m_metaFile = null;
	private Map<String, BehaviorTree> m_behaviortrees = new HashMap<>();
	private Map<String, java.lang.reflect.Method> m_btCreators;
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
		if (m_behaviortrees == null) {
			m_behaviortrees = new HashMap<String, BehaviorTree>();
		}
		return m_behaviortrees;
	}

	private Map<String, java.lang.reflect.Method> GetBTCreators() {
		if (m_btCreators == null) {
			m_btCreators = new HashMap<>();
		}

		return m_btCreators;
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

	public boolean TryInit() {
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

		Debug.Log(String.format("FilePath: %s\n", this.GetFilePath()));
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

		m_behaviortrees.remove(relativePath);
	}

	public void UnLoadAll() {
		m_behaviortrees.clear();
		m_btCreators.clear();
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

	public boolean Load(String relativePath, boolean bForce) {
		Debug.Check(Utils.IsNullOrEmpty(StringUtils.FindExtension(relativePath)), "no extention to specify");
		Debug.Check(this.IsValidPath(relativePath));

		TryInit();

		BehaviorTree pBT = null;

		if (m_behaviortrees.containsKey(relativePath)) {
			if (!bForce)
				return true;
			pBT = m_behaviortrees.get(relativePath);
		}

		var fullPath = Utils.Combine(this.GetFilePath(), relativePath);
		fullPath = fullPath.replace('\\', '/');

		var ext = ".xml";
		EFileFormat f = this.GetFileFormat();

		var bLoadResult = false;
		var bCleared = false;
		var bNewly = false;

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

		return bLoadResult;
	}

	public boolean Load(String relativePath) {
		return this.Load(relativePath, false);
	}

	public BehaviorTree LoadBehaviorTree(String relativePath) {
		var r = m_behaviortrees.get(relativePath);
		if (r != null)
			return r;
		if (this.Load(relativePath, true)) {
			return m_behaviortrees.get(relativePath);
		}
		return null;
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

		BehaviorTree bt = m_behaviortrees.get(relativePath);

		if (bt == null) {
			if (this.Load(relativePath)) {
				bt = m_behaviortrees.get(relativePath);
			}
		}

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
		for (var clazz : PackageClass.find("org.gof.behaviac")) {
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
