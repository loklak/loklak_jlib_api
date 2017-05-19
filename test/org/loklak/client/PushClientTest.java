package org.loklak.client;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.TimelineStub;
import org.loklak.harvester.TwitterScraper;
import org.loklak.objects.Timeline;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.loklak.Constants.*;

/**
 * This class provides unit-test for {@link PushClient}.
 */
public class PushClientTest {

    private static Timeline sTimeline = null;

    /**
     * Internally this method uses <code>search</code> method of {@link TwitterScraper} to obtain
     * a {@link Timeline} object. The <code>search</code> method handles IOExceptions by using
     * <code>printStackTrace</code> method. The output of the unit-test may be bloated with
     * stack-trace.
     */
    @BeforeClass
    public static void createTimelineObject() {
        sTimeline = (new TimelineStub()).getTimeline();
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

        assertTrue(jsonObject.has(JSON_KEY_PUSH_STATUS));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_MESSAGE));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_RECORDS));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_MPS));
    }

}
