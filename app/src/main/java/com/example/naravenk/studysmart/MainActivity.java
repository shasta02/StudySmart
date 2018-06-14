package com.example.naravenk.studysmart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    JSONObject api;
    String target;
    String rootid;

    ArrayList<HashMap<String,Object>> exercises;
    ArrayList<HashMap<String,Object>> videos;
    ArrayList<HashMap<String,Object>> quizzes;
    ArrayList<HashMap<String,Object>> articles;
    ArrayList<String> blacklist;


    //container class to port json to
    /*
    private class KAT{
        String icon_src;
        String current_revision_key;
        Boolean importable;
        String relative_url;
        Boolean enable_fpm_mastery;
        Boolean has_topic_unit_test;
        String creation_date;
        String content_id;
        String web_url;
        String ka_url;
        String has_user_authored_content_types;
        String translated_title;
        String author_key;
        String gplus_url;


    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //stuff we added
    Runnable run1 = new Runnable (){
        public void run(){
            //networking logic
        }

    };




    JSONObject j = new JSONObject();
    rootid = "one-dimensional-motion"; //here we hardcode rootid to physics, after linking camera in it will be set by input
    target = rootid;

    blacklist = new ArrayList<String>();
    exercises = new ArrayList<HashMap<String,Object>>();
    videos = new ArrayList<HashMap<String,Object>>();
    quizzes = new ArrayList<HashMap<String,Object>>();
    articles = new ArrayList<HashMap<String,Object>>();

    //getJSON();


    //KAT root = new KAT<Map<
    //KAT root = gson.fromJson(api.toString(),KAT.class);




    }


    public static final String EXTRA_MESSAGE = "com.example.naravenk.studysmart2.MESSAGE";

    /** Called when the user taps the Send button */

    public void sendMessage(View view) {
        System.out.println("BOY");
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    /**
    public String studyGuide(){
        String out = "";
        int i = 0;
        EditText editText = (EditText) findViewById(R.id.editText2);
        int time = Integer.parseInt(editText.toString());
        while(time > 0){
            out += ""
        }
    }
    */


    public void parse(){
        Gson gson = new Gson();
        String apitext = api.toString();
        Map<String,Object> map = new HashMap<String,Object>();
        map = (Map<String,Object>) gson.fromJson(apitext, map.getClass());
        Map<String,Object> ChildDataMap = new HashMap<String,Object>();
        //ChildDataMap = (Map<String,Object>) ((ArrayList<Object>) map.get("child_data")); //map2 is the map of the children array
        //Map<String,Object> ChildMap = new HashMap<String,Object>();
        //ChildMap = (Map<String,Object>) ((ArrayList<Object>) map.get("children"));

        String id = "";
        int storei = 0;
        boolean blacklisted=false;
        boolean whitelisted = false;
        //loop through children until a non blacklisted child is found. if all children are blacklisted, add this id to the blacklist.
        for(int i = 0;i<((ArrayList<Object>) map.get("child_data")).size(); i++)
        {
            for(int j = 0; j<blacklist.size();j++) { //this loops through the blacklist, if it matches our element we set blacklist to true.

                if (((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id").equals(blacklist.get(j))) {

                    blacklisted = true;


                }
            }
            if(!blacklisted) //if the loop exhausts itself without finding a match, blacklisted will break out of the outer loop
            {
                id = (String)(((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id"));
                storei = i;

                whitelisted = true;
                break;
            }
            blacklisted = false; //reset blacklisted to false so it can be used again
        }
        //NOT DONE YET: Check if all children are blacklisted. if so, blacklist this element.

        if(whitelisted == false && blacklist.size()!=0)
        {
            blacklist.add(map.get("slug").toString());

            target = rootid;
            getJSON();
            return;
        }

        //Check if child is a topic or not
        //If it is a topic, make api call after setting target to child
        if( (((Map<String, Object>) ((ArrayList<Object>) map.get("child_data")).get(storei)).get("kind").toString()).equals("Topic") )
        {
            target = (((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(storei)).get("id").toString());
            getJSON();
            return;
        }
        else
        {
            //If child is a video or exercize, loop through array of children and add them to the appropriate list.
            for(int i = 0; i < ((ArrayList<Object>) map.get("children")).size(); i++)
            {

                if( ((Map<String, Object>) ((ArrayList<Object>) map.get("child_data")).get(i)).get("kind").equals("Video") )
                {
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    temp.put("name", ((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id") );
                    temp.put("time", ((Map<String, Object>)map.get("children")).get("duration"));
                    System.out.println("video: "+((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id")+"/");
                    videos.add(temp);
                }
                if( ((Map<String, Object>) ((ArrayList<Object>) map.get("child_data")).get(i)).get("kind").equals("Article") )
                {
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    temp.put("name", ((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id") );
                    temp.put("time", timeToComplete() );
                    System.out.println("exercize: "+((Map<String, Object>) ((ArrayList<Object>) map.get("children")).get(i)).get("id")+"/");
                    articles.add(temp);
                }
            }
            blacklist.add(map.get("slug").toString());

            target = rootid;

            getJSON();
            return;
        }






    }

    public int timeToComplete(){
        return 300;
    }




    /*
    public void findContent(Map<String, Object> map){
        Map<String, Object> originalMap = new HashMap<String, Object>(map);



        String id;
        int index = 0;

        ArrayList<Object> children = (ArrayList<Object>)map.get("child_data");
        Map<String, Object> child = (Map<String, Object>)children.get(index);

        while(true){
            id = (String)child.get("id");
            if (child.get("kind").equals("Topic") && blacklisted.indexOf(id) < 0){
                index = 0;
                target = id;
            }
            else if (!child.get("kind").equals("Topic")){
                for (int i = 0; i < children.size(); i++){
                    child = (Map<String, Object>)children.get(i);
                    if (child.get("kind").equals("Video")){
                        videos.add(id);
                    }
                    else{
                        exercises.add(id);
                    }
                    blacklisted.add((String)child.get("id"));
                }
            }
            else if (index >= children.size()){

            }
            else if (blacklisted.indexOf(id) >= 0){
                index++;
            }
            child = (Map<String, Object>)children.get(index);
        }


    }
    */

    public void getJSON(){
        new AsyncJson().execute();
    }


    private class AsyncJson extends AsyncTask<Void, Void, Void>
    {
        JSONObject j;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... params) {
            String url = "https://www.khanacademy.org/api/v1/topic/" + target;
            HttpClient c = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            String total = null;
            try {
                HttpResponse r = c.execute(get);
                InputStream is = r.getEntity().getContent();
                total = "";
                BufferedReader b = new BufferedReader(new InputStreamReader(is));
                String data = b.readLine();
                while (data != null) {
                    total += data + "\n";
                    data = b.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            j = null;
            try {
                j = new JSONObject(total);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            /*
            try {
                System.out.println(j.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            api = j;
            parse();
        }

    }



}