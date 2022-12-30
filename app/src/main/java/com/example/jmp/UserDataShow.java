package com.example.jmp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class UserDataShow extends AppCompatActivity {

    private TextView t;
    private ImageView p;
    DBHelper db = new DBHelper(this);
    String text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_show);

        t = findViewById(R.id.showUsers);

        List<User> users_data = db.getAllUsers();

        for (User u : users_data) {
            String log = "Nomor urut: " + u.getId() + "\n"
                    + "Nama: " + u.getName() + "\n"
                    + "Alamat: " + u.getAddress() + "\n"
                    + "Jenis Kelamin: " + u.getGender() + "\n"
                    + "No HP: " + u.getPhone() + "\n"
                    + "Kota: " + u.getCity() + "\n\n";
            text = text + log;
        }
        t.setText(text);

        LinearLayout layout = (LinearLayout) findViewById(R.id.isifotos);

        for (User u : users_data){
            ImageView foto = new ImageView(this);
            Bitmap bl = getImage(u.getPhoto());
            foto.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
            foto.setPadding(0,80,0,0);
            foto.setImageBitmap(bl);
            layout.addView(foto);
        }

    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
