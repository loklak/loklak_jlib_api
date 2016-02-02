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

public class JsonIO {

    public static JSONObject loadJson(String url) throws JSONException, IOException {
        StringBuilder sb = NetworkIO.loadString(url);
        if (sb == null || sb.length() == 0) return new JSONObject();
        JSONObject json = null;
        json = new JSONObject(sb.toString());
        return json;
    }
    
    public static JSONObject pushJson(String requestURL, String jsonDataName, JSONObject json_out) throws IOException, JSONException {
        StringBuilder sb = NetworkIO.pushString(requestURL, jsonDataName, json_out.toString());
        JSONObject json_in = new JSONObject(sb.toString());
        return json_in;
    }

}
