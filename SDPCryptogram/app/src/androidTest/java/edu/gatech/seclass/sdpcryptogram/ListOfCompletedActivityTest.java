package edu.gatech.seclass.sdpcryptogram;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.equalTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * IMPORTANT NOTE
 *
 * If you're running these tests, make sure that your emulator screen is turned on, otherwise the
 * tests will fail!
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListOfCompletedActivityTest {

    //we can start by putting in a user into the system
    private String un = "playerTestCompleted";
    private String fn = "sdfsdf";
    private String ln = "dfgdfgdfg";
    private String em = "asrettu@gmail.com";

    private String cname = "completeMe";
    private String cencoded = "PjndJ Tejr 15%"; //a->j, b->t
    private String cactual = "PandA Bear 15%";
    private int numAttempts = 3;

    private final int cryptsToCreate = 3; //don't change this

    //We start from the home screen and simply insert into the DB a couple of cryptograms
    @Rule
    public ActivityTestRule<PlayerHomeScreenActivity> mActivityRule =
            new ActivityTestRule<PlayerHomeScreenActivity>(PlayerHomeScreenActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, PlayerHomeScreenActivity.class);
                    result.putExtra("CurrentUser", un);
                    return result;
                }
            };

    @Test
    public void solvingCryptogramInsertsIntoCompletedTable() {
        //go to the unsolved cryptograms, click one.
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"0"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and pass
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //now we should be on the home screen, so go to completed ones
        onView(withId(R.id.button_playerHomeScreen_ListOfCompletedCryptogram)).perform(click());

        onData(hasToString(equalTo(cname+"0"))).inAdapterView(withId(R.id.listview_listCompleted_CryptogramNameColumn)).check(matches(isDisplayed()));
    }

    @Test
    public void FailingInsertsIntoCompletedTable() {
        //go to the unsolved cryptograms, click one.
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"1"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Z"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());

        //fail it
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //now we should be on the home screen, so go to completed ones
        onView(withId(R.id.button_playerHomeScreen_ListOfCompletedCryptogram)).perform(click());

        onData(hasToString(equalTo(cname+"1"))).inAdapterView(withId(R.id.listview_listCompleted_CryptogramNameColumn)).check(matches(isDisplayed()));
    }

    public void maybeInsertPlayer() {
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        if (!myDB.playerExists(un, fn, ln, em)) {
            myDB.insertPlayer(un, fn, ln, em);
        }
        myDB.closeDatabase();
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
