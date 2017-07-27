package org.loklak.tools;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.client.APIGenerator;
import org.loklak.client.LoklakAPI;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.loklak.Constants.*;
import static org.loklak.Utility.hasKey;

/**
 *This class provides unit-test for {@link JsonIO}.
 */
public class JsonIOTest {

    private static JSONObject searchAPIResult;

    /**
     * Result of <code>search</code> API endpoint is obtained, using {@link LoklakAPI} and
     * {@link APIGenerator}.
     */
    @BeforeClass
    public static void getSearchApiResult() {
        LoklakAPI loklakAPI = APIGenerator.createApiMethods(LoklakAPI.class, HOST_SERVER_URL);
        searchAPIResult = loklakAPI.search("android");
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testLoadJson() {
        JSONObject jsonObject;
        try {
            jsonObject = JsonIO.loadJson(HELLO_API_URL);
        } catch (IOException e) {
            return;
        }

        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_ALGORITHM_KEY));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PEER_HASH));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PUBLIC_KEY));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PEER_HASH_ALGORITHM));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_SESSION));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_STATUS));
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testPushJson() {
        JSONObject jsonObject;
        try {
            jsonObject = JsonIO.pushJson(PUSH_API_URL, "data", searchAPIResult);
        } catch (IOException | NullPointerException e) {
            return;
        }

        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_STATUS));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_MESSAGE));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_RECORDS));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_MPS));
    }
}
