module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;

    opens app to javafx.fxml;
    opens app.quiz.view to javafx.fxml;
    exports app;
    opens app.views to javafx.fxml;
}
