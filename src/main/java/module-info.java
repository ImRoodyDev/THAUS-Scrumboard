module com.thaus.chatbox {
	requires javafx.fxml;
	requires com.jfoenix;
	requires javafx.controls;
	requires java.desktop;

	// requires transitive javafx.graphics;

	// requires MaterialFX;
	// requires org.controlsfx.controls;
	// requires com.dlsc.formsfx;
	// requires org.kordamp.bootstrapfx.core;

	exports com.thaus.chatbox;
	exports com.thaus.chatbox.controllers;
	exports com.thaus.chatbox.types;
	exports com.thaus.chatbox.views;
	exports com.thaus.chatbox.base;

	opens com.thaus.chatbox.controllers to javafx.fxml;
	opens com.thaus.chatbox.base to javafx.fxml;
	opens com.thaus.chatbox.views to javafx.fxml;

}