package com.silva.benjamin.guessthehashtag.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjamin on 12/9/15.
 */
public class User {
    String username;
    String full_name;
    String profile_picture;
    int score;
    int max_streak;
    int week_score;

    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMax_streak() {
        return max_streak;
    }

    public void setMax_streak(int max_streak) {
        this.max_streak = max_streak;
    }

    public int getWeek_score() {
        return week_score;
    }

    public void setWeek_score(int week_score) {
        this.week_score = week_score;
    }
}
