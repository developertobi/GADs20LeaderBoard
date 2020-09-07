package com.e.gadsleaderboard.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.e.gadsleaderboard.adapters.LearningAdapter;
import com.e.gadsleaderboard.R;
import com.e.gadsleaderboard.model.VolleySingleton;
import com.e.gadsleaderboard.model.URL;
import com.e.gadsleaderboard.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LearningLeader extends Fragment {

JsonArrayRequest request;
List<User> userList;
Context context;
RecyclerView recyclerView;
    ProgressBar progressBar;
    public LearningLeader() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_leader, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        userList = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBar);
        apiCall();

        return view;

    }

    public void apiCall(){

        showProgressBar();
                request =
                new JsonArrayRequest(URL.URL_HOUR, new Response.Listener<JSONArray>() {
                    JSONObject object =null;
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i<response.length();i++){
                            try {
                                object = response.getJSONObject(i);
                                User user = new User();
                                user.setName(object.getString("name"));
                                user.setCountry(object.getString("country"));
                                user.setHour_skill(object.getString("hours"));
                                user.setImg_url(object.getString("badgeUrl"));
                                userList.add(user);
                                Log.d("LearningLeader", "onResponse: "+toString());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        setAdapter(userList);
                        hideProgressBar();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LearningLeader", "onErrorResponse: "+error.getMessage());
                        Log.d("LearningLeader", "onErrorResponse: "+URL.URL_HOUR);
                        hideProgressBar();
                    }
                });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    public void setAdapter(List<User> user){
        LearningAdapter leaderAdapter = new LearningAdapter(getActivity(),user);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       recyclerView.setAdapter(leaderAdapter);
    }

    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public  void hideProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }


}
