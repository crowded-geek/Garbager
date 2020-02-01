package com.aayushman.garbager.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aayushman.garbager.R;
import com.aayushman.garbager.util.CameraPreview;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PuttingActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CAMERA = 2321;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private CardView backBtn;
    private TextView dateStamp, timeStamp, myLoc;
    private FloatingActionButton save;
    private Bitmap capBitmap;
    Location locat;
    List<Address> addresses;
    byte[] placeImage;
    private FusedLocationProviderClient fusedLocationClient;
    Geocoder geocoder;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putting);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            && (
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_CAMERA);
        } else {
            startUpSetUp();
        }

    }

    private void startUpSetUp() {
        myLoc = findViewById(R.id.myLocAdd);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null) {
                            try {
                                locat = location;
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                myLoc.setText(addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            myLoc.setText("Null");
                        }
                    }
                });
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dateStamp = findViewById(R.id.dateStamp);
        dateStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PuttingActivity.this, "Date", Toast.LENGTH_SHORT).show();
            }
        });
        timeStamp = findViewById(R.id.timeStamp);
        timeStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PuttingActivity.this, "Time", Toast.LENGTH_SHORT).show();
            }
        });
        save = findViewById(R.id.addSave);

        timeStamp.setText(getCurrentTimeUsingCalendar());
        dateStamp.setText(getCUrrentDate());
        myLoc.setSelected(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1280, 720, true);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                        capBitmap = rotatedBitmap;
                        refreshCam();

                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                        placeImage = bao.toByteArray();
                        String name = Long.toString(System.currentTimeMillis());
                        File file = new File(Environment.getExternalStorageDirectory()+ File.separator + name + ".jpg");
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(placeImage);
                            fos.close();
                        } catch (Exception error) {
                            error.printStackTrace();
                            Toast.makeText(PuttingActivity.this, "Image could not be saved.", Toast.LENGTH_LONG).show();
                        } finally {
                            Intent intent = new Intent(PuttingActivity.this, PreviewActivity.class);
                            intent.putExtra("imageName", name);
                            intent.putExtra("dateStamp", dateStamp.getText().toString());
                            intent.putExtra("timeStamp", timeStamp.getText().toString());
                            intent.putExtra("lat", String.valueOf(locat.getLatitude()));
                            intent.putExtra("long", String.valueOf(locat.getLongitude()));
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    public void refreshCam(){
        mCamera.stopPreview();
        mCamera.startPreview();
    }

    public static String getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate=dateFormat.format(date);
        return formattedDate;
    }

    public static String getCUrrentDate(){
        Calendar cal;
        cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission's needed!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                startUpSetUp();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCamera!=null){
            refreshCam();
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}
