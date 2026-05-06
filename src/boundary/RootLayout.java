package boundary;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import entity.User;

public class RootLayout extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private CardLayout cardLayout;
    
    // Declare panels for each view
    private FrmManufacturer manufacturersPanel;
    private FrmWine winesPanel;
    private FrmOrder ordersPanel;
    private FrmWineTypeManagement wineTypesPanel;
    private FrmUsers usersPanel;
    // Panels for additional views
    private FrmInventory inventoryPanel;
    private FrmEmployee employeePanel;
    private FrmCustomer customerPanel;
    private JMenu notificationMenu;
    private FrmLandPageMarketing landingMarketing;
    private FrmLandPageSales landingSales;
    private FrmLandPageAdmin landingAdmin;
    private String role;
    
    /**
     * Create the frame.
     */
    public RootLayout(User user) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 50, 900, 700);
        setResizable(false);
        
        // Initialize the main content panel with CardLayout
        contentPane = new JPanel();
        cardLayout = new CardLayout();
        contentPane.setLayout(cardLayout);
        setContentPane(contentPane);
        
        // Initialize panels
        manufacturersPanel = new FrmManufacturer();
        winesPanel = new FrmWine(user.getUserRole());
        ordersPanel = new FrmOrder();
        wineTypesPanel = new FrmWineTypeManagement(user.getUserRole());
        usersPanel = new FrmUsers();
        inventoryPanel = new FrmInventory();
        employeePanel = new FrmEmployee();
        customerPanel = new FrmCustomer(user.getUserRole());
        
        // Add panels (cards) with unique keys
        contentPane.add(manufacturersPanel, "Manufacturers");
        contentPane.add(winesPanel, "Wines");
        contentPane.add(ordersPanel, "Orders");
        contentPane.add(wineTypesPanel, "WineTypes");
        contentPane.add(usersPanel, "Users");
        contentPane.add(inventoryPanel, "Inventory");
        contentPane.add(employeePanel, "Employees");
        contentPane.add(customerPanel, "Customers");
        
        String username = (user != null) ? user.getUsername() : "User";
        role = (user != null) ? user.getUserRole() : "";
        
        // Initialize role-specific landing pages
        landingMarketing = new FrmLandPageMarketing(username);
        landingSales = new FrmLandPageSales(username);
        landingAdmin = new FrmLandPageAdmin(username);
        
        contentPane.add(landingMarketing, "LandingMarketing");
        contentPane.add(landingSales, "LandingSales");
        contentPane.add(landingAdmin, "LandingAdmin");
        
        // Set up the menu bar using role-specific logic
        createMenuBar();
        
        // Show the appropriate landing page based on role
        if (role.equalsIgnoreCase("Marketing")) {
            showCard("LandingMarketing");
        } else if (role.equalsIgnoreCase("Sales")) {
            showCard("LandingSales");
        } else if (role.equalsIgnoreCase("Admin")) {
            showCard("LandingAdmin");
        }
    }
    
    /**
     * Switch between cards.
     */
    public void showCard(String cardName) {
        cardLayout.show(contentPane, cardName);
    }
    
    public void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(135, 108, 52), null, null, null));
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(217, 166, 165));
        menuBar.setForeground(new Color(194, 163, 97));
        
        // File Menu (always visible)
        JMenu file = new JMenu("File");
        file.setForeground(new Color(190, 86, 102));
        file.setMnemonic(KeyEvent.VK_F);
        JMenuItem signOutItem = new JMenuItem("SignOut");
        signOutItem.setMnemonic(KeyEvent.VK_S);
        signOutItem.setToolTipText("Sign out");
        signOutItem.addActionListener((ActionEvent event) -> {
            LoginPage lg = new LoginPage();
            lg.setVisible(true);
            dispose(); // Close current frame
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener((ActionEvent event) -> System.exit(0));
        file.add(signOutItem);
        file.add(exitItem);
        menuBar.add(file);
        
        // Role-specific landing page menu (always visible)
        if (role.equalsIgnoreCase("Admin")) {
            JMenu mnAdmin = new JMenu("Admin");
            mnAdmin.setForeground(new Color(190, 86, 102));
            mnAdmin.addMenuListener(new MenuListener() {
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
                public void menuSelected(MenuEvent e) {
                    showCard("LandingAdmin");
                }
            });
            menuBar.add(mnAdmin);
        } else if (role.equalsIgnoreCase("Marketing")) {
            JMenu mnMarketing = new JMenu("Marketing");
            mnMarketing.setForeground(new Color(190, 86, 102));
            mnMarketing.addMenuListener(new MenuListener() {
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
                public void menuSelected(MenuEvent e) {
                    showCard("LandingMarketing");
                }
            });
            menuBar.add(mnMarketing);
        } else if (role.equalsIgnoreCase("Sales")) {
            JMenu mnSales = new JMenu("Sales");
            mnSales.setForeground(new Color(190, 86, 102));
            mnSales.addMenuListener(new MenuListener() {
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
                public void menuSelected(MenuEvent e) {
                    showCard("LandingSales");
                }
            });
            menuBar.add(mnSales);
        }
        
        // Add menus based on role restrictions:
        // For Sales: Hide Producers, Employees, Inventory, and Users.
        // For Marketing: Hide Orders and Users.
        // For Admin: Show all menus.
        
        // Manufacturers (Producers) Menu (hide for Sales)
        if (!role.equalsIgnoreCase("Sales")) {
            JMenu mnManufacturers = new JMenu("Producers");
            mnManufacturers.setForeground(new Color(190, 86, 102));
            mnManufacturers.addMenuListener(new MenuListener() {
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
                public void menuSelected(MenuEvent e) {
                    showCard("Manufacturers");
                }
            });
            menuBar.add(mnManufacturers);
        }
        
        // Customers Menu (always visible)
        JMenu mnCustomers = new JMenu("Customers");
        mnCustomers.setForeground(new Color(190, 86, 102));
        mnCustomers.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                showCard("Customers");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        menuBar.add(mnCustomers);
        
        // Employees Menu (hide for Sales)
        if (!role.equalsIgnoreCase("Sales")) {
            JMenu mnEmployees = new JMenu("Employees");
            mnEmployees.setForeground(new Color(190, 86, 102));
            mnEmployees.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    showCard("Employees");
                }
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
            });
            menuBar.add(mnEmployees);
        }
        
        // Inventory Menu (hide for Sales)
        if (!role.equalsIgnoreCase("Sales")) {
            JMenu mnInventory = new JMenu("Inventory");
            mnInventory.setForeground(new Color(190, 86, 102));
            mnInventory.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    showCard("Inventory");
                }
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
            });
            menuBar.add(mnInventory);
        }
        
        // Orders Menu (hide for Marketing)
        if (!role.equalsIgnoreCase("Marketing")) {
            JMenu mnOrders = new JMenu("Orders");
            mnOrders.setForeground(new Color(190, 86, 102));
            mnOrders.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    showCard("Orders");
                }
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
            });
            menuBar.add(mnOrders);
        }
        
        // Wines Menu (always visible)
        JMenu mnWines = new JMenu("Wines");
        mnWines.setForeground(new Color(190, 86, 102));
        mnWines.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                showCard("Wines");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        menuBar.add(mnWines);
        
        // WineTypes Menu (always visible)
        JMenu mnWineTypes = new JMenu("WineTypes");
        mnWineTypes.setForeground(new Color(190, 86, 102));
        mnWineTypes.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                showCard("WineTypes");
            }
            public void menuCanceled(MenuEvent e) {}
            public void menuDeselected(MenuEvent e) {}
        });
        menuBar.add(mnWineTypes);
        
        // Users Menu (hide for Sales and Marketing)
        if (!(role.equalsIgnoreCase("Sales") || role.equalsIgnoreCase("Marketing"))) {
            JMenu mnUsers = new JMenu("Users");
            mnUsers.setForeground(new Color(190, 86, 102));
            mnUsers.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    showCard("Users");
                }
                public void menuCanceled(MenuEvent e) {}
                public void menuDeselected(MenuEvent e) {}
            });
            menuBar.add(mnUsers);
        }
        
        // Notification Menu (always at the end)
        notificationMenu = new JMenu();
        notificationMenu.setIcon(new ImageIcon(getClass().getResource("/boundary/images/bell.png")));
        notificationMenu.setToolTipText("Notifications");
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(notificationMenu);
        
        setJMenuBar(menuBar);
    }
    
    public void addNotification(String message) {
        JMenuItem notificationItem = new JMenuItem(message);
        notificationItem.setEnabled(false);
        notificationMenu.add(notificationItem);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a dummy user for testing purposes.
            User dummyUser = new User(1, 100, "TestUser", "password", "Admin");
            dummyUser.setUsername("TestUser");
            dummyUser.setUserRole("Admin"); // Change to "Marketing" or "Sales" to test different views
            RootLayout rootLayout = new RootLayout(dummyUser);
            rootLayout.setLocationRelativeTo(null); // Centers the frame on the screen
            rootLayout.setVisible(true);
        });
    }
}
