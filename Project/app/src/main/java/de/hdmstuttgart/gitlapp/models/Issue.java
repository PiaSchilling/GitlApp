package de.hdmstuttgart.gitlapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "issues")
public class Issue {

    @PrimaryKey
    private int id;
    //private int iid;

    private int weight;
    private String title;
    private String description;
    private int hashcode;
    private int authorId; //FK

    @ColumnInfo(name = "thumbs_up")
    private int thumbsUp;
    @ColumnInfo(name = "thumbs_down")
    private int thumbsDown;

    private int project_id;

    @Ignore
    private State state;


    @Ignore
    public Issue(int id, int weight, int authorId, State state, String title, String description, int thumbsUp, int thumbsDown, int project_id) {
        this.id = id;
        this.weight = weight;
        this.authorId = authorId;
        this.state = state;
        this.title = title;
        this.description = description;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
        this.project_id = project_id;

        this.hashcode = hashCode();
    }

    public Issue(){

    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", weight=" + weight +
                ", title='" + title + '\'' +
                ", hashcode=" + hashcode +
                '}';
    }

    @Override//todo recreate for real issue implementation
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return id == issue.id && weight == issue.weight && hashcode == issue.hashcode && thumbsUp == issue.thumbsUp && thumbsDown == issue.thumbsDown && project_id == issue.project_id && authorId == issue.authorId && title.equals(issue.title) && description.equals(issue.description) && state == issue.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, title, description,thumbsUp, thumbsDown, project_id, authorId, state);
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public State getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAuthorId(int author_id) {
        this.authorId = author_id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
