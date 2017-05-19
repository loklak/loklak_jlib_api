package org.loklak;

import org.loklak.harvester.TwitterScraper;
import org.loklak.objects.Timeline;

/**
 * Provides a {@link Timeline} object, so that it can be used to test <code>POST</code> request
 * methods. Timeline object is pushed using <code>PUSH</code> API endpoint of loklak.
 */
public class TimelineStub {

    private final String SEARCH_QUERY = "This is just a testing tweet used for writing " +
            "unit-tests for loklak_jlib_api.";

    private Timeline mTimeline;

    /**
     * Using <code>search</code> method of {@link TwitterScraper} a Timeline object is obtained.
     */
    public TimelineStub() {
        mTimeline = TwitterScraper.search(SEARCH_QUERY, Timeline.Order.CREATED_AT);
    }

    /**
     * @return Timeline object.
     */
    public Timeline getTimeline() {
        return mTimeline;
    }
}
