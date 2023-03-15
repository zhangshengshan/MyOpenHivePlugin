package mydata.studio;

import com.intellij.openapi.wm.ToolWindow;
import lombok.Data;
import mydata.studio.listener.ClearActionListener;
import mydata.studio.listener.ExecuteButtonListener;
import mydata.studio.listener.OpenButtonListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author nobody
 */
@Data
public class MyDataStudio {
    private JButton Execute;
    private JPanel Container;
    private JTextArea Prompt;
    private JButton OpenFIle;
    private JTable workTable;
    private JTabbedPane tabbedPane1;
    private JPanel CheckBox;
    private JRadioButton 字段信息RadioButton;
    private JComboBox comboBox1;
    private JButton plotHierachyButton;
    private JButton plotERButton;
    private JSlider slider1;
    private JButton clearButton;
    private JRadioButton hierachyRadioButton;
    private JRadioButton 榫卯结构RadioButton;
    private JRadioButton hierachyFieldRadioButton;

    private String inputFile;

    /**
     * 初始化工具窗口，设置按钮监听器等
     *
     * @param toolWindow
     */
    public MyDataStudio(ToolWindow toolWindow) {
        Object data[][] = {};
        //    workTable.setModel(new DefaultTableModel(data, new String[] {}));
        //    workTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        /* 让Jtable支持列选择*/
        workTable.setRowSelectionAllowed(true);
        workTable.setColumnSelectionAllowed(true);
        final ExecuteButtonListener executeButtonListener = new ExecuteButtonListener(this);

        // TODO: 2022/11/18 刚刚点击checkbox之后，执行按钮不能运行
        Execute.addActionListener(executeButtonListener);
        Execute.addKeyListener(executeButtonListener);

        OpenFIle.addActionListener(new OpenButtonListener(this));
        clearButton.addActionListener(new ClearActionListener(this));
    }

    /*
     * only for ui preview purpose
     */

    public void setWorkTable(Object[][] data, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        workTable.setModel(model);
    }

    public JPanel getContent() {
        return Container;
    }

    public Boolean inputFileExists() {
        return inputFile != null;
    }
}
