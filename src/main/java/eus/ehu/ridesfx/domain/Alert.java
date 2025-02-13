/**
 * The Alert class represents an alert in the ride sharing system.
 * It is marked as an Entity, meaning that it is mapped to a table in the database.
 * Each Alert is associated with a Traveler and a Location.
 * The class implements Serializable, which means it can be converted to a byte stream and recovered later.
 */
package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
@Entity
public class Alert implements Serializable {
    @Id
    @GeneratedValue
    private Integer alertNumber;
    private int numPlaces;
    private Date date;

    private String state = "No rides found";

    @ManyToOne
    private Traveler traveler;

    @ManyToOne
    private Location locationTo;

    @ManyToOne
    private Location locationFrom;

    /**
     * Default constructor for the Alert class.
     * Initializes a new Alert with no parameters.
     */
    public Alert() {
        super();
    }

    /**
     * Parameterized constructor for the Alert class.
     * Initializes a new Alert with the provided parameters.
     *
     * @param alertNumber The number of the alert.
     * @param from The origin location of the alert.
     * @param to The destination location of the alert.
     * @param date The date of the alert.
     * @param numPlaces The number of places available in the alert.
     * @param traveler The traveler of the alert.
     */
    public Alert(Integer alertNumber, Location from, Location to, Date date, int numPlaces, Traveler traveler) {
        super();
        this.alertNumber = alertNumber;
        this.locationFrom = from;
        this.locationTo = to;
        this.numPlaces = numPlaces;
        this.date = date;
        this.traveler = traveler;
    }

    /**
     * Parameterized constructor for the Alert class.
     * Initializes a new Alert with the provided parameters.
     *
     * @param from The origin location of the alert.
     * @param to The destination location of the alert.
     * @param date The date of the alert.
     * @param numPlaces The number of places available in the alert.
     * @param traveler The traveler of the alert.
     */
    public Alert(Location from, Location to, Date date, int numPlaces, Traveler traveler) {
        super();
        this.locationFrom = from;
        this.locationTo = to;
        this.numPlaces = numPlaces;
        this.date = date;
        this.traveler = traveler;
    }


    /**
     * Get the  number of the alert
     *
     * @return the alert number
     */
    public Integer getAlertNumber() {
        return alertNumber;
    }


    /**
     * Set the alert number to a alert
     *
     * @param alertNumber Number to be set	 */

    public void setAlertNumber(Integer alertNumber) {
        this.alertNumber = alertNumber;
    }


    /**
     * Get the origin  of the alert
     *
     * @return the origin location
     */
    public Location getFromLocation() {
        return locationFrom;
    }


    /**
     * Set the origin of the alert
     *
     * @param origin to be set
     */
    public void setFromLocation(Location origin) {
        this.locationFrom = origin;
    }

    /**
     * Get the destination  of the alert
     *
     * @return the destination location
     */
    public Location getToLocation() {
        return locationTo;
    }


    /**
     * Set the origin of the alert
     *
     * @param destination to be set
     */
    public void setToLocation(Location destination) {
        this.locationTo = destination;
    }


    /**
     * Get the date  of the alert
     *
     * @return the alert date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date of the alert
     *
     * @param date to be set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the num places of the alert
     *
     * @return the places asked
     */
    public int getNumPlaces() {
        return numPlaces;
    }

    /**
     * Set the num places of the alert
     *
     * @param numPlaces to be set
     */
    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }


    /**
     * Get the traveler associated to the alert
     *
     * @return the associated traveler
     */
    public Traveler getTraveler() {
        return traveler;
    }

    /**
     * Set the traveler associated to the alert
     *
     * @param traveler to associate to the alert
     */
    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }

    /**
     * Get the state of the alert
     *
     * @return the state of the alert
     */
    public String getState() {
        return state;
    }

    /**
     * Set the state of the alert
     *
     * @param state to set
     */
    public void setState(String state) {
        this.state = state;
    }


    /**
     * This method returns a string with the alert information
     *
     * @return the alert information
     */
    public String toString() {
        return alertNumber + ";" + ";" + locationFrom + ";" + locationTo + ";" + date;
    }


}
