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
public class PlayCryptogramActivityTest {

    //we can start by putting in a user into the system
    private String un = "player123456";
    private String fn = "sdfsdf";
    private String ln = "dfgdfgdfg";
    private String em = "asrettu@gmail.com";

    int cryptsToCreate = 5;

    private String cname = "cryptSolveMe";
    private String cencoded = "PjndJ Tejr 15%"; //a->j, b->t
    private String cactual = "PandA Bear 15%";
    private int cNumAttempts = 3;

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
    public void enteringSubstitutionCorrectlyUpdatedDisplayPhrase() {
        //go to the unsolved cryptograms, click one.
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"0"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());

        onView(withId(R.id.textView_PlayCryptogram_decodedPhrase)).check(matches(withText("PandA Tear 15%")));
    }

    @Test
    public void incorrectSolutionCorrectlyDecrementsRemainingAttempts() {
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);
        //go to the unsolved cryptograms, click one.
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"1"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        onView(withId(R.id.textView_PlayCryptogram_AttemptsRemaining)).check(matches(withText("2")));
    }

    @Test
    public void incorrectSolutionCorrectlyIncrementsAttemptsTaken() {
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        //go to the unsolved cryptograms, click one.
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"2"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        onView(withId(R.id.textView_PlayCryptogram_AttemptsTaken)).check(matches(withText("1")));
    }

    @Test
    public void maximumIncorrectSolutionsBootsUser() {
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        //go to the unsolved cryptograms, click one.
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"3"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());

        //submit three times
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //verify we're back at the home screen
        onView(withId(R.id.button_playerHomeScreen_CreateNewCryptogram)).check(matches(isDisplayed()));
    }

    @Test
    public void correctSolutionBeforeLastAttemptBootsUser() {
        maybeInsertPlayer();
        maybeInsertCryptograms(cryptsToCreate);

        //go to the unsolved cryptograms, click one.
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo(cname+"4"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //substitute some letters and fail
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());

        //submit incorrectly once
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //now get it right
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //verify we're back at the home screen
        onView(withId(R.id.button_playerHomeScreen_CreateNewCryptogram)).check(matches(isDisplayed()));
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
            if (!myDB.cryptogramExists(cname+String.valueOf(i),cactual, cencoded, cNumAttempts)) {
                inserted = myDB.insertCryptogram(cname+String.valueOf(i),cactual, cencoded, cNumAttempts);
            }
        }

        myDB.closeDatabase();
    }

    public void deleteCryptograms() {
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        myDB.removeAllCryptograms();
        myDB.closeDatabase();
    }

}
