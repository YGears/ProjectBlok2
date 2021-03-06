package model;

import java.awt.*;
import java.util.Random;

public class Model extends AbstractModel
{
    private static double geld = 0;
    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private int weekDayArrivals = 100;    // average number of arriving cars per hour
    private int weekendArrivals = 200;    // average number of arriving cars per hour
    private int weekDayPassArrivals = 50; // average number of arriving cars per hour
    private int weekendPassArrivals = 30;// average number of arriving cars per hour
    private int enterSpeed = 3;           // number of cars that can enter per minute
    private int paymentSpeed = 7;         // number of cars that can pay per minute
    private int exitSpeed = 5;            // number of cars that can leave per minute
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Car[][][] cars;
    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private static int totaalPassCars;
    private static int totaalAdHocCars;

    private int reservations = 120;
    private boolean reservationBoolean = false;



    public Model()
    {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        setNumberOfFloors(3);
        setNumberOfRows(6);
        setNumberOfPlaces(30);

        numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;

        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    public void adHocCarDay(int i){
        weekDayArrivals += i;
    }
    public void adHocCarEnd(int i){
        weekendArrivals += i;
    }
    public void parkingPassDay(int i){
        weekDayPassArrivals += i;
    }
    public void parkingPassEnd(int i){
        weekendPassArrivals += i;
    }
    public void reservationsIncrement(int i){
     reservations += i;
    }
    public void tick()
    {
        advanceTime();
        handleExit();
    }

    public void tock()
    {
        handleYellow();
        handleEntrance();


    }

    private void advanceTime()
    {
        // Advance the time by one minute.
        minute++;
        while (minute > 59)
        {
            minute -= 60;
            hour++;
        }
        while (hour > 23)
        {
            hour -= 24;
            day++;
        }
        while (day > 6)
        {
            day -= 7;
        }

    }
    private String getTimeString()
    {
        String minutes;
        String hours;
        if (minute < 10)
        {
            minutes = "0" + minute;
        } else
        {
            minutes = "" + minute;
        }
        if (hour < 10)
        {
            hours = "0" + hour;
        } else
        {
            hours = "" + hour;
        }
        return hours + ":" + minutes;
    }
    private String getDayString()
    {
        String[] week = new String[]{"Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"};
        return week[day];
    }


    private void handleEntrance()
    {
        carsArriving();
        passEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);
    }

    private void handleExit()
    {
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    private void handleYellow()
    {
        if (reservationBoolean == false)
        {
            int i = 0;
            while (i < reservations)
            {
                Car car = new Yellow();
                Location freeLocation = getLastFreeLocation();
                setCarAt(freeLocation, car);
                i++;
            }
            reservationBoolean = true;
        }
    }

    private void carsArriving()
    {
        int numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);
        numberOfCars = getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
    }

    private void carsEntering(CarQueue queue)
    {
        int i = 0;
        // Remove car from the front of the queue and assign to a parking space.
        while (queue.carsInQueue() > 0 &&
                getNumberOfOpenSpots() > 0 &&
                i < enterSpeed)
        {
            Car car = queue.removeCar();
            Location freeLocation = getFirstFreeLocation();
            setCarAt(freeLocation, car);
            i++;
        }
    }
    private void passEntering(CarQueue queue)
{
    int i = 0;
    // Remove car from the front of the queue and assign to a parking space.
    while (queue.carsInQueue() > 0 &&
            getNumberOfOpenSpots() > 0 &&
            i < enterSpeed)
    {
        Car car = queue.removeCar();
        Location freeLocation = getFirstFreePassLocation();
        removeCarAt(freeLocation);
        setCarAt(freeLocation, car);
        i++;
    }
}
    private Location getFirstFreePassLocation()
{
    for (int floor = 0; floor < getNumberOfFloors(); floor++)
    {
        for (int row = 0; row < getNumberOfRows(); row++)
        {
            for (int place = 0; place < getNumberOfPlaces(); place++)
            {
                Location location = new Location(floor, row, place);
                if (getCarAt(location) != null && getCarAt(location).getColor() == Color.yellow)
                {
                    return location;
                }
            }
        }
    }


        return null;
    }


    private void carsReadyToLeave()
    {
        // Add leaving cars to the payment queue.
        Car car = getFirstLeavingCar();
        while (car != null)
        {
            if (car.getHasToPay())
            {
                car.setIsPaying(true);
                paymentCarQueue.addCar(car);
            } else
            {
                passLeavesSpot(car);
            }
            car = getFirstLeavingCar();
        }
    }

