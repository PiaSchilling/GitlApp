package de.hdmstuttgart.gitlapp.fragments.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.models.Project;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private final List<Project> projectList;
    private final OnProjectClickListener clickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder{


        private final TextView projectNameTextView;
        private final ImageView projectAvatar;
        private final TextView projectInitials;
        private final TextView ownerNameTextView;



        public TextView getProjectNameTextView() {
            return projectNameTextView;
        }

        public TextView getProjectInitials() {
            return projectInitials;
        }

        public TextView getOwnerNameTextView() {
            return ownerNameTextView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.projectTitle);
            projectAvatar = itemView.findViewById(R.id.avatarProject);
            projectInitials = itemView.findViewById(R.id.initialsProject);
            ownerNameTextView = itemView.findViewById(R.id.projectOwnerName);
        }
    }

    public ProjectListAdapter (List<Project> projectList, OnProjectClickListener clickListener){
        this.projectList = projectList;
        this.clickListener = clickListener;
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
        String ownerName = projectOnPosition.getOwner_name();

        // set Text
        holder.getProjectNameTextView().setText(projectName);
        holder.getOwnerNameTextView().setText(ownerName);


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


        holder.itemView.setOnClickListener(v -> clickListener.onProjectClick(projectOnPosition));
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}