package com.example.dreamweaver_refactor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AIFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AIFragment newInstance(String param1, String param2) {
        AIFragment fragment = new AIFragment();
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

    public ArrayList<chat_componenets> messages;
    public recycler_view_adapter_chat adapter;
    public RecyclerView Chat_recycler;
    private final ArrayList<JSONObject> conversationHistory = new ArrayList<>();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_i, container, false);

        ImageView send_button = view.findViewById(R.id.imageView8);
        EditText edittext_chat = view.findViewById(R.id.editTextText);
        TextView textview =view.findViewById(R.id.textview4);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_input = edittext_chat.getText().toString().trim();
                Addtochat(user_input,chat_componenets.sent_from_user);
                edittext_chat.setText("");
                textview.setVisibility(View.GONE);
                callAPI(user_input);
            }
        });

        messages = new ArrayList<>();
        adapter = new recycler_view_adapter_chat(getActivity());
        adapter.setContacts(messages);
        Chat_recycler = view.findViewById(R.id.recyclerview2);
        Chat_recycler.setAdapter(adapter);
        Chat_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));




        return view;
    }
    void Addtochat(String message,String sent_by){
        getActivity().runOnUiThread(new Runnable() {  // we use runOnUIThread because it allows us to make changes to the UI (updating recyclerview) inside the thread
            @Override
            public void run() {
                if (getActivity() == null) return;
                messages.add(new chat_componenets(message,sent_by));
                adapter.notifyDataSetChanged();
                Chat_recycler.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        Addtochat(response,chat_componenets.sent_from_gpt);
    }

    void callAPI (String question){
        JSONObject jsonbody = new JSONObject();
        try{
            // conversation history: adds all messages back into the call before answering again
            conversationHistory.add(new JSONObject().put("role", "user").put("content", question));
            JSONArray messagesArray = new JSONArray();
            messagesArray.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
            for (JSONObject message : conversationHistory) {
                messagesArray.put(message);
            }


            jsonbody.put("model","gpt-3.5-turbo");
            jsonbody.put("messages", messagesArray);
            jsonbody.put("max_tokens", 1000);
            jsonbody.put("temperature", 0.7);
        }
        catch(JSONException e){
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(jsonbody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer ")// secret key found in https://platform.openai.com/settings/organization/api-keys
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("an issue has occurred " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonobject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonobject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        conversationHistory.add(new JSONObject().put("role", "assistant").put("content", result));
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("an issue has occurred" + response.body().string());
                }

            }
        });
    }}