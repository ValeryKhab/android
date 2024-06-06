package com.example.toytrade2;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HolidaysApi {
    @GET("/v1/")
    Call<List<Holiday>> getHolidays(@Query("api_key") String apiKey, @Query("country") String country,
                                    @Query("year") int year, @Query("month") int month, @Query("day") int day);

}
