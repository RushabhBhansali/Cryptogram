package edu.gatech.seclass.sdpcryptogram;

import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import org.junit.Rule;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.typeText;

/**
 * IMPORTANT NOTE
 *
 * If you're running these tests, make sure that your emulator screen is turned on, otherwise the
 * tests will fail!
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    //we can start by putting in a user into the system
    private String firstName = "Billy";
    private String lastName = "Banana";
    private String email = "nanners@gmail.com";
    private String username = "Bananerman";

    @Rule
    public ActivityTestRule<NewUserActivity> mActivityRule = new ActivityTestRule<>(
            NewUserActivity.class);

    @Test
    public void loginWithExistingPlayerTakesUsToHomeScreen() {

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(email), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now we're at the login screen
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText(username), closeSoftKeyboard());

        //click login button
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());

        //now verify that we made it to the home screen by just checking an element from it is there
        onView(withId(R.id.button_playerHomeScreen_CreateNewCryptogram)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithNonExistingPlayerGivesError() {

        String USERNAME_DOES_NOT_EXIST = "This username doesn't exist";

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(username+"2"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(email), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now we're at the login screen. login with a nonexistant username, or try to.
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText("grazieadiochecristianoebianconero"), closeSoftKeyboard());

        //click login button
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());

        //now verify that we made it to the home screen by just checking an element from it is there
        onView(withId(R.id.edittext_login_screen_username)).check(matches(hasErrorText(USERNAME_DOES_NOT_EXIST)));
    }
}
