package hierachyconfig;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author zhangshengshan
 */
public class MyConfigUI implements ConfigurableUi<MyConfigurable> {
    private JPanel panel1;
    private JTextField textField1;
    private JButton ColorSchemeSelect;
    private JButton FieldsFontColorButton;
    private JButton FieldBgColorButton;
    private JButton ClusterFontColorButton;
    private JPanel colorConfig;
    private JPanel shapeConfig;
    private JPanel relationConfig;
    private JButton ClusterBgColorButton;
    private JButton TableBgColorButton;
    private JButton ArrowColorButton;
    private JButton TableFontColor;
    private JCheckBox randomColorCheckBox;
    private JCheckBox uniqKeyDisplay;
    private JCheckBox fieldsDisplay;
    private JComboBox tableSize;
    private JComboBox fieldSize;
    private JComboBox arrowSize;
    private JComboBox tableShape;
    private JComboBox fieldsShape;
    private JComboBox arrowShape;
    private JTabbedPane tabbedPane1;
    private JButton DASHBOARDFONTButton;
    private JButton DATASETFONTButton;
    private JButton CLICKHOUSEFONTButton;
    private JButton HIVEFONTButton;
    private JButton MYSQLFONTButton;
    private JButton MYSQLBGButton;
    private JButton HIVEBGButton;
    private JButton CLICKHOUSEBGButton;
    private JButton dataSetBGButton;
    private JButton DASHBOARDBGButton;
    private JCheckBox upStreamCheckBox;
    private JCheckBox downStreamCheckBox;
    private JCheckBox colorAdvanceCheckBox;
    private JCheckBox hiveLayerColorCheckBox;
    private JButton STGBGButton;
    private JButton ODSBGButton;
    private JButton DWDBGButton;
    private JButton DWMBGButton;
    private JButton APPBGButton;
    private JButton DIMBGButton;
    private JCheckBox filterCheckBox;
    private JButton filterBGButton;
    private JButton filterFontButton;
    private JTextField textField2;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton keyWord1BG;
    private JButton KeyWord2BG;
    private JButton KeyWord3BG;
    private JButton STGFontButton;
    private JButton ODSFontButton;
    private JButton DWDFontButton;
    private JButton DWMFontButton;
    private JButton APPFontButton;
    private JButton DIMFontButton;
    private JButton colorRestButton;
    private JComboBox hierachyDepth;
    private JRadioButton batchFieldHierachy;
    private JRadioButton OpenAfterGen;
    private JTextField outputPath;
    private JRadioButton downloadAfterExtractRadioButton;
    private JRadioButton genScoutCmdsRadioButton;
    private JComboBox tennonModeSelector;
    private JRadioButton OnlyHiveTable;


    private JButton color1Button;
    private JButton color2Button;
    private JButton color3Button;
    private JButton color4Button;


