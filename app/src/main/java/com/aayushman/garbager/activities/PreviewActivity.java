package com.aayushman.garbager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aayushman.garbager.R;
import com.aayushman.garbager.model.Putting;
import com.aayushman.garbager.model.Transaction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

public class PreviewActivity extends AppCompatActivity {
    private String fileName, dateStamp, timeStamp, locationAdd;
    private Intent intent;
    private CardView backBtn;
    private ProgressDialog d;
    private FloatingActionButton saveIn;
    private TextView dateStamptv, timeStamptv, myLoctv;
    private String lat, lon;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        intent = getIntent();
        fileName = intent.getStringExtra("imageName");
        dateStamp = intent.getStringExtra("dateStamp");
        timeStamp = intent.getStringExtra("timeStamp");
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("long");
        myLoctv = findViewById(R.id.myLocAdd);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            myLoctv.setText(geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1).get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dateStamptv = findViewById(R.id.dateStamp);
        timeStamptv = findViewById(R.id.timeStamp);
        dateStamptv.setText(dateStamp);
        timeStamptv.setText(timeStamp);
        myLoctv.setSelected(true);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        backImage = findViewById(R.id.backImage);
        final File file = new File(Environment.getExternalStorageDirectory()+File.separator + fileName + ".jpg");
        Bitmap bitmap;
        byte[] MyImage = new byte[(int) file.length()];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(MyImage, 0, MyImage.length);
            buf.close();
        } catch (Exception error) {
            Toast.makeText(PreviewActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
        } finally {
            bitmap = BitmapFactory.decodeByteArray(MyImage, 0, MyImage.length);
            Glide.with(PreviewActivity.this).load(bitmap).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(backImage);
        }
        saveIn = findViewById(R.id.addSave);
        saveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new ProgressDialog(PreviewActivity.this);
                d.setMessage("Uploading...");
                d.show();
                uploadImage(file);

            }
        });

    }

    private void uploadImage(File file) {
        try {
            FirebaseStorage.getInstance().getReference().child(String.valueOf(file.hashCode())).putFile(Uri.fromFile(file)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    d.hide();
                    showThanks();
                    String url = task.getResult().getUploadSessionUri().toString();
                    Putting p = new Putting(
                            url, timeStamp, dateStamp, lon, lat
                    );
                    FirebaseDatabase.getInstance().getReference().child("puttings").push().setValue(p);
                    Transaction s = new Transaction(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()
                    , 1, false);
                    final Integer[] conts = new Integer[1];
                    FirebaseDatabase.getInstance().getReference().child("transactions").push().setValue(s);
                    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseDatabase.getInstance().getReference().child("totalfunds").setValue(Integer.parseInt(dataSnapshot.child("totalfunds").getValue().toString())-1);
                            conts[0] = Integer.valueOf(dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("contributions").getValue().toString());
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("contributions").setValue(
                                    Integer.parseInt(dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child("contributions").getValue().toString())+1
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showThanks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
        builder.setTitle("Thanks!");
        builder.setMessage("We thank you for making one step towards cleanliness.");
        builder.setPositiveButton("Your welcome", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
}