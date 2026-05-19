package bookmanagement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookRegistration extends JFrame {

    // ── Fields ────────────────────────────────────────────────
    private JTextField       txtBookId;
    private JTextField       txtBookTitle;
    private JTextField       txtAuthor;
    private JTextField       txtCategory;
    private JComboBox<String> cmbStatus;       // dropdown instead of text field
    private JTextField       txtSearch;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;
    private JButton btnLogout;

    private JTable            tblBooks;
    private DefaultTableModel tableModel;

    // Stats labels
    private JLabel lblStatTotal;
    private JLabel lblStatAvail;
    private JLabel lblStatBorrow;

    private BookDAO bookDAO;
    private User    currentUser;

    public BookRegistration(User user) {
        this.currentUser = user;
        try { bookDAO = new BookDAO(); }
        catch (SQLException e) { JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage()); }
        initComponents();
        setupListeners();
        loadAllBooks();
    }

    private void initComponents() {
        setTitle("BookManager — Book Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 550));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        add(root);
    }

    // ── Top bar ───────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UITheme.BG_SIDEBAR);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Left: logo + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        JPanel iconP = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.ACCENT);
                g2.fillRoundRect(0, 0, 28, 28, 7, 7);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(4, 4, 11, 8);
                g2.drawRect(4, 14, 7, 8);
                g2.drawRect(14, 14, 8, 8);
                g2.dispose();
            }
        };
        iconP.setPreferredSize(new Dimension(28, 28));
        iconP.setOpaque(false);

        JLabel appName = new JLabel("BookManager");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        appName.setForeground(Color.WHITE);

        JLabel sep = new JLabel("  /  ");
        sep.setForeground(new Color(255, 255, 255, 60));
        sep.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel page = new JLabel("Book Registration");
        page.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        page.setForeground(new Color(255, 255, 255, 180));

        left.add(iconP); left.add(appName); left.add(sep); left.add(page);
        bar.add(left, BorderLayout.WEST);

        // Right: avatar + welcome + logout
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x3B82F6));
                g2.fillOval(0, 0, getWidth(), getHeight());
                String initials = currentUser.getUsername()
                    .substring(0, Math.min(2, currentUser.getUsername().length())).toUpperCase();
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initials,
                    (getWidth()  - fm.stringWidth(initials)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(28, 28));
        avatar.setOpaque(false);

        JLabel welcome = new JLabel("Welcome, " + currentUser.getUsername());
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        welcome.setForeground(new Color(255, 255, 255, 180));

        btnLogout = new JButton("Logout") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnLogout.setForeground(new Color(255, 255, 255, 200));
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(70, 26));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        right.add(avatar); right.add(welcome); right.add(btnLogout);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── Center content ────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(UITheme.BG_PAGE);
        center.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        center.add(buildStatsRow(), BorderLayout.NORTH);
        center.add(buildMainPanel(), BorderLayout.CENTER);
        return center;
    }

    // ── Stats row ─────────────────────────────────────────────
    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 12, 0));
        row.setOpaque(false);

        lblStatTotal  = new JLabel("0");
        lblStatAvail  = new JLabel("0");
        lblStatBorrow = new JLabel("0");

        row.add(statCard("Total Books", lblStatTotal,  UITheme.ACCENT));
        row.add(statCard("Available",   lblStatAvail,  UITheme.SUCCESS));
        row.add(statCard("Borrowed",    lblStatBorrow, UITheme.WARNING));
        return row;
    }

    private JPanel statCard(String label, JLabel valLabel, Color valColor) {
        JPanel card = new JPanel(new BorderLayout(6, 4));
        card.setBackground(UITheme.BG_SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLabel.setForeground(valColor);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        card.add(valLabel, BorderLayout.CENTER);
        card.add(lbl, BorderLayout.SOUTH);
        return card;
    }

    // ── Main panel ────────────────────────────────────────────
    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.add(buildFormPanel(), BorderLayout.NORTH);
        panel.add(buildSearchAndTable(), BorderLayout.CENTER);
        return panel;
    }

    // ── Form panel ────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UITheme.BG_SURFACE);
        card.setBorder(UITheme.sectionBorder("Book Details"));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Row 0: Book ID + Status dropdown
        g.gridy = 0;
        addFormField(fields, g, 0, "Book ID", txtBookId = UITheme.styledTextFieldReadOnly(8));

        // Status label
        g.gridx = 2; g.gridwidth = 1; g.weightx = 0;
        fields.add(UITheme.fieldLabel("Status"), g);

        // Status combobox
        cmbStatus = new JComboBox<>(new String[]{"Available", "Borrowed"});
        cmbStatus.setFont(UITheme.FONT_INPUT);
        cmbStatus.setBackground(UITheme.BG_INPUT);
        cmbStatus.setPreferredSize(new Dimension(150, 32));
        cmbStatus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        g.gridx = 3; g.weightx = 0.5;
        fields.add(cmbStatus, g);

        // Row 1: Title (full width)
        g.gridy = 1; g.gridx = 0; g.gridwidth = 1; g.weightx = 0;
        fields.add(UITheme.fieldLabel("Title"), g);
        g.gridx = 1; g.gridwidth = 3; g.weightx = 1;
        txtBookTitle = UITheme.styledTextField(30);
        fields.add(txtBookTitle, g);

        // Row 2: Author + Category
        g.gridwidth = 1;
        g.gridy = 2;
        addFormField(fields, g, 0, "Author",   txtAuthor   = UITheme.styledTextField(20));
        addFormField(fields, g, 2, "Category", txtCategory = UITheme.styledTextField(15));

        card.add(fields, BorderLayout.CENTER);
        card.add(buildFormButtons(), BorderLayout.SOUTH);
        return card;
    }

    private void addFormField(JPanel p, GridBagConstraints g, int col, String label, JTextField field) {
        g.gridx = col; g.gridwidth = 1; g.weightx = 0;
        p.add(UITheme.fieldLabel(label), g);
        g.gridx = col + 1; g.weightx = 0.5;
        p.add(field, g);
    }

    private JPanel buildFormButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        btnAdd    = UITheme.colorButton("Add",    UITheme.ACCENT);
        btnUpdate = UITheme.colorButton("Update", UITheme.SUCCESS);
        btnDelete = UITheme.colorButton("Delete", UITheme.DANGER);
        btnClear  = UITheme.ghostButton("Clear");
        row.add(btnAdd);
        row.add(btnUpdate);
        row.add(btnDelete);
        row.add(btnClear);
        return row;
    }

    // ── Search + Table ────────────────────────────────────────
    private JPanel buildSearchAndTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        // Search bar
        JPanel searchLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchLeft.setOpaque(false);
        JLabel lblSearch = new JLabel("Search by title:");
        lblSearch.setFont(UITheme.FONT_LABEL);
        lblSearch.setForeground(UITheme.TEXT_SECONDARY);
        txtSearch = UITheme.styledTextField(22);
        txtSearch.setPreferredSize(new Dimension(240, 32));
        btnRefresh = UITheme.ghostButton("Refresh");
        searchLeft.add(lblSearch);
        searchLeft.add(txtSearch);
        searchLeft.add(btnRefresh);
        panel.add(searchLeft, BorderLayout.NORTH);

        // Table
        String[] cols = {"Book ID", "Title", "Author", "Category", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBooks = new JTable(tableModel);
        styleTable();

        JScrollPane scroll = new JScrollPane(tblBooks);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_SURFACE);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void styleTable() {
        tblBooks.setFont(UITheme.FONT_TABLE);
        tblBooks.setForeground(UITheme.TEXT_PRIMARY);
        tblBooks.setRowHeight(30);
        tblBooks.setShowGrid(false);
        tblBooks.setIntercellSpacing(new Dimension(0, 0));
        tblBooks.setSelectionBackground(UITheme.TABLE_ROW_SELECTED);
        tblBooks.setSelectionForeground(UITheme.TEXT_PRIMARY);
        tblBooks.setBackground(UITheme.BG_SURFACE);
        tblBooks.getTableHeader().setFont(UITheme.FONT_TABLE_H);
        tblBooks.getTableHeader().setBackground(UITheme.TABLE_HEADER_BG);
        tblBooks.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        tblBooks.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        tblBooks.getTableHeader().setPreferredSize(new Dimension(0, 32));

        // Column widths
        int[] widths = {70, 220, 160, 140, 100};
        for (int i = 0; i < widths.length; i++)
            tblBooks.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Custom renderer: alternating rows + status badge colors
        tblBooks.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (sel) {
                    c.setBackground(UITheme.TABLE_ROW_SELECTED);
                } else {
                    c.setBackground(row % 2 == 0 ? UITheme.BG_SURFACE : new Color(0xFAFAFB));
                }
                if (col == 4 && val != null) {
                    String v = val.toString().toLowerCase();
                    if (v.equals("available")) {
                        setForeground(new Color(0x166534));
                        setBackground(sel ? UITheme.TABLE_ROW_SELECTED : new Color(0xDCFCE7));
                    } else if (v.equals("borrowed")) {
                        setForeground(new Color(0x92400E));
                        setBackground(sel ? UITheme.TABLE_ROW_SELECTED : new Color(0xFEF3C7));
                    } else {
                        setForeground(UITheme.TEXT_SECONDARY);
                    }
                } else {
                    setForeground(col == 0 ? UITheme.TEXT_SECONDARY : UITheme.TEXT_PRIMARY);
                    setFont(col == 1 ? new Font("Segoe UI", Font.BOLD, 12) : UITheme.FONT_TABLE);
                }
                return c;
            }
        });
    }

    // ── Listeners ─────────────────────────────────────────────
    private void setupListeners() {
        btnAdd.addActionListener(e    -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e  -> handleClear());
        btnRefresh.addActionListener(e -> handleRefresh());
        btnLogout.addActionListener(e -> handleLogout());

        tblBooks.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFormFromTable();
        });

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { handleSearch(); }
            public void removeUpdate(DocumentEvent e)  { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });
    }

    private void handleAdd() {
        String title  = txtBookTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String cat    = txtCategory.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();

        if (title.isEmpty() || author.isEmpty() || cat.isEmpty()) {
            warn("Please fill in all fields."); return;
        }
        try {
            // ── NEW: duplicate title check ──
            if (bookDAO.titleExists(title)) {
                warn("\"" + title + "\" already exists. Duplicate titles are not allowed.");
                return;
            }
            if (bookDAO.add(new Book(title, author, cat, status))) {
                JOptionPane.showMessageDialog(this, "Book added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClear();
                loadAllBooks();
            } else warn("Failed to add book.");
        } catch (SQLException ex) { error(ex.getMessage()); }
    }

    private void handleUpdate() {
        if (txtBookId.getText().trim().isEmpty()) {
            warn("Select a book from the table first."); return;
        }
        String title  = txtBookTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String cat    = txtCategory.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();

        if (title.isEmpty() || author.isEmpty() || cat.isEmpty()) {
            warn("All fields are required."); return;
        }
        if (JOptionPane.showConfirmDialog(this, "Update this book?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            int id = Integer.parseInt(txtBookId.getText().trim());
            if (bookDAO.update(new Book(id, title, author, cat, status))) {
                JOptionPane.showMessageDialog(this, "Book updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClear();
                loadAllBooks();
            } else warn("Update failed.");
        } catch (SQLException ex) { error(ex.getMessage()); }
    }

    private void handleDelete() {
        if (txtBookId.getText().trim().isEmpty()) {
            warn("Select a book from the table first."); return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete this book? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        try {
            int id = Integer.parseInt(txtBookId.getText().trim());
            if (bookDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Book deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                handleClear();
                loadAllBooks();
            } else warn("Delete failed.");
        } catch (SQLException ex) { error(ex.getMessage()); }
    }

    private void handleClear() {
        txtBookId.setText("");
        txtBookTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        cmbStatus.setSelectedIndex(0);   // resets to "Available"
        tblBooks.clearSelection();
        txtBookTitle.requestFocus();
    }

    private void handleRefresh() {
        txtSearch.setText("");
        loadAllBooks();
    }

    private void handleSearch() {
        String kw = txtSearch.getText().trim();
        try {
            List<Book> books = kw.isEmpty() ? bookDAO.getAll() : bookDAO.search(kw);
            populateTable(books);
        } catch (SQLException ex) { error(ex.getMessage()); }
    }

    private void handleLogout() {
        if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            dispose();
        }
    }

    // ── Helpers ───────────────────────────────────────────────
    private void loadAllBooks() {
        try {
            List<Book> books = bookDAO.getAll();
            populateTable(books);
            updateStats(books);
        } catch (SQLException ex) { error(ex.getMessage()); }
    }

    private void populateTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books)
            tableModel.addRow(new Object[]{
                b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getStatus()
            });
    }

    private void updateStats(List<Book> books) {
        long avail  = books.stream().filter(b -> b.getStatus().equalsIgnoreCase("Available")).count();
        long borrow = books.stream().filter(b -> b.getStatus().equalsIgnoreCase("Borrowed")).count();
        lblStatTotal.setText(String.valueOf(books.size()));
        lblStatAvail.setText(String.valueOf(avail));
        lblStatBorrow.setText(String.valueOf(borrow));
    }

    private void populateFormFromTable() {
        int row = tblBooks.getSelectedRow();
        if (row < 0) return;
        txtBookId.setText(tableModel.getValueAt(row, 0).toString());
        txtBookTitle.setText(tableModel.getValueAt(row, 1).toString());
        txtAuthor.setText(tableModel.getValueAt(row, 2).toString());
        txtCategory.setText(tableModel.getValueAt(row, 3).toString());
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
    }

    private void warn(String m)  { JOptionPane.showMessageDialog(this, m, "Notice", JOptionPane.WARNING_MESSAGE); }
    private void error(String m) { JOptionPane.showMessageDialog(this, m, "Error",  JOptionPane.ERROR_MESSAGE); }
}