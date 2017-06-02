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

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.loklak.Constants.HOST_SERVER_URL;
import static org.loklak.Utility.hasKey;

/**
 * This class provides unit-test for {@link SearchClient}.
 */
public class SearchClientTest {

    private final static Class SEARCH_CLIENT_CLASS = SearchClient.class;
    private final static String SEARCH_QUERY = "fossasia";
    // names of fields in SearchClient class
    private final static String[] SEARCH_API_PARAMS = {
            "SEARCH_API",
            "PARAM_TIMEZONE_OFFSET",
            "PARAM_MINIFIED",
            "PARAM_MINIFIED_VALUE"
    };
    // keys of JSONObejct in reply of search API
    private final String JSON_KEY_SEARCH_METADATA = "search_metadata";
    private final String JSON_KEY_STATUSES = "statuses";
    private final String JSON_KEY_AGGREGATIONS = "aggregations";
    // SEARCH_METADATA JSONObject keys
    private final String SM_KEY_PERIOD = "period";
    private final String SM_KEY_COUNT_BACKEND = "count_backend";
    private final String SM_KEY_START_RECORD = "startRecord";
    private final String SM_KEY_QUERY = "query";
    private final String SM_KEY_SCRAPER_INFO = "scraperInfo";
    private final String SM_KEY_COUNT = "count";
    private final String SM_KEY_COUNT_TWITTER_ALL = "count_twitter_all";
    private final String SM_KEY_SERVICE_REDUCTION = "servicereduction";
    private final String SM_KEY_COUNT_TWITTER_NEW = "count_twitter_new";
    private final String SM_KEY_INDEX = "index";
    private final String SM_KEY_CACHE_HITS = "cache_hits";
    private final String SM_KEY_MAXIMUM_RECORDS = "maximumRecords";
    private final String SM_KEY_HITS = "hits";
    private final String SM_KEY_CLIENT = "client";
    private final String SM_KEY_TIME = "time";
    // STATUS JSONObject key
    private final String STATUS_KEY_TIMESTAMP = "timestamp";
    private final String STATUS_KEY_SCREEN_NAME = "screen_name";
    private final String STATUS_KEY_LINK = "link";
    private final String STATUS_KEY_USER = "user";
    // USER JSONObject key
    private final String USER_KEY_SCREEN_NAME = "screen_name";
    private final String USER_KEY_USER_ID = "user_id";
    private final String USER_KEY_NAME = "name";
    private final String USER_KEY_PROFILE_IMAGE_URL = "profile_image_url_https";
    private final String USER_KEY_APPEARANCE_FIRST = "appearance_first";
    private final String USER_KEY_APPEARANCE_LAST = "appearance_latest";

    // values are stored in the same order of params above
    private static List<String> sSearchApiParamValues = new ArrayList<>();
    private static String sSearchApiUrl;

    /**
     * Creates the required url to send API request.
     */
    @BeforeClass
    public static void createSearchApiUrl() {
        try {
            Field[] fields = SEARCH_CLIENT_CLASS.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                for (String param : SEARCH_API_PARAMS) {
                    if (fieldName.equals(param)) {
                        field.setAccessible(true);
                        sSearchApiParamValues.add(field.get(fieldName).toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            fail("Search API fields doesn't exist.");
        }

        sSearchApiUrl = HOST_SERVER_URL
                + sSearchApiParamValues.get(0) + SEARCH_QUERY // param: base API url and query
                + sSearchApiParamValues.get(1) + Integer.toString(-330) // param: timezoneOffset
                + sSearchApiParamValues.get(2) + sSearchApiParamValues.get(3); // param: minified
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testSearch() {
        JSONObject jsonObject = null;
        try {
            jsonObject = JsonIO.loadJson(sSearchApiUrl);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            if (e instanceof JSONException) {
                fail("JSON parsing unsuccessful");
            }
            return;
        }

        assertThat(jsonObject, hasKey(JSON_KEY_SEARCH_METADATA));
        assertThat(jsonObject, hasKey(JSON_KEY_STATUSES));
        assertThat(jsonObject, hasKey(JSON_KEY_AGGREGATIONS));

        JSONObject jsonObjectSearchMetadata = jsonObject.getJSONObject(JSON_KEY_SEARCH_METADATA);
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_PERIOD));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_COUNT_BACKEND));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_START_RECORD));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_QUERY));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_SCRAPER_INFO));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_COUNT));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_COUNT_TWITTER_ALL));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_SERVICE_REDUCTION));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_COUNT_TWITTER_NEW));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_INDEX));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_CACHE_HITS));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_MAXIMUM_RECORDS));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_HITS));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_CLIENT));
        assertThat(jsonObjectSearchMetadata, hasKey(SM_KEY_TIME));

        JSONArray jsonArrayStatus = jsonObject.getJSONArray(JSON_KEY_STATUSES);
        if (jsonArrayStatus.length() > 0) {
            JSONObject jsonObjectStatus = jsonArrayStatus.getJSONObject(0);
            assertThat(jsonObjectStatus, hasKey(STATUS_KEY_TIMESTAMP));
            assertThat(jsonObjectStatus, hasKey(STATUS_KEY_SCREEN_NAME));
            assertThat(jsonObjectStatus, hasKey(STATUS_KEY_LINK ));
            assertThat(jsonObjectStatus, hasKey(STATUS_KEY_USER ));

            JSONObject jsonObjectUser = jsonObjectStatus.getJSONObject(STATUS_KEY_USER);
            assertThat(jsonObjectUser, hasKey(USER_KEY_SCREEN_NAME));
            assertThat(jsonObjectUser, hasKey(USER_KEY_USER_ID));
            assertThat(jsonObjectUser, hasKey(USER_KEY_NAME ));
            assertThat(jsonObjectUser, hasKey(USER_KEY_PROFILE_IMAGE_URL));
            assertThat(jsonObjectUser, hasKey(USER_KEY_APPEARANCE_FIRST));
            assertThat(jsonObjectUser, hasKey(USER_KEY_APPEARANCE_LAST));
        }
    }
}
