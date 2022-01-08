package de.hdmstuttgart.gitlapp.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Get API clients (the app will only use the gitlab api this is why its not dynamic)
 */
public class ServiceGenerator {

    private final GitLabClient gitLabClient; //todo singleton or static

    public ServiceGenerator(String baseUrl){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = retrofitBuilder.build();
        gitLabClient = retrofit.create(GitLabClient.class);
    }

    public GitLabClient getGitLabClient(){
        return gitLabClient;
    }

}
