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
	exports com.thaus.chatbox.components.views;
	exports com.thaus.chatbox.base;
	exports com.thaus.chatbox.interfaces;
	exports com.thaus.chatbox.components.tabs;
	exports com.thaus.chatbox.components.interactive;
	exports com.thaus.chatbox.utils;
	exports com.thaus.chatbox.classes;

	opens com.thaus.chatbox to javafx.fxml;
	opens com.thaus.chatbox.components.tabs to javafx.fxml;
	opens com.thaus.chatbox.components.interactive to javafx.fxml;
	opens com.thaus.chatbox.controllers to javafx.fxml;
	opens com.thaus.chatbox.base to javafx.fxml;
	opens com.thaus.chatbox.components.views to javafx.fxml;
	opens com.thaus.chatbox.interfaces to javafx.fxml;
	exports com.thaus.chatbox.components.informative;
	opens com.thaus.chatbox.components.informative to javafx.fxml;


}