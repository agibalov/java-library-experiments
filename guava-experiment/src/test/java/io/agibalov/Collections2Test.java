package io.agibalov;

import com.google.common.collect.Collections2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Collections2Test {
    @Test
    public void canGetAllPermutations() {
        Collection<List<Integer>> permutations = Collections2.permutations(Arrays.asList(1, 2, 3));
        assertEquals(6, permutations.size());

        assertTrue(permutations.contains(Arrays.asList(1, 2, 3)));
        assertTrue(permutations.contains(Arrays.asList(1, 3, 2)));
        assertTrue(permutations.contains(Arrays.asList(2, 1, 3)));
        assertTrue(permutations.contains(Arrays.asList(2, 3, 1)));
        assertTrue(permutations.contains(Arrays.asList(3, 1, 2)));
        assertTrue(permutations.contains(Arrays.asList(3, 2, 1)));
    }
}
