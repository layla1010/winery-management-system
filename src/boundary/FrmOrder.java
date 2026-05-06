package boundary;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class FrmOrder extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public FrmOrder() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create and style the menu bar similar to RootLayout
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(135, 108, 52), null, null, null));
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(217, 166, 165));
        menuBar.setForeground(new Color(194, 163, 97));

        // Create JMenu items for All Orders, Regular Order, and Urgent Order.
        JMenu mnAllOrders = new JMenu("All Orders");
        mnAllOrders.setForeground(new Color(190, 86, 102));
        mnAllOrders.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                cardLayout.show(cardPanel, "AllOrders");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        
        JMenu mnRegularOrder = new JMenu("Regular Order");
        mnRegularOrder.setForeground(new Color(190, 86, 102));
        mnRegularOrder.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                cardLayout.show(cardPanel, "RegularOrder");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        
        JMenu mnUrgentOrder = new JMenu("Urgent Order");
        mnUrgentOrder.setForeground(new Color(190, 86, 102));
        mnUrgentOrder.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                cardLayout.show(cardPanel, "UrgentOrder");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        
        // Add the menu items to the menu bar with spacing (using glue to push items to the left, if desired)
        menuBar.add(mnAllOrders);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(mnRegularOrder);
        menuBar.add(Box.createHorizontalStrut(10));
        menuBar.add(mnUrgentOrder);

        // Add the menu bar to the top of the panel
        add(menuBar, BorderLayout.NORTH);

        // Create a card panel with CardLayout to switch between panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        // Create the panels for each order type
        FrmRegularOrder regularOrderPanel = new FrmRegularOrder();
        FrmUrgentOrder urgentOrderPanel = new FrmUrgentOrder();
        FrmAllOrders allOrdersPanel = new FrmAllOrders();

        // Add all panels to the card panel with unique identifiers
        cardPanel.add(allOrdersPanel, "AllOrders");
        cardPanel.add(regularOrderPanel, "RegularOrder");
        cardPanel.add(urgentOrderPanel, "UrgentOrder");

        // Add the card panel to the center of the main panel
        add(cardPanel, BorderLayout.CENTER);

        // Show All Orders panel by default
        cardLayout.show(cardPanel, "AllOrders");
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new FrmOrder());
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
