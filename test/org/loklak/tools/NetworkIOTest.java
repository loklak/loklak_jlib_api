package org.loklak.tools;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loklak.TimelineStub;
import org.loklak.objects.Timeline;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.loklak.Constants.*;
import static org.loklak.Utility.hasKey;

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

        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PUBLIC_KEY));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_ALGORITHM_KEY));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PEER_HASH));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_PEER_HASH_ALGORITHM));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_SESSION));
        assertThat(jsonObject, hasKey(JSON_KEY_HELLO_STATUS));
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
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_STATUS));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_MESSAGE));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_RECORDS));
        assertThat(jsonObject, hasKey(JSON_KEY_PUSH_MPS));
    }
}
