package org.loklak.client;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import static org.loklak.tools.JsonIO.loadJson;
import static org.loklak.tools.JsonIO.pushJson;

public class APIGenerator {

    // to restrict instantiation of class
    private APIGenerator() {
    }

    private static class ApiInvocationHandler implements InvocationHandler {

        private String mBaseUrl;

        public ApiInvocationHandler(String baseUrl) {
            this.mBaseUrl = baseUrl;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] values) throws Throwable {
            Parameter[] params = method.getParameters();
            Object[] paramValues = values;

            /*
            format of annotation name:
            @packageWhereAnnotationIsDeclared$AnnotationName()
            Example: @org.loklak.client.LoklakAPI$GET()
            */
            Annotation annotation = method.getAnnotations()[0];
            String annotationName = annotation.toString().toLowerCase();

            String apiUrl = createGetRequestUrl(mBaseUrl, method.getName(),
                    params, paramValues);
            if (annotationName.contains("get")) { // GET REQUEST
                return loadJson(apiUrl);
            } else { // POST REQUEST
                JSONObject jsonObjectToPush = (JSONObject) paramValues[0];
                String postRequestUrl = createPostRequestUrl(mBaseUrl, method.getName());
                return pushJson(postRequestUrl, params[0].getName(), jsonObjectToPush);
            }
        }
    }

    /**
     * Implements the interface that defines loklak API endpoints using <code>Proxy</code>.
     * @param service Interface that defines API methods, here LoklakAPI.class
     * @param baseUrl Base url for loklak_server
     * @param <T> Type of interface defining API endpoints
     * @return
     */
    public static <T> T createApiMethods(Class<T> service, final String baseUrl) {
        ApiInvocationHandler apiInvocationHandler = new ApiInvocationHandler(baseUrl);
        return (T) Proxy.newProxyInstance(
                service.getClassLoader(), new Class<?>[]{service}, apiInvocationHandler);
    }

    private static String createGetRequestUrl(
            String baseUrl, String methodName, Parameter[] params, Object[] paramValues) {
        String apiEndpointUrl = baseUrl.replaceAll("/+$", "") + "/api/" + methodName + ".json";
        StringBuilder url = new StringBuilder(apiEndpointUrl);
        if (params.length > 0) {
            String queryParamAndVal = "?" + params[0].getName() + "=" + paramValues[0];
            url.append(queryParamAndVal);
            for (int i = 1; i < params.length; i++) {
                String paramAndVal = "&" + params[i].getName()
                        + "=" + String.valueOf(paramValues[i]);
                url.append(paramAndVal);
            }
        }
        return url.toString();
    }

    private static String createPostRequestUrl(String baseUrl, String methodName) {
        baseUrl = baseUrl.replaceAll("/+$", "");
        return baseUrl + "/api/" + methodName + ".json";
    }

    // uncomment following to test
    /*public static void main(String[] argv) {
        String baseUrl = "https://api.loklak.org";
        LoklakAPI loklakAPI = createApiMethods(LoklakAPI.class, baseUrl);

        System.out.println(loklakAPI.search("FOSSAsia").toString());
        System.out.println(loklakAPI.suggest("linux", 10).toString());
        System.out.println(loklakAPI.apps());

        JSONObject jsonObject = loklakAPI.search("Linux");
        JSONObject replyOfPush = loklakAPI.push(jsonObject);
        System.out.println("Reply of Push: " + replyOfPush.toString());
    }*/
}
