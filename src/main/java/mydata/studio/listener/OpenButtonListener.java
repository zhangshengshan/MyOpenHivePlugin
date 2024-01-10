package mydata.studio.listener;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import mydata.studio.MyDataStudio;
import reader.DataFrame;
import reader.ExcelReader;
import scala.collection.immutable.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author nobody
 */
public class OpenButtonListener implements ActionListener {

    private MyDataStudio myDataStudio;

    public OpenButtonListener(MyDataStudio myDataStudio) {
        this.myDataStudio = myDataStudio;
    }

    static String[] getHeaders(DataFrame dataFrame) {
        List<String> headers = dataFrame.headers();
        String[] ret = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            ret[i] = headers.apply(i);
        }
        return ret;
    }

    static String[][] getRows(DataFrame dataFrame) {
        List<List<String>> rows = dataFrame.data();
        String[][] ret = new String[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.apply(i);
            String[] rowArr = new String[row.size()];
            for (int j = 0; j < row.size(); j++) {
                rowArr[j] = row.apply(j);
            }
            ret[i] = rowArr;
        }
        return ret;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final VirtualFile virtualFile =
                FileChooser.chooseFile(
                        FileChooserDescriptorFactory.createSingleFileDescriptor(), null, null);

        this.myDataStudio.setInputFile(virtualFile.getPath());
        this.myDataStudio.getPromptArea().setText(virtualFile.getPath());
        final String path = virtualFile.getPath();
        final String name = virtualFile.getNameWithoutExtension();
        // TODO: 2022/11/4 添加专门的ERPLOT LISTENER
        //    final Graph graph = GraphBuilder.make(path);
        //    graph.render(name);
        ExcelReader excelReader = new ExcelReader(virtualFile.getPath());
        final List<String> sheetNames = excelReader.getSheetNames();
        String[] array = new String[sheetNames.size()];
        for (int i = 0; i < sheetNames.size(); i++) {
            array[i] = sheetNames.apply(i);
        }

        final String inputSheet =
                Messages.showEditableChooseDialog("请选择要打开的sheet", "选择sheet", null, array, array[0], null);
        final DataFrame dataFrame = excelReader.getDataFrame(inputSheet);

        // TODO: 2022/11/4 check size of sheetNames and data
        this.myDataStudio
                .getWorkTable()
                .setModel(
                        new javax.swing.table.DefaultTableModel(getRows(dataFrame), getHeaders(dataFrame)));

        /*
         * //deprecated
         * final ExcelReader erConfig = new ExcelReader(path);
         * graph.GraphMeta graphMeta = new graph.GraphMeta(erConfig);
         * graphMeta.toGraphViz();
         */
    }
}
