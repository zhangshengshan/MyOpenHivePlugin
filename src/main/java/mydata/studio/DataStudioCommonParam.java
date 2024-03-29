package mydata.studio;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import lombok.Data;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 */
@Data
public class DataStudioCommonParam {
    List<String> params = new ArrayList<>();

    public DataStudioCommonParam(MyDataStudio myDataStudio)
            throws IOException, UnsupportedFlavorException {

        final ListSelectionModel selectionModel = myDataStudio.getWorkTable().getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        final int[] selectedRows = myDataStudio.getWorkTable().getSelectedRows();
        final int[] selectedColumns = myDataStudio.getWorkTable().getSelectedColumns();

        boolean twoColumns;
        if (selectedColumns.length == 2) {
            // 选中两列，组合成为库表名称
            twoColumns = true;
        } else {
            twoColumns = false;
        }

        for (int selectedRow : selectedRows) {
            for (int j = 0; j < selectedColumns.length; j++) {

                if (twoColumns) {
                    final Object db =
                            myDataStudio.getWorkTable().getValueAt(selectedRow, selectedColumns[j]);
                    j += 1;
                    final Object tb =
                            myDataStudio.getWorkTable().getValueAt(selectedRow, selectedColumns[j]);
                    this.params.add(db.toString() + "." + tb.toString());
                } else {
                    final Object dbtb =
                            myDataStudio.getWorkTable().getValueAt(selectedRow, selectedColumns[j]);
                    this.params.add(dbtb.toString());
                }
            }
        }
        final String text = myDataStudio.getPromptArea().getText();
        // 如果没有选中任何行，从prompt中提取
        if (this.params.isEmpty() && !"".equals(text)) {
            final String[] split = text.split("\n");
            for (String s : split) {
                if (!"".equals(s) && !" ".equals(s)) {
                    this.params.add(s);
                }
            }
        }
        // 如果没有选中
        if (this.params.isEmpty()) {
            final Transferable contents = CopyPasteManager.getInstance().getContents();
            final DataFlavor stringFlavor = DataFlavor.stringFlavor;
            final Object transferData = contents.getTransferData(stringFlavor);
            final String[] split = transferData.toString().split("\n");
            this.params.addAll(List.of(split));
        }

        // 如果没有选中任何行
        // なにも選択されていない場合
        if (this.params.isEmpty()) {
            final String s = Messages.showInputDialog("请输入要执行的库表", "提示", Messages.getInformationIcon());
            // TODO: 2022/11/8 check input correctness
            this.params.add(s);
        }
    }
}
