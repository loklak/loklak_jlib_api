/**
 *  PushClient
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

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.loklak.objects.Timeline;
import org.loklak.tools.JsonIO;

/**
 *The PushClient class provides <code>push</code> method which delivers scraped tweets to server
 * hosting loklak_server.
 */
public class PushClient {

    private static final String PUSH_API = "/api/push.json";
    private static final String JSON_DATA_KEY = "data";

    // restricts instantiation of PushClient
    private PushClient() {}

    /**
     * transmits the {@link Timeline} to server hosting loklak_server
     * @param hostServerUrl Url of server hosting loklak_server
     * @param timeline contains the details of scraped tweets
     * @return the JSONObject from the host API if the transfer was successful or null otherwise
     * @throws IOException 
     * @throws JSONException 
     */
    public static JSONObject push(String hostServerUrl, Timeline timeline)
            throws JSONException, IOException {
        // transmit the timeline
        if (hostServerUrl.endsWith("/")) {
            hostServerUrl = hostServerUrl.substring(0, hostServerUrl.length() - 1);
        }
        String pushApiUrl = hostServerUrl + PUSH_API;
        return JsonIO.pushJson(pushApiUrl, JSON_DATA_KEY, timeline.toJSON(false));
    }
}
