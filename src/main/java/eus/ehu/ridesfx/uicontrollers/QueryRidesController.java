package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.domain.Ride;
import eus.ehu.ridesfx.domain.Traveler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import eus.ehu.ridesfx.ui.MainGUI;
import eus.ehu.ridesfx.utils.Dates;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class QueryRidesController implements Controller {

    @FXML
    private BorderPane QueryRidesPane;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnClose;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Label rideDate;

    @FXML
    private TableColumn<Ride, String> qc1;

    @FXML
    private TableColumn<Ride, Integer> qc2;

    @FXML
    private TableColumn<Ride, Float> qc3;

    @FXML
    private ComboBox<String> comboArrivalCity;

    @FXML
    private ComboBox<String> comboDepartCity;

//  @FXML
//  private TableView<Event> tblEvents;

    @FXML
    private TableView<Ride> tblRides;

    @FXML
    private Button bookinButton;

    @FXML
    private Label quantityOfSeatsLabel;

    @FXML
    private TextField numSeats;


    private MainGUI mainGUI;

    private MainGUIController mainGUIController;

    private List<LocalDate> datesWithBooking = new ArrayList<>();

    private BlFacade businessLogic;

    public QueryRidesController(BlFacade bl, MainGUIController mainGUIController) {
        businessLogic = bl;
        this.mainGUIController = mainGUIController;
        this.mainGUIController.setQueryRidesController(this);
    }


    @FXML
    void closeClick(ActionEvent event) {

        // mainGUI.showMain();


        //beste modu batera, mainGUIControllerren istantzia bat sortuta:


        mainGUIController.showInitialGUI();

    }

    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    // we need to mark (highlight in pink) the events for the previous, current and next month
    // this method will be called when the user clicks on the << or >> buttons
    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }


    private void updateDatePickerCellFactory(DatePicker datePicker) {

        List<Date> dates = businessLogic.getDatesWithRides(comboDepartCity.getValue(), comboArrivalCity.getValue());

        // extract datesWithBooking from rides
        datesWithBooking.clear();
        for (Date day : dates) {
            datesWithBooking.add(Dates.convertToLocalDateViaInstant(day));
        }

        datePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (datesWithBooking.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            } else {
                                this.setStyle("-fx-background-color: rgb(235, 235, 235)");
                            }
                        }
                    }
                };
            }
        });
    }

    @FXML
    void initialize() {

        numSeats.setVisible(false);
        quantityOfSeatsLabel.setVisible(false);
        bookinButton.setVisible(false);


        // Update DatePicker cells when ComboBox value changes
        comboArrivalCity.valueProperty().addListener(
                (obs, oldVal, newVal) -> updateDatePickerCellFactory(datepicker));

        ObservableList<String> departureCities = FXCollections.observableArrayList(new ArrayList<>());
        departureCities.setAll(businessLogic.getDepartCities());

        ObservableList<String> arrivalCities = FXCollections.observableArrayList(new ArrayList<>());

        comboDepartCity.setItems(departureCities);
        comboArrivalCity.setItems(arrivalCities);

        // when the user selects a departure city, update the arrival cities
        comboDepartCity.setOnAction(e -> {
            arrivalCities.clear();
            arrivalCities.setAll(businessLogic.getDestinationCities(comboDepartCity.getValue()));
        });

        // a date has been chosen, update the combobox of Rides
        datepicker.setOnAction(actionEvent -> {

            tblRides.getItems().clear();
            // Vector<domain.Ride> events = businessLogic.getEvents(Dates.convertToDate(datepicker.getValue()));
            List<Ride> rides = businessLogic.getRides(comboDepartCity.getValue(), comboArrivalCity.getValue(), Dates.convertToDate(datepicker.getValue()));
            // List<Ride> rides = Arrays.asList(new Ride("Bilbao", "Donostia", Dates.convertToDate(datepicker.getValue()), 3, 3.5f, new Driver("pepe@pepe.com", "pepe")));
            for (Ride ride : rides) {
                tblRides.getItems().add(ride);
            }


            rideDate.setText(Dates.convertToDate(datepicker.getValue()).toString());
        });

        datepicker.setOnMouseClicked(e -> {
            // get a reference to datepicker inner content
            // attach a listener to the  << and >> buttons
            // mark events for the (prev, current, next) month and year shown
            DatePickerSkin skin = (DatePickerSkin) datepicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {


                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();

                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);

                    // print month value
                    System.out.println("Month:" + ym.getMonthValue());


                });
            });
        });

        // show just the driver's name in column1
        qc1.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ride, String> data) {
                Driver driver = data.getValue().getDriver();
                return new SimpleStringProperty(driver != null ? driver.getName() : "<no name>");
            }
        });

        qc2.setCellValueFactory(new PropertyValueFactory<>("numPlaces"));
        qc3.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add listener to TableView's selection model to know when to change the GUI with new buttons
        tblRides.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                manageGUi();
            }
        });

    }


    /*

      private void setupEventSelection() {
        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null) {

            tblQuestions.getItems().clear();
            for (Question q : tblEvents.getSelectionModel().getSelectedItem().getQuestions()) {
              tblQuestions.getItems().add(q);
            }
          }
        });
      }
    */


    //TODO create the method to book a ride
    @FXML
    public void bookRide(ActionEvent actionEvent) {

        //Lortu behar dira data, bidaia eta erabiltzailea
        Date date = Dates.convertToDate(datepicker.getValue());
        Ride ride = tblRides.getSelectionModel().getSelectedItem();
        int numSeats = Integer.parseInt(this.numSeats.getText());


        //suposatzen da erreserbatzen sahiatzen bada, traveler izan behar duela
        Traveler traveler = businessLogic.getCurrentTraveler();

        //Call the business logic to book the ride and check if the number of seats is available
        if(!businessLogic.bookRide(date, ride, traveler, numSeats)){
            //TODO show error message(number of seats not available)
            System.out.println("Error booking ride. Number of seats not available");

        }else{
            //TODO show success message
            System.out.println("Ride booked successfully");
        }



    }


    /**
     * This method manages the GUI to work when the user selects a ride
     */
    public void manageGUi() {
        if (tblRides.getSelectionModel().getSelectedItem() != null) {
            bookinButton.setVisible(true);
            numSeats.setVisible(true);
            quantityOfSeatsLabel.setVisible(true);
        }
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}
