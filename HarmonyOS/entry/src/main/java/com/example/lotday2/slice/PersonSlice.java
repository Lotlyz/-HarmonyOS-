package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.User;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PersonSlice extends AbilitySlice implements Component.ClickedListener {
    Button updata,delete;
    TextField tfname,tfphone,tfpassword;
    Intent parameter;
    Image image;
    CommonDialog deteledialog;
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_person);
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        parameter = intent;
        ht = new HelpTool(getApplicationContext());
        initComponents();
    }

    private void initComponents() {
//        image = (Image) findComponentById(ResourceTable.Id_person_touxiang);
//        ht.LoadImage(ht.preferences.getString("user_image",""),image,getContext());
//        tfname = (TextField) findComponentById(ResourceTable.Id_person_name);
//        tfname.setText(parameter.getStringParam("username"));
//        tfphone = (TextField) findComponentById(ResourceTable.Id_person_phone);
//        tfphone.setText(parameter.getStringParam("userphone"));
//        tfpassword = (TextField) findComponentById(ResourceTable.Id_person_password);
//        tfpassword.setText(parameter.getStringParam("userpassword"));
//        updata = (Button) findComponentById(ResourceTable.Id_person_updata);
//        delete = (Button) findComponentById(ResourceTable.Id_person_detele);
//        updata.setClickedListener(this);
//        delete.setClickedListener(this);
        image = (Image) findComponentById(ResourceTable.Id_person_touxiang);
        ht.LoadImage(ht.preferences.getString("userImage",""),image,getContext());
        tfname = (TextField) findComponentById(ResourceTable.Id_person_name);
        tfname.setText(ht.preferences.getString("userName",""));
        tfphone = (TextField) findComponentById(ResourceTable.Id_person_phone);
        tfphone.setText(ht.preferences.getString("userPhone",""));
        tfpassword = (TextField) findComponentById(ResourceTable.Id_person_password);
        tfpassword.setText(ht.preferences.getString("userPassword",""));
        updata = (Button) findComponentById(ResourceTable.Id_person_updata);
        delete = (Button) findComponentById(ResourceTable.Id_person_detele);
        updata.setClickedListener(this);
        delete.setClickedListener(this);
    }
    @Override
    public void onActive() {
        super.onActive();
    }
    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    @Override
    public void onClick(Component component) {
        if (component == updata){
            String name = tfname.getText().toString();
            String phone = tfphone.getText().toString();
            String password = tfpassword.getText().toString();
//            User user = new User();
//            user.setUser_id(Integer.parseInt((parameter.getStringParam("userid"))));
//            user.setUser_name(name);
//            user.setUser_phone(phone);
//            user.setUser_password(password);


            String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.LOCALHOST_URL+"/userUpdate?userId="+ht.preferences.getInt("userId", 0)+"&userName="+name
                            +"&userPhone="+phone+"&userPassword="+password+"&userImage="+a+"&userMotto"+ht.preferences.getString("userMotto","")
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
                    }
                    String result = new String(outputStream.toByteArray());
                    getUITaskDispatcher().asyncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                            if(Json.size()== 0){
                                new ToastDialog(PersonSlice.this)
                                        .setText("修改失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(PersonSlice.this)
                                        .setText("修改成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                tfname.setText("");
                                tfphone.setText("");
                                tfpassword.setText("");
                                ht.preferences.putString("userName",name);
                                ht.preferences.putString("userPhone",phone);
                                ht.preferences.putString("userPassword",password);
                                ht.preferences.putString("userImage",a);
                                terminate();
                            }
//                                initListContainer();  //数据适配控件
                        }
                    });
                    HttpResponseCache.getInstalled().flush();
                } catch (IOException e) {
                    //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                }
            });
        }else if(component == delete){
            deteledialog = new CommonDialog(getContext());
            deteledialog.setTitleText("提示");
            deteledialog.setContentText("确定注销这个账户？");
            deteledialog.setSize(500,400);
            deteledialog.setCornerRadius(36.0f);
            deteledialog.setButton(IDialog.BUTTON1, "关闭", (iDialog, i) -> iDialog.destroy());
            deteledialog.setButton(IDialog.BUTTON2, "确认", (iDialog, i) -> {

                CommonDialog deteledialog2 = new CommonDialog(getContext());
                deteledialog2.setTitleText("提示");
                deteledialog2.setContentText("再考虑一下吧");
                deteledialog2.setSize(500,400);
                deteledialog2.setCornerRadius(36.0f);
                deteledialog2.setButton(IDialog.BUTTON1, "关闭", (iDialog2, i2) -> iDialog.destroy());
                deteledialog2.setButton(IDialog.BUTTON2, "确认", (iDialog2, i2) -> {


                    NetManager netManager = NetManager.getInstance(null);
                    if (!netManager.hasDefaultNet()) {
                        return;
                    }
                    ThreadPoolUtil.submit(() -> {
                        NetHandle netHandle = netManager.getDefaultNet();
                        HttpURLConnection connection = null;
                        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                            String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
                            URL url = new URL(ht.LOCALHOST_URL+"/userDelete?userId="+ht.preferences.getInt("userId",0));
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
                            }
                            String result = new String(outputStream.toByteArray());
                            getUITaskDispatcher().asyncDispatch(new Runnable() {
                                @Override
                                public void run() {
                                    JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                                    if(Json.size()== 0){
                                        deteledialog.destroy();
                                        new ToastDialog(PersonSlice.this)
                                                .setText("删除成功")
                                                .setAlignment(LayoutAlignment.CENTER)
                                                .show();
                                        ht.preferences.putString("state","0");
                                        ht.preferences.flush();
                                        Intent i1 = new Intent();
                                        Operation operation = new Intent.OperationBuilder()
                                                .withDeviceId("")
                                                .withBundleName("com.example.lotday2")
                                                .withAbilityName("com.example.lotday2.MineAbility")
                                                .build();
                                        i1.setOperation(operation);
                                        startAbility(i1);

                                    }else if (Json.size()>0){
                                        new ToastDialog(PersonSlice.this)
                                                .setText("删除失败")
                                                .setAlignment(LayoutAlignment.CENTER)
                                                .show();
                                    }
                                }
                            });
                            HttpResponseCache.getInstalled().flush();
                        } catch (IOException e) {
                        }
                    });
                });
                deteledialog2.show();
            });
            deteledialog.show();
        }
    }
}
