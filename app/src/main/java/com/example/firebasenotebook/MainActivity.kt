package com.example.firebasenotebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTitle:AppCompatEditText
    private lateinit var editTextDescription:AppCompatEditText
    private lateinit var btnAddNote:AppCompatButton
    private lateinit var btnLoadData:AppCompatButton
    private lateinit var txtOutput:TextView
    //Firebase References
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var collectionRef: CollectionReference = db.collection("Notes")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Initialize the views
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        btnAddNote = findViewById(R.id.button_addNote)
        btnLoadData = findViewById(R.id.loadButtonData)
        txtOutput = findViewById(R.id.txtOutput)
        //Events
        btnAddNote.setOnClickListener {
            addNote()
        }
        btnLoadData.setOnClickListener {
            loadData()
        }
    }

    override fun onStart() {
        super.onStart()
        collectionRef.addSnapshotListener { value, error ->
            error?.let {
               return@addSnapshotListener
            }
            value?.let {
                if(it !==null){
                    var data = ""
                    for (document in it){

                        var note = document.toObject(Notes::class.java)
                        var title = note.title
                        var description = note.description
                        data+= "Title: $title \n Description: $description \n\n"

                    }
                    txtOutput.text = data
                }
            }
        }
    }

    //Add notes
    private fun addNote(){
        if(editTextTitle.text.toString().isEmpty() || editTextDescription.text.toString().isEmpty()){
            Toast.makeText(this,"Fields cannot be empty!",Toast.LENGTH_SHORT).show()
        }else{
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()
            val note = Notes(title,description)
            collectionRef.add(note).addOnSuccessListener {
                Toast.makeText(this,"Note has been added successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Error adding note!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Retrieve the notes
    private fun loadData(){

        collectionRef.get().addOnSuccessListener { result ->
            var data = ""
            for (document in result){

                var note = document.toObject(Notes::class.java)
                var title = note.title
                var description = note.description
                data+= "Title: $title \n Description: $description \n\n"

            }
            txtOutput.text = data
        }
    }
}