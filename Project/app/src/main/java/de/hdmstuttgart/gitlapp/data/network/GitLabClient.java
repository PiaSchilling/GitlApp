package de.hdmstuttgart.gitlapp.data.network;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Project;
import de.hdmstuttgart.gitlapp.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

//all the requests for the api
public interface GitLabClient {

    // - - - - - issues - - - - -
    @GET("projects/{id}/issues?with_labels_details=true")
    Call<List<Issue>> getProjectIssues(@Path ("id") int projectId,
                                       @Header("Authorization") String auth,
                                       @Query("page") int page);

    @PUT("projects/{id}/issues/{issue_iid}?state_event=close")
    Call<Void> closeIssue(@Path("id") int projectId,
                          @Path("issue_iid") int issueIid,
                          @Header("Authorization") String auth);

    @POST("projects/{id}/issues")
    Call<Void> postNewIssue(@Path("id") int projectId,
                            @Header("Authorization") String auth,
                            @Query("title") String issueTitle,
                            @Query("description") String issueDescription,
                            @Query("due_date") String dueDate,
                            @Query("weight") int weight,
                            @Query("milestone_id") int milestoneId,
                            @Query("labels") String labels);

    // - - - - - projects - - - - - -
    @GET("projects?membership=true")
    Call<List<Project>> getMemberProjects(@Header("Authorization") String auth);

    @GET("projects/{id}/labels")
    Call<List<Label>> getProjectLabels(@Path ("id") int projectId,
                                 @Header("Authorization") String auth);

    @GET("projects/{id}/milestones")
    Call<List<Milestone>> getProjectMilestones(@Path("id") int projectId,
                                               @Header("Authorization") String auth);


    // - - - - - users - - - - -
    @GET("users/{id}")
    Call<User> getSingleUser(@Path ("id") int userId,
                             @Header("Authorization") String auth);

    @GET
    Call<User> getSingleUserWithWholeUrl(@Url String url,
                                         @Header("Authorization") String auth);


}
