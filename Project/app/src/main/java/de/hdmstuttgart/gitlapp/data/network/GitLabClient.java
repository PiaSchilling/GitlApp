package de.hdmstuttgart.gitlapp.data.network;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Project;
import de.hdmstuttgart.gitlapp.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

//all the requests for the api
public interface GitLabClient {

    @GET("projects/{id}/issues?with_labels_details=true") //todo make project id not hardcoded
    Call<List<Issue>> getProjectIssues(@Path ("id") int projectId, @Header("Authorization") String auth);

    @GET("projects?membership=true")
    Call<List<Project>> getMemberProjects(@Header("Authorization") String auth);

    @GET("users/{id}")
    Call<User> getSingleUser(@Path ("id") int userId, @Header("Authorization") String auth);


}
