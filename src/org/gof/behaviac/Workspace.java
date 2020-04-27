package org.gof.behaviac;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gof.behaviac.node.BehaviorTree;
import org.gof.behaviac.node.BehaviorTreeTask;

public class Workspace implements Closeable {

	public static Workspace Instance = null;
	
	
	public final int EFF_XML = 1;
	public final int EFF_BSON = 2;

	private int fileFormat = EFF_XML;
	private String m_filePath = null;
	private String m_metaFile = null;
	private Map<String, BehaviorTree> m_behaviorTrees = new HashMap<>();

	private static String getDefaultFilePath() {
		return "";
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public boolean Load(String relativePath, boolean bForce) {
		// TODO Auto-generated method stub
		return false;
	}

	public void RecordBTAgentMapping(String relativePath, Agent agent) {
		// TODO Auto-generated method stub
		
	}

	public BehaviorTreeTask CreateBehaviorTreeTask(String relativePath) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean Load(String relativePath) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean IsValidPath(String relativePath) {
		// TODO Auto-generated method stub
		return false;
	}

	public void DestroyBehaviorTreeTask(BehaviorTreeTask task, Agent agent) {
		// TODO Auto-generated method stub
		
	}

	public void UnLoad(String relativePath) {
		// TODO Auto-generated method stub
		
	}

}
