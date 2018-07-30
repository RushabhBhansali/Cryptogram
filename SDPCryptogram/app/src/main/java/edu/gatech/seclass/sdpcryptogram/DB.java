package edu.gatech.seclass.sdpcryptogram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DB {

    // JM - Added helper function to check for valid string input
    // Base on: https://stackoverflow.com/questions/3598770/check-whether-a-string-is-not-null-and-not-empty
    private static boolean empty(final String s) {
        return !(s != null && !(s.trim().isEmpty()));
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database; //made public for easier testing

    //TODO - remove ability for SQL injections with some parameter binding in this list of methods
    //TODO - go through these and make db.close() calls for all the methods.
    //TODO - i stopped writing database = dbHelper.getWritable/Readable, used db instead. start using database again. saves memory

    public DB(Context context) {
        //System.out.println("Calling the DB constructor");
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public DBHelper getHelper() {
        return this.dbHelper;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public boolean databaseIsOpen() {
        return this.database.isOpen();
    }

    public void closeDatabase() {
        this.database.close();
    }

    public boolean resetDatabases() {
        database = dbHelper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS players");
        database.execSQL("DROP TABLE IF EXISTS cryptograms");
        database.execSQL("DROP TABLE IF EXISTS puzzles");

        // JM - Refactored for better ZLS / Null constraints/checks
        //create players table
        database.execSQL(
                "create table players " +
                        "(id integer primary key autoincrement," +
                        "username text not null check(length(username) >=1 )," +
                        "firstname text not null check(length(firstname) >=1 )," +
                        "lastname text not null check(length(lastname) >=1 )," +
                        "email text not null check(length(email) >=1 )," +
                        "CONSTRAINT unique_username UNIQUE (username) )"
        );

        // JM - Refactored for better ZLS / Null constraints/checks
        //create cryptograms table
        database.execSQL(
                "create table cryptograms " +
                        "(id integer primary key autoincrement," +
                        "cryptoName text not null check(length(cryptoName) >=1), " +
                        "unencodedPhrase text not null check(length(unencodedPhrase) >=1)," +
                        "encryptionKey text not null check(length(encryptionKey) >=1)," +
                        "maxSolutionAttempts integer not null check(maxSolutionAttempts > 0), " +
                        "dateCreated numeric not null," +
                        "CONSTRAINT unique_cryptoname UNIQUE(cryptoName))"
        );

        // JM - Refactored for better ZLS / Null constraints/checks
        //create puzzles table
        database.execSQL(
                "create table puzzles " +
                        "(username text not null check(length(username) >=1), " +
                        "cryptoName text not null check(length(cryptoName) >=1), " +
                        "dateCompleted numeric, " +
                        "attempts integer not null, " +
                        "successful integer not null, " +
                        "FOREIGN KEY(username) REFERENCES players(username)," +
                        "FOREIGN KEY(cryptoName) REFERENCES cryptograms(cryptoName))"
        );
        database.close();

        return true;
    }

    //TODO - kill these hardcoded column names
    public boolean insertPlayer(String username, String firstname, String lastname, String email) {
        // Preconditions
        if (empty(username) || empty(firstname) || empty(lastname) || empty(email))
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("email", email);

        database = dbHelper.getWritableDatabase();
        long success = database.insert("players", null, contentValues);

        // JM - Refactored to single function exit point.
        database.close();
        return (success != -1);
    }

    public boolean playerExists(String username, String firstname, String lastname, String email) {
        // Preconditions
        if (empty(username) || empty(firstname) || empty(lastname) || empty(email))
            return false;

        database = dbHelper.getReadableDatabase();
        boolean success;

        Cursor cursor = database.rawQuery("SELECT * FROM players WHERE username = ? AND firstname = ? AND lastname = ? AND email = ?", new String[]{username, firstname, lastname, email});

        //System.out.println(cursor.getCount());

        if (cursor.getCount() == 1) {
            success = true;
        } else {
            success = false;
        }
        cursor.close();
        database.close();
        return success;
    }

    //TODO - kill these hardcoded column names
    // JM - Preconditions and minor refactoring
    public boolean insertCryptogram(String cryptoName, String unencodedPhrase, String encryptionKey, int maxSolutionAttempts) {
        // Preconditions
        if (empty(cryptoName) || empty(unencodedPhrase) || empty(encryptionKey) || maxSolutionAttempts < 0)
            return false;

        ContentValues contentValues = new ContentValues();

        Date myDate = new Date(); //I have a date, I have a date! Better get my fancy shoes!
        long now = myDate.getTime();

        contentValues.put("cryptoName", cryptoName);
        contentValues.put("unencodedPhrase", unencodedPhrase);
        contentValues.put("encryptionKey", encryptionKey);
        contentValues.put("maxSolutionAttempts", maxSolutionAttempts);
        contentValues.put("dateCreated", now);

        database = dbHelper.getWritableDatabase();
        long success = database.insert("cryptograms", null, contentValues);

        database.close();
        return (success != -1);
    }

    //verifies if a certain cryptogram exists in the database
    // JM - Preconditions and variable name cleanup
    public boolean cryptogramExists(String cryptoName, String unencodedPhrase, String encryptionKey, int maxSolutionAttempts) {
        // Preconditions
        if (empty(cryptoName) || empty(unencodedPhrase) || empty(encryptionKey) || maxSolutionAttempts < 0)
            return false;

        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM cryptograms WHERE cryptoName = ? AND unencodedPhrase = ? AND encryptionKey = ? AND maxSolutionAttempts = ?", new String[]{cryptoName, unencodedPhrase, encryptionKey, Integer.toString(maxSolutionAttempts)});

        boolean success;
        if (cursor.getCount() == 1) {
            success = true;
        } else {
            success = false;
        }
        cursor.close();
        database.close();
        return success;
    }

    //TODO - kill these hardcoded column names
    // JM - Refactored for single exit point and took out the "puzzleExists" check - should not "wrap" methods.
    // (Not just general practice, but also complicates unit testing greatly in this specific case)
    public boolean insertPuzzle(String username, String cryptoName) {
        // Preconditions
        if (empty(username) || empty(cryptoName))
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("cryptoName", cryptoName);
        contentValues.put("successful", 0);
        contentValues.put("attempts", 0);

        database = dbHelper.getWritableDatabase();
        long success = database.insert("puzzles", null, contentValues);

        database.close();
        return (success != -1);
    }

    // JM - Preconditions and made public
    public boolean markPuzzleSuccessful(String username, String cryptoName) {
        // Preconditions
        if (empty(username) || empty(cryptoName))
            return false;

        database = dbHelper.getWritableDatabase();
        Date myDate = new Date(); //I have a date, I have a date! Better get my fancy shoes!
        long now = myDate.getTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put("successful", 1);
        contentValues.put("dateCompleted", now);

        String[] args = new String[]{username, cryptoName};

        long marked = database.update("puzzles", contentValues, "username=? AND cryptoName=?", args);

        database.close();
        return (marked != -1);
    }


    // JM - Preconditions and minor refactoring, made public
    public boolean updatePuzzleAttempts(String username, String cryptoName, int attempts) {

        //Preconditions
        if (empty(username) || empty(cryptoName) || (attempts < 0)) {
            return false;
        }

        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("attempts", String.valueOf(attempts));

        String[] args = new String[]{username, cryptoName};

        long success = database.update("puzzles", contentValues, "username=? AND cryptoName=?", args);

        database.close();
        return (success != -1);
    }

    // JM - Preconditions
    public int getPuzzleAttempts(String username, String cryptoName) {
        // Preconditions
        if (empty(username) || empty(cryptoName))
            return -1;

        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT attempts FROM puzzles WHERE username = ? AND cryptoName = ?", new String[]{username, cryptoName});
        if (cursor.getCount() < 1) {
            cursor.close();
            database.close();
            return -1; //error
        } else {
            cursor.moveToFirst();
            int attempts = cursor.getInt(cursor.getColumnIndex("attempts"));
            cursor.close();
            database.close();
            return attempts;
        }
    }

    // JM - Preconditions and minor refactoring
    // TODO: Should this include the username?
    public boolean puzzleExists(String username, String cryptoName) {
        // Preconditions
        if (empty(username) || empty(cryptoName))
            return false;

        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM puzzles WHERE username = ? AND cryptoName = ?", new String[]{username, cryptoName});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();

        return exists;
    }

    /**
     * checks a cryptogram solution. Will return
     * 1 = solution is correct
     * 0 = solution is not correct
     * -1 = error occurred
     */
    // TODO: Refactor and clean logic (need username? return false for non-existing?)
    public int checkSolution(String username, String cryptoName, String userSolution) {
        // Preconditions
        if (empty(username) || empty(cryptoName) || empty(userSolution))
            return -1;

        boolean solutionIsCorrect;

        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT unencodedPhrase FROM cryptograms WHERE cryptoName = ?", new String[]{cryptoName});
        if (cursor.getCount() < 1) {
            return -2;
        } //couldnt find cryptogram.
        cursor.moveToFirst();

        //System.out.println("Database correct solution is");
        //System.out.println(cursor.getString(cursor.getColumnIndex("unencodedPhrase")));

        if (cursor.getString(cursor.getColumnIndex("unencodedPhrase")).equals(userSolution)) {
            solutionIsCorrect = true;
            //System.out.println("Solution is correct");
        } else {
            solutionIsCorrect = false;
            //System.out.println("Solution is not correct");
        }

        //if the solution is correct, we need to update the puzzles table to mark the user solved it
        if (solutionIsCorrect) {
            boolean marked = markPuzzleSuccessful(username, cryptoName);
            if (!marked) {
                database.close();
                return -1;
            }
            //System.out.println("Puzzle was marked");
        }

        int attempts = getPuzzleAttempts(username, cryptoName);
        attempts += 1;

        //System.out.println("Attempts is now:" + String.valueOf(attempts));

        boolean incremented = updatePuzzleAttempts(username, cryptoName, attempts);

        //System.out.println("Incrementing was successful");
        database.close();
        if (!incremented) {
            return -1;
        } else if (solutionIsCorrect) {
            //System.out.println("Returning 1");
            return 1;
        } else {
            //System.out.println("Returning 0");
            return 0;
        }
    }

    // JM - Preconditions and refactoring
    public boolean loginPlayer(String username) {
        // Preconditions
        if (empty(username))
            return false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("players", new String[]{"username", "firstname"}, "username" + "=?",
                new String[]{username}, null, null, null, null);

        //TODO - better logic here, at least something more robust. This works for an alpha though!
        boolean success = (cursor.getCount() == 1);
        cursor.close();
        db.close();

        return success;
    }


    // TODO: Refactor and clean logic (1 SQL statement)
    public HashMap<String, Integer[]> ListUnsolved(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        HashMap<String, Integer[]> cryptMap = new HashMap<>(); //we will return cryptname -> {attempts remaining, attempts taken}
        String curName;
        Integer curAttempts;
        Integer maxAttempts;

        //we first need to get all the cryptograms from the cryptograms table
        Cursor cursor = db.rawQuery("SELECT cryptoName, maxSolutionAttempts FROM cryptograms WHERE maxSolutionAttempts > 0", null);

        try {
            while (cursor.moveToNext()) {
                curName = cursor.getString(cursor.getColumnIndex("cryptoName"));
                maxAttempts = cursor.getInt(cursor.getColumnIndex("maxSolutionAttempts"));
                cryptMap.put(curName, new Integer[]{maxAttempts, 0});
            }
        } finally {
            cursor.close();
        }

        //now we need to subtract away the puzzles that are successful for this user
        cursor = db.rawQuery("SELECT cryptoName FROM puzzles WHERE successful = 1 AND username = ?", new String[]{username});

        try {
            while (cursor.moveToNext()) {
                curName = cursor.getString(cursor.getColumnIndex("cryptoName"));
                cryptMap.remove(curName);
            }
        } finally {
            cursor.close();
        }

        //now we need to subtract away the solution attempts for puzzles they've already tried, but haven't solved
        cursor = db.rawQuery("SELECT cryptoName, attempts FROM puzzles WHERE successful = 0 AND username = ?", new String[]{username});

        try {
            while (cursor.moveToNext()) {
                curName = cursor.getString(cursor.getColumnIndex("cryptoName"));
                curAttempts = cursor.getInt(cursor.getColumnIndex("attempts"));
                maxAttempts = cryptMap.get(curName)[0]; //we can safely get this key, because we know the cryptogram must exist

                //remove the cryptogram entirely if they have no attempts remaining
                if (maxAttempts - curAttempts == 0) {
                    cryptMap.remove(curName);
                } else {
                    //otherwise just update the attempts
                    cryptMap.put(curName, new Integer[]{maxAttempts - curAttempts, curAttempts}); //update with however many attempts you've failed with
                }
            }
        } finally {
            cursor.close();
        }

        db.close();
        return cryptMap;
    }


    // TODO: Refactor and document error/missing return
    public String getCryptogramEncodedText(String cryptogramName) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String encoded;

        Cursor cursor = db.rawQuery("SELECT encryptionKey FROM cryptograms WHERE cryptoName = ?", new String[]{cryptogramName});

        try {
            cursor.moveToFirst();
            encoded = cursor.getString(cursor.getColumnIndex("encryptionKey"));
        } finally {
            cursor.close();
        }
        db.close();
        return encoded;
    }

    //TODO - might not necessarily be the best way of doing this.
    public static class CompletedCryptogram {
        String cryptoName;
        long dateCompleted;
        int attempts;
    }

    public ArrayList<CompletedCryptogram> ListSolved(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<CompletedCryptogram> cryptArray = new ArrayList<>(); //we will return cryptname -> {attempts taken}

        //grab the cryptograms we've solved from the puzzles table for that user
        Cursor cursor = db.rawQuery("SELECT cryptoName, attempts, dateCompleted FROM puzzles WHERE successful = 1 AND username = ?", new String[]{username});

        try {
            while (cursor.moveToNext()) {
                CompletedCryptogram completed = new CompletedCryptogram();
                completed.attempts = cursor.getInt(cursor.getColumnIndex("attempts"));
                completed.cryptoName = cursor.getString(cursor.getColumnIndex("cryptoName"));
                completed.dateCompleted = cursor.getLong(cursor.getColumnIndex("dateCompleted"));
                cryptArray.add(completed);
            }
        } finally {
            cursor.close();
        }

        db.close();
        return cryptArray;
    }

    public ArrayList<String> ListExpiredPuzzles(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> cryptArray = new ArrayList<>(); //just the names of the cryptograms
        Cursor cursor = db.rawQuery("SELECT DISTINCT p.cryptoName AS names FROM puzzles p INNER JOIN cryptograms c WHERE p.successful = 0 AND p.attempts = c.maxSolutionAttempts AND p.username = ?", new String[]{username});

        try {
            while (cursor.moveToNext()) {
                cryptArray.add(cursor.getString(cursor.getColumnIndex("names")));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return cryptArray;
    }

    public long getCryptogramCreationDate(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long creationDate = -1;
        Cursor cursor = db.rawQuery("SELECT dateCreated FROM cryptograms WHERE cryptoName = ?", new String[]{name});

        if (cursor.getCount() < 1) {
            cursor.close();
            db.close();
            return -1;
        } else {
            cursor.moveToFirst();
            creationDate = cursor.getLong(cursor.getColumnIndex("dateCreated"));
            cursor.close();
            db.close();
            return creationDate;
        }
    }

    public int getCryptogramNumTimesSolved(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int timesSolved = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS timesSolved FROM puzzles WHERE successful = 1 AND cryptoName = ?", new String[]{name});

        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            timesSolved = cursor.getInt(cursor.getColumnIndex("timesSolved"));
        }
        cursor.close();
        db.close();
        return timesSolved;
    }

    public void removeAllCryptograms() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cryptograms", null, null);
        db.close();
    }

    public void removeAllPlayers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("players", null, null);
        db.close();
    }

    public void removeAllPuzzles() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("puzzles", null, null);
        db.close();
    }

    public ArrayList<String> getCryptogramFirstThreeSolvers(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> solvers = new ArrayList<>();
        solvers.add("N/A");
        solvers.add("N/A");
        solvers.add("N/A");
        Cursor cursor = db.rawQuery("SELECT username FROM puzzles WHERE successful = 1 AND cryptoName = ? ORDER BY dateCompleted", new String[]{name});

        for (int i = 0; i < Math.min(cursor.getCount(), 3); i++) {
            cursor.moveToNext();
            solvers.add(i, cursor.getString(cursor.getColumnIndex("username")));
        }
        cursor.close();
        db.close();
        return solvers;
    }


    public ArrayList<String> getAllCryptogramNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> cryptArray = new ArrayList<>(); //just the names of the cryptograms
        Cursor cursor = db.rawQuery("SELECT cryptoName FROM cryptograms ORDER BY dateCreated", null);

        try {
            while (cursor.moveToNext()) {
                cryptArray.add(cursor.getString(cursor.getColumnIndex("cryptoName")));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return cryptArray;
    }
}