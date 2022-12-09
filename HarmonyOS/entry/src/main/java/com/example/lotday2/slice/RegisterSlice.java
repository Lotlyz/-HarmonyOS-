package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
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

public class RegisterSlice extends AbilitySlice implements Component.ClickedListener {

    Button back,register;
    TextField tfname,tfphone,tfpassword;
    HelpTool ht;  //引入工具类
    public static ShapeElement themecolor  = new ShapeElement();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_register);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        back = findComponentById(ResourceTable.Id_register_back);
        back.setClickedListener(listener -> present(new LoginAbilitySlice(), new Intent()));
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

        DirectionalLayout directionalLayout = findComponentById(ResourceTable.Id_registerdir);
        directionalLayout.setBackground(themecolor);
        tfname = findComponentById(ResourceTable.Id_register_name);
        tfphone = findComponentById(ResourceTable.Id_register_phone);
        tfpassword = findComponentById(ResourceTable.Id_register_password);

        back = findComponentById(ResourceTable.Id_register_back);
        register = findComponentById(ResourceTable.Id_register_register);
        back.setClickedListener(this);
        register.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {

        if (component == register) {
            String name = tfname.getText();
            String phone = tfphone.getText();
            String password = tfpassword.getText();
            String image = "https://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/tx.jpg";
            String motto = "白驹过隙，忽然而已。Lotday，记录你的每一天。";

            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                    URL url = new URL(ht.URL+"/User/useradd.php?user_name="+name+"&user_phone="+phone
//                            +"&user_password="+password+"&user_image="+image+"&user_motto="+motto
//                    );
                    URL url = new URL(ht.LOCALHOST_URL+"/userAdd?userName="+name+"&userPhone="+phone
                            +"&userPassword="+password+"&userImage="+image+"&userMotto="+motto
                    );
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
                                new ToastDialog(RegisterSlice.this)
                                        .setText("注册失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(RegisterSlice.this)
                                        .setText("注册成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                tfname.setText("");
                                tfphone.setText("");
                                tfpassword.setText("");
                                Intent i = new Intent();
                                present(new LoginAbilitySlice(), i);
                            }
                        }
                    });
                    HttpResponseCache.getInstalled().flush();
                } catch (IOException e) {
                }
            });
        }else if (component == back) {
            Intent i = new Intent();
            present(new LoginAbilitySlice(), i);
        }
    }
}
