package com.example.mymoviememoir.networkconnection;

import android.util.Log;

import com.example.mymoviememoir.SignInActivity;
import com.example.mymoviememoir.entity.Credential;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {

    private OkHttpClient client=null;
    private String results;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    public NetworkConnection(){
        client=new OkHttpClient();
    }
    //http://192.168.157.1:43592/MyMovieMemoir/webresources/restmovie.credential/findByUsername/aa
    private static final String BASE_URL =
            "http://192.168.157.1:43592/MyMovieMemoir/webresources/";

    public String findByUsername(String username, String password){
        final String methodPath = "restmovie.credential/findByUsername/" + username;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        String passwordHash = "";
        String credentialID = "";
        JSONArray array = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
            array = new JSONArray(results);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(results.equals("[]"))
        {
            results = "Incorrect username";
        }
        else {
            JSONObject object = null;
            try {
                object = array.getJSONObject(0);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            try {
                passwordHash = object.getString("passwordhash");
                credentialID = object.getString("credentialid");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (passwordHash.equalsIgnoreCase(password)) {
                results = credentialID;
            } else {
                results = "Incorrect password";
            }
        }
        return results;
    }

    public String addPerson(String[] details) {
        Credential student = new Credential(Integer.parseInt(details[0]), details[1],details[2]);
        student.setCredentialid(Integer.parseInt(details[3]));
        Gson gson = new Gson();
        String studentJson = gson.toJson(student);
        String strResponse="";
//this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , studentJson);
        final String methodPath = "student.student/";
        RequestBody body = RequestBody.create(studentJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public String addCredential(String[] details) {
        Credential student = new Credential(Integer.parseInt(details[0]), details[1],details[2]);
        student.setCredentialid(Integer.parseInt(details[3]));
        Gson gson = new Gson();
        String studentJson = gson.toJson(student);
        String strResponse="";
//this is for testing, you can check how the json looks like in Logcat
        Log.i("json " , studentJson);
        final String methodPath = "student.student/";
        RequestBody body = RequestBody.create(studentJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }
}
