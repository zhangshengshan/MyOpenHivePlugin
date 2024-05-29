package myui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyMultiChoiceDialog extends DialogWrapper {


    private Set<String> selectedOptions = new HashSet<>();
    private final List<String> options;
    private JBList<String> list;
    private JTextField searchField;
    private DefaultListModel<String> model;

    public MyMultiChoiceDialog(List<String> options) {
        super(true);
        this.options = options;
        init();
        setTitle("Multi Choice Dialog");
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create the search field
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            // Filter the list based on the search field's content
            private void filter() {
                selectedOptions.addAll(list.getSelectedValuesList());

                String filterText = searchField.getText();
                if (filterText.isEmpty()) {
                    list.setModel(new DefaultListModel<>());
                    options.forEach(((DefaultListModel<String>) list.getModel())::addElement);
                } else {
                    DefaultListModel<String> filteredModel = new DefaultListModel<>();
                    for (String option : options) {
                        if (option.toLowerCase().contains(filterText.toLowerCase())) {
                            filteredModel.addElement(option);
                        }
                    }
                    list.setModel(filteredModel);
                }
            }
        });

        // Create the list
        model = new DefaultListModel<>();
        options.forEach(model::addElement);
        list = new JBList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add the search field and the list to the panel
        panel.add(searchField, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        return panel;
    }

    public List<String> getSelectedOptions() {
        // Convert the Set to a List before returning
        selectedOptions.addAll(list.getSelectedValuesList());
        return new ArrayList<>(selectedOptions);
    }
}