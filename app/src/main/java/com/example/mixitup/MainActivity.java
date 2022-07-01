package com.example.mixitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.mixitup.playlists.AddPlaylistFragment;
import com.example.mixitup.data.Repository;
import com.example.mixitup.data.SpotifyConnection;
import com.example.mixitup.playlists.PlaylistsFragment;
import com.example.mixitup.queue.QueueFragment;
import com.google.android.material.navigation.NavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Repository repo;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new Repository();

        fragments = new ArrayList<>();
        fragments.add(new PlaylistsFragment());
        fragments.add(new QueueFragment());
        fragments.add(new AddPlaylistFragment());
        setCurrentFragment(fragments.get(0), R.id.flFragments);

        DrawerLayout navDrawer = findViewById(R.id.dlNav);
        navDrawer.addDrawerListener(new MyDrawerListener());

        NavigationView navView = findViewById(R.id.nvNavView);
        navView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.navPlaylists:
                    setCurrentFragment(fragments.get(0), R.id.flFragments);
                    navDrawer.closeDrawer(Gravity.LEFT);
                    break;
                case R.id.navQueue:
                    setCurrentFragment(fragments.get(1), R.id.flFragments);
                    navDrawer.closeDrawer(Gravity.LEFT);
            }
            return true;
        });
        navView.setCheckedItem(R.id.navPlaylists);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(SpotifyConnection.getClientId(), AuthorizationResponse.Type.TOKEN, SpotifyConnection.getRedirectUri());

        builder.setScopes(new String[]{ "streaming", "playlist-read-private" });
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, SpotifyConnection.getRequestCode(), request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == SpotifyConnection.getRequestCode()) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    connectAppRemote(response.getAccessToken());
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    System.out.println(response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void connectAppRemote(String token) {

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(SpotifyConnection.getClientId())
                        .setRedirectUri(SpotifyConnection.getRedirectUri())
                        .showAuthView(true).build();
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {

            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                Log.d("MainActivity", "Connected! Yay!");
                repo.initSpotifyConnection(token, spotifyAppRemote);
                repo.getCurrentUser(result -> {
                    System.out.println(result.id + " - " + result.displayName);
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void navAddPlaylist() {
        setCurrentFragment(fragments.get(2), R.id.flFragments);
    }

    public void navPlaylists() {
        setCurrentFragment(fragments.get(0), R.id.flFragments);
    }

    public void navQueue() { setCurrentFragment(fragments.get(1), R.id.flFragments); }

    private void setCurrentFragment(Fragment fragment, int frameLayoutId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(frameLayoutId, fragment);
        ft.commit();
    }

    class MyDrawerListener extends SimpleDrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            findViewById(R.id.dlNav).setTranslationZ(slideOffset);
        }
    }
}