package org.loklak.client;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.harvester.TwitterScraper;
import org.loklak.objects.Timeline;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * This class is an unit-test for {@link PushClient}.
 */
public class PushClientTest {

    private final static String SEARCH_QUERY = "This is just a testing tweet used for writing " +
            "unit-tests for loklak_jlib_api.";
    private final String HOST_SERVER_URL = "https://api.loklak.org";
    // keys of JSONObject in reply of push API
    private final String JSON_KEY_STATUS = "status";
    private final String JSON_KEY_MESSAGE = "message";
    private final String JSON_KEY_RECORDS = "records";
    private final String JSON_KEY_MPS = "mps";

    private static Timeline sTimeline = null;

    /**
     * Internally this method uses <code>search</code> method of {@link TwitterScraper} to obtain
     * a {@link Timeline} object. The <code>search</code> method handles IOExceptions by using
     * <code>printStackTrace</code> method. The output of the unit-test may be bloated with
     * stack-trace.
     */
    @BeforeClass
    public static void createTimelineObject() {
        sTimeline = TwitterScraper.search(SEARCH_QUERY, Timeline.Order.CREATED_AT);
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testPush() {
        JSONObject jsonObject = null;
        try {
            jsonObject = PushClient.push(HOST_SERVER_URL, sTimeline);
        } catch (IOException | NullPointerException e) {
            // sTimeline object can be null, so Timeline.toJSON method can throw
            // NullPointerException
            return;
        }

        assertTrue(jsonObject.has(JSON_KEY_STATUS));
        assertTrue(jsonObject.has(JSON_KEY_MESSAGE));
        assertTrue(jsonObject.has(JSON_KEY_RECORDS));
        assertTrue(jsonObject.has(JSON_KEY_MPS));
    }

}
