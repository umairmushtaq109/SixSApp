package com.example.sixsapp.utilis;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

public class MyApplication extends Application {

    @Override
    public void onTerminate() {
        super.onTerminate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAutoCacheClearEnabled = sharedPreferences.getBoolean("auto_cache_clear", true);

        if (isAutoCacheClearEnabled) {
            clearCache();
        }
    }

    private void clearCache() {
        String rootDataDir = getApplicationContext().getCacheDir().getAbsolutePath() + "/EasyImage";
        File easyImageFolder = new File(rootDataDir);
        if(easyImageFolder.exists()){
            File[] cachedImages = easyImageFolder.listFiles();
            if(cachedImages != null && cachedImages.length > 0){
                for (File file: cachedImages) {
                    file.delete();
                }
                Toast.makeText(this, "Cache Cleared!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
