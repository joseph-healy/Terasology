/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.utilities;

import org.junit.Assert;
import org.junit.Test;
import org.terasology.utilities.procedural.FastNoise;
import org.terasology.utilities.random.FastRandom;

/**
 * A simple validity test for {@link FastNoise}
 *
 * @author Martin Steiger
 */
public class FastNoiseTest {

    @Test
    public void distributionTest() {
        FastRandom rng = new FastRandom(0xBEEF);
        FastNoise noiseGen = new FastNoise(0xDEADC0DE);

        int count = 1000000;
        int bucketCount = 20;
        int[] buckets = new int[bucketCount];

        for (int i = 0; i < count; i++) {
            float posX = rng.nextFloat() * 100f;
            float posY = rng.nextFloat() * 100f;
            float posZ = rng.nextFloat() * 100f;

            float noise = noiseGen.noise(posX, posY, posZ);
            int idx = (int) (noise * bucketCount);
            if (idx == bucketCount) {
                idx = bucketCount - 1;
            }
            buckets[idx]++;
        }

        float avg = count / bucketCount;

        for (int i = 0; i < bucketCount; i++) {
            float val = Math.abs((buckets[i] - avg) / avg);
            // less than 5% deviation from the expected average
            Assert.assertTrue(val < 0.05);
        }
    }

    @Test
    public void resolutionTest() {
        FastRandom rng = new FastRandom(0xBEEF);
        FastNoise noiseGen = new FastNoise(0xDEADC0DE);

        int count = 1000000;

        for (int i = 0; i < count; i++) {
            float posX = rng.nextFloat() * 100f;
            float posY = rng.nextFloat() * 100f;
            float posZ = rng.nextFloat() * 100f;

            float noise = noiseGen.noise(posX, posY, posZ);
            if (noise > 0 && noise < 0.00005) {
                return;
            }
        }

        Assert.fail();
    }
}