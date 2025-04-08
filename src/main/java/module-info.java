module com.thaus.chatbox {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires org.kordamp.bootstrapfx.core;

	opens com.thaus.chatbox to javafx.fxml;
	exports com.thaus.chatbox;
	exports com.thaus.chatbox.controllers;
	opens com.thaus.chatbox.controllers to javafx.fxml;
}