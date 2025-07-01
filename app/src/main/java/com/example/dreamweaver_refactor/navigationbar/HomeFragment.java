package com.example.dreamweaver_refactor.navigationbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.example.dreamweaver_refactor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    de.hdodenhof.circleimageview.CircleImageView Record_Button;
    private boolean isRecording = false; // Flag to track if recording was stopped manually
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // record button implementation //
        Record_Button = view.findViewById(R.id.circle_image);


        Record_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // If permission is not granted, request it.
                    // The result is handled by requestPermissionLauncher.
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                    return; // Exit here, will retry starting recording after permission is granted
                }

                // Toggle recording state based on current is_recording flag
                if (!isRecording) {
                    // If not recording, start it
                    startSpeechRecognition();
                } else {
                    // If recording, stop it
                    stopSpeechRecognition();
                }



                //Toast.makeText(getActivity(), "Button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        EditText textbox = view.findViewById(R.id.dream_input_text);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override public void onReadyForSpeech(Bundle bundle) {Toast.makeText(getActivity(), "Ready to listen", Toast.LENGTH_SHORT).show();}
            @Override public void onBeginningOfSpeech() {Toast.makeText(getActivity(), "Recording", Toast.LENGTH_SHORT).show();}
            @Override public void onRmsChanged(float v) {}
            @Override public void onBufferReceived(byte[] bytes) {}
            @Override
            public void onEndOfSpeech() {
                if (!isRecording) {// Restart only if user didn't stop manually
                    startSpeechRecognition();
                }
                else{}
            }
            @Override
            public void onError(int i) {
                //Toast.makeText(getActivity(), "Something went wrong "+ i, Toast.LENGTH_SHORT).show();
                if (!isRecording) {  // Restart only if user didn't stop manually
                    startSpeechRecognition();
                }
            }
            @Override
            public void onResults(Bundle bundle) {
                // Get the transcribed text
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String transcribedText = matches.get(0);
                    textbox.setText(textbox.getText() + " " +transcribedText);

                    //Toast.makeText(getActivity(), "Transcribed: " + transcribedText, Toast.LENGTH_LONG).show();
                }

            }
            @Override public void onPartialResults(Bundle bundle) {}
            @Override public void onEvent(int i, Bundle bundle) {}
        });

        Button save_Button = view.findViewById(R.id.save_button);
        save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSpeechRecognition();
                addToDreamDatabase(textbox.getText().toString());
                textbox.setText("");
            }
        });

        return view;
    }


    //helper functions
    private void startSpeechRecognition() {
        isRecording = true;  // Reset flag when starting new recording

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US"); // Set the language
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        // Start listening
        speechRecognizer.startListening(intent);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null; // Optional: Set to null to avoid memory leaks
        }
    }
    private void stopSpeechRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening(); // Stop the current listening session
            isRecording = false; // Mark that the user has stopped recording
            Toast.makeText(requireContext(), "Recording stopped.", Toast.LENGTH_SHORT).show();
            // TODO: Update button UI to indicate stopped state
        }
    }

    private void addToDreamDatabase(String description) {
        final HashMap<String, String> log = new HashMap<>();
        log.put("description", description);
        log.put("date", generateDate());

        // Step 1: Generate Tag
        callAPI(description, "generate a one word tag from this description examples include (scary, ethereal, calming, ...) use simple words nothing too convoluted: ", tag -> {
            log.put("tag", tag);

            // Step 2: Generate Title
            callAPI(description, "generate a less than 6 word title from this description (no quotation marks around the title just the title): ", title -> {
                log.put("title", title);

                // Step 3: Save to Firebase after both tag & title are received
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference Logref = database.getReference("Dream Journal");

                String key = Logref.push().getKey();
                log.put("key", key);

                Logref.child(key).setValue(log).addOnCompleteListener(task -> {
                    Toast.makeText(getActivity(), "Saved to DB", Toast.LENGTH_SHORT).show();
                });
            });
        });
    }


    private String generateDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(calendar.getTime());
        return date;}

    interface APICallback {
        void onResult(String response);
    }

    private void callAPI(String question, String prompt, APICallback callback) {
        JSONObject jsonbody = new JSONObject();
        try {
            jsonbody.put("model", "gpt-3.5-turbo");
            jsonbody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."))
                    .put(new JSONObject().put("role", "user").put("content", prompt + " " + question))
            );
            jsonbody.put("max_tokens", 200);
            jsonbody.put("temperature", 0.7);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonbody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer") // Keep this safe; never share it in public
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onResult("an issue has occurred " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray choices = jsonObject.getJSONArray("choices");
                        String result = choices.getJSONObject(0).getJSONObject("message").getString("content");
                        callback.onResult(result.trim());
                    } catch (JSONException e) {
                        callback.onResult("JSON parsing error: " + e.getMessage());
                    }
                } else {
                    callback.onResult("API Error: " + response.body().string());
                }
            }
        });
    }

}




