package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ordered_list extends BaseActivity {

    RecyclerView order_items_recyclerView;
    TextView txt_no_record_found;
    ArrayList<JSONObject> jsonObjectsordered = new ArrayList<>();
    orderAdapter orderAdapter;
    ImageView btn_back,btn_my_orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_list);
        txt_no_record_found = findViewById(R.id.no_rec_found_ordered);
        btn_back = findViewById(R.id.btn_back);
        btn_my_orders = findViewById(R.id.img_my_order);

        order_items_recyclerView = findViewById(R.id.order_items_recycle);
        order_items_recyclerView.setLayoutManager(new LinearLayoutManager(ordered_list.this, RecyclerView.VERTICAL, false));
        orderAdapter = new orderAdapter(ordered_list.this, jsonObjectsordered);
        order_items_recyclerView.setItemAnimator(new DefaultItemAnimator());
        order_items_recyclerView.setAdapter(orderAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ordered_list.this,my_orders_list.class);
                startActivity(intent);
            }
        });

        order_list();
    }

    public void order_list(){
        APIClass apiClass = new APIClass();
        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
            @Override
            public void onSuccess(String message, JSONObject json) {

                try {
                    if(new Utility().checkJSONDataNotNull(json, "orders")){
                        JSONArray jsonArray = new JSONArray(json.getString("orders"));
                        jsonObjectsordered.clear();
                        int saleFlag = 1;
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectsordered.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObjectsordered.size() <= 0){
                    txt_no_record_found.setVisibility(View.VISIBLE);
                }
                else{
                    txt_no_record_found.setVisibility(View.GONE);
                }

                orderAdapter.notifyDataSetChanged();
//                catelogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                //((MainActivity) mainActivity).messageToast("ERROR", message);
                jsonObjectsordered.clear();
                if(jsonObjectsordered.size() <= 0){
                    txt_no_record_found.setVisibility(View.VISIBLE);
                }
                else{
                    txt_no_record_found.setVisibility(View.GONE);
                }
                //jsonObjectscatelog.clear();
                // jsonObjectsprosale.clear();
                orderAdapter.notifyDataSetChanged();
                //catelogAdapter.notifyDataSetChanged();
            }
        });
        String UserID = sharedPreferences.getString("UserID", "");
        apiClass.get_orders_data(UserID,"0");
    }

    public void showAlertDialog(String store_order_item_id) {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message for the dialog
        builder.setTitle("Cancel Order")
                .setMessage("If you cancel your order, We will deduct 10% service charge. \n \nAre you sure you want to cancel your order ? \n ")

                // Set the "Yes" button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the Yes button click
                        // For example, log or perform an action
                        String UserID = sharedPreferences.getString("UserID", "");
                        APIClass apiClass = new APIClass();
                        apiClass.set_cancel_order(UserID, store_order_item_id);
                        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                            @Override
                            public void onSuccess(String message, JSONObject json) {
                                messageToast("SUCCESS", message);
                                order_list();
                            }
                            @Override
                            public void onFailure(String message) {
                                messageToast("ERROR", message);
                            }
                        });
                    }
                })
                // Set the "No" button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the No button click
                        // For example, dismiss the dialog
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

class orderAdapter extends RecyclerView.Adapter<orderViewHolder> {

    Activity activity;
    ArrayList<JSONObject> product = new ArrayList<>();

    orderAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.product = product;
    }

    @NonNull
    public orderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_items, parent,false);
        return new orderViewHolder(view, viewType, activity);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(final orderViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(product.get(position).toString());
            holder.txt_product.setText(empObject.getString("fname"));
            holder.txt_amount.setText("\u20B9 "+empObject.getString("product_total"));
            holder.txt_qty.setText("Qty:"+empObject.getString("product_quantity"));
            holder.txt_estimate_time.setText("Time:"+empObject.getString("formatted_prepartion_time"));
            holder.txt_store.setText(empObject.getString("store_name"));

            // Set a max of 9 to correspond to 10 steps (0-9)
            holder.seekBar.setMax(3);

            holder.seekBar.setProgress(Integer.valueOf(empObject.getString("order_status"))); // Start from step 1

            holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;  // This will consume the touch event and prevent interaction
                }
            });

            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
            }

            if(empObject.getString("ftype").equals("1")){
                Glide.with(activity).load(activity.getDrawable(R.drawable.non_veg_icon)).into(holder.veg_non);
            }
            else{
                Glide.with(activity).load(activity.getDrawable(R.drawable.veg1)).into(holder.veg_non);
            }

            if(empObject.getString("order_status").equals("0")){
                holder.btn_cancle_order.setVisibility(View.VISIBLE);
            }
            else{
                holder.btn_cancle_order.setVisibility(View.INVISIBLE);
            }

            holder.btn_cancle_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((ordered_list) activity).showAlertDialog(empObject.getString("store_order_item_id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(activity, single_food_dtl.class);
//                    try {
//                        intent.putExtra("store_id", empObject.getString("store_id"));
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    activity.startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getItemCount() {
        return product.size();
    }
}

class orderViewHolder extends RecyclerView.ViewHolder {

    TextView txt_product,txt_store;
    TextView txt_amount,txt_estimate_time,txt_qty;
    ImageView pro_image,veg_non,btn_cancle_order;
    ConstraintLayout constraintLayout;
    SeekBar seekBar;


    orderViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        txt_store = itemView.findViewById(R.id.store_name);
        txt_product = itemView.findViewById(R.id.pro_name);
        txt_amount = itemView.findViewById(R.id.pro_amt);
        txt_qty = itemView.findViewById(R.id.pro_qty);
        txt_estimate_time = itemView.findViewById(R.id.pro_estimate_time);
        pro_image = itemView.findViewById(R.id.pro_img);
        veg_non = itemView.findViewById(R.id.pro_veg_non);
        btn_cancle_order = itemView.findViewById(R.id.btn_cancle_order);
        constraintLayout =  itemView.findViewById(R.id.order_items_product);

        seekBar = itemView.findViewById(R.id.order_status_bar);
    }

}
