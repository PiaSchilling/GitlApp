package de.hdmstuttgart.gitlapp.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import de.hdmstuttgart.gitlapp.MainActivity;
import de.hdmstuttgart.gitlapp.R;

/**
 * basic ui test for login screen
 *
 * due to tight coupling between fragment and viewModel we were not able to mock data here
 * therefore testing is done with a gitlab account created specifically for this test (which contains only a test project)
 * we know that it is not a good idea to hardcode the access token here (to be on the safe side, the token has been created with an expiration date of february 28th)
 *
 * log out or clear app data before testing otherwise login screen will not be shown
 */
public class LoginFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoginFragment() {

        onView(withId(R.id.gitlab_title)).check(matches(isDisplayed()));


        onView(withId(R.id.openUrl_button))
                .perform(click()); //should not open browser when host url is not set

        onView(withId(R.id.login_button))
                .perform(click()); //should not login with empty fields

        onView(withId(R.id.hostUrl_textField))
                .perform(click());

        onView(withId(R.id.hostUrl_editText))
                .perform(typeText("https://gitlab.com"), closeSoftKeyboard());

        onView(withId(R.id.userId_editText))
                .perform(click());

        onView(withId(R.id.userId_editText))
                .perform(typeText("10725890"), closeSoftKeyboard());

        onView(withId(R.id.accessToken_editText))
                .perform(click());

        onView(withId(R.id.accessToken_editText))
                .perform(typeText("glpat-MQyhAW81xUxn-JzofrW3"), closeSoftKeyboard()); //access token expires on february 28th

        onView(withId(R.id.login_button))
                .perform(click()); //should now login

        try { //wait for the api to respond
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.userNameLabel)).check(matches(withText("Pia Schilling")));


    }
}