package org.gof.behaviac;

public class ClassInfo {
	private boolean isList = false;
	private Class<?> elemClass = null;

	public ClassInfo(Class<?> elementClass) {
		this.elemClass = elementClass;
	}

	public ClassInfo(boolean isList, Class<?> elementClass) {
		this.isList = isList;
		this.elemClass = elementClass;
	}

	public boolean isList() {
		return isList;
	}

	public Class<?> getElemClass() {
		return elemClass;
	}

	@Override
	public String toString() {
		return String.format("(%b,%s)", this.isList, this.elemClass.getName());
	}
}
