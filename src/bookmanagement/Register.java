package bookmanagement;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Register extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton        btnSubmit;
    private JButton        btnClear;
    private UserDAO        userDAO;
    private Login          loginForm;

    public Register(Login loginForm) {
        this.loginForm = loginForm;
        try { userDAO = new UserDAO(); }
        catch (SQLException e) { JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage()); }
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        setTitle("BookManager — Create Account");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.BG_PAGE);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BG_PAGE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        wrapper.add(buildCard());
        add(wrapper);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { goBackToLogin(); }
        });
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_SURFACE);
        card.setBorder(UITheme.cardBorder());

        card.add(logoRow());
        card.add(Box.createVerticalStrut(18));

        JLabel t = new JLabel("Create account");
        t.setFont(UITheme.FONT_TITLE); t.setForeground(UITheme.TEXT_PRIMARY); t.setAlignmentX(LEFT_ALIGNMENT);
        card.add(t);
        JLabel s = new JLabel("Fill in your details below");
        s.setFont(UITheme.FONT_SMALL); s.setForeground(UITheme.TEXT_SECONDARY); s.setAlignmentX(LEFT_ALIGNMENT);
        card.add(s);
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
        card.add(Box.createVerticalStrut(12));

        card.add(UITheme.fieldLabel("Confirm Password"));
        card.add(Box.createVerticalStrut(4));
        txtConfirmPassword = UITheme.styledPasswordField(20);
        txtConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtConfirmPassword.setAlignmentX(LEFT_ALIGNMENT);
        card.add(txtConfirmPassword);
        card.add(Box.createVerticalStrut(6));

        JLabel hint = new JLabel("* Max 10 characters for each field");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(UITheme.TEXT_SECONDARY);
        hint.setAlignmentX(LEFT_ALIGNMENT);
        card.add(hint);
        card.add(Box.createVerticalStrut(20));

        btnSubmit = UITheme.primaryButton("Create Account");
        btnSubmit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnSubmit.setAlignmentX(LEFT_ALIGNMENT);
        card.add(btnSubmit);
        card.add(Box.createVerticalStrut(10));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 8, 0));
        btnRow.setBackground(UITheme.BG_SURFACE);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnRow.setAlignmentX(LEFT_ALIGNMENT);
        btnClear = UITheme.ghostButton("Clear");
        JButton btnBack = UITheme.ghostButton("← Back to Login");
        btnBack.setForeground(UITheme.ACCENT);
        btnBack.addActionListener(e -> goBackToLogin());
        btnRow.add(btnClear);
        btnRow.add(btnBack);
        card.add(btnRow);
        return card;
    }

    private JPanel logoRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(UITheme.BG_SURFACE);
        row.setAlignmentX(LEFT_ALIGNMENT);
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.ACCENT);
                g2.fillRoundRect(0, 0, 30, 30, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRect(5, 5, 12, 8); g2.drawRect(5, 15, 8, 8); g2.drawRect(15, 15, 8, 8);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(30, 30)); p.setOpaque(false);
        row.add(p); row.add(Box.createHorizontalStrut(10));
        JLabel logo = new JLabel("BookManager");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logo.setForeground(UITheme.TEXT_PRIMARY);
        row.add(logo);
        return row;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(UITheme.BORDER);
        return sep;
    }

    private void setupListeners() {
        btnSubmit.addActionListener(e -> handleSubmit());
        btnClear.addActionListener(e -> { txtUsername.setText(""); txtPassword.setText(""); txtConfirmPassword.setText(""); txtUsername.requestFocus(); });
    }

    private void handleSubmit() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirm  = new String(txtConfirmPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) { warn("All fields are required."); return; }
        if (username.length() > 10) { warn("Username must not exceed 10 characters."); return; }
        if (password.length() > 10) { warn("Password must not exceed 10 characters."); return; }
        if (!password.equals(confirm)) { warn("Passwords do not match."); txtConfirmPassword.setText(""); txtPassword.setText(""); txtPassword.requestFocus(); return; }

        try {
            if (userDAO.usernameExists(username)) { warn("Username '" + username + "' is already taken."); return; }
            if (userDAO.add(new User(username, password))) {
                JOptionPane.showMessageDialog(this, "Registration successful! You may now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                goBackToLogin();
            } else { error("Registration failed. Please try again."); }
        } catch (SQLException ex) { error("Database error: " + ex.getMessage()); }
    }

    private void goBackToLogin() { loginForm.setVisible(true); dispose(); }
    private void warn(String m)  { JOptionPane.showMessageDialog(this, m, "Notice", JOptionPane.WARNING_MESSAGE); }
    private void error(String m) { JOptionPane.showMessageDialog(this, m, "Error",  JOptionPane.ERROR_MESSAGE); }
}
