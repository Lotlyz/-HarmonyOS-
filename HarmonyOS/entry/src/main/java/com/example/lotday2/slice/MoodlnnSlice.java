package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Mood;
import com.example.lotday2.provider.MoodlnnItemProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MoodlnnSlice extends AbilitySlice implements Component.ClickedListener {
    Button back;
    private final ArrayList<Mood> moodlnnlist = new ArrayList<>();
    HelpTool ht;  //引入工具类
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_moodlnn);
        ht = new HelpTool(getContext()); //初始化
        initListContainer();
        initComponents();
    }

    private void initListContainer() {
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer moodlnnlistContainer = findComponentById(ResourceTable.Id_moodlnnlistcontainer);
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        MoodlnnItemProvider moodlnnItemProvider = new MoodlnnItemProvider(moodlnnlist,this);

        moodlnnlistContainer.setItemProvider(moodlnnItemProvider);
    }

    private void initComponents() {
//        back = (Button) findComponentById(ResourceTable.Id_about_back);
//        back.setClickedListener(this);
    }
    @Override
    protected void onActive() {
        super.onActive();
        moodlnnlist.clear(); //先清空
        getMoodlnnData(); //加载数据库数据
    }
    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    @Override
    public void onClick(Component component) {
//        if (component == back){
//            Intent i = new Intent();
//            present(new MineAbilitySlice(), i);
//        }
    }
    private void getMoodlnnData() {

        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }
        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection2 = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                URL url = new URL(ht.LOCALHOST_URL+"/Mood/moodAll");
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection2 = (HttpURLConnection) urlConnection;
                }
                connection2.setRequestMethod("GET");
                connection2.connect();
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                }
                String result = new String(outputStream.toByteArray());
                getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                moodlnnlist.add
                                        (new Mood(Integer.parseInt(model.get("moodId").toString()),
                                                model.get("moodImage").toString(),
                                                model.get("moodSheying").toString(),
                                                model.get("moodSheyingzhe").toString(),
                                                model.get("moodMood").toString(),
                                                model.get("moodDate").toString(),
                                                model.get("moodAddress").toString(),
                                                model.get("moodAuthor").toString(),
                                                Integer.parseInt(model.get("moodUserId").toString())
                                        ));
                            }
                        }
                        initListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
            }
        });

    }
}
