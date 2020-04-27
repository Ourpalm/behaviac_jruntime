package org.gof.behaviac;

public class CArrayItemVariable implements IInstantiatedVariable {
	String _name;
	long _parentId;

	public CArrayItemVariable(long parentId, String name) {
		_parentId = parentId;
		_name = name;
	}

	public Object GetValue(Agent self, int index) {
		IInstantiatedVariable v = self.GetInstantiatedVariable(this._parentId);
		if (v != null) {
			return v.GetValueObject(self, index);
		}
		return null;
	}

	@Override
	public Object GetValueObject(Agent self) {
		Debug.Check(false);
		return null;
	}

	@Override
	public Object GetValueObject(Agent self, int index) {
		return GetValue(self, index);
	}

	@Override
	public void SetValueFromString(Agent self, String valueStr) {
		Debug.Check(false);
	}

	@Override
	public void SetValue(Agent self, Object value) {
		Debug.Check(false);
	}

	@Override
	public void SetValue(Agent self, Object value, int index) {
		IInstantiatedVariable v = self.GetInstantiatedVariable(this._parentId);
		if (v != null) {
			v.SetValue(self, value, index);
			return;
		}
		Debug.Check(false);
	}

	@Override
	public String GetName() {
		return this._name;
	}

	@Override
	public void Log(Agent self) {
		// TODO Auto-generated method stub

	}

	@Override
	public IInstantiatedVariable Clone() {
		 CArrayItemVariable p = new CArrayItemVariable(this._parentId, this._name);

         return p;
	}

	@Override
	public void CopyTo(Agent pAgent) {
		 Debug.Check(false);
	}

}
