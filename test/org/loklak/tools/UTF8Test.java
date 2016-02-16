package org.loklak.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTF8Test {
    
    @Test
    public void string2byte() throws Exception {
        String test = Long.toString(System.currentTimeMillis());
        byte[] b = UTF8.getBytes(test);
        String t = UTF8.String(b);
        assertEquals(test, t);
    }
    
    public void byte2string() throws Exception {
        byte[] test = Long.toString(System.currentTimeMillis()).getBytes();
        String t = UTF8.String(test);
        byte[] b = UTF8.getBytes(t);
        assertEquals(test, b);
    }

}
