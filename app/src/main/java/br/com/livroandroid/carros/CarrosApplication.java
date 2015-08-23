package br.com.livroandroid.carros;

import android.app.Application;
import android.util.Log;

/**
 * Created by Guilherme on 23-Aug-15.
 */
public class CarrosApplication extends Application {
    private static final String TAG = "CarrosApplication";
    private static CarrosApplication instance = null;

    public static CarrosApplication getInstance(){
        return instance;
    }
    @Override
    public void onCreate(){
        Log.d(TAG, "CarrosApplication.onCreate()");
    }
    @Override
    public void onTerminate(){
        super .onTerminate();
        Log.d(TAG, "CarrosApplication.onTerminate()");
    }
}
