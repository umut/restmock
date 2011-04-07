package org.hoydaa.restmock.client.util;

/**
 * @author Umut Utkan
 */
public class Assert {

    public static void assertNotNull(Object obj, String message) {
        if (null == obj) {
            throw new RuntimeException(message);
        }
    }

    public static void assertTrue(boolean check, String message) {
        assertFalse(!check, message);
    }

    public static void assertFalse(boolean check, String message) {
        if (check) {
            throw new RuntimeException(message);
        }
    }

}
