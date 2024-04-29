package eus.ehu.ridesfx.businessLogic;

import eus.ehu.ridesfx.domain.*;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;

import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Interface that specifies the business logic.
 */

public interface BlFacade {
    /**
     * This method retrieves the rides from two locations on a given date
     *
     * @param from the origin location of a ride
     * @param to   the destination location of a ride
     * @param date the date of the ride
     * @return collection of rides
     */
    List<Ride> getRides(Location from, Location to, Date date);

    /**
     * This method retrieves from the database the dates a month for which there are events
     *
     * @param from the origin location of a ride
     * @param to   the destination location of a ride
     * @param date of the month for which days with rides want to be retrieved
     * @return collection of rides
     */
    public List<Date> getThisMonthDatesWithRides(Location from, Location to, Date date);


    /**
     * This method retrieves from the database the dates in a month for which there are events
     *
     * @param date of the month for which days with events want to be retrieved
     * @return collection of dates
     */
    public Vector<Date> getEventsMonth(Date date);


    void setCurrentUser(User user);

    User getCurrentUser();

    Traveler getCurrentTraveler();

    Ride createRide(Location text, Location text1, Date date, int inputSeats, float price, String email) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;


    /**
     * This method returns all the cities where rides depart
     *
     * @return collection of cities
     */

    public List<Location> getDepartCities();

    /**
     * This method returns all the arrival destinations, from all rides that depart from a given city
     *
     * @param from the departure location of a ride
     * @return all the arrival destinations
     */

    public List<Location> getDestinationCities(Location from);


    List<Date> getDatesWithRides(Location value, Location value1);

    boolean registerUser(String username, String password, String email, String name, String role);

    User checkUser(String username);
    boolean checkPassword(String username, String password);

    /**
     * This method creates a new alert for the traveler
     * @param fromPlace
     * @param toPlace
     * @param inputSeats
     * @param date
     * @param email
     * @return the created alert
     */
    Alert createAlert(Location fromPlace, Location toPlace, int inputSeats, Date date, String email);


    /**
     * This method books a ride for a traveler
     * @param ride
     * @param traveler
     * @param numPlaces
     * @return true if the ride is booked, false otherwise
     */


    boolean makeReservation(Traveler traveler, Ride ride,int numPlaces);

    /**
     * This method returns the reservations of the current traveler
     */

    List<Reservation> getReservations();

    /**
     * This method returns the reservations of a driver
     */
    List<Reservation> getReservationDriver();

    /**
     * This method deletes a reservation
     * @param selectedItem
     */

    void deleteReservation(Reservation selectedItem);

    /**
     * This method changes the state of a reservation
     * @param selectedItem
     * @param state
     */

    void changeReservationState(Reservation selectedItem, String state);

    /**
     * This method returns the available seats for a ride
     *
     * @param selectedItem
     * @return collection of available seats
     */
    List<Integer> getAvailableSeats(Ride selectedItem);

    /**
     * This method checks if there are matching rides for the given alert
     *
     * @param alert
     * @return list of matching rides
     */
    List<Ride> areMatchingRides(Alert alert);

    /**
     * This method returns all the alerts of the current traveler
     *
     * @return list of alerts
     */
    List<Alert> getAlerts();

    /**
     * This method returns all the alerts in the database
     *
     * @return list of alerts
     */
    List<Alert> getAllAlerts();



    /**
     * This method deletes an alert
     *
     * @param alert
     */
    void deleteAlert(Alert alert);

    /**
     * This method updates the state of an alert
     *
     * @param alert
     */
    void updateAlertState(Alert alert);

    /**
     * This method returns all the locations
     *
     * @return list of locations
     */

    List<Location> getLocations();


    /**
     * This method creates a new location
     *
     * @param name
     */
    Location createLocation(String name);

    /**
     * This method deletes a location
     * @param location
     */
    void deleteLocation(String location);


}