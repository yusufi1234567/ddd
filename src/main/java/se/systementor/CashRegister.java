package se.systementor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CashRegister {
    private JFrame frame;
    private JTextArea receiptArea;
    private JTextField quantityField;
    private JTextField selectedProductField;
    private List<JButton> productButtons;
    private Product selectedProduct;
    private ArrayList<String> receiptLines;
    private double total;
    private Database database;

    public CashRegister() {
        database = new Database();
        receiptLines = new ArrayList<>();
        total = 0.0;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Cash Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Products panel
        JPanel productsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        productButtons = new ArrayList<>();
        List<Product> products = database.getProducts();
        
        for (Product product : products) {
            JButton button = new JButton(product.getName());
            button.addActionListener(e -> selectProduct(product));
            productButtons.add(button);
            productsPanel.add(button);
        }

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        selectedProductField = new JTextField(20);
        selectedProductField.setEditable(false);
        quantityField = new JTextField(5);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addToReceipt());
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> processPayment());

        controlPanel.add(new JLabel("Selected:"));
        controlPanel.add(selectedProductField);
        controlPanel.add(new JLabel("Quantity:"));
        controlPanel.add(quantityField);
        controlPanel.add(addButton);
        controlPanel.add(payButton);

        // Receipt area
        receiptArea = new JTextArea(20, 30);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);

        // Add components to frame
        frame.add(productsPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.EAST);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void selectProduct(Product product) {
        selectedProduct = product;
        selectedProductField.setText(product.getName());
    }

    private void addToReceipt() {
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(frame, "Please select a product first!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid quantity!");
                return;
            }

            double lineTotal = selectedProduct.getPrice() * quantity;
            String line = String.format("%dx %s %.2f kr", quantity, selectedProduct.getName(), lineTotal);
            receiptLines.add(line);
            total += lineTotal;
            updateReceiptDisplay();
            
            // Clear inputs
            selectedProduct = null;
            selectedProductField.setText("");
            quantityField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for quantity!");
        }
    }

    private void processPayment() {
        if (receiptLines.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No items in receipt!");
            return;
        }

        receiptLines.add(String.format("\nTotal: %.2f kr", total));
        receiptLines.add("\nTACK FÖR DITT KÖP!");
        updateReceiptDisplay();

        // Clear receipt after payment
        receiptLines.clear();
        total = 0.0;
    }

    private void updateReceiptDisplay() {
        StringBuilder receipt = new StringBuilder();
        for (String line : receiptLines) {
            receipt.append(line).append("\n");
        }
        receiptArea.setText(receipt.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CashRegister());
    }
}
