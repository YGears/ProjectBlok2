package view;

import controller.Controller;
import model.Model;

import javax.swing.*;

public abstract class AbstractView extends JFrame
{
    protected Model model;
    protected Controller controller;

    public AbstractView()
    {

    }

    public void setController(Controller controller)
    {
        this.controller = controller;
    }


    public void updateView() {
        repaint();
    }
}
