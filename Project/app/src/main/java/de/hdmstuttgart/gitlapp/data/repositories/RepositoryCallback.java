package de.hdmstuttgart.gitlapp.data.repositories;

import androidx.lifecycle.LiveData;

public interface RepositoryCallback<T> {

    void onComplete(LiveData<T> result);

}
