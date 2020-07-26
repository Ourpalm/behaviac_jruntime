package org.gof.behaviac;

public enum EFileFormat {
	EFF_xml(1), // specify to use xml
;
	
	private int value;

	EFileFormat(int v) {
		value = v;
	}

	public int getValue() {
		return this.value;
	}
}
