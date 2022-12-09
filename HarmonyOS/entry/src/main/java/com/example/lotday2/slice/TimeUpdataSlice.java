package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Time;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.components.TimePicker;
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
import java.util.Calendar;
import java.util.Date;

public class TimeUpdataSlice extends AbilitySlice implements Component.ClickedListener {
    public String starttime,endtime;

    public String timeline,duration="",dateStr;
    boolean hasBorrow = false;

    //创建数据库配置对象（参数：数据库名）
    private final StoreConfig timeconfig = StoreConfig.newDefaultConfig("Time.db");
    private static final RdbOpenCallback TimeCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_time (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "time_timeline varchar(100),time_type varchar(100), time_duration varchar(100))");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };
    int shours,sminutess,sseconds,ehoure,eminutese,eseconde;
    TimePicker timeadd_start,timeadd_end;
    TextField tftimetype,timedate,timereamrks;
    Button timesave;
    Intent parameter;
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_time_updata);
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        parameter = intent;
        ht = new HelpTool(getApplicationContext());
        Calendar c = Calendar.getInstance();
        shours = c.get(Calendar.HOUR_OF_DAY);
        ehoure = c.get(Calendar.HOUR_OF_DAY);
        sminutess = c.get(Calendar.MINUTE);
        eminutese = c.get(Calendar.MINUTE);
        sseconds = c.get(Calendar.SECOND);
        eseconde = c.get(Calendar.SECOND);
        starttime = shours+":"+sminutess+":"+sseconds;
        endtime = ehoure+":"+eminutese+":"+eseconde;


        initComponents();
    }
    private void initComponents() {
        timedate = findComponentById(ResourceTable.Id_timedate);
        timedate.setText(parameter.getStringParam("time_date"));
        tftimetype = findComponentById(ResourceTable.Id_timeupdata_type);
        tftimetype.setText(parameter.getStringParam("time_type"));
        timereamrks = findComponentById(ResourceTable.Id_timeupdata_remarks);
        timereamrks.setText(parameter.getStringParam("time_remarks"));
        timeadd_start = findComponentById(ResourceTable.Id_time_updata_start);

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

        timeadd_end = findComponentById(ResourceTable.Id_time_updata_end);

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
        timesave = findComponentById(ResourceTable.Id_timeupdata_save);
        timesave.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {
        if (component == timesave) {
            String type = tftimetype.getText();
            String timedate2 = timedate.getText();
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
                new ToastDialog(TimeUpdataSlice.this)
                        .setText("您输入的时间不合理")
                        .setAlignment(LayoutAlignment.CENTER)
                        .show();

            } else {

                time.setTimeId(Integer.parseInt(parameter.getStringParam("timeId")));
                time.setTimeTimeline(timeline);
                time.setTimeType(type);
                time.setTimeDuration(duration);

                int time_id =Integer.parseInt(parameter.getStringParam("time_id"));
                String remarks = "nice";
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        URL url = new URL(ht.URL+"/Time/Timeupdata.php?time_timeline="+timeline+"&time_type="+type
                                +"&time_duration="+duration+"&time_remarks="+remarks+"&time_date="+timedate2+"&time_user_id="+ht.preferences.getInt("user_id",0)+"&time_id="+time_id
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
                                    new ToastDialog(TimeUpdataSlice.this)
                                            .setText("修改失败")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else
                                if (Json.size()>0){
                                    new ToastDialog(TimeUpdataSlice.this)
                                            .setText("修改成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                    tftimetype.setText("");
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
}
