package com.example.kitchenhive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Registration extends BaseActivity {

    TextView txtalreadylogin;
    EditText txtname,txtemail,txtnumber,txtpwd;
    Button btn_register_now;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtalreadylogin = findViewById(R.id.txtreglogin);
        txtname = findViewById(R.id.nameedittext);
        txtemail = findViewById(R.id.emailedittext);
        txtnumber = findViewById(R.id.phoneedittext);
        txtpwd =  findViewById(R.id.pwdedittext);
        btn_register_now = findViewById(R.id.btn_register);
        progress = findViewById(R.id.register_progressbar);

        txtalreadylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this ,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;

                String errorMessage = "";

                String name = txtname.getText().toString();
                String mail = txtemail.getText().toString();
                String password = txtpwd.getText().toString();
                String phone = txtnumber.getText().toString();


                if(!checkStringNotNull(name)){
                    valid = false;
                    errorMessage="Name can not be empty";
                }

                if(valid && !checkStringNotNull(mail)){
                    valid = false;
                    errorMessage="E-mail can not be empty";
                }

                boolean validEmail = new Utility().isValidEmail(mail);
                if(valid && !validEmail){
                    valid = false;
                    errorMessage="Please enter valid email address";
                }


                if(phone.isEmpty() && valid){
                    valid = false;
                    errorMessage = "Phone Number can not be empty";
                }

                boolean validphone = new Utility().isValidMobile(phone);
                if(valid && !validphone){
                    valid = false;
                    errorMessage="Enter Correct Number";
                }

                if(password.isEmpty() && valid){
                    valid = false;
                    errorMessage = "Enter Password";
                }

                if(valid){

                    setInprogress(progress,btn_register_now,true);
                    APIClass apiClass = new APIClass();
                    apiClass.register_user(name, mail, password, phone);
                    apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                        @Override
                        public void onSuccess(String message, JSONObject json) {
                            setInprogress(progress,btn_register_now,false);
                            Intent intent = new Intent(Registration.this, Login.class);
                            intent.putExtra("message", message);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String message) {
                            messageToast("", message);
                            setInprogress(progress,btn_register_now,false);
                        }
                    });
                }
                else{
                    messageToast("",errorMessage);
                }
            }

        });

    };
}