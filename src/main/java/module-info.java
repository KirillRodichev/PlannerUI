module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;

    opens org.openjfx.mvc.view.jfxControllers to javafx.fxml;
    exports org.openjfx.mvc.view.jfxControllers;
}