package com.example.lotday2.slice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lotday2.HelpTool;
import com.example.lotday2.ResourceTable;
import com.example.lotday2.bean.Diarybook;
import com.example.lotday2.bean.Diary;
import com.example.lotday2.provider.DiaryBookItemProvider;
import com.example.lotday2.provider.DiaryItemProvider;
import com.example.lotday2.provider.DiaryPageSliderProvider;
import com.example.lotday2.utils.ThreadPoolUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.PopupDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.WindowManager;
import ohos.data.DatabaseHelper;
import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;
import ohos.data.rdb.StoreConfig;
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
import java.util.List;
import java.util.Optional;

public class DiaryAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    TabList tabList;
    Button Home,Diarybtn,Time,Mine;
    CommonDialog bookdialog,diarypreviewdialog,bookupdialog;
    int bookid,diaryid,bookuserid;
    String bookname,bookimage,booktype,diarybookupda_imageurl;
    DiaryBookItemProvider diaryBookItemProvider;
    CommonDialog pikerDialog,imagepikerDialog,pikerimageDialog;
    private PopupDialog popupDialog;
    PageSlider pageSlider;
    Image bookbtn;
    HelpTool ht;  //引入工具类
    ArrayList<Diarybook> booklist = new ArrayList<>();
    ArrayList<Diary> diarylist = new ArrayList<>();
    int Firstbookid;
    String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker1.jpg";
    String b = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagpiker5.jpg";
    String c = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker4.jpg";
    String d = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepiker3.jpg";
    String e = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/iamgepiker6.jpg";
    String f = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/LotDay/imagepike2.jpg";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_diary);
        ht = new HelpTool(getApplicationContext());
