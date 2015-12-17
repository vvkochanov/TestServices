package testservices.vvkservices.rhcloud.com.testservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class FlashActivity extends AppCompatActivity {
    private static final String TAG = "FlashActivity";
    private ProgressBar progressBar;
    private BroadcastReceiver flashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case Constants.BROADCAST_ACTION:
                    Intent intentActivity = new Intent(context, MainActivity.class);
                    startActivity(intentActivity);
                    finish();
            }
        }
    };

    private Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                for (int i = 0; i < 15; i++) {
                    Log.v(TAG, String.format("Flash thread sleep by %d sec", i));
                    sleep(1000);
                }
                Log.v(TAG, String.format("Flash thread sleep by %d sec", 15));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                Intent intent = new Intent(Constants.BROADCAST_ACTION)
                        .putExtra(Constants.EXT_DATA_STATUS, Constants.VAL_STATUS_FINISH);
                LocalBroadcastManager.getInstance(getParent()).sendBroadcast(intent);
                super.run();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //start service and wait 15 sec for service activity
        progressBar.setVisibility(ProgressBar.VISIBLE);
        // запуск сервиса с фоновыми задачами
        startService(new Intent(this, FirstIntentService.class));
        // запуск фонового потока для отсчета времени ожидания ответа от сервера
        thread.start();
        // теперь создаем фильтры для ресивера
        IntentFilter mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(flashReceiver, mStatusIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(flashReceiver);
    }
}
