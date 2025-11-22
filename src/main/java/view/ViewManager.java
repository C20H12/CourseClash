package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel, JFrame application) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;

        // Register the listener
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // FIX: Listen to both "view" (manual) and "state" (default from ViewModel)
        if (evt.getPropertyName().equals("view") || evt.getPropertyName().equals("state")) {

            // The newValue should be the View Name (String)
            if (evt.getNewValue() instanceof String) {
                String viewModelName = (String) evt.getNewValue();

                System.out.println("VIEW MANAGER: Switching to " + viewModelName);
                cardLayout.show(views, viewModelName);
            }
        }
    }
}