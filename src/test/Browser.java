package test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.ResourceBundle;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class Browser implements Initializable {

	@FXML
	WebView browser;
	@FXML
	Button btn;

	WebEngine engine;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		engine = browser.getEngine();

		String url = getClass().getResource("/META-INF/web/viewer.html").toExternalForm();

		// connect CSS styles to customize pdf.js appearance
		engine.setUserStyleSheetLocation(getClass().getResource("/META-INF/web/viewer.css").toExternalForm());

		engine.setJavaScriptEnabled(true);
		engine.load(url);

		engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			// to debug JS code by showing console.log() calls in IDE console
			JSObject window = (JSObject) engine.executeScript("window");
			engine.executeScript("console.log = function(message){ java.log(message); };");

			// this pdf file will be opened on application startup
			if (newValue == Worker.State.SUCCEEDED) {
				try {
					// readFileToByteArray() comes from commons-io library
					byte[] data = Files.readAllBytes(new File("C:\\Users\\Learn\\Desktop\\testPdf.pdf").toPath());
					String base64 = Base64.getEncoder().encodeToString(data);
					// call JS function from Java code
					engine.executeScript("openFileFromBase64('" + base64 + "')");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// this file will be opened on button click
		btn.setOnAction(actionEvent -> {
			try {
				byte[] data = Files.readAllBytes(new File("C:\\Users\\Learn\\Desktop\\testPdf.pdf").toPath());
				String base64 = Base64.getEncoder().encodeToString(data);
				engine.executeScript("openFileFromBase64('" + base64 + "')");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}