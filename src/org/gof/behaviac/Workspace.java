package org.gof.behaviac;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gof.behaviac.behaviortree.BehaviorTree;


public class Workspace implements Closeable {

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

}
