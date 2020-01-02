# Android：OkHttp请求网络数据、JSON、GSON解析数据
简书地址：  
https://www.jianshu.com/p/5b1381ea27b3    
#### OkHttp请求服务器数据
添加权限  
``` 
<uses-permission android:name="android.permission.INTERNET"></uses-permission>  
```  
添加OkHttp、GSON依赖
 ```
implementation 'com.squareup.okhttp3:okhttp:3.4.1'   
implementation 'com.google.code.gson:gson:2.7'   
```  
##### URI和URL、URN
uri Uniform Resource Identifier  
url uniform resource locator  
urn Uniform Resource Name  
![三者关系](https://upload-images.jianshu.io/upload_images/19741117-8c292577c5fed81d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
理解uri关键在于id，url在于locator位置、urn在于name你可以通过在地球上确定一个人的方法来理解他们  
uri相当于你的id card number 可以唯一标识你  
urn相当于你的名字我们认为也是可以唯一标识你  
url相当于你的地理位置精确到你比如某某省某某市某街道多少号你的位置也可以唯一标识你  

##### okhttp使用方法：
**get方法**  

```
  OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://v1.hitokoto.cn/")
                            .build();
                    Response response = client.newCall(request).execute();
                    String jsondata = response.body().string();
```  
**post方法**  
通过在get方法的基础上增加一个requestBody  
然后增加连缀.post(requestBody)即可  
post可以使用把填写的表单的信息放在报文的body中进行传输  
get直接把用户填写的数据增加到uri里面用户也可以看到  

**安全性**
这样的get暴露毕竟是不安全的  
但是post也并不是安全的  
因为即使在bodu里面的数据，只要我们截取了数据包，还是可以提取body里面的数据  
解决安全问题不能简单通过get和post来解决  
于是诞生了https就是增加SSL、TLS来封装http  
利用公钥和私钥的技术来解决数据的加密传输的问题  
公钥私钥就是服务器和客户端使用客户端发给服务器的私钥来加密和封装数据   
即使其他用户截取了数据包不知道私钥也无法解析数据  
```
RequestBody requestBody = new FormBody.Builder()
                            .add("username","admin")
                            .add("password","admin")
                            .build();
 OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://v1.hitokoto.cn/")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String jsondata = response.body().string();
```  
服务器返回的是json格式的数据  
![json数据格式](https://upload-images.jianshu.io/upload_images/19741117-b9be8bdd3587b780.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

这样我们就获得了这样的json数据的字符串  
我们需要对这些字符串做处理  
可以使用json或者是GSON工具来处理  
GSON使用特别方便，只需要创建javabean然后就可以获取
***
#### GSON解析数据  
```
//                  若服务器返回的是当个json我们可以使用JSONObject（传入数据）来构造一个json对象取值
                    JSONObject jsonObject =  new JSONObject(jsondata);
//                    如果是多个json数据就使用JSONArray来获取
                    JSONArray jsonArray = new JSONArray(jsondata);
                    Gson gson = new Gson();
//                    如果是多条json数据，使用List来构造返回一个List对象的javaBean
                    List<One> oneList = gson.fromJson(jsondata,new TypeToken<List<One>>(){}.getType());
                    for(One one:oneList){
                        one.getHitokoto();
                    }
                    One one = gson.fromJson(jsondata, One.class);
```
对于少数的json数据你可以使用json来解析，但是对于大多数情况，使用GSON更加快捷和方便  
创建GSON对象，通过调用gson对象的fromJson（）方法  
**参数：json格式的字符串数据 + 需要返回的对象的类型**  
这样就可以返回一个List和一个对象来自于你创建的javabean  
通过调用这个javabean的方法你可以获取每个key对应的value  
***
#### 主要代码：
```
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
```
