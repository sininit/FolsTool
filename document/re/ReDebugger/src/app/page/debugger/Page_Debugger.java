package app.page.debugger;

import app.Application;
import app.config;
import app.page.IWindowsInstance;
import app.page.Main;
import app.page.TableManager;
import app.utils.Configuration;
import app.utils.MyConfigurationKeys;
import app.utils.swing.JOptionPaneUtil;
import app.utils.swing.JTableUtil.InitTableParam;
import app.utils.swing.input.InputTextBox;
import top.fols.atri.io.Streams;
import top.fols.atri.lang.Objects;
import top.fols.atri.time.Times;
import top.fols.atri.util.Throwables;
import top.fols.box.net.interconnect.InterconnectSocketSerializeReceiver;
import top.fols.box.reflect.re.*;
import top.fols.box.reflect.re.Re_ZDebuggerClient;
import top.fols.box.reflect.re.Re_ZDebuggerServer;
import top.fols.box.util.encode.HexEncoders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.utils.swing.JTableUtil.initTable;

@SuppressWarnings({"UnnecessaryLocalVariable", "UnusedReturnValue"})
public class Page_Debugger implements IWindowsInstance {
    public Main main;
    private transient boolean init = false;





















    public static class Setting {
        public static class ConfigurationKeys extends MyConfigurationKeys {
            public static final String LAST_ADDRESS           = "LAST_ADDRESS";

            public static final String TABLE_TRACE_WIDTH_LIST = "TABLE_TRACE_WIDTH_LIST";
            public static final String TABLE_OBJ_WIDTH_LIST   = "TABLE_OBJ_WIDTH_LIST";



            public static final String LAST_CODE = "LAST_CODE";
            public static final String LAST_CODE_RESULT = "LAST_CODE_RESULT";
            public ConfigurationKeys() {
                add(LAST_ADDRESS);
                add(TABLE_TRACE_WIDTH_LIST);
                add(TABLE_OBJ_WIDTH_LIST);

                add(LAST_CODE);
                add(LAST_CODE_RESULT);
            }

            static final MyConfigurationKeys CONFIGURATION_KEY = new ConfigurationKeys();
        }
        public static final Configuration settingConfiguration = config.getConfiguration(Setting.class, ConfigurationKeys.CONFIGURATION_KEY);

        public static final Configuration.Element<String> LAST_ADDRESS = settingConfiguration.createElement(ConfigurationKeys.LAST_ADDRESS, String.class);

        public static final Configuration.Element<int[]> TABLE_TRACE_WIDTH_LIST = settingConfiguration.createElement(ConfigurationKeys.TABLE_TRACE_WIDTH_LIST, int[].class);
        public static final Configuration.Element<int[]> TABLE_OBJ_WIDTH_LIST   = settingConfiguration.createElement(ConfigurationKeys.TABLE_OBJ_WIDTH_LIST, int[].class);

        public static final Configuration.Element<String> LAST_CODE = settingConfiguration.createElement(ConfigurationKeys.LAST_CODE, String.class);
        public static final Configuration.Element<String> LAST_CODE_RESULT = settingConfiguration.createElement(ConfigurationKeys.LAST_CODE_RESULT, String.class);
    }
    public void readStyle() {
        try {
            this.setAddressTextFieldString(Setting.LAST_ADDRESS.get());
        } catch (Throwable ignored) {}


        //table size
        try {
            int[] gson = Setting.TABLE_TRACE_WIDTH_LIST.getGson();
            this.FirstTable_Trace.operate.setColumnWidths(gson);
        } catch (Throwable ignored) {}
        try {
            int[] gson = Setting.TABLE_OBJ_WIDTH_LIST.getGson();
            this.TwoTable_Object.operate.setColumnWidths(gson);
        } catch (Throwable ignored) {}
    }
    public void saveStyle() {
        try {
            Setting.LAST_ADDRESS.set(getAddressTextFieldString());
        } catch (Throwable ignored) {}

        //table size
        {
            int[] columnWidths = this.FirstTable_Trace.operate.getColumnWidths();
            Setting.TABLE_TRACE_WIDTH_LIST.setGson(columnWidths);
        }
        {
            int[] columnWidths = this.TwoTable_Object.operate.getColumnWidths();
            Setting.TABLE_OBJ_WIDTH_LIST.setGson(columnWidths);
        }
    }
    @Override
    public boolean save() {
        boolean result = true;
        try {
            saveStyle();
        } catch (Throwable e) {
            e.printStackTrace();
            Application.logln(Throwables.toString(e));
            result = false;
        }
        return result;
    }




