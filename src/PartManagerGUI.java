import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PartManagerGUI extends JFrame {
    private final JComboBox<Manufacturer> manufacturerComboBox = new JComboBox<>();
    private final JComboBox<PartCategory> categoryComboBox = new JComboBox<>();
    private final JTextField nameField = new JTextField();
    private final JTextField codeField = new JTextField();
    private final JTextField buyPriceField = new JTextField();
    private final JTextField sellPriceField = new JTextField();
    private final JTable partsTable;
    private final DefaultTableModel tableModel;
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private final JTextField checkField = new JTextField(10);
    private final JLabel checkLabel = new JLabel();

    private Part selectedPart = null;

    public PartManagerGUI() {
        setTitle("Part Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 500);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 7, 5, 5));
        inputPanel.add(new JLabel("Manufacturer:"));
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(new JLabel("Code:"));
        inputPanel.add(new JLabel("Buy Price:"));
        inputPanel.add(new JLabel("Sell Price:"));
        inputPanel.add(new JLabel());
        inputPanel.add(manufacturerComboBox);
        inputPanel.add(categoryComboBox);
        inputPanel.add(nameField);
        inputPanel.add(codeField);
        inputPanel.add(buyPriceField);
        inputPanel.add(sellPriceField);

        JPanel btnPanel = new JPanel();
        JButton addButton = new JButton("Add");
        btnPanel.add(addButton);
        btnPanel.add(editButton);
        btnPanel.add(deleteButton);
        inputPanel.add(btnPanel);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Code", "Buy", "Sell", "Manufacturer", "Category"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        partsTable = new JTable(tableModel);
        partsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(partsTable);

        JPanel checkPanel = new JPanel();
        checkPanel.add(new JLabel("Check part:"));
        checkPanel.add(checkField);
        JButton checkButton = new JButton("Check");
        checkPanel.add(checkButton);
        checkPanel.add(checkLabel);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(checkPanel, BorderLayout.SOUTH);

        loadManufacturers();
        loadCategories();
        loadParts();

        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        checkButton.addActionListener(e -> handleCheck());
        partsTable.getSelectionModel().addListSelectionListener(e -> loadSelectedPart());

        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void loadManufacturers() {
        try {
            manufacturerComboBox.removeAllItems();
            for (Manufacturer m : DBUtil.getAllManufacturers()) manufacturerComboBox.addItem(m);
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            for (PartCategory c : DBUtil.getAllCategories()) categoryComboBox.addItem(c);
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void loadParts() {
        try {
            tableModel.setRowCount(0);
            for (Part p : DBUtil.getAllParts()) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getCode(),
                        p.getBuyPrice(), p.getSellPrice(),
                        p.getManufacturer(), p.getCategory()
                });
            }
        } catch (Exception e) { showError(e.getMessage()); }
        clearFields();
    }

    private void handleAdd() {
        try {
            Part p = new Part();
            p.setName(nameField.getText().trim());
            p.setCode(codeField.getText().trim());
            p.setBuyPrice(Double.parseDouble(buyPriceField.getText().trim()));
            p.setSellPrice(Double.parseDouble(sellPriceField.getText().trim()));
            p.setManufacturer((Manufacturer) manufacturerComboBox.getSelectedItem());
            p.setCategory((PartCategory) categoryComboBox.getSelectedItem());
            DBUtil.insertPart(p);
            loadParts();
        } catch (Exception e) { showError("Add failed: " + e.getMessage()); }
    }

    private void handleEdit() {
        if (selectedPart == null) return;
        try {
            selectedPart.setName(nameField.getText().trim());
            selectedPart.setCode(codeField.getText().trim());
            selectedPart.setBuyPrice(Double.parseDouble(buyPriceField.getText().trim()));
            selectedPart.setSellPrice(Double.parseDouble(sellPriceField.getText().trim()));
            selectedPart.setManufacturer((Manufacturer) manufacturerComboBox.getSelectedItem());
            selectedPart.setCategory((PartCategory) categoryComboBox.getSelectedItem());
            DBUtil.updatePart(selectedPart);
            loadParts();
        } catch (Exception e) { showError("Edit failed: " + e.getMessage()); }
    }

    private void handleDelete() {
        if (selectedPart == null) return;
        int conf = JOptionPane.showConfirmDialog(this, "Delete selected part?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                DBUtil.deletePart(selectedPart.getId());
                loadParts();
            } catch (Exception e) { showError("Delete failed: " + e.getMessage()); }
        }
    }

    private void handleCheck() {
        try {
            String s = checkField.getText().trim();
            Part p = DBUtil.findPartByCodeOrName(s);
            checkLabel.setText(p != null ? "Available: " + p.getName() : "Not available.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void loadSelectedPart() {
        int row = partsTable.getSelectedRow();
        if (row == -1) {
            selectedPart = null;
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            clearFields();
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            selectedPart = DBUtil.getPartById(id);
            if (selectedPart != null) {
                nameField.setText(selectedPart.getName());
                codeField.setText(selectedPart.getCode());
                buyPriceField.setText(String.valueOf(selectedPart.getBuyPrice()));
                sellPriceField.setText(String.valueOf(selectedPart.getSellPrice()));
                manufacturerComboBox.setSelectedItem(selectedPart.getManufacturer());
                categoryComboBox.setSelectedItem(selectedPart.getCategory());
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void clearFields() {
        nameField.setText("");
        codeField.setText("");
        buyPriceField.setText("");
        sellPriceField.setText("");
        manufacturerComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        partsTable.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PartManagerGUI().setVisible(true));
    }
}
