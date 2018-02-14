package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel{
    private Controller controller;

    // Create all buttons
    private JButton AdHocCarDay = new JButton("NormalWeekday+");
    private JButton ParkingPassDay = new JButton("PassWeekday+");
    private JButton AdHocCarEnd = new JButton("NormalWeekend+");
    private JButton ParkingPassEnd = new JButton("PassWeekend+");
    private JButton Reservations = new JButton("Reservations+");

    /**
     * Constructor for objects of class ButtonView
     */
    public SettingsView(Controller controller) {
        this.controller = controller;
        setLayout(new FlowLayout());

        // All buttons need to be added to the buttonView
        add(AdHocCarDay);
        add(ParkingPassDay);
        add(AdHocCarEnd);
        add(ParkingPassEnd);
        add(Reservations);
        // ActionListeners; all lambdas reference the Controller
        AdHocCarDay.addActionListener(e -> controller.incrementAdHocCarDay());
        ParkingPassDay.addActionListener(e -> controller.incrementParkingPassDay());
        AdHocCarEnd.addActionListener(e -> controller.incrementAdHocCarEnd());
        ParkingPassEnd.addActionListener(e -> controller.incrementParkingPassEnd());
        Reservations.addActionListener(e -> controller.incrementReservations());
    }
}
