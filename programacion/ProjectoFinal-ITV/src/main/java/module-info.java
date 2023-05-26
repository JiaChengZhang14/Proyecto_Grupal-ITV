module com.example.projectofinalitv {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    // Logger
    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Result
    requires kotlin.result.jvm;

    // Koin
    requires koin.core.jvm;
    requires java.sql;

    // Gson
    requires com.google.gson;

    // Jsoup
    requires org.jsoup;
    requires java.desktop;

    // dtos, abrimos a Gson
    opens com.example.projectofinalitv.dto to com.google.gson;

    opens com.example.projectofinalitv to javafx.fxml;
    exports com.example.projectofinalitv;

    opens com.example.projectofinalitv.models to javafx.fxml;
    exports com.example.projectofinalitv.models;

    opens com.example.projectofinalitv.controllers.cita to javafx.fxml;
    exports com.example.projectofinalitv.controllers.cita;

    opens com.example.projectofinalitv.controllers.vehiculo to javafx.fxml;
    exports com.example.projectofinalitv.controllers.vehiculo;

    opens com.example.projectofinalitv.controllers.propietario to javafx.fxml;
    exports com.example.projectofinalitv.controllers.propietario;

    opens com.example.projectofinalitv.controllers.detalles to javafx.fxml;
    exports com.example.projectofinalitv.controllers.detalles;

    opens com.example.projectofinalitv.controllers.acercaDe to javafx.fxml;
    exports com.example.projectofinalitv.controllers.acercaDe;
}
