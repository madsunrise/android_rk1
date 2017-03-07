package com.rv150.rk1.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rv150.rk1.R;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

/**
 * Created by ivan on 07.03.17.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linear);

        for (String topic: Topics.ALL_TOPICS) {
            final Button button = new Button(this);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            button.setLayoutParams(layoutParams);
            button.setText(topic);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topic = button.getText().toString();
                    Storage.getInstance(SettingsActivity.this).saveCurrentTopic(topic);
                    finish();
                }
            });

            linearLayout.addView(button);
        }
    }
}
