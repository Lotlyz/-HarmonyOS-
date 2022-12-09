package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Time;
import com.example.lotday2.provider.TimeItemProvider;
import com.example.lotday2.provider.TimePageSliderProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
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
import java.util.ArrayList;
import java.util.List;

public class TimeAbilitySlice extends AbilitySlice implements Component.ClickedListener {

    Button Home,Diary,Time,Mine;
    CommonDialog timedialog;
    int timeid;
    PageSlider pageSlider;
    public PageSlider getPageSlider() {
        return pageSlider;
    }
    HelpTool ht;  //引入工具类
    //存储数据的列表
    private final ArrayList<Time> list = new ArrayList<>();
    //创建数据库配置对象（参数：数据库名）
    private final StoreConfig timeconfig = StoreConfig.newDefaultConfig("Time.db");

    private static final RdbOpenCallback TimeCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_time (time_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "time_timeline varchar(100),time_type varchar(100), time_duration varchar(100))");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_time);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initComponents();
        initSystemUi();
        //初始化Tab
        TabList tabList = findComponentById(ResourceTable.Id_Time_tab_list);
        String[] tablistTags = { "时间"};
        for (int i=0;i<tablistTags.length;i++){
            TabList.Tab tab = tabList.new Tab(this);
            tab.setText(tablistTags[i]);
            tabList.addTab(tab);
        }
        //初始化PageSlider
        //1、创建适配器
        //2、初始化PageSlider
        List<Integer> layoutFieIds = new ArrayList<>();
        layoutFieIds.add(ResourceTable.Layout_ability_time_gailan);
//        layoutFieIds.add(ResourceTable.Layout_ability_time_tongji);
        pageSlider = findComponentById(ResourceTable.Id_Time_page_slider);
        pageSlider.setProvider(new TimePageSliderProvider(layoutFieIds,this));
        //3、Tablist与Pageslider联动
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                //获取点击菜单的索引
                int index = tab.getPosition();
                //设置pageslider的索引与菜单索引一致
                pageSlider.setCurrentPage(index);
                if(index == 0){
                    //概览
                    inittimeG(pageSlider);

                }else if (index == 1){
                    //统计
                    inittimeT(pageSlider);
                }
            }

            @Override
            public void onUnselected(TabList.Tab tab) { }

            @Override
            public void onReselected(TabList.Tab tab) { }
        });
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {

                //参数i就是表单当前pageSlidedr的索引
                if(tabList.getSelectedTabIndex() != i){
                    tabList.selectTabAt(i);
                }
            }
        });

        //4、tablist默认选中第二个菜单，加载PageSlider的第一个页面（默认）
        tabList.selectTabAt(0);

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
        gettimeData(); //加载数据库数据
    }
    private void inittimeT(PageSlider pageSlider) {
    }
    public void inittimeG(PageSlider pageSlider) {
        initListContainer();
        }
    private void initListContainer() {
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer timelistContainer = findComponentById(ResourceTable.Id_timeG_listcontainer);
//        List<Time> timedata = gettimeData();
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        TimeItemProvider timeItemProvider = new TimeItemProvider(list, this);
        timelistContainer.setItemProvider(timeItemProvider);
        //响应Item点击事件
//        timelistContainer.setItemClickedListener((container, component, position, id) -> {
//            Time timeitem = (Time) timelistContainer.getItemProvider().getItem(position);
////            Time time = timeDao.queryTimeById(timeitem.getTime_id());
//
//            Intent intent2 = new Intent();
//            intent2.setParam("time_id", String.valueOf(timeitem.getTime_id()));
//            intent2.setParam("time_timeline", timeitem.getTime_timeline());
//            intent2.setParam("time_duration", timeitem.getTime_duration());
//            intent2.setParam("time_type", timeitem.getTime_type());
//            presentForResult(new TimeUpdataSlice(), intent2, 0);
//
//
//
//        });
        //响应Item长按事件
//        timelistContainer.setItemLongClickedListener((container, component, position, id) -> {
//            Time timeitem = (Time) timelistContainer.getItemProvider().getItem(position);
//            timeid = timeitem.getTime_id();
//
//            //对话框实验
//            timedialog = new CommonDialog(getContext());
//            timedialog.setTitleText("更多操作");
////            dialog.setContentText("删除或修改");
//            timedialog.setSize(500,400);
//            timedialog.setCornerRadius(36.0f);
//            timedialog.setButton(IDialog.BUTTON1, "修改", (iDialog, i) -> iDialog.destroy());
//            timedialog.setButton(IDialog.BUTTON2, "删除", (iDialog, i) -> CommonDiadetele());
//            timedialog.setButton(IDialog.BUTTON3, "关闭", (iDialog, i) -> iDialog.destroy());
//            timedialog.show();
//            return false;
//        });
        Image timeaddsave = findComponentById(ResourceTable.Id_timeG_add);
        timeaddsave.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent i = new Intent();
                present(new TimeAddSlice(), i);
            }
        });
    }
