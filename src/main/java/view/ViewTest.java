// src/main/UI/MainView.java
package view;
import utility.FontLoader;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ViewTest {
    public static void main(String[] args) {
        FontLoader.registerFonts();

        SwingUtilities.invokeLater(() -> {
            FontLoader.registerFonts();
            JFrame frame = new JFrame("Main Screen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setResizable(false); // Fixed size window
            frame.setLayout(new BorderLayout(10, 10));

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
            // Search field
            JTextField searchField = new JTextField();
            panel.add(searchField, BorderLayout.NORTH);

            // Scrollable list
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> list = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(list);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Sample data
            String[] courses = {"Math", "Physics", "Chemistry", "Biology"};
            for (String c : courses) listModel.addElement(c);

            // Filtering
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                void update() {
                    String text = searchField.getText().toLowerCase();
                    listModel.clear();
                    for (String c : courses)
                        if (c.toLowerCase().contains(text))
                            listModel.addElement(c);
                }
                public void insertUpdate(DocumentEvent e) { update(); }
                public void removeUpdate(DocumentEvent e) { update(); }
                public void changedUpdate(DocumentEvent e) { update(); }
            });
            frame.add(panel, BorderLayout.CENTER);
            // ---------- Finish ----------
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}
