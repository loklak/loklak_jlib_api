/**
 *  NetworkIO
 *  Copyright 02.02.2016 by Michael Peter Christen, @0rb1t3r
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 *NetworkIO class provides methods for sending GET and POST requests, which is used by
 * {@link JsonIO} internally.
 */
public class NetworkIO {

    private final static String CHARSET = "UTF-8";
    private final static String CRLF = "\r\n";
    private final static String REQUEST_METHOD = "GET";
    private final static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36";
    // request properties
    private final static String REQUEST_PROPERTY_USER_AGENT = "User-Agent";
    private final static String REQUEST_PROPERTY_CONTENT_TYPE = "Content-Type";
    // header parameter types
    private final static String HEADER_PARAM_CONTENT_DISPOSITION = "Content-Disposition: " +
            "form-data; name=\"";
    private final static String HEADER_PARAM_CONTENT_TYPE = "Content-Type: text/plain; charset=";

    /**
     *Sends GET request to host API.
     * @param url Url for GET request
     * @return Server reply as String (in JSON format) from host API
     * @throws IOException
     */
    public static StringBuilder loadString(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        URLConnection uc = (new URL(url)).openConnection();
        HttpURLConnection connection = url.startsWith("https") ?
                (HttpsURLConnection) uc : (HttpURLConnection) uc;
        try {
            connection.setReadTimeout(6000);
            connection.setConnectTimeout(6000);
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setDoInput(true);
            connection.setRequestProperty(REQUEST_PROPERTY_USER_AGENT, USER_AGENT);
            sb = load(connection);
        } catch (IOException e) {
            throw e;
        } finally {
            connection.disconnect();
        }
        return sb;
    }

    private static StringBuilder load(HttpURLConnection connection) throws IOException {
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append('\n');
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) br.close();
            connection.disconnect();
        }
        return sb;
    }

    /**
     *Sends POST request to host API.
     * @param requestURL Url for POST request
     * @param jsonDataKey Name of JSONObject's key
     * @param bodyText JSONObject as String
     * @return Server reply as String (in JSON format) from host API
     * @throws IOException
     * @throws JSONException
     */
    public static StringBuilder pushString(String requestURL, String jsonDataKey, String bodyText)
            throws IOException, JSONException {
        String boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        //HttpURLConnection con = (HttpURLConnection) url.openConnection();
        URLConnection urlConnection = (url).openConnection();
        HttpURLConnection connection =
                requestURL.startsWith("https") ? (HttpsURLConnection) urlConnection :
                        (HttpURLConnection) urlConnection;
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty(
                REQUEST_PROPERTY_CONTENT_TYPE, "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty(REQUEST_PROPERTY_USER_AGENT, USER_AGENT);
        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);

        writer.append("--" + boundary).append(CRLF);
        writer.append(HEADER_PARAM_CONTENT_DISPOSITION+ jsonDataKey + "\"").append(CRLF);
        writer.append(HEADER_PARAM_CONTENT_TYPE + CHARSET).append(CRLF);
        writer.append(CRLF);
        writer.append(bodyText).append(CRLF);
        writer.flush();

        writer.append(CRLF).flush();
        writer.append("--" + boundary + "--").append(CRLF);
        writer.close();
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            return load(connection);
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }
}
