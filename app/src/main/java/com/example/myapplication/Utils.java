package com.example.myapplication;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    static StorageReference ref = null;
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference getStorageReference() {
        if (ref == null) ref = storage.getReference();
        return ref;
    }
    public static void showInstruction(View rootView, String msg) {
        // inflate the layout of the popup window
        Context ctx = rootView.getContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.game_pop_up, null);

        // Setting the pop up text
        TextView txt = popupView.findViewById(R.id.popupText);
        txt.setText(msg);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setElevation(20);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    static int lcs(String X, String Y, int m, int n)
    {
        int[][] L = new int[m + 1][n + 1];

        // Following steps build L[m+1][n+1] in bottom up
        // fashion. Note that L[i][j] contains length of LCS
        // of X[0..i-1] and Y[0..j-1]
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0)
                    L[i][j] = 0;
                else if (X.charAt(i - 1) == Y.charAt(j - 1))
                    L[i][j] = L[i - 1][j - 1] + 1;
                else
                    L[i][j] = max(L[i - 1][j], L[i][j - 1]);
            }
        }
        return L[m][n];
    }

    // Utility function to get max of 2 integers
    static int max(int a, int b) { return (a > b) ? a : b; }

    static String arrToString(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for(String s:arr) sb.append(s);
        return sb.toString();
    }

    static String arrToString(List<Integer> arr) {
        StringBuilder sb = new StringBuilder();
        for(int s:arr) sb.append(s);
        return sb.toString();
    }

    static void uploadImageToStorageAndScore(
            File file, Context ctx, String parentFolder, String testName, String apiUrl
    ) {
        StorageReference ref = getStorageReference();
        String phoneNumber = getPhoneNumber(ctx);
        try {
            InputStream stream = new FileInputStream(file);

            // since file name will be unique , using that
            String name = file.getName();
            StorageReference imageRef = ref.child(
                    String.format("%s/%s/%s", parentFolder, phoneNumber, name)
            );

            imageRef.putStream(stream).addOnFailureListener(
                    (u) -> {
                        Toast.makeText(ctx, "Error uploading the file", Toast.LENGTH_SHORT).show();
                    }
            ).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(
                    uri -> {
                        System.out.println("URL : "+uri.toString());
                        updateScore(apiUrl, uri.toString(), ctx, testName);
                    }
            ));
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, "Error handling the file", Toast.LENGTH_SHORT).show();
        }
    }

    static String getPhoneNumber(Context ctx) {
        return ctx.getSharedPreferences(
                ctx.getString(R.string.sharedPrefsName),
                Context.MODE_PRIVATE
        ).getString(
                ctx.getString(R.string.login),
                ""
        );
    }

    static void updateScore(String url, String imageUrl, Context ctx, String testName) {
        Map<String, String> params = new HashMap<>();
        params.put("image_url", imageUrl);

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params) ,response -> {
            System.out.println("SCORE "+testName+" "+response);
            ScoreMaintainer scoreMaintainer=  ScoreMaintainer.getInstance();
            String score = "0";
            try {
                score = (String) response.get("score");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            scoreMaintainer.updateScore(testName, Integer.parseInt(score));
            System.out.println(scoreMaintainer.getAllScores());
        }, error -> {
            Toast.makeText(ctx, "Error calculating the score for test "+testName, Toast.LENGTH_SHORT).show();
            System.out.println("Error on getting the score --------");
            System.out.println(error.getMessage());
            System.out.println(error);
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(
                4000 * 1000 * 60, // 4 min
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MySingleton.getInstance(ctx).addToRequestQueue(req);
    }
}
