package eus.ehu.ridesfx.ui;

import eus.ehu.ridesfx.configuration.Config;
import eus.ehu.ridesfx.businessLogic.BlFacade;
import eus.ehu.ridesfx.businessLogic.BlFacadeImplementation;
import eus.ehu.ridesfx.domain.User;

import java.util.Locale;

public class ApplicationLauncher {

  public static void main(String[] args) {

    Config config = Config.getInstance();

    Locale.setDefault(new Locale(config.getLocale()));
    System.out.println("Locale: " + Locale.getDefault());

    BlFacade businessLogic;

    try {

      if (config.isBusinessLogicLocal()) {
        businessLogic = new BlFacadeImplementation();
        User user = new User("notLoggedInUser", "notLoggedInUser");
        businessLogic.setCurrentUser(user);


        new MainGUI(businessLogic);

      }
    }
    catch (Exception e) {
      System.err.println("Error in ApplicationLauncher: " + e);
    }

  }


}