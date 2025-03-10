module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;

    opens app.db.entities to javafx.base;
    opens app to javafx.fxml;
    opens app.views to javafx.fxml;

    exports app;
}