//        getWindow().setStatusBarColor(Color.getIntColor(new MineAbilitySlice().windowcardcolor));

        initSystemUi();
        initComponents();

        Image topbaradd = findComponentById(ResourceTable.Id_diaryadd_xin);
        topbaradd.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                PopupDialog popupDialog = new PopupDialog(getContext(), topbaradd,300,200);
                RadioContainer radioContainer = new RadioContainer(getContext());
                    RadioButton radioButton1 = new RadioButton(getContext());
                radioButton1.setText("创建日记本");
                radioButton1.setWidth(260);
                radioButton1.setHeight(60);
                radioButton1.setTextSize(40);
                    ShapeElement bg = new ShapeElement();
                    bg.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#007DFF")));
                    bg.setCornerRadius(30);
                radioButton1.setBackground(bg);
                radioButton1.setTextColorOn(Color.BLACK);
                radioButton1.setTextColorOff(Color.WHITE);
                radioButton1.setMarginsLeftAndRight(10, 10);
                radioButton1.setMarginsTopAndBottom(10, 10);
                radioButton1.setTextAlignment(TextAlignment.LEFT);
                radioContainer.addComponent(radioButton1);
                RadioButton radioButton2 = new RadioButton(getContext());
                radioButton2.setText("添加日记");
                radioButton2.setWidth(260);
                radioButton2.setHeight(60);
                radioButton2.setTextSize(40);
                ShapeElement bg2 = new ShapeElement();
                bg2.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#007DFF")));
                bg2.setCornerRadius(30);
                radioButton2.setBackground(bg2);
                radioButton2.setTextColorOn(Color.BLACK);
                radioButton2.setTextColorOff(Color.WHITE);
                radioButton2.setMarginsLeftAndRight(10, 10);
                radioButton2.setMarginsTopAndBottom(10, 10);
                radioButton2.setTextAlignment(TextAlignment.LEFT);
                radioContainer.addComponent(radioButton2);
                radioContainer.setPadding(10, 10, 10, 10);
                radioContainer.setMarkChangedListener((Container, index) -> {
                    radioButton1.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            Intent i = new Intent();
                            present(new DiaryBookAddSlice(), i);
                            popupDialog.destroy();
                        }
                    });
                    radioButton2.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            Intent i = new Intent();
                            present(new DiaryAddSlice(), i);
                            popupDialog.destroy();
                            tabList.selectTabAt(1);
                        }
                    });
                });
                popupDialog.setCustomComponent(radioContainer);
                popupDialog.setHasArrow(true);
                popupDialog.setAutoClosable(true);
                popupDialog.setMode(LayoutAlignment. BOTTOM| LayoutAlignment.RIGHT);
                popupDialog.setBackColor(new Color(Color.getIntColor("#F0F0F0")));
                popupDialog.setCornerRadius(30);
                popupDialog.show();
            }
        });
        //初始化Tab
        tabList = findComponentById(ResourceTable.Id_Diary_tab_list);
        String[] tablistTags = { "日","记"};
        for (int i=0;i<tablistTags.length;i++){
            TabList.Tab tab = tabList.new Tab(this);
            tab.setText(tablistTags[i]);
            tabList.addTab(tab);
        }
        //初始化PageSlider
        //1、创建适配器
        //2、初始化PageSlider
        List<Integer> layoutFieIds = new ArrayList<>();
        layoutFieIds.add(ResourceTable.Layout_ability_diary_rijiben);
        layoutFieIds.add(ResourceTable.Layout_ability_diary_shijianxian);
        pageSlider = findComponentById(ResourceTable.Id_Diary_page_slider);
        pageSlider.setProvider(new DiaryPageSliderProvider(layoutFieIds,this));
        //3、Tablist与Pageslider联动
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                //获取点击菜单的索引
                int index = tab.getPosition();
                //设置pageslider的索引与菜单索引一致
                pageSlider.setCurrentPage(index);
                if(index == 0){
                    //日记本
                    initdiarybook(pageSlider);

                }else if (index == 1){
                    //时间线
                    initdiarytime(pageSlider);
                }
            }

            @Override
            public void onUnselected(TabList.Tab tab) { }

            @Override
            public void onReselected(TabList.Tab tab) { }
        });
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {
            }
            @Override
            public void onPageSlideStateChanged(int i) {
            }
            @Override
            public void onPageChosen(int i) {
                //参数i就是表单当前pageSlidedr的索引
                if(tabList.getSelectedTabIndex() != i){
                    tabList.selectTabAt(i);
                }
            }
        });
        //4、tablist默认选中第二个菜单，加载PageSlider的第一个页面（默认）

        tabList.selectTabAt(1);

    }
    private void initSystemUi() {
        // 状态栏设置为透明
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        // 导航栏 ActionBar
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_NAVIGATION);
        // 全屏
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
    }

    @Override//listcontainer数据更新
    protected void onActive() {
        super.onActive();
        diarylist.clear(); //先清空
        getDiaryData(); //加载数据库数据
        booklist.clear(); //先清空
        getBookData(); //加载数据库数据
    }
//日记pageslider
    private void initdiarytime(PageSlider pageSlider) {
        initDiaryListContainer();
    }
//日记本pageslider
    public void initdiarybook(PageSlider pageSlider) {
        initBookListContainer();
    }
//日记列表
    private void initDiaryListContainer() {
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer diarylistContainer = findComponentById(ResourceTable.Id_diarylistcontainer);
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        DiaryItemProvider diaryItemProvider = new DiaryItemProvider(diarylist,this);
        diarylistContainer.setItemProvider(diaryItemProvider);
        //日记列表点击事件
        diarylistContainer.setItemClickedListener((container, component, position, id) -> {
            Diary diaryitem = (Diary) diarylistContainer.getItemProvider().getItem(position);

            DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_ability_diary_peview, null, false);
            Text text1 = layout.findComponentById(ResourceTable.Id_diary_preview_title);
            Text text2 = layout.findComponentById(ResourceTable.Id_diary_preview_content);
            Text text3 = layout.findComponentById(ResourceTable.Id_diary_preview_topbar_book);
            text1.setText(diaryitem.getDiaryTitle());
            text2.setText(diaryitem.getDiaryContent());
            text3.setText(String.valueOf(diaryitem.getDiaryDiarybookId()));
            Image back =layout.findComponentById(ResourceTable.Id_diary_preview_back);
            back.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    diarypreviewdialog.destroy();
                }
            });
            diarypreviewdialog = new CommonDialog(layout.getContext());
            diarypreviewdialog.setSize(1080, 2340);
            diarypreviewdialog.setCornerRadius(30);
            diarypreviewdialog.setContentCustomComponent(layout);
            diarypreviewdialog.show();
        });
    }
