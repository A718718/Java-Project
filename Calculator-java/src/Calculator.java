import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private double firstNumber, secondNumber, result;
    private String operator;

    public Calculator() {
        setTitle("Java Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display field
        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "CE"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(this);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
            display.setText(display.getText() + command);
        }
        else if (command.equals("C")) {
            display.setText("");
        }
        else if (command.equals("CE")) {
            display.setText("");
            firstNumber = secondNumber = result = 0;
        }
        else if (command.equals("=")) {
            secondNumber = Double.parseDouble(display.getText());
            calculateResult();
            display.setText(String.valueOf(result));
        }
        else { // Operator
            firstNumber = Double.parseDouble(display.getText());
            operator = command;
            display.setText("");
        }
    }

    private void calculateResult() {
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot divide by zero!");
                }
                break;
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}

