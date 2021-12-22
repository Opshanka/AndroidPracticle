package com.appmobio.androidpractical;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appmobio.androidpractical.model.Item;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LoginButton loginButton;
    CallbackManager callbackManager;
    TextView txtUsername, txtEmail;
    String status;
    JSONArray jsonArray;
    SessionManager sessionManager;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing variables
        loginButton = findViewById(R.id.login_button);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        lv = findViewById(R.id.item_list);

        // If the access token is available already assign it.
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;


        if (!loggedOut) {
            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());
            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        sessionManager=new SessionManager(this);


    }
    private void checkSession() {
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if(!loggedOut){
        if (sessionManager.getLogin()){
            txtUsername.setVisibility(View.VISIBLE);
            txtUsername.setVisibility(View.VISIBLE);
            txtUsername.setText( sessionManager.get_first_name());
            txtEmail.setText( sessionManager.get_email());
        }
        else{
            txtUsername.setVisibility(View.GONE);
            txtUsername.setVisibility(View.GONE);
        }
        }
        else{
            txtUsername.setVisibility(View.GONE);
            txtUsername.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //use volley library to call api
        String userItems  =Url.getItems() ;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, userItems, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setItems(response);
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        );
        queue.add(request);

        checkSession();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {

                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            txtUsername.setText(first_name + " " + last_name);
                            txtEmail.setText(email);

                            sessionManager.setLogin(true);
                            sessionManager.set_first_name("Name:" + first_name + " " + last_name);
                            sessionManager.set_email(email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            sessionManager.setLogin(false);
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void setItems(JSONObject response) {
        try {
            status=response.get("status").toString();
            jsonArray = response.getJSONArray("data");

        }catch (JSONException exception){
            exception.printStackTrace();
        }
        List<HashMap<String, String>> list = new ArrayList<>();
        List<Item> listItems = new ArrayList<>();
        for(int i = 0; i<jsonArray.length(); i++){
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                 HashMap<String,String> map = new HashMap<String, String>();
                map.put("item_id",obj.getString("id"));
                map.put("latitude",obj.getString("latitude"));
                map.put("longitude",obj.getString("longitude"));
                map.put("title",obj.getString("title"));
                map.put("address",obj.getString("address"));
                JSONObject objImg = obj.getJSONObject("image");
                map.put("img",objImg.getString("small"));
                Item item = new Item(
                        obj.getString("id"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.getString("address"),
                        obj.getString("postcode"),
                        obj.getString("phoneNumber"),
                        obj.getString("latitude"),
                        obj.getString("longitude"),
                        objImg.getString("small"),
                        objImg.getString("medium"),
                        objImg.getString("large")
                );
                listItems.add(item);
                list.add(map);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //1. Layout FIle
        int layout = R.layout.account_list_layout;

        //2 View List
        int[] views = {R.id.item_id,R.id.title, R.id.item_address,R.id.item_image};

        //3. Column List
        String[] cols = {"item_id","title", "address","img"};

        //4. Create Simple Adapter
        final SimpleAdapter adapter = new SimpleAdapter(this,list,layout,cols,views);

        lv.setAdapter(adapter);

        //5. Added event listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                loadingDetails(listItems.get(position));
            }
        });
    }

    private void loadingDetails(Item item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_DATA,item);
        startActivity(intent);
    }


}