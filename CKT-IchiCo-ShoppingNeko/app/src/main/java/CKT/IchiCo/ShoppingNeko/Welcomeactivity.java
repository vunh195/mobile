package CKT.IchiCo.ShoppingNeko;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import CKT.IchiCo.ShoppingNeko.activities.LoginActivity;
import CKT.IchiCo.ShoppingNeko.activities.RegisterActivity;


public class Welcomeactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcomeactivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}