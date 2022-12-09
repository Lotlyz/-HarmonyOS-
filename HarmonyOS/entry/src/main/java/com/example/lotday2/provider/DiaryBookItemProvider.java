package com.example.lotday2.provider;

import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Diarybook;
import com.example.lotday2.slice.MineAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;

import java.util.List;


/*
*适配器的作用：就是将集合中的数据List<DiaryBook>绑定到一个Item的布局中（组件）
* 1、继承BaseItemProvider，实现四个方法；
* 2、定义一个数据集合，通过有参构造器传入集合
* 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class DiaryBookItemProvider extends BaseItemProvider {

    private final List<Diarybook> list;
    private final AbilitySlice abilitySlice;
    HelpTool ht;  //引入工具类
    public DiaryBookItemProvider(List<Diarybook> diarybooks, AbilitySlice abilitySlice) {
        this.list = diarybooks;
        this.abilitySlice = abilitySlice;
        ht = new HelpTool(abilitySlice.getApplicationContext());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //用于渲染列表中的数据的：集合中有多少条数据，此方法就会被调用多少次
    //参数：i表示调用的索引
    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {

        //每次获取一个日记本信息
        Diarybook biarybook = list.get(i);
        //获取item模板:需要视图容器上下文
        DirectionalLayout diarybookitemlayout = (DirectionalLayout)LayoutScatter.getInstance(abilitySlice)
                .parse(ResourceTable.Layout_diary_list_item_rijiben,null,false);
        DirectionalLayout card = diarybookitemlayout.findComponentById(ResourceTable.Id_itemdiarybookdir);

        card.setBackground(new ShapeElement(){{
            setRgbColor(RgbPalette.TRANSPARENT);      //设置内部组件透明
        }});
        Image image = diarybookitemlayout.findComponentById(ResourceTable.Id_diarybookimage);
        Text text = diarybookitemlayout.findComponentById(ResourceTable.Id_diarybookname);
        //绑定数据
        ht.LoadImage(biarybook.getDiarybookImage(),image,abilitySlice.getContext());
        text.setText(biarybook.getDiarybookName());
        //返回渲染数据后的组件
        return diarybookitemlayout;

    }
}
