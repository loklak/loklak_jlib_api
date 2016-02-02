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

public class NetworkIO {

    private final static String charset = "UTF-8";
    private final static String CRLF = "\r\n";
    private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    public static StringBuilder loadString(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        URLConnection uc = (new URL(url)).openConnection();
        HttpURLConnection con = url.startsWith("https") ? (HttpsURLConnection) uc : (HttpURLConnection) uc;
        try {
            con.setReadTimeout(6000);
            con.setConnectTimeout(6000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setRequestProperty("User-Agent", USER_AGENT);
            sb = load(con);
        } catch (IOException e) {
            throw e;
        } finally {
            con.disconnect();
            
        }
        return sb;
    }

    private static StringBuilder load(HttpURLConnection con) throws IOException {
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String s;
            while ((s = br.readLine()) != null) sb.append(s).append('\n');
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) br.close();
            con.disconnect();
        }
        return sb;
    }
    
    public static StringBuilder pushString(String requestURL, String jsonDataName, String bodytext) throws IOException, JSONException {
        String boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        //HttpURLConnection con = (HttpURLConnection) url.openConnection();
        URLConnection uc = (url).openConnection();
        HttpURLConnection con = requestURL.startsWith("https") ? (HttpsURLConnection) uc : (HttpURLConnection) uc;
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setRequestProperty("User-Agent", USER_AGENT);
        OutputStream outputStream = con.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);

        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"" + "data" + "\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        writer.append(CRLF);
        writer.append(bodytext).append(CRLF);
        writer.flush();

        writer.append(CRLF).flush();
        writer.append("--" + boundary + "--").append(CRLF);
        writer.close();
        int status = con.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            StringBuilder sb = load(con);
            return sb;
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
    }
}
