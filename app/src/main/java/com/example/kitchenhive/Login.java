package com.example.kitchenhive;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.Manifest;

public class Login extends BaseActivity {

    TextView  txtcreate,txt_forget_pwd;
    EditText inputemail,inputpass;
    Button btn_login;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtcreate = findViewById(R.id.txtcreate);
        txt_forget_pwd = findViewById(R.id.txt_forget_pwd);
        btn_login = findViewById(R.id.btnlogin);
        inputemail = findViewById(R.id.emailedittext);
        inputpass = findViewById(R.id.pwdedittext);
        progress = findViewById(R.id.login_progressbar);

        ask_permission();

        txtcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        txt_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prominentDialog();
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

    public void ask_permission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //System.out.println("here ask");
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get the location
                //getCurrentLocation();
                //System.out.println("im in granted");
            } else {
                // Permission denied, show a message or take an alternative action
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                //System.out.println("im in");
                //ask_permission();
            }
        }
    }


    public void prominentDialog(){
        Dialog dialog = new Dialog(Login.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgetpassword);
        Login.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final Button sendBtn = dialog.findViewById(R.id.send_btn);
        final Button cancleBtn = dialog.findViewById(R.id.email_cancle_btn);
        final EditText inputemail = dialog.findViewById(R.id.reg_email);
        final EditText input_new_pass = dialog.findViewById(R.id.reset_password);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Proceed();
                if (!inputemail.getText().toString().isEmpty() && !input_new_pass.getText().toString().isEmpty()){
                    APIClass apiClass = new APIClass();
                    apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                        @Override
                        public void onSuccess(String message, JSONObject json) {
                            dialog.dismiss();
                            messageToast("SUCCESS", message);
                        }
                        @Override
                        public void onFailure(String message) {
                            dialog.dismiss();
                            messageToast("ERROR", message);
                        }
                    });
                    apiClass.user_forget_password(inputemail.getText().toString(),input_new_pass.getText().toString());

                }
                else {
                    messageToast("ERROR","Please Enter Email");
                }
            }
        });
        dialog.show();
    }
}