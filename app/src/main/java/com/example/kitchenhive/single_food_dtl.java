package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class single_food_dtl extends BaseActivity {

    TextView txt_veg_nonveg,txt_distance,txt_open_close,txt_open_close_time,txt_store_name,txt_owner_name,txt_store_address,txt_store_number;
    RecyclerView store_pro_recyclerView;
    String store_id = "";
    ImageButton btn_back;


    ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    storeproAdapter storeproAdapter;

    FusedLocationProviderClient fusedLocationClient;
    double latitude = 0;
    double longitude = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store_dtl_pro);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();
        getCurrentLocation();

        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");

        btn_back = findViewById(R.id.btn_back);

        txt_veg_nonveg = findViewById(R.id.store_veg_nonveg);
        txt_distance = findViewById(R.id.store_distance);
        txt_open_close = findViewById(R.id.store_open_close);
        txt_open_close_time = findViewById(R.id.store_open_close_time);
        txt_store_name = findViewById(R.id.store_name);
        txt_owner_name = findViewById(R.id.store_owner_name);
        txt_store_address = findViewById(R.id.store_address);
        txt_store_number = findViewById(R.id.store_number);
        store_pro_recyclerView = findViewById(R.id.stores_pro_items);

        store_pro_recyclerView.setLayoutManager(new LinearLayoutManager(single_food_dtl.this, RecyclerView.VERTICAL, false));
        storeproAdapter = new storeproAdapter(single_food_dtl.this, jsonObjects);
        store_pro_recyclerView.setItemAnimator(new DefaultItemAnimator());
        store_pro_recyclerView.setAdapter(storeproAdapter);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        String userID = sharedPreferences.getString("UserID", "");
        APIClass apiClass = new APIClass();
        apiClass.get_stores_data(userID, store_id, String.valueOf(latitude),String.valueOf(longitude));
        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
            @Override
            public void onSuccess(String message, JSONObject json) {
                try {
                    JSONObject store = new JSONObject(json.getString("store"));
                    txt_veg_nonveg.setText(store.getString("veg_non"));
                    txt_open_close.setText(store.getString("open_close"));
                    txt_open_close_time.setText(store.getString("open_close_time"));
                    txt_store_name.setText(store.getString("store_name"));
                    txt_distance.setText(store.getString("distance"));
                    txt_owner_name.setText(store.getString("owner_name"));
                    txt_store_address.setText(store.getString("store_address"));
                    txt_store_number.setText(store.getString("contact_no"));


                    if(new Utility().checkJSONDataNotNull(json, "products")){
                        JSONArray jsonArray = new JSONArray(json.getString("products"));
                        jsonObjects.clear();
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjects.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }
                    storeproAdapter.notifyDataSetChanged();
                    //txtamount.setText(product.getString("amount"));
                    //txtdesc.setText(product.getString("description"));
//                    if(new Utility().checkJSONDataNotNull(product, "image_url")) {
//                        Glide.with(ProductDetail.this).load(product.getString("image_url")).into(proimg);
//                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onFailure(String message) {

                messageToast("ERROR", message);
            }
        });

        bind_cart_bottom(true, single_food_dtl.this);
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Use the location object to get the latitude and longitude
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } else {
                            // Location is null, handle accordingly (e.g., prompt user to enable GPS)
                            System.out.println("Location : Location is null");
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 3 && grantResults.length > 0){
            checkPermission();
        }
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(single_food_dtl.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //System.out.println("NOT GRANTED");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                String backAccess = "";
                if(ContextCompat.checkSelfPermission(single_food_dtl.this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    backAccess = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
                ActivityCompat.requestPermissions(single_food_dtl.this, new String[]{backAccess, android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);
            }else{
                ActivityCompat.requestPermissions(single_food_dtl.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);
            }
        }else{
            getCurrentLocation();
        }
    }

    //quantity increase decrease function
    public int change_qty(String type, TextView txt_qty, Button btn_add, ConstraintLayout layout){
        int qty = Integer.valueOf(txt_qty.getText().toString());
        if(type.equals("INC")){
            qty++;
        } else if(type.equals("DEC")){
            if(qty>=1){
                qty--;
            }
            if(qty == 0){
                btn_add.setVisibility(View.VISIBLE);
                layout.setVisibility(View.INVISIBLE);
            }
        }
        txt_qty.setText(String.valueOf(qty));
        return qty;
    }
}

class storeproAdapter extends RecyclerView.Adapter<storeproViewHolder> {

    Activity activity;
    ArrayList<JSONObject> storepro = new ArrayList<>();

    storeproAdapter(Activity activity, ArrayList<JSONObject> storepro)
    {
        this.activity = activity;
        this.storepro = storepro;
    }

