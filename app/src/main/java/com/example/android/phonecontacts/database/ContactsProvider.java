package com.example.android.phonecontacts.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

// Verify that TaskContentProvider extends from ContentProvider and implements required methods
public class ContactsProvider extends ContentProvider {

    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private ContactsDbHelper mDbHalper;

    // Define final integer constants for the directory of tasks and a single item.
    public static final int CONTACTS = 100;
    public static final int CONTACTS_WITH_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*
          All paths added to the UriMatcher have a corresponding int.
         */
        uriMatcher.addURI(ContactsContract.AUTHORITY , ContactsContract.CONTACT_PATH , CONTACTS);
        uriMatcher.addURI(ContactsContract.AUTHORITY , ContactsContract.CONTACT_PATH + "/#" , CONTACTS_WITH_ID);
        return uriMatcher;
    }

    /* onCreate() is where you should initialize anything you’ll need to setup
       your underlying data source. */
    @Override
    public boolean onCreate() {
        // onCreate() and initialize a TaskDbhelper on startup
        // Declare the DbHelper as a global variable
        Context context = getContext();
        mDbHalper = new ContactsDbHelper(context);
        return false;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDbHalper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);

        // Empty cursor to be returned.
        Cursor returnCursor;

        switch (match){
            case CONTACTS:
                returnCursor = db.query(ContactsContract.ContactEntry.TABLE_NAME ,
                        projection ,
                        selection ,
                        selectionArgs ,
                        null ,
                        null ,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("unknown uri "+ uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver() , uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDbHalper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);

        // Empty URI to be returned.
        Uri returnUri;

        switch (match){
            case CONTACTS:
                long id = db.insert(ContactsContract.ContactEntry.TABLE_NAME , null , contentValues);
                // Insert new values into the database
                // Inserting values into tasks table
                if (id>0){
                    returnUri = ContentUris.withAppendedId(uri ,id);
                }else{
                    throw new android.database.SQLException("failed" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Failed" + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mDbHalper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int deletedContact;

        switch (match){
            case CONTACTS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deletedContact = db.delete(ContactsContract.ContactEntry.TABLE_NAME , "_id=?" , new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("uri = " + uri);
        }

        if (deletedContact!=0){
            getContext().getContentResolver().notifyChange(uri , null);
        }
        return deletedContact;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHalper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int updatedContact;
        switch (match){
            case CONTACTS:
                updatedContact = db.update(
                        ContactsContract.ContactEntry.TABLE_NAME , contentValues , selection , selectionArgs);
                break;

            case CONTACTS_WITH_ID:
                selection = ContactsContract.ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                updatedContact = db.update(
                        ContactsContract.ContactEntry.TABLE_NAME , contentValues ,selection ,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("uri = " + uri);

        }

        if (updatedContact != 0){
            getContext().getContentResolver().notifyChange(uri , null);
        }

        return updatedContact;
    }
}
