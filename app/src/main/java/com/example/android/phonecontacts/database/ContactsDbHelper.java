package com.example.android.phonecontacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.phonecontacts.database.ContactsContract.ContactEntry;

public class ContactsDbHelper extends SQLiteOpenHelper {

    // The name of the database
    public static final String DATABASE_NAME = "contacts.db";

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION  = 1;

    public ContactsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + ContactEntry.TABLE_NAME + " ( "+
                ContactEntry._ID + " INTEGER PRIMARY KEY, " +
                ContactEntry.CONTACT_NAME + " TEXT NOT NULL, " +
                ContactEntry.CONTACT_NUMBER + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
