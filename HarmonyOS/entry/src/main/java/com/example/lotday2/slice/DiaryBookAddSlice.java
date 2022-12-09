package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Diarybook;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
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

public class DiaryBookAddSlice extends AbilitySlice implements Component.ClickedListener {
    TextField tfname;
    Button save,image;
    String imageurl;
    HelpTool ht;  //引入工具类
    String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker1.jpg";
    String b = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagpiker5.jpg";
    String c = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker4.jpg";
    String d = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker3.jpg";
    String e = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/iamgepiker6.jpg";
    String f = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepike2.jpg";
    CommonDialog pikerDialog,imagepikerDialog,pikerimageDialog;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_diarybook_add);
        ht = new HelpTool(getApplicationContext());
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initComponents();
    }
    private void initComponents() {
        tfname = findComponentById(ResourceTable.Id_bookadd_name);
        save = findComponentById(ResourceTable.Id_diarybookadd_save);
        image = findComponentById(ResourceTable.Id_diarybookadd_image);
        image.setClickedListener(new Component.ClickedListener() {
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
        save.setClickedListener(this);
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
                imageurl="";
                imageurl=a;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imageurl="";
                imageurl=b;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imageurl="";
                imageurl=c;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imageurl="";
                imageurl=d;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imageurl="";
                imageurl=e;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                imageurl="";
                imageurl=f;
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
        if (component == save) {
            String name = tfname.getText();
            Diarybook diaryBook = new Diarybook();
            diaryBook.setDiarybookName(name);
            String type ="生活";
            NetManager netManager = NetManager.getInstance(null);
            if (!netManager.hasDefaultNet()) {
                return;
            }
            ThreadPoolUtil.submit(() -> {
                NetHandle netHandle = netManager.getDefaultNet();
                HttpURLConnection connection = null;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    URL url = new URL(ht.URL+"/DiaryBook/diarybookadd.php?diarybook_name="+name+"&diarybook_image="+imageurl
                            +"&diarybook_type="+type+"&diarybook_user_id="+ht.preferences.getInt("user_id",0)
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
                                new ToastDialog(DiaryBookAddSlice.this)
                                        .setText("添加失败")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                            }else
                            if (Json.size()>0){
                                new ToastDialog(DiaryBookAddSlice.this)
                                        .setText("添加成功")
                                        .setAlignment(LayoutAlignment.CENTER)
                                        .show();
                                terminate();
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
