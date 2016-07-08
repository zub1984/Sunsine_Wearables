package com.example.android.sunshine.app.wearable;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


public class DataManager {


    private static final String TAG = "DataManager";
    private static DataManager instance = null;

    public static DataManager getInstance() {
        if(instance == null){
            instance = new DataManager();
        }
        return instance;
    }


    public  void syncDataMap(GoogleApiClient mGoogleApiClient, DataMap dataMap,
                             String path,
                             ResultCallback<DataApi.DataItemResult> resultCallback){
        PutDataMapRequest putdatamaprequest = PutDataMapRequest.create(path);
        putdatamaprequest.getDataMap().putAll(dataMap);
        PutDataRequest request = putdatamaprequest.asPutDataRequest();

        if (!mGoogleApiClient.isConnected()) {
            return;
        }
        Log.d(TAG, "DataMap  " + dataMap + " sending to " + path);
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)  .setResultCallback(resultCallback);
    }


}
