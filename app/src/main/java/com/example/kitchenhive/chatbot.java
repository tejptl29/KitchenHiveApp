package com.example.kitchenhive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatbot extends BaseActivity {
//    EditText userInput;
//    Button sendButton;
//    TextView chatbotResponse;
//    private API chatService;
    RecyclerView faq_ques_rcv,faq_ans_rcv;
    ImageView btn_back,btn_send;
    EditText input_ques;
    ArrayList<JSONObject> faqjsonObjects = new ArrayList<>();
    ArrayList<JSONObject> ansjsonObjects = new ArrayList<>();

    faqAdapter faqAdapter;
    ansAdapter ansAdapter;

    public Handler handler = new Handler();
    public int dotCount = 0;
    public int charIndex = 0;
    public boolean isTypingStarted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        btn_back = findViewById(R.id.back_btn);
        input_ques = findViewById(R.id.chatbot_msg_writer);
        btn_send = findViewById(R.id.send_btn);
        faq_ques_rcv = findViewById(R.id.chatbot_faq_qus_rcv);
        faq_ques_rcv.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL));
        faqAdapter = new faqAdapter(chatbot.this, faqjsonObjects);
        faq_ques_rcv.setItemAnimator(new DefaultItemAnimator());
        faq_ques_rcv.setAdapter(faqAdapter);
        get_chatbot_faq_api_call();

        JSONObject default_message1 = new JSONObject();
        JSONObject default_message2 = new JSONObject();
        try {
            default_message1.put("faq_qus", "___default");
            default_message1.put("faq_ans", "Hi, How can i assist you?");
            //default_message2.put("faq_qus", "___default");
            //default_message2.put("faq_ans", "How can i help you?");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ansjsonObjects.add(default_message1);
        //ansjsonObjects.add(default_message2);

        faq_ans_rcv = findViewById(R.id.chatbot_ai_user_rcv);
        faq_ans_rcv.setLayoutManager(new LinearLayoutManager(chatbot.this, LinearLayoutManager.VERTICAL, false));
        ansAdapter = new ansAdapter(chatbot.this, ansjsonObjects);
        faq_ans_rcv.setItemAnimator(new DefaultItemAnimator());
        faq_ans_rcv.setAdapter(ansAdapter);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_ques.getText().toString();
                APIClass apiClass = new APIClass();
                apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                    @Override
                    public void onSuccess(String message, JSONObject json) {
                        System.out.println(json);
                        try {
                            if (new Utility().checkJSONDataNotNull(json, "data")) {
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                ansjsonObjects.add(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ansAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        // faqjsonObjects.clear();
                        //faqAdapter.notifyDataSetChanged();
                    }
                });
                String UserID = sharedPreferences.getString("UserID", "");
                apiClass.get_chatbot_ques(UserID,input_ques.getText().toString());
                input_ques.setText("");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void get_chatbot_faq_api_call() {
        APIClass apiClass = new APIClass();
        apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
            @Override
            public void onSuccess(String message, JSONObject json) {
                faqjsonObjects.clear();
                try {
                    if (new Utility().checkJSONDataNotNull(json, "questions")) {
                        JSONArray jsonArray = new JSONArray(json.getString("questions"));
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                faqjsonObjects.add((JSONObject) jsonArray.get(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                faqAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {
                faqjsonObjects.clear();
                faqAdapter.notifyDataSetChanged();
            }
        });
        String UserID = sharedPreferences.getString("UserID", "");
        apiClass.faq_ques_ans(UserID);
    }

    public void startLoadingAnimation(TextView textView, String ans) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String baseText = "Loading";
                dotCount = (dotCount + 1) % 4; // Cycle through 0, 1, 2, 3 dots
                String dots = new String(new char[dotCount]).replace("\0", ".");
                textView.setText(baseText + dots);

                if (!isTypingStarted) {
                    handler.postDelayed(this, 500); // Keep loading until typing starts
                } else {
                    //textView.setVisibility(View.GONE);
                }
            }
        }, 500);

        // Start typing effect after 3 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.VISIBLE);
                startTypingEffect(textView, ans);
                isTypingStarted = true;
            }
        },2000);
    }

    // Typing effect: character by character
    public void startTypingEffect(TextView textView, String ans) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < ans.length()) {
                    // Add one character at a time
                    textView.setText(ans.substring(0, charIndex + 1));
                    charIndex++;

                    scrollToBottom();
                    // Delay between each character
                    handler.postDelayed(this, 10); // 50ms for fast typing like a chatbot
                }
            }
        },10);
    }

    public void scrollToBottom() {
        faq_ans_rcv.post(new Runnable() {
            @Override
            public void run() {
                faq_ans_rcv.smoothScrollToPosition(ansAdapter.getItemCount() - 1);
            }
        });
    }
}

