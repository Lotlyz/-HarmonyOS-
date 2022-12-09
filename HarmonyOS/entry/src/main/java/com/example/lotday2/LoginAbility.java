package com.example.lotday2;

import com.example.lotday2.slice.LoginAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class LoginAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(LoginAbilitySlice.class.getName());
    }
}
