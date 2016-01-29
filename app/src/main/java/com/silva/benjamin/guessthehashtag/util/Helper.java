package com.silva.benjamin.guessthehashtag.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.silva.benjamin.guessthehashtag.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by benjamin on 12/7/15.
 */
public class Helper {

    public static final String SETTINGS_NAME = "GUESS_THE_HASHTAG_SETTINGS";
    public static final String TOKEN_PREFERENCE = "API_TOKEN";
    public static final String USERNAME_PREFERENCE = "USERNAME";
    public static final String CLIENT_ID = "efc16b4fd66b42fb8f7b0cc40cfe39f9";//"57f40135143043aba4325de3089be16d";
    public static final String CALLBACK_URL = "http://localhost";
    public static int mCurrentStreak = 0;
    public static User currentUser;

    public static void saveToken(String token, Context context) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helper.TOKEN_PREFERENCE, token);
        editor.commit();
    }

    public static void removeToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helper.TOKEN_PREFERENCE, "has-no-token");
        editor.commit();
    }

    public static boolean hasToken(Context context) {
        return !getToken(context).equals("has-no-token");
    }

    public static String getToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        String token = settings.getString(Helper.TOKEN_PREFERENCE, "has-no-token");
        return token;
    }

    public static InstagramService service() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com")
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client) //uncomment this line to debug
                .build();
        InstagramService service = retrofit.create(InstagramService.class);
        return service;
    }


    public static ArrayList<String> getAllHashtags() {
        return StaticData.allHashtags;
    }

    public static ArrayList<String> getRandomHashtagOptions(String used, ArrayList<String> banned) {
        ArrayList<String> total = new ArrayList<>(getAllHashtags());
        total.removeAll(banned);
        Collections.shuffle(total);
        total.subList(2, total.size() - 1).clear();
        total.add(used);
        Collections.shuffle(total);
        return total;
    }

    public static void saveCurrentUserName(Context context, String username) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helper.USERNAME_PREFERENCE, username);
        editor.commit();

    }

    public static String getCurrentUserName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        String username = settings.getString(Helper.USERNAME_PREFERENCE, "##no-username##");
        return username;

    }

    public static boolean hasUsername(Context context) {
        return !getCurrentUserName(context).equals("##no-username##");
    }

    public static void removeUsername(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Helper.SETTINGS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Helper.USERNAME_PREFERENCE, "##no-username##");
        editor.commit();
    }

    public static String getRandomHashTag() {
        ArrayList<String> total = new ArrayList<>(getAllHashtags());
        Collections.shuffle(total);
        Random rnd = new Random();
        return total.get(rnd.nextInt(total.size()));
    }
}
