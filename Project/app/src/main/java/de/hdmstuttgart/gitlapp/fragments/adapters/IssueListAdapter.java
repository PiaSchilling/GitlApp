package de.hdmstuttgart.gitlapp.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.fragments.IssueDetailFragment;
import de.hdmstuttgart.gitlapp.fragments.IssueOverviewFragment;
import de.hdmstuttgart.gitlapp.models.Issue;

public class IssueListAdapter extends RecyclerView.Adapter<IssueListAdapter.ViewHolder>{

    private final List<Issue> issueList;
    private final FragmentActivity activity; //only for fragment transaction

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView issueTitleTextView;
        private final TextView createdDateTextView;
        private final TextView authorTextView;
        private final TextView dueDateTextView;
        private final ImageView calendarIcon;
        private final TextView iidTextView;
        private final ImageView statusBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            issueTitleTextView = itemView.findViewById(R.id.issue_name);
            createdDateTextView = itemView.findViewById(R.id.time_string);
            authorTextView = itemView.findViewById(R.id.author_name);
            dueDateTextView = itemView.findViewById(R.id.date_posted);
            calendarIcon = itemView.findViewById(R.id.calendar_icon);
            iidTextView = itemView.findViewById(R.id.issue_iid_card);
            statusBar = itemView.findViewById(R.id.status_bar);


        }

        public TextView getIssueTitleTextView() {
            return issueTitleTextView;
        }

        public TextView getCreatedDateTextView() {
            return createdDateTextView;
        }

        public TextView getAuthorTextView() {
            return authorTextView;
        }

        public TextView getDueDateTextView() {
            return dueDateTextView;
        }

        public ImageView getCalendarIcon() {
            return calendarIcon;
        }

        public TextView getIidTextView() {
            return iidTextView;
        }

        public ImageView getStatusBar() {
            return statusBar;
        }
    }

    public IssueListAdapter (List<Issue> issueList, FragmentActivity activity){
        this.issueList = issueList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public IssueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_issue_card,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueListAdapter.ViewHolder holder, int position) {

        Issue issueOnPosition = issueList.get(position);
        Context context = holder.getCreatedDateTextView().getContext();

        String issueTitle = issueOnPosition.getTitle();
        String createdDate = issueOnPosition.getCreated_at();
        String authorName = issueOnPosition.getAuthor().getName();
        String dueDate = issueOnPosition.getDue_date();
        int iid = issueOnPosition.getIid();

        holder.getIssueTitleTextView().setText(issueTitle);
        holder.getCreatedDateTextView().setText(context.getString(R.string.issue_create_date,createdDate));
        holder.getAuthorTextView().setText(authorName);
        holder.getDueDateTextView().setText(context.getString(R.string.issue_due_date,dueDate));
        holder.getIidTextView().setText(context.getString(R.string.issue_iid,iid));

        if(dueDate == null || dueDate.isEmpty()){
            holder.getDueDateTextView().setText(""); //todo remove due date
        }

        if(issueOnPosition.getState().equals("opened")){
            holder.getStatusBar().setBackgroundColor(ContextCompat.getColor(context,R.color.green));
        }else{
            holder.getStatusBar().setBackgroundColor(ContextCompat.getColor(context,R.color.red));
        }

        //onclick on listview item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("issueId",issueOnPosition.getId());

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, IssueDetailFragment.class, bundle)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }


}
