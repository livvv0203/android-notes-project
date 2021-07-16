package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;
    boolean modifiedFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        modifiedFlag = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);

        // Get the intent
        Intent intent = getIntent();

        // Pull the note id
        noteId = intent.getIntExtra("noteId", -1);
        boolean isCreate = noteId < 0 || intent.getBooleanExtra("isCreate", noteId < 0);

        // Pull the proper String and put it into the text edit
        if (!isCreate) {
            // if note id is not negative we reached here by editing existing note.
            editText.setText(MainActivity.notes.get(noteId));
            Log.i("Info","Editing note with id " + noteId);
        } else // if (isCreate)
            {
            // if note id is negative, we've reached here by add new note
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
                Log.i("Info","Added note with id " + noteId);

                //MainActivity.arrayAdapter.notifyDataSetChanged();
            modifiedFlag = true;
        }

        // Update the Changed info to the Main List View
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() != 0) {
                    MainActivity.notes.set(noteId, noteId + ":  " + String.valueOf(charSequence));

                    SharedPreferences sharedPreferences = getApplicationContext()
                            .getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    // Convert the ArrayList in to a HashSet, and the Hash set can save our shared preferences
                    HashSet<String> set = new HashSet<>(MainActivity.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                } else {
                    MainActivity.notes.set(noteId, noteId + ":  " + "None");

                }
                modifiedFlag = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }); // End of add listeners.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modifiedFlag){
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
    }
}