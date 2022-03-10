# GitlApp

GitLapp is a small android app for managing GitLab issues. It was developed as part of a university project in a team of two students. 



### What GitlApp can do

- easy connection to (custom) Gitlab servers 
- user-friendly forwarding to generate an access token
- access to all projects in which you are a member 
- quick overview of all project issues within a project filtered by its status
- view issue details like due date, author, milestone, labels, title and description
- add new issues to a project with title, description, due date, select labels and milestones (which are already defined for the project) 
- convenient closing of an issue

![Screens](https://user-images.githubusercontent.com/96486990/157705869-31e6f385-f365-49e2-8c67-3e7a899c819b.png)


### Requirements

The Android devices requires at least API 26 (Android 8.0 Oreo) or higher. 

### Technolgy Stack
 
 - [Retrofit](https://square.github.io/retrofit/) - for Gitlab API communication
 - androidx [Room](https://developer.android.com/jetpack/androidx/releases/room), - for local data caching 
 - [Glide](https://github.com/bumptech/glide) - for loading images
 - [JUnit](https://junit.org/junit5/) - for simple unit tests
 - several other androidx libraries 

### Known issues

- Issues that are deleted in the browser still exist in GitlApp
- DB actions are executed in the UI thread, which should actually be done in a separate background thread
- inefficient way of working: with onConflictStrategy.REPLACE all data in the DB is overwritten every time and not only the changes are updated. API calls should also be reduced (sometimes duplicate API calls)
- access token is stored unencrypted in the DB, which is a security hole. Should be stored encrypted in SharedPreferences.
- Difficulties in testing, because the fragments are tightly coupled with the ViewModels (Mock- Repository difficult to include for testing)



### Developers

Pia Schilling 
Sara Tietze 