class faqAdapter extends RecyclerView.Adapter<faqViewHolder> {

    Activity activity;
    ArrayList<JSONObject> cart = new ArrayList<>();

    faqAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.cart = product;
    }

    @NonNull
    public faqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbot_faq_layout, parent,false);
        return new faqViewHolder(view, viewType, activity);
    }

    public void onBindViewHolder(final faqViewHolder holder, final int position) {

        try {
            JSONObject faq = new JSONObject(String.valueOf(cart.get(position)));

            holder.txt_ques.setText(faq.getString("faq_qus"));

            holder.txt_ques.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APIClass apiClass = new APIClass();
                    apiClass.setOnJSONDataListener(new APIClass.JsonDataInterface() {
                        @Override
                        public void onSuccess(String message, JSONObject json) {
                            System.out.println(json);
                            try {
                                if (new Utility().checkJSONDataNotNull(json, "data")) {
                                    JSONObject jsonObject = new JSONObject(json.getString("data"));
                                    ((chatbot) activity).ansjsonObjects.add(jsonObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ((chatbot) activity).ansAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String message) {
                            // faqjsonObjects.clear();
                            //faqAdapter.notifyDataSetChanged();
                        }
                    });
                    String UserID = ((chatbot)activity).sharedPreferences.getString("UserID", "");
                    try {
                        apiClass.get_faq_ans(UserID,faq.getString("id"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public int getItemCount() {
        return cart.size();
    }
}

class faqViewHolder extends RecyclerView.ViewHolder {

    TextView txt_ques;
    ConstraintLayout constraintLayout;


    faqViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        constraintLayout = itemView.findViewById(R.id.faq_constrain);
        txt_ques = itemView.findViewById(R.id.faq_ques);
    }
}

class ansAdapter extends RecyclerView.Adapter<ansViewHolder> {

    Activity activity;
    ArrayList<JSONObject> cart = new ArrayList<>();

    ansAdapter(Activity activity, ArrayList<JSONObject> product)
    {
        this.activity = activity;
        this.cart = product;
    }

    @NonNull
    public ansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items_layout, parent,false);
        return new ansViewHolder(view, viewType, activity);
    }

    public void onBindViewHolder(final ansViewHolder holder, final int position) {

        try {
            JSONObject ans = new JSONObject(String.valueOf(cart.get(position)));

            if(ans.getString("faq_qus").equals("___default")){
                holder.right_chat_view.setVisibility(View.GONE);
            }
            else {
                holder.right_chat_view.setVisibility(View.VISIBLE);
                holder.txt_ques.setText(ans.getString("faq_qus"));
            }

            ((chatbot) activity).scrollToBottom();

            holder.txt_ques.setText(ans.getString("faq_qus"));

//            holder.txt_answ.setText(ans.getString("faq_ans"));

            if((position + 1) == cart.size()){
                ((chatbot) activity).dotCount = 0;
                ((chatbot) activity).charIndex = 0;
                ((chatbot) activity).isTypingStarted=false;
                ((chatbot) activity).startLoadingAnimation(holder.txt_answ, ans.getString("faq_ans"));
            }
            else{
                holder.txt_answ.setText(ans.getString("faq_ans"));
            }

            //((chatbot) activity).startLoadingAnimation(holder.txt_answ, ans.getString("faq_ans"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public int getItemCount() {
        return cart.size();
    }
}

class ansViewHolder extends RecyclerView.ViewHolder {

    TextView txt_ques,txt_answ;
    ConstraintLayout relativeLayout, right_chat_view;


    ansViewHolder(View itemView, int viewType, Activity context) {
        super(itemView);
        relativeLayout = itemView.findViewById(R.id.constrain_ans);
        txt_ques = itemView.findViewById(R.id.right_chat_text_view);
        txt_answ = itemView.findViewById(R.id.left_chat_text_view);
        right_chat_view = itemView.findViewById(R.id.right_chat_view);
    }
}

