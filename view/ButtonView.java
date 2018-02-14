package view;

//import com.sun.corba.se.impl.orbutil.graph.Graph;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ButtonView extends JPanel
{
    private Controller controller;

    // Create all buttons
    private JButton start = new JButton("Start");
    private JButton stop = new JButton("Stop");
    private JButton minute = new JButton("+1 Minute");
    private JButton hour = new JButton("+1 Hour");
    private JButton day = new JButton("+1 Day");
    private JButton graph = new JButton("Graph");
    private JButton statistics = new JButton("Statistics");
    private JButton piechart = new JButton("Piechart");

    /**
     * Constructor for objects of class ButtonView
     */
    public ButtonView(Controller controller) {
        this.controller = controller;
        setLayout(new FlowLayout());

        // All buttons need to be added to the buttonView
        add(start);
        add(stop);
        add(minute);
        add(hour);
        add(day);
        add(statistics);
        add(graph);
        add(piechart);
        // ActionListeners; all lambdas reference the Controller
        start.addActionListener(e -> controller.start());
        stop.addActionListener(e -> controller.stop());
        minute.addActionListener(e -> controller.manualTick(1));
        hour.addActionListener(e -> controller.manualTick(60));
        day.addActionListener(e -> controller.manualTick(1440));

        graph.addActionListener(e -> controller.graph());
        statistics.addActionListener(e -> controller.finances());
        piechart.addActionListener(e -> controller.pieChart());
    }
}