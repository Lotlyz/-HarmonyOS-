package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
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

public class MoodAddSlice extends AbilitySlice implements Component.ClickedListener {
    Button save,imagepikerbtn;
    TextField moodadd_image,moodadd_sheying,moodadd_sheyingzhe,moodadd_mood,moodadd_date,
            moodadd_address,moodadd_author;
//    MoodDao moodDao;
    private StoreConfig moodconfig = StoreConfig.newDefaultConfig("Mood.db");
    private static RdbOpenCallback MoodCallback = Mood.getMoodCallback();
    CommonDialog pikerDialog;
    HelpTool ht;  //引入工具类
    String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker1.jpg";
    String b = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagpiker5.jpg";
    String c = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker4.jpg";
    String d = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker3.jpg";
    String e = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/iamgepiker6.jpg";
    String f = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepike2.jpg";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_moodadd);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initComponents();
    }

    private void initComponents() {
        moodadd_image = (TextField) findComponentById(ResourceTable.Id_moodadd_image);
        moodadd_sheying = (TextField) findComponentById(ResourceTable.Id_moodadd_sheying);
        moodadd_sheying.setText("摄影 | ");
        moodadd_sheyingzhe = (TextField) findComponentById(ResourceTable.Id_moodadd_sheyingzhe);
        moodadd_sheyingzhe.setText("Lotlyz");
        moodadd_mood = (TextField) findComponentById(ResourceTable.Id_moodadd_mood);
        moodadd_mood.setText("lim我=你↔♥即使给我整个世界，我也只在你的身边♥");
        moodadd_date = (TextField) findComponentById(ResourceTable.Id_moodadd_date);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        String dateStr = sdf.format(date);
        moodadd_date.setText(dateStr);
        moodadd_address = (TextField) findComponentById(ResourceTable.Id_moodadd_address);
        moodadd_address.setText("南宁");
        moodadd_author = (TextField) findComponentById(ResourceTable.Id_moodadd_author);
        moodadd_author.setText("Lotlyz");
        save = (Button) findComponentById(ResourceTable.Id_moodadd_sava);
        save.setClickedListener(this);
        imagepikerbtn = findComponentById(ResourceTable.Id_moodimagepiker);

        imagepikerbtn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imagepiker();
            }
        });

    }

    private void imagepiker() {


        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_image_datepiker, null, false);
        Image piker1 = layout.findComponentById(ResourceTable.Id_imagepiker1);
        Image piker2 = layout.findComponentById(ResourceTable.Id_imagepiker2);
        Image piker3 = layout.findComponentById(ResourceTable.Id_imagepiker3);
        Image piker4 = layout.findComponentById(ResourceTable.Id_imagepiker4);
        Image piker5 = layout.findComponentById(ResourceTable.Id_imagepiker5);
        Image piker6 = layout.findComponentById(ResourceTable.Id_imagepiker6);
        ht.LoadImage(a,piker1,layout.getContext());
        ht.LoadImage(b,piker2,layout.getContext());
        ht.LoadImage(c,piker3,layout.getContext());
        ht.LoadImage(d,piker4,layout.getContext());
        ht.LoadImage(e,piker5,layout.getContext());
        ht.LoadImage(f,piker6,layout.getContext());
        piker1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(a);
                pikerDialog.destroy();
            }
        });
        piker2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(b);
                pikerDialog.destroy();
            }
        });
        piker3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(c);
                pikerDialog.destroy();
            }
        });
        piker4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(d);
                pikerDialog.destroy();
            }
        });
        piker5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(e);
                pikerDialog.destroy();
            }
        });
        piker6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodadd_image.setText("");
                moodadd_image.setText(f);
                pikerDialog.destroy();
            }
        });


        pikerDialog = new CommonDialog(layout.getContext());
        pikerDialog.setAutoClosable(true);
        pikerDialog.setSize(960, 1450);
        pikerDialog.setCornerRadius(30);
        pikerDialog.setContentCustomComponent(layout);
        pikerDialog.show();



    }

    @Override
    public void onClick(Component component) {

        if (component == save) {

            String image = moodadd_image.getText().toString();

            String sheying = moodadd_sheying.getText().toString();
            String sheyingzhe = moodadd_sheyingzhe.getText().toString();
            String mooddata = moodadd_mood.getText().toString();
            String date = moodadd_date.getText().toString();
            String address = moodadd_address.getText().toString();
            String author = moodadd_author.getText().toString();
            Mood mood = new Mood();

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
                    URL url = new URL(ht.URL+"/Mood/moodAdd?moodImage="+image+"&moodSheying="+sheying
                    +"&moodSheyingzhe="+sheyingzhe+"&moodMood="+mooddata+"&moodDate="+date+"&moodAddress="+address
                            +"&moodAuthor="+author+"&moodUserId="+ht.preferences.getInt("userId",0)
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
                                new ToastDialog(MoodAddSlice.this)
                                        .setText("添加失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                                if (Json.size()>0){
                                new ToastDialog(MoodAddSlice.this)
                                        .setText("添加成功")
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
                        }
                    });
                    HttpResponseCache.getInstalled().flush();
                } catch (IOException e) {
                }
            });

        }
        }
}
