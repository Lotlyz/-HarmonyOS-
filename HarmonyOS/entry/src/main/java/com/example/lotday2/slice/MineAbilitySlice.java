package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.bean.Timetype;
import com.example.lotday2.provider.MoodlnnItemProvider;
import com.example.lotday2.provider.TimetypeItemProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.PopupDialog;
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

public class MineAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    DirectionalLayout minedir;
    Button Home,Diary,Time,Mine;
    Button Login, About, Help, Message,Setting,Theme,grade,Timetype;
    Image touxiang;
    private final ArrayList<com.example.lotday2.bean.Timetype> list = new ArrayList<>();
    CommonDialog themedialog;
    private HelpTool ht;  //引入工具类
    PopupDialog timetypedialog;
    public static ShapeElement themecolor  = new ShapeElement();
    public  static ShapeElement changetheme  = new ShapeElement();
    public  static ShapeElement cardcolor  = new ShapeElement();
    public  static String windowcardcolor  = "#FFFFFF";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_mine);
        initComponents();
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initSystemUi();
        //检查是否登录
        if (ht.preferences.getString("state","").equals("1")) {

            Login = findComponentById(ResourceTable.Id_mine_login);
            Login.setVisibility(1);
            Text username = findComponentById(ResourceTable.Id_mine_user_name);
//            username.setText(ht.preferences.getString("user_name",""));
//            Text motto = findComponentById(ResourceTable.Id_mine_user_motto);
//            motto.setText(ht.preferences.getString("user_motto",""));
//            touxiang = findComponentById(ResourceTable.Id_mine_touxiang);
//            ht.LoadImage(ht.preferences.getString("user_image",""),touxiang,getContext());
            username.setText(ht.preferences.getString("userName",""));
            Text motto = findComponentById(ResourceTable.Id_mine_user_motto);
            motto.setText(ht.preferences.getString("userMotto",""));
            touxiang = findComponentById(ResourceTable.Id_mine_touxiang);
            ht.LoadImage(ht.preferences.getString("userImage",""),touxiang,getContext());
            float[] CORNERS = {30.0f, 30.0f, 0.0f, 0.0f, 30.0f, 30.0f, 0.0f, 0.0f};
            touxiang.setCornerRadii(CORNERS);

        } else {
            grade = findComponentById(ResourceTable.Id_mine_grade);
            grade.setVisibility(1);
            touxiang = findComponentById(ResourceTable.Id_mine_touxiang);
            touxiang.setVisibility(1);
        }

    }
    private void initSystemUi() {
        // 状态栏设置为透明
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        // 导航栏 ActionBar
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_NAVIGATION);
        // 全屏
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
    }
    protected void onActive() {
        super.onActive();
        list.clear(); //先清空
        gettimetypeData(); //加载数据库数据
    }

    private void initComponents() {
        minedir = findComponentById(ResourceTable.Id_mine_dircontent) ;
        minedir.setBackground(changetheme);
        Home =  findComponentById(ResourceTable.Id_mine_home);
        Diary =  findComponentById(ResourceTable.Id_mine_diary);
        Time = findComponentById(ResourceTable.Id_mine_time);
        Mine =  findComponentById(ResourceTable.Id_mine_mine);
        touxiang = findComponentById(ResourceTable.Id_mine_touxiang);
        Theme = findComponentById(ResourceTable.Id_mine_theme);
        Timetype = findComponentById(ResourceTable.Id_mine_shujubeifen);


        touxiang.setClickedListener(this);
        Home.setClickedListener(this);
        Diary.setClickedListener(this);
        Time.setClickedListener(this);
        Mine.setClickedListener(this);
        Theme.setClickedListener(this);
        Timetype.setClickedListener(this);
        Login = findComponentById(ResourceTable.Id_mine_login);
        About = findComponentById(ResourceTable.Id_mine_about);
        Help = findComponentById(ResourceTable.Id_mine_help);
        Message = findComponentById(ResourceTable.Id_mine_liuyanban);
        Setting = findComponentById(ResourceTable.Id_mine_setting);
        Login.setClickedListener(listener -> present(new LoginAbilitySlice(), new Intent()));
        About.setClickedListener(listener -> present(new AboutSlice(), new Intent()));
        Help.setClickedListener(listener -> present(new HelpSlice(), new Intent()));
        Message.setClickedListener(listener -> present(new MessageSlice(), new Intent()));
        Setting.setClickedListener(listener -> present(new SettingSlice(), new Intent()));
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
        }
        else if (component == touxiang) {
            if (ht.preferences.getString("state","").equals("1")) {
                Intent i = new Intent();
//                i.setParam("userid",String.valueOf(ht.preferences.getInt("user_id",0)));
//                i.setParam("username",ht.preferences.getString("user_name",""));
//                i.setParam("userphone",ht.preferences.getString("user_phone",""));
//                i.setParam("userpassword",ht.preferences.getString("user_password",""));
//                i.setParam("userimage",ht.preferences.getString("user_image",""));
//                i.setParam("usermotto",ht.preferences.getString("user_motto",""));
//                presentForResult(new PersonSlice(),i,0);
                present(new PersonSlice(),i);
            }
        }
        else if (component == Theme) {
            Switchtheme();
        }
        else if (component == Timetype) {
            TimetypeGM();
        }
    }

    private void TimetypeGM() {
        //气泡框中加入xml布局
        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_ability_time_typeGM, null, false);
        timetypedialog = new PopupDialog(this, layout, DirectionalLayout.LayoutConfig.MATCH_CONTENT,
                DirectionalLayout.LayoutConfig.MATCH_CONTENT);

        ListContainer timetypelistContainer = layout.findComponentById(ResourceTable.Id_time_type_listcontainerMG);
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        TimetypeItemProvider typeItemProvider = new TimetypeItemProvider(list,this);
        timetypelistContainer.setItemProvider(typeItemProvider);

        timetypedialog.setCustomComponent(layout);
        timetypedialog.setBackColor(Color.TRANSPARENT);
        timetypedialog.setAutoClosable(true);
        timetypedialog.showOnCertainPosition(LayoutAlignment.CENTER, 0, 0);
        timetypedialog.show();

    }
    private void Switchtheme() {
        Component container = LayoutScatter.getInstance(getContext())
                .parse(ResourceTable.Layout_ability_Theme,null,false);
        Button theme1 = container.findComponentById(ResourceTable.Id_theme_1);
        Button theme2 = container.findComponentById(ResourceTable.Id_theme_2);
        Button theme3 = container.findComponentById(ResourceTable.Id_theme_3);
        Button theme4 = container.findComponentById(ResourceTable.Id_theme_4);
        Button theme5 = container.findComponentById(ResourceTable.Id_theme_5);
        Button theme6 = container.findComponentById(ResourceTable.Id_theme_6);
        theme1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);
                //浅蓝色主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(255 ,105 ,180), new RgbColor(0, 191, 255)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#00BFFF")));
                windowcardcolor= "#00BFFF";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#00BFFF")));
                cardcolor.setCornerRadius(36);

                themedialog.destroy();
            }
        });
        theme2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);
                //橙黄色主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(255 ,218 ,185), new RgbColor(255, 127, 80)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#f9906f")));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#f9906f")));
                windowcardcolor= "#f9906f";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setCornerRadius(36);
                themedialog.destroy();
            }
        });
        theme3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);
                //粉红色主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(230 ,230 ,250), new RgbColor(255, 110, 180)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#FF6EB4")));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#FF6EB4")));
                windowcardcolor= "#FF6EB4";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setCornerRadius(36);
                themedialog.destroy();
            }
        });
        theme4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);
                //浅绿色主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(236, 236, 236), new RgbColor(0, 255, 127)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#90EE90")));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#90EE90")));
                windowcardcolor= "#90EE90";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setCornerRadius(36);
                themedialog.destroy();
            }
        });
        theme5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);
                //浅灰色主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(245, 245, 255), new RgbColor(207, 207, 207)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#CFCFCF")));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#CFCFCF")));
                windowcardcolor= "#CFCFCF";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setCornerRadius(36);
                themedialog.destroy();
            }
        });
        theme6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                changetheme.setShape(ShapeElement.RECTANGLE);

                //白色默认主题
                RgbColor[] rgbColors = new RgbColor[]{new RgbColor(255, 255, 255), new RgbColor(255, 255, 255)};
                changetheme.setRgbColors(rgbColors);
                changetheme.setGradientOrientation(ShapeElement.Orientation.BOTTOM_END_TO_TOP_START);
                themecolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#FFFFFF")));
                cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#FFFFFF")));
                windowcardcolor= "#FFFFFF";
                getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
                cardcolor.setCornerRadius(36);
                themedialog.destroy();
            }
        });

        themedialog = new CommonDialog(this);
        themedialog.setSize(520, 420);
        themedialog.setCornerRadius(30);
        themedialog.setContentCustomComponent(container);
        themedialog.show();
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
                                                Integer.parseInt(model.get("timetypeId").toString()),
                                                model.get("timetypeName").toString(),
                                                Integer.parseInt(model.get("timetypeUserId").toString())
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
