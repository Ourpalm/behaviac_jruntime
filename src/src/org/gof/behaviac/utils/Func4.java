package org.gof.behaviac.utils;

@FunctionalInterface
public interface Func4<R, P1, P2, P3, P4> {
	R run(P1 p1, P2 p2, P3 p3, P4 p4);
}