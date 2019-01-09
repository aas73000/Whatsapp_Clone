package com.example.administrator.whatsapp_clone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com/")
                .build()
        );

        super.onCreate();
    }
}
