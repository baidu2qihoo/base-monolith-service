package com.hugh.base.service;

import com.hugh.base.service.trace.TraceIdUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TraceIdTest {
    @Test
    public void testGenerate() {
        String t = TraceIdUtils.getOrCreate();
        assertNotNull(t);
        assertFalse(t.isEmpty());
    }
}
