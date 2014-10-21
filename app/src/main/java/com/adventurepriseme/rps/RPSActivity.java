package com.adventurepriseme.rps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;


public class RPSActivity extends ActionBarActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Cast.MessageReceivedCallback {

    MediaRouter mMediaRouter;
    MediaRouteSelector mMediaRouteSelector;
    static MediaRouter.Callback mMediaRouterCallback;
    private static final String TAG = "RPS Activity";
    private GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rps);

        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast( /*CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID))*/
                        /*"3DC89F06"))"ACE707D0"))//*/ "21857AF0"))
                .build();

        mMediaRouterCallback = new MyMediaRouterCallback();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.r, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // fixme
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        //return super.onOptionsItemSelected(item);
        return true;
    }
    private boolean mWaitingForReconnect = false;
    private boolean mApplicationStarted = false;
    private HelloWorldChannel mHelloWorldChannel;
    @Override
    public void onConnected(Bundle connectionHint) {
        if (mWaitingForReconnect) {
            mWaitingForReconnect = false;
            //reconnectChannels();
        } else {
            try {
                Cast.CastApi.launchApplication( mApiClient,
                        /*"3DC89F06","ACE707D0",*/"21857AF0", ///CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID,
                                                false).setResultCallback (
                        new ResultCallback<Cast.ApplicationConnectionResult>() {
                            @Override
                            public void onResult (Cast.ApplicationConnectionResult result) {
                                Status status = result.getStatus();
                                if (status.isSuccess()) {
                                    ApplicationMetadata applicationMetadata =
                                        result.getApplicationMetadata();
                                    String sessionId = result.getSessionId();
                                    String applicationStatus = result.getApplicationStatus();
                                    boolean wasLaunched = result.getWasLaunched();

                                    mApplicationStarted = true;

                                    mHelloWorldChannel = new HelloWorldChannel();
                                    try {
                                        Cast.CastApi.setMessageReceivedCallbacks(mApiClient,
                                                mHelloWorldChannel.getNamespace(),
                                                mHelloWorldChannel);

                                        //sendMessage("http://gnosm.net/missilecommand/sounds/524.mp3");
                                    } catch (IOException e) {
                                        Log.e(TAG, "Exception while creating channel", e);
                                    }



                                }
                            }
                        }
                );
            } catch (Exception e) {
                Log.e(TAG, "Failed to launch application", e);
            }
        }
    }

    private void sendMessage (String message) {
        if (mApiClient != null && mHelloWorldChannel != null) {
            try {
                Cast.CastApi.sendMessage(mApiClient, mHelloWorldChannel.getNamespace(), message)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status result) {
                                        if (!result.isSuccess()) {
                                            Log.e(TAG, "Sending message failed");
                                        }
                                    }
                                });
            } catch (Exception e) {
                Log.e(TAG, "Exception while sending message", e);
            }

        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mWaitingForReconnect = true;
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // fixme teardown();
    }
    public String getNamespace() {
        return "com.adventurpriseme.rps";
    }
    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace,
                                  String message) {
        Log.d(TAG, "onMessageReceived: " + message);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }
    @Override
    protected void onPause() {
        if (isFinishing()) {
            mMediaRouter.removeCallback(mMediaRouterCallback);
        }
        super.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }
    @Override
    protected void onStop() {
        mMediaRouter.removeCallback(mMediaRouterCallback);
        super.onStop();
    }
    private final class MyMediaRouterCallback extends MediaRouter.Callback {

        private CastDevice mSelectedDevice;
        private Cast.Listener mCastClientListener;
        private final String TAG = "My Media Router Callback";

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            String routeId = info.getId();

            mCastClientListener = new Cast.Listener() {
                @Override
                public void onApplicationStatusChanged() {
                    if (mApiClient != null) {
                        Log.d(TAG, "onApplicationStatusChanged: "
                                + Cast.CastApi.getApplicationStatus(mApiClient));


                    }
                }

                @Override
                public void onVolumeChanged() {
                    if (mApiClient != null) {
                        Log.d(TAG, "onVolumeChanged: " + Cast.CastApi.getVolume(mApiClient));
                    }
                }

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    // fixme teardown();
                }
            };


            Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                    .builder(mSelectedDevice, mCastClientListener);

            mApiClient = new GoogleApiClient.Builder(RPSActivity.this)
                    .addApi(Cast.API, apiOptionsBuilder.build())
                    .addConnectionCallbacks(RPSActivity.this)
                    .addOnConnectionFailedListener(RPSActivity.this)
                    .build();

            mApiClient.connect();


        }

    }
}