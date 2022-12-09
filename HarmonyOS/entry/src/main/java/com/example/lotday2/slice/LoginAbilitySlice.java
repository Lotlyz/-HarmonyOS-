package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.User;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LoginAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    TextField tfLphone,tfLpassword;
    Button register,login,back,dislogin;
    ArrayList<User> list = new ArrayList<>();
    HelpTool ht;  //引入工具类
    public static ShapeElement themecolor  = new ShapeElement();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login);
        ht = new HelpTool(getApplicationContext());
        initComponents();
        initSystemUi();
        RgbColor[] rgbColors = new RgbColor[]{new RgbColor(255 ,105 ,180), new RgbColor(0, 191, 255)};
        themecolor.setRgbColors(rgbColors);
        themecolor.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
    }
    private void initSystemUi() {
        // 状态栏设置为透明
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        // 导航栏 ActionBar
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_NAVIGATION);
        // 全屏
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
    }
    private void initComponents() {

        DirectionalLayout directionalLayout = findComponentById(ResourceTable.Id_logindir);
        directionalLayout.setBackground(themecolor);
        tfLphone =findComponentById(ResourceTable.Id_login_phone);
        tfLpassword =findComponentById(ResourceTable.Id_login_password);
        login = findComponentById(ResourceTable.Id_login_login);
        dislogin =  findComponentById(ResourceTable.Id_mine_login);
        register = findComponentById(ResourceTable.Id_login_register);
        back = findComponentById(ResourceTable.Id_login_back);
        back.setClickedListener(this);
        register.setClickedListener(this);
        login.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {
        if (component == login) {
            String phone = tfLphone.getText();
            String password = tfLpassword.getText();


            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.LOCALHOST_URL+"/login?userPhone="+phone
                            +"&userPassword="+password
                    );

//                    URL url = new URL(ht.LOCALHOST_URL+"/login?userPhone="+phone
//                            +"&userPassword="+password
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
                    getUITaskDispatcher().asyncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                            if(Json.size()== 0){
                                new ToastDialog(LoginAbilitySlice.this)
                                        .setText("手机号或者密码错误")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                for(int i=0;i<Json.size();i++){
                                    JSONObject modeluser = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                    list.add
                                            (new User(Integer.parseInt(modeluser.get("userId").toString()),
                                                    modeluser.get("userName").toString(),
                                                    modeluser.get("userPhone").toString(),
                                                    modeluser.get("userPassword").toString(),
                                                    modeluser.get("userImage").toString(),
                                                    modeluser.get("userMotto").toString())
                                            );
                                    //创建轻量数据库设置登录状态
                                    ht.preferences.putInt("userId",Integer.parseInt(modeluser.get("userId").toString()));
                                    ht.preferences.putString("userName",modeluser.get("userName").toString());
                                    ht.preferences.putString("userPhone",modeluser.get("userPhone").toString());
                                    ht.preferences.putString("userPassword",modeluser.get("userPassword").toString());
                                    ht.preferences.putString("userImage",modeluser.get("userImage").toString());
                                    ht.preferences.putString("userMotto",modeluser.get("userMotto").toString());
                                    ht.preferences.putString("state","1");
                                    ht.preferences.flush();
                                }
                                new ToastDialog(LoginAbilitySlice.this)
                                        .setText("登录成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                tfLphone.setText("");
                                tfLpassword.setText("");
                                Intent i = new Intent();
                                Operation operation = new Intent.OperationBuilder()
                                        .withDeviceId("")
                                        .withBundleName("com.example.lotday2")
                                        .withAbilityName("com.example.lotday2.MainAbility")
                                        .build();
                                i.setOperation(operation);
                                startAbility(i);
                            }
                        }
                    });
                    HttpResponseCache.getInstalled().flush();
                } catch (IOException e) {
                }
            });
        }else if(component == register){
            Intent i = new Intent();
            present(new RegisterSlice(), i);
        }else if (component == back){
            Intent i = new Intent();
            present(new MineAbilitySlice(), i);
        }
    }
}