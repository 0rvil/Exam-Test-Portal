package com.orvil;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.event.*;

// Unimplimented JFrame that prompts the user for the password and has it displayed in a JPasswordField
// Could not be implimented due to complications with thread handaling of JFrame versue JOptionPane
public class PasswordFrame extends JFrame {

    public static ImageIcon passwordIcon = new ImageIcon("src/main/Assets/password.png");

    // Window parameters
    private int WINDOW_WIDTH = 450;
    private int WINDOW_HEIGHT = 300;

    private JPanel panel; // Panel for our content
    private JLabel passwordLabel; // A label for the password field
    private JLabel iconLabel; // A label for the icon
    private JPasswordField passwordField; // A password field for user's to enter their password
    private JButton okBtn; // A button to confirm entry of password
    private JButton cancelBtn; // A button to cancel password entry

    private String outpuString;

    public PasswordFrame() {
        setTitle("Password Frame");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createPanel();
        add(panel);
        setVisible(true);

    }

    private void createPanel() {
        iconLabel = new JLabel(passwordIcon);
        passwordLabel = new JLabel("Enter your password: ");
        passwordField = new JPasswordField(8);

        okBtn = new JButton("OK");
        okBtn.setForeground(Color.BLUE);
        okBtn.setBackground(Color.BLACK);
        okBtn.addActionListener(new ConfirmBtnListener());

        cancelBtn = new JButton("CANCEL");
        cancelBtn.addActionListener(new CancelBtnListener());

        panel = new JPanel();
        panel.add(iconLabel);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(okBtn);
        panel.add(cancelBtn);

    }

    public void setOutputString(String str) {
        outpuString = str;
    }

    public String getOutputString() {
        return outpuString;
    }

    private class ConfirmBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            char[] passwordChars = passwordField.getPassword();
            if (passwordChars.length > 4) {
                String password = "";
                for (char c : passwordChars) {
                    password += c;
                }

                setOutputString(password);
                dispose();
            }
        }
    }

    private class CancelBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
