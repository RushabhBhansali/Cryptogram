package edu.gatech.seclass.sdpcryptogram;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import org.junit.Rule;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

/**
 * IMPORTANT NOTE
 *
 * If you're running these tests, make sure that your emulator screen is turned on, otherwise the
 * tests will fail!
 */

@RunWith(AndroidJUnit4.class)
@LargeTest


public class StatActivityTest {

    //we can start by putting in a user into the system
    private String un = "playerStat";
    private String cname = "statCrypts";
    private String cencoded = "potato kitty"; //a->j, b->t
    private String cactual = "potato kitty";
    private int numAttempts = 3;
    private int cryptsToInsert = 1;

    //We start from the care
    @Rule
    public ActivityTestRule<NewUserActivity> mActivityRule = new ActivityTestRule<>(
            NewUserActivity.class);

    @Test
    public void endToEndTopThreeVerificationForCreatedCryptogram() {
        //type them into the appropriate edit text fields

        maybeInsertCryptograms(cryptsToInsert);
        int usersToInsert = 4;

        //four times we will create a unique user, solve the cryptogram
        for (int i = 0; i < usersToInsert; i++) {
            onView(withId(R.id.newUserName)).perform(typeText(un + String.valueOf(i)), closeSoftKeyboard());
            onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
            onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
            onView(withId(R.id.newUserEmail)).perform(typeText("abc@gmail.com"), closeSoftKeyboard());

            //click the create user button
            onView(withId(R.id.runButton)).perform(click());

            //login with the user
            //now we're at the login screen
            onView(withId(R.id.edittext_login_screen_username)).perform(typeText(un + String.valueOf(i)), closeSoftKeyboard());

            //click login button
            onView(withId(R.id.button_login_screen_LogIn)).perform(click());

            //go to unsolved table
            onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());

            //click the cryptogram we just created
            onData(hasToString(equalTo(cname+"0"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

            //solve the trivial cryptogram
            onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

            //switch user
            onView(withId(R.id.button_playerHomeScreen_SwitchUser)).perform(click());

            //create new user, unless at the last one in the loop
            if (i<usersToInsert-1) {
                onView(withId(R.id.GoNewUserActivity)).perform(click());
            }
        }

        //login as any of the users we just created
        onView(withId(R.id.GoLoginActivity)).perform(click());
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText(un+"0"), closeSoftKeyboard());
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());

        //go to stat creation screen
        onView(withId(R.id.button_playerHomeScreen_CryptogramStatistics)).perform(click());

        //select the cryptogram
        onData(hasToString(equalTo(cname+"0"))).inAdapterView(withId(R.id.listview_listCompleted_CryptogramNameColumn)).perform(click());

        //you should view manually that it has been solved four times, but verify the third solver in the list is player cname2
        onView(withId(R.id.textView_SingleCryptogramStat_ThirdSolver)).check(matches(withText(un+"2")));
    }

    public void maybeInsertCryptograms(int toInsert) {
        boolean inserted;
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        for (int i=0; i<toInsert; i++) {
            if (!myDB.cryptogramExists(cname+String.valueOf(i),cactual, cencoded, numAttempts)) {
                inserted = myDB.insertCryptogram(cname+String.valueOf(i),cactual, cencoded, numAttempts);
            }
        }

        myDB.closeDatabase();
    }

}
