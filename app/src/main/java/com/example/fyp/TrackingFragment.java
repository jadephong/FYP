package com.example.fyp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackingFragment extends Fragment {

    private ListView trackingContanxtList;
    private SessionManager sessionManager;
    private String currusername;
    private ArrayList<HashMap<String, Object>> friendList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracking_view, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sessionManager = new SessionManager();
        currusername = sessionManager.getUserName(getActivity()).toString();
        trackingContanxtList = (ListView) v.findViewById(R.id.contact_list);
        friendList = new ArrayList<HashMap<String, Object>>();
        getFriend(currusername);
        return v;
    }

    //get all friend of current user
    public void getFriend(final String param) {
        String urlSuffix = null;
        if (param.contains("@")) {
            urlSuffix = "?email=" + param;
        } else if (param.matches("\\d+")) {
            urlSuffix = "?phone_num=" + param;
        } else if (param.matches("[a-zA-Z]+")) {
            urlSuffix = "?username=" + param;
        }
        class GetFriend extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "", "Please Wait...", true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    if (s.equalsIgnoreCase("connect failed")) {
                        Toast.makeText(getActivity(), "Please Check Your Connection!", Toast.LENGTH_LONG).show();
                    } else {
                        showFriend(s);
                        loading.dismiss();
                    }
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String param = params[0];
                RequestHandler rh = new RequestHandler();
                if (Validator.checknetwork(getActivity()) != false) {
                    String s1 = rh.sendGetRequestParam("http://jstarcnavigator.esy.es/andriod_user_api/RetrieveFriend.php", param);
                    return s1;
                } else {
                    return "connect failed";
                }
            }
        }
        GetFriend getFriend = new GetFriend();
        getFriend.execute(urlSuffix);
    }

    //retrieve friend details
    public void showFriend(String json) {
        try {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(URLDecoder.decode(json, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObj = result.getJSONObject(i);
                String username = jsonObj.getString("username");
                String dob = jsonObj.getString("DOB");
                String gender = jsonObj.getString("gender");
                String phone_num = jsonObj.getString("phone_num");
                String email = jsonObj.getString("email");
                String status = jsonObj.getString("status");
                String gcm_registration_id = jsonObj.getString("gcm_registration_id");
                if (status.equals("A")) {
                    Bitmap b = getImage(jsonObj);
                    HashMap<String, Object> friend = new HashMap<String, Object>();
                    friend.put("username", username);
                    friend.put("dob", dob);
                    friend.put("gender", gender);
                    friend.put("phone_num", phone_num);
                    friend.put("email", email);
                    friend.put("gcm_registration_id", gcm_registration_id);
                    if (b != null) {
                        friend.put("image", b);
                    } else {
                        friend.put("image", R.drawable.userclear);
                    }
                    friendList.add(friend);
                }
            }

            String[] from = {"username", "image"};

            int[] to = {R.id.username, R.id.picture_listview};
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), friendList, R.layout.friend_list_item, from, to);
            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                        ImageView iv = (ImageView) view;
                        Bitmap bm = (Bitmap) data;
                        RoundImage roundedImage = new RoundImage(bm);
                        iv.setImageDrawable(roundedImage);
                        return true;
                    }
                    return false;

                }

            });
            trackingContanxtList.setAdapter(adapter);
            //here
            trackingContanxtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    trackingContanxtList = (ListView) arg0;
                    TextView friendName = (TextView) arg0.getChildAt(position - trackingContanxtList.getFirstVisiblePosition()).findViewById(R.id.username);
                    String reg_id = null;
                    for (int i = 0; i < friendList.size(); i++) {
                        if (friendName.getText().toString().equals(friendList.get(i).get("username").toString())) {
                            reg_id = friendList.get(i).get("gcm_registration_id").toString();
                        }
                    }
                    sendMessageToGCMAppServer(friendName.getText().toString(), reg_id, currusername, "track");

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //retrieve friend profile picture
    public Bitmap getImage(JSONObject jo) {
        URL url = null;
        Bitmap image = null;
        try {
            if (jo.getString("image").replace("\\", "") != null) {
                if (Patterns.WEB_URL.matcher(jo.getString("image").replace("\\", "")).matches() == true) {
                    url = new URL(jo.getString("image").replace("\\", ""));
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    //send tracking request, once the friend receive will go to GCMNotificationIntentService
    public void sendMessageToGCMAppServer(final String toUserName, final String regid, final String fromusername, final String type) {
        String urlSuffix = null;
        if (toUserName != null && regid != null) {
            urlSuffix = "?id=" + regid + "&frenname=" + toUserName + "&touser=" + fromusername + "&type=" + type;
        }
        class Gcm extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];

                String URL = "http://jstarcnavigator.esy.es/gcm/gcm_panel.php";
                BufferedReader bufferedReader = null;
                try {
                    if (Validator.checknetwork(getActivity()) != false) {
                        URL url = new URL(URL + s);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String result;
                        result = bufferedReader.readLine();
                        return result;
                    } else {
                        return "connect failed";
                    }
                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                Log.d("MainActivity", "Result: " + result);
                if (result.equals("not send")) {
                    Toast.makeText(getActivity(), "Tracking Request Not Sent", Toast.LENGTH_LONG)
                            .show();
                } else if (result.equalsIgnoreCase("connect failed")) {
                    Toast.makeText(getActivity(), "Please Check Your Connection!", Toast.LENGTH_LONG).show();
                }
            }
        }

        Gcm gcm = new Gcm();
        gcm.execute(urlSuffix);

    }
}


   
     
