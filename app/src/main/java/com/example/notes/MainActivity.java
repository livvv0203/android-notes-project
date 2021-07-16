package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static SharedPreferences sharedPreferences;
    ListView listView;
//    static int nextIssuingId = 100;
//
//    public static void notifyIdConsumed() {
//        ++nextIssuingId;
//        sharedPreferences.edit().putInt("nextIssuingId", nextIssuingId).apply();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is where we created the shared preferences
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        // Add a List View to the Main Activity
        listView = findViewById(R.id.listView);

        // Save list contents to HashSet
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        //nextIssuingId = sharedPreferences.getInt("nextIssuingId", 100);

        // Check if Main activity is empty, if yes, create a default one, if no, keep the previous notes
        if (set == null) {
            notes.add("Example Note");
        } else {
            notes = new ArrayList<>(set);
        }

        // Set an adapter to the listView to give action/listener to the View
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        // Get into the text editor for changing note contents
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *
             * @param adapterView
             * @param view The view within the AdapterView that was clicked
             *             (this will be a view provided by the adapter)
             * @param i The position of the view in the adapter.
             * @param l The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", i);
                intent.putExtra("isCreate", false);
                startActivity(intent);

            }
        });

        // For deleting an item of the list
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                int itemToDelete = i;

                // Display an Alert window for user to delete
                // First, access the item that we want to delete, which is the integer "i" here
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you really want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                // Updated the notes
                                // Convert the ArrayList in to a HashSet, and the Hash set can save our shared preferences
                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    // MENU
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            intent.putExtra("isCreate", true);
            startActivity(intent);
            return true;
        }

        return false;
    }
}






















