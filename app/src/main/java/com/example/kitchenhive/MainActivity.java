package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.kitchenhive.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{

    Button btn_logout;
    SwipeRefreshLayout swipeRefreshLayout;
    ActivityMainBinding binding;
    ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    ArrayList<JSONObject> jsonObjectspro = new ArrayList<>();
    ArrayList<JSONObject> jsonObjectscatelog = new ArrayList<>();
    List<CartItem> jsonObjectscart = new ArrayList<>();

    categoriesAdapter categoriesAdapter;
    productAdapter productAdapter;
    catelogAdapter catelogAdapter;
    cartAdapter cartAdapter;


    TextView cat_no_record;
    TextView home_no_rec_found;
    RecyclerView recyclerViewcatlog,recyclerView,cartrecyclerView;;
    Switch non_veg_switch;

    ArrayList<String> str_categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String to_cart = intent.getStringExtra("to_cart");


        if(to_cart != null && to_cart.equals("1")) {
            replaceFragment(new CartFragment(MainActivity.this));
        }
        else{
            replaceFragment(new HomeFragment(MainActivity.this));
        }
        binding.bottomnavview.setSelectedItemId(R.id.home);

        binding.bottomnavview.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment(MainActivity.this));
            }
            else if(item.getItemId() == R.id.catalog){
                replaceFragment(new CatalogFragment(MainActivity.this));
            }
            else if(item.getItemId() == R.id.cart){
                replaceFragment(new CartFragment(MainActivity.this));
            }
            else if(item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment(MainActivity.this));
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //bind_cart_bottom(false);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_frag);

        if (currentFragment != null) {
            String fragmentName = currentFragment.getClass().getSimpleName();
            if(fragmentName.equals("CatalogFragment")){
                bind_cart_bottom(true, MainActivity.this);
            }
            else{
                bind_cart_bottom(false, MainActivity.this);
            }
            System.out.println(("MainActivity"+ "Current Fragment: " + fragmentName));
        } else {

        }
    }

    public void prominentDialog(){
        Dialog dialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.non_veg);
        MainActivity.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final Button sendBtn = dialog.findViewById(R.id.proceed_btn);
        final Button cancleBtn = dialog.findViewById(R.id.btn_cancle);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                non_veg_switch.setChecked(true);
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Proceed();
                sharedPreferences.edit().putBoolean("VEG", false).apply();
                call_dashboard_api();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void get_products_api_call(String search,String cat_id,String veg){
        APIClass apiClass = new APIClass();
        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
            @Override
            public void onSuccess(String message, JSONObject json) {
                // spinner in catelog call
                jsonObjectscatelog.clear();
                try {
                    if(new Utility().checkJSONDataNotNull(json, "products")){
                        JSONArray jsonArray = new JSONArray(json.getString("products"));
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectscatelog.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObjectscatelog.size() <= 0){
                    cat_no_record.setVisibility(View.VISIBLE);
                    recyclerViewcatlog.setVisibility(View.GONE);
                }
                else{
                   cat_no_record.setVisibility(View.GONE);
                    recyclerViewcatlog.setVisibility(View.VISIBLE);
                }
                catelogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                //((MainActivity) mainActivity).messageToast("ERROR", message);
                System.out.println(message);
                jsonObjectscatelog.clear();
                if(jsonObjectscatelog.size() <= 0){
                   cat_no_record.setVisibility(View.VISIBLE);
                }
                else{
                   cat_no_record.setVisibility(View.GONE);
                }
                catelogAdapter.notifyDataSetChanged();
            }
        });
        String UserID = sharedPreferences.getString("UserID", "");
        apiClass.get_products(UserID,search,cat_id,veg);
    }


    public void call_dashboard_api() {
        String veg = "1";
        if(sharedPreferences.contains("VEG")){
            veg = (sharedPreferences.getBoolean("VEG", true) == true ? "1" : "0");
        }
        _call_dashboard_api(veg);
    }

    public void _call_dashboard_api(String veg){
        APIClass apiClass = new APIClass();
        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
            @Override
            public void onSuccess(String message, JSONObject json) {

                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                // spinner in catelog call
                try {
                    if(new Utility().checkJSONDataNotNull(json, "categories")){
                        JSONArray jsonArray = new JSONArray(json.getString("categories"));
                        jsonObjects.clear();
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjects.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }
                    if(new Utility().checkJSONDataNotNull(json, "products")){
                        JSONArray jsonArray = new JSONArray(json.getString("products"));
                        jsonObjectspro.clear();
                        // jsonObjectscatelog.clear();
//                        jsonObjectsprosale.clear();
                        int saleFlag = 1;
                        if(jsonArray.length() > 0) {
                            // txt_no_record.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectspro.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObjects.size() <= 0 && jsonObjectspro.size() <= 0){
                    home_no_rec_found.setVisibility(View.VISIBLE);
                }
                else{
                    home_no_rec_found.setVisibility(View.GONE);
                }

                categoriesAdapter.notifyDataSetChanged();
                productAdapter.notifyDataSetChanged();
//                catelogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                //((MainActivity) mainActivity).messageToast("ERROR", message);
                System.out.println(message);
                jsonObjects.clear();
                jsonObjectspro.clear();
                if(jsonObjects.size() <= 0 && jsonObjectspro.size() <= 0){
                    home_no_rec_found.setVisibility(View.VISIBLE);
                }
                else{
                    home_no_rec_found.setVisibility(View.GONE);
                }
                //jsonObjectscatelog.clear();
                // jsonObjectsprosale.clear();
                categoriesAdapter.notifyDataSetChanged();
                productAdapter.notifyDataSetChanged();
                //catelogAdapter.notifyDataSetChanged();
            }
        });
        String UserID = sharedPreferences.getString("UserID", "");
        apiClass.dashboard_data(UserID, veg);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frag,fragment);
        fragmentTransaction.commit();
    }


}

