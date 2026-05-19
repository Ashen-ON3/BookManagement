package bookmanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;
    private JButton        btnClear;
    private JButton        btnRegister;
    private UserDAO        userDAO;

    public Login() {
        try { userDAO = new UserDAO(); }
        catch (SQLException e) { showError("Database connection failed:\n" + e.getMessage()); }
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        setTitle("BookManager — Sign In");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_PAGE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BG_PAGE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        wrapper.add(buildCard());
        add(wrapper);
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_SURFACE);
        card.setBorder(UITheme.cardBorder());

        card.add(logoRow());
        card.add(Box.createVerticalStrut(18));
        addHeading(card, "Welcome back", "Sign in to your account to continue");
        card.add(Box.createVerticalStrut(16));
        card.add(divider());
        card.add(Box.createVerticalStrut(16));

        card.add(UITheme.fieldLabel("Username"));
        card.add(Box.createVerticalStrut(4));
        txtUsername = UITheme.styledTextField(20);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtUsername.setAlignmentX(LEFT_ALIGNMENT);
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));

        card.add(UITheme.fieldLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        txtPassword = UITheme.styledPasswordField(20);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtPassword.setAlignmentX(LEFT_ALIGNMENT);
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(20));

        btnLogin = UITheme.primaryButton("Sign In");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnLogin.setAlignmentX(LEFT_ALIGNMENT);
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 8, 0));
        btnRow.setBackground(UITheme.BG_SURFACE);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnRow.setAlignmentX(LEFT_ALIGNMENT);
        btnClear    = UITheme.ghostButton("Clear");
        btnRegister = UITheme.ghostButton("Register →");
        btnRegister.setForeground(UITheme.ACCENT);
        btnRow.add(btnClear);
        btnRow.add(btnRegister);
        card.add(btnRow);
        return card;
    }

    private JPanel logoRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(UITheme.BG_SURFACE);
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.add(iconBox());
        row.add(Box.createHorizontalStrut(10));
        JLabel logoText = new JLabel("BookManager");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoText.setForeground(UITheme.TEXT_PRIMARY);
        row.add(logoText);
        return row;
    }

    private JPanel iconBox() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.ACCENT);
                g2.fillRoundRect(0, 0, 30, 30, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRect(5, 5, 12, 8);
                g2.drawRect(5, 15, 8, 8);
                g2.drawRect(15, 15, 8, 8);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(30, 30));
        p.setOpaque(false);
        return p;
    }

    private void addHeading(JPanel card, String title, String sub) {
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_TITLE);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setAlignmentX(LEFT_ALIGNMENT);
        card.add(t);
        JLabel s = new JLabel(sub);
        s.setFont(UITheme.FONT_SMALL);
        s.setForeground(UITheme.TEXT_SECONDARY);
        s.setAlignmentX(LEFT_ALIGNMENT);
        card.add(s);
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(UITheme.BORDER);
        return sep;
    }

    private void setupListeners() {
        btnLogin.addActionListener(e -> handleLogin());
        btnClear.addActionListener(e -> { txtUsername.setText(""); txtPassword.setText(""); txtUsername.requestFocus(); });
        btnRegister.addActionListener(e -> { new Register(this).setVisible(true); setVisible(false); });
        txtPassword.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) { showWarn("Please enter both username and password."); return; }
        try {
            User user = userDAO.login(username, password);
            if (user != null) { new BookRegistration(user).setVisible(true); setVisible(false); }
            else { showWarn("Invalid username or password."); txtPassword.setText(""); txtUsername.requestFocus(); }
        } catch (SQLException ex) { showError("Database error: " + ex.getMessage()); }
    }

    private void showWarn(String m)  { JOptionPane.showMessageDialog(this, m, "Notice", JOptionPane.WARNING_MESSAGE); }
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Error",  JOptionPane.ERROR_MESSAGE); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new Login().setVisible(true);
        });
    }
}
