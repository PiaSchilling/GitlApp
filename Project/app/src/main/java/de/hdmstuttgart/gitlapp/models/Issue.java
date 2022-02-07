package de.hdmstuttgart.gitlapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "issues")
public class Issue {

    @PrimaryKey
    private int id;
    private int iid;

    private int weight;
    private String title;
    private String description;
    @Ignore
    private List<Label> labels = new ArrayList<>();

    @Ignore
    private User author;
    private int author_id;
    private int project_id;

    private int thumbs_up;
    private int thumbs_down;

    private int hashcode = this.hashCode(); //todo remove

    private String state;

    private String created_at;
    private String due_date;

    @Ignore
    private Milestone milestone;
    private int milestone_id;



    @Ignore
    public Issue(int id, int iid, int weight, String title, String description, User author, int author_id, int project_id, int thumbs_up, int thumbs_down, String state) {
        this.id = id;
        this.iid = iid;
        this.weight = weight;
        this.title = title;
        this.description = description;
        this.author = author;
        this.author_id = author_id;
        this.project_id = project_id;
        this.thumbs_up = thumbs_up;
        this.thumbs_down = thumbs_down;
        this.state = state;
    }

    public Issue() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", iid=" + iid + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return id == issue.id && weight == issue.weight && hashcode == issue.hashcode && thumbs_up == issue.thumbs_up && thumbs_down == issue.thumbs_down && project_id == issue.project_id && author_id == issue.author_id && Objects.equals(title,issue.title) && Objects.equals(description,issue.description) && Objects.equals(state,issue.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weight, title, description, thumbs_up, thumbs_down, state);
    }

    // - - - - - - getter and setter(for room) - - - - - -

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getThumbs_up() {
        return thumbs_up;
    }

    public void setThumbs_up(int thumbs_up) {
        this.thumbs_up = thumbs_up;
    }

    public int getThumbs_down() {
        return thumbs_down;
    }

    public void setThumbs_down(int thumbs_down) {
        this.thumbs_down = thumbs_down;
    }

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public int getMilestone_id() {
        return milestone_id;
    }

    public void setMilestone_id(int milestone_id) {
        this.milestone_id = milestone_id;
    }
}


