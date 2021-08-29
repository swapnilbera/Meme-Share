package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    ImageView meme;
    String meme_url;
    ProgressBar progressBar;
    Button next,share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meme=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progressBar3);
        next=findViewById(R.id.button2);
        share=findViewById(R.id.button);

        meme_genarator();
    }
    private void meme_genarator(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme?fbclid=IwAR26WJZDW7KUsOxbmAdbR28YPGe2DKf8rZ7tq5sp3F-7agYS9ebB9KVAicA";

// Request a string response from the provided URL.
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null , new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               // Display the first 500 characters of the response string.
               meme_url= null;
               progressBar.setVisibility(View.VISIBLE);
               try {
                   meme_url = response.getString("url");
               } catch (JSONException e) {
                   e.printStackTrace();
               }

               Glide.with(MainActivity.this).load(meme_url).listener(new RequestListener<Drawable>() {
                   @Override
                   public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       // log exception
                       progressBar.setVisibility(View.INVISIBLE);
                       return false; // important to return false so the error placeholder can be placed
                   }

                   @Override
                   public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                       progressBar.setVisibility(View.INVISIBLE);
                       return false;
                   }
               }).into(meme);

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
           }
       });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void next_meme(View view) {
        meme_genarator();
    }

    public void share_meme(View view) {
       Intent intent=new Intent(Intent.ACTION_SEND);
       intent.setType("text/plain");
       intent.putExtra(Intent.EXTRA_TEXT,meme_url);
        Intent chooser=Intent.createChooser(intent,"Share this meme");
        startActivity(chooser);


    }
}