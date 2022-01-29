package de.hdmstuttgart.gitlapp.fragments.adapters;

import de.hdmstuttgart.gitlapp.models.Project;

public interface OnProjectClickListener {


    /**
     * defines click action when clicking an issue list element
     * @param project the project which is represented in the list view
     */
    void onProjectClick(Project project);
}
