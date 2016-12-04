package com.saba.bogchat.asynchtask;

import java.util.ArrayList;
import java.util.List;

import com.saba.bogchat.model.Contact;
import com.saba.bogchat.util.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for downloading contacts list from remote
 * JSON file URL.
 */
public class URLContactListDownloaderTask extends ContactListDownloaderTask {

    @Override
    protected List<Contact> doInBackground(String... params) {
        /* URL where JSON file is located at */
        String url = params[0];

        /* JSON key that corresponds to required JSON Array */
        String arrKey = params[1];

        /* Downloading JSON file */
        JSONParser parser = new JSONParser();
        JSONObject jObj = parser.getJSONFromUrl(url);

        /* Parsing and returning JSON file to list of contacts */
        return JSONToList(jObj, arrKey);
    }

    /*
     * Parses JSON Object containing JSON Array with given key and
     * returns corresponding list of contacts, assuming given JSON Object
     * was properly formatted.
     */
    private List<Contact> JSONToList(JSONObject jobj, String arrKey) {
        /* List of contacts to be returned */
        List<Contact> result = new ArrayList<>();
        try {
            /* Getting JSON Array with given key */
            JSONArray jsonArray = jobj.getJSONArray(arrKey);

            /* Iterating over JSON Array and constructing contact objects */
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject curr = jsonArray.getJSONObject(i);
                String id = curr.getString("id");
                String displayName = curr.getString("displayName");
                String phoneNumber = curr.getString("phoneNumber");
                String avatarURL = curr.getString("avatarImg");

                /* Constructing contact object */
                Contact contact = new Contact(id, displayName, phoneNumber, avatarURL);
                result.add(contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}
