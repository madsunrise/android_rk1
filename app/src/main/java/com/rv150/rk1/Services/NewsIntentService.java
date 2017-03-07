package com.rv150.rk1.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;

/**
 * Created by ivan on 07.03.17.
 */

public class NewsIntentService extends IntentService {

    public NewsIntentService() {
        super("NewsIntentService");
    }

    public static final String EXTRA_NEWS_RESULT_RECEIVER = "extra_news_result_receiver";
    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_ERROR = 2;


    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_NEWS_RESULT_RECEIVER);
        String topic = Storage.getInstance(this).loadCurrentTopic();

        try {
            News news = new NewsLoader().loadNews(topic);
            Storage.getInstance(this).saveNews(news);
            if (receiver != null) {
                receiver.send(RESULT_SUCCESS, null);
            }
        }
        catch (IOException ex) {
            if (receiver != null) {
                receiver.send(RESULT_ERROR, null);
            }
        }
    }
}
