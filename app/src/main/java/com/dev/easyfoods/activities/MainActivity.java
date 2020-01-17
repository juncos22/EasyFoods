package com.dev.easyfoods.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dev.easyfoods.R;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread splashThread = new Thread(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                Log.println(Log.ERROR, "ERROR", Objects.requireNonNull(e.getMessage()));
            }finally {
                startActivity(new Intent(MainActivity.this, MenuActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }
        });
        
        splashThread.start();
    }
}
