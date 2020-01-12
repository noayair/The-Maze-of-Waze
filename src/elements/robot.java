package elements;

import Server.game_service;
import org.json.JSONException;
import org.json.JSONObject;

public class robot implements Robot_I {
    private game_service game;

    public Robot_I init(String s) {
        try {
            JSONObject rob = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
