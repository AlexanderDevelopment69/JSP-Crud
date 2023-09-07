module com.java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.java to javafx.fxml;
    exports com.java;
    exports com.java.factory.MySQL;
    opens com.java.factory.MySQL to javafx.fxml;
    exports com.java.dao;
    opens com.java.dao to javafx.fxml;
    exports com.java.factory;
    opens com.java.factory to javafx.fxml;
}