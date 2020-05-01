package org.gof.behaviac;

import org.gof.behaviac.members.CCustomizedArrayItemProperty;
import org.gof.behaviac.members.CCustomizedProperty;
import org.gof.behaviac.members.CInstanceConst;
import org.gof.behaviac.members.CInstanceCustomizedProperty;
import org.gof.behaviac.members.ICustomizedProperty;
import org.gof.behaviac.members.IInstanceMember;

public class CommonTypeCreator extends TypeCreator {
	private ClassInfo _clazz;

	public CommonTypeCreator(ClassInfo clazz) {
		_clazz = clazz;
	}

	@Override
	public ICustomizedProperty CreateArrayItemProperty(long parentId, String parentName) {
		return new CCustomizedArrayItemProperty(_clazz, parentId, parentName);
	}

	@Override
	public ICustomizedProperty CreateCustomizedArrayItemProperty(long id, String name) {
		return new CCustomizedArrayItemProperty(_clazz, id, name);
	}

	@Override
	public ICustomizedProperty CreateCustomizedProperty(long id, String name, String valueStr) {
		return new CCustomizedProperty(_clazz, id, name, valueStr);
	}

	@Override
	public IInstanceMember CreateInstanceConst(String typeName, String valueStr) {
		return new CInstanceConst(_clazz, typeName, valueStr);
	}

	@Override
	public IInstanceMember CreateInstanceProperty(String instance, IInstanceMember indexMember, long varId) {
		return new CInstanceCustomizedProperty(this._clazz, instance, indexMember, varId);
	}

	@Override
	public ICustomizedProperty CreateProperty(long propId, String propName, String valueStr) {
		return new CCustomizedProperty(_clazz, propId, propName, valueStr);
	}
}
