/**
 *  JsonIO
 *  Copyright 17.11.2015 by Michael Peter Christen, @0rb1t3r
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

package org.loklak.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Provides helper methods for sending GET and POST requests.
 */
public class JsonIO {

    // restricts instantiation of JsonIO
    private JsonIO() {}

    /**
     * Facilitates to send GET request API calls
     * @param requestUrl Url to make API calls
     * @return JSONObject from the host API
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject loadJson(String requestUrl) throws JSONException, IOException {
        StringBuilder sb = NetworkIO.loadString(requestUrl);
        if (sb == null || sb.length() == 0)
            return new JSONObject();
        return new JSONObject(sb.toString());
    }

    /**
     * Facilitates to send POST request API calls
     * @param requestURL Url to make API calls
     * @param jsonObjectKey key of JSONObject to be sent
     * @param jsonObject JSONObject to be sent
     * @return JSONObject from the host API
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject pushJson(
            String requestURL,
            String jsonObjectKey,
            JSONObject jsonObject) throws IOException, JSONException {
        StringBuilder sb = NetworkIO.pushString(requestURL, jsonObjectKey, jsonObject.toString());
        return new JSONObject(sb.toString());
    }

}