    @NonNull
    public storeproViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_store_pro_ui, parent,false);
        return new storeproViewHolder(view, viewType, activity);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(final storeproViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(storepro.get(position).toString());
            System.out.println(empObject);
            holder.txt_food_name.setText(empObject.getString("fname"));
            holder.txt_price.setText("\u20B9 "+empObject.getString("fprice"));
            holder.txt_desc.setText(empObject.getString("fdescription"));
            holder.txt_pre_time.setText(empObject.getString("preparation_time"));
            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
            }
            if(empObject.getString("ftype").equals("1")){
                Glide.with(activity).load(activity.getDrawable(R.drawable.non_veg_icon)).into(holder.txt_veg_non);
            }
            else{
                Glide.with(activity).load(activity.getDrawable(R.drawable.veg1)).into(holder.txt_veg_non);
            }

            if(empObject.getString("product_closed").equals("1")){
                holder.btn_add.setVisibility(View.GONE);
                holder.qty_layout.setVisibility(View.GONE);
            }
            else{
                CartManager cartManager = new CartManager(activity);
                int pro_qty = cartManager.getItemQuantity(empObject.getString("id"));
                if(pro_qty > 0){
                    holder.btn_add.setVisibility(View.INVISIBLE);
                    holder.qty_layout.setVisibility(View.VISIBLE);
                    holder.txt_qty.setText(String.valueOf(pro_qty));
                }
                else{
                    holder.btn_add.setVisibility(View.VISIBLE);
                    holder.qty_layout.setVisibility(View.INVISIBLE);
                    holder.txt_qty.setText("0");
                }

                holder.btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.btn_add.setVisibility(View.INVISIBLE);
                        holder.qty_layout.setVisibility(View.VISIBLE);
                        holder.txt_qty.setText("1");

                        try {

                            double total = Double.valueOf(empObject.getString("fprice")) * 1;
                            CartItem newItem = new CartItem(empObject.getString("id"), empObject.getString("store_id"), empObject.getString("fname"), Double.valueOf(empObject.getString("fprice")), 1, empObject.getString("image_url"), total, empObject.getString("ftype"));
                            cartManager.addItem(newItem);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        ((single_food_dtl) activity).bind_cart_bottom(true, activity);
                    }
                });

                holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int qty = ((single_food_dtl) activity).change_qty("DEC", holder.txt_qty, holder.btn_add, holder.qty_layout);

                        try {
                            CartManager cartManager = new CartManager(activity);
                            if(qty > 0) {
                                cartManager.removeItem(empObject.getString("id"));
                                double total = Double.valueOf(empObject.getString("fprice")) * qty;
                                CartItem newItem = new CartItem(empObject.getString("id"), empObject.getString("store_id"), empObject.getString("fname"), Double.valueOf(empObject.getString("fprice")), qty, empObject.getString("image_url"), total, empObject.getString("ftype"));
                                cartManager.addItem(newItem);
                            }
                            else{
                                cartManager.removeItem(empObject.getString("id"));
                            }
                            System.out.println(cartManager.getCart());
                            System.out.println(cartManager.getItemQuantity(empObject.getString("id")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        ((single_food_dtl) activity).bind_cart_bottom(true, activity);
                    }
                });

                holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int qty = ((single_food_dtl) activity).change_qty("INC", holder.txt_qty, holder.btn_add, holder.qty_layout);
                        try {
                            CartManager cartManager = new CartManager(activity);
                            if(qty > 0) {
                                cartManager.removeItem(empObject.getString("id"));
                                double total = Double.valueOf(empObject.getString("fprice")) * qty;
                                CartItem newItem = new CartItem(empObject.getString("id"), empObject.getString("store_id"), empObject.getString("fname"), Double.valueOf(empObject.getString("fprice")), qty, empObject.getString("image_url"), total, empObject.getString("ftype"));
                                cartManager.addItem(newItem);
                            /*if(cartManager.product_exist(empObject.getString("id"))){
                                cartManager.setItemQuantity(empObject.getString("id"), qty);
                            }
                            else{
                                CartItem newItem = new CartItem(empObject.getString("id"), empObject.getString("fname"), Double.valueOf(empObject.getString("fprice")), 1);
                                cartManager.addItem(newItem);
                            }*/
                            }
                            else{
                                cartManager.removeItem(empObject.getString("id"));
                            }
                            System.out.println(cartManager.getCart());
                            System.out.println(cartManager.getItemQuantity(empObject.getString("id")));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        ((single_food_dtl) activity).bind_cart_bottom(true, activity);
                    }
                });
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getItemCount() {
        return storepro.size();
    }
}

class storeproViewHolder extends RecyclerView.ViewHolder {

    TextView txt_qty,txt_food_name,txt_price,txt_desc,txt_pre_time;
    ImageView pro_image,txt_veg_non;
    Button btn_add,btn_plus,btn_minus;
    ConstraintLayout qty_layout;
    ConstraintLayout constraintLayout;


    storeproViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        constraintLayout =  itemView.findViewById(R.id.particular_store_product);
        qty_layout = itemView.findViewById(R.id.qty_layout);
        txt_qty = itemView.findViewById(R.id.pro_qty);
        btn_add = itemView.findViewById(R.id.btn_add);
        btn_minus = itemView.findViewById(R.id.btn_minus);
        btn_plus = itemView.findViewById(R.id.btn_plus);
        txt_veg_non = itemView.findViewById(R.id.pro_veg_non);
        pro_image = itemView.findViewById(R.id.pro_img);
        txt_food_name = itemView.findViewById(R.id.pro_name);
        txt_price = itemView.findViewById(R.id.pro_amt);
        txt_desc = itemView.findViewById(R.id.pro_desc);
        txt_pre_time = itemView.findViewById(R.id.pro_pre_time);


    }
}
