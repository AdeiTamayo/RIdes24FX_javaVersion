
package eus.ehu.ridesfx.uicontrollers;

import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.businessLogic.BlFacadeImplementation;
import eus.ehu.ridesfx.domain.Driver;
import eus.ehu.ridesfx.ui.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;


import java.io.IOException;

public class LoginController implements Controller {


    //private MainGUIController mainGUIController;

    @FXML
    private Label Text;

    @FXML
    private Label TestLabel;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Label InvalidUser;
    @FXML
    private Label WrongPassword;

    private MainGUI mainGUI;


    private BlFacade businessLogic;

    MainGUIController mainGUIController = new MainGUIController();


    public LoginController(BlFacade bl) {
        this.businessLogic = bl;


    }


    /**
     * This method is used to register a new user
     *
     * @param event
     */
    @FXML
    void loginBtnClick(ActionEvent event) {
        String Email = email.getText();
        String Password = password.getText();
        Text.setVisible(false);
        WrongPassword.setVisible(false);
        InvalidUser.setVisible(false);
        if (Email.equals("") || Password.equals("")) {
            System.out.println("Please fill in all the fields");
            Text.setText("Please fill in all the fields");
            Text.setVisible(true);
        } else if (businessLogic.checkUser(Email) == null) {
            System.out.println("A user with this email doen't exist. Please register first.");
            InvalidUser.setVisible(true);
        } else {
            if (!businessLogic.checkPassword(Email, Password)) {
                System.out.println("The password is incorrect");
                WrongPassword.setVisible(true);
            } else {
                Text.setText("You have been correctly logged in!");
                Text.setVisible(true);
                businessLogic.setCurrentDriver(businessLogic.checkUser(Email));


                //FIXME this is a test the functionality of getting the name

                //This prints the name of the driver in the console
                System.out.println(businessLogic.getCurrentDriver().getName());

                //This following line sets the name of the driver in the MainGUIController, but it throws a null pointer exception because lbl is null
                mainGUIController.setDriverName(businessLogic.getCurrentDriver().getName());

                //This prints the name of the driver in the LoginGuim it is a test to see if it is the problem of the fxml (It is not)
                TestLabel.setText(businessLogic.getCurrentDriver().getName());





            }
        }
    }


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @FXML
    void initialize() {

        InvalidUser.setVisible(false);
        WrongPassword.setVisible(false);
        Text.setVisible(false);
        Text.setWrapText(true);
        Text.setAlignment(javafx.geometry.Pos.CENTER);


    }
}
