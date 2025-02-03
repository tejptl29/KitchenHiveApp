package com.example.kitchenhive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splashscreen extends BaseActivity {

    private Handler handler = new Handler();

    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
// session Maintain
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=null;
                System.out.println(sharedPreferences.getAll());
                if(!sharedPreferences.getString("UserID", "").isEmpty()){
                    intent = new Intent(Splashscreen.this,MainActivity.class);
                }
                else{
                    intent = new Intent(Splashscreen.this,Login.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 3000);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}