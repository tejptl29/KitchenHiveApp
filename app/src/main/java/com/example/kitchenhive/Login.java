package com.example.kitchenhive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Login extends BaseActivity {

    TextView  txtcreate;
    EditText inputemail,inputpass;
    Button btn_login;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtcreate = findViewById(R.id.txtcreate);
        btn_login = findViewById(R.id.btnlogin);
        inputemail = findViewById(R.id.emailedittext);
        inputpass = findViewById(R.id.pwdedittext);
        progress = findViewById(R.id.login_progressbar);


        txtcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validEmail = new Utility().isValidEmail(inputemail.getText().toString().trim());

                if (inputemail.getText().toString().trim().isEmpty() || inputpass.getText().toString().trim().isEmpty()){
                    messageToast("ERROR","Please Enter Credentials");
                }else if(!validEmail){
                    messageToast("ERROR","Please Enter Valid Email Address");
                }
                else {

                    if (!inputemail.getText().toString().trim().isEmpty() && !inputpass.getText().toString().trim().isEmpty()) {

                        // Api call & Login Code
                        HashMap<String, String> map = new HashMap<>();
                        map.put("username", inputemail.getText().toString());
                        map.put("password", inputemail.getText().toString());

                        setInprogress(progress,btn_login,true);

                        APIClass apiClass = new APIClass();
                        apiClass.check_user_auth(inputemail.getText().toString(), inputpass.getText().toString());
                        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                            @Override
                            public void onSuccess(String message, JSONObject json) {
                                setInprogress(progress,btn_login,false);
                                try {
                                    if (checkJSONDataNotNull(json, "id")) {
                                        sharedPreferences.edit().putString("UserID", json.getString("id")).apply();

                                        if (new Utility().checkJSONDataNotNull(json, "name")) {
                                            sharedPreferences.edit().putString("UserName", json.getString("name")).apply();
                                        }

                                        if(new Utility().checkJSONDataNotNull(json, "phone")){
                                            sharedPreferences.edit().putString("UserPhone",json.getString("phone")).apply();
                                        }

                                        if (new Utility().checkJSONDataNotNull(json, "email")) {
                                            sharedPreferences.edit().putString("UserEmail", json.getString("email")).apply();
                                        }

                                        Intent intent = new Intent(Login.this, MainActivity.class);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    } else {
                                        messageToast("", "Server did not respond");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                messageToast("ERROR", message);
                                setInprogress(progress,btn_login,false);
                            }
                        });
                    } else {
                        messageToast("ERROR", "Please Enter Credentials");
                    }
                }

            }
        });

    }

}