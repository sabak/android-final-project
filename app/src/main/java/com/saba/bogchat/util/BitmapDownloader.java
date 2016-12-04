package com.saba.bogchat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class responsible for downloading bitmaps from remote locations
 * specified by given URL.
 */
public final class BitmapDownloader {

    /**
     * Downloads bitmap from given URL location, assuming it directly
     * points to file to be downloaded
     *
     * @param url URL location of bitmap file to be downloaded
     * @return Downloaded bitmap file
     */
    public Bitmap getBitmapFromURL(String url) {
        InputStream is = null;

        try {
            /* Downloading to input stream */
            is = downloadUrl(url);

            /* Constructing bitmap file from given input stream */
            return BitmapFactory.decodeStream(is);
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

        return null;
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
