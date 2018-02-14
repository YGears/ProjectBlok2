package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GeldView extends JPanel{

    private JLabel nonParkingPass;
    private JLabel parkingPass;
    private JLabel adHocCar;
    JFrame frame = new JFrame("Statistics");

    private double nonParkingPassGeld;
    private int parkingPassAantal;
    private int adHocCarsAantal;



    public GeldView(){
        // de nieuwe FlowLayout
        setLayout(new FlowLayout());
// Hier vragen we de double geld op
        nonParkingPassGeld = Controller.getFinances();
        // de uitkomst voor deze regel, er wordt een gepaste zin gemaakt
        nonParkingPass = new JLabel("The money made from non-parking pass users: $ "+nonParkingPassGeld+".");
        //wat er gebeurd wanneer je hovert op de bovenstaande zin
        nonParkingPass.setToolTipText("These people do not have a subscription.");
        add(nonParkingPass);
        //De aantallen voor AdHocCars
        adHocCarsAantal = Controller.getAdHocCarsAantal();
        adHocCar = new JLabel("There have been " + adHocCarsAantal + " cars WITHOUT a parking pass in total.");
        adHocCar.setToolTipText("These people do not have a subscription.");
        add(adHocCar);
//Hetzelfde als voor nonParkingPass
        parkingPassAantal = Controller.getParkingPassAantal();
        parkingPass = new JLabel("There have been " + parkingPassAantal + " cars WITH a parking pass in total.");
        parkingPass.setToolTipText("These people do have a subscription.");
        add(parkingPass);




    }
    public static void CreateAndShowGeld(){
        GeldView pakGeld = new GeldView();
        pakGeld.setPreferredSize(new Dimension(400 , 150));
        JFrame frame = new JFrame("Statistics");

        //nu runt ie ook als je de grafiek hebt gesloten? als die exit on close er wel was stopt de hele simulatie
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(pakGeld);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
