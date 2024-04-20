package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;

import eus.ehu.ridesfx.domain.Driver;
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

    private MainGUIController MainGUIController;


    @FXML
    private DatePicker datePicker;


    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblErrorMinBet;


    @FXML
    private TextField txtArrivalCity;

    @FXML
    private TextField txtDepartCity;


    //private TextField numberSeatsSpinner;

    @FXML
    private TextField txtPrice;

    @FXML
    private ComboBox<String> ArrivalCityComboBox;

    @FXML
    private ComboBox<String> DepartCityComboBox;

    @FXML
    private Spinner<Integer> numberSeatsSpinner;

    public CreateRideController(BlFacade bl, MainGUIController mainGUIController) {
        this.businessLogic = bl;
        setMainApp(mainGUIController);
        this.MainGUIController.setCreateRideController(this);
    }





    private String field_Errors() {

        try {
            if ((txtDepartCity.getText().isEmpty()) || (txtArrivalCity.getText().isEmpty()) || (numberSeatsSpinner.getValue()==0 || (txtPrice.getText().isEmpty())))
                return ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorQuery");
            else {

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
                Ride r = businessLogic.createRide(txtDepartCity.getText(), txtArrivalCity.getText(), Dates.convertToDate(datePicker.getValue()), inputSeats, price, driver.getEmail());
                displayMessage(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideCreated"), "success");


            } catch (RideMustBeLaterThanTodayException e1) {
                displayMessage(e1.getMessage(), "danger");
            } catch (RideAlreadyExistException e1) {
                displayMessage(e1.getMessage(), "danger");
            }
        }

/*
    if (lblErrorMinBet.getText().length() > 0 && showErrors) {
      lblErrorMinBet.getStyleClass().setAll("lbl", "lbl-danger");
    }
    if (lblErrorQuestion.getText().length() > 0 && showErrors) {
      lblErrorQuestion.getStyleClass().setAll("lbl", "lbl-danger");
    }
 */
    }

    private List<LocalDate> holidays = new ArrayList<>();

  /*private void setEventsPrePost(int year, int month) {
    LocalDate date = LocalDate.of(year, month, 1);
    setEvents(date.getYear(), date.getMonth().getValue());
    setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
    setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
  }*/

 /* private void setEvents(int year, int month) {

    Date date = Dates.toDate(year, month);

    for (Date day : businessLogic.getEventsMonth(date)) {
      holidays.add(Dates.convertToLocalDateViaInstant(day));
    }
  }*/

    @FXML
    void initialize() {

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        numberSeatsSpinner.setValueFactory(valueFactory);



        // only show the text of the event in the combobox (without the id)
/*
    Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
      @Override
      protected void updateItem(Event item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getDescription());
      }
    };


     comboEvents.setCellFactory(factory);
    comboEvents.setButtonCell(factory.call(null));

 */


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

        // when a date is selected...
        datePicker.setOnAction(actionEvent -> {
     /* comboEvents.getItems().clear();

      oListEvents = FXCollections.observableArrayList(new ArrayList<>());
      oListEvents.setAll(businessLogic.getEvents(Dates.convertToDate(datePicker.getValue())));

      comboEvents.setItems(oListEvents);

      if (comboEvents.getItems().size() == 0)
        btnCreateRide.setDisable(true);
      else {
         btnCreateRide.setDisable(false);
        // select first option
        comboEvents.getSelectionModel().select(0);
      }
*/
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
        txtDepartCity.setText("");
        txtArrivalCity.setText("");
        txtPrice.setText("");
        lblErrorMessage.setText("");
        lblErrorMinBet.setText("");
        datePicker.setValue(null);
        ArrivalCityComboBox.setValue(null);
        DepartCityComboBox.setValue(null);
        numberSeatsSpinner.getValueFactory().setValue(0);

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
        MainGUIController.showInitialGUI();
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
        this.MainGUIController = mainGUIController;
    }
}
