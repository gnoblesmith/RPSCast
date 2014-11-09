package com.adventurepriseme.rps;

import android.util.Log;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Created by Gabe on 10/19/2014.
 */
class RPSChannel extends CC_MediaRouter_Activity implements Cast.MessageReceivedCallback {

    private final String TAG = "RPS Cast Channel";

    public String getNamespace() {
        return "urn:x-cast:com.adventurpriseme.rps";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace,
                                  String message) {
        Log.d(TAG, "onMessageReceived: " + message);

        // FIXME: This is commented out because its implementation is broken
    //    handleResults (Integer.parseInt (message));
    }

    // FIXME: Handle results of RPS game
    // TODO: Move this into its own unit
//    private void handleResults (int nResult) {
//        try {
//            switch (nResult) {
//                case 0:     // Draw
//                {
//                    TextView tvResults = (TextView) findViewById(R.id.textRoundResult);
//                    //    tvResults.setTextColor(0x00FF00);
//                    tvResults.setText("Draw!");
//                }
//                case 1:     // Player 1 wins
//                {
//                    TextView tvResults = (TextView) findViewById(R.id.textRoundResult);
//                    //    tvResults.setTextColor(0x0000FF);
//                    tvResults.setText("Win!");
//                }
//                case 2:     // Player 2 wins
//                {
//                    TextView tvResults = (TextView) findViewById(R.id.textRoundResult);
//                    //   tvResults.setTextColor(0xFF0000);
//                    tvResults.setText("Lose!");
//                }
//                default:
//
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception thrown processing RPS results: ", e);
//        }
//    }
}
