package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Time;
import com.example.lotday2.bean.Timetype;
import com.example.lotday2.provider.TimeItemProvider;
import com.example.lotday2.provider.TimetypeItemProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
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
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeAddSlice extends AbilitySlice implements Component.ClickedListener {
    public String starttime,endtime;

    public String timeline,duration="",dateStr;
    boolean hasBorrow = false;
    private final ArrayList<Timetype> list = new ArrayList<>();

//    //创建数据库配置对象（参数：数据库名）
//    private StoreConfig timeconfig = StoreConfig.newDefaultConfig("Time.db");
//
//    private static RdbOpenCallback TimeCallback = new RdbOpenCallback() {
//        @Override
//        public void onCreate(RdbStore store) {
//            store.executeSql("create table if not exists yz_time (id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "time_timeline varchar(100),time_type varchar(100), time_duration varchar(100))");
//        }
//        @Override
//        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
//        }
//    };
    int shours,sminutess,sseconds,ehoure,eminutese,eseconde;
    TimePicker timeadd_start,timeadd_end;
    TextField tftimeremarks,timedate;
    Button timesave;
    String typename;
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_time_add);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initListContainer();
        initComponents();
    }
    private void initListContainer() {
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer timetypelistContainer = findComponentById(ResourceTable.Id_timeaddtypelistcontainer);
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        TimetypeItemProvider timetypeItemProvider = new TimetypeItemProvider(list, this);
        timetypelistContainer.setItemProvider(timetypeItemProvider);

        timetypelistContainer.setItemClickedListener((container, component, position, id) -> {
            Timetype timetypeitem = (Timetype) timetypelistContainer.getItemProvider().getItem(position);
            typename = timetypeitem.getTimetypeName();
        });


    }

    @Override
    protected void onActive() {
        super.onActive();
        list.clear(); //先清空
        gettimetypeData(); //加载数据库数据
    }
    private void initComponents() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy年-MM月-dd日");
        dateStr = sdf.format(date);
        timedate = findComponentById(ResourceTable.Id_timedate);
        timedate.setText(dateStr);
        Calendar c = Calendar.getInstance();
        shours = c.get(Calendar.HOUR_OF_DAY);
        ehoure = c.get(Calendar.HOUR_OF_DAY);
        sminutess = c.get(Calendar.MINUTE);
        eminutese = c.get(Calendar.MINUTE);
        sseconds = c.get(Calendar.SECOND);
        eseconde = c.get(Calendar.SECOND);
        starttime = shours+":"+sminutess+":"+sseconds;
        endtime = ehoure+":"+eminutese+":"+eseconde;
        timeadd_start = (TimePicker) findComponentById(ResourceTable.Id_time_add_start);
        timeadd_start.showSecond(false);
        timeadd_start.setTimeChangedListener(new TimePicker.TimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hours, int minutess, int seconds) {
                shours = hours;
                sminutess = minutess;
                sseconds = seconds;
                starttime = hours+":"+minutess;
            }
        });

        timeadd_end = (TimePicker) findComponentById(ResourceTable.Id_time_add_end);
        timeadd_end.showSecond(false);
        timeadd_end.setTimeChangedListener(new TimePicker.TimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int houre, int minutese, int seconde) {
                ehoure = houre;
                eminutese = minutese;
                eseconde = seconde;
                endtime = houre+":"+minutese;
            }
        });
        tftimeremarks = findComponentById(ResourceTable.Id_timeadd_remarks);
        timesave = (Button) findComponentById(ResourceTable.Id_timeadd_save);
        timesave.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {
        if (component == timesave) {
            String remarks = tftimeremarks.getText().toString();
            String timedatetf = timedate.getText();
            Time time = new Time();
            timeline = starttime+"~"+endtime;
            int minuteDiff = eminutese - sminutess;
            if (hasBorrow) {
                minuteDiff = minuteDiff - 1;
                hasBorrow = false;
            }
            if (minuteDiff != 0) {
                if (minuteDiff < 0) {
                    hasBorrow = true;
                    minuteDiff = 60 + minuteDiff;
                }
                duration += minuteDiff + "分";
            }

            int hourDiff = ehoure - shours;
            if (hasBorrow) {
                hourDiff = hourDiff - 1;
                hasBorrow = false;
            }
            if (hourDiff != 0) {
                if (hourDiff < 0) {
                    hasBorrow = true;
                    hourDiff = 24 + hourDiff;
                }
            }
            duration = hourDiff + "时" + minuteDiff + "分";

            if (hasBorrow) {
                hasBorrow = false;
                new ToastDialog(TimeAddSlice.this)
                        .setText("您输入的时间不合理")
                        .setAlignment(LayoutAlignment.CENTER)
                        .show();

            } else {
//                time.setTime_timeline(timeline);
//                time.setTime_type(type);
//                time.setTime_duration(duration);

                int time_user_id = 1;
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        URL url = new URL(ht.URL+"/Time/Timeadd.php?time_timeline="+timeline+"&time_type="+typename
                                +"&time_duration="+duration+"&time_remarks="+remarks+"&time_date="+timedatetf+"&time_user_id="+ht.preferences.getInt("user_id",0)
                        );
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
                                if(Json.size()== 0){
                                    new ToastDialog(TimeAddSlice.this)
                                            .setText("添加失败")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else
                                if (Json.size()>0){
                                    new ToastDialog(TimeAddSlice.this)
                                            .setText("添加成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                    Intent i = new Intent();
                                    Operation operation = new Intent.OperationBuilder()
                                            .withDeviceId("")
                                            .withBundleName("com.example.lotday2")
                                            .withAbilityName("com.example.lotday2.TimeAbility")
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

    public void gettimetypeData() {

        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                URL url = new URL(ht.URL+"/TimeType/Timetype.php");
                URL url = new URL(ht.LOCALHOST_URL+"/TimeType/timeTypeAll");
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
                                        (new Timetype(
//                                                Integer.parseInt(model.get("timetype_id").toString()),
//                                                model.get("timetype_name").toString(),
//                                                Integer.parseInt(model.get("timetype_user_id").toString())

                                                Integer.parseInt(model.get("timetypeId").toString()),
                                                model.get("timetypeName").toString(),
                                                Integer.parseInt(model.get("timetypeUserId").toString())
                                        ));
                            }
                        }
                        initListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
            }
        });

    }
}
