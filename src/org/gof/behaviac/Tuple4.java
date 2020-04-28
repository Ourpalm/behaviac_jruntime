package org.gof.behaviac;

public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
	public D value4;

	public Tuple4(A a, B b, C c, D d) {
		super(a, b, c);
		value4 = d;
	}

	public String toString() {
		return "(" + value1 + "," + value2 + "," + value3 + "," + value4 + ")";
	}
}
