package edu.msu.cse476.baragurr.ecosnap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VisionApiService {
    @POST("v1/images:annotate")
    Call<VisionResponse> annotateImage(@Body VisionRequest request, @Query("key") String apiKey);
}