package com.example.lotday2;
import com.example.lotday2.slice.*;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
public class DiaryAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DiaryAbilitySlice.class.getName());
        super.addActionRoute("DiaryAdd", DiaryAddSlice.class.getName());
    }
}
