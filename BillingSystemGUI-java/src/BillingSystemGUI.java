import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class BillingSystemGUI {

    private static List<Item> cart = new ArrayList<>();
    private static double totalAmount = 0.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Item Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JLabel itemNameLabel = new JLabel("Item Name:");
        JTextField itemNameField = new JTextField();

        JLabel itemPriceLabel = new JLabel("Item Price:");
        JTextField itemPriceField = new JTextField();

        JLabel itemQuantityLabel = new JLabel("Item Quantity:");
        JTextField itemQuantityField = new JTextField();

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> {
            String name = itemNameField.getText().trim();
            String priceText = itemPriceField.getText().trim();
            String quantityText = itemQuantityField.getText().trim();

            if (!name.isEmpty() && !priceText.isEmpty() && !quantityText.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceText);
                    int quantity = Integer.parseInt(quantityText);

                    if (price > 0 && quantity > 0) {
                        Item item = new Item(name, price, quantity);
                        cart.add(item);
                        totalAmount += price * quantity;

                        JOptionPane.showMessageDialog(frame, "Item added successfully!");
                        itemNameField.setText("");
                        itemPriceField.setText("");
                        itemQuantityField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Price and quantity must be positive numbers.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input for price or quantity.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            }
        });

        inputPanel.add(itemNameLabel);
        inputPanel.add(itemNameField);
        inputPanel.add(itemPriceLabel);
        inputPanel.add(itemPriceField);
        inputPanel.add(itemQuantityLabel);
        inputPanel.add(itemQuantityField);
        inputPanel.add(addItemButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Bill Display Panel
        JTextArea billTextArea = new JTextArea();
        billTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton calculateButton = new JButton("Calculate Total");
        calculateButton.addActionListener(e -> {
            StringBuilder bill = new StringBuilder("Bill Details:\n\n");
            bill.append(String.format("%-20s %-10s %-10s %-10s\n", "Item Name", "Price", "Qty", "Total"));
            bill.append("------------------------------------------------\n");

            for (Item item : cart) {
                bill.append(String.format("%-20s %-10.2f %-10d %-10.2f\n",
                        item.getName(), item.getPrice(), item.getQuantity(), item.getTotal()));
            }

            bill.append("\nTotal Amount: $" + NumberFormat.getCurrencyInstance().format(totalAmount));
            billTextArea.setText(bill.toString());
        });

        JButton applyDiscountButton = new JButton("Apply Discount");
        applyDiscountButton.addActionListener(e -> {
            String discountText = JOptionPane.showInputDialog(frame, "Enter discount percentage (e.g., 10 for 10%):");
            if (discountText != null && !discountText.isEmpty()) {
                try {
                    double discountPercentage = Double.parseDouble(discountText);
                    if (discountPercentage >= 0 && discountPercentage <= 100) {
                        double discountAmount = (discountPercentage / 100) * totalAmount;
                        totalAmount -= discountAmount;

                        JOptionPane.showMessageDialog(frame, "Discount applied! New total: $" +
                                NumberFormat.getCurrencyInstance().format(totalAmount));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Discount percentage must be between 0 and 100.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid discount percentage.");
                }
            }
        });

        JButton printBillButton = new JButton("Print Bill");
        printBillButton.addActionListener(e -> {
            if (!cart.isEmpty()) {
                StringBuilder bill = new StringBuilder("Final Bill:\n\n");
                bill.append(String.format("%-20s %-10s %-10s %-10s\n", "Item Name", "Price", "Qty", "Total"));
                bill.append("------------------------------------------------\n");

                for (Item item : cart) {
                    bill.append(String.format("%-20s %-10.2f %-10d %-10.2f\n",
                            item.getName(), item.getPrice(), item.getQuantity(), item.getTotal()));
                }

                bill.append("\nTotal Amount: $" + NumberFormat.getCurrencyInstance().format(totalAmount));
                JOptionPane.showMessageDialog(frame, bill.toString());
            } else {
                JOptionPane.showMessageDialog(frame, "Cart is empty. Please add items first.");
            }
        });

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.addActionListener(e -> {
            cart.clear();
            totalAmount = 0.0;
            billTextArea.setText("");
            JOptionPane.showMessageDialog(frame, "Cart cleared successfully!");
        });

        buttonPanel.add(calculateButton);
        buttonPanel.add(applyDiscountButton);
        buttonPanel.add(printBillButton);
        buttonPanel.add(clearCartButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    static class Item {
        private String name;
        private double price;
        private int quantity;

        public Item(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotal() {
            return price * quantity;
        }
    }
}