    private void carsPaying()
    {
        // Let cars pay.
        int i = 0;
        while (paymentCarQueue.carsInQueue() > 0 && i < paymentSpeed)
        {
            double betalen = AdHocCar.getGeld();
            geld += betalen;
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
        }
    }
    public static double getGeld(){return geld;}

    private void carsLeaving()
    {
        // Let cars leave.
        int i = 0;
        while (exitCarQueue.carsInQueue() > 0 && i < exitSpeed)
        {
            exitCarQueue.removeCar();
            i++;
        }
    }



    public int getNumberOfCars(int weekDay, int weekend)
    {
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int) Math.round(numberOfCarsPerHour / 60);
    }



    private void addArrivingCars(int numberOfCars, String type)
    {
        // Add the cars to the back of the queue.
        switch (type)
        {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++)
                {
                    entranceCarQueue.addCar(new AdHocCar());
                    totaalAdHocCars++;
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++)
                {
                    entrancePassQueue.addCar(new ParkingPassCar());
                    totaalPassCars++;
                }
                break;
        }
    }

    public static int getParkingPassCars(){return totaalPassCars;}
    public static int getAdHocCars(){return totaalAdHocCars;}

    public int countHocCar()
    {
     int i = entranceCarQueue.carsInQueue();
     return i;
    }
    public int countParkingPassCar()
    {
        int i = entrancePassQueue.carsInQueue();
        return i;
    }

    private void carLeavesSpot(Car car)
    {
        removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    private void passLeavesSpot(Car car)
    {
        Car newYellow = new Yellow();
        Location test = car.getLocation();
        removeCarAt(test);
        setCarAt(test, newYellow);
        exitCarQueue.addCar(car);
    }

    public void tickCar()
    {
        for (int floor = 0; floor < getNumberOfFloors(); floor++)
        {
            for (int row = 0; row < getNumberOfRows(); row++)
            {
                for (int place = 0; place < getNumberOfPlaces(); place++)
                {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null)
                    {
                        car.tick();
                    }
                }
            }
        }
    }

    private Car removeCarAt(Location location)
    {
        if (!locationIsValid(location))
        {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null)
        {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    private boolean locationIsValid(Location location)
    {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        return floor >= 0 && floor < numberOfFloors && row >= 0 && row <= numberOfRows && place >= 0 && place <= numberOfPlaces;
    }

    // Getters

    public int getNumberOfFloors()
    {
        return numberOfFloors;
    }

    public int getNumberOfRows()
    {
        return numberOfRows;
    }

    public int getNumberOfPlaces()
    {
        return numberOfPlaces;
    }

    private int getNumberOfOpenSpots()
    {
        return numberOfOpenSpots;
    }

    public Car getCarAt(Location location)
    {
        if (!locationIsValid(location))
        {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    private Location getFirstFreeLocation()
    {
        for (int floor = 0; floor < getNumberOfFloors(); floor++)
        {
            for (int row = 0; row < getNumberOfRows(); row++)
            {
                for (int place = 0; place < getNumberOfPlaces(); place++)
                {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null)
                    {
                        return location;
                    }
                }
            }
        }
        return null;
    }
    private Location getLastFreeLocation()
    {
        for (int floor = getNumberOfFloors() - 1; floor >= 0; floor--)
        {
            for (int row = getNumberOfRows() - 1; row >= 0; row--)
            {
                for (int place = getNumberOfPlaces() - 1; place >= 0; place--)
                {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null)
                    {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    private Car getFirstLeavingCar()
    {
        for (int floor = 0; floor < getNumberOfFloors(); floor++)
        {
            for (int row = 0; row < getNumberOfRows(); row++)
            {
                for (int place = 0; place < getNumberOfPlaces(); place++)
                {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying())
                    {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    // Setters

    private int setNumberOfFloors(int floors)
    {
        return numberOfFloors = floors;
    }

    private int setNumberOfRows(int rows)
    {
        return numberOfRows = rows;
    }

    private int setNumberOfPlaces(int places)
    {
        return numberOfPlaces = places;
    }

    private boolean setCarAt(Location location, Car car)
    {
        if (!locationIsValid(location))
        {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null)
        {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }


}
