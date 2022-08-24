package com.artesaniasclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.artesaniasclient.R;
import com.artesaniasclient.model.User;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Util {

    public static final String adminMail = "cristhian.crypton@gmail.com";
    public static final String adminPassword = "1207334184.Qwerty";
    public static int countCraftsOfUser = 0;
    public static int countCompaniesOfUser = 0;
    public static String typeSuscriptionOfUser = "";
    public static final String premiumSuscription = "Premium";
    public static final String freeSuscription = "Free";

    public static Bundle getBundleFusion(Bundle bundleOld, Bundle bundleNew) {
        if (bundleOld == null) return bundleNew;
        bundleOld.putAll(bundleNew);
        return bundleOld;
    }

    public static Properties getPropertiesJavaMail (){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    public static byte[] getBytesImageView(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String getMessageTask(Exception exception) {
        String message = null;
        if (exception != null) {
            message = exception.getMessage();
        }
        return message;
    }

    public static User getUserConnect(Context context) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userJSON = sharedPreferences.getString(context.getString(R.string.CURRENT_USER_KEY_STORE), gson.toJson(new User()));
        User user = gson.fromJson(userJSON, User.class);
        return user;
    }
}
