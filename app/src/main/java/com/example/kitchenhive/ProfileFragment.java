package com.example.kitchenhive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected Activity mainActivity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(Activity activity) {
        // Required empty public constructor
        mainActivity = activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    Button btn_logout;
    TextView name_initial,txt_name,txt_email,txt_phone;
    ImageView chat_bot_btn;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

       btn_logout = view.findViewById(R.id.btn_logout);
       name_initial = view.findViewById(R.id.profile_name_inital);
       txt_name = view.findViewById(R.id.name_txt);
       txt_email = view.findViewById(R.id.email_txt);
       txt_phone = view.findViewById(R.id.phone_txt);
       chat_bot_btn = view.findViewById(R.id.chat_bot_btn);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(btn_logout);
                ((MainActivity) mainActivity).doLogout(mainActivity);
            }
        });

        chat_bot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((MainActivity) mainActivity), chatbot.class);
                startActivity(intent);
            }
        });

        txt_name.setText(((MainActivity) mainActivity).sharedPreferences.getString("UserName", ""));
        txt_email.setText(((MainActivity) mainActivity).sharedPreferences.getString("UserEmail", ""));
        txt_phone.setText("+91 "+((MainActivity) mainActivity).sharedPreferences.getString("UserPhone", ""));
        String name_initial_str = new Utility().getNameInitials(((MainActivity) mainActivity).sharedPreferences.getString("UserName", ""));
        name_initial.setText(name_initial_str);

        ((MainActivity) mainActivity).bind_cart_bottom(false, mainActivity);

       return view;
    }
}