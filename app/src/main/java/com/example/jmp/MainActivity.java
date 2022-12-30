package com.example.jmp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseErrorHandler;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView city, latitude, longitude;
    public static final int REQUEST_CODE = 100;

    Button submit, checkloc, pilihfoto;
    EditText isinama, isialamat, isinohp;
    DBHelper db = new DBHelper(this);

    ImageView isifoto;
    private final int GALLERY_REQ_CODE = 1000;
    public Bitmap fototersimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.submit);
        isinama = (EditText) findViewById(R.id.isinama);
        isialamat = (EditText) findViewById(R.id.isialamat);
        isinohp = (EditText) findViewById(R.id.isinohp);
        isifoto = (ImageView) findViewById(R.id.isifoto);
        pilihfoto= (Button) findViewById(R.id.pilihfoto);

        city = (TextView) findViewById(R.id.kota);
        latitude = (TextView) findViewById(R.id.koordinatlintang);
        longitude = (TextView) findViewById(R.id.koordinatbujur);
        checkloc = (Button) findViewById(R.id.ceklokasi);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Findlocation();
            }
        });
        RadioGroup Gender = (RadioGroup) findViewById(R.id.gender);

        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Gallery = new Intent(Intent.ACTION_PICK);
                Gallery.setData (MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Gallery, GALLERY_REQ_CODE);
            }
        });

        submit.setOnClickListener(view -> {
            String jk = "?";
            String name = isinama.getText().toString();
            String address = isialamat.getText().toString();
            String phone = isinohp.getText().toString();
            String kota = city.getText().toString();

            switch (Gender.getCheckedRadioButtonId()){
                case R.id.laki:
                    jk = "Laki-laki";
                    break;
                case R.id.perempuan:
                    jk = "Perempuan";
                    break;
            }
            fototersimpan = ((BitmapDrawable)isifoto.getDrawable()).getBitmap();
            byte[] foto = getBitmapAsByteArray(fototersimpan);

            addUser(new User(name, address, jk, phone, kota, foto));

            Intent myIntent = new Intent(view.getContext(), UserDataShow.class);
            startActivity(myIntent);
        });
    }
    private void Findlocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addressesList = null;
                        try {
                            addressesList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            city.setText(" " + addressesList.get(0).getLocality());
                            latitude.setText("Lintang: " + addressesList.get(0).getLatitude());
                            longitude.setText("Bujur: " + addressesList.get(0).getLongitude());
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        else {
            Askpermission();
        }
    }
    private void Askpermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Findlocation();
            }
            else{
                toastMessage("Mohon akses lokasi! ");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                isifoto.setImageURI(data.getData());
            }
        }
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        return outputStream.toByteArray();
    }


    public void addUser(User newUser) {
        boolean insertUser = db.addUser(newUser);
        if(insertUser)
            toastMessage("Data berhasil ditambahkan");
        else
            toastMessage("Telah terjadi kesalahan");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}