    public void initWindowsMenu() {
    }














    public static class FirstTableTraceKey {
        public static class Element extends TableManager.RowElement {
            @IVar(name = TableFinalKey.KEY_EXECUTOR_MESSAGE)
            protected String executorMessage;
            @IVar(name = TableFinalKey.KEY_TRACK_MESSAGE)
            protected String message;

            @IVar(name = HideKeys.KEY_ID)
            protected String id;

            @IVar(name = HideKeys.KEY_EXECUTOR_ID)
            protected String executorID;



            public String getMessage() {return message;}
            public String getId() {return id;}
            public String getExecutorID() {return executorID;}


            public Re_ZDebuggerServer.ObjectID getExecutorObjectID() {return null == executorID?null:new Re_ZDebuggerServer.ObjectID(executorID);}

            Element(){}
            public Element(
                    String executorMessage, String message,
                           String id, String executorID) {
                this.executorMessage = executorMessage;
                this.message = message;

                this.id = id;
                this.executorID = executorID;
            }
        }

        public static final String PRIMARY_KEY = HideKeys.KEY_ID;

        public static class TableFinalKey extends MyConfigurationKeys {
            public static final String KEY_EXECUTOR_MESSAGE = "执行器";
            public static final String KEY_TRACK_MESSAGE = "跟踪";

            public TableFinalKey() {
                super();

                add(KEY_EXECUTOR_MESSAGE);
                add(KEY_TRACK_MESSAGE);
            }

            public static final MyConfigurationKeys CONFIGURATION_KEY = new TableFinalKey();
        }


        public static class HideKeys extends MyConfigurationKeys {
            public static final String KEY_EXECUTOR_ID = "EXECUTOR_ID";
            public static final String KEY_ID          = "ID";
            public HideKeys() {
                super();

                add(KEY_ID);
                add(KEY_EXECUTOR_ID);
            }

            public static final MyConfigurationKeys CONFIGURATION_KEY = new HideKeys();
        }


        public static final MyConfigurationKeys ALL_KEY = new MyConfigurationKeys() {{
            addAll(TableFinalKey.CONFIGURATION_KEY);
            addAll(HideKeys.CONFIGURATION_KEY);
        }};
    }



    InitTableParam FirstTable_Trace;
    public static class TwoTableObjectKey {
        public static class Element extends TableManager.RowElement {
            @IVar(name = TableFinalKey.KEY_KEY)
            String k;

            @IVar(name = TableFinalKey.KEY_VALUE)
            String v;

            @IVar(name = TableFinalKey.KEY_JTYPE)
            String jtype;


            @IVar(name = HideKeys.KEY_SERAL)
            String localID;

            @IVar(name = HideKeys.KEY_KID)
            String kid;

            @IVar(name = HideKeys.KEY_VID)
            String vid;

            @IVar(name = HideKeys.KEY_ORIGIN)
            String source;



            public String getK() {return k;}
            public String getV() {return v;}

            public String getKID() {return kid;}
            public String getVID() {return vid;}

            public Re_ZDebuggerServer.ObjectID getKObjectID() {return null == kid?null:new Re_ZDebuggerServer.ObjectID(kid);}
            public Re_ZDebuggerServer.ObjectID getVObjectID() {return null == vid?null:new Re_ZDebuggerServer.ObjectID(vid);}