//日记本列表
    private void initBookListContainer() {

        //渲染数据：通过接口从后台获取数据，并显示到abilit_diary_rijiben.xml布局文件的组件中
        //渲染列表，将获取到的日记本数据按照Item的布局，显示到列表中ListContainer
        ListContainer listContainer = findComponentById(ResourceTable.Id_diary_diarybook_listcontainer);
//        List<DiaryBook> data = getData();
        //数据适配器，将data中的一条diarybook信息，渲染到一个Item的布局视图，再将渲染后的Item载入到ListContainer列表中
        diaryBookItemProvider = new DiaryBookItemProvider(booklist,this);
        listContainer.setItemProvider(diaryBookItemProvider);

        //日记本长按事件
        listContainer.setItemLongClickedListener((container, component, position, id) -> {
            Diarybook item = (Diarybook) listContainer.getItemProvider().getItem(position);
//            DiaryBook diaryBook = diaryBookDao.queryDiaryBookById(item.getDiarybook_id());
            bookid = item.getDiarybookId();
            bookname = item.getDiarybookName();
            diarybookupda_imageurl = item.getDiarybookImage();
            booktype = item.getDiarybookType();
            bookuserid= item.getDiarybookUserId();

            //内容对话框
            bookdialog = new CommonDialog(getContext());
            bookdialog.setContentText("更多操作");
            bookdialog.setSize(700,200);
            bookdialog.setCornerRadius(36.0f);
            bookdialog.setButton(IDialog.BUTTON1, "修改", (iDialog, i) -> {
                bookdialog.destroy();

                Component container2 = LayoutScatter.getInstance(getContext())
                        .parse(ResourceTable.Layout_ability_diarybookupdata,null,false);

                    TextField bookupdata_name = container2.findComponentById(ResourceTable.Id_bookupdata_name);
                bookupdata_name.setText(bookname);
//                    TextField tel = (TextField) container.findComponentById(ResourceTable.Id_tel);
//                    tel.setText(contact.getTelPhone());
                    Button confirm_button = container2.findComponentById(ResourceTable.Id_confirm_button);
                    Button image = container2.findComponentById(ResourceTable.Id_diarybookupdata_image);
                    image.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            pikerimageDialog = new CommonDialog(getContext());

//        pikerDialog.setAutoClosable(true);
                            pikerimageDialog.setSize(666, 666);
                            pikerimageDialog.setCornerRadius(30);
                            pikerimageDialog.setContentText("请输入图片网址：");
                            pikerimageDialog.setButton(IDialog.BUTTON1, "选择系统图片", (iDialog, i) -> imagepiker());
//                pikerimageDialog.setContentCustomComponent(DiaryAddSlice);
                            pikerimageDialog.show();
                        }
                    });
                    confirm_button.setClickedListener(new Component.ClickedListener() {
                        @Override
                        public void onClick(Component component) {
                            String bookupname = bookupdata_name.getText();
                            NetManager netManager = NetManager.getInstance(null);
                            if (!netManager.hasDefaultNet()) {
                                return;
                            }
                            ThreadPoolUtil.submit(() -> {
                                NetHandle netHandle = netManager.getDefaultNet();
                                HttpURLConnection connection = null;
                                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                                    URL url = new URL(ht.URL+"/DiaryBook/diarybookupdata.php?diarybook_id="+bookid+"&diarybook_name="+bookupname
                                            +"&diarybook_image="+diarybookupda_imageurl+"&diarybook_type="+booktype+"&diarybook_user_id="+bookuserid
                                    );
                                    URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                                    if (urlConnection instanceof HttpURLConnection) {
                                        connection = (HttpURLConnection) urlConnection;
                                    }
                                    connection.setRequestMethod("GET");
                                    connection.connect();
                                    try (InputStream inputStream = urlConnection.getInputStream()) {
                                        byte[] cache = new byte[2 * 1024];
                                        int len = inputStream.read(cache);
                                        while (len != -1) {
                                            outputStream.write(cache, 0, len);
                                            len = inputStream.read(cache);
                                        }
                                    } catch (IOException e) {
                                        //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                                    }
                                    String result = new String(outputStream.toByteArray());
                                    getUITaskDispatcher().asyncDispatch(new Runnable() {
                                        @Override
                                        public void run() {
                                            JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                                            if(Json.size()== 0){
                                                new ToastDialog(DiaryAbilitySlice.this)
                                                        .setText("修改失败")
                                                        .setAlignment(LayoutAlignment.CENTER)
                                                        .show();
                                            }else
                                            if (Json.size()>0){
                                                new ToastDialog(DiaryAbilitySlice.this)
                                                        .setText("修改成功")
                                                        .setAlignment(LayoutAlignment.CENTER)
                                                        .show();
//                                        Intent i = new Intent();
//                                        Operation operation = new Intent.OperationBuilder()
//                                                .withDeviceId("")
//                                                .withBundleName("com.example.lotday2")
//                                                .withAbilityName("com.example.lotday2.DiaryAbility")
//                                                .build();
//                                        i.setOperation(operation);
//                                        startAbility(i);
                                            }
                                        }
                                    });
                                    HttpResponseCache.getInstalled().flush();
                                } catch (IOException e) {
                                    //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                                }
                            });
                            bookupdialog.destroy();
                            //刷新列表
                            onActive();
                            initdiarybook(pageSlider);
                        }
                    });
                bookupdialog = new CommonDialog(this);
                bookupdialog.setTitleText("重命名");
                bookupdialog.setAutoClosable(true);
                bookupdialog.setSize(1080, 600);
                bookupdialog.setCornerRadius(30);
                bookupdialog.setContentCustomComponent(container2);
                bookupdialog.show();
            });
            bookdialog.setButton(IDialog.BUTTON2, "删除", (iDialog, i) -> {
                NetManager netManager = NetManager.getInstance(null);
                if (!netManager.hasDefaultNet()) {
                    return;
                }
                ThreadPoolUtil.submit(() -> {
                    NetHandle netHandle = netManager.getDefaultNet();
                    HttpURLConnection connection = null;
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        URL url = new URL(ht.URL+"/DiaryBook/diarybookdelete.php?diarybook_id="+bookid);
                        URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                        if (urlConnection instanceof HttpURLConnection) {
                            connection = (HttpURLConnection) urlConnection;
                        }
                        connection.setRequestMethod("GET");
                        connection.connect();
                        try (InputStream inputStream = urlConnection.getInputStream()) {
                            byte[] cache = new byte[2 * 1024];
                            int len = inputStream.read(cache);
                            while (len != -1) {
                                outputStream.write(cache, 0, len);
                                len = inputStream.read(cache);
                            }
                        } catch (IOException e) {
                            //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                        }
                        String result = new String(outputStream.toByteArray());
                        getUITaskDispatcher().asyncDispatch(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                                if(Json.size()== 0){
                                    new ToastDialog(DiaryAbilitySlice.this)
                                            .setText("删除成功")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }else if (Json.size()>0){
                                    new ToastDialog(DiaryAbilitySlice.this)
                                            .setText("删除失败")
                                            .setAlignment(LayoutAlignment.CENTER)
                                            .show();
                                }

                            }
                        });
                        HttpResponseCache.getInstalled().flush();
                    } catch (IOException e) {
                        //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
                    }
                });
                bookdialog.destroy();
                //刷新列表
                onActive();
                initdiarybook(pageSlider);
            });
            bookdialog.setButton(IDialog.BUTTON3, "关闭", (iDialog, i) -> iDialog.destroy());
            bookdialog.show();
            //对话框透明化
