package org.gof.behaviac;

import java.util.Random;

public class RandomGenerator {
	private static RandomGenerator Instance = new RandomGenerator();

	private static RandomGenerator GetInstance() {
		return RandomGenerator.Instance;
	}

	public synchronized static float Random() {
		return GetInstance().GetRandom();
	}

	// [0, 1)
	public float GetRandom() {
		float r = random.nextFloat();
		Debug.Check(r >= 0.0f && r < 1.0f);
		return r;
	}

	private Random random = new Random();
}
