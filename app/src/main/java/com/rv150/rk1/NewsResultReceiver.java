package com.rv150.rk1;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.rv150.rk1.Services.ServiceHelper;

/**
 * Created by ivan on 07.03.17.
 */

public class NewsResultReceiver extends ResultReceiver {

    private ServiceHelper.Callback mCallback;

    public NewsResultReceiver(final Handler handler) {
        super(handler);
    }

    public void setCallback(ServiceHelper.Callback callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (mCallback != null) {
            mCallback.onNewsLoaded(resultCode);
        }
    }
}
