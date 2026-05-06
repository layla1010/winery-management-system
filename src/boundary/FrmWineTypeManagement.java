package boundary;

import javax.swing.*;
import java.awt.*;

public class FrmWineTypeManagement extends JPanel {
    private JPanel sideMenu, contentPanel;
    private CardLayout cardLayout;
    private String userRole;

    // Store references to the buttons so we can modify them later
    private JButton btnWineTypes;
    private JButton btnOccasions;
    private JButton btnDishes;
    private JButton btnReports;
    private JButton btnLinkWine;

    // Modified constructor that accepts the role.
    public FrmWineTypeManagement(String role) {
        this.userRole = role;
        setPreferredSize(new Dimension(900, 550));
        setLayout(new BorderLayout());

        // Create the side menu
        sideMenu = createSideMenu();

        // Create the header panel
        JPanel headerPanel = createHeaderPanel();

        // Create the content panel using CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        setupContentPanels();

        add(sideMenu, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Adjust UI based on the user role
        customizeForRole();
    }

    private JPanel createSideMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(105, 20, 56));
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("Management Modules", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(217, 166, 165));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));

        // Create buttons and store references
        btnWineTypes = createMenuButton("Wine Types", "🍷");
        btnOccasions = createMenuButton("Occasions", "🎉");
        btnDishes = createMenuButton("Dishes", "🍽️");
        btnReports = createMenuButton("Recommendation Report", "📊");
        btnLinkWine = createMenuButton("Link WineTypes", "🔗");

        // Add components to the menu panel
        menu.add(lblTitle);
        menu.add(Box.createVerticalStrut(20));
        menu.add(btnWineTypes);
        menu.add(btnOccasions);
        menu.add(btnDishes);
        menu.add(btnReports);
        menu.add(btnLinkWine);

        // Button listeners switch the card on the content panel:
        btnWineTypes.addActionListener(e -> cardLayout.show(contentPanel, "WineType"));
        btnOccasions.addActionListener(e -> cardLayout.show(contentPanel, "Occasion"));
        btnDishes.addActionListener(e -> cardLayout.show(contentPanel, "Dish"));
        btnReports.addActionListener(e -> cardLayout.show(contentPanel, "Recommendation Report"));
        btnLinkWine.addActionListener(e -> cardLayout.show(contentPanel, "Link WineTypes"));

        return menu;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(new Color(69, 16, 32));
        header.setPreferredSize(new Dimension(0, 60));
        header.setLayout(null);

        JLabel lblManage = new JLabel("Wine Type Management", SwingConstants.LEFT);
        lblManage.setBounds(43, 0, 300, 80);
        lblManage.setForeground(new Color(234, 171, 55));
        lblManage.setFont(new Font("Book Antiqua", Font.BOLD, 20));
        header.add(lblManage);

        return header;
    }

    private void setupContentPanels() {
        // Instantiate your existing panels
        FrmWineType wineTypePanel = new FrmWineType(userRole);
        FrmOccasion occasionPanel = new FrmOccasion();
        FrmDish dishPanel = new FrmDish();
        FrmRecommendation reportPanel = new FrmRecommendation();
        FrmLinkWineType linkWinePanel = new FrmLinkWineType();

        // Add panels to the card layout with unique identifiers
        contentPanel.add(wineTypePanel, "WineType");
        contentPanel.add(occasionPanel, "Occasion");
        contentPanel.add(dishPanel, "Dish");
        contentPanel.add(reportPanel, "Recommendation Report");
        contentPanel.add(linkWinePanel, "Link WineTypes");
    }

    private JButton createMenuButton(String text, String icon) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 60));
        button.setBackground(new Color(105, 20, 56));
        button.setForeground(new Color(235, 174, 63));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        return button;
    }

    // Customize the UI based on the user role
    private void customizeForRole() {
        if("Marketing".equalsIgnoreCase(userRole)) {
            // Disable the Reports button for Marketing users.
            btnReports.setEnabled(false);
        }
        if("Sales".equalsIgnoreCase(userRole)) {
            // Disable the Occasions, Dishes, and Link WineTypes buttons for Sales users.
            btnOccasions.setEnabled(false);
            btnDishes.setEnabled(false);
            btnLinkWine.setEnabled(false);
            btnWineTypes.setEnabled(false);
        }
    }
}
