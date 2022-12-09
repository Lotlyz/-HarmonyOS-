package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.DatabaseHelper;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.StoreConfig;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoodUpdataSlice extends AbilitySlice implements Component.ClickedListener {
    Button moodudatasave;
    TextField moodadd_image,moodadd_sheying,moodadd_sheyingzhe,moodadd_mood,moodadd_date,
            moodadd_address,moodadd_author;
//    MoodDao moodDao;
    Intent parameter;
    HelpTool ht;  //引入工具类
    int updatamood_id;
    private StoreConfig moodconfig = StoreConfig.newDefaultConfig("Mood.db");
    private static RdbOpenCallback MoodCallback = Mood.getMoodCallback();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_mood_updata);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        parameter = intent;
        initComponents();
    }

    private void initComponents() {
        moodadd_image = (TextField) findComponentById(ResourceTable.Id_moodupdata_image);
        moodadd_image.setText(parameter.getStringParam("moodImage"));
        moodadd_sheying = (TextField) findComponentById(ResourceTable.Id_moodupdata_sheying);
        moodadd_sheying.setText(parameter.getStringParam("moodSheying"));
        moodadd_sheyingzhe = (TextField) findComponentById(ResourceTable.Id_moodupdata_sheyingzhe);
        moodadd_sheyingzhe.setText(parameter.getStringParam("moodSheyingzhe"));

        moodadd_mood = (TextField) findComponentById(ResourceTable.Id_moodupdata_mood);
        moodadd_mood.setText(parameter.getStringParam("moodMood"));

        moodadd_date = (TextField) findComponentById(ResourceTable.Id_moodupdata_date);
        moodadd_date.setText(parameter.getStringParam("moodDate"));
        moodadd_address = (TextField) findComponentById(ResourceTable.Id_moodupdata_address);
        moodadd_address.setText(parameter.getStringParam("moodAddress"));
        moodadd_author = (TextField) findComponentById(ResourceTable.Id_moodupdata_author);
        moodadd_author.setText(parameter.getStringParam("moodAuthor"));
        moodudatasave = (Button) findComponentById(ResourceTable.Id_moodupdata_sava);
        moodudatasave.setClickedListener(this);

    }

    @Override
    public void onClick(Component component) {

        if (component == moodudatasave) {

            String image = moodadd_image.getText().toString();
            String sheying = moodadd_sheying.getText().toString();
            String sheyingzhe = moodadd_sheyingzhe.getText().toString();
            String mooddata = moodadd_mood.getText().toString();
            String date = moodadd_date.getText().toString();
            String address = moodadd_address.getText().toString();
            String author = moodadd_author.getText().toString();
            Mood mood = new Mood();

            updatamood_id = Integer.parseInt(parameter.getStringParam("moodId"));
//            mood.setMood_id(Integer.parseInt(parameter.getStringParam("mood_id")));
//
//            mood.setMood_image(image);
//            mood.setMood_sheying(sheying);
//            mood.setMood_sheyingzhe(sheyingzhe);
//            mood.setMood_mood(mooddata);
//            mood.setMood_date(date);
//            mood.setMood_address(address);
//            mood.setMood_author(author);


            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.URL+"/moodUpdata?moodImage="+image+"&moodSheying="+sheying
                            +"&moodSheyingzhe="+sheyingzhe+"&moodMood="+mooddata+"&moodDate="+date+"&moodAddress="+address
                            +"&moodAuthor="+author+"&moodId="+updatamood_id+"&moodUserId="+ht.preferences.getInt("userId",0)
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
                                new ToastDialog(MoodUpdataSlice.this)
                                        .setText("修改失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(MoodUpdataSlice.this)
                                        .setText("修改成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                Intent i = new Intent();
                                Operation operation = new Intent.OperationBuilder()
                                        .withDeviceId("")
                                        .withBundleName("com.example.lotday2")
                                        .withAbilityName("com.example.lotday2.MainAbility")
                                        .build();
                                i.setOperation(operation);
                                startAbility(i);
                            }
//                                initListContainer();  //数据适配控件
                        }
                    });
                    HttpResponseCache.getInstalled().flush();
                } catch (IOException e) {
                    //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                }
            });
        }
        }
}
