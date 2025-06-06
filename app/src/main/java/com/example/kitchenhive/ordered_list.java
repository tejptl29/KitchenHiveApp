package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ordered_list extends BaseActivity {

    RecyclerView order_items_recyclerView;
    TextView txt_no_record_found;
    ArrayList<JSONObject> jsonObjectsordered = new ArrayList<>();
    orderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_list);
        txt_no_record_found = findViewById(R.id.no_rec_found_ordered);


        order_items_recyclerView = findViewById(R.id.order_items_recycle);
        order_items_recyclerView.setLayoutManager(new LinearLayoutManager(ordered_list.this, RecyclerView.VERTICAL, false));
        orderAdapter = new orderAdapter(ordered_list.this, jsonObjectsordered);
        order_items_recyclerView.setItemAnimator(new DefaultItemAnimator());
        order_items_recyclerView.setAdapter(orderAdapter);

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
        apiClass.get_orders_data(UserID);
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

class orderViewHolder extends RecyclerView.ViewHolder {

    TextView txt_product,txt_store;
    TextView txt_amount,txt_estimate_time,txt_qty;
    ImageView pro_image,veg_non;
    ConstraintLayout constraintLayout;


    orderViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        txt_store = itemView.findViewById(R.id.store_name);
        txt_product = itemView.findViewById(R.id.pro_name);
        txt_amount = itemView.findViewById(R.id.pro_amt);
        txt_qty = itemView.findViewById(R.id.pro_qty);
        txt_estimate_time = itemView.findViewById(R.id.pro_estimate_time);
        pro_image = itemView.findViewById(R.id.pro_img);
        veg_non = itemView.findViewById(R.id.pro_veg_non);
        constraintLayout =  itemView.findViewById(R.id.order_items_product);

    }
}