package se.systementor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CashRegisterForm {
    private JPanel panel1;
    private JPanel panelRight;
    private JPanel panelLeft;
    private JTextArea receiptArea;
    private JPanel buttonsPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton addButton;
    private JButton payButton;
    private Database database = new Database();
    private double total = 0;
    private ArrayList<String> receiptLines = new ArrayList<>();
    private Product selectedProduct;

    public CashRegisterForm() {
        for (Product product : database.getProducts()) {
            JButton button = new JButton(product.getName());
            buttonsPanel.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField1.setText(product.getName());
                    selectedProduct = product;
                }
            });
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (selectedProduct == null) {
                        JOptionPane.showMessageDialog(null, "Välj en produkt först!");
                        return;
                    }

                    int quantity = Integer.parseInt(textField2.getText());
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Ange ett giltigt antal!");
                        return;
                    }

                    double subtotal = selectedProduct.getPrice() * quantity;
                    total += subtotal;

                    String line = String.format("%dx %s %.2f kr", quantity, selectedProduct.getName(), subtotal);
                    receiptLines.add(line);
                    updateReceiptDisplay();

                    // Reset fields
                    textField1.setText("");
                    textField2.setText("");
                    selectedProduct = null;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ange ett giltigt antal!");
                }
            }
        });

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (receiptLines.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inga varor i kvittot!");
                    return;
                }

                receiptLines.add(String.format("\nTotalt: %.2f kr", total));
                receiptLines.add("\nTACK FÖR DITT KÖP!");
                updateReceiptDisplay();

                // Reset receipt
                receiptLines.clear();
                total = 0.0;
            }
        });
    }

    private void updateReceiptDisplay() {
        receiptArea.setText("");
        receiptArea.append("                     ONLINESHOP\n");
        receiptArea.append("----------------------------------------------------\n");
        receiptArea.append("\n");
        receiptArea.append("Kvittonummer: 13       Datum: 2024-03-3 13:00:21\n");
        receiptArea.append("----------------------------------------------------\n");

        for (String line : receiptLines) {
            receiptArea.append(line + "\n");
        }
    }

    public void run(){
        JFrame frame = new JFrame("Cash Register");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
