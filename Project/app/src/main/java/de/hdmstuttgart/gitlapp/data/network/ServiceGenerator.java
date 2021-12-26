package de.hdmstuttgart.gitlapp.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static String mBaseUrl  = "https://gitlab.mi.hdm-stuttgart.de/api/v4/";

    public ServiceGenerator(String baseUrl){
        mBaseUrl = "https://gitlab.mi.hdm-stuttgart.de/api/v4/";
    }

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static IssueApi issueApi = retrofit.create(IssueApi.class);

    public IssueApi getIssueApi(){
        return issueApi;
    }
}
