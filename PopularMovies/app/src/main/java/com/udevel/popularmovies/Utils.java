package com.udevel.popularmovies;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by benny on 8/9/2015.
 */
public class Utils {

    public static Intent getYouTubeIntent(PackageManager pm, String url) {
        Intent intent;
        if (url.length() == 11) {
            // youtube video id
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
        } else {
            // url to video
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        try {
            if (pm.getPackageInfo("com.google.android.youtube", 0) != null) {
                intent.setPackage("com.google.android.youtube");
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return intent;
    }
}
