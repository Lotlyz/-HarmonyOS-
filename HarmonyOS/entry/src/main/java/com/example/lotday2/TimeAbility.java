package com.example.lotday2;

//import com.example.lotday2.slice.LoginSlice;
import com.example.lotday2.slice.TimeAbilitySlice;
import com.example.lotday2.slice.TimeAddSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class TimeAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(TimeAbilitySlice.class.getName());
        super.addActionRoute("TimeAdd", TimeAddSlice.class.getName());
    }
}
