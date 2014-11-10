package com.adventurepriseme.rps;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;

/**
 * Created by Gabe on 10/19/2014.
 */
class RPSChannel extends CC_MediaRouter_Activity implements Cast.MessageReceivedCallback {

    private final String TAG = "RPS Cast Channel";
    private TextView tv;

    public RPSChannel (TextView _tv) {
        tv = _tv;
    }

    public String getNamespace() {
        return "urn:x-cast:com.adventurpriseme.rps";
    }

    @Override
    public void onMessageReceived(CastDevice castDevice, String namespace,
                                  String message) {
        Log.d(TAG, "onMessageReceived: " + message);

        // Process the RPS results
        handleMessageRPS(message);
        tv.setText(message);

        // FIXME: This is commented out because its implementation is broken
    //    handleResults (Integer.parseInt (message));
    }

    // Handle results of RPS game
    // TODO: Move this into its own unit
    private void handleMessageRPS(String strResult) {
        try {
            if (strResult != null && !strResult.isEmpty()) {
                if (strResult.contains("draw")) {
                    tv.setText("Draw!");
                } else if (strResult.contains("p1")) {  // P1 wins
                    tv.setText("Win!");
                } else if (strResult.contains("p2")) {  // P2 wins
                    tv.setText("Lose!");
                } else if (strResult.contains("rock")) {
                    tv.setText("You threw rock!");
                } else if (strResult.contains("paper")) {
                    tv.setText("You threw paper");
                } else if (strResult.contains("scissors")) {
                    tv.setText("You threw scissors");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception thrown processing RPS results: ", e);
        }
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
