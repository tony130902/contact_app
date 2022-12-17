package com.example.android.phonecontacts.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContactsContract{

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.android.phonecontacts";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String CONTACT_PATH = "contacts";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class ContactEntry  implements BaseColumns{

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(CONTACT_PATH).build();

        // Task table and column names
        public static final String TABLE_NAME = "contacts";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below ;

        public static final String CONTACT_NAME = "contact_name";
        public static final String CONTACT_NUMBER = "contact_number";

    }
    /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows
        Note: Because this implements BaseColumns, the _id column is generated automatically
        tasks
         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    contact_name    |    contact_number |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |         john      |       xxx       |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |    aeron        |       www      |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |   Learn guitar     |       yyy      |
         - - - - - - - - - - - - - - - - - - - - - -
         */
}