class categoriesAdapter extends RecyclerView.Adapter<categoriesViewHolder> {

    Activity activity;
    ArrayList<JSONObject> catgories = new ArrayList<>();

    categoriesAdapter(Activity activity, ArrayList<JSONObject> catgories)
    {
        this.activity = activity;
        this.catgories = catgories;
    }

    @NonNull
    public categoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitems, parent,false);
        return new categoriesViewHolder(view, viewType, activity);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(final categoriesViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(catgories.get(position).toString());
            holder.txt_categories.setText(empObject.getString("name"));

            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.catimage);
            }

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("cat_id", empObject.getString("id"));
                        bundle.putString("cat_name", empObject.getString("name"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    CatalogFragment fragobj = new CatalogFragment(activity);
                    fragobj.setArguments(bundle);
                    ((MainActivity)activity).replaceFragment(fragobj);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getItemCount() {
        return catgories.size();
    }
}

class categoriesViewHolder extends RecyclerView.ViewHolder {

    TextView txt_categories;
    ImageView catimage;
    ConstraintLayout constraintLayout;


    categoriesViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        txt_categories = itemView.findViewById(R.id.txtview_catitem);
        catimage = itemView.findViewById(R.id.cat_image);
        constraintLayout =  itemView.findViewById(R.id.cat_view_items);

    }
}

class productAdapter extends RecyclerView.Adapter<productViewHolder> {

    Activity activity;
    ArrayList<JSONObject> product = new ArrayList<>();

    productAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.product = product;
    }

    @NonNull
    public productViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_for_sales, parent,false);
        return new productViewHolder(view, viewType, activity);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(final productViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(product.get(position).toString());
            holder.txt_product.setText(empObject.getString("fname"));
            holder.txt_amount.setText(empObject.getString("fprice"));
            holder.txt_store.setText(empObject.getString("store_name"));
            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
            }
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, single_food_dtl.class);
                    try {
                        intent.putExtra("store_id", empObject.getString("store_id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    activity.startActivity(intent);
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

class productViewHolder extends RecyclerView.ViewHolder {

    TextView txt_product,txt_store;
    TextView txt_amount;
    ImageView pro_image;
    ConstraintLayout constraintLayout;


    productViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        txt_store = itemView.findViewById(R.id.store_name);
        txt_product = itemView.findViewById(R.id.txt_food);
        txt_amount = itemView.findViewById(R.id.txt_price);
        pro_image = itemView.findViewById(R.id.pro_img);
        constraintLayout =  itemView.findViewById(R.id.sales_view_items);

    }
}

class catelogAdapter extends RecyclerView.Adapter<catelogViewHolder> {

    Activity activity;
    ArrayList<JSONObject> catelog = new ArrayList<>();

    catelogAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.catelog = product;
    }

    @NonNull
    public catelogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_for_sales, parent,false);
        return new catelogViewHolder(view, viewType, activity);
    }


    public void onBindViewHolder(final catelogViewHolder holder, final int position) {
        try {
            JSONObject empObject = new JSONObject(catelog.get(position).toString());
            holder.txt_product.setText(empObject.getString("fname"));
            holder.txt_store.setText(empObject.getString("store_name"));
            holder.txt_amount.setText(empObject.getString("fprice"));

//
//            if(empObject.getString("user_fav").equals("1")){
//                holder.fav_btn.setTag("1");
//                System.out.println("in fav");
//                holder.fav_btn.setImageDrawable(activity.getDrawable(R.drawable.favorites_dark));
//            }
//            else{
//                holder.fav_btn.setTag("0");
//                holder.fav_btn.setImageDrawable(activity.getDrawable(R.drawable.favorites));
//            }

            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
            }

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, single_food_dtl.class);
                    try {
                        intent.putExtra("store_id", empObject.getString("store_id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    activity.startActivity(intent);
                }
            });

