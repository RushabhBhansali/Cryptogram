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
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * IMPORTANT NOTE
 *
 * If you're running these tests, make sure that your emulator screen is turned on, otherwise the
 * tests will fail!
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateNewCryptogramTest {

    //we can start by putting in a user into the system
    private String un = "sldkfjsdlkfj";
    private String fn = "sdfsdf";
    private String ln = "dfgdfgdfg";
    private String em = "asrettu@gmail.com";

    @Rule
    public ActivityTestRule<CreateNewCryptogramActivity> mActivityRule =
            new ActivityTestRule<CreateNewCryptogramActivity>(CreateNewCryptogramActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, CreateNewCryptogramActivity.class);
                    result.putExtra("CurrentUser", un);
                    return result;
                }
    };

    @Test
    public void SubstitutionDisplayValid() {
        insertPlayer();

        String actual = "PandA bear 15%";
        String expected = "PjndJ tejr 15%";

        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("TestCrypto1"), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(actual), closeSoftKeyboard());
        //onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(typeText("4"), closeSoftKeyboard());

        //substitute a couple letters that will create a solvable cryptogram, such as A->J
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //and also let's do B->T
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //check the substitution looks right
        onView(withId(R.id.textView_CreateNewCryptogram_decodedPhrase)).check(matches(withText(expected)));
    }

    @Test
    public void RepetitiveCryptonameErrorDisplays() {
        insertPlayer();

        String EXPECTED_ERROR = "This name already exists";

        //insert a cryptogram real quick
        String cname = "potatokitty";
        String cunencoded = "taterdiddurd";
        String cencoded = "taterdiddurd";
        int cattempts = 13;

        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        boolean inserted = myDB.insertCryptogram(cname,cunencoded,cencoded,cattempts);
        myDB.closeDatabase();

        //now insert another one using the UI with the same cryptoname
        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText(cname), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(cunencoded), closeSoftKeyboard());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //verify the thrown error is correctly displayed
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).check(matches(hasErrorText(EXPECTED_ERROR)));
    }

    @Test
    public void MissingCryptoNameErrorDisplays() {
        insertPlayer();

        String EXPECTED_ERROR = "This field is required!";

        //insert a cryptogram real quick
        String cname = "";
        String cunencoded = "taterdiddurd";

        //now insert another one using the UI with the same cryptoname
        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText(cname), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(cunencoded), closeSoftKeyboard());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //verify the thrown error is correctly displayed
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).check(matches(hasErrorText(EXPECTED_ERROR)));
    }

    @Test
    public void MissingSolutionPhraseDisplaysError() {
        insertPlayer();

        String EXPECTED_ERROR = "This field is required!";

        //insert a cryptogram real quick
        String cname = "potato";
        String cunencoded = "";

        //now insert another one using the UI with the same cryptoname
        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText(cname), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(cunencoded), closeSoftKeyboard());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //verify the thrown error is correctly displayed
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).check(matches(hasErrorText(EXPECTED_ERROR)));
    }

    @Test
    public void ZeroAttemptsPhraseDisplaysError() {
        insertPlayer();

        String EXPECTED_ERROR = "Must be at least 1";

        //insert a cryptogram real quick
        String cname = "potato";
        String cunencoded = "potatopotato";

        //now insert another one using the UI with the same cryptoname
        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText(cname), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(cunencoded), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(clearText(), closeSoftKeyboard()).perform(typeText("0"), closeSoftKeyboard());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //verify the thrown error is correctly displayed
        onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).check(matches(hasErrorText(EXPECTED_ERROR)));
    }

    @Test
    public void CryptogramInsertsSuccessfullyToDB() {
        insertPlayer();

        String actual = "PandA bear 15%";
        String expected = "PjndJ tejr 15%";

        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("TestCrypto1"), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(actual), closeSoftKeyboard());
        //onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(typeText("4"), closeSoftKeyboard());

        //substitute a couple letters that will create a solvable cryptogram, such as A->J
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //and also let's do B->T
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("T"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //We should now be able to see the cryptogram in the database
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        boolean success = myDB.cryptogramExists("TestCrypto1", actual, expected, 3);
        myDB.closeDatabase();

        Assert.assertEquals(success, true);
    }

    @Test
    public void UnsolvableCryptogramNotInsertedIntoDB() {
        insertPlayer();

        String actual = "PandA bear 15%";
        String expected = "PjndJ jejr 15%";

        //type them into the appropriate edit text fields
        onView(withId(R.id.editText_CreateNewCryptogram_puzzleName)).perform(typeText("UnsolvableCrypto"), closeSoftKeyboard());
        onView(withId(R.id.editText_CreateNewCryptogram_solutionPhrase)).perform(typeText(actual), closeSoftKeyboard());
        //onView(withId(R.id.editText_CreateNewCryptogram_maxAttempts)).perform(typeText("4"), closeSoftKeyboard());

        //substitute a couple letters that will create a solvable cryptogram, such as A->J
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //and also let's do B->J, thus making an unsolvable cryptogram!
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterFrom)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());
        onView(withId(R.id.Spinner_CreateNewCryptogram_NewLetterTo)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("J"))).perform(click());
        onView(withId(R.id.Button_CreateNewCryptogram_NewLetterSave)).perform(click());

        //Create The Cryptogram
        onView(withId(R.id.button_CreateNewCryptogram_saveCryptogram)).perform(click());

        //We should now be able to see the cryptogram in the database
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        boolean success = myDB.cryptogramExists("UnsolvableCrypto", actual, expected, 3);
        myDB.closeDatabase();

        Assert.assertEquals(success, false);
    }

    public void insertPlayer() {
        DB myDB = new DB(InstrumentationRegistry.getInstrumentation().getTargetContext());
        if (!myDB.playerExists(un, fn, ln, em)) {
            myDB.insertPlayer(un, fn, ln, em);
        }
        myDB.closeDatabase();
    }
}
