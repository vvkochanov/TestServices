package testservices.vvkservices.rhcloud.com.testservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FirstIntentService extends IntentService {

    private final static String TAG = "FirstIntentService: ";
    private final static String name = "FirstIntentService";
    private final FirstBinder mBinder = new FirstBinder();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Field name Used to name the worker thread, important only for debugging.
     */
    public FirstIntentService() {
        super(name);
        Log.i(TAG, "constructor, thread - " + name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, String.format("%s, flags: %d, startId: %d", "onStartCommand", flags, startId));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent Begin...");
        long beg_time = System.currentTimeMillis();

        List<DownloadInThread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
                Log.v(TAG, String.format("Service create new Thread %d ...", i));
                threads.add(new DownloadInThread(String.format("Thread %d", i)));
            }
        for (int i = 0; i < threads.size(); i++){
                Log.v(TAG, String.format("Service wait for Thread %d is finished ...", i));
                try {
                    threads.get(i).join(11000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Log.v(TAG, String.format("Thread %d is finished. Continue Service...", i));
                }

        }
        long end_time = System.currentTimeMillis();
        Log.i(TAG, String.format("onHandleIntent End. Time of work: %d sec.", (end_time - beg_time)/1000));
    }
    /*******************************************
     * Inner classes
      ******************************************/
    private class DownloadInThread extends Thread{
        private String threadName;

        private void writeFile(String filename) {
            try {
// отрываем поток для записи
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        openFileOutput(filename, MODE_PRIVATE)));
// пишем данные
                Log.v(TAG, String.format("Файл %s открыт для записи...", filename));
                long curTime = System.currentTimeMillis();
                String curStrDate = new SimpleDateFormat("dd mmm yyyy hh:mm").format(curTime);
                bw.write(String.format("File %s wrote at %s", filename, curStrDate));
// закрываем поток
                bw.close();
                Log.d(TAG, String.format("Файл %s записан", filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public DownloadInThread(String threadName) {
            super(threadName);
            this.threadName = threadName;
            Log.v(TAG, String.format(" > %s is starting...", threadName));
            start();
        }

        @Override
            public void run() {

                try {
                    synchronized (this) {
                        for (int i = 0; i < 10; i++) {
                            String s = String.format(" >> %s run for %d sec...", threadName, i);
                            Log.v(TAG, s);
                            sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Log.v(TAG, String.format(" >> %s completed! <<", threadName));
                }
            writeFile(String.format("File of %s.txt", threadName));
                super.run();
            }
    };

    public class FirstBinder extends Binder {
        FirstIntentService getService (){
            return FirstIntentService.this;
        }
    }
}
