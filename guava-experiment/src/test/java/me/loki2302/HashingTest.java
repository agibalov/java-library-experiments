package me.loki2302;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashingTest {
    @Test
    public void dummy() {
        HashFunction md5HashFunction = Hashing.md5();
        HashCode hashCode = md5HashFunction.newHasher()
                .putString("loki", Charsets.UTF_8)
                .putInt(2302)
                .hash();

        assertEquals(-6881752532756084124L, hashCode.asLong());
    }
}
