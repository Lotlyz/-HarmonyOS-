package com.example.lotday2;

import com.example.lotday2.slice.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MineAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MineAbilitySlice.class.getName());
        super.addActionRoute("Register", RegisterSlice.class.getName());
        super.addActionRoute("About", AboutSlice.class.getName());
        super.addActionRoute("Help", HelpSlice.class.getName());
        super.addActionRoute("Message", MessageSlice.class.getName());
        super.addActionRoute("DiaryUpdata", MessageSlice.class.getName());
    }
}
