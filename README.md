# RidesFX: a JavaFX (modular+hibernate+h2+properties) desktop application project 

A JavaFX+Hibernate+H2+JUnit template for the Software Engineering I class of the Faculty of Computer Science of Donostia. 


* First, you need to create some Java POJO entities in the domain folder
* Then, you can map those entities to an H2 database: 
  * In the hibernate.cfg.xml file, edit this line:
  
           <property name="hibernate.hbm2ddl.auto">update</property>

    Change the value to "create" to create the database from scratch, or "update" to update the database with the new entities.
  * In the hibernate.cfg.xml file, edit this line:
  
           <property name="hibernate.connection.url">jdbc:h2:~/database</property>

    Change the value to the path where you want to store the database. In this case,
  the database will be stored in the home folder of the user.

* Rename the package (from eus.ehu.ridesfx to eus.ehu.XXXXX where XXXX is your package name)
* Rename the module (from template to XXXXX where XXXX is your module name)
* You may need to change the controller path in the hello-view.fxml file
* Remember that, by default, the open mode of the db is `initialize` (this property is set in the `config.properties` file. 
Change it to `update` if you want to keep the data between executions

------------
ITERATION 1
 Implemented requrements analysis: Domain Model + Use Case Diagram + Event Flow
 Changed project layout to BorderPane layout
 Implemented the register and login use cases
 