//    private void CommonDiadetele() {
//        NetManager netManager = NetManager.getInstance(null);
//        if (!netManager.hasDefaultNet()) {
//            return;
//        }
//        ThreadPoolUtil.submit(() -> {
//            NetHandle netHandle = netManager.getDefaultNet();
//            HttpURLConnection connection = null;
//            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                URL url = new URL(ht.URL+"/Time/Timedelete.php?time_id="+timeid);
//                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
//                if (urlConnection instanceof HttpURLConnection) {
//                    connection = (HttpURLConnection) urlConnection;
//                }
//                connection.setRequestMethod("GET");
//                connection.connect();
//                try (InputStream inputStream = urlConnection.getInputStream()) {
//                    byte[] cache = new byte[2 * 1024];
//                    int len = inputStream.read(cache);
//                    while (len != -1) {
//                        outputStream.write(cache, 0, len);
//                        len = inputStream.read(cache);
//                    }
//                } catch (IOException e) {
//                    //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
//                }
//                String result = new String(outputStream.toByteArray());
//                getUITaskDispatcher().asyncDispatch(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
//                        if(Json.size()== 0){
//                            new ToastDialog(TimeAbilitySlice.this)
//                                    .setText("删除成功")
//                                    .setAlignment(LayoutAlignment.CENTER)
//                                    .show();
//                        }else if (Json.size()>0){
//                            new ToastDialog(TimeAbilitySlice.this)
//                                    .setText("删除失败")
//                                    .setAlignment(LayoutAlignment.CENTER)
//                                    .show();
//                        }
////                                initListContainer();  //数据适配控件
//                    }
//                });
//                HttpResponseCache.getInstalled().flush();
//            } catch (IOException e) {
//                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
//            }
//        });
//        timedialog.destroy();
//        onActive();
//        inittimeG(pageSlider);
//    }
    public void gettimeData() {

    NetManager netManager = NetManager.getInstance(null);
    if (!netManager.hasDefaultNet()) {
        return;
    }

    ThreadPoolUtil.submit(() -> {
        NetHandle netHandle = netManager.getDefaultNet();
        HttpURLConnection connection = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
            URL url = new URL(ht.LOCALHOST_URL+"/Time/selectByTimeUserId?timeUserId="+ht.preferences.getInt("userId",0));
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
                    if(Json.size()>0){
                        for(int i=0;i<Json.size();i++){
                            JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                            list.add
                                    (new Time(Integer.parseInt(model.get("timeId").toString()),
                                            model.get("timeTimeline").toString(),
                                            model.get("timeType").toString(),
                                            model.get("timeDuration").toString(),
                                            model.get("timeRemarks").toString(),
                                            model.get("timeDate").toString(),
                                            Integer.parseInt(model.get("timeUserId").toString())
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

    private void initComponents() {
        StackLayout stackLayout = findComponentById(ResourceTable.Id_time_stackLayout);
//        stackLayout.setBackground(new SettingSlice().bg2);
        Home = findComponentById(ResourceTable.Id_time_home);
        Diary = findComponentById(ResourceTable.Id_time_diary);
        Time = findComponentById(ResourceTable.Id_time_time);
        Mine = findComponentById(ResourceTable.Id_time_mine);
        DependentLayout toptimede = findComponentById(ResourceTable.Id_toptimede);
        toptimede.setBackground(new MineAbilitySlice().themecolor);
        TabList timetablist = findComponentById(ResourceTable.Id_Time_tab_list);
        timetablist.setBackground(new MineAbilitySlice().themecolor);
        DirectionalLayout bottom = findComponentById(ResourceTable.Id_ability_bottom_dir_t);
        bottom.setBackground(new MineAbilitySlice().themecolor);
        Home.setClickedListener(this);
        Diary.setClickedListener(this);
        Time.setClickedListener(this);
        Mine.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {

        if (component == Home){

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MainAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        }else if (component == Diary){

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.DiaryAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        }else if( component == Time){

            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.TimeAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);

        }else if(component == Mine){

            Intent i = new Intent();

            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MineAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }

    }
}
