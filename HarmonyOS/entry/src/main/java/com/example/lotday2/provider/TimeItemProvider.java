package com.example.lotday2.provider;

import com.alibaba.fastjson.JSONArray;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Time;
import com.example.lotday2.slice.MineAbilitySlice;
import com.example.lotday2.slice.TimeUpdataSlice;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
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
 *适配器的作用：就是将集合中的数据List<Time>绑定到一个Item的布局中（组件）
 * 1、继承BaseItemProvider，实现四个方法；
 * 2、定义一个数据集合，通过有参构造器传入集合
 * 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class TimeItemProvider extends BaseItemProvider {

    private final List<Time> list;
    private final AbilitySlice timeabilitySlice;
    HelpTool ht;  //引入工具类

    public TimeItemProvider(List<Time> time, AbilitySlice abilitySlice) {
        this.list = time;
        this.timeabilitySlice = abilitySlice;
        ht = new HelpTool(timeabilitySlice.getContext()); //初始化
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

        return list.get(position).getTimeId();  //返回ID
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
        Time time = list.get(position);
        //获取item模板:需要视图容器上下文
        DirectionalLayout timeitemlayout = (DirectionalLayout) LayoutScatter.getInstance(timeabilitySlice)
                .parse(ResourceTable.Layout_time_list_item_G,null,false);
        DependentLayout card = timeitemlayout.findComponentById(ResourceTable.Id_itemtimede);
        card.setBackground(new MineAbilitySlice().cardcolor);
//        Text Texti = (Text) diaryitemlayout.findComponentById(ResourceTable.Id_diary_id);
        Text Textt = timeitemlayout.findComponentById(ResourceTable.Id_time_timeline);
        Text textc = timeitemlayout.findComponentById(ResourceTable.Id_time_type);
        Text textd = timeitemlayout.findComponentById(ResourceTable.Id_time_duration);
        Text texte = timeitemlayout.findComponentById(ResourceTable.Id_time_timedate);

        Image imageedit= timeitemlayout.findComponentById(ResourceTable.Id_time_item_edit);
        Image imagedelete= timeitemlayout.findComponentById(ResourceTable.Id_time_item_dele);
//        //绑定数据
////        Texti.setText(biary.getDiary_id());
        Textt.setText(time.getTimeTimeline());
        textc.setText(time.getTimeType());
        textd.setText(time.getTimeDuration());
        texte.setText(time.getTimeDate());
        //按钮点击事件
        imageedit.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent2 = new Intent();
                intent2.setParam("timeId", String.valueOf(time.getTimeId()));
                intent2.setParam("timeTimeline", time.getTimeTimeline());
                intent2.setParam("timeDuration", time.getTimeDuration());
                intent2.setParam("timeType", time.getTimeType());
                intent2.setParam("timeDate", time.getTimeDate());
                intent2.setParam("timeRemarks", time.getTimeRemarks());
                timeabilitySlice.presentForResult(new TimeUpdataSlice(), intent2, 0);
            }
        });
        imagedelete.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        URL url = new URL(ht.LOCALHOST_URL+"/Time/delete?timeId="+time.getTimeId());
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
                        timeabilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                                if(Json.size()== 0){
                                    new ToastDialog(timeabilitySlice)
                                            .setText("删除成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else if (Json.size()>0){
                                    new ToastDialog(timeabilitySlice)
                                            .setText("删除失败")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }
//                                initListContainer();  //数据适配控件
                            }
                        });
                        HttpResponseCache.getInstalled().flush();
                    } catch (IOException e) {
                        //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                    }
                });
//                Component mooditemlayout = LayoutScatter.getInstance(timeabilitySlice).parse(ResourceTable.Layout_ability_time,null,false);
//                PageSlider pageSlider =timeabilitySlice.findComponentById(ResourceTable.Id_Time_page_slider);
//                TimeAbilitySlice.inittimeG(pageSlider);
//                notifyDataChanged();
                Intent i = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.example.lotday2")
                        .withAbilityName("com.example.lotday2.TimeAbility")
                        .build();
                i.setOperation(operation);
                timeabilitySlice.startAbility(i);

            }
        });
        //返回渲染数据后的组件
        return timeitemlayout;
    }
}
