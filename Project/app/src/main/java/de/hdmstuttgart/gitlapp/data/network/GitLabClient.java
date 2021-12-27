package de.hdmstuttgart.gitlapp.data.network;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

//all the requests for the api
public interface GitLabClient {

    @GET("projects/6414/issues") //todo make project id not hardcoded
    Call<List<Issue>> getSearchResult(@Header("Authorization") String auth);
}
