module com.ceciliaarcos.flightsfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.ceciliaarcos.flightsfx to javafx.fxml;
    exports com.ceciliaarcos.flightsfx;
    exports com.ceciliaarcos.flightsfx.model;
}