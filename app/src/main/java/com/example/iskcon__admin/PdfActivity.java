package com.example.iskcon__admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

public class PdfActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUESTT = 2;
    ImageView imagebrowse,imageupload,filelogo,cancelfile,mImageView2;
    Uri filepath;
    private Button mButtonChooseImagee;
    EditText filetitle;
    private Uri mImageUri2;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private StorageTask mUploadTask;
    String imageuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mImageView2 = findViewById(R.id.image_view2);
        //
        mButtonChooseImagee = findViewById(R.id.button_choose_image2);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("mydocuments");

        filetitle=findViewById(R.id.filetitle);

        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);


        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });


        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupload(filepath);
            }
        });


        mButtonChooseImagee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESTT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            filelogo.setVisibility(View.VISIBLE);
            cancelfile.setVisibility(View.VISIBLE);
            imagebrowse.setVisibility(View.INVISIBLE);
        }

        if (requestCode == PICK_IMAGE_REQUESTT && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri2 = data.getData();
            Picasso.get().load(mImageUri2).into(mImageView2);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("uploadspdfs/"+ System.currentTimeMillis()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        //
                        //
                        if (mImageUri2 !=null){
                            final StorageReference fileReference2 = storageReference.child("uploadsimagepdf/"+System.currentTimeMillis()
                                    + "." + getFileExtension(mImageUri2));
                            mUploadTask = fileReference2.putFile(mImageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {




                                    fileReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {



                                            Toast.makeText(PdfActivity.this, "image Upload successful", Toast.LENGTH_LONG).show();

                                           imageuri= uri.toString();



                                        }
                                    });


                                   //



                                }
                            });

                        }



                        //
                        //

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                model obj=new model(filetitle.getText().toString(),uri.toString(),imageuri);
                                databaseReference.child(databaseReference.push().getKey()).setValue(obj);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");
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
}


