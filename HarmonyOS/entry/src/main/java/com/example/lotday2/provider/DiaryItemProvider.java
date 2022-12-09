package com.example.lotday2.provider;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.ShadowDrawable;
import com.example.lotday2.bean.Diary;
import com.example.lotday2.slice.DiaryupdataSlice;
import com.example.lotday2.slice.MineAbilitySlice;
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
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/*
*适配器的作用：就是将集合中的数据List<DiaryBook>绑定到一个Item的布局中（组件）
* 1、继承BaseItemProvider，实现四个方法；
* 2、定义一个数据集合，通过有参构造器传入集合
* 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class DiaryItemProvider extends BaseItemProvider {

    private final List<Diary> list;
    private final AbilitySlice diaryabilitySlice;
    CommonDialog diarydialog;
    HelpTool ht;  //引入工具类

    public DiaryItemProvider(List<Diary> diary, AbilitySlice abilitySlice) {
        this.list = diary;
        this.diaryabilitySlice = abilitySlice;
        ht = new HelpTool(diaryabilitySlice.getContext()); //初始化
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position >= 0 && position < list.size()){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {

        return list.get(position).getDiaryId();  //返回ID
    }
    //用于渲染列表中的数据的：集合中有多少条数据，此方法就会被调用多少次
    //参数：i表示调用的索引
    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        return getItemComponent(position);
    }
    private Component getItemComponent(int position) {
        return getComponent(position);
    }

    private Component getComponent(int position) {
    //每次获取一个日记本信息
        Diary biary = list.get(position);
        //获取item模板:需要视图容器上下文
        DirectionalLayout diaryitemlayout = (DirectionalLayout)LayoutScatter.getInstance(diaryabilitySlice)
                .parse(ResourceTable.Layout_diary_list_item_diary,null,false);
        Text Textt = diaryitemlayout.findComponentById(ResourceTable.Id_diary_title);
        Text Texty = diaryitemlayout.findComponentById(ResourceTable.Id_diary_date_year);
        Text Textm = diaryitemlayout.findComponentById(ResourceTable.Id_diary_date_month);
        Text Textd = diaryitemlayout.findComponentById(ResourceTable.Id_diary_date_day);
        Image image = diaryitemlayout.findComponentById(ResourceTable.Id_diary_itemimage);
        Image imageedit= diaryitemlayout.findComponentById(ResourceTable.Id_diary_item_edit);
        Image diaryimagedelete= diaryitemlayout.findComponentById(ResourceTable.Id_diary_item_dele);
        //绑定数据
        Textt.setText(biary.getDiaryTitle());
        Textd.setText(String.valueOf(biary.getDiaryDateDay()));
        Textm.setText(String.valueOf(biary.getDiaryDateMonth()));
        Texty.setText(String.valueOf(biary.getDiaryDateYear()));
        ht.LoadImage(biary.getDiaryImage(),image,diaryabilitySlice.getContext());

        imageedit.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                intent.setParam("previewid", String.valueOf(biary.getDiaryId()));
                intent.setParam("previewtitle", biary.getDiaryTitle());
                intent.setParam("previewcontent",biary.getDiaryContent());
                intent.setParam("previewimage",biary.getDiaryImage());
                intent.setParam("previewdate_year",String.valueOf(biary.getDiaryDateYear()));
                intent.setParam("previewdate_month",String.valueOf(biary.getDiaryDateMonth()));
                intent.setParam("previewdate_day",String.valueOf(biary.getDiaryDateDay()));
                intent.setParam("previewbook",String.valueOf(biary.getDiaryDiarybookId()));
                diaryabilitySlice.presentForResult(new DiaryupdataSlice(),intent,0);
            }
        });
        diaryimagedelete.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {

                diarydialog = new CommonDialog(diaryabilitySlice.getContext());
                diarydialog.setTitleText("提示");
                diarydialog.setContentText("确定删除日记？");
                diarydialog.setSize(500,400);
                diarydialog.setCornerRadius(36.0f);
                diarydialog.setButton(IDialog.BUTTON1, "取消", (iDialog, i) -> iDialog.destroy());
                diarydialog.setButton(IDialog.BUTTON2, "确认", (iDialog, i) -> {
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        URL url = new URL(ht.URL+"/Diary/diarydelete.php?diary_id="+biary.getDiaryId());
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
                        diaryabilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                                if(Json.size()== 0){
                                    new ToastDialog(diaryabilitySlice)
                                            .setText("删除成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else if (Json.size()>0){
                                    new ToastDialog(diaryabilitySlice)
                                            .setText("删除失败")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }

                            }
                        });
                        HttpResponseCache.getInstalled().flush();
                    } catch (IOException e) {
                        //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                    }
                });
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.lotday2")
                        .withAbilityName("com.example.lotday2.DiaryAbility")
                        .build();
                    intent.setOperation(operation);
                diaryabilitySlice.startAbility(intent);
                });
                diarydialog.show();
            }

        });
        //返回渲染数据后的组件
        return diaryitemlayout;
    }
}
