package bookmanagement;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Centralized UI theme constants and factory methods.
 * Keeps all forms visually consistent.
 */
public class UITheme {

    // ── Palette ──────────────────────────────────────────────
    public static final Color BG_PAGE       = new Color(0xF5F6F8);
    public static final Color BG_SURFACE    = Color.WHITE;
    public static final Color BG_INPUT      = new Color(0xF8F9FB);
    public static final Color BG_SIDEBAR    = new Color(0x1A1F36);
    public static final Color BG_TOPBAR     = Color.WHITE;

    public static final Color ACCENT        = new Color(0x2563EB);
    public static final Color ACCENT_HOVER  = new Color(0x1D4ED8);
    public static final Color SUCCESS       = new Color(0x0F766E);
    public static final Color DANGER        = new Color(0xDC2626);
    public static final Color WARNING       = new Color(0xD97706);
    public static final Color MUTED         = new Color(0x64748B);

    public static final Color TEXT_PRIMARY   = new Color(0x1A1F36);
    public static final Color TEXT_SECONDARY = new Color(0x8892A4);

    public static final Color BORDER        = new Color(0xE2E6EF);
    public static final Color BORDER_INPUT  = new Color(0xDDE1EA);

    public static final Color TABLE_HEADER_BG   = new Color(0xF8F9FB);
    public static final Color TABLE_ROW_SELECTED = new Color(0xEFF6FF);
    public static final Color TABLE_ROW_HOVER    = new Color(0xF8F9FB);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BOLD_SM = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_H = new Font("Segoe UI", Font.BOLD, 11);

    // ── Borders ───────────────────────────────────────────────
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(20, 24, 20, 24)
        );
    }

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_INPUT, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }

    public static Border inputBorderFocus() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }

    public static Border sectionBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER, 1), title
        );
        tb.setTitleFont(FONT_BOLD_SM);
        tb.setTitleColor(MUTED);
        return BorderFactory.createCompoundBorder(tb,
            BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    // ── Component Factories ───────────────────────────────────

    public static JTextField styledTextField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(BG_INPUT);
        tf.setBorder(inputBorder());
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 32));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                tf.setBorder(inputBorderFocus());
                tf.setBackground(new Color(0xEFF6FF));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                tf.setBorder(inputBorder());
                tf.setBackground(BG_INPUT);
            }
        });
        return tf;
    }

    public static JPasswordField styledPasswordField(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(FONT_INPUT);
        pf.setForeground(TEXT_PRIMARY);
        pf.setBackground(BG_INPUT);
        pf.setBorder(inputBorder());
        pf.setPreferredSize(new Dimension(pf.getPreferredSize().width, 32));
        pf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                pf.setBorder(inputBorderFocus());
                pf.setBackground(new Color(0xEFF6FF));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                pf.setBorder(inputBorder());
                pf.setBackground(BG_INPUT);
            }
        });
        return pf;
    }

    public static JTextField styledTextFieldReadOnly(int cols) {
        JTextField tf = styledTextField(cols);
        tf.setEditable(false);
        tf.setBackground(new Color(0xF0F2F5));
        tf.setForeground(TEXT_SECONDARY);
        return tf;
    }

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? ACCENT_HOVER :
                            getModel().isRollover() ? ACCENT_HOVER : ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton colorButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? color.darker() : color;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(95, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xF0F2F5) : BG_SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(MUTED);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(95, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(MUTED);
        return lbl;
    }
}
