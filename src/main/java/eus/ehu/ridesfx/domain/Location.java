package eus.ehu.ridesfx.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;


@Entity
@DiscriminatorValue("LOCATION")
public class Location implements Serializable {



    @Id
    private String name;

    @OneToMany
    private List<Ride> ride;


    @OneToOne
    private Alert alert;



    /**
     * Default constructor for the Location class with no parameters.
     */
    public Location () {

    }

    /**
     * Parameterized constructor for the Location class.
     * Initializes a new Location with the provided name.
     *
     * @param name The name of the location.
     */
    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
