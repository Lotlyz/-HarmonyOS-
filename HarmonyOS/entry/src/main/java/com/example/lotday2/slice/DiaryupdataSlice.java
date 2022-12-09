package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
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
import java.util.ArrayList;
import java.util.Date;

public class DiaryupdataSlice extends AbilitySlice implements Component.ClickedListener {


    //创建数据库配置对象（参数：数据库名）
    private final StoreConfig config = StoreConfig.newDefaultConfig("Diary.db");
    private static final RdbOpenCallback Callback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_diary (diary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "diary_title varchar(100),diary_content varchar(500), diary_date varchar(100),diary_author varchar(100) )");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    HelpTool ht;  //引入工具类
    Text imagepiker,tfdate;
    PopupDialog popupDialog;
    ArrayList<Diarybook> booklist = new ArrayList<>();
    Text huanhang,suojin,bigtext,smalltext,pikeron,pikeroff,txbook;
    TextField tftitle,tfcontent,pikerday,pikermonth,pikeryear;
    Image diarysave,back;
    Intent parameter;
    CommonDialog pikerDialog,imagepikerDialog,pikerimageDialog;
    int diaryid,book,date_year,date_month,date_day;
    String dateStr1,dateStr2,dateStr3;
    String title,content,image;
    int dateday,datemonth,dateyear,bookid;
    String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker1.jpg";
    String b = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagpiker5.jpg";
    String c = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker4.jpg";
    String d = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker3.jpg";
    String e = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/iamgepiker6.jpg";
    String f = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepike2.jpg";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_diary_updata);
        parameter =intent;
        ht = new HelpTool(getApplicationContext());
        initComponents();
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
    }
    protected void onActive() {
        super.onActive();
        booklist.clear(); //先清空
        getBookData(); //加载数据库数据
    }
    private void initComponents() {

        diaryid = Integer.parseInt(parameter.getStringParam("previewid"));
        title = parameter.getStringParam("previewtitle");
        content = parameter.getStringParam("previewcontent");
        image = parameter.getStringParam("previewimage");
        date_year = Integer.parseInt(parameter.getStringParam("previewdate_year"));
        date_month = Integer.parseInt(parameter.getStringParam("previewdate_month"));
        date_day = Integer.parseInt(parameter.getStringParam("previewdate_day"));
        book  = Integer.parseInt(parameter.getStringParam("previewbook"));

        back=findComponentById(ResourceTable.Id_diary_updata_back);
        back.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent i = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.lotday2")
                        .withAbilityName("com.example.lotday2.DiaryAbility")
                        .build();
                i.setOperation(operation);
                startAbility(i);
            }
        });
        diarysave = findComponentById(ResourceTable.Id_diary_updata_save);

        tftitle = findComponentById(ResourceTable.Id_diaryupdata_title);
        tfcontent = findComponentById(ResourceTable.Id_updata_content);
        tfdate = findComponentById(ResourceTable.Id_diary_updata_topbar_date);
        txbook = findComponentById(ResourceTable.Id_diary_updata_topbar_book);
        txbook.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                bookpiker();
            }
        });

        huanhang = findComponentById(ResourceTable.Id_diaryupdata_botbar_huanhang);
        huanhang.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                tfcontent.setText(tfcontent.getText()+"\n");
            }
        });
        suojin = findComponentById(ResourceTable.Id_diaryupdata_botbar_suojin);
        suojin.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                tfcontent.setText("    "+tfcontent.getText());
            }
        });
        bigtext = findComponentById(ResourceTable.Id_diaryupdata_botbar_bigtextsize);
        bigtext.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                tfcontent.setTextSize(99);
            }
        });
        smalltext = findComponentById(ResourceTable.Id_diaryupdata_botbar_smalltextsize);
        smalltext.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                tfcontent.setTextSize(66);
            }
        });
        imagepiker = findComponentById(ResourceTable.Id_diaryupdata_botbar_image);
        imagepiker.setClickedListener(new Component.ClickedListener() {
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
        diarysave.setClickedListener(this);
        tftitle.setText(title);
        tfdate.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                datepiker();
            }
        });
        tfcontent.setText(content);
        tfdate.setText(date_year+"年"+date_month+"月"+date_day+"日");
//        tfbook.setText(book);
//        tfbook.setVisibility(1);
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
                tfdate.setText(dateyear+"年"+datemonth+"月"+dateday+"日");
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
                image="";
                image=a;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                image="";
                image=b;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                image="";
                image=c;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                image="";
                image=d;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                image="";
                image=e;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                image="";
                image=f;
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

    @Override
    public void onClick(Component component) {
        if (component == diarysave) {
            String titleu = tftitle.getText();
            String contentu = tfcontent.getText();
            String dateu = tfdate.getText();
            String imageu=image;
            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.URL+"/Diary/diaryupdata.php?diary_id="+diaryid+"&diary_title="+titleu
                            +"&diary_image="+imageu+"&diary_content="+contentu+"&diary_date_year="+dateyear+"&diary_date_month="+datemonth+"&diary_date_day="+dateday+"&diary_diarybook_id="+bookid+"&diary_user_id="+ht.preferences.getInt("user_id",0)
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
                                new ToastDialog(DiaryupdataSlice.this)
                                        .setText("修改失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(DiaryupdataSlice.this)
                                        .setText("修改成功")
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
