package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.ShadowDrawable;
import com.example.lotday2.bean.Diary;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.provider.MoodItemProvider;
import com.example.lotday2.provider.MoodlnnItemProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.PopupDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.StoreConfig;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    Button Home, Diary, Time, Mine;
    Image Moodstation;
    DependentLayout moodtop;
    HelpTool ht;  //引入工具类
    PopupDialog moodlnndialog,moodlnndialog2;
    public  static int cangsatate  = 1;
    public  static int cangsatate2  = 1;
    //存储数据的列表
    private final ArrayList<Mood> moodlnnlist = new ArrayList<>();
    private final ArrayList<Mood> list = new ArrayList<>();
//    MoodDao moodDao;
    CommonDialog mooddialog,moodlnnpreviewdialog;
    int mdeleteid;
    final StoreConfig moodconfig = StoreConfig.newDefaultConfig("Mood.db");
    private static final RdbOpenCallback MoodCallback = Mood.getMoodCallback();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        ht = new HelpTool(getApplicationContext());
        ht.checkLogin(getContext());
//        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initSystemUi();
        initComponents();
    }
    private void initSystemUi() {
        // 状态栏设置为透明
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        // 导航栏 ActionBar
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_NAVIGATION);
        // 全屏
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);


    }
    @Override
    protected void onActive() {
        super.onActive();
        list.clear(); //先清空
        getMoodData(); //加载数据库数据
        moodlnnlist.clear(); //先清空
        getMoodlnnData(); //加载数据库数据
    }

    private void initListContainer() {
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer moodlistContainer = findComponentById(ResourceTable.Id_moodlistcontainer);
//        List<Mood> mooddata = getMoodData();
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        MoodItemProvider moodItemProvider = new MoodItemProvider(list,this);
        moodlistContainer.setItemProvider(moodItemProvider);

        //动态单击事件
        moodlistContainer.setItemClickedListener((container, component, position, id) -> {
            Mood mooditem = (Mood) moodlistContainer.getItemProvider().getItem(position);

        });
        //动态列表长按事件
        moodlistContainer.setItemLongClickedListener((container, component, position, id) -> {
            Mood mooditem = (Mood) moodlistContainer.getItemProvider().getItem(position);
            mdeleteid = mooditem.getMoodId();
//            Mood mood = moodDao.queryMoodById(mid);
//            moodid = mood.getMood_id();
            //内容对话框
            mooddialog = new CommonDialog(getContext());
            mooddialog.setTitleText("提示");
            mooddialog.setContentText("确定删除该动态？");
            mooddialog.setSize(500,400);
            mooddialog.setCornerRadius(36.0f);
            mooddialog.setButton(IDialog.BUTTON1, "取消", (iDialog, i) -> iDialog.destroy());
            mooddialog.setButton(IDialog.BUTTON2, "确认", (iDialog, i) -> {
//                int deletemoodid = moodDao.deleteMoodById(mdeleteid);
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
//                        URL url = new URL(ht.URL+"/mooddelete.php?mood_id="+mdeleteid);
                        URL url = new URL(ht.LOCALHOST_URL+"/Mood/moodDelete?moodId="+mdeleteid);
                        URLConnection urlConnection = netHandle.openConnection(url, Proxy.NO_PROXY);
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
                                    new ToastDialog(MainAbilitySlice.this)
                                            .setText("删除成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else if (Json.size()>0){
                                    new ToastDialog(MainAbilitySlice.this)
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
                mooddialog.destroy();
                Intent intent1 = new Intent();
                present(new MainAbilitySlice(),intent1);
            });
            mooddialog.show();
            return false;
        });
    }

    private void initComponents() {
        Home = findComponentById(ResourceTable.Id_main_home);
        Diary = findComponentById(ResourceTable.Id_main_diary);
        Time = findComponentById(ResourceTable.Id_main_time);
        Mine = findComponentById(ResourceTable.Id_main_mine);
        Moodstation = findComponentById(ResourceTable.Id_mood_moodstation);
//        ListContainer moodlistconainer = findComponentById(ResourceTable.Id_moodlistcontainer);
//        moodlistconainer.setBackground(new MineAbilitySlice().cardcolor);
        DirectionalLayout bottom = findComponentById(ResourceTable.Id_ability_bottom_dir_m);
        bottom.setBackground(new MineAbilitySlice().themecolor);
        moodtop = findComponentById(ResourceTable.Id_moodtoptheme);
        moodtop.setBackground(new MineAbilitySlice().themecolor);

        Home.setClickedListener(this);
        Diary.setClickedListener(this);
        Time.setClickedListener(this);
        Mine.setClickedListener(this);
        Moodstation.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {

        if (component == Home) {

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MainAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        } else if (component == Diary) {

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.DiaryAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        } else if (component == Time) {

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.TimeAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        } else if (component == Mine) {

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MineAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        } else if (component == Moodstation) {
            Moodlnn();
        }
    }

    private void Moodlnn() {
        //气泡框中加入xml布局
        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_moodlnn, null, false);
        moodlnndialog = new PopupDialog(this, layout, DirectionalLayout.LayoutConfig.MATCH_PARENT,
                DirectionalLayout.LayoutConfig.MATCH_PARENT);
        Button back = layout.findComponentById(ResourceTable.Id_moodlnn_back);
        back.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                moodlnndialog.destroy();
            }
        });
        ListContainer moodlnnlistContainer = layout.findComponentById(ResourceTable.Id_moodlnnlistcontainer);
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        MoodlnnItemProvider moodlnnItemProvider = new MoodlnnItemProvider(moodlnnlist,this);
        moodlnnlistContainer.setItemProvider(moodlnnItemProvider);
        //动态单击事件
        moodlnnlistContainer.setItemClickedListener((container, component2, position, id) -> {
//                moodlnndialog.destroy();
            Mood moodpreviewitem = (Mood) moodlnnlistContainer.getItemProvider().getItem(position);

            DirectionalLayout layout2 = (DirectionalLayout) LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_moodlnn_preview_item, null, false);

            Image moodadd_image = layout2.findComponentById(ResourceTable.Id_moodpreview_image);
            ht.LoadImage(moodpreviewitem.getMoodImage(),moodadd_image,layout2.getContext());
//                moodadd_image.setText(parameter.getStringParam("mood_image"));
            Text moodadd_sheying = layout2.findComponentById(ResourceTable.Id_moodpreview_sheying);
            moodadd_sheying.setText(moodpreviewitem.getMoodSheying());
            Text moodadd_sheyingzhe = layout2.findComponentById(ResourceTable.Id_moodpreview_sheyingshi);
            moodadd_sheyingzhe.setText(moodpreviewitem.getMoodSheyingzhe());
            Text moodadd_mood = layout2.findComponentById(ResourceTable.Id_moodpreview_mood);
            moodadd_mood.setText(moodpreviewitem.getMoodMood());
            Text moodadd_date = layout2.findComponentById(ResourceTable.Id_moodpreview_date);
            moodadd_date.setText(moodpreviewitem.getMoodDate());
            Text moodadd_address = layout2.findComponentById(ResourceTable.Id_moodpreview_address);
            moodadd_address.setText(moodpreviewitem.getMoodAddress());
            Text moodadd_author =layout2.findComponentById(ResourceTable.Id_moodpreview_author);
            moodadd_author.setText(moodpreviewitem.getMoodAuthor());

            Image cang = layout2.findComponentById(ResourceTable.Id_preview_moodshoucang);
            Image zan = layout2.findComponentById(ResourceTable.Id_preview_mooddianzan);
            Image fen = layout2.findComponentById(ResourceTable.Id_preview_moodfenxiang);
            cang.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    if (cangsatate==1) {
                        cang.setPixelMap(ResourceTable.Media_moodcang2);
                        cangsatate = 2;
                    }else if(cangsatate == 2){
                        cang.setPixelMap(ResourceTable.Media_moodcang);
                        cangsatate = 1;
                    }
                }
            });
            zan.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    if (cangsatate2==1) {
                        zan.setPixelMap(ResourceTable.Media_moodzan2);
                        cangsatate2 = 2;
                    }else if(cangsatate2 == 2){
                        zan.setPixelMap(ResourceTable.Media_moodzan);
                        cangsatate2 = 1;
                    }
                }
            });
            fen.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    new ToastDialog(layout2.getContext())
                            .setText("已发送")
                            .setAlignment(LayoutAlignment.CENTER)
                            .show();
                }
            });
            moodlnnpreviewdialog = new CommonDialog(layout2.getContext());
            moodlnnpreviewdialog.setAutoClosable(true);
            moodlnnpreviewdialog.setSize(960, 1775);
            moodlnnpreviewdialog.setCornerRadius(30);
            moodlnnpreviewdialog.setContentCustomComponent(layout2);
            moodlnnpreviewdialog.show();

        });
        moodlnndialog.setCustomComponent(layout);
        moodlnndialog.setBackColor(Color.TRANSPARENT);
        moodlnndialog.setAutoClosable(true);
        moodlnndialog.showOnCertainPosition(LayoutAlignment.CENTER, 0, 0);
        moodlnndialog.show();

    }

    //获取数据
    private void getMoodData() {

        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }
        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
