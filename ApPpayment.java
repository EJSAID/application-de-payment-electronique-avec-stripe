import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import com.stripe.param.CustomerCreateParams;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ApPpayment extends JFrame {
        private JTextField amountField;
        private JTextField cardNumberField;
        private JTextField expirationDateField;
        private JTextField cvvField;
        private JPanel panel;

        public ApPpayment() {
                // Set window title
                setTitle("Payment Form");

                // Set up main panel
                panel = new JPanel(new BorderLayout());
                panel.setBorder(new EmptyBorder(30, 30, 30, 30));
                panel.setBackground(Color.WHITE);

                // Add form fields
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBackground(Color.WHITE);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(10, 0, 10, 0);
                formPanel.add(createLabel("Amount:"), gbc);
                gbc.gridy++;
                amountField = createTextField();
                formPanel.add(amountField, gbc);
                gbc.gridy++;
                formPanel.add(createLabel("Card Number:"), gbc);
                gbc.gridy++;
                cardNumberField = createTextField();
                formPanel.add(cardNumberField, gbc);
                gbc.gridy++;
                formPanel.add(createLabel("Expiration Date:"), gbc);
                gbc.gridy++;
                expirationDateField = createTextField();
                formPanel.add(expirationDateField, gbc);
                gbc.gridy++;
                formPanel.add(createLabel("CVV:"), gbc);
                gbc.gridy++;
                cvvField = createTextField();
                formPanel.add(cvvField, gbc);
                panel.add(formPanel, BorderLayout.CENTER);

                // Add submit button
                JButton submitButton = new JButton("Submit");
                // Add submit button ActionListener
                submitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                String amount = amountField.getText();
                                String cardNumber = cardNumberField.getText();
                                String expirationDate = expirationDateField.getText();
                                String cvv = cvvField.getText();

                                // TODO: Use Stripe API to create a token for the payment information
                                Stripe.apiKey = "pk_test_51MjT6FKUy3ZECc7bFXjWngDbv3DnrEqN0o3ESE2yR1Y8O4gEGzAEjO1wLDPzX5Oygyq93yc36ECJpDmwygPSCARN00BCHct4X1";
                                Map<String, Object> cardParams = new HashMap<String, Object>();
                                cardParams.put("number", cardNumber);
                                cardParams.put("exp_month", Integer.parseInt(expirationDate.substring(0, 2)));
                                cardParams.put("exp_year", Integer.parseInt(expirationDate.substring(3)));
                                cardParams.put("cvc", cvv);

                                Token token = null;
                                try {
                                        Map<String, Object> tokenParams = new HashMap<String, Object>();
                                        tokenParams.put("card", cardParams);
                                        token = Token.create(tokenParams);
                                } catch (StripeException ex) {
                                        // handle exception
                                }

                                Card card = token.getCard();

                                if (card != null) {
                                        System.out.println("Card successfully created!");
                                } else {
                                        System.out.println("Card creation failed!");
                                }


                                // TODO: Process payment using the Stripe token
                                Stripe.apiKey = "sk_test_51MjT6FKUy3ZECc7bA1jAALqaNwVnF22ouNlcLoeM1VkBo6SAYVZAiCTBSwoCZJxitRJ7QhI8MvsvtGz1zbrZYBHt00P4OKy2ZD";
                                CustomerCreateParams params1= CustomerCreateParams.builder()
                                        .setEmail("eljammaasaid@gmail.com")
                                        .setName("said ej jammaa")
                                        .setDescription("Customer for said ej jammaa")
                                        .build();

                                try {
                                        Customer customer = Customer.create(params1);
                                } catch (StripeException ex) {
                                        throw new RuntimeException(ex);
                                }
                                Map<String, Object> chargeParams = new HashMap<String, Object>();
                                chargeParams.put("amount", amount);
                                chargeParams.put("currency", "usd");
                                chargeParams.put("description", "Example charge");
                                chargeParams.put("source", token.getId());

                                Charge charge = null;
                                try {
                                        charge = Charge.create(chargeParams);
                                } catch (StripeException ex) {
                                        throw new RuntimeException(ex);
                                }

                                if (charge.getStatus().equals("succeeded")) {
                                        System.out.println("Payment processed successfully!");
                                } else {
                                        System.out.println("Payment processing failed!");
                                }

                                // Display confirmation message
                                JOptionPane.showMessageDialog(ApPpayment.this, "Payment of " + amount + " was successful!");
                        }
                });

                submitButton.setBackground(Color.BLUE);
                submitButton.setForeground(Color.WHITE);
                submitButton.setPreferredSize(new Dimension(100, 30));
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(Color.WHITE);
                buttonPanel.add(submitButton);
                panel.add(buttonPanel, BorderLayout.SOUTH);

                // Set window properties
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setPreferredSize(new Dimension(450, 500));
                add(panel);
                pack();
                setLocationRelativeTo(null);
                setVisible(true);
        }

        private JLabel createLabel(String text) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Arial", Font.PLAIN, 16));
                label.setForeground(Color.BLUE);
                return label;
        }

        private JTextField createTextField() {
                JTextField textField = new JTextField();
                textField.setFont(new Font("Arial", Font.PLAIN, 16));
                textField.setForeground(Color.BLACK);
                textField.setPreferredSize(new Dimension(250, 30));
                textField.setBorder(BorderFactory.createLineBorder(Color.BLUE));
                return textField;
        }
}