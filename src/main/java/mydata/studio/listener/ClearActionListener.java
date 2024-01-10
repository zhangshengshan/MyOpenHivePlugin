package mydata.studio.listener;

import mydata.studio.MyDataStudio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author nobody
 */
public class ClearActionListener implements ActionListener {
    MyDataStudio myDataStudio;

    public ClearActionListener(MyDataStudio myDataStudio) {
        this.myDataStudio = myDataStudio;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.myDataStudio.getPrompt().setText("");
        this.myDataStudio.getWorkTable().clearSelection();
        this.myDataStudio
                .getWorkTable()
                .setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{}));

        this.myDataStudio.getFieldsInfoButton().setSelected(false);
        this.myDataStudio.getHierachyRadioButton().setSelected(false);
        this.myDataStudio.getTennonButton().setSelected(false);

    }
}
