/**
 *  SearchClient
 *  Copyright 22.02.2015 by Michael Peter Christen, @0rb1t3r
 *  This class is the android version from the original file,
 *  taken from the loklak_server project. It may be slightly different.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package org.loklak.client;

import org.json.JSONArray;
import org.json.JSONObject;
import org.loklak.objects.MessageEntry;
import org.loklak.objects.Timeline;
import org.loklak.objects.UserEntry;
import org.loklak.tools.JsonIO;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * The SearchClient class provides <code>search</code> method which delivers scraped tweets
 * stored in loklak_server.
 */
public class SearchClient {

    private static final String SEARCH_API = "/api/search.json?q=";
    private static final String ENCODING = "UTF-8";
    // search API parameter fields
    private static final String PARAM_TIMEZONE_OFFSET = "&timezoneOffset=";
    private static final String PARAM_MAXIMUM_RECORDS = "&maximumRecords=";
    private static final String PARAM_SOURCE = "&source=";
    private static final String PARAM_MINIFIED = "&minified=";
    private static final String PARAM_TIMEOUT = "&timeout=";
    // search API parameter values
    private static final String PARAM_SOURCE_VALUE = "all";
    private static final String PARAM_MINIFIED_VALUE = "true";
    // key of JSONObjects
    private static final String JSON_KEY_STATUSES = "statuses";
    private static final String JSON_KEY_USER = "user";
    private static final String JSON_KEY_SEARCH_METADATA = "search_metadata";
    private static final String JSON_KEY_HITS = "hits";
    private static final String JSON_KEY_SCRAPER_INFO = "scraperInfo";

    // restricts instantiation of SearchClient
    private SearchClient() {}

    // TODO: documentation of order
    // TODO: use enumeration for source parameter (api users can't provide vague values)
    /**
     *Returns a {@link Timeline} object containing the search results.
     * @param hostServerUrl Url of server hosting loklak_server
     * @param query query string
     * @param order
     * @param source tweet searching source, possible values: cache, twitter, all (default)
     * @param count maximum number of search result for the query
     * @param timezoneOffset offset of a timezone in minutes
     * @param timeout maximum waiting time of results in milliseconds
     * @throws IOException
     * @return Timeline object containing details and tweets of search result
     */
    public static Timeline search(final String hostServerUrl,
                                  final String query,
                                  final Timeline.Order order,
                                  final String source,
                                  final int count,
                                  final int timezoneOffset,
                                  final long timeout) throws IOException {
        Timeline timeline = new Timeline(order);
        String searchApiUrl = hostServerUrl
                + SEARCH_API
                + URLEncoder.encode(query.replace(' ', '+'), ENCODING)
                + PARAM_TIMEZONE_OFFSET + timezoneOffset
                + PARAM_MAXIMUM_RECORDS + count
                + PARAM_SOURCE + (source == null ? PARAM_SOURCE_VALUE : source)
                + PARAM_MINIFIED + PARAM_MINIFIED_VALUE
                + PARAM_TIMEOUT + timeout;

        JSONObject json = JsonIO.loadJson(searchApiUrl);

        if (json.length() == 0)
            return timeline;

        JSONArray statuses = json.getJSONArray(JSON_KEY_STATUSES);
        if (statuses != null) {
            for (int i = 0; i < statuses.length(); i++) {
                JSONObject tweet = statuses.getJSONObject(i);
                JSONObject user = tweet.getJSONObject(JSON_KEY_USER);
                if (user == null)
                    continue;
                tweet.remove(JSON_KEY_USER);
                UserEntry userEntry = new UserEntry(user);
                MessageEntry messageEntry = new MessageEntry(tweet);
                timeline.add(messageEntry, userEntry);
            }
        }

        if (json.has(JSON_KEY_SEARCH_METADATA)) {
            JSONObject metadata = json.getJSONObject(JSON_KEY_SEARCH_METADATA);
            if (metadata.has(JSON_KEY_HITS)) {
                timeline.setHits((Integer) metadata.get(JSON_KEY_HITS));
            }
            if (metadata.has(JSON_KEY_SCRAPER_INFO)) {
                String scraperInfo = (String) metadata.get(JSON_KEY_SCRAPER_INFO);
                timeline.setScraperInfo(scraperInfo);
            }
        }
        //System.out.println(parser.text());
        return timeline;
    }

}
