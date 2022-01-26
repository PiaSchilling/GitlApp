package de.hdmstuttgart.gitlapp.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdmstuttgart.gitlapp.R;
import de.hdmstuttgart.gitlapp.models.Issue;

public class IssueListAdapter extends ListAdapter<Issue, IssueListAdapter.ViewHolder> {

    private final OnIssueClickListener issueClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView issueTitleTextView;
        private final TextView createdDateTextView;
        private final TextView authorTextView;
        private final TextView dueDateTextView;
        private final TextView iidTextView;
        private final ImageView statusBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            issueTitleTextView = itemView.findViewById(R.id.issue_name);
            createdDateTextView = itemView.findViewById(R.id.time_string);
            authorTextView = itemView.findViewById(R.id.author_name);
            dueDateTextView = itemView.findViewById(R.id.date_posted);
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

        public TextView getIidTextView() {
            return iidTextView;
        }

        public ImageView getStatusBar() {
            return statusBar;
        }
    }

    public IssueListAdapter(List<Issue> issueList, OnIssueClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.submitList(issueList);
        Log.e("Bug",getCurrentList().toString());
        this.issueClickListener = clickListener;
    }

    public static final DiffUtil.ItemCallback<Issue> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Issue>() {
                @Override
                public boolean areItemsTheSame( //todo understand
                        @NonNull Issue oldIssue, @NonNull Issue newIssue) {
                    return oldIssue.getId() == newIssue.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Issue oldIssue, @NonNull Issue newIssue) {
                    return oldIssue.equals(newIssue);
                }
            };


    @NonNull
    @Override
    public IssueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_issue_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueListAdapter.ViewHolder holder, int position) {

        Issue issueOnPosition = getCurrentList().get(position);
        Context context = holder.getCreatedDateTextView().getContext();

        String issueTitle = issueOnPosition.getTitle();
        String createdDate = issueOnPosition.getCreated_at();
        String authorName = issueOnPosition.getAuthor().getName();
        String dueDate = issueOnPosition.getDue_date();
        int iid = issueOnPosition.getIid();

        holder.getIssueTitleTextView().setText(issueTitle);
        holder.getCreatedDateTextView().setText(context.getString(R.string.issue_create_date, createdDate));
        holder.getAuthorTextView().setText(authorName);
        holder.getDueDateTextView().setText(context.getString(R.string.issue_due_date, dueDate));
        holder.getIidTextView().setText(context.getString(R.string.issue_iid, iid));

        if (dueDate == null || dueDate.isEmpty()) {
            holder.getDueDateTextView().setText("");
        }

        if (issueOnPosition.getState().equals("opened")) {
            holder.getStatusBar().setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.getStatusBar().setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }

        //onclick on listview item
        holder.itemView.setOnClickListener(view -> issueClickListener.onIssueClick(issueOnPosition));

    }

}