//            holder.fav_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    try {
//                        String action = (view.getTag().equals("1") ? "UNFAV" : "FAV");
//                        ((MainActivity) activity).callSetFavProduct(((MainActivity) activity).sharedPreferences.getString("UserID", ""), empObject.getString("id"), action, holder.fav_btn, activity);
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getItemCount() {
        return catelog.size();
    }
}

class catelogViewHolder extends RecyclerView.ViewHolder {

    TextView txt_store,txt_product;
    TextView txt_amount;
    ImageView pro_image;
    ConstraintLayout constraintLayout;


    catelogViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.sales_view_items);
        txt_store = itemView.findViewById(R.id.store_name);
        txt_product = itemView.findViewById(R.id.txt_food);
        txt_amount = itemView.findViewById(R.id.txt_price);
        pro_image = itemView.findViewById(R.id.pro_img);
    }
}

class cartAdapter extends RecyclerView.Adapter<cartViewHolder> {

    Activity activity;
    List<CartItem> cart = new ArrayList<>();

    cartAdapter(Activity activity, List<CartItem> product)
    {
        this.activity = activity;
        this.cart = product;
    }

    @NonNull
    public cartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_ui, parent,false);
        return new cartViewHolder(view, viewType, activity);
    }


    public void onBindViewHolder(final cartViewHolder holder, final int position) {

            System.out.println(cart.get(position));
            CartItem item = cart.get(position);
            holder.txt_product.setText(item.getProductName());
            holder.txt_amount.setText("\u20B9 "+String.valueOf(item.getPrice()));
            holder.txt_qty.setText(String.valueOf("Qty:-"+item.getQuantity()));
            holder.txt_finall_amt.setText(String.valueOf("\u20B9 "+item.getTotal()));
            if(!item.getImage().isEmpty()) {
                Glide.with(activity).load(item.getImage()).into(holder.pro_image);
            }
            if(item.getVeg().equals("1")){
                Glide.with(activity).load(activity.getDrawable(R.drawable.non_veg_icon)).into(holder.txt_veg_non);
            }
            else{
                Glide.with(activity).load(activity.getDrawable(R.drawable.veg1)).into(holder.txt_veg_non);
            }
//
//            if(empObject.getString("user_fav").equals("1")){
//                holder.fav_btn.setTag("1");
//                System.out.println("in fav");
//                holder.fav_btn.setImageDrawable(activity.getDrawable(R.drawable.favorites_dark));
//            }
//            else{
//                holder.fav_btn.setTag("0");
//                holder.fav_btn.setImageDrawable(activity.getDrawable(R.drawable.favorites));
//            }

//            if(new Utility().checkJSONDataNotNull(empObject, "image_url")) {
//                Glide.with(activity).load(empObject.getString("image_url")).into(holder.pro_image);
//            }

            holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartManager cartManager = new CartManager(activity);
                    cartManager.removeItem(item.getProductId());
                    CartFragment fragobj = new CartFragment(activity);
                    ((MainActivity)activity).replaceFragment(fragobj);
                }
            });

    }
    public int getItemCount() {
        return cart.size();
    }
}

class cartViewHolder extends RecyclerView.ViewHolder {

    TextView txt_product,txt_amount,txt_finall_amt,txt_qty;
    ImageView pro_image,txt_veg_non;
    ImageView btn_remove;
    ConstraintLayout constraintLayout;


    cartViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.cart_item);
        txt_product = itemView.findViewById(R.id.pro_name);
        txt_amount = itemView.findViewById(R.id.pro_amt);
        txt_qty = itemView.findViewById(R.id.pro_qty);
        txt_finall_amt = itemView.findViewById(R.id.total_amt);
        pro_image = itemView.findViewById(R.id.pro_img);
        txt_veg_non = itemView.findViewById(R.id.pro_veg_non);
        btn_remove = itemView.findViewById(R.id.btn_remove_cart);
    }
}


