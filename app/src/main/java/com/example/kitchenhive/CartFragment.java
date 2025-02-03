package com.example.kitchenhive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    protected Activity mainActivity;

    public CartFragment(Activity activity) {
        mainActivity = activity;
    }



    View view;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        CartManager cartManager = new CartManager(mainActivity);
        ((MainActivity) mainActivity).jsonObjectscart.clear();
        ((MainActivity) mainActivity).jsonObjectscart = cartManager.getCart();

        ((MainActivity) mainActivity).cartrecyclerView = view.findViewById(R.id.cart_items_recycle);
        ((MainActivity) mainActivity).cartrecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false));
        ((MainActivity) mainActivity).cartAdapter = new cartAdapter(((MainActivity) mainActivity), ((MainActivity) mainActivity).jsonObjectscart);
        ((MainActivity) mainActivity).cartrecyclerView.setItemAnimator(new DefaultItemAnimator());
        ((MainActivity) mainActivity).cartrecyclerView.setAdapter(((MainActivity) mainActivity).cartAdapter);

        return view;
    }
}