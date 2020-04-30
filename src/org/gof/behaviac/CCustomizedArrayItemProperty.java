package org.gof.behaviac;

import java.util.List;

public class CCustomizedArrayItemProperty extends CProperty implements ICustomizedProperty {
	long _parentId;

	public CCustomizedArrayItemProperty(ClassInfo clazz, long parentId, String parentName) {
		super(parentName, clazz);
		_parentId = parentId;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object GetValue(Agent self, int index) {
		List list = self.GetVariable(_parentId);
		Debug.Check(list != null);
		return list.get(index);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void SetValue(Agent self, Object value, int index) {
		List list = self.GetVariable(_parentId);
		Debug.Check(list != null);
		list.set(index, value);
	}

	@Override
	public IInstantiatedVariable Instantiate() {
		return new CArrayItemVariable(_parentId, this.getName());
	}

}
