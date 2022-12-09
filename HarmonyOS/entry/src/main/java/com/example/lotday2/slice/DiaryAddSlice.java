package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Diary;
import com.example.lotday2.bean.Diarybook;
import com.example.lotday2.provider.DiaryBookItemProvider;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryAddSlice extends AbilitySlice implements Component.ClickedListener {

    //创建数据库配置对象（参数：数据库名）
    private final StoreConfig config = StoreConfig.newDefaultConfig("Diary.db");

    private  static final RdbOpenCallback Callback = Diary.getDiaryCallback();
    Text tfdate,tfbook;
    CommonDialog pikerDialog,imagepikerDialog,pikerimageDialog;
    PopupDialog popupDialog;
    Text huanhang,suojin,bigtext,smalltext,pikeron,pikeroff,tfimage;
    TextField TFcontent,tftitle,pikerday,pikermonth,pikeryear;
    Image diarysave,back;
    String dateStr1,dateStr2,dateStr3,diaryimageadd;
    int dateday,datemonth,dateyear,bookid;
    ArrayList<Diarybook> booklist = new ArrayList<>();
    String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker1.jpg";
    String b = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagpiker5.jpg";
    String c = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker4.jpg";
    String d = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker3.jpg";
    String e = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/iamgepiker6.jpg";
    String f = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepike2.jpg";
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_diary_add);
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        ht = new HelpTool(getApplicationContext());
        initComponents();
    }
    protected void onActive() {
        super.onActive();
        booklist.clear(); //先清空
        getBookData(); //加载数据库数据
    }
    private void initComponents() {
        tfdate=findComponentById(ResourceTable.Id_diaryadd_topbar_date);
        tfdate.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                datepiker();
            }
        });
        tfbook=findComponentById(ResourceTable.Id_diaryadd_topbar_book);
        tfbook.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                bookpiker();
            }
        });
        tftitle =findComponentById(ResourceTable.Id_diary_title);
        TFcontent = findComponentById(ResourceTable.Id_content);
        huanhang = findComponentById(ResourceTable.Id_diaryadd_botbar_huanhang);
        huanhang.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                TFcontent.setText(TFcontent.getText()+"\n");
            }
        });
        suojin = findComponentById(ResourceTable.Id_diaryadd_botbar_suojin);
        suojin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                TFcontent.setText("    "+TFcontent.getText());
            }
        });
        bigtext = findComponentById(ResourceTable.Id_diaryadd_botbar_bigtextsize);
        bigtext.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                TFcontent.setTextSize(110);
            }
        });
        smalltext = findComponentById(ResourceTable.Id_diaryadd_botbar_smalltextsize);
        smalltext.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                TFcontent.setTextSize(66);
            }
        });
        tfimage = findComponentById(ResourceTable.Id_diaryadd_botbar_image);
        tfimage.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                pikerimageDialog = new CommonDialog(getContext());

//        pikerDialog.setAutoClosable(true);
                pikerimageDialog.setSize(666, 666);
                pikerimageDialog.setCornerRadius(30);
                pikerimageDialog.setContentText("请输入图片网址：");
                pikerimageDialog.setButton(IDialog.BUTTON1, "选择系统图片", (iDialog, i) -> imagepiker());
//                pikerimageDialog.setContentCustomComponent(DiaryAddSlice);
                pikerimageDialog.show();
            }
        });