//                Optional<WindowManager.LayoutConfig> configOpt = bookdialog.getWindow().getLayoutConfig();
//                configOpt.ifPresent(config -> {
//                    config.dim = 0.0f;
//                    bookdialog.getWindow().setLayoutConfig(config);
//                });
            return false;
        });

    }

    private void imagepiker() {

        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_image_datepiker, null, false);
        Image piker1 = layout.findComponentById(ResourceTable.Id_imagepiker1);
        Image piker2 = layout.findComponentById(ResourceTable.Id_imagepiker2);
        Image piker3 = layout.findComponentById(ResourceTable.Id_imagepiker3);
        Image piker4 = layout.findComponentById(ResourceTable.Id_imagepiker4);
        Image piker5 = layout.findComponentById(ResourceTable.Id_imagepiker5);
        Image piker6 = layout.findComponentById(ResourceTable.Id_imagepiker6);
        ht.LoadImage(a,piker1,layout.getContext());
        ht.LoadImage(b,piker2,layout.getContext());
        ht.LoadImage(c,piker3,layout.getContext());
        ht.LoadImage(d,piker4,layout.getContext());
        ht.LoadImage(e,piker5,layout.getContext());
        ht.LoadImage(f,piker6,layout.getContext());
        piker1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=a;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=b;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker3.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=c;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker4.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=d;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker5.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=e;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });
        piker6.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                diarybookupda_imageurl="";
                diarybookupda_imageurl=f;
                imagepikerDialog.destroy();
                pikerimageDialog.destroy();
            }
        });


        imagepikerDialog = new CommonDialog(layout.getContext());
        imagepikerDialog.setAutoClosable(true);
        imagepikerDialog.setSize(960, 1450);
        imagepikerDialog.setCornerRadius(30);
        imagepikerDialog.setContentCustomComponent(layout);
        imagepikerDialog.show();
    }

    private void initComponents() {
        Home = findComponentById(ResourceTable.Id_diary_home);
        Diarybtn = findComponentById(ResourceTable.Id_diary_diary);
        Time = findComponentById(ResourceTable.Id_diary_time);
        Mine = findComponentById(ResourceTable.Id_diary_mine);
        Mine = findComponentById(ResourceTable.Id_diary_mine);
        bookbtn = findComponentById(ResourceTable.Id_diary_book);
        DependentLayout topde = findComponentById(ResourceTable.Id_diarytopde);
        topde.setBackground(new MineAbilitySlice().themecolor);
        TabList tabList = findComponentById(ResourceTable.Id_Diary_tab_list);
        tabList.setBackground(new MineAbilitySlice().themecolor);
        DirectionalLayout bottom = findComponentById(ResourceTable.Id_ability_bottom_dir_d);
        bottom.setBackground(new MineAbilitySlice().themecolor);
        Home.setClickedListener(this);
        Diarybtn.setClickedListener(this);
        Time.setClickedListener(this);
        Mine.setClickedListener(this);
        bookbtn.setClickedListener(this);
    }
    @Override
    public void onClick(Component component) {
        if (component == Home){
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MainAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }else if (component == Diarybtn){
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.DiaryAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }else if( component == Time){
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.TimeAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }else if(component == Mine){
            Intent i = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.lotday2")
                    .withAbilityName("com.example.lotday2.MineAbility")
                    .build();
            i.setOperation(operation);
            startAbility(i);
        }else if(component == bookbtn){
            bookpiker();
        }
    }
    private void bookpiker() {
        //气泡框中加入xml布局
        DirectionalLayout layout = (DirectionalLayout) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_book_piker, null, false);
        popupDialog = new PopupDialog(this, layout, DirectionalLayout.LayoutConfig.MATCH_PARENT,
                1080);
        ListContainer listContainer2 = layout.findComponentById(ResourceTable.Id_diary_diarybook_listcontainer);
        DirectionalLayout dir =layout.findComponentById(ResourceTable.Id_bookitemdir);

        DiaryBookItemProvider diaryBookItemProvider2 = new DiaryBookItemProvider(booklist,this);
        listContainer2.setItemProvider(diaryBookItemProvider2);


        listContainer2.setItemClickedListener((container, component2, position, id) -> {
            Diarybook item = (Diarybook) listContainer2.getItemProvider().getItem(position);
            Firstbookid= item.getDiarybookId();
            diarylist.clear(); //先清空
            getSDiaryData(); //加载数据库数据
            initdiarytime(pageSlider);
            popupDialog.destroy();
        });
        popupDialog.setCustomComponent(layout);
        popupDialog.setBackColor(Color.TRANSPARENT);
