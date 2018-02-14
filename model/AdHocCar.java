package model;

import java.awt.*;
import java.util.Random;

public class AdHocCar extends Car
{
    private static int geld;
    private static final Color COLOR = Color.red;

    public AdHocCar()
    {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        geld = (stayMinutes/60) * 2;
    }

    public Color getColor()
    {
        return COLOR;
    }
    public static int getGeld(){ return geld;}
}

