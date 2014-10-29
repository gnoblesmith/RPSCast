package com.adventurepriseme.rps;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Created by Gabe on 10/19/2014.
 */
class RPSChannel implements Cast.MessageReceivedCallback {

    private final String TAG = "RPS Cast Channel";

    public String getNamespace() {
        return "urn:x-cast:com.adventurpriseme.rps";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace,
                                  String message) {
        Log.d(TAG, "onMessageReceived: " + message);
    }
}
