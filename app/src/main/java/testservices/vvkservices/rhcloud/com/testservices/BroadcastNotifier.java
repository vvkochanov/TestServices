package testservices.vvkservices.rhcloud.com.testservices;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Класс, обеспечивающий отправку широковещательных сообщений
 */
public class BroadcastNotifier {
    private final static String TAG = BroadcastNotifier.class.getSimpleName();
    private LocalBroadcastManager localBroadcastManager;

    public BroadcastNotifier(Context context) {
        Log.d(TAG, "Enter BroadcastNotifier");
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void sendStatusNotify(String status){
        Log.d(TAG, "Enter sendStatusNotify - " + status);
        Intent intent = new Intent(status);
        Log.d(TAG, "In sendStatusNotify - new intent with Action: " + intent.getAction());
        Log.d(TAG, "In sendStatusNotify - localBroadcastManager: " + Boolean.valueOf(localBroadcastManager != null).toString());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        localBroadcastManager.sendBroadcast(intent);

    }
}
