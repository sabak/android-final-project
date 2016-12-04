package com.saba.bogchat.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class responsible for downloading and parsing JSON file from
 * remote location specified by URL.
 */
public final class JSONParser {

    /**
     * Downloads JSONObject from given URL, assuming URL
     * directly points to JSON file.
     *
     * @param url Remote location of requested JSON file
     * @return Downloaded JSONObject from given URL
     */
    public JSONObject getJSONFromUrl(String url) {
        InputStream is = null;
        String result = null;

        try {
            /* Downloading to inputstream */
            is = downloadUrl(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            /* Parsing data as string */
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* try to parse the string to a JSON object */
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(result);
        } catch (JSONException ignored) {
        }

        /* return JSON String */
        return jObj;
    }

    /*
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        /* Starts the query */
        conn.connect();
        return conn.getInputStream();
    }

}
