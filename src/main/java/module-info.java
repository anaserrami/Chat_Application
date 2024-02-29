module err.anas.chatyou {
        requires javafx.controls;
        requires javafx.fxml;

        requires com.dlsc.formsfx;
        requires org.kordamp.bootstrapfx.core;
        requires java.sql;
        requires mysql.connector.j;

        opens err.anas.chatyou.presentation to javafx.fxml;
        opens err.anas.chatyou.presentation.controllers to javafx.fxml;
        opens err.anas.chatyou.dao to javafx.base;
        opens err.anas.chatyou.dao.entities to javafx.base;
        exports err.anas.chatyou.presentation;
}