//                URL url = new URL(ht.URL+"/UMood.php?mood_user_id="+ht.preferences.getInt("user_id",0));
                URL url = new URL(ht.LOCALHOST_URL+"/Mood/moodUser?moodUserId="+ht.preferences.getInt("userId",0));
                URLConnection urlConnection = netHandle.openConnection(url, Proxy.NO_PROXY);
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
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                list.add
                                        (new Mood(Integer.parseInt(model.get("moodId").toString()),
                                        model.get("moodImage").toString(),
                                        model.get("moodSheying").toString(),
                                        model.get("moodSheyingzhe").toString(),
                                        model.get("moodMood").toString(),
                                        model.get("moodDate").toString(),
                                        model.get("moodAddress").toString(),
                                        model.get("moodAuthor").toString(),
                                        Integer.parseInt(model.get("moodUserId").toString())
                                        ));
                            }
                        }
                        initListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });

    }

    private void getMoodlnnData() {

        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }
        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection2 = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                URL url = new URL(ht.URL+"/mood.php");
                URL url = new URL(ht.LOCALHOST_URL+"/Mood/moodAll");
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection2 = (HttpURLConnection) urlConnection;
                }
                connection2.setRequestMethod("GET");
                connection2.connect();
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
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                moodlnnlist.add
                                        (new Mood(Integer.parseInt(model.get("moodId").toString()),
                                                model.get("moodImage").toString(),
                                                model.get("moodSheying").toString(),
                                                model.get("moodSheyingzhe").toString(),
                                                model.get("moodMood").toString(),
                                                model.get("moodDate").toString(),
                                                model.get("moodAddress").toString(),
                                                model.get("moodAuthor").toString(),
                                                Integer.parseInt(model.get("moodUserId").toString())
                                        ));
                            }
                        }
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
            }
        });

    }
}