    public MyConfigUI(MyConfigurable settings) {

        ClusterBgColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            ClusterBgColorButton.setBackground(color);
            ClusterBgColorButton.setForeground(color);
        });
        ClusterFontColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            ClusterFontColorButton.setBackground(color);
            ClusterFontColorButton.setForeground(color);
        });

        TableBgColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            TableBgColorButton.setBackground(color);
            TableBgColorButton.setForeground(color);
        });
        TableFontColor.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            TableFontColor.setBackground(color);
            TableFontColor.setForeground(color);
        });
        FieldBgColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            FieldBgColorButton.setBackground(color);
            FieldBgColorButton.setForeground(color);
        });
        FieldsFontColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            FieldsFontColorButton.setBackground(color);
            FieldsFontColorButton.setForeground(color);
        });
        ArrowColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            ArrowColorButton.setBackground(color);
            ArrowColorButton.setForeground(color);
        });
        STGBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            STGBGButton.setBackground(color);
            STGBGButton.setForeground(color);
        });

        ODSBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            ODSBGButton.setBackground(color);
            ODSBGButton.setForeground(color);
        });
        DWDBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DWDBGButton.setBackground(color);
            DWDBGButton.setForeground(color);
        });
        DWMBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DWMBGButton.setBackground(color);
            DWMBGButton.setForeground(color);
        });
        APPBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            APPBGButton.setBackground(color);
            APPBGButton.setForeground(color);
        });
        DIMBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DIMBGButton.setBackground(color);
            DIMBGButton.setForeground(color);
        });
        filterBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            filterBGButton.setBackground(color);
            filterBGButton.setForeground(color);
        });
        filterFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            filterFontButton.setBackground(color);
            filterFontButton.setForeground(color);
        });

        keyWord1BG.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            keyWord1BG.setBackground(color);
            keyWord1BG.setForeground(color);
        });

        KeyWord2BG.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            KeyWord2BG.setBackground(color);
            KeyWord2BG.setForeground(color);
        });

        KeyWord3BG.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            KeyWord3BG.setBackground(color);
            KeyWord3BG.setForeground(color);
        });

        MYSQLFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            MYSQLFONTButton.setBackground(color);
            MYSQLFONTButton.setForeground(color);
        });
        HIVEFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            HIVEFONTButton.setBackground(color);
            HIVEFONTButton.setForeground(color);
        });
        CLICKHOUSEFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            CLICKHOUSEFONTButton.setBackground(color);
            CLICKHOUSEFONTButton.setForeground(color);
        });
        DATASETFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DATASETFONTButton.setBackground(color);
            DATASETFONTButton.setForeground(color);
        });
        DASHBOARDFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DASHBOARDFONTButton.setBackground(color);
            DASHBOARDFONTButton.setForeground(color);
        });
        STGFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            STGFontButton.setBackground(color);
            STGFontButton.setForeground(color);
        });
        ODSFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            ODSFontButton.setBackground(color);
            ODSFontButton.setForeground(color);
        });
        DWDFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DWDFontButton.setBackground(color);
            DWDFontButton.setForeground(color);
        });
        DWMFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DWMFontButton.setBackground(color);
            DWMFontButton.setForeground(color);
        });
        APPFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            APPFontButton.setBackground(color);
            APPFontButton.setForeground(color);
        });
        DIMFontButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DIMFontButton.setBackground(color);
            DIMFontButton.setForeground(color);
        });
        MYSQLBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            MYSQLBGButton.setBackground(color);
            MYSQLBGButton.setForeground(color);
        });
        MYSQLFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            MYSQLFONTButton.setBackground(color);
            MYSQLFONTButton.setForeground(color);
        });
        HIVEBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            HIVEBGButton.setBackground(color);
            HIVEBGButton.setForeground(color);
        });
        HIVEFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            HIVEFONTButton.setBackground(color);
            HIVEFONTButton.setForeground(color);
        });
        CLICKHOUSEBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            CLICKHOUSEBGButton.setBackground(color);
            CLICKHOUSEBGButton.setForeground(color);
        });
        CLICKHOUSEFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            CLICKHOUSEFONTButton.setBackground(color);
            CLICKHOUSEFONTButton.setForeground(color);
        });
        dataSetBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            dataSetBGButton.setBackground(color);
            dataSetBGButton.setForeground(color);
        });
        DATASETFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DATASETFONTButton.setBackground(color);
            DATASETFONTButton.setForeground(color);
        });
        DASHBOARDBGButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DASHBOARDBGButton.setBackground(color);
            DASHBOARDBGButton.setForeground(color);
        });
        DASHBOARDFONTButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            DASHBOARDFONTButton.setBackground(color);
            DASHBOARDFONTButton.setForeground(color);
        });

        color1Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            color1Button.setBackground(color);
            color1Button.setForeground(color);
        });

        color2Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            color2Button.setBackground(color);
            color2Button.setForeground(color);
        });

        color3Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            color3Button.setBackground(color);
            color3Button.setForeground(color);
        });

        color4Button.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
            final Color color = new Color(newColor.getRGB());
            color4Button.setBackground(color);
            color4Button.setForeground(color);
        });

        colorRestButton.addActionListener(e -> {
            HIVEBGButton.setBackground(new Color(DefaultColor.alizarin100));
            HIVEBGButton.setForeground(new Color(DefaultColor.alizarin100));
        });
        hierachyDepth.addActionListener(e -> {

        });


    }

    @Override
    public void reset(@NotNull MyConfigurable settings) {
        this.textField1.setText(settings.getParam());
        this.ClusterBgColorButton.setBackground(new Color(settings.getClusterBgColor()));
        this.ClusterFontColorButton.setForeground(new Color(settings.getClusterFontColor()));
        this.TableBgColorButton.setForeground(new Color(settings.getTableBgColor()));
        this.TableFontColor.setForeground(new Color(settings.getTableFontColor()));
        this.FieldBgColorButton.setForeground(new Color(settings.getFieldBgColor()));
        this.FieldsFontColorButton.setForeground(new Color(settings.getFieldsFontColor()));
        this.ArrowColorButton.setForeground(new Color(settings.getArrowColor()));
        this.hiveLayerColorCheckBox.setSelected(settings.isHiveLayerColor());
        this.STGBGButton.setBackground(new Color(settings.getSTGBG()));
        this.STGFontButton.setForeground(new Color(settings.getSTGFont()));
        this.ODSBGButton.setBackground(new Color(settings.getODSBG()));
        this.ODSFontButton.setForeground(new Color(settings.getODSFont()));
        this.DWDBGButton.setBackground(new Color(settings.getDWDBG()));
        this.DWDFontButton.setForeground(new Color(settings.getDWDFont()));
        this.DWMBGButton.setBackground(new Color(settings.getDWMBG()));
        this.DWMFontButton.setForeground(new Color(settings.getDWMFont()));
        this.APPBGButton.setBackground(new Color(settings.getAPPBG()));
        this.APPFontButton.setForeground(new Color(settings.getAPPFont()));
        this.DIMBGButton.setBackground(new Color(settings.getDIMBG()));
        this.DIMFontButton.setForeground(new Color(settings.getDIMFont()));

        this.MYSQLBGButton.setBackground(new Color(settings.getMYSQLBG()));
        this.MYSQLFONTButton.setForeground(new Color(settings.getMYSQLFONT()));
        this.HIVEBGButton.setBackground(new Color(settings.getHIVEBG()));
        this.HIVEFONTButton.setForeground(new Color(settings.getHIVEFONT()));
        this.CLICKHOUSEBGButton.setBackground(new Color(settings.getCLICKHOUSEBG()));
        this.CLICKHOUSEFONTButton.setForeground(new Color(settings.getCLICKHOUSEFONT()));
        this.dataSetBGButton.setBackground(new Color(settings.getDataSetBG()));
        this.DATASETFONTButton.setForeground(new Color(settings.getDataSetBG()));
        this.DASHBOARDBGButton.setBackground(new Color(settings.getDASHBOARDBG()));
        this.DASHBOARDFONTButton.setForeground(new Color(settings.getDASHBOARDFONT()));

        this.downStreamCheckBox.setSelected(settings.isDownStream());
        this.upStreamCheckBox.setSelected(settings.isUpStream());
        this.fieldsDisplay.setSelected(settings.isFieldsShow());

        this.hierachyDepth.setSelectedItem(settings.getHierachyDepth());

        this.batchFieldHierachy.setSelected(settings.isBatchFieldHierachy());
        this.OpenAfterGen.setSelected(settings.isOpenAfterGen());
        this.outputPath.setText(settings.getOutputPath());
        this.downloadAfterExtractRadioButton.setSelected(settings.isDownloadAfterExtract());

        this.genScoutCmdsRadioButton.setSelected(settings.isGenScoutCode());

        this.tennonModeSelector.setSelectedItem(settings.getTennonShowMode());

        this.OnlyHiveTable.setSelected(settings.isHiveOnlyHierachy());

        this.color1Button.setBackground(new Color(settings.getColor1()));
        this.color2Button.setBackground(new Color(settings.getColor2()));
        this.color3Button.setBackground(new Color(settings.getColor3()));
        this.color4Button.setBackground(new Color(settings.getColor4()));
    }

    @Override
    public boolean isModified(@NotNull MyConfigurable settings) {
        boolean isModified = false;
        isModified |= !this.textField1.getText().equals(settings.getParam());
        isModified |= this.ClusterBgColorButton.getBackground().getRGB() != (settings.getClusterBgColor());
        isModified |= this.ClusterFontColorButton.getForeground().getRGB() != (settings.getClusterFontColor());
        isModified |= this.TableBgColorButton.getForeground().getRGB() != (settings.getTableBgColor());
        isModified |= this.TableFontColor.getForeground().getRGB() != (settings.getTableFontColor());
        isModified |= this.FieldBgColorButton.getForeground().getRGB() != (settings.getFieldBgColor());
        isModified |= this.FieldsFontColorButton.getForeground().getRGB() != (settings.getFieldsFontColor());
        isModified |= this.ArrowColorButton.getForeground().getRGB() != (settings.getArrowColor());
        isModified |= this.hiveLayerColorCheckBox.isSelected() != (settings.isHiveLayerColor());
        isModified |= this.STGBGButton.getBackground().getRGB() != (settings.getSTGBG());
        isModified |= this.STGFontButton.getForeground().getRGB() != (settings.getSTGFont());
        isModified |= this.ODSBGButton.getBackground().getRGB() != (settings.getODSBG());
        isModified |= this.ODSFontButton.getForeground().getRGB() != (settings.getODSFont());
        isModified |= this.DWDBGButton.getBackground().getRGB() != (settings.getDWDBG());
        isModified |= this.DWDFontButton.getForeground().getRGB() != (settings.getDWDFont());
        isModified |= this.DWMBGButton.getBackground().getRGB() != (settings.getDWMBG());
        isModified |= this.DWMFontButton.getForeground().getRGB() != (settings.getDWMFont());
        isModified |= this.APPBGButton.getBackground().getRGB() != (settings.getAPPBG());
        isModified |= this.APPFontButton.getForeground().getRGB() != (settings.getAPPFont());
        isModified |= this.DIMBGButton.getBackground().getRGB() != (settings.getDIMBG());
        isModified |= this.DIMFontButton.getForeground().getRGB() != (settings.getDIMFont());
        isModified |= this.MYSQLBGButton.getBackground().getRGB() != (settings.getMYSQLBG());
        isModified |= this.MYSQLFONTButton.getForeground().getRGB() != (settings.getMYSQLFONT());
        isModified |= this.HIVEBGButton.getBackground().getRGB() != (settings.getHIVEBG());
        isModified |= this.HIVEFONTButton.getForeground().getRGB() != (settings.getHIVEFONT());
        isModified |= this.CLICKHOUSEBGButton.getBackground().getRGB() != (settings.getCLICKHOUSEBG());
        isModified |= this.CLICKHOUSEFONTButton.getForeground().getRGB() != (settings.getCLICKHOUSEFONT());
        isModified |= this.dataSetBGButton.getBackground().getRGB() != (settings.getDataSetBG());
        isModified |= this.DATASETFONTButton.getForeground().getRGB() != (settings.getDATASETFONT());
        isModified |= this.DASHBOARDBGButton.getBackground().getRGB() != (settings.getDASHBOARDBG());
        isModified |= this.DASHBOARDFONTButton.getForeground().getRGB() != (settings.getDASHBOARDFONT());

        isModified |= this.downStreamCheckBox.isSelected() != (settings.isDownStream());
        isModified |= this.upStreamCheckBox.isSelected() != (settings.isUpStream());
        isModified |= this.fieldsDisplay.isSelected() != (settings.isFieldsShow());

        isModified |= !(this.hierachyDepth.getSelectedItem().equals(settings.hierachyDepth));

        isModified |= this.batchFieldHierachy.isSelected() != settings.isBatchFieldHierachy();
        isModified |= this.OpenAfterGen.isSelected() != (settings.isOpenAfterGen());


        isModified |= !(this.outputPath.getText().equals(settings.getOutputPath()));

        isModified |= this.downloadAfterExtractRadioButton.isSelected() != settings.isDownloadAfterExtract();

        isModified |= !(this.genScoutCmdsRadioButton.isSelected() == settings.isGenScoutCode());

        isModified |= !(this.tennonModeSelector.getSelectedItem().equals(settings.getTennonShowMode()));

        isModified |= this.OnlyHiveTable.isSelected() != settings.isHiveOnlyHierachy();


        isModified |= this.color1Button.getBackground().getRGB() != settings.getColor1();
        isModified |= this.color2Button.getBackground().getRGB() != settings.getColor2();
        isModified |= this.color3Button.getBackground().getRGB() != settings.getColor3();
        isModified |= this.color4Button.getBackground().getRGB() != settings.getColor4();

        return isModified;
    }

    @Override
    public void apply(@NotNull MyConfigurable settings) {
        settings.setParam(this.textField1.getText());
        settings.setClusterBgColor(this.ClusterBgColorButton.getForeground().getRGB());
        settings.setClusterFontColor(this.ClusterFontColorButton.getForeground().getRGB());
        settings.setTableBgColor(this.TableBgColorButton.getForeground().getRGB());
        settings.setTableFontColor(this.TableFontColor.getForeground().getRGB());
        settings.setFieldBgColor(this.FieldBgColorButton.getForeground().getRGB());
        settings.setFieldsFontColor(this.FieldsFontColorButton.getForeground().getRGB());
        settings.setArrowColor(this.ArrowColorButton.getForeground().getRGB());

        settings.setHiveLayerColor(this.hiveLayerColorCheckBox.isSelected());
        settings.setSTGBG(this.STGBGButton.getBackground().getRGB());
        settings.setSTGFont(this.STGFontButton.getForeground().getRGB());
        settings.setODSBG(this.ODSBGButton.getBackground().getRGB());
        settings.setODSFont(this.ODSFontButton.getForeground().getRGB());
        settings.setDWDBG(this.DWDBGButton.getBackground().getRGB());
        settings.setDWDFont(this.DWDFontButton.getForeground().getRGB());
        settings.setDWMBG(this.DWMBGButton.getBackground().getRGB());
        settings.setDWMFont(this.DWMFontButton.getForeground().getRGB());
        settings.setAPPBG(this.APPBGButton.getBackground().getRGB());
        settings.setAPPFont(this.APPFontButton.getForeground().getRGB());


        settings.setMYSQLBG(this.MYSQLBGButton.getBackground().getRGB());
        settings.setMYSQLFONT(this.MYSQLFONTButton.getForeground().getRGB());
        settings.setHIVEBG(this.HIVEBGButton.getBackground().getRGB());
        settings.setHIVEFONT(this.HIVEFONTButton.getForeground().getRGB());
        settings.setCLICKHOUSEBG(this.CLICKHOUSEBGButton.getBackground().getRGB());
        settings.setCLICKHOUSEFONT(this.CLICKHOUSEFONTButton.getForeground().getRGB());
        settings.setDataSetBG(this.dataSetBGButton.getBackground().getRGB());
        settings.setDATASETFONT(this.DATASETFONTButton.getForeground().getRGB());
        settings.setDASHBOARDBG(this.DASHBOARDBGButton.getBackground().getRGB());
        settings.setDASHBOARDFONT(this.DASHBOARDFONTButton.getForeground().getRGB());

        settings.setDownStream(this.downStreamCheckBox.isSelected());
        settings.setUpStream(this.upStreamCheckBox.isSelected());
        settings.setFieldsShow(this.fieldsDisplay.isSelected());

        settings.setHierachyDepth(this.hierachyDepth.getSelectedItem().toString());
        settings.setBatchFieldHierachy(this.batchFieldHierachy.isSelected());
        settings.setOpenAfterGen(this.OpenAfterGen.isSelected());
        settings.setOutputPath(this.outputPath.getText());
        settings.setDownloadAfterExtract(this.downloadAfterExtractRadioButton.isSelected());
        settings.setGenScoutCode(this.genScoutCmdsRadioButton.isSelected());
        settings.setTennonShowMode(this.tennonModeSelector.getSelectedItem().toString());

        settings.setHiveOnlyHierachy(this.OnlyHiveTable.isSelected());


        settings.setColor1(this.color1Button.getBackground().getRGB());
        settings.setColor2(this.color2Button.getBackground().getRGB());
        settings.setColor3(this.color3Button.getBackground().getRGB());
        settings.setColor4(this.color4Button.getBackground().getRGB());

    }

    @Override
    public @NotNull JComponent getComponent() {
        return this.panel1;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