//            popupDialog.setArrowSize(50, 30);
//            popupDialog.setArrowOffset(100);
        popupDialog.setHasArrow(false);
        popupDialog.setCornerRadius(60);
        popupDialog.setAutoClosable(true);
        popupDialog.showOnCertainPosition(LayoutAlignment.CENTER, 0, 0);
        popupDialog.show();

    }
    public void getBookData() {
        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                String a = "http://lotlyzbolg.oss-cn-shenzhen.aliyuncs.com/img/ww.jpeg";
                URL url = new URL(ht.LOCALHOST_URL+"/DiaryBook/selectByDiarybookUserId?diarybookUserId="+ht.preferences.getInt("userId",0));
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.connect();
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                    //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                booklist.add
                                        (new Diarybook(Integer.parseInt(model.get("diarybookId").toString()),
                                                model.get("diarybookName").toString(),
                                                model.get("diarybookImage").toString(),
                                                model.get("diarybookType").toString(),
                                                Integer.parseInt(model.get("diarybookUserId").toString())
                                        ));
                            }
                        }
                        initBookListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }
    public void getDiaryData() {
        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                URL url = new URL(ht.LOCALHOST_URL+"/Diary/selectByDiaryUserId?diaryUserId="+ht.preferences.getInt("userId",0));
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.connect();
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                    //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                diarylist.add
                                        (new Diary(Integer.parseInt(model.get("diaryId").toString()),
                                                model.get("diaryTitle").toString(),
                                                model.get("diaryContent").toString(),
                                                model.get("diaryImage").toString(),
                                                Integer.parseInt(model.get("diaryDateYear").toString()),
                                                Integer.parseInt(model.get("diaryDateMonth").toString()),
                                                Integer.parseInt(model.get("diaryDateDay").toString()),
                                                Integer.parseInt(model.get("diaryDiarybookId").toString()),
                                                Integer.parseInt(model.get("diaryUserId").toString())
                                        ));
                            }
                        }
                        initDiaryListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }
    public void getSDiaryData() {
        NetManager netManager = NetManager.getInstance(null);
        if (!netManager.hasDefaultNet()) {
            return;
        }

        ThreadPoolUtil.submit(() -> {
            NetHandle netHandle = netManager.getDefaultNet();
            HttpURLConnection connection = null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                URL url = new URL(ht.URL+"/Diary/diaryS.php?diary_user_id="+ht.preferences.getInt("user_id",0)+"&diarybook_id="+Firstbookid);
                URLConnection urlConnection = netHandle.openConnection(url, java.net.Proxy.NO_PROXY);
                if (urlConnection instanceof HttpURLConnection) {
                    connection = (HttpURLConnection) urlConnection;
                }
                connection.setRequestMethod("GET");
                connection.connect();
                try (InputStream inputStream = urlConnection.getInputStream()) {
                    byte[] cache = new byte[2 * 1024];
                    int len = inputStream.read(cache);
                    while (len != -1) {
                        outputStream.write(cache, 0, len);
                        len = inputStream.read(cache);
                    }
                } catch (IOException e) {
                    //    HiLog.error(LABEL_LOG, "%{public}s", "netRequest inner IOException");
                }
                String result = new String(outputStream.toByteArray());
                getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray Json = JSONArray.parseArray(result);   //json数组遍历
                        if(Json.size()>0){
                            for(int i=0;i<Json.size();i++){
                                JSONObject model = Json.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                diarylist.add
                                        (new Diary(Integer.parseInt(model.get("diary_id").toString()),
                                                model.get("diary_title").toString(),
                                                model.get("diary_content").toString(),
                                                model.get("diary_image").toString(),
                                                Integer.parseInt(model.get("diary_date_year").toString()),
                                                Integer.parseInt(model.get("diary_date_month").toString()),
                                                Integer.parseInt(model.get("diary_date_day").toString()),
                                                Integer.parseInt(model.get("diary_diarybook_id").toString()),
                                                Integer.parseInt(model.get("diary_user_id").toString())
                                        ));
                            }
                        }
                        initDiaryListContainer();  //数据适配控件
                    }
                });
                HttpResponseCache.getInstalled().flush();
            } catch (IOException e) {
                //  HiLog.error(LABEL_LOG, "%{public}s", "netRequest IOException");
            }
        });
    }
}
