package de.hdmstuttgart.gitlapp.data.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdmstuttgart.gitlapp.models.Issue;
import de.hdmstuttgart.gitlapp.models.State;
import de.hdmstuttgart.gitlapp.models.User;

public class DummyAPI {

    List<Issue> issues = new ArrayList<>();

    public DummyAPI(){
       /* User user = new User(1,"Pia Schilling","Schilling Pia","url","email@mail.com");

        Issue i1 = new Issue(2,1,1, State.CLOSED,"Issue one","Awesome issue",2,3,6414);
        Issue i2 = new Issue(3,1,1, State.CLOSED,"Issue one","Awesome issue",2,3,6414);
        Issue i3 = new Issue(4,1,1, State.CLOSED,"Issue one","Awesome issue",2,3,6414);

        Issue i4 = new Issue(5,1,2, State.CLOSED,"Issue one","Awesome issue",2,3,6414);
        Issue i5 = new Issue(6,1,2, State.CLOSED,"Issue one","Awesome issue",2,3,6414);

        issues.add(i1);
        issues.add(i2);
        issues.add(i3);*/
    }

    public List<Issue> getIssues(){
        return issues;
    }


}
