package org.loklak.tools;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.TimelineStub;
import org.loklak.objects.Timeline;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.loklak.Constants.*;

/**
 *This class provides unit-test for {@link NetworkIO}.
 */
public class NetworkIOTest {

    private static Timeline sTimeline;

    /**
     * Timeline objected is assigned to the return value of <code>search</code> method in
     * {@link org.loklak.harvester.TwitterScraper} class.
     */
    @BeforeClass
    public static void createHelloApiUrlAndTimeline() {
        sTimeline = (new TimelineStub()).getTimeline();
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testLoadString() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(NetworkIO.loadString(HELLO_API_URL).toString());
        } catch (IOException e) {
            return;
        }

        assertTrue(jsonObject.has(JSON_KEY_HELLO_PUBLIC_KEY));
        assertTrue(jsonObject.has(JSON_KEY_HELLO_ALGORITHM_KEY));
        assertTrue(jsonObject.has(JSON_KEY_HELLO_PEER_HASH));
        assertTrue(jsonObject.has(JSON_KEY_HELLO_PEER_HASH_ALGORITHM));
        assertTrue(jsonObject.has(JSON_KEY_HELLO_SESSION));
        assertTrue(jsonObject.has(JSON_KEY_HELLO_STATUS));
    }

    /**
     * Tests by checking whether the returned JSONObject has required keys.
     */
    @Test
    public void testPushString() {
        String pushReply;
        try {
            pushReply = NetworkIO.pushString(
                    PUSH_API_URL, "data", sTimeline.toJSON(false).toString()).toString();
        } catch (IOException | NullPointerException e) {
            return;
        }

        JSONObject jsonObject = new JSONObject(pushReply);
        assertTrue(jsonObject.has(JSON_KEY_PUSH_STATUS));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_MESSAGE));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_RECORDS));
        assertTrue(jsonObject.has(JSON_KEY_PUSH_MPS));
    }
}
