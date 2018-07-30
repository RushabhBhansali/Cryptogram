package edu.gatech.seclass.sdpcryptogram;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class DatabaseTest extends AndroidTestCase {

    private DB myDB;

    // Some hardcoded test data to make things easier (should be in a properties file...)
    private static final String testUserName = "potato";
    private static final String testFirstName = "spud";
    private static final String testLastName = "tater";
    private static final String testEmail = "fryme@idaho.com";

    private static final String testUserName2 = "crinkle";
    private static final String testFirstName2 = "french";
    private static final String testLastName2 = "fry";
    private static final String testEmail2 = "totsalot@idaho.com";


    private static final String testCryptoName = "Pizza123@&%";
    private static final String testUnencodedPhrase = "I wish this project was done already !!! 123 @#$";
    private static final String testEncryptionKey = "J xjti uijt qspkfdu xbt epof bmsfbez !!! 123 @#$";
    private static final int testMaxSolutionAttempts = 5;

    // JUnit utility methods
    //TODO - see if we can get the @After call working with the mydb.resetdatabses() and mydb.closedatbase() calls.
    @Override
    public void setUp() throws Exception {
        super.setUp();
        myDB = new DB(mContext);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        myDB.resetDatabases();
        myDB.closeDatabase();
    }

    /*
     * DB class tests
     * 1. These generally follow the order of the methods in the class being tested.
     * 2. Use AssertTrue or AssertFalse, rather than operating on the return value.
     * 3. Naming convention: test_METHODNAME_SHORTDESCRIPTION
     */

    /*
     *
     * Generic unit tests for methods
     *
     */

    // Test consistency of getHelper() calls.
    @Test
    public void test_getHelper_Consistency() {
        DBHelper dbh1 = myDB.getHelper();
        DBHelper dbh2 = myDB.getHelper();

        assertTrue(dbh1.equals(dbh2));
    }

    // Test consistency of getDatabase() calls.
    @Test
    public void test_getDatabase_Consistency() {
        SQLiteDatabase sql1 = myDB.getDatabase();
        SQLiteDatabase sql2 = myDB.getDatabase();

        assertTrue(sql1.equals(sql2));
    }

    // Test consistency of database open calls.
    @Test
    public void test_databaseIsOpen_Consistency() {
        boolean open1 = myDB.databaseIsOpen();
        boolean open2 = myDB.databaseIsOpen();

        assertTrue(open1 == open2);
    }

    // Test successful call to close database
    @Test
    public void test_closeDatabase_Availability() {
        boolean isOpen = myDB.databaseIsOpen();

        if (isOpen) {
            myDB.closeDatabase();
            isOpen = myDB.databaseIsOpen();
        }

        assertTrue(isOpen == false);
    }

    /*
     *
     * insertPlayer tests
     *
     */

    //Test that if we insert a player with valid credentials, we get success back
    @Test
    public void test_insertPlayer_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);
    }

    // Test that if we insert a player with valid credentials, we get success back, but then if we
    // insert the same player, it fails
    @Test
    public void test_insertPlayer_Duplicate() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        //if we insert them again, it should fail on duplicate username
        success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertFalse(success);
    }

    // Test insert with ZLS User Name
    @Test
    public void test_insertPlayer_zlsUserName() {
        boolean success = myDB.insertPlayer("", testFirstName, testLastName, testEmail);
        assertFalse(success);
    }

    // Test insert with null User Name
    @Test
    public void test_insertPlayer_nullUserName() {
        boolean success = myDB.insertPlayer(null, testFirstName, testLastName, testEmail);
        assertFalse(success);
    }

    // Test insert with ZLS First Name
    @Test
    public void test_insertPlayer_zlsFirstName() {
        boolean success = myDB.insertPlayer(testUserName, "", testLastName, testEmail);
        assertFalse(success);
    }

    // Test insert with null First Name
    @Test
    public void test_insertPlayer_nullFirstName() {
        boolean success = myDB.insertPlayer(testUserName, null, testLastName, testEmail);
        assertFalse(success);
    }

    // Test insert with ZLS Last Name
    @Test
    public void test_insertPlayer_zlsLastName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, "", testEmail);
        assertFalse(success);
    }

    // Test insert with null Last Name
    @Test
    public void test_insertPlayer_nullLastName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, null, testEmail);
        assertFalse(success);
    }

    // Test insert with ZLS email
    @Test
    public void test_insertPlayer_zlsEmail() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, "");
        assertFalse(success);
    }

    // Test insert with null email
    @Test
    public void test_insertPlayer_nullEmail() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, null);
        assertFalse(success);
    }

    /*
     * playerExists tests
     */

    // Test successful insertion and existence check
    @Test
    public void test_playerExists_Validity() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);
        assertTrue(myDB.playerExists(testUserName, testFirstName, testLastName, testEmail));
    }

    // Test successful insertion and non-existence check
    @Test
    public void test_playerExists_Invalid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);
        assertFalse(myDB.playerExists("no", "no", "no", "no"));
    }

    /*
     * insertCryptogram tests
     */

    // Test - inserting valid Cryptogram call
    @Test
    public void test_insertCryptogram_Validity() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);
    }

    // Test - inserting with ZLS Crypto Name call
    @Test
    public void test_insertCryptogram_zlsName() {
        boolean success = myDB.insertCryptogram("", testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with null Crypto Name call
    @Test
    public void test_insertCryptogram_nullName() {
        boolean success = myDB.insertCryptogram(null, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with ZLS unencodedPhrase call
    @Test
    public void test_insertCryptogram_zlsUnencodedPhrase() {
        boolean success = myDB.insertCryptogram(testCryptoName, "", testEncryptionKey, testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with null unencodedPhrase call
    @Test
    public void test_insertCryptogram_nullUnencodedPhrase() {
        boolean success = myDB.insertCryptogram(testCryptoName, null, testEncryptionKey, testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with ZLS encryptionKey
    @Test
    public void test_insertCryptogram_zlsEncryptionKey() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, "", testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with null encryptionKey
    @Test
    public void test_insertCryptogram_nullEncryptionKey() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, null, testMaxSolutionAttempts);
        assertFalse(success);
    }

    // Test - inserting with zero maxAttempts
    @Test
    public void test_insertCryptogram_zeroMaxAttempts() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, 0);
        assertFalse(success);
    }

    // Test - inserting with negative maxAttempts
    @Test
    public void test_insertCryptogram_negativeMaxAttempts() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, -1);
        assertFalse(success);
    }

    @Test
    public void test_insertCryptogram_duplicateName() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, "abc", "xyz", testMaxSolutionAttempts);
        assertFalse(success);
    }

    /*
     * cryptogramExists tests
     */

    // Test successful insertion and existence
    @Test
    public void test_cryptogramExists_Valid() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.cryptogramExists(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);
    }

    // Test successful insertion and existence
    @Test
    public void test_cryptogramExists_Invalid() {
        boolean success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.cryptogramExists("abcxyz", testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertFalse(success);
    }

    /*
     * insertPuzzle tests
     * TODO: These are not fully functional tests as SQLite doesn't enforce FK constraints by default.
     * It's arguable where to enforce these constraints, but these tests as written assume application
     * logic will enforce things like checking that the user exists before attempts to insert a Puzzle.
     */
    @Test
    public void test_insertPuzzle_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);
    }

    @Test
    public void test_insertPuzzle_zlsUserName() {
        boolean success = myDB.insertPuzzle("", testCryptoName);
        assertFalse(success);
    }

    @Test
    public void test_insertPuzzle_nullUserName() {
        boolean success = myDB.insertPuzzle(null, testCryptoName);
        assertFalse(success);
    }

    @Test
    public void test_insertPuzzle_zlsCryptoName() {
        boolean success = myDB.insertPuzzle(testUserName, "");
        assertFalse(success);
    }

    @Test
    public void test_insertPuzzle_nullCryptoName() {
        boolean success = myDB.insertPuzzle(testUserName, null);
        assertFalse(success);
    }

    /*
     * markPuzzleSuccessful test
     * TODO: Functional caveat as insertPuzzle tests.
     */
    @Test
    public void test_markPuzzleSuccessful_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.markPuzzleSuccessful(testUserName, testCryptoName);
        assertTrue(success);
    }

    @Test
    public void test_markPuzzleSuccessful_zlsUserName() {
        boolean success = myDB.markPuzzleSuccessful("", testCryptoName);
        assertFalse(success);

    }

    @Test
    public void test_markPuzzleSuccessful_nullUserName() {
        boolean success = myDB.markPuzzleSuccessful(null, testCryptoName);
        assertFalse(success);

    }

    @Test
    public void test_markPuzzleSuccessful_zlsCryptoName() {
        boolean success = myDB.markPuzzleSuccessful(testUserName, "");
        assertFalse(success);
    }

    @Test
    public void test_markPuzzleSuccessful_nullCryptoName() {
        boolean success = myDB.markPuzzleSuccessful(testUserName, null);
        assertFalse(success);
    }

    @Test
    public void test_markPuzzleSuccessful_Multiple() {

        // First player
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.markPuzzleSuccessful(testUserName, testCryptoName);
        assertTrue(success);

        ArrayList<String> solvers = myDB.getCryptogramFirstThreeSolvers(testCryptoName);
        assertTrue(solvers.get(0).equals(testUserName));

        // Second player
        success = myDB.insertPlayer(testUserName2, testFirstName2, testLastName2, testEmail2);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName2, testCryptoName);
        assertTrue(success);

        success = myDB.markPuzzleSuccessful(testUserName2, testCryptoName);
        assertTrue(success);

        solvers = myDB.getCryptogramFirstThreeSolvers(testCryptoName);
        assertTrue(solvers.get(0).equals(testUserName));
        assertTrue(solvers.get(1).equals(testUserName2));
    }

    /*
     * updatePuzzleAttempts test
     * TODO: Functional caveat as insertPuzzle tests
     */

    @Test
    public void test_updatePuzzleAttempts_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);
    }

    @Test
    public void test_updatePuzzleAttempts_zlsUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts("", testCryptoName, 1);
        assertFalse(success);

    }

    @Test
    public void test_updatePuzzleAttempts_nullUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(null, testCryptoName, 1);
        assertFalse(success);
    }

    @Test
    public void test_updatePuzzleAttempts_zlsCryptName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, "", 1);
        assertFalse(success);
    }

    @Test
    public void test_updatePuzzleAttempts_nullCryptoName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, null, 1);
        assertFalse(success);
    }

    @Test
    public void test_updatePuzzleAttempts_negativeAttempts() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, -1);
        assertFalse(success);
    }

    /*
     * getPuzzleAttempts tests
     * TODO: Functional caveat as insertPuzzle tests
     */
    @Test
    public void test_getPuzzleAttemps_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);

        int attempts = myDB.getPuzzleAttempts(testUserName, testCryptoName);
        assertTrue(attempts == 1);
    }

    @Test
    public void test_getPuzzleAttempts_zlsUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);

        int attempts = myDB.getPuzzleAttempts("", testCryptoName);
        assertTrue(attempts == -1);
    }

    @Test
    public void test_getPuzzleAttempts_nullUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);

        int attempts = myDB.getPuzzleAttempts(null, testCryptoName);
        assertTrue(attempts == -1);
    }

    @Test
    public void test_getPuzzleAttempts_zlsCryptoName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);

        int attempts = myDB.getPuzzleAttempts(testUserName, "");
        assertTrue(attempts == -1);
    }

    @Test
    public void test_getPuzzleAttempts_nullCryptoName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.updatePuzzleAttempts(testUserName, testCryptoName, 1);
        assertTrue(success);

        int attempts = myDB.getPuzzleAttempts(testUserName, null);
        assertTrue(attempts == -1);
    }

    /*
     * puzzleExists tests
     */
    @Test
    public void test_puzzleExists_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists(testUserName, testCryptoName);
        assertTrue(success);
    }

    @Test
    public void test_puzzleExists_Invalid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists(testUserName, "NO");
        assertFalse(success);
    }

    @Test
    public void test_puzzleExists_zlsUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists("", testCryptoName);
        assertFalse(success);
    }

    @Test
    public void test_puzzleExists_nullUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists(null, testCryptoName);
        assertFalse(success);
    }

    @Test
    public void test_puzzleExists_zlsCryptoName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists(testUserName, "");
        assertFalse(success);
    }

    @Test
    public void test_puzzleExists_nullCryptoName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.insertCryptogram(testCryptoName, testUnencodedPhrase, testEncryptionKey, testMaxSolutionAttempts);
        assertTrue(success);

        success = myDB.insertPuzzle(testUserName, testCryptoName);
        assertTrue(success);

        success = myDB.puzzleExists(testUserName, null);
        assertFalse(success);
    }

    /*
     * checkSolution tests
     */

    // TODO: these tests.


    /*
     * loginPlayer tests
     */
    @Test
    public void test_loginUser_Valid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.loginPlayer(testUserName);
        assertTrue(success);
    }

    @Test
    public void test_loginUser_Invalid() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.loginPlayer("NO");
        assertFalse(success);
    }

    @Test
    public void test_loginUser_zlsUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.loginPlayer("");
        assertFalse(success);
    }

    @Test
    public void test_loginUser_nullUserName() {
        boolean success = myDB.insertPlayer(testUserName, testFirstName, testLastName, testEmail);
        assertTrue(success);

        success = myDB.loginPlayer(null);
        assertFalse(success);
    }


}