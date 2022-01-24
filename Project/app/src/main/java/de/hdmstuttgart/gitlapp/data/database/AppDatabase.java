package de.hdmstuttgart.gitlapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.hdmstuttgart.gitlapp.models.Issue;
/*import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Note;
import de.hdmstuttgart.gitlapp.models.Profile;
import de.hdmstuttgart.gitlapp.models.Project;*/
import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Note;
import de.hdmstuttgart.gitlapp.models.Profile;
import de.hdmstuttgart.gitlapp.models.Project;
import de.hdmstuttgart.gitlapp.models.User;

@Database(entities = {Issue.class, Label.class, Milestone.class, Note.class, Profile.class, Project.class, User.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    //Singleton access bc each db object is expensive (its recommended to implement this as a singleton on android.developers.com)
    public static AppDatabase getDatabaseInstance(Context context) {
        synchronized (AppDatabase.class){
        if(INSTANCE == null){
                INSTANCE = Room
                        .databaseBuilder(context,AppDatabase.class,"app-database")
                        .allowMainThreadQueries() //todo remove
                        .build();
            }
        }
       return INSTANCE;
    }

    public abstract IssueDao issueDao();
    public abstract LabelDao labelDao();
    public abstract MilestoneDao milestoneDao();
    public abstract NoteDao noteDao();
    public abstract ProfileDao profileDao();
    public abstract ProjectDao projectDao();
    public abstract UserDao userDao();
}
