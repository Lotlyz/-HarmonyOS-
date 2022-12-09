package com.example.lotday2.provider;

import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Time;
import com.example.lotday2.bean.Timetype;
import com.example.lotday2.slice.MineAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

/*
 *适配器的作用：就是将集合中的数据List<Time>绑定到一个Item的布局中（组件）
 * 1、继承BaseItemProvider，实现四个方法；
 * 2、定义一个数据集合，通过有参构造器传入集合
 * 3、 定义一个abilitySlice，通过有参构造器传入集合
 */
public class TimetypeItemProvider extends BaseItemProvider {

    private final List<Timetype> list;
    private final AbilitySlice timetypeabilitySlice;

    public TimetypeItemProvider(List<Timetype> timetype, AbilitySlice abilitySlice) {
        this.list = timetype;
        this.timetypeabilitySlice = abilitySlice;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        Timetype timetype = list.get(i);
        //获取item模板:需要视图容器上下文
        DirectionalLayout timetypeitemlayout = (DirectionalLayout) LayoutScatter.getInstance(timetypeabilitySlice)
                .parse(ResourceTable.Layout_time_list_item_type,null,false);
//        DependentLayout card = timetypeitemlayout.findComponentById(ResourceTable.Id_timetypecardde);
//        card.setBackground(new MineAbilitySlice().cardcolor);
        Checkbox typeitemname = timetypeitemlayout.findComponentById(ResourceTable.Id_time_type_name);
//        //绑定数据
        typeitemname.setText(timetype.getTimetypeName());
        //返回渲染数据后的组件
        return timetypeitemlayout;
    }
}
