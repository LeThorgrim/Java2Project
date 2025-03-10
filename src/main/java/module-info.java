module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;

    opens app.db.entities;
    opens app to javafx.fxml;
    exports app;
    opens app.views to javafx.fxml;
}
