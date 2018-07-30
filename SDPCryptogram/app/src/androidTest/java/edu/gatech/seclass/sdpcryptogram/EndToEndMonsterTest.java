package edu.gatech.seclass.sdpcryptogram;

import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import org.junit.Rule;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
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
 *
 *
 * The purpose of this class is simple. It has one giant, giant method that will represent a long series of actions
 * in the program. If there is an issue, it is very, very likely this method will fail somewhere, and not reach the final
 * assertion statement.
 *
 * This will also help you get an idea of how the app works, if you've never seen it before or are confused somewhere in the docs.
 *
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndToEndMonsterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void monsterTest() {

        //some back button functionality
        onView(withId(R.id.GoNewUserActivity)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.GoLoginActivity)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.GoNewUserActivity)).perform(click());

        //create the first user, and login with them
        onView(withId(R.id.newUserName)).perform(typeText("monsterUser1"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText("abc@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.runButton)).perform(click());
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText("monsterUser1"), closeSoftKeyboard());
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());

        //create a cryptogram
        onView(withId(R.id.button_playerHomeScreen_CreateNewCryptogram)).perform(click());


        /*
        Create a cryptogram with this user, the cryptogram will be as follows

        This Cryptogram Will be Tough123%!    <--- Real

        Replace t->g, g->t, w->d, l->h
        Ghis Crypgotram Dihh be Gouth123%!    <--- This should fail because of the l->h violation unsolvability

        mapping h->x will make it solvable
        Gxis Crypgotram Dihh be Goutx123%!
        */

        //should fail without name
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("monsterCrypto1"), closeSoftKeyboard());

        //should fail without solution
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText("This Cryptogram Will be Tough123%!"), closeSoftKeyboard());

        //set max attempts to zero
        onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(clearText(), closeSoftKeyboard());

        //substitute some letters to make an unsolvable cryptogram
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("W"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("L"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //this won't work
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //so then make it solvable
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("X"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //still shouldn't work because max is zero
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());
        onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(typeText("4"), closeSoftKeyboard());

        //this will work
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //should see it in there, then go back
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        Espresso.pressBack();

        //switch users
        onView(withId(R.id.button_playerHomeScreen_SwitchUser)).perform(click());

        onView(withId(R.id.GoNewUserActivity)).perform(click());

        //create the second user and login with them
        onView(withId(R.id.newUserName)).perform(typeText("monsterUser1"), closeSoftKeyboard());
        onView(withId(R.id.newUserFirstName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserLastName)).perform(typeText("abc"), closeSoftKeyboard());
        onView(withId(R.id.newUserEmail)).perform(typeText("abc@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.runButton)).perform(click()); //should fail because name is not unique

        //should login as user just fine after switching username
        onView(withId(R.id.newUserName)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.newUserName)).perform(typeText("monsterUser2"), closeSoftKeyboard());
        onView(withId(R.id.runButton)).perform(click()); //should fail because name is not unique
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText("monsterUser2"), closeSoftKeyboard());
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());



        //make another cryptogram with the same name
        //create a cryptogram
        onView(withId(R.id.button_playerHomeScreen_CreateNewCryptogram)).perform(click());
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("monsterCrypto1"), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText("This Cryptogram Will be Tough123%!"), closeSoftKeyboard());

        //make it solvable
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("W"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("L"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("X"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click()); //attempt to save, but name isn't unique

        //fix name
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("monsterCrypto2"), closeSoftKeyboard());
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //try to solve the first cryptogram by getting it from the unsolved list
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo("monsterCrypto1"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //hit the back button, and go right back to it
        Espresso.pressBack();
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo("monsterCrypto1"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //get it wrong completely all four times
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //try to solve the second one
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo("monsterCrypto2"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //solve it
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("W"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("L"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("X"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //go see it in the stats, see your name, then go back twice to the home screen
        onView(withId(R.id.button_playerHomeScreen_CryptogramStatistics)).perform(click());
        onData(hasToString(equalTo("monsterCrypto2"))).inAdapterView(withId(R.id.listview_listCompleted_CryptogramNameColumn)).perform(click());
        Espresso.pressBack();
        Espresso.pressBack();

        //switch back to user 1
        onView(withId(R.id.button_playerHomeScreen_SwitchUser)).perform(click());
        onView(withId(R.id.GoLoginActivity)).perform(click());

        //login with a name that doesn't exist, clear it, then log in as user 1
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText("monsterUser3"), closeSoftKeyboard());
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());
        onView(withId(R.id.edittext_login_screen_username)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.edittext_login_screen_username)).perform(typeText("monsterUser1"), closeSoftKeyboard());
        onView(withId(R.id.button_login_screen_LogIn)).perform(click());

        //go solve the second cryptogram
        //try to solve the second one
        onView(withId(R.id.button_playerHomeScreen_ListOfUnsolvedCryptograms)).perform(click());
        onData(hasToString(equalTo("monsterCrypto2"))).inAdapterView(withId(R.id.listview_unsolvedCryptogram_cryptogramNameColumn)).perform(click());

        //solve it
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("G"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("W"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("L"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("X"))).perform(click());
        onView(withId(R.id.Spinner_PlayCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("H"))).perform(click());
        onView(withId(R.id.Button_PlayCryptogram_NewLetterSave)).perform(click());
        onView(withId(R.id.button_PlayCryptogram_attemptSolution)).perform(click());

        //see it in the completed list
        onView(withId(R.id.button_playerHomeScreen_ListOfCompletedCryptogram)).perform(click());
        Espresso.pressBack();

        //go see it in the stats, verify user 1 is in position 2
        onView(withId(R.id.button_playerHomeScreen_CryptogramStatistics)).perform(click());
        onData(hasToString(equalTo("monsterCrypto2"))).inAdapterView(withId(R.id.listview_listCompleted_CryptogramNameColumn)).perform(click());
        onView(withId(R.id.textView_SingleCryptogramStat_SecondSolver)).check(matches(withText("monsterUser1")));

    }
}


