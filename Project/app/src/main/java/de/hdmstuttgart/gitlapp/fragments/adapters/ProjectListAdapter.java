package de.hdmstuttgart.gitlapp.fragments.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.fragments.IssueDetailFragment;
import de.hdmstuttgart.gitlapp.fragments.IssueOverviewFragment;
import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private final List<Project> projectList;
    private FragmentActivity activity;


    public static class ViewHolder extends RecyclerView.ViewHolder{


        private final TextView projectNameTextView;
        private final ImageView projectAvatar;
        private final TextView projectInitials;



        public TextView getProjectNameTextView() {
            return projectNameTextView;
        }

        public ImageView getProjectAvatar(){
            return projectAvatar;
        }

        public TextView getProjectInitials() {
            return projectInitials;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.projectTitle);
            projectAvatar = itemView.findViewById(R.id.avatarProject);
            projectInitials = itemView.findViewById(R.id.initialsProject);
        }
    }

    public ProjectListAdapter (List<Project> projectList, FragmentActivity activity){
        this.projectList = projectList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProjectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_menu_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ViewHolder holder, int position) {
        Project projectOnPosition = projectList.get(position);
        String projectName = projectOnPosition.getName();

        // set Text
        holder.getProjectNameTextView().setText(projectName);


        // set Image
        Glide.with(holder.getProjectNameTextView().getContext())
                .load(projectOnPosition.getAvatar_url())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("ST-", "load failed" + projectOnPosition.getName());
                        holder.getProjectInitials().setText(projectName.substring(0,2));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .fitCenter()
                .error(R.drawable.ic_circle)
                .into(holder.projectAvatar);



        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("projectName", projectOnPosition.getName());
            bundle.putInt("projectId",projectOnPosition.getId());

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, IssueOverviewFragment.class, bundle)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}