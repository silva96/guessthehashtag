package com.silva.benjamin.guessthehashtag.util;


import com.silva.benjamin.guessthehashtag.models.SearchData;
import com.silva.benjamin.guessthehashtag.models.UserData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstagramService {

    @GET("v1/tags/{hashtag}/media/recent")
    Call<SearchData> listImagesUsingTag(@Path("hashtag") String hashtag,
                                        @Query("count") String count,
                                        @Query("access_token") String access_token);

    @GET("v1/users/self")
    Call<UserData> getCurrentUser(@Query("access_token") String access_token);
}
