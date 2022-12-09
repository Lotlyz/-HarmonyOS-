package com.example.lotday2.slice;

import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;

public class SettingSlice extends AbilitySlice implements Component.ClickedListener {
    Button back,change,esc;
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_setting);
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
        ht = new HelpTool(getApplicationContext());
        initComponents();
    }
    private void initComponents() {
        change = findComponentById(ResourceTable.Id_setting_change);
        esc = findComponentById(ResourceTable.Id_setting_esc);
        back = findComponentById(ResourceTable.Id_setting_back);
        change.setClickedListener(this);
        esc.setClickedListener(this);
        back.setClickedListener(this);

        //检查是否登录
        if (ht.preferences.getString("state","").equals("1")) {
        } else {
            change.setVisibility(1);
            esc.setVisibility(1);
        }
    }
    @Override
    public void onClick(Component component) {
        if (component == back){
            terminate();
        }else if (component == change){
            Intent intent =new Intent();
            present(new LoginAbilitySlice(),intent);
        }else if (component == esc){
//            ht.preferences.putString("state","3");
            ht.databaseHelper.deletePreferences("pdb");
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MainAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }
    }
}
