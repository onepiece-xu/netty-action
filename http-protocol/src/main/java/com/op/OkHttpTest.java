package com.op;

import okhttp3.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/1513:16
 */
public class OkHttpTest {

    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES) // 设置连接超时时间
            .readTimeout(2, TimeUnit.MINUTES) // 设置读取超时时间
            .build();

    public static void doGet(String url, Map<String,String> headers, Map<String,String> params){
        Request.Builder reqBuild = new Request.Builder();
        if(headers != null && !headers.isEmpty()){
            for(Map.Entry<String, String> entry : headers.entrySet()){
                reqBuild.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if(params != null && !params.isEmpty()){
            HttpUrl.Builder urlBuilder =HttpUrl.parse(url)
                    .newBuilder();
            for(Map.Entry<String, String> entry : params.entrySet()){
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            reqBuild.url(urlBuilder.build());
        }else{
            reqBuild.url(url);
        }
        Request request = reqBuild.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            InputStream inputStream = response.body().byteStream();
            FileOutputStream fos = new FileOutputStream("E:\\BaiduNetdiskDownload\\test\\test\\1.mp4");
            byte[] bytes = new byte[1<<13];
            int sum = 0;
            int read;
            while ((read = inputStream.read(bytes)) > 0){
//                System.out.println(new String(bytes));
                System.out.println(sum +=read);
                fos.write(bytes);
            }
            fos.flush();
            System.out.println("传输完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        doGet("https://yzz.qk62.com/remote_control.php?time=1615785978&cv=b3d269bbd064fbcb5ef650b3b21d92c0&lr=0&cv2=ed4ad1bb598a69e14b67667e16d84234&file=%2Fvideos%2F6000%2F6261%2F6261.mp4&cv3=ce64e874ac9929ff70827e04c29d7a99&cv4=52ae4c0757566249612ebd1a2b27595b",null,null);
    }
}
