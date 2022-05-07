package com.example.mixitup.data;

import com.example.mixitup.Utils;
import com.example.mixitup.data.Track;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SpotifyConnection {

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "88e0b9a9d20b4d9299ec788f630e0638";
    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";

    private String token;
    private SpotifyAppRemote appRemote;

    public interface APIGetUserCallback {
        void onGetUser(User newUser);
    }

    public interface APIGetPlaylistsCallback {
        void onGetPlaylists(HashMap<String, Playlist> playlists);
    }

    public interface APIGetPlaylistTracksCallback {
        void onGetPlaylistTracks(ArrayList<Track> tracks);
    }

    public static int getRequestCode() { return REQUEST_CODE; }

    public static String getClientId() { return CLIENT_ID; }

    public static String getRedirectUri() { return REDIRECT_URI; }

    public void initAPI(String accessToken, SpotifyAppRemote spotifyAppRemote) {
        token = accessToken;
        appRemote = spotifyAppRemote;
    }

    public void disconnect() {
        SpotifyAppRemote.disconnect(appRemote);
    }

    public void getCurrentUser(APIGetUserCallback callback) {

        try {
            Utils.getHttpResponseAsync("https://api.spotify.com/v1/me", token, result -> {
                try {
                    String id  = (String)new JSONObject(result).get("id");
                    String displayName  = (String)new JSONObject(result).get("display_name");
                    callback.onGetUser(new User(id, displayName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void getUserPlaylists(APIGetPlaylistsCallback callback) {

        try {
            Utils.getHttpResponseAsync("https://api.spotify.com/v1/me/playlists", token, result -> {
                try {
                    JSONArray playlistsJson = new JSONObject(result).getJSONArray("items");
                    HashMap<String, Playlist> playlists = new HashMap<>();
                    for(int i = 0; i < playlistsJson.length(); i++) {
                        JSONObject playlistJson = playlistsJson.getJSONObject(i);
                        String id = (String)playlistJson.get("id");
                        String name = (String)playlistJson.get("name");
                        String owner = (String)playlistJson.getJSONObject("owner").get("display_name");
                        int numTracks = (int)playlistJson.getJSONObject("tracks").get("total");
                        playlists.put(id, new Playlist(id, name, owner, numTracks));
                    }
                    callback.onGetPlaylists(playlists);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void getPlaylistTracks(String playlistId, APIGetPlaylistTracksCallback callback) {
        try {
            Utils.getHttpResponseAsync(String.format("https://api.spotify.com/v1/playlists/%s/tracks", playlistId), token, result -> {
                try {
                    JSONArray tracksJson = new JSONObject(result).getJSONArray("items");
                    ArrayList<Track> tracks = new ArrayList<>();
                    for(int i = 0; i < tracksJson.length(); i++) {
                        JSONObject trackJson = tracksJson.getJSONObject(i).getJSONObject("track");
                        String id = (String)trackJson.get("id");
                        String name = (String)trackJson.get("name");
                        JSONArray artists = trackJson.getJSONArray("artists");
                        String artistStr = "";
                        for(int j = 0; j < artists.length(); j++) {
                            artistStr += artists.getJSONObject(j).get("name") + ", ";
                        }
                        artistStr = artistStr.substring(0, artistStr.length() - 2);
                        tracks.add(new Track(id, name, artistStr));
                    }
                    callback.onGetPlaylistTracks(tracks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
