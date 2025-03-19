package com.example.notesapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private var selectedNote: Note? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = NoteDatabase.getDatabase(this)
        val noteDao = database.noteDao()
        val factory = NoteViewModel.provideFactory(noteDao)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        val etTitle = findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = findViewById<TextInputEditText>(R.id.etDescription)
        val btnAdd = findViewById<MaterialButton>(R.id.btnAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        adapter = NoteAdapter(emptyList(),
            onEditClick = { note ->
                selectedNote = note
                etTitle.setText(note.title)
                etDescription.setText(note.description)
                btnAdd.text = "Update Note"
            },
            onDeleteClick = { note ->
                noteViewModel.delete(note)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        noteViewModel.allNotes.observe(this) { notes ->
            adapter.updateNotes(notes)
        }

        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                if (selectedNote == null) {
                    noteViewModel.insert(Note(title = title, description = description))
                } else {
                    val updatedNote = selectedNote!!.copy(title = title, description = description)
                    noteViewModel.update(updatedNote)
                    btnAdd.text = "Add Note"
                    selectedNote = null
                }
                etTitle.text?.clear()
                etDescription.text?.clear()
            }
        }
    }
}