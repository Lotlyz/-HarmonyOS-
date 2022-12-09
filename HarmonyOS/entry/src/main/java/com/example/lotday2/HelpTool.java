package com.example.lotday2;

import com.example.lotday2.bean.User;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.StoreConfig;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HelpTool {
    //轻量级数据库
    public  Preferences preferences;
    public  DatabaseHelper databaseHelper;
    private static final String dbname = "pdb";
    public String jsonStr;
    //服务器地址
    public String URL ="http://120.79.182.135:2211";
//    public String LOCALHOST_URL ="http://172.28.26.42:3099";
//    public String LOCALHOST_URL ="http://139.9.64.19:3099";
    public static User user;
//    public String LOCALHOST_URL ="http://192.168.1.105:3099";
    public String LOCALHOST_URL ="http://139.9.64.19:3099";
    //    http://192.168.1.105:3099/login?userPhone=18377451477&userPassword=123456
    public HelpTool(Context context) {
        //轻量级数据库初始化
        databaseHelper = new DatabaseHelper(context);
        preferences = databaseHelper.getPreferences(dbname);
        jsonStr = "";
    }
    //主页面Ability跳转
    public void PageJump(String pageName,Context context) {
        Intent intent =new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(context.getBundleName())
                .withAbilityName(pageName)
                .build();
        intent.setOperation(operation);
        context.startAbility(intent,0);
    }

    //检查是否登录
    public void checkLogin(Context context) {
        if(preferences.getString("state","").equals(""))
            PageJump("com.example.lotday2.LoginAbility",context);
    }

    //加载网络图片
    //使用前记得在配置文件加权限  "reqPermissions": [{"name": "ohos.permission.INTERNET"}],
    //添加ThreadPoolUtil工具类到你的项目
    public void LoadImage(String urlImage,Image imageHdp,Context context) {
        ThreadPoolUtil.submit(() -> {
            try {
                //通过openConnection来获取URLConnection
                URL url = new URL(urlImage);//api接口的url字符串
                URLConnection urlConnection = url.openConnection();

                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection connection = (HttpURLConnection)urlConnection;
                    //发送请求建立连接
                    connection.connect();
                    //从连接中获取输入流，读取网址返回的数据
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
                        ImageSource imageSource = ImageSource.create(connection.getInputStream(), srcOpts);
                        PixelMap pixelMap = imageSource.createPixelmap(null);
                        context.getUITaskDispatcher().syncDispatch(() ->imageHdp.setPixelMap(pixelMap));
                    }
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //加载网络json
    public void loadUrl(Context context,String para) {
        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            netManager.addDefaultNetStatusCallback(null);
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                URL url = new URL(URL+para);
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.connect();
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                    //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                context.getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = result;
                    }
                });

                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }


}
