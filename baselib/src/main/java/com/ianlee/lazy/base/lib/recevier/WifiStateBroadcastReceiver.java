package com.ianlee.lazy.base.lib.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.ianlee.lazy.base.lib.network.utils.LogUtils;

import com.ianlee.lazy.base.lib.network.utils.LogUtils;
import com.ianlee.lazy.base.lib.recevier.listener.ReceiverWifiStateCallback;


/**
 * Created by Ian on
 * Email: yixin0212@qq.com
 * Function : WIFI广播
 */
public class WifiStateBroadcastReceiver extends BroadcastReceiver {

    private ReceiverWifiStateCallback mReceiverWifiStateCallback;

    public WifiStateBroadcastReceiver(ReceiverWifiStateCallback mReceiverWifiStateCallback) {
        this.mReceiverWifiStateCallback = mReceiverWifiStateCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        switch (action) {
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                    LogUtils.INSTANCE.debugInfo("网络变化：" + networkInfo.getState() + "  netInfo extra :" + networkInfo.getExtraInfo());
                    if (isConnected) {
                        if (mReceiverWifiStateCallback != null) {
                            mReceiverWifiStateCallback.onNetworkState(true);
                        }
                    }
                }
                break;
        }
    }
}
