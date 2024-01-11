package hierachyconfig;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hierachyconfig.DefaultColor.*;

@Data
@State(name = "MyConfigurable", storages = @Storage("hierachy.xml"))
public class MyConfigurable implements PersistentStateComponent<MyConfigurable>, Disposable {


    String param = "hello";

    int clusterBgColor = 0xffff00;
    int clusterFontColor = 0x111111;
    int tableBgColor = 0x222222;
    int tableFontColor = 0x333333;
    int fieldBgColor = 0x444444;
    int fieldsFontColor = 0x41ff57;
    int arrowColor = 0x666666;

    boolean HiveLayerColor = false;
    int STGBG = 0x00ff00;
    int STGFont = 0x00ff00;
    int ODSBG = 0x00ff00;
    int ODSFont = 0x00ff00;
    int DWDBG = 0x00ff00;
    int DWDFont = 0x00ff00;
    int DWMBG = 0x00ff00;
    int DWMFont = 0x00ff00;
    int APPBG = 0x00ff00;
    int APPFont = 0x00ff00;
    int DIMBG = 0x00ff00;
    int DIMFont = 0x00ff00;

    int MYSQLBG = TURQUOISE_100;
    int MYSQLFONT = GREENSEA_100;
    int HIVEFONT = EMERALD_100;
    int HIVEBG = NEPHRITIS_100;
    int CLICKHOUSEBG = peterriver100;

    int CLICKHOUSEFONT = SUNFLOWER_100;
    int DataSetBG = ORANGE_100;
    int DATASETFONT = CARROT_100;
    int DASHBOARDBG = PUMPKIN_100;
    int DASHBOARDFONT = ALIZARIN_100;


    int color1 = SUNFLOWER_50;
    int color2 = SUNFLOWER_100;
    int color3 = ORANGE_50;
    int color4 = ORANGE_100;



    boolean DownStream = true;
    boolean UpStream = true;
    boolean fieldsShow = true;

    String hierachyDepth = "5";

    boolean batchFieldHierachy = false;
    boolean openAfterGen = true;
    String outputPath = "/Users/zhangshengshan/Desktop";


    boolean downloadAfterExtract = false;

    boolean genScoutCode = false;

    String tennonShowMode = "Simple";

    boolean hiveOnlyHierachy = false;

    public static MyConfigurable getInstance() {
        return ApplicationManager.getApplication().getService(MyConfigurable.class);
    }

    @Override
    public void dispose() {
        return;
    }

    @Override
    public @Nullable MyConfigurable getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MyConfigurable state) {
        XmlSerializerUtil.copyBean(state, this);
    }


}

