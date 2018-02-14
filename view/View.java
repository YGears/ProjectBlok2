package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class View extends AbstractView
{
    private Controller controller;

    private CarParkView carParkView;

    public View()
    {
        // This constructor is purposely left empty.
    }

    public void startView()
    {
        // Initiate the necessary Views
        // Contains the simulator
        carParkView = new CarParkView(controller);

        SettingsView settingsView = new SettingsView(controller);

        // Contains all buttons
        ButtonView buttonView = new ButtonView(controller);

        // Contains the slider
        SliderView sliderView = new SliderView(controller);
        //Controller.set
        // TO-DO: finalize a layout

        // Create a Container
        Container contentPane = getContentPane();
        // Add all Views
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(buttonView, BorderLayout.SOUTH);
        contentPane.add(sliderView, BorderLayout.EAST);
        contentPane.add(settingsView, BorderLayout.NORTH);
  
        // Finalize
        pack();

        // Make everything visible and draw the parking lot
        setVisible(true);
        updateView();
      //application closes when this panel is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    public void updateView(int floors, int rows, int places)
    {
        carParkView.updateView(floors, rows, places);
    }


}