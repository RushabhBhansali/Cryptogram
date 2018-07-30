package edu.gatech.seclass.sdpcryptogram;

import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

//adapted from examples available at https://www.tutorialspoint.com/android/android_sqlite_database.htm
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Team48DB.db";
    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String PLAYERS_COLUMN_USERNAME = "username";
    private static final String CRYPTOGRAM_TABLE_NAME = "cryptograms";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        //System.out.println("Calling the DBHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //System.out.println("Database is being created");

        //create players table
        db.execSQL(
                "create table players " +
                        "(id integer primary key autoincrement," +
                        "username text check(length(username) >=1 )," +
                        "firstname text check(length(firstname) >=1 )," +
                        "lastname text check(length(lastname) >=1 )," +
                        "email text check(length(email) >=1 )," +
                        "CONSTRAINT unique_username UNIQUE (username) )"
        );

        //create cryptograms table
        //TODO - probably make an int primary key, but would require a few fixes elsewhere.
        db.execSQL(
                "create table cryptograms " +
                        "(cryptoName text primary key not null, unencodedPhrase text not null, encryptionKey text not null, maxSolutionAttempts integer not null, dateCreated numeric not null)");


        //create puzzles table
        db.execSQL(
                "create table puzzles " +
                        "(username text not null, cryptoName text not null, dateCompleted numeric, attempts integer not null, successful integer not null," +
                        "FOREIGN KEY(username) REFERENCES players(username)," +
                        "FOREIGN KEY(cryptoName) REFERENCES cryptograms(cryptoName))"
        );

        //System.out.println("Databases created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //System.out.println("Database upgrade has been called");
        db.execSQL("DROP TABLE IF EXISTS players");
        db.execSQL("DROP TABLE IF EXISTS cryptograms");
        db.execSQL("DROP TABLE IF EXISTS puzzles");
        onCreate(db);
    }
}