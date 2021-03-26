package com.example.iskcon__admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class AudioChoserActivity extends AppCompatActivity {

    private static final int PICK_AUD_REQUEST = 7;
    Button selectaudio,upload_button;
    TextView tvshowpath;
    EditText mNameOfAudio;
    Uri filePath;



    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDataReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_choser);
       selectaudio = findViewById(R.id.select_audio);
       tvshowpath = findViewById(R.id.selectedtext);
       upload_button = findViewById(R.id.submit_song);
       mNameOfAudio = findViewById(R.id.songNmae);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDataReference = FirebaseDatabase.getInstance().getReference("audios");


       selectaudio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               chosemp3data();
           }
       });
       upload_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (mNameOfAudio.getText().toString() == null ){
                   Toast.makeText(AudioChoserActivity.this, "enter the song name ", Toast.LENGTH_SHORT).show();
               }
               else{
                   uploadAud();
               }
           }
       });



    }

    private void chosemp3data() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Aud"), PICK_AUD_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_AUD_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Toast.makeText(this, "Got data", Toast.LENGTH_SHORT).show();
             filePath = data.getData();
        }
        tvshowpath.setText(filePath.toString()+ " file gotted ok ");
       // uploadAud();
    }

    public void uploadAud()
    {
        final ProgressDialog pd =  new ProgressDialog(AudioChoserActivity.this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".mp3");

        reference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String name = mNameOfAudio.getText().toString();
                                String url = uri.toString();
                                writeNewAudInfoToDB(name, url);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();



                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });


    }

    private void writeNewAudInfoToDB(String name, String url) {
        UploadInfo_audios info = new UploadInfo_audios(name, url);

        String key = mDataReference.push().getKey();
        mDataReference.child(key).setValue(info);
    }



}