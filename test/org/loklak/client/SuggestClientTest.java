package org.loklak.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.tools.JsonIO;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * This class is an unit-test for {@link SuggestClient}.
 */
public class SuggestClientTest {

    private final static Class SUGGEST_CLIENT_CLASS = SuggestClient.class;
    private final static String HOST_SERVER_URL = "https://api.loklak.org";
    private final static String SEARCH_QUERY = "fossasia";
    // names of fields in SuggestClient class
    private final static String[] SUGGEST_API_PARAMS = {
            "SUGGEST_API",
            "PARAM_MINIFIED",
            "PARAM_MINIFIED_VALUE"
    };
    // keys of JSONObejct in reply of suggest API
    private final String JSON_KEY_SEARCH_METADATA = "search_metadata";
    private final String JSON_KEY_QUERIES = "queries";
    // SEARCH_METADATA JSONObject keys
    private final String SM_KEY_COUNT = "count";
    private final String SM_KEY_HITS = "hits";
    private final String SM_KEY_QUERY = "query";
    private final String SM_KEY_ORDER = "order";
    private final String SM_KEY_ORDER_BY = "orderby";
    private final String SM_KEY_CLIENT = "client";
    // QUERY JSONObject keys
    private final String QUERY_KEY_QUERY = "query";
    private final String QUERY_KEY_QUERY_COUNT = "query_count";
    private final String QUERY_KEY_MSG_PERIOD = "message_period";
    private final String QUERY_KEY_SOURCE_TYPE = "source_type";
    private final String QUERY_KEY_RET_LAST = "retrieval_last";
    private final String QUERY_KEY_MSG_PER_DAY = "messages_per_day";
    private final String QUERY_KEY_QUERY_LENGTH = "query_length";
    private final String QUERY_KEY_TIMEZONE_OFFSET = "timezoneOffset";
    private final String QUERY_KEY_RET_NEXT = "retrieval_next";
    private final String QUERY_KEY_SCORE_RET = "score_retrieval";
    private final String QUERY_KEY_QUERY_LAST = "query_last";
    private final String QUERY_KEY_EXPECTED_NEXT = "expected_next";
    private final String QUERY_KEY_SCORE_SUGGEST = "score_suggest";
    private final String QUERY_KEY_RET_COUNT = "retrieval_count";
    private final String QUERY_KEY_QUERY_FIRST = "query_first";

    private static List<String> sSuggestApiParamValues = new ArrayList<>();
    private static String sSuggestApiUrl;

    /**
     * Creates the required url to send API request.
     */
    @BeforeClass
    public static void createSuggestApiUrl() {
        try {
            Field[] fields = SUGGEST_CLIENT_CLASS.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                for (String param : SUGGEST_API_PARAMS) {
                    if (fieldName.equals(param)) {
                        field.setAccessible(true);
                        sSuggestApiParamValues.add(field.get(fieldName).toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            fail("Suggest API field doesn't exist.");
        }
        sSuggestApiUrl = HOST_SERVER_URL
                + sSuggestApiParamValues.get(0) + SEARCH_QUERY // param: base API url and query
                + sSuggestApiParamValues.get(1) + sSuggestApiParamValues.get(2); // param: minified
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testSuggest() {
        JSONObject jsonObject = null;
        try {
            jsonObject = JsonIO.loadJson(sSuggestApiUrl);
        } catch (IOException | JSONException e) {
            if (e instanceof JSONException) {
                fail("JSON parsing unsuccessful.");
            }
            return;
        }

        assertTrue(jsonObject.has(JSON_KEY_SEARCH_METADATA));
        assertTrue(jsonObject.has(JSON_KEY_QUERIES));

        JSONObject jsonMetadataObject = jsonObject.getJSONObject(JSON_KEY_SEARCH_METADATA);
        assertTrue(jsonMetadataObject.has(SM_KEY_COUNT));
        assertTrue(jsonMetadataObject.has(SM_KEY_HITS));
        assertTrue(jsonMetadataObject.has(SM_KEY_QUERY));
        assertTrue(jsonMetadataObject.has(SM_KEY_ORDER));
        assertTrue(jsonMetadataObject.has(SM_KEY_ORDER_BY));
        assertTrue(jsonMetadataObject.has(SM_KEY_CLIENT));

        JSONArray jsonQueryArray = jsonObject.getJSONArray(JSON_KEY_QUERIES);
        if (jsonQueryArray.length() > 0) {
            JSONObject queryObject = jsonQueryArray.getJSONObject(0);
            assertTrue(queryObject.has(QUERY_KEY_QUERY));
            assertTrue(queryObject.has(QUERY_KEY_QUERY_COUNT));
            assertTrue(queryObject.has(QUERY_KEY_MSG_PERIOD));
            assertTrue(queryObject.has(QUERY_KEY_SOURCE_TYPE));
            assertTrue(queryObject.has(QUERY_KEY_RET_LAST));
            assertTrue(queryObject.has(QUERY_KEY_MSG_PER_DAY));
            assertTrue(queryObject.has(QUERY_KEY_QUERY_LENGTH));
            assertTrue(queryObject.has(QUERY_KEY_TIMEZONE_OFFSET));
            assertTrue(queryObject.has(QUERY_KEY_RET_NEXT));
            assertTrue(queryObject.has(QUERY_KEY_SCORE_RET));
            assertTrue(queryObject.has(QUERY_KEY_QUERY_LAST));
            assertTrue(queryObject.has(QUERY_KEY_EXPECTED_NEXT));
            assertTrue(queryObject.has(QUERY_KEY_SCORE_SUGGEST));
            assertTrue(queryObject.has(QUERY_KEY_RET_COUNT));
            assertTrue(queryObject.has(QUERY_KEY_QUERY_FIRST));
        }
    }
}
