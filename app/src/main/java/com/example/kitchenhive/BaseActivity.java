package com.example.kitchenhive;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView errorBlock;
    Handler handler = new Handler();
    ConstraintLayout cart_bottom_layout;
    Button btn_view_cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.service", Context.MODE_PRIVATE);
    }

    public boolean checkJSONDataNotNull(JSONObject jsonObject, String key){
        try {
            if(jsonObject.has(key) && !jsonObject.isNull(key) && !jsonObject.getString(key).trim().isEmpty()){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkStringNotNull(String string){
        if(!string.trim().isEmpty()){
            return true;
        }
        return false;
    }

    public void doLogout(Activity activity){
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(activity, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void  setInprogress(ProgressBar progressbar, Button button, boolean inprogress){
        if (inprogress){
            progressbar.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }else {
            progressbar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }




    //error msg display & layout
//    public void errorMessageToast(String message)
//    {
//        handler.removeCallbacksAndMessages(null);
//        errorBlock = findViewById(R.id.error_block);
//        errorBlock.setBackgroundColor(Color.RED);
//        errorBlock.setTextColor(Color.WHITE);
//        errorBlock.setText(message);
//        errorBlock.setVisibility(View.VISIBLE);
//        errorBlock.animate().alpha(0.0f);
//        errorBlock.animate()
//                .setDuration(500)
//                .alpha(1.0f)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                errorBlock.animate()
//                                        .alpha(0.0f)
//                                        .setDuration(500)
//                                        .setListener(new AnimatorListenerAdapter() {
//                                            @Override
//                                            public void onAnimationEnd(Animator animation) {
//                                                super.onAnimationEnd(animation);
//                                                errorBlock.setVisibility(View.GONE);
//                                            }
//                                        });
//                            }
//                        }, 3000);
//
//                    }
//                });
//    }

    public void bind_cart_bottom(boolean show, Activity activity){
        cart_bottom_layout = findViewById(R.id.cart_bottom_layout);
        btn_view_cart = findViewById(R.id.btn_view_cart);

        btn_view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity instanceof MainActivity){
                    ((MainActivity) activity).replaceFragment(new CartFragment(activity));
                }
                else{
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("to_cart", "1");
                    startActivity(intent);
                }
            }
        });

        if(show){
            cart_bottom_layout.setVisibility(View.VISIBLE);
            CartManager cartManager = new CartManager(BaseActivity.this);
            int total_items = cartManager.getCart().size();
            if(total_items > 0){

                btn_view_cart.setText("View Cart \n"+total_items+" items");



                cart_bottom_layout.setVisibility(View.VISIBLE);
            }
            else{
                cart_bottom_layout.setVisibility(View.GONE);
            }
        }
        else{
            cart_bottom_layout.setVisibility(View.GONE);
        }
    }

    // msg display & layout
    public void messageToast(String type, String message)
    {
        handler.removeCallbacksAndMessages(null);
        errorBlock = findViewById(R.id.error_block);

        if(type.equals("SUCCESS")){
            errorBlock.setBackgroundColor(Color.GREEN);
        }
        else if(type.equals("WARNING")){
            errorBlock.setBackgroundColor(Color.YELLOW);
        }
        else{
            errorBlock.setBackgroundTintList(getResources().getColorStateList(R.color.error));

//            errorBlock.setBackgroundColor(Color.RED);
        }

        //errorBlock.setBackgroundColor(Color.RED);
        errorBlock.setTextColor(Color.WHITE);
        errorBlock.setText(message);
        errorBlock.setVisibility(View.VISIBLE);
        errorBlock.animate().alpha(0.0f);
        errorBlock.animate()
                .setDuration(500)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorBlock.animate()
                                        .alpha(0.0f)
                                        .setDuration(500)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                errorBlock.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        }, 3000);

                    }
                });
    }

//    public void callSetFavProduct(String user_id, String product_id, String action, ImageButton button, Activity activity){
//        APIClass apiClass = new APIClass();
//        apiClass.set_products_fav(user_id,product_id,action);
//        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
//            @Override
//            public void onSuccess(String message, JSONObject json) {
//                if(action.equals("FAV")) {
//                    button.setImageDrawable(getDrawable(R.drawable.favorites_dark));
//                    button.setTag("1");
//
//
//                }
//                else{
//                    button.setImageDrawable(getDrawable(R.drawable.favorites));
//                    button.setTag("0");
//                }
//                ((MainActivity) activity).fav_call(activity);
//            }
//            @Override
//            public void onFailure(String message) {
//
//            }
//        });
//    }
}
