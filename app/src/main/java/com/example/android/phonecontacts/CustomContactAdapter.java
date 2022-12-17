package com.example.android.phonecontacts;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.phonecontacts.database.ContactsContract;

public class CustomContactAdapter extends RecyclerView.Adapter<CustomContactAdapter.ContactView> {

    private Context mContext;
    private Cursor mCursor;

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param context the current Context
     */
    public CustomContactAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public ContactView onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.contact_items, parent, false);

        return new ContactView(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(ContactView holder, int position) {

        // Indices for the _id, description, and priority columns
        int id_index = mCursor.getColumnIndex(ContactsContract.ContactEntry._ID);
        int name_index = mCursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NAME);
        int number_index = mCursor.getColumnIndex(ContactsContract.ContactEntry.CONTACT_NUMBER);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(id_index);
        String contact_name = mCursor.getString(name_index);
        char initial_char = contact_name.charAt(0);
        Log.d("nitesh" , "initial character = " + initial_char);
        long contact_number = mCursor.getLong(number_index);

        //Set values
        holder.itemView.setTag(id);

        int itemId = holder.getPosition();
        holder.circleTextView.setText(String.valueOf(initial_char));

        holder.contact_name_view.setText(contact_name);

        String string_number = "" + contact_number;
        holder.contact_number_view.setText(string_number);


    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    // Inner class for creating ViewHolders
    class ContactView extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Class variables for the task description and priority TextViews
        TextView contact_name_view;
        TextView contact_number_view;
        TextView circleTextView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public ContactView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            contact_name_view = itemView.findViewById(R.id.contact_name);
            contact_number_view = itemView.findViewById(R.id.contact_number);
            circleTextView = itemView.findViewById(R.id.circleTextView);

//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(new MainActivity());
//            boolean checkbox = sharedPreferences.getBoolean("number_visibility" , false);
//            if (checkbox){
//                contact_number_view.setVisibility(View.VISIBLE);
//            }else{
//                contact_number_view.setVisibility(View.GONE);
//            }

        }

//        public void preference(){
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(new MainActivity());
//            boolean checkbox = sharedPreferences.getBoolean("number_visibility" , false);
//            if (checkbox){
//                contact_number_view.setVisibility(View.VISIBLE);
//            }else{
//                contact_number_view.setVisibility(View.GONE);
//            }
//        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, AddContactActivity.class);
            int itemId = (int) view.getTag();
            intent.putExtra("itemId" , ""+ itemId);
            mContext.startActivity(intent);
        }
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c){

        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bcoz nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
