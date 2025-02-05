package com.example.kitchenhive;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    public CatalogFragment() {
        // Required empty public constructor
    }

    protected Activity mainActivity;

    public CatalogFragment(Activity activity) {
        mainActivity = activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatlogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView cat_name;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("cat_id");
            mParam3 = getArguments().getString("cat_name");
            mParam2 = getArguments().getString("search");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_catlog, container, false);
        cat_name = view.findViewById(R.id.txt_cat);

        ((MainActivity) mainActivity).cat_no_record = view.findViewById(R.id.no_rec_found);
        cat_name.setText((mParam3 != null ? mParam3 : "Food"));

        ((MainActivity) mainActivity).recyclerViewcatlog = view.findViewById(R.id.catelog_items_recycle);
        ((MainActivity) mainActivity).recyclerViewcatlog.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false));
        ((MainActivity) mainActivity).catelogAdapter = new catelogAdapter(((MainActivity) mainActivity), ((MainActivity) mainActivity).jsonObjectscatelog);
        ((MainActivity) mainActivity).recyclerViewcatlog.setItemAnimator(new DefaultItemAnimator());
        ((MainActivity) mainActivity).recyclerViewcatlog.setAdapter(((MainActivity) mainActivity).catelogAdapter);

        ((MainActivity) mainActivity).get_products_api_call(mParam2, mParam1,(((MainActivity) mainActivity).sharedPreferences.getBoolean("VEG", true) == true ? "1" : "0"));

        ((MainActivity) mainActivity).bind_cart_bottom(true, mainActivity);
        return view;
    }
}