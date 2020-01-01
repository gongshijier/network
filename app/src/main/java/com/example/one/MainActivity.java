package com.example.one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.one.GSON.One;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = findViewById(R.id.showdata);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });


    }

    private void getdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://v1.hitokoto.cn/")
                            .build();
                    Response response = client.newCall(request).execute();
                    String jsondata = response.body().string();
                    String words = "";
//                  由于服务器返回的是当个json我们可以使用JSONObject（传入数据）来构造一个json对象取值
//                        JSONObject jsonObject =  new JSONObject(jsondata);
//                    如果是多个json数据就使用JSONArray来获取
//                    JSONArray jsonArray = new JSONArray(jsondata);
                    Gson gson = new Gson();
//                    如果是多条json数据，使用List来构造返回一个List对象的javaBean
//                    List<One> oneList = gson.fromJson(jsondata,new TypeToken<List<One>>(){}.getType());
//                    for(One one:oneList){
//                        one.getHitokoto();
//                    }
                    One one = gson.fromJson(jsondata, One.class);
                    words = one.getHitokoto();
                    changeUI(words);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void changeUI(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                textview.setText(data);
            }
        }).start();

    }
}
