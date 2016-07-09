package com.example.jubair.wear.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jubair.wear.SunshineWatchFace;
import com.example.jubair.wear.util.DigitalWatchFaceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.jubair.utils.Constants;



public class ConfigDataListenerService extends WearableListenerService implements
        com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ConfigDataListenerService.class.getSimpleName();

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d(TAG, " onPeerConnected ");
        if (isServiceRunning(this, SunshineWatchFace.class)) {
            //Log.d(TAG, " sending start watch face ");
            sendMessageToDevice(Constants.PATH_START_WATCH_FACE);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean startWatchFace = checkAction(intent, Constants.KEY_START_WATCH_FACE);
        if (startWatchFace) {
            sendMessageToDevice(Constants.PATH_START_WATCH_FACE);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private static boolean checkAction(Intent intent, String booleanKey) {
        return intent != null && intent.getBooleanExtra(booleanKey, false);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: ");
        try {
            for (DataEvent dataEvent : dataEvents) {
                if (dataEvent.getType() != DataEvent.TYPE_CHANGED) {
                    continue;
                }
                DataItem dataItem = dataEvent.getDataItem();
                //Log.d(TAG, "path: " + dataItem.getUri().getPath());
                if (!dataItem.getUri().getPath().equals(Constants.PATH_WEATHER_DATA)) {
                    continue;
                }
                DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                DataMap config = dataMapItem.getDataMap();
                DigitalWatchFaceUtil.overwriteKeysInConfigDataMap(mGoogleApiClient, config, Constants.PATH_WEATHER_DATA);
            }
        } finally {
            dataEvents.release();
        }
    }

    private void sendMessageToDevice(final String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient == null) {
                    mGoogleApiClient = (new com.google.android.gms.common.api.GoogleApiClient.Builder(ConfigDataListenerService.this)).addConnectionCallbacks(ConfigDataListenerService.this).addOnConnectionFailedListener(ConfigDataListenerService.this).addApi(Wearable.API).build();
                }
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                if (nodes != null && nodes.getNodes() != null) {
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), key, null).await();
                        if (!result.getStatus().isSuccess()) {
                            Log.e(TAG, "Error during sending message");
                        } else {
                            Log.i(TAG, "Success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Log.d(TAG, (new StringBuilder()).append("onConnected: ").append(bundle).toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.d(TAG, (new StringBuilder()).append("onConnectionSuspended: ").append(i).toString());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.d(TAG, (new StringBuilder()).append("onConnectionFailed: ").append(connectionResult).toString());
    }


    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
