package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.et_notes_title);
        contentEditText = findViewById(R.id.et_notes_content);
        saveNoteBtn = findViewById(R.id.btn_save_note);
        pageTitleTextView = findViewById(R.id.tv_page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_noteTextView_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditMode) {
            pageTitleTextView.setText("Edit your Note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        deleteNoteTextViewBtn.setOnClickListener((v) -> deleteNoteFromFirebase());

    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(noteTitle==null || noteTitle.isEmpty()) {
            Utility.showToast(NoteDetailsActivity.this, "Title is required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        //note.setTimestamp(timestamp);
        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = Utility.getCollectionReferenceForNotes().getParent().document(docId);
        }else {
            documentReference = Utility.getCollectionReferenceForNotes().getParent().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, "Note added Successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Note failed");
                }
            }
        });
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().getParent().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted Successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Note failed while deleting");
                }
            }
        });
    }

}