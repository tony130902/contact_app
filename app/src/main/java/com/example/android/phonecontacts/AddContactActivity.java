package com.example.android.phonecontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.phonecontacts.database.ContactsContract;

public class AddContactActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private String itemId;
    private static final int EXISTING_CONTACT_LOADER = 10;
    EditText mNameEditText;
    EditText mNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mNameEditText = findViewById(R.id.name_entered_editText);
        mNumberEditText = findViewById(R.id.number_entered_editText);

        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");

        if (itemId == null) {
            setTitle("Add Contacts");
        } else {
            setTitle("Edit Contact");
            Toast.makeText(this, "Item Id = " + itemId, Toast.LENGTH_SHORT).show();
            getLoaderManager().initLoader(EXISTING_CONTACT_LOADER , null , this);
        }
    }

    public void onClickSaveEntry(View view) {

        String user_name = ((EditText) findViewById(R.id.name_entered_editText)).getText().toString();
        String user_number = ((EditText) findViewById(R.id.number_entered_editText)).getText().toString();

        if (user_name.length() == 0 || user_number.length() != 10 || user_number.startsWith("0")) {
            Toast.makeText(this, "Please Enter valid Name or Number", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ContactsContract.ContactEntry.CONTACT_NAME, user_name);
        values.put(ContactsContract.ContactEntry.CONTACT_NUMBER, user_number);

        Uri uri = getContentResolver().insert(ContactsContract.ContactEntry.CONTENT_URI, values);

        if (uri != null) {
            Toast.makeText(this, "Contact Number Saved ", Toast.LENGTH_SHORT).show();
        }

        finish();

    }


//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//
//        return new CursorLoader(AddContactActivity.this,
//                Uri.parse(ContactsContract.ContactEntry.CONTENT_URI + "/" + itemId),
//                null,
//                null,
//                null,
//                null);
////        return null;
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(AddContactActivity.this,
                Uri.parse(ContactsContract.ContactEntry.CONTENT_URI + "/" + itemId),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        int nameColoumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
        int numberColoumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUMBER);

        String name = data.getString(nameColoumnIndex);
        long number = data.getLong(numberColoumnIndex);

        mNameEditText.setText(name);

        String stringNumber = "" + number;
        mNumberEditText.setText(stringNumber);
        Toast.makeText(this, "onLoadFinished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mNameEditText.setText("");
        mNumberEditText.setText("");
        Toast.makeText(this, "onLoadReset", Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//
//        data.moveToFirst();
//
//        int nameColoumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
//        int numberColoumnIndex = data.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUMBER);
//
//        String name = data.getString(nameColoumnIndex);
//        long number = data.getLong(numberColoumnIndex);
//
//        mNameEditText.setText(name);
//
//        String stringNumber = "" + number;
//        mNumberEditText.setText(stringNumber);
//        Toast.makeText(this, "onLoadFinished", Toast.LENGTH_SHORT).show();

//    }

//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        mNameEditText.setText("");
//        mNumberEditText.setText("");
//        Toast.makeText(this, "onLoadReset", Toast.LENGTH_SHORT).show();
//    }

}