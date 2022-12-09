package com.example.lotday2.slice;

import com.example.lotday2.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.webengine.Navigator;
import ohos.agp.components.webengine.WebView;
import ohos.agp.utils.Color;

public class HelpSlice extends AbilitySlice implements Component.ClickedListener {
//    public static final String EXAMPLE_URL = "https://www.lotlyz.cn/";

    public static final String EXAMPLE_URL = "https://www.lotlyz.cn/";
    Button back;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_mine_help);
        initComponents();
        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));
    }

    private void initComponents() {
        WebView webView = (WebView) findComponentById(ResourceTable.Id_help_webview);
        webView.getWebConfig().setJavaScriptPermit(true);  // 如果网页需要使用JavaScript，增加此行；如何使用JavaScript下文有详细介绍
        final String url = EXAMPLE_URL; // EXAMPLE_URL由开发者自定义
        webView.load(url);

        Navigator navigator = webView.getNavigator();
        if (navigator.canGoBack()) {
            navigator.goBack();
        }
        if (navigator.canGoForward()) {
            navigator.goForward();
        }

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


    }
}
