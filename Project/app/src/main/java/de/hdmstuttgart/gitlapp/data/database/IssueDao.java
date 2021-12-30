package de.hdmstuttgart.gitlapp.data.database;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Arrays;
import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;

@Dao
public abstract class IssueDao {
    //todo handle unique constraint failed exception

    @Insert(onConflict = OnConflictStrategy.REPLACE) //onConflict is handled by insertOrUpdate (but to avoid app crash REPLACE if something went wrong)
    public abstract void insertIssues(Issue ... issues);

    @Update
    public abstract int updateIssues(Issue ... issues);

    @Delete
    public abstract int deleteIssues(Issue ... issues);

    @Query("SELECT * FROM issues")
    public abstract List<Issue> getAllIssues();

    @Query("SELECT * FROM issues WHERE project_id = :projectId")
    public abstract List<Issue> getProjectIssues(int projectId);

    @Query("SELECT id FROM issues WHERE hashcode = :hashcode")
    public abstract Integer getIssueByHashcode(int hashcode);

    @Query("SELECT id FROM issues WHERE id = :id")
    public abstract Integer getIssueById(int id);

    @Query("SELECT * FROM issues WHERE author_id = :userId")
    public abstract List<Issue> issuesByUser(int userId);


    public void insertOrUpdate(List<Issue> issues){
        //todo does not work, remove or change
        for (Issue issue : issues){
            Integer temp = issue.hashCode();
            if(getIssueById(issue.getId()) == null){
                Log.d("DataLabel","IssueId not present, insert issue");
                insertIssues(issue); //issue is not present
            }else if(getIssueByHashcode(temp) == null){
                Log.d("DataLabel","IssueId present and checksum different, update issue");
                updateIssues(issue); //issue is present but checksum is different
            }else{
                //do nothing if checksum is same
                Log.d("DataLabel","IssueId present and checksum same, nothing updated");
            }
        }
    }
}
