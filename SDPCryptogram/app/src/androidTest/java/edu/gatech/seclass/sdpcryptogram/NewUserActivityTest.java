package edu.gatech.seclass.sdpcryptogram;

import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
public class NewUserActivityTest {

    private String firstName = "Bruce";
    private String lastName = "Banner";
    private String email = "hulk@incredible.com";
    private String username = "StrongestAvenger";

    @Rule
    public ActivityTestRule<NewUserActivity> mActivityRule = new ActivityTestRule<>(
            NewUserActivity.class);

    @Test
    public void invalidEmailDisplaysError() {
        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText("mwerpoismqwouwermdfop"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText("abc"), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());
        onView(withId(R.id.newUserEmail)).check(matches(hasErrorText("Not a valid email address!")));
    }

    @Test
    public void repetitiveUserNameDisplaysError() {
        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText("abc@gmail.com"), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent myIntent = new Intent(targetContext, NewUserActivity.class);

        //relaunch it and do it again
        mActivityRule.launchActivity(myIntent);

        //retype them
        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText("abc@gmail.com"), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());
        onView(withId(R.id.newUserName)).check(matches(hasErrorText("Username already exists")));
    }

    @Test
    //make an invalid first name, click the create button, make sure the appropriate error is set on the
    public void invalidFirstNameDisplaysError() {

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(email), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now make sure that we got the error about the field not being appropriate
        onView(withId(R.id.newUserFirstName)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    //make an invalid first name, click the create button, make sure the appropriate error is set on the
    public void invalidUserNameDisplaysError() {

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(email), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now make sure that we got the error about the field not being appropriate
        onView(withId(R.id.newUserName)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    //make an invalid first name, click the create button, make sure the appropriate error is set on the
    public void invalidLastNameDisplaysError() {

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(email), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now make sure that we got the error about the field not being appropriate
        onView(withId(R.id.newUserLastName)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    //make an invalid first name, click the create button, make sure the appropriate error is set on the
    public void missingEmailDisplaysError() {

        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(""), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //now make sure that we got the error about the field not being appropriate
        onView(withId(R.id.newUserEmail)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    //make a valid user, make sure the DB has exactly that user in it
    public void validUserProperlyInsertsIntoDB() {

        String un = "validNewUser";
        String fn = "potato";
        String ln = "big potato 123";
        String em = "tater@potato.com";

        //insert the user
        //type them into the appropriate edit text fields
        onView(withId(R.id.newUserName)).perform(typeText(un), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText(fn), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText(ln), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText(em), closeSoftKeyboard());

        //click the create user button
        onView(withId(R.id.runButton)).perform(click());

        //We should now be able to see the cryptogram in the database
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        boolean success = myDB.playerExists(un, fn, ln, em);
        myDB.closeDatabase();

        Assert.assertEquals(success, true);
    }


}





