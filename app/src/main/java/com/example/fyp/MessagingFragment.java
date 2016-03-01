package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MessagingFragment extends Fragment {
    private ArrayList<HashMap<String, Object>> friendList;
    private ListView list;
    private String myJSON;
    private ImageView picture_listview;
    private String username;
    private ArrayList<String> friendusernameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messaging_view, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle b = getActivity().getIntent().getExtras();
        username = b.getString("username");
        list = (ListView) view.findViewById(R.id.contact_list);
        picture_listview = (ImageView) view.findViewById(R.id.picture_listview);
        friendList = new ArrayList<HashMap<String, Object>>();
        friendusernameList = new ArrayList<String>();
        final FloatingActionButton addFrenFab = (FloatingActionButton) view.findViewById(R.id.addFrenFab);
        final FloatingActionButton fabApprove = (FloatingActionButton) view.findViewById(R.id.fabApprove);
        final FloatingActionButton fabchat = (FloatingActionButton) view.findViewById(R.id.fabchat);
        addFrenFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriend.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putStringArrayList("friendusernameList", friendusernameList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        fabApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendRequest.class);
                startActivity(intent);
            }
        });
        fabchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatBubbleActivity.class);
                startActivity(intent);
            }
        });

        getFriend(username);
        return view;
    }

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
                    friendusernameList.add(username);
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
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    list = (ListView) arg0;
                    TextView friendName = (TextView) arg0.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.username);
                    ImageView friendImage = (ImageView) arg0.getChildAt(position - list.getFirstVisiblePosition()).findViewById(R.id.picture_listview);
                    String reg_id = null;
                    for (int i = 0; i < friendList.size(); i++) {
                        if (friendName.getText().toString().equals(friendList.get(i).get("username").toString())) {
                            reg_id = friendList.get(i).get("gcm_registration_id").toString();
                        }
                    }
                    friendImage.buildDrawingCache();
                    Bitmap image = friendImage.getDrawingCache();
                    Bundle extras = new Bundle();
                    extras.putParcelable("imagebitmap", image);
                    extras.putString("friendName", friendName.getText().toString());
                    extras.putString("reg_id", reg_id);
                    Intent intent = new Intent(getActivity(), ChatBubbleActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
}
