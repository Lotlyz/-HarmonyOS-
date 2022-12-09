package com.example.lotday2.provider;

import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.ShadowDrawable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.slice.MineAbilitySlice;
import com.example.lotday2.slice.MoodUpdataSlice;
import com.example.lotday2.slice.MoodlnnSlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.DisplayManager;
import ohos.media.image.PixelMap;

import java.util.List;

/*
 *适配器的作用：就是将集合中的数据List<Time>绑定到一个Item的布局中（组件）
 * 1、继承BaseItemProvider，实现四个方法；
 * 2、定义一个数据集合，通过有参构造器传入集合
 * 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class MoodlnnItemProvider extends BaseItemProvider {
    private final List<Mood> listmoodlnn;
    public final AbilitySlice moodlnnabilitySlice;
    private final HelpTool ht;  //引入工具类

    public MoodlnnItemProvider(List<Mood> mood, AbilitySlice moodlnnabilitySlice) {
        this.listmoodlnn = mood;
        this.moodlnnabilitySlice = moodlnnabilitySlice;
        ht = new HelpTool(moodlnnabilitySlice.getContext()); //初始化
    }

    public AbilitySlice getMoodlnnabilitySlice() {
        return moodlnnabilitySlice;
    }

    @Override
    public int getCount() {
        return listmoodlnn == null ? 0 : listmoodlnn.size();
    }

    @Override
    public Object getItem(int position) {
        if (listmoodlnn != null && position >= 0 && position < listmoodlnn.size()){
            return listmoodlnn.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {

        return listmoodlnn.get(position).getMoodId();  //返回ID
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
        Mood mood = listmoodlnn.get(position);
        Component moodlnnitemlayout = LayoutScatter.getInstance(moodlnnabilitySlice).parse(ResourceTable.Layout_mood_list_item_moodlnn,null,false);
        DependentLayout card1 = moodlnnitemlayout.findComponentById(ResourceTable.Id_moodlinnitemde1);
        card1.setBackground(new MineAbilitySlice().cardcolor);
        //设置背景为颜色为#2979FF，圆角为8dp, 阴影颜色为#992979FF，宽度为6dp的背景
//        ShadowDrawable.setShadowDrawable(card1, Color.getIntColor("#2979FF"), dpToPx(8),Color.getIntColor("#992979FF"), dpToPx(6), 0, 0);
        ShadowDrawable.setShadowDrawable(card1, Color.getIntColor("#FFDEAD"), 8 * 3,
                Color.getIntColor("#eeFFFFF0"), 2, 0, 0);
        Image image1 = moodlnnitemlayout.findComponentById(ResourceTable.Id_moodlnnimage1);
        Text text2 = moodlnnitemlayout.findComponentById(ResourceTable.Id_moodlnnimage1_authour);


        //绑定数据
        ht.LoadImage(mood.getMoodImage(),image1,moodlnnabilitySlice.getContext());
        text2.setText("By:"+mood.getMoodAuthor());
//            ht.LoadImage(mood.getMood_image(), image2, moodlnnabilitySlice.getContext());
//            text3.setText(mood.getMood_author());
//        if(position < listmoodlnn.size()-1) {
////          Mood mood2 = listmoodlnn.get(position);
//            Mood mood2 = listmoodlnn.get(position + 1);
//            ht.LoadImage(mood2.getMood_image(), image2, moodlnnabilitySlice.getContext());
//            text3.setText(mood2.getMood_author());

//        }else if(position == listmoodlnn.size()-1){
//            Mood mood3 = listmoodlnn.get(position);
//            ht.LoadImage(mood3.getMood_image(),image1,moodlnnabilitySlice.getContext());
//            text2.setText(mood3.getMood_author());
//            image2.setVisibility(1);
//            text3.setVisibility(1);
//            card2.setVisibility(1);
//        }
//        //心情驿站
//        image1.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                Intent i = new Intent();
//                moodabilitySlice.present(new MoodlnnSlice(),i);
//            }
//        });
////修改动态数据信息
//        image2.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                Intent intent2 = new Intent();
//                intent2.setParam("mood_id", String.valueOf(mood.getMood_id()));
//                intent2.setParam("mood_image", mood.getMood_image());
//                intent2.setParam("mood_sheying",mood.getMood_sheying());
//                intent2.setParam("mood_sheyingzhe",mood.getMood_sheyingzhe());
//                intent2.setParam("mood_mood",mood.getMood_mood());
//                intent2.setParam("mood_date",mood.getMood_date());
//                intent2.setParam("mood_address",mood.getMood_address());
//                intent2.setParam("mood_author",mood.getMood_author());
//                moodabilitySlice.presentForResult(new MoodUpdataSlice(),intent2,0);
//            }
//        });
//        image3.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                if (cangsatate==1) {
//                    image3.setPixelMap(ResourceTable.Media_moodcang2);
//                    cangsatate = 2;
//                }else if(cangsatate == 2){
//                    image3.setPixelMap(ResourceTable.Media_moodcang);
//                    cangsatate = 1;
//
//                }
//            }
//        });
//        image4.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                if (cangsatate2==1) {
//                    image4.setPixelMap(ResourceTable.Media_moodzan2);
//                    cangsatate2 = 2;
//                }else if(cangsatate2 == 2){
//                    image4.setPixelMap(ResourceTable.Media_moodzan);
//                    cangsatate2 = 1;
//                }
//            }
//        });
//        image5.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                new ToastDialog(moodabilitySlice.getContext())
//                        .setText("已发送")
//                        .setAlignment(LayoutAlignment.CENTER)
//                        .show();
//            }
//        });
//        //返回渲染数据后的组件
        return moodlnnitemlayout;
    }
//    private int dpToPx(int dp) {
//        float density = DisplayManager.getInstance().getDefaultDisplay(moodlnnabilitySlice).get().getAttributes().densityPixels;
//        return (int) (dp * density + 0.5f);
//    }
}
