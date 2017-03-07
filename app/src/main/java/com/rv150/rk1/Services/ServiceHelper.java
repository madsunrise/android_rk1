package com.rv150.rk1.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.rv150.rk1.NewsResultReceiver;

/**
 * Created by ivan on 07.03.17.
 */

public class ServiceHelper {

    private static final ServiceHelper instance = new ServiceHelper();

    private NewsResultReceiver mResultReceiver = new NewsResultReceiver(new Handler());

    private ServiceHelper() {
    }

    public static ServiceHelper getInstance() {
        return instance;
    }

    public void requestNews(Context context) {
        Intent intent = new Intent(context, NewsIntentService.class);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_RESULT_RECEIVER, mResultReceiver);
        context.startService(intent);
    }

    public void setCallback(Callback callback) {
        mResultReceiver.setCallback(callback);
    }

    public interface Callback {
        void onNewsLoaded(int resultCode);
    }
}