package model;

import view.AbstractView;

import java.util.List;


public abstract class AbstractModel
{
    private List<AbstractView> views;


    public void addView(AbstractView view){ views.add(view);}
}
