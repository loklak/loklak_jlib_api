/**
 *  SuggestClient
 *  Copyright 13.11.2015 by Michael Peter Christen, @0rb1t3r
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
import org.json.JSONException;
import org.json.JSONObject;
import org.loklak.objects.QueryEntry;
import org.loklak.objects.ResultList;
import org.loklak.tools.JsonIO;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * The SuggestClient class provides <code>suggest</code> method which delivers search suggestions
 * for tweets.
 */
public class SuggestClient {

    private static final String SUGGEST_API = "/api/suggest.json?q=";
    private static final String ENCODING = "UTF-8";
    // suggest api parameter fields
    private static final String PARAM_TIMEZONE_OFFSET = "&timezoneOffset=";
    private static final String PARAM_COUNT = "&count=";
    private static final String PARAM_SOURCE = "&source=";
    private static final String PARAM_MINIFIED = "&minified=";
    private static final String PARAM_ORDER = "&order=";
    private static final String PARAM_ORDER_BY = "&orderby=";
    private static final String PARAM_SINCE = "&since=";
    private static final String PARAM_UNTIL = "&until=";
    private static final String PARAM_SELECT_BY = "&selectby=";
    private static final String PARAM_RANDOM = "&random=";
    // suggest API parameter values
    private static final String PARAM_SOURCE_VALUE = "all";
    private static final String PARAM_MINIFIED_VALUE = "true";
    // key of JSONObjects
    private static final String JSON_KEY_SEARCH_METADATA = "search_metadata";
    private static final String JSON_KEY_QUERIES = "queries";
    private static final String JSON_KEY_HITS = "hits";

    // restricts instantiation of SuggestClient
    private SuggestClient() {}

    // TODO: documentation of selectBy and random
    /**
     * Returns a {@link ResultList} of type {@link QueryEntry} containing search suggestions for
     * tweets matching <code>query</code>
     * @param hostServerUrl Url of server hosting loklak_server
     * @param query query string
     * @param source tweet searching source, possible values: cache, twitter, all (default)
     * @param count maximum number of search result for the query
     * @param order possible values: desc, asc; default: desc
     * @param orderBy a field name of the query index schema, i.e. retrieval_next or query_count
     * @param timezoneOffset offset of a timezone in minutes
     * @param since left bound of a query time
     * @param until right bound of a query time
     * @param selectBy
     * @param random
     * @return search suggestions as ResultList<QueryEntry>
     * @throws JSONException
     * @throws IOException
     */
    public static ResultList<QueryEntry> suggest(
            final String hostServerUrl,
            final String query,
            final String source,
            final int count,
            final String order,
            final String orderBy,
            final int timezoneOffset,
            final String since,
            final String until,
            final String selectBy,
            final int random) throws JSONException, IOException {
        ResultList<QueryEntry>  resultList = new ResultList<>();
        String suggestApiUrl = hostServerUrl
                + SUGGEST_API
                + URLEncoder.encode(query.replace(' ', '+'), ENCODING)
                + PARAM_TIMEZONE_OFFSET + timezoneOffset
                + PARAM_COUNT + count
                + PARAM_SOURCE + (source == null ? PARAM_SOURCE_VALUE : source)
                + (order == null ? "" : (PARAM_ORDER + order))
                + (orderBy == null ? "" : (PARAM_ORDER_BY + orderBy))
                + (since == null ? "" : (PARAM_SINCE + since))
                + (until == null ? "" : (PARAM_UNTIL + until))
                + (selectBy == null ? "" : (PARAM_SELECT_BY + selectBy))
                + (random < 0 ? "" : (PARAM_RANDOM + random))
                + PARAM_MINIFIED + PARAM_MINIFIED_VALUE;

        JSONObject json = JsonIO.loadJson(suggestApiUrl);

        if (json.length() == 0) {
            return resultList;
        }

        JSONArray queries = json.getJSONArray(JSON_KEY_QUERIES);
        if (queries != null) {
            for (int i = 0; i < queries.length(); i++) {
                JSONObject queryJsonObject = queries.getJSONObject(i);
                if (queryJsonObject == null) continue;
                QueryEntry queryEntry = new QueryEntry(queryJsonObject);
                resultList.add(queryEntry);
            }
        }

        JSONObject metadata = json.getJSONObject(JSON_KEY_SEARCH_METADATA);
        if (metadata != null) {
            long hits = metadata.getLong(JSON_KEY_HITS);
            resultList.setHits(hits);
        }
        return resultList;
    }

}
