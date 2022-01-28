package de.hdmstuttgart.gitlapp.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.gitlapp.models.Label;
import de.hdmstuttgart.gitlapp.models.Milestone;
import de.hdmstuttgart.gitlapp.models.Project;

public interface IProjectRepository {

    void initProjects();

    void fetchProjects();

    MutableLiveData<List<Project>> getProjectsLiveData();

    MutableLiveData<String> getNetworkCallMessage();

    void fetchProjectLabels(int projectId);

    void fetchProjectMilestones(int projectId);

    MutableLiveData<List<Label>> getLabelsLiveData(int projectId);

    MutableLiveData<List<Milestone>> getMilestoneLiveData(int projectId);
}
