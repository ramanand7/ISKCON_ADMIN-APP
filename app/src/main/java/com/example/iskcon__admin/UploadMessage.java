package com.example.iskcon__admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadMessage extends AppCompatActivity {
  EditText date,message;
  Button submitmessage;


    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_message);
        message = findViewById(R.id.messageedittext);
        date = findViewById(R.id.editTextDate);
        submitmessage = findViewById(R.id.save_message);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("messages");

        submitmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                UploadMesI upload = new UploadMesI(message.getText().toString() ,
                        date.getText().toString());
                String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(uploadId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadMessage.this, "message published suscessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadMessage.this, "message is not uploades some error occured ", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }

}