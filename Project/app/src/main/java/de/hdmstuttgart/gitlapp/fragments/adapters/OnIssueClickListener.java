package de.hdmstuttgart.gitlapp.fragments.adapters;

import de.hdmstuttgart.gitlapp.models.Issue;

public interface OnIssueClickListener { //todo implement this also for project list

    /**
     * defines click action when clicking an issue list element
     * @param issue the issue which is represented by the list item view
     */
    void onIssueClick(Issue issue);

}
