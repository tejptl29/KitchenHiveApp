package com.example.kitchenhive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected Activity mainActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(Activity activity) {
        mainActivity = activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerViewpro;
    ImageSlider imageSlider;
    EditText txt_search;
    TextView txt_customer_name,name_initial;
    ImageView btn_search,btn_ordered_list;
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_ordered_list = view.findViewById(R.id.img_order);
        imageSlider = view.findViewById(R.id.img_view);
        txt_search= view.findViewById(R.id.search_bar);
        btn_search = view.findViewById(R.id.btn_search);
        txt_customer_name = view.findViewById(R.id.txt_name);
        name_initial = view.findViewById(R.id.name_inital);

        ((MainActivity) mainActivity).home_no_rec_found = view.findViewById(R.id.home_no_rec_found);

        //customer name display
        txt_customer_name.setText(((MainActivity) mainActivity).sharedPreferences.getString("UserName", ""));

        //name initial display
        String name_initial_str = new Utility().getNameInitials(((MainActivity) mainActivity).sharedPreferences.getString("UserName", ""));
        name_initial.setText(name_initial_str);

        ((MainActivity) mainActivity).non_veg_switch = view.findViewById(R.id.btn_veg);
        boolean veg = true;
        if( ((MainActivity) mainActivity).sharedPreferences.contains("VEG")){
            veg = ((MainActivity) mainActivity).sharedPreferences.getBoolean("VEG", true);
        }
        ((MainActivity) mainActivity).non_veg_switch.setChecked(veg);


        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img7, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


        ((MainActivity) mainActivity).non_veg_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false) {
                    ((MainActivity) mainActivity).prominentDialog();
                }
                else{
                    ((MainActivity) mainActivity).sharedPreferences.edit().putBoolean("VEG", true).apply();
                    ((MainActivity) mainActivity).call_dashboard_api();
                }
            }
        });

        ((MainActivity) mainActivity).swipeRefreshLayout = view.findViewById(R.id.dashboard_refersh);
        ((MainActivity) mainActivity).swipeRefreshLayout.setOnRefreshListener(this);

        //categories recycler
        ((MainActivity) mainActivity). recyclerView = view.findViewById(R.id.category_recycle);
        ((MainActivity) mainActivity). recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 1, RecyclerView.HORIZONTAL, false));
        ((MainActivity) mainActivity).categoriesAdapter = new categoriesAdapter(((MainActivity) mainActivity), ((MainActivity) mainActivity).jsonObjects);
        ((MainActivity) mainActivity). recyclerView.setItemAnimator(new DefaultItemAnimator());
        ((MainActivity) mainActivity). recyclerView.setAdapter(((MainActivity) mainActivity).categoriesAdapter);

        ((MainActivity) mainActivity).call_dashboard_api();

        //product item categories
        recyclerViewpro = view.findViewById(R.id.item_recycle);
        recyclerViewpro.setLayoutManager(new GridLayoutManager(mainActivity, 1, RecyclerView.VERTICAL, false));
        ((MainActivity) mainActivity).productAdapter = new productAdapter(((MainActivity) mainActivity), ((MainActivity) mainActivity).jsonObjectspro);
        recyclerViewpro.setItemAnimator(new DefaultItemAnimator());
        recyclerViewpro.setAdapter(((MainActivity) mainActivity).productAdapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("search", txt_search.getText().toString());
                CatalogFragment fragobj = new CatalogFragment(mainActivity);
                fragobj.setArguments(bundle);
                ((MainActivity) mainActivity).replaceFragment(fragobj);
            }
        });

       ((MainActivity) mainActivity).bind_cart_bottom(false, mainActivity);

       btn_ordered_list.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mainActivity,ordered_list.class);
              // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
           }
       });

        return view;
    }

    @Override
    public void onRefresh() {
        if(!((MainActivity) mainActivity).swipeRefreshLayout.isRefreshing())
            ((MainActivity) mainActivity).swipeRefreshLayout.setRefreshing(true);
        ((MainActivity) mainActivity).call_dashboard_api();
    }


}