package testservices.vvkservices.rhcloud.com.testservices;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import static testservices.vvkservices.rhcloud.com.testservices.FirstIntentService.*;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnFragmentElemClickListener {

    private static final String TAG = "MainActivity";
    private FIS_BroadcastReceiver fisBroadcastReceiver;

    private  boolean mBoundFrstService = false; // имеется ли привязка к сервису FirstIntentService
    private FirstIntentService mFrstSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // теперь создаем фильтры для ресивера
        IntentFilter mStartSVCIntentFilter = new IntentFilter(Constants.BROADCAST_START_SVC);
        IntentFilter mStopSVCIntentFilter = new IntentFilter(Constants.BROADCAST_STOP_SVC);
        LocalBroadcastManager.getInstance(this).registerReceiver(fisBroadcastReceiver, mStartSVCIntentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(fisBroadcastReceiver, mStopSVCIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBoundFrstService) {
            unbindService(mConnection);
            mBoundFrstService = false;
        }
    }

    @Override
    public void onElementClick(View view) {
        Log.v(TAG, view.getClass().getSimpleName());
        switch (view.getId()){
            case R.id.BtnStartService:
                startService(new Intent(this, FirstIntentService.class));
                break;
            case R.id.BtnBindService:
                if (!mBoundFrstService) {
                    bindService(new Intent(this, FirstIntentService.class), mConnection, Context.BIND_AUTO_CREATE);
                }else {
                    unbindService(mConnection);
                    mBoundFrstService = false;
                    Snackbar.make(view, "FirstIntentService is already binding! Unbind it!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FirstIntentService.FirstBinder binder = (FirstIntentService.FirstBinder) service;
            mFrstSvc = binder.getService();
            FirstIntentService.actionStartService(getApplicationContext());
            mBoundFrstService = true;
            Log.i(TAG, "Service " + name.getShortClassName() + " is connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundFrstService = false;
            Log.i(TAG, "Service is disconnected!");
        }
    };
    private class FIS_BroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            switch (intent.getAction()){
                case Constants.BROADCAST_START_SVC:
                    mainActivityFragment.setTextView1("Запуск сервиса");
                    return;
                case Constants.BROADCAST_STOP_SVC:
                    mainActivityFragment.setTextView1("Остановка сервиса");

            }
        }
    }

}
