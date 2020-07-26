package org.gof.behaviac.utils;

@FunctionalInterface
public interface Func3<R, P1, P2, P3> {
	R run(P1 p1, P2 p2, P3 p3);
}
