package com.example.lotday2.slice;

import com.example.lotday2.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;

public class AboutSlice extends AbilitySlice implements Component.ClickedListener {
    Button back;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_mine_about);
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        initComponents();
    }
    private void initComponents() {
        back = (Button) findComponentById(ResourceTable.Id_about_back);
        back.setClickedListener(this);
    }
    @Override
    public void onActive() {
        super.onActive();
    }
    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    @Override
    public void onClick(Component component) {
        if (component == back){
            Intent i = new Intent();
            present(new MineAbilitySlice(), i);
        }
    }
}
