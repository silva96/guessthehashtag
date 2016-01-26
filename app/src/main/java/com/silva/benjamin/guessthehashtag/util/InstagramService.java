package com.silva.benjamin.guessthehashtag.util;


import com.silva.benjamin.guessthehashtag.models.SearchData;
import com.silva.benjamin.guessthehashtag.models.UserData;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface InstagramService {
    @GET("v1/media/search")
    Call<SearchData> listImages(@Query("lat") String lat,
                                @Query("lng") String lng,
                                @Query("access_token") String access_token);

    @GET("v1/tags/{hashtag}/media/recent")
    Call<SearchData> listImagesUsingTag(@Path("hashtag") String hashtag,
                                        @Query("count") String count,
                                        @Query("access_token") String access_token);

    @GET("v1/users/self")
    Call<UserData> getCurrentUser(@Query("access_token") String access_token);
}
