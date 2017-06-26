package org.loklak.client;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class APIGeneratorTest {

    private final static String LOKLAK_BASE_URL = "https://api.loklak.org/";
    private final static String[] API_GENERATOR_METHOD_NAMES = {
            "createGetRequestUrl",
            "createPostRequestUrl"
    };
    private final static String URL_CHECK_ASSERTION_MESSAGE = "Expected url doesn't match with " +
            "actual url.";

    private static List<Method> sAPIGeneratorMethods = new ArrayList<>();
    private static LoklakAPI sLoklakAPI;

    @BeforeClass
    public static void getPrivateMethod() {
        Method[] methods = APIGenerator.class.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            for (String selectedMethodName : API_GENERATOR_METHOD_NAMES) {
                if (selectedMethodName.equals(methodName)) {
                    method.setAccessible(true);
                    sAPIGeneratorMethods.add(method);
                }
            }
        }

        sLoklakAPI = APIGenerator.createApiMethods(LoklakAPI.class, LOKLAK_BASE_URL);
    }

    @Test
    public void testCreateApiMethods() {
        int methodsInLoklakAPI = LoklakAPI.class.getDeclaredMethods().length;

        // -3 because, toString, hashCode and equals method are also implemented,
        int implementedMethods = sLoklakAPI.getClass().getDeclaredMethods().length - 3;
        assertEquals(implementedMethods, methodsInLoklakAPI);
    }

    @Test
    public void testNonParametricGetMethods() {
        Method createGetRequestUrl = sAPIGeneratorMethods.get(0);

        try {
            String methodName, actualUrl, expectedUrl;

            expectedUrl = "https://api.loklak.org/api/peers.json";
            methodName = "peers";
            actualUrl = (String) createGetRequestUrl.invoke(null, LOKLAK_BASE_URL,
                    methodName, new Parameter[]{}, new Object[]{});
            assertEquals(URL_CHECK_ASSERTION_MESSAGE, expectedUrl, actualUrl);

            expectedUrl = "https://api.loklak.org/api/status.json";
            methodName = "status";
            actualUrl = (String) createGetRequestUrl.invoke(null, LOKLAK_BASE_URL,
                    methodName, new Parameter[]{}, new Object[]{});
            assertEquals(URL_CHECK_ASSERTION_MESSAGE, expectedUrl, actualUrl);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("\"createGetRequestUrl\" can't be invoked properly.");
        }
    }

    @Test
    public void testParametricGetMethods() {
        Method createGetRequestUrl = sAPIGeneratorMethods.get(0);

        try {
            String actualUrl, expectedUrl;
            Method method;
            Parameter[] parameters;

            // one parameter
            expectedUrl = "https://api.loklak.org/api/search.json?q=Fossasia";
            method = LoklakAPI.class.getMethod("search", String.class);
            parameters = method.getParameters();
            actualUrl = (String) createGetRequestUrl.invoke(null, LOKLAK_BASE_URL,
                    method.getName(), parameters, new Object[]{"Fossasia"});
            assertEquals(URL_CHECK_ASSERTION_MESSAGE, expectedUrl, actualUrl);

            // two parameter
            expectedUrl = "https://api.loklak.org/api/suggest.json?q=FossAsia&count=10";
            method = LoklakAPI.class.getMethod("suggest", String.class, int.class);
            parameters = method.getParameters();
            actualUrl = (String) createGetRequestUrl.invoke(null, LOKLAK_BASE_URL,
                    method.getName(), parameters, new Object[]{"FossAsia", 10});
            assertEquals(URL_CHECK_ASSERTION_MESSAGE, expectedUrl, actualUrl);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("\"createGetRequestUrl\" can't be invoked properly.");
        } catch (NoSuchMethodException e) {
            fail("Method doesn't exist.");
        }
    }

    @Test
    public void testPostMethods() {
        Method createPostRequestUrl = sAPIGeneratorMethods.get(1);

        try {
            String expectedUrl, actualUrl;

            expectedUrl = "https://api.loklak.org/api/push.json";
            Method method = sLoklakAPI.getClass().getMethod("push", JSONObject.class);
            actualUrl = (String) createPostRequestUrl.invoke(null, LOKLAK_BASE_URL,
                    method.getName());
            assertEquals(URL_CHECK_ASSERTION_MESSAGE, expectedUrl, actualUrl);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("\"createPostRequestUrl\" can't be invoked properly");
        } catch (NoSuchMethodException e) {
            fail("Method doesn't exist.");
        }
    }
}
