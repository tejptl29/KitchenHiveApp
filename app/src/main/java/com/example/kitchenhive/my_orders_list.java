package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class my_orders_list extends BaseActivity {

    RecyclerView my_order;
    TextView txt_no_record_found;
    ArrayList<JSONObject> jsonObjectmyorders = new ArrayList<>();
    myorderAdapter myorderAdapter;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_list);

        txt_no_record_found = findViewById(R.id.no_rec_found_ordered);
        btn_back = findViewById(R.id.btn_back);

        my_order = findViewById(R.id.my_order_recycle);
        my_order.setLayoutManager(new LinearLayoutManager(my_orders_list.this, RecyclerView.VERTICAL, false));
        myorderAdapter = new myorderAdapter(my_orders_list.this, jsonObjectmyorders);
        my_order.setItemAnimator(new DefaultItemAnimator());
        my_order.setAdapter(myorderAdapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                        jsonObjectmyorders.clear();
                        int saleFlag = 1;
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectmyorders.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObjectmyorders.size() <= 0){
                    txt_no_record_found.setVisibility(View.VISIBLE);
                }
                else{
                    txt_no_record_found.setVisibility(View.GONE);
                }

                myorderAdapter.notifyDataSetChanged();
//                catelogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                //((MainActivity) mainActivity).messageToast("ERROR", message);
                jsonObjectmyorders.clear();
                if(jsonObjectmyorders.size() <= 0){
                    txt_no_record_found.setVisibility(View.VISIBLE);
                }
                else{
                    txt_no_record_found.setVisibility(View.GONE);
                }
                //jsonObjectscatelog.clear();
                // jsonObjectsprosale.clear();
                myorderAdapter.notifyDataSetChanged();
                //catelogAdapter.notifyDataSetChanged();
            }
        });
        String UserID = sharedPreferences.getString("UserID", "");
        apiClass.get_orders_data(UserID,"1");
    }

}



class myorderAdapter extends RecyclerView.Adapter<myorderViewHolder> {

    Activity activity;
    ArrayList<JSONObject> product = new ArrayList<>();

    myorderAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.product = product;
    }

    @NonNull
    public myorderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_items, parent,false);
        return new myorderViewHolder(view, viewType, activity);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(final myorderViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(product.get(position).toString());
            holder.txt_product.setText(empObject.getString("fname"));
            holder.txt_amount.setText("\u20B9 "+empObject.getString("product_total"));
            holder.txt_qty.setText("Qty:"+empObject.getString("product_quantity"));
            holder.date.setText("Date:"+empObject.getString("created_on"));
            holder.status.setText("Status:"+empObject.getString("order_status_text"));
            holder.txt_store.setText(empObject.getString("store_name"));

            // Set a max of 9 to correspond to 10 steps (0-9)
//            holder.seekBar.setMax(3);
//
//            holder.seekBar.setProgress(Integer.valueOf(empObject.getString("order_status"))); // Start from step 1
//
//            holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;  // This will consume the touch event and prevent interaction
//                }
//            });

            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
            }
            if(empObject.getString("ftype").equals("1")){
                Glide.with(activity).load(activity.getDrawable(R.drawable.non_veg_icon)).into(holder.veg_non);
            }
            else{
                Glide.with(activity).load(activity.getDrawable(R.drawable.veg1)).into(holder.veg_non);
            }
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

class myorderViewHolder extends RecyclerView.ViewHolder {

    TextView txt_product,txt_store;
    TextView txt_amount,date,txt_qty,status;
    ImageView pro_image,veg_non;
    ConstraintLayout constraintLayout;


    myorderViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        txt_store = itemView.findViewById(R.id.store_name);
        txt_product = itemView.findViewById(R.id.pro_name);
        txt_amount = itemView.findViewById(R.id.pro_amt);
        txt_qty = itemView.findViewById(R.id.pro_qty);
        date = itemView.findViewById(R.id.order_date);
        pro_image = itemView.findViewById(R.id.pro_img);
        status = itemView.findViewById(R.id.order_status);
        veg_non = itemView.findViewById(R.id.pro_veg_non);
        constraintLayout =  itemView.findViewById(R.id.my_order_items_product);

    }
}