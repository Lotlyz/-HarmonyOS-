package com.example.lotday2.provider;

import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.slice.MoodAddSlice;
import com.example.lotday2.slice.MoodUpdataSlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;

/*
 *适配器的作用：就是将集合中的数据List<Time>绑定到一个Item的布局中（组件）
 * 1、继承BaseItemProvider，实现四个方法；
 * 2、定义一个数据集合，通过有参构造器传入集合
 * 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class MoodItemProvider extends BaseItemProvider {
    private  List<Mood> list;
    private  AbilitySlice moodabilitySlice;
    public  static int cangsatate  = 1;
    public  static int cangsatate2  = 1;
    private final ArrayList<Mood> moodlnnlist = new ArrayList<>();
    HelpTool ht;  //引入工具类
    CommonDialog moodlnndialog;

    public MoodItemProvider(List<Mood> mood, AbilitySlice moodabilitySlice) {
        this.list = mood;
        this.moodabilitySlice = moodabilitySlice;
        ht = new HelpTool(moodabilitySlice.getContext()); //初始化
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

        return list.get(position).getMoodId();  //返回ID
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
        //每次获取一个心情信息
        Mood mood = list.get(position);
        Component mooditemlayout = LayoutScatter.getInstance(moodabilitySlice).parse(ResourceTable.Layout_mood_list_item,null,false);
        DependentLayout card = mooditemlayout.findComponentById(ResourceTable.Id_itemmoodcard);
        ShapeElement cardcolor  = new ShapeElement();
        cardcolor.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#f9906f")));
        cardcolor.setCornerRadius(36);
        card.setBackground(cardcolor);
//        ShadowDrawable.setShadowDrawable(card, Color.getIntColor("#f9906f"), 30,
//                Color.getIntColor("#aaFF4040"), 36,0,0 );
//动态下部图片按钮
        Image image1 = mooditemlayout.findComponentById(ResourceTable.Id_moodlot);
        Image image2 = mooditemlayout.findComponentById(ResourceTable.Id_moodupdata);
        Image image3 = mooditemlayout.findComponentById(ResourceTable.Id_moodshoucang);
        Image image4 = mooditemlayout.findComponentById(ResourceTable.Id_mooddianzan);
        Image image5 = mooditemlayout.findComponentById(ResourceTable.Id_moodfenxiang);

//动态list组件
        Image image = mooditemlayout.findComponentById(ResourceTable.Id_mood_image);
        Text text2 = mooditemlayout.findComponentById(ResourceTable.Id_mood_sheying);
        Text text3 = mooditemlayout.findComponentById(ResourceTable.Id_mood_sheyingshi);
        Text text4 = mooditemlayout.findComponentById(ResourceTable.Id_mood_mood);
        Text text5 = mooditemlayout.findComponentById(ResourceTable.Id_mood_date);
        Text text6 = mooditemlayout.findComponentById(ResourceTable.Id_mood_address);
        Text text7 = mooditemlayout.findComponentById(ResourceTable.Id_mood_author);
        //绑定数据
        ht.LoadImage(mood.getMoodImage(),image,moodabilitySlice.getContext());
        text2.setText(mood.getMoodSheying());
        text3.setText(mood.getMoodSheyingzhe());
        text4.setText(mood.getMoodMood());
        text5.setText(mood.getMoodDate());
        text6.setText(mood.getMoodAddress());
        text7.setText(mood.getMoodAuthor());

        //心情驿站
        image1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
//                moodlnnlist.clear(); //先清空
//                getMoodlnnData(); //加载数据库数据
//                initListContainer();

//                Intent i = new Intent();
//                moodabilitySlice.present(new MoodlnnSlice(),i);

                Intent i = new Intent();
                moodabilitySlice.present(new MoodAddSlice(),i);
            }
        });

//修改动态数据信息
        image2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent2 = new Intent();
                intent2.setParam("moodId", String.valueOf(mood.getMoodId()));
                intent2.setParam("moodImage", mood.getMoodImage());
                intent2.setParam("moodSheying",mood.getMoodSheying());
                intent2.setParam("moodSheyingzhe",mood.getMoodSheyingzhe());
                intent2.setParam("moodMood",mood.getMoodMood());
                intent2.setParam("moodDate",mood.getMoodDate());
                intent2.setParam("moodAddress",mood.getMoodAddress());
                intent2.setParam("moodAuthor",mood.getMoodAuthor());
                moodabilitySlice.presentForResult(new MoodUpdataSlice(),intent2,0);
            }
        });
        image3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (cangsatate==1) {
                    image3.setPixelMap(ResourceTable.Media_moodcang2);
                    cangsatate = 2;
                }else if(cangsatate == 2){
                    image3.setPixelMap(ResourceTable.Media_moodcang);
                    cangsatate = 1;
                }
            }
        });
        image4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (cangsatate2==1) {
                    image4.setPixelMap(ResourceTable.Media_moodzan2);
                    cangsatate2 = 2;
                }else if(cangsatate2 == 2){
                    image4.setPixelMap(ResourceTable.Media_moodzan);
                    cangsatate2 = 1;
                }
            }
        });
        image5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                new ToastDialog(moodabilitySlice.getContext())
                        .setText("已发送")
                        .setAlignment(LayoutAlignment.CENTER)
                        .show();
            }
        });
        //返回渲染数据后的组件
        return mooditemlayout;
    }
//    private void initListContainer() {
//        Component container = LayoutScatter.getInstance(moodabilitySlice.getContext())
//                .parse(ResourceTable.Layout_moodlnn,null,false);
//        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
//        ListContainer moodlnnlistContainer = moodabilitySlice.findComponentById(ResourceTable.Id_moodlnnlistcontainer);
//        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
//        MoodlnnItemProvider moodlnnItemProvider = new MoodlnnItemProvider(moodlnnlist,moodabilitySlice);
//
//        moodlnndialog = new CommonDialog(moodabilitySlice.getContext());
//        moodlnndialog.setAutoClosable(true);
//        moodlnndialog.setSize(960, 1600);
//        moodlnndialog.setCornerRadius(30);
//        moodlnndialog.setContentCustomComponent(container);
//        moodlnndialog.show();
////        moodlnnlistContainer.setItemProvider(moodlnnItemProvider);
//    }
//    private void getMoodlnnData() {
//
//        NetManager netManager = NetManager.getInstance(null);
//        if (!netManager.hasDefaultNet()) {
//            return;
//        }
//        ThreadPoolUtil.submit(() -> {
//            NetHandle netHandle = netManager.getDefaultNet();
//            HttpURLConnection connection2 = null;
//            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                URL url = new URL(ht.URL+"/mood.php");
//                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
//                if (urlConnection instanceof HttpURLConnection) {
//                    connection2 = (HttpURLConnection) urlConnection;
//                }
//                connection2.setRequestMethod("GET");
//                connection2.connect();
//                try (InputStream inputStream = urlConnection.getInputStream()) {
//                    byte[] cache = new byte[2 * 1024];
//                    int len = inputStream.read(cache);
//                    while (len != -1) {
//                        outputStream.write(cache, 0, len);
//                        len = inputStream.read(cache);
//                    }
//                } catch (IOException e) {
//                }
//                String result = new String(outputStream.toByteArray());
//                moodabilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
//                        if(Json.size()>0){
//                            for(int i=0;i<Json.size();i++){
//                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//                                moodlnnlist.add
//                                        (new Mood(Integer.parseInt(model.get("mood_id").toString()),
//                                                model.get("mood_image").toString(),
//                                                model.get("mood_sheying").toString(),
//                                                model.get("mood_sheyingzhe").toString(),
//                                                model.get("mood_mood").toString(),
//                                                model.get("mood_date").toString(),
//                                                model.get("mood_address").toString(),
//                                                model.get("mood_author").toString(),
//                                                Integer.parseInt(model.get("mood_user_id").toString())
//                                        ));
//                            }
//                        }
//                        initListContainer();  //数据适配控件
//                    }
//                });
//                HttpResponseCache.getInstalled().flush();
//            } catch (IOException e) {
//            }
//        });
//
//    }
//private void getMoodlnnData() {
//
//    NetManager netManager = NetManager.getInstance(null);
//    if (!netManager.hasDefaultNet()) {
//        return;
//    }
//    ThreadPoolUtil.submit(() -> {
//        NetHandle netHandle = netManager.getDefaultNet();
//        HttpURLConnection connection2 = null;
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            URL url = new URL(ht.URL+"/mood.php");
//            URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
//            if (urlConnection instanceof HttpURLConnection) {
//                connection2 = (HttpURLConnection) urlConnection;
//            }
//            connection2.setRequestMethod("GET");
//            connection2.connect();
//            try (InputStream inputStream = urlConnection.getInputStream()) {
//                byte[] cache = new byte[2 * 1024];
//                int len = inputStream.read(cache);
//                while (len != -1) {
//                    outputStream.write(cache, 0, len);
//                    len = inputStream.read(cache);
//                }
//            } catch (IOException e) {
//            }
//            String result = new String(outputStream.toByteArray());
//            moodabilitySlice.getUITaskDispatcher().asyncDispatch(new Runnable() {
//                @Override
//                public void run() {
//                    JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
//                    if(Json.size()>0){
//                        for(int i=0;i<Json.size();i++){
//                            JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//                            moodlnnlist.add
//                                    (new Mood(Integer.parseInt(model.get("mood_id").toString()),
//                                            model.get("mood_image").toString(),
//                                            model.get("mood_sheying").toString(),
//                                            model.get("mood_sheyingzhe").toString(),
//                                            model.get("mood_mood").toString(),
//                                            model.get("mood_date").toString(),
//                                            model.get("mood_address").toString(),
//                                            model.get("mood_author").toString(),
//                                            Integer.parseInt(model.get("mood_user_id").toString())
//                                    ));
//                        }
//                    }
//                    initListContainer();  //数据适配控件
//                }
//            });
//            HttpResponseCache.getInstalled().flush();
//        } catch (IOException e) {
//        }
//    });
//
//}
}