//        tfdate.setText(dateStr);
//        tfauthor = findComponentById(ResourceTable.Id_diary_author);
        diarysave = findComponentById(ResourceTable.Id_mood_add_save);
        back = findComponentById(ResourceTable.Id_mood_add_back);
        diarysave.setClickedListener(this);
        back.setClickedListener(this);
    }

    private void bookpiker() {
        //气泡框中加入xml布局
        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_book_piker, null, false);
        popupDialog = new PopupDialog(this, layout, DirectionalLayout.LayoutConfig.MATCH_PARENT,
                1080);
        ListContainer listContainer2 = layout.findComponentById(ResourceTable.Id_diary_diarybook_listcontainer);
        DiaryBookItemProvider diaryBookItemProvider = new DiaryBookItemProvider(booklist,this);
        listContainer2.setItemProvider(diaryBookItemProvider);
        listContainer2.setItemClickedListener((container, component2, position, id) -> {
            Diarybook item = (Diarybook) listContainer2.getItemProvider().getItem(position);
            bookid= item.getDiarybookId();
            popupDialog.destroy();
        });
        popupDialog.setCustomComponent(layout);
        popupDialog.setBackColor(Color.TRANSPARENT);
        popupDialog.setArrowSize(50, 30);
        popupDialog.setArrowOffset(100);
        popupDialog.setHasArrow(true);
        popupDialog.setAutoClosable(true);
        popupDialog.showOnCertainPosition(LayoutAlignment.CENTER, 0, 0);
        popupDialog.show();
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
                diaryimageadd="";
                diaryimageadd=a;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diaryimageadd="";
                diaryimageadd=b;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diaryimageadd="";
                diaryimageadd=c;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diaryimageadd="";
                diaryimageadd=d;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diaryimageadd="";
                diaryimageadd=e;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diaryimageadd="";
                diaryimageadd=f;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });


        imagepikerDialog = new CommonDialog(layout.getContext());
        imagepikerDialog.setAutoClosable(true);
        imagepikerDialog.setSize(960, 1450);
        imagepikerDialog.setCornerRadius(30);
        imagepikerDialog.setContentCustomComponent(layout);
        imagepikerDialog.show();
    }

    private void datepiker() {

        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_date_datepiker, null, false);
        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyy年-MM月-dd日");
//        dateStr = sdf.format(date);
        SimpleDateFormat day = new SimpleDateFormat("dd");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat year = new SimpleDateFormat("yyy");
        dateStr1 = day.format(date);
        dateStr2 = month.format(date);
        dateStr3 = year.format(date);
        //datepiker
        pikerday = layout.findComponentById(ResourceTable.Id_date_pikerday);
        pikermonth = layout.findComponentById(ResourceTable.Id_date_pikermonth);
        pikeryear = layout.findComponentById(ResourceTable.Id_date_pikeryear);
        pikeroff = layout.findComponentById(ResourceTable.Id_date_pikce_off);
        pikeron = layout.findComponentById(ResourceTable.Id_date_pikce_on);
        pikerday.setText(dateStr1);
        pikermonth.setText(dateStr2);
        pikeryear.setText(dateStr3);
        pikeron.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                dateday= Integer.parseInt(pikerday.getText());
                datemonth= Integer.parseInt(pikermonth.getText());
                dateyear= Integer.parseInt(pikeryear.getText());
                pikerDialog.destroy();
            }
        });
        pikeroff.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                pikerDialog.destroy();
            }
        });

        pikerDialog = new CommonDialog(layout.getContext());
//        pikerDialog.setAutoClosable(true);
        pikerDialog.setSize(888, 666);
        pikerDialog.setCornerRadius(30);
        pikerDialog.setContentCustomComponent(layout);
        pikerDialog.show();
    }


    @Override
    public void onClick(Component component) {
        if (component == diarysave) {

            String title = tftitle.getText();
            String content = TFcontent.getText().toString();
            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.URL+"/Diary/diaryadd.php?diary_user_id="+ht.preferences.getInt("user_id",0)+"&diary_title="+title
                            +"&diary_content="+content+"&diary_image="+diaryimageadd+"&diary_date_year="+dateyear+"&diary_date_month="+datemonth+"&diary_date_day="+dateday+"&diary_diarybook_id="+bookid
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
                    }
                    String result = new String(outputStream.toByteArray());
                    getUITaskDispatcher().asyncDispatch(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                            if(Json.size()== 0){
                                new ToastDialog(DiaryAddSlice.this)
                                        .setText("添加失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(DiaryAddSlice.this)
                                        .setText("添加成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                Intent i = new Intent();
                                Operation operation = new Intent.OperationBuilder()
                                        .withDeviceId("")
                                        .withBundleName("com.example.lotday2")
                                        .withAbilityName("com.example.lotday2.DiaryAbility")
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
        else if(component == back){
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.DiaryAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }
    }

    public void getBookData() {
        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
                URL url = new URL(ht.URL+"/DiaryBook/diarybook.php?diarybook_user_id="+ht.preferences.getInt("user_id",0));
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
                                booklist.add
                                        (new Diarybook(Integer.parseInt(model.get("diarybook_id").toString()),
                                                model.get("diarybook_name").toString(),
                                                model.get("diarybook_image").toString(),
                                                model.get("diarybook_type").toString(),
                                                Integer.parseInt(model.get("diarybook_user_id").toString())
                                        ));
                            }
                        }
//                        initBookListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }
}
