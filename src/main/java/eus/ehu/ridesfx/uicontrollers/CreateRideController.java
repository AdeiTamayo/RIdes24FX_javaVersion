package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;

import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Location;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.exceptions.RideAlreadyExistException;
import eus.ehu.ridesfx.exceptions.RideMustBeLaterThanTodayException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;

import eus.ehu.ridesfx.utils.Dates;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateRideController implements Controller {


    private BlFacade businessLogic;

    private MainGUIController mainGUIController;


    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblErrorMinBet;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<Location> ArrivalCityComboBox;

    @FXML
    private ComboBox<Location> DepartCityComboBox;

    @FXML
    private Spinner<Integer> numberSeatsSpinner;

    @FXML
    private TextField newDepartCityText;

    @FXML
    private TextField newArrivalCityText;

    public CreateRideController(BlFacade bl, MainGUIController mainGUIController) {
        this.businessLogic = bl;
        setMainApp(mainGUIController);
        this.mainGUIController.setCreateRideController(this);
    }


    private String field_Errors() {

        try {
            if (numberSeatsSpinner.getValue() == 0 || DepartCityComboBox.getValue() == null || ArrivalCityComboBox.getValue() == null || txtPrice.getText().isEmpty() || datePicker.getValue() == null) {
                System.out.println("Errorea egon da ez da zeoze detektau sortzerakon");
                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");

            } else {

                // trigger an exception if the introduced string is not a number
                int inputSeats = numberSeatsSpinner.getValue();

                if (inputSeats <= 0) {
                    return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.SeatsMustBeGreaterThan0");
                } else {
                    float price = Float.parseFloat(txtPrice.getText());
                    if (price <= 0)
                        return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.PriceMustBeGreaterThan0");

                    else
                        return null;

                }
            }
        } catch (NumberFormatException e1) {

            return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorNumber");
        } catch (Exception e1) {

            e1.printStackTrace();
            return null;

        }
    }


    @FXML
    void createRideClick(ActionEvent e) {

        clearErrorLabels();

        //  Event event = comboEvents.getSelectionModel().getSelectedItem();
        String errors = field_Errors();

        if (errors != null) {
            // businessLogic.createQuestion(event, inputQuestion, inputPrice);
            displayMessage(errors, "danger");

        } else {
            try {

                int inputSeats = numberSeatsSpinner.getValue();
                float price = Float.parseFloat(txtPrice.getText());
                Driver driver = (Driver) businessLogic.getCurrentUser();

                if (ArrivalCityComboBox.getValue().getName().equals("Create new city") && DepartCityComboBox.getValue().getName().equals("Create new city")) {
                    String arrivalCity = newArrivalCityText.getText();
                    Location l = businessLogic.createLocation(arrivalCity);

                    ArrivalCityComboBox.getItems().add(ArrivalCityComboBox.getValue());
                    ArrivalCityComboBox.setValue(l);

                    newDepartCityText.setVisible(true);
                    String departCity = newDepartCityText.getText();
                    Location l2 = businessLogic.createLocation(departCity);
                    DepartCityComboBox.getItems().add(l2);
                    DepartCityComboBox.setValue(l2);

                    //delete the "create new city" location
                    businessLogic.deleteLocation("Create new city");


                } else if (ArrivalCityComboBox.getValue().getName().equals("Create new city")) {
                    String arrivalCity = newArrivalCityText.getText();
                    Location loc = businessLogic.createLocation(arrivalCity);
                    ArrivalCityComboBox.getItems().add(loc);
                    ArrivalCityComboBox.setValue(loc);

                    //delete the "create new city" location
                    businessLogic.deleteLocation("Create new city");


                } else if (DepartCityComboBox.getValue().getName().equals("Create new city")) {
                    newDepartCityText.setVisible(true);
                    String departCity = newDepartCityText.getText();
                    Location lc = businessLogic.createLocation(departCity);
                    DepartCityComboBox.getItems().add(lc);
                    DepartCityComboBox.setValue(lc);

                    //delete the "create new city" location
                    businessLogic.deleteLocation("Create new city");

                }

                Ride r = businessLogic.createRide(DepartCityComboBox.getValue(), ArrivalCityComboBox.getValue(), Dates.convertToDate(datePicker.getValue()), inputSeats, price, driver.getEmail());
                clearCreateRideMethod();
                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success");


                mainGUIController.updateComboBoxesQueryRides();


            } catch (RideMustBeLaterThanTodayException e1) {
                displayMessage(e1.getMessage(), "error");
            } catch (RideAlreadyExistException e1) {
                displayMessage(e1.getMessage(), "error");
            }
        }


    }

    private List<LocalDate> holidays = new ArrayList<>();


    @FXML
    void initialize() {

        newArrivalCityText.setVisible(false);
        newDepartCityText.setVisible(false);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        numberSeatsSpinner.setValueFactory(valueFactory);

        //Method converts the ride already created into locations
        //businessLogic.convertRideToLocation();

        // Populate ArrivalCityComboBox and DepartCityComboBox with locations
        List<Location> locations = businessLogic.getLocations();

        // Clear the ComboBoxes
        ArrivalCityComboBox.getItems().clear();
        DepartCityComboBox.getItems().clear();

        // Add each location's name to the ComboBoxes
        Location defaultArrLocation = new Location("Create new city");
        Location defaultDesLocation = new Location("Create new city");
        ArrivalCityComboBox.getItems().add(defaultArrLocation);
        DepartCityComboBox.getItems().add(defaultDesLocation);

        for (Location location : locations) {
            ArrivalCityComboBox.getItems().add(location);
            DepartCityComboBox.getItems().add(location);
        }

        // Listener for ArrivalCityComboBox
        ArrivalCityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (defaultArrLocation.equals(newValue)) {
                newArrivalCityText.setVisible(true);
            }
        });

        // Listener for DepartCityComboBox
        DepartCityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (defaultDesLocation.equals(newValue)) {
                newDepartCityText.setVisible(true);
            }
        });


        // setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());


        // get a reference to datepicker inner content
        // attach a listener to the  << and >> buttons
        // mark events for the (prev, current, next) month and year shown
        datePicker.setOnMouseClicked(e -> {
            DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {
                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);
                    // setEventsPrePost(ym.getYear(), ym.getMonthValue());
                });
            });


        });

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            }
                        }
                    }
                };
            }
        });


    }

    //New methods

    /**
     * Clear the fields of the create ride window when selected
     *
     * @param event
     */
    @FXML
    void clearCreateRide(ActionEvent event) {
        clearCreateRideMethod();
    }

    /**
     * Clear the fields of the create ride window
     */
    public void clearCreateRideMethod() {


        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        datePicker.setValue(null);
        ArrivalCityComboBox.setValue(null);
        DepartCityComboBox.setValue(null);
        numberSeatsSpinner.getValueFactory().setValue(0);
        newDepartCityText.setText("");
        newDepartCityText.setVisible(false);
        newArrivalCityText.setText("");
        newArrivalCityText.setVisible(false);
        txtPrice.setText("");


    }

    /**
     * Display a message in the error label
     *
     * @param message The message to be displayed
     * @param label   The label style
     */
    void displayMessage(String message, String label) {
        lblErrorMessage.getStyleClass().clear();
        lblErrorMessage.getStyleClass().setAll("lbl", "lbl-" + label);
        lblErrorMessage.setText(message);
    }


    /**
     * Close the create ride window and goes to Initial Window
     *
     * @param event
     */
    @FXML
    void closeClick(ActionEvent event) {
        clearErrorLabels();
        mainGUIController.showInitialGUI();
    }

    /**
     * Clear the error labels
     */
    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        lblErrorMinBet.getStyleClass().clear();
        lblErrorMessage.getStyleClass().clear();
    }

    @Override
    public void setMainApp(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }


}
