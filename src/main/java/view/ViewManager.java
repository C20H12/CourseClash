package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;
    private final JFrame frame;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel, JFrame frame) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
        this.frame = frame;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final String viewModelName = (String) evt.getNewValue();
            SwingUtilities.invokeLater(() -> {
                cardLayout.show(views, viewModelName);
                if ("log in".equals(viewModelName)) {
                    frame.setSize(300, 200);
                    frame.setLocationRelativeTo(null);
                } else {
                    frame.setSize(1200, 800);
                    frame.setLocationRelativeTo(null);
                }
                frame.revalidate();
                frame.repaint();
            });
        }
    }
}