            public Re_ZDebuggerServer.GetObjectVariableList.VarElement getVarElement() {
                try {
                    byte[] decode = HexEncoders.decode(source);
                    Object o = Streams.ObjectTool.toObject(decode);
                    return (Re_ZDebuggerServer.GetObjectVariableList.VarElement) o;
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            }


            Element(){}
            public Element(String serialID,
                           String k, String v, String jtype,
                           String kid, String vid,
                           Re_ZDebuggerServer.GetObjectVariableList.VarElement element) {
                this.k  = k;
                this.v  = v;
                this.jtype = jtype;

                this.localID = serialID;

                this.kid = kid;
                this.vid = vid;

                try {
                    this.source = HexEncoders.encodeToString(Streams.ObjectTool.toBytes(element));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public static final String PRIMARY_KEY = HideKeys.KEY_SERAL;

        public static class TableFinalKey extends MyConfigurationKeys {
            public static final String KEY_KEY   = "key";
            public static final String KEY_VALUE = "value";
            public static final String KEY_JTYPE = "jtype";
            public TableFinalKey() {
                super();

                add(KEY_KEY);
                add(KEY_VALUE);
                add(KEY_JTYPE);
            }

            public static final MyConfigurationKeys CONFIGURATION_KEY = new TableFinalKey();
        }


        public static class HideKeys extends MyConfigurationKeys {
            public static final String KEY_SERAL    = "serial";//本地id
            public static final String KEY_KID      = "kid";
            public static final String KEY_VID      = "vid";
            public static final String KEY_ORIGIN   = "source";

            public HideKeys() {
                super();


                add(KEY_SERAL);
                add(KEY_KID);
                add(KEY_VID);
                add(KEY_ORIGIN);
            }

            public static final MyConfigurationKeys CONFIGURATION_KEY = new HideKeys();
        }


        public static final MyConfigurationKeys ALL_KEY = new MyConfigurationKeys() {{
            addAll(TableFinalKey.CONFIGURATION_KEY);
            addAll(HideKeys.CONFIGURATION_KEY);
        }};

    }
    InitTableParam TwoTable_Object;






    @Override
    public void initWindows() throws Throwable {
        if (!this.init) {
            this.initWindowsMenu();

            main.调试页_TableTracePane.getVerticalScrollBar().setUnitIncrement(16);
            initTable(FirstTable_Trace = new InitTableParam(main.调试页_TableTrace, FirstTableTraceKey.PRIMARY_KEY, false) {

                @Override
                public void loadTableManagerData() {
                    tableManager.removeAll();

                    for (String key: FirstTableTraceKey.TableFinalKey.CONFIGURATION_KEY.keySet())
                        tableManager.addColumn(key);

                    /*
                      隐藏列项, 必须 先添加完 必须列，再添加隐藏列
                      添加隐藏项目之后不可再添加 列
                     */
                    for (String header: FirstTableTraceKey.HideKeys.CONFIGURATION_KEY.keySet())
                        if (tableManager.getColumnIndex(header) < 0)
                            tableManager.addHideColumn(header);
                }

                @Override
                public void readStyle() {
                    Page_Debugger.this.readStyle();
                }

                @Override
                public void saveStyle() {
                    Page_Debugger.this.saveStyle();
                }

                @Override
                public void mouseClicked(InitTableParam initTableParam, MouseEvent evt) {
                    int focusedRowIndex = initTableParam.jTable.rowAtPoint(evt.getPoint());
                    if (focusedRowIndex == -1) { return; }

                    try {
                        int button = evt.getButton();
                        switch (button) {
                            case MouseEvent.BUTTON1: {
                                if (evt.getClickCount() == 2) {
                                    TableManager manager = initTableParam.tableManager;
                                    String[] selectRowPrimaryKey = manager.getSelectRowPrimaryKey();
                                    String first = Objects.first(selectRowPrimaryKey);
                                    if (selectRowPrimaryKey.length > 1) {
                                        manager.select(new String[]{first});
                                    }

                                    FirstTableTraceKey.Element rowAsElement = manager.getRowAsElement(first, FirstTableTraceKey.Element.class);

                                    String primaryKey = rowAsElement.getId();
                                    if (!Objects.empty(primaryKey)) {

                                        resetTwoObjectTable();
                                        String executorID = rowAsElement.getExecutorID();
                                        twoTablePage.add(executorID);
                                        loadTwoTableFromObject(new Re_ZDebuggerServer.ObjectID(executorID));
                                    }
                                }
                                break;
                            }
                            case MouseEvent.BUTTON3: {
                                //右击
                                //通过点击位置找到点击为表格中的行
                                //将表格所选项设为当前右键点击的行
                                //table1.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                                //右击
                                createFirstTableMenu().show(jTable, evt.getX(), evt.getY());
                                break;
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }


                @Override public Object getValue(int row, int col) {return tableManager.getValue(row, col);}
                @Override public void setValue(int row, int col, Object oldValue, Object value) {}
            });



            main.调试页_TableObjectPane.getVerticalScrollBar().setUnitIncrement(16);
            initTable(TwoTable_Object = new InitTableParam(main.调试页_TableObject, TwoTableObjectKey.PRIMARY_KEY, false) {

                @Override
                public void loadTableManagerData() throws Throwable {
                    tableManager.removeAll();

                    for (String key: TwoTableObjectKey.TableFinalKey.CONFIGURATION_KEY.keySet())
                        tableManager.addColumn(key);

                    /*
                      隐藏列项, 必须 先添加完 必须列，再添加隐藏列
                      添加隐藏项目之后不可再添加 列
                     */
                    for (String header: TwoTableObjectKey.HideKeys.CONFIGURATION_KEY.keySet())
                        if (tableManager.getColumnIndex(header) < 0)
                            tableManager.addHideColumn(header);
                }

                @Override
                public void readStyle() {
                    Page_Debugger.this.readStyle();
                }

                @Override
                public void saveStyle() {
                    Page_Debugger.this.saveStyle();
                }

                @Override
                public void mouseClicked(InitTableParam initTableParam, MouseEvent evt) {
                    int focusedRowIndex = initTableParam.jTable.rowAtPoint(evt.getPoint());
                    if (focusedRowIndex == -1) { return; }

                    try {
                        int button = evt.getButton();
                        switch (button) {
                            case MouseEvent.BUTTON1: {
                                if (evt.getClickCount() == 2) {
                                    TableManager manager = initTableParam.tableManager;
                                    String[] selectRowPrimaryKey = manager.getSelectRowPrimaryKey();
                                    String first = Objects.first(selectRowPrimaryKey);
                                    if (selectRowPrimaryKey.length > 1) {
                                        manager.select(new String[]{first});
                                    }

                                    joinTwoTableLine(first, false);
                                }
                                break;
                            }
                            case MouseEvent.BUTTON3: {
                                //右击
                                createTwoTableMenu().show(jTable, evt.getX(), evt.getY());
                                break;
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }


                @Override public Object getValue(int row, int col) {return tableManager.getValue(row, col);}
                @Override public void setValue(int row, int col, Object oldValue, Object value) {}
            });



            this.initEvent();
            this.initCheckStackIDThread();

            this.init = true;
        }
    }

    void initCheckStackIDThread() {
        new Thread(() -> {
            while (true) {
                try {
                    if (isLink()) {
                        acceptStackID();
                    } else {
                        setLinkStatusLabelText(false);
                    }
                    Times.sleep(100L);
                } catch (Throwable e) {
                    e.printStackTrace();
                };
            }
        }).start();
    }
























    final Object lock = new Object();
    Re_ZDebuggerClient client;







    public String getAddressTextFieldString() {
        return main.调试页_地址textField.getText();
    }
    public void setAddressTextFieldString(String v) {
        main.调试页_地址textField.setText(v);
    }
    public void setLinkStatusLabelText(String s) {
        main.调试页_状态_链接.setText(s);
    }
    public void setLinkStatusLabelText(boolean isLink) {
        setLinkStatusLabelText(isLink? "已连接": "未连接");
    }
    public String getStackIDTextField() {
       return main.调试页_StackIDTextField.getText();
    }
    public void setStackIDTextField(String id) {
        if (null == id) {
            main.调试页_StackIDTextField.setText("");
        } else {
            main.调试页_StackIDTextField.setText(id);
        }
    }
    public boolean hasStackIDTextField() {
        String stackIDTextField = getStackIDTextField();
        return !(null == stackIDTextField|| "".equals(stackIDTextField));
    }




    public void initEvent() {
        {
            main.调试页_链接Button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        link(getAddressTextFieldString());
                    } catch (Throwable ex) {
                        JOptionPaneUtil.notify(Throwables.toString(ex));
                    }
                }
            });
            main.调试页_断开Button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        disconnected();
                    } catch (Throwable ex) {
                        JOptionPaneUtil.notify(Throwables.toString(ex));
                    }
                }
            });
        }


        {
            main.调试页_恢复Button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        queryRecoveryDebugger();
                    } catch (Throwable ex) {
                        JOptionPaneUtil.notify(Throwables.toString(ex));
                    }
                }
            });
        }
    }




























    /**
     * 是否断开链接
     */
    public boolean isDisconnected() {
        synchronized (lock) {
            if (null == client) {
                return !hasStackIDTextField();
            }
            return false;
        }
    }

    /**
     * 断开链接
     */
    public void disconnected() {
        synchronized (lock) {
            try {
                if (null != client) {
                    client.close();
                }
                resetAllTable();
            } finally {
                client = null;
            }
        }
    }

    /**
     * 是否已连接 并且通道正常
     */
    public boolean isLink() {
        synchronized (lock) {
            if (null == client) {
                return false;
            }
            return client.isConnect();
        }
    }

    public void link(String link) throws IOException {
        synchronized (lock) {
            String address;
            int port;
            if (Objects.isDigit(link)) {
                address = null;
                port = Objects.get_int(link);
            } else {
                String[] split = link.split(":");
                address = split[0];
                port = Objects.get_int(split[1]);
            }

            if (isLink()) {
                disconnected();
            }

            if (null == address) {
                client = createClient(port);
            } else {
                client = createClient(InetAddress.getByName(address), port);
            }
            setLinkStatusLabelText(true);
        }
    }


    Re_ZDebuggerClient createClient(int port) throws UnknownHostException {
        return createClient(InetAddress.getLocalHost(), port);
    }
    Re_ZDebuggerClient createClient(java.net.InetAddress inetAddress,
                                   int port) {
        return new Re_ZDebuggerClient(inetAddress, port);
    }



    public JPopupMenu createFirstTableMenu() {
        JPopupMenu menu =new JPopupMenu();

        JMenuItem jMenuItem;
        JSeparator separator;

        jMenuItem = new JMenuItem();
        jMenuItem.setText("运行");
        jMenuItem.addActionListener(evt -> {
            TableManager manager = FirstTable_Trace.tableManager;
            String[] selectRowPrimaryKey = manager.getSelectRowPrimaryKey();
            String first = Objects.first(selectRowPrimaryKey);
            if (selectRowPrimaryKey.length == 1) {
                manager.select(new String[]{first});
            } else {
                JOptionPaneUtil.notify("只能选择一项");
                return;
            }

            try {
                runExecutorCode(first);
            } catch (Throwable e) {
                JOptionPaneUtil.notify(Throwables.toString(e));
            }
        });
        menu.add(jMenuItem);


        separator = new JSeparator();
        menu.add(separator);

        return menu;
    }

    public JPopupMenu createTwoTableMenu() {
        JPopupMenu menu =new JPopupMenu();

        JMenuItem jMenuItem;
        JSeparator separator;

        jMenuItem = new JMenuItem();
        jMenuItem.setText("编辑");
        jMenuItem.addActionListener(evt -> {
            TableManager manager = TwoTable_Object.tableManager;
            String[] selectRowPrimaryKey = manager.getSelectRowPrimaryKey();
            String first = Objects.first(selectRowPrimaryKey);
            if (selectRowPrimaryKey.length == 1) {
                manager.select(new String[]{first});
            } else {
                JOptionPaneUtil.notify("只能选择一项");
                return;
            }

            try {
                joinTwoTableLine(first, true);
            } catch (Throwable e) {
                JOptionPaneUtil.notify(Throwables.toString(e));
            }
        });
        menu.add(jMenuItem);


        separator = new JSeparator();
        menu.add(separator);

        return menu;
    }


    /**
     * 清空所有表数据 包括状态
     */
    void resetAllTable() {
        try {
            this.resetFirstTraceTable();
            this.resetTwoObjectTable();
        } finally {
            setLinkStatusLabelText(false);
            setStackIDTextField(null);
        }
    }
    void resetFirstTraceTable() {this.removeFirstTraceTable();}
    void removeFirstTraceTable() {
        TableManager tableManager;

        tableManager = this.FirstTable_Trace.tableManager;
        tableManager.removeAllRow();
    }

    void resetTwoObjectTable() {
        this.removeTwoObjectTableLiens();
        exitTwoTable();
    }
    void removeTwoObjectTableLiens() {
        TableManager tableManager;

        tableManager = this.TwoTable_Object.tableManager;
        tableManager.removeAllRow();
    }


    Re_ZDebuggerServer.GetObjectData.ObjectData queryExecute(Re_ZDebuggerServer.IGetObjectID executor, String expression) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        return client.setObjectVariableValueFromCodeResult(executor, Re_Variable.Null.get(), expression);
    }
    private  Boolean  queryRecoveryDebugger() throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        return client.recoveryDebuggerStack();
    }
    private  Re_ZDebuggerServer.GetObjectData.ObjectData  queryObjectData(Re_ZDebuggerServer.IGetObjectID id) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        return client.getObjectData(id);
    }
    private  Re_ZDebuggerServer.GetObjectVariableList.VarElementList  queryObjectVariableList(Re_ZDebuggerServer.IGetObjectID id) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        return client.getObjectVariableList(id);
    }
    private  Re_ZDebuggerServer.GetObjectData.ObjectData  queryObjectVariableSet(Re_ZDebuggerServer.IGetObjectID id, Re_ZDebuggerServer.IGetObjectID key, String expression) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        return client.setObjectVariableValueFromCodeResult(id, key, expression);
    }
    private  Re_ZDebuggerServer.GetObjectData.ObjectData  queryObjectVariableSet(Re_ZDebuggerServer.IGetObjectID id,
                                                                                Object key, Re_ZDebuggerServer.IGetObjectID keyID,
                                                                                String expression) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        if (null == keyID || Objects.empty(keyID.getID())) {
            return client.setObjectVariableValueFromCodeResult(id, key, expression);
        } else {
            return client.setObjectVariableValueFromCodeResult(id, keyID, expression);
        }
    }


    /**
     * 自动检测栈id是否改变，如果改变则重新加载表
     */
    public void acceptStackID() throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        Re_ZDebuggerServer.FindDebuggerStack.NativeStack debuggerStack = client.findDebuggerStack();

        String id = null == debuggerStack ? null : debuggerStack.getID();
        String lastID = getStackIDTextField();

        try {
            if (!Objects.equals(id, lastID)) {
                resetAllTable();

                if (null != debuggerStack) {
                    loadFirstTableStack(debuggerStack);
                }
            }
        } finally {
            setLinkStatusLabelText(true);
            setStackIDTextField(id);
        }
    }
    public boolean hasFirstTableStack(FirstTableTraceKey.Element element){
        if (null != element) {
            String id = element.getId();
            return FirstTable_Trace.tableManager.getRowIndex(id) >= 0;
        }
        return false;
    }
    public void loadFirstTableStack(Re_ZDebuggerServer.FindDebuggerStack.NativeStack debuggerExecutorObjectID) {
        resetAllTable();

        TableManager TableTraceManager = this.FirstTable_Trace.tableManager;

        for (Re_ZDebuggerServer.FindDebuggerStack.NativeStackElement nativeStackElement : debuggerExecutorObjectID.getNativeStackElements()) {
            TableTraceManager.addRowElement(new FirstTableTraceKey.Element(
                    nativeStackElement.getNativeStackElementExecutorAsString(),
                    nativeStackElement.getNativeStackElementAsString(),

                    nativeStackElement.getNativeStackElementID(),
                    nativeStackElement.getExecutorID()
            ));
        }
    }
    public void runExecutorCode(String keyid) throws Throwable {
        TableManager manager = FirstTable_Trace.tableManager;
        FirstTableTraceKey.Element rowAsElement = manager.getRowAsElement(keyid, FirstTableTraceKey.Element.class);

        Page_Debugger_Executor.newInstance(main, rowAsElement);
    }







    public static final String TWO_TABLE_FINAL_VAR_KEY_PREV = "..";
    public static final String TWO_TABLE_FINAL_VAR_KEY_CURRENT = ".";



    private final List<String> twoTablePage = new ArrayList<>();
    private void loadTwoTableFromObject(Re_ZDebuggerServer.IGetObjectID id) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        removeTwoObjectTableLiens();

        Re_ZDebuggerServer.GetObjectData.ObjectData objectData = queryObjectData(id);
        Re_ZDebuggerServer.GetObjectVariableList.VarElementList varElementList = queryObjectVariableList(id);

        TableManager tableManager = this.TwoTable_Object.tableManager;

        {
            String ks = TWO_TABLE_FINAL_VAR_KEY_PREV;
            String vjtype = TWO_TABLE_FINAL_VAR_KEY_PREV;
            String vs = ">> " + "" + twoTablePage.size();

            TwoTableObjectKey.Element element = new TwoTableObjectKey.Element(
                    UUID.randomUUID().toString(),
                    ks, vs, vjtype,
                    null, null,
                    null);
            tableManager.addRowElement(element);
        }

        {
            String ks = TWO_TABLE_FINAL_VAR_KEY_CURRENT;
            String vjtype = objectData.getJavaClass();
            String vs = ">> " + objectData.getName();

            TwoTableObjectKey.Element element = new TwoTableObjectKey.Element(
                    UUID.randomUUID().toString(),
                    ks, vs, vjtype,
                    null, objectData.getID(),
                    null);
            tableManager.addRowElement(element);
        }



        Re_ZDebuggerServer.GetObjectVariableList.VarElement[] data = varElementList.getData();
        for (Re_ZDebuggerServer.GetObjectVariableList.VarElement varElement : data) {
            Re_ZDebuggerServer.GetObjectData.ObjectData valueData = varElement.getValueData();

            String ks = varElement.getKeyToString();
            String vjtype = valueData.getJavaClass();
            String vs;
            if (valueData.isBaseType()) {
                vs = String.valueOf(valueData.getValueFromBaseType());
            } else {
                vs = ">> " + valueData.getName();
            }

            String kid = varElement.getKeyID().getID();
            String vid = varElement.getValueID().getID();

            TwoTableObjectKey.Element element = new TwoTableObjectKey.Element(
                    UUID.randomUUID().toString(),
                    ks, vs, vjtype,
                    kid, vid,
                    varElement);
            tableManager.addRowElement(element);
        }
    }
    private void reloadTwoTable() throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        Re_ZDebuggerServer.IGetObjectID twoTablePage = getTwoTableCurrentID();
        if (null == twoTablePage) {
            return;
        }
        loadTwoTableFromObject(twoTablePage);
    }
    private Re_ZDebuggerServer.IGetObjectID getTwoTableCurrentID() {
        int size = twoTablePage.size();
        if (size > 0) {
            return new Re_ZDebuggerServer.ObjectID(twoTablePage.get(size - 1));
        }
        return null;
    }
    private void gotoPrevTwoTable() throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        int size = twoTablePage.size();
        if (size <= 1) {
            return;
        }
        int current = size - 1;
        int prev = current - 1;
        String s = twoTablePage.get(prev);//倒数第二个
        twoTablePage.remove(current);

        loadTwoTableFromObject(new Re_ZDebuggerServer.ObjectID(s));
    }

    private void exitTwoTable() {
        twoTablePage.clear();
    }


    /**
     * @param keyid 选择行
     * @param isForceEdit (如果为真则可以编辑所有变量)，(如果为假只编辑基础类型,如果非基本类型则载入变量表)
     */
    public void joinTwoTableLine(String keyid, boolean isForceEdit) throws InterconnectSocketSerializeReceiver.RemoteThrowException, IOException, ClassNotFoundException {
        TableManager manager = TwoTable_Object.tableManager;
        TwoTableObjectKey.Element rowAsElement = manager.getRowAsElement(keyid, TwoTableObjectKey.Element.class);
        Re_ZDebuggerServer.GetObjectVariableList.VarElement source = rowAsElement.getVarElement();

        Object keyFromBaseType      = null == source ? null :  source.getKeyFromBaseType();

        Object valueFromBaseType    = null == source ? null :  source.getValueData().getValueFromBaseType();
        String valueAsDeclareString = Re_CodeLoader._CompileTimeCodeSourceReader.getBaseDataToDeclareString(valueFromBaseType);

        String k = rowAsElement.getK();
        String vid = rowAsElement.getVID();
        Re_ZDebuggerServer.IGetObjectID twoTableCurrentID = getTwoTableCurrentID();

        if (Objects.empty(vid)) {
            //后退
            if (TWO_TABLE_FINAL_VAR_KEY_PREV.equals(k)) {
                gotoPrevTwoTable();
            } else {
                extracted(rowAsElement, keyFromBaseType, valueAsDeclareString, k, twoTableCurrentID);
            }
        } else if (TWO_TABLE_FINAL_VAR_KEY_CURRENT.equals(k)) {
            //刷新
            reloadTwoTable();
        } else {
            //进入
            if (isForceEdit) {
                extracted(rowAsElement, keyFromBaseType, valueAsDeclareString, k, twoTableCurrentID);
            } else {
                twoTablePage.add(vid);
                loadTwoTableFromObject(new Re_ZDebuggerServer.ObjectID(vid));
            }
        }
    }

    private void extracted(TwoTableObjectKey.Element rowAsElement, Object keyFromBaseType, String valueAsDeclareString, String k, Re_ZDebuggerServer.IGetObjectID twoTableCurrentID) {
        InputTextBox inputTextBox = InputTextBox.newInstance();
        inputTextBox
                .title("修改：" + k + "（代码）")
                .append(valueAsDeclareString)
                .setConfirmAbstractAction(expression -> {
                    try {
                        Re_ZDebuggerServer.GetObjectData.ObjectData objectData = queryObjectVariableSet(
                                twoTableCurrentID,
                                keyFromBaseType, rowAsElement.getKObjectID(),
                                expression);
                        if (Objects.equals(twoTableCurrentID, getTwoTableCurrentID())) {
                            reloadTwoTable();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        JOptionPaneUtil.notify(Throwables.toString(e));
                    }
                })
                .size(620,320)
                .create();
    }


}
