package mydata.studio.listener;

import com.intellij.openapi.ui.Messages;
import lombok.SneakyThrows;
import mydata.studio.DataStudioCommonParam;
import mydata.studio.MyDataStudio;
import mydata.studio.op.GenOp;
import openbrowser.TenonStructureOp$;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * @author nobody
 */
public class ExecuteButtonListener implements ActionListener, KeyListener {
    MyDataStudio myDataStudio;

    public ExecuteButtonListener(MyDataStudio myDataStudio) {
        this.myDataStudio = myDataStudio;
    }

    @SneakyThrows
    @Override
    public void actionPerformed(ActionEvent e) {

        final DataStudioCommonParam dataStudioCommonParam = new DataStudioCommonParam(this.myDataStudio);

        final List<String> params = dataStudioCommonParam.getParams();

        for (String param : params) {
            System.out.println(param);
        }

        // todo: judge which button is clicked and execute the corresponding command

        params.forEach(param -> {

            if (this.myDataStudio.getFieldsInfoButton().isSelected()) {
                GenOp.getSelectPart(param);
            }

            if (this.myDataStudio.getTennonButton().isSelected()) {
                TenonStructureOp$.MODULE$.genTenonStructureGraph(param);
            }

            if (this.myDataStudio.getHierachyFieldRadioButton().isSelected()) {
                Messages.showInfoMessage("Not implemented yet", "Not implemented yet");
            }

            if (this.myDataStudio.getHierachyRadioButton().isSelected()) {
                Messages.showInfoMessage("Not implemented yet", "Not implemented yet");
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @SneakyThrows
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
            this.actionPerformed(null);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.actionPerformed(null);
        }
    }

    // 私わたしの名前は
    // 何ですか

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
