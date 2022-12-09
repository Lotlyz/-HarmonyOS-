package com.example.lotday2;

import com.example.lotday2.slice.MainAbilitySlice;
import com.example.lotday2.slice.MoodAddSlice;
import com.example.lotday2.slice.MoodUpdataSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;

public class MainAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        super.addActionRoute("MoodAdd", MoodAddSlice.class.getName());
        super.addActionRoute("MoodUpdata", MoodUpdataSlice.class.getName());

    }
}