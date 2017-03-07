package com.rv150.rk1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rv150.rk1.R;
import com.rv150.rk1.Services.NewsIntentService;
import com.rv150.rk1.Services.ServiceHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;


public class MainActivity extends AppCompatActivity implements ServiceHelper.Callback {
    private TextView mNewsHeader;
    private TextView mNewsContent;
    private TextView mNewsDate;

    private final ServiceHelper mServiceHelper = ServiceHelper.getInstance();
    private Storage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsHeader = (TextView) findViewById(R.id.news_header);
        mNewsContent = (TextView) findViewById(R.id.news_content);
        mNewsDate = (TextView) findViewById(R.id.news_date);

        findViewById(R.id.background_update_on).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setBackgroundUpdate(true);
                    }
                }
        );
        findViewById(R.id.background_update_off).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setBackgroundUpdate(false);
                    }
                }
        );

        mServiceHelper.setCallback(this);

        mStorage = Storage.getInstance(this);
        String topic = mStorage.loadCurrentTopic();
        if (topic.isEmpty()) {
            topic = Topics.ALL_TOPICS[0];
            mStorage.saveCurrentTopic(topic);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Берем из кэша
        News news = mStorage.getLastSavedNews();
        if (news != null) {
            displayNews(news);
        }
        // Запрос на обновление новости
        updateNews(null);
    }


    public void updateNews(View view) {
        mServiceHelper.requestNews(this);
    }


    private void setBackgroundUpdate(boolean update) {
        Intent intent = new Intent(this, NewsIntentService.class);
        Scheduler scheduler = Scheduler.getInstance();
        if (update) {
            scheduler.schedule(this, intent, 60*1000L);
        }
        else {
            scheduler.unschedule(this, intent);
        }
    }


    @Override
    public void onNewsLoaded(int resultCode) {
        if (resultCode == NewsIntentService.RESULT_SUCCESS) {
            News news = mStorage.getLastSavedNews();
            displayNews(news);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                runSettingsActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServiceHelper.setCallback(null);
    }


    private void runSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    private void displayNews(News news) {
        String title = news.getTitle();
        String body = news.getBody();

        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        mNewsHeader.setText(title);
        mNewsContent.setText(body);
        mNewsDate.setText(simpleDateFormat.format(news.getDate()));
    }
}
