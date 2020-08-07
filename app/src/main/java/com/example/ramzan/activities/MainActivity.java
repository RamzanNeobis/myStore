package com.example.ramzan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ramzan.adapter.NoteAdapter;
import com.example.ramzan.R;
import com.example.ramzan.data.NoteViewModel;
import com.example.ramzan.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    private Button buttonDelete;
    private Note note;
    private NoteAdapter noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        final NoteAdapter adapter = new NoteAdapter();



        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);


            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT)
                       .show();
            }
        }).attachToRecyclerView(recyclerView);





        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_QUANTITY, note.getQuantity());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRICE, note.getPrice());
                intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE, note.getImage());
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int quantity = data.getIntExtra(AddEditNoteActivity.EXTRA_QUANTITY, 1);
            double price = data.getDoubleExtra(AddEditNoteActivity.EXTRA_PRICE, 1);
            String image = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE);



            note = new Note(image, title, description, price, quantity);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT)
                    .show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT)
                        .show();
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int quantity = data.getIntExtra(AddEditNoteActivity.EXTRA_QUANTITY, 1);
            double price = data.getDoubleExtra(AddEditNoteActivity.EXTRA_PRICE, 1);
            String image = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE);


            note = new Note(image, title, description, price, quantity);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT)
                    .show();


        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.delete_all_notes:
                showAlertDialogButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all notes");
        builder.setMessage("Would you like to delete all notes?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All notes delete", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}