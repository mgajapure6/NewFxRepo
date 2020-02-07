package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import app.global.PDFGenerator;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class WebViewTest extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException, DocumentException {
		String[] colArr = { "a", "b", "c" };
		String[] dataArr = { "a", "b", "c" };

		File f = new File("D:\\pdffile.pdf");
		if (!f.exists()) {
			f.createNewFile();
		}

		PDFGenerator.getPdf(f.getAbsolutePath(), "fddfg", colArr, dataArr);
		System.out.println(f.getAbsolutePath());

		WebView webView = new WebView();
		webView.getEngine()
				.setUserStyleSheetLocation(getClass().getResource("/META-INF/plugin/web/viewer.css").toExternalForm());

		webView.getEngine().setJavaScriptEnabled(true);

		webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			JSObject window = (JSObject) webView.getEngine().executeScript("window");
			window.setMember("java", new JSLogListener());
			if (newValue == Worker.State.SUCCEEDED) {
				try {
					byte[] data = FileUtils.readFileToByteArray(new File(f.getAbsolutePath()));
					String base64 = Base64.getEncoder().encodeToString(data);
					String js = "openFileFromBase64('" + base64 + "')";
					webView.getEngine().executeScript(js);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		webView.getEngine().load(getClass().getResource("/META-INF/plugin/web/viewer.html").toExternalForm());

		BorderPane bp = new BorderPane(webView);
		VBox.setVgrow(bp, Priority.ALWAYS);
		Scene scene = new Scene(bp, 960, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("PDF test app");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public class JSLogListener {

		public void log(String text) {
			System.out.println("log::" + text);
		}
	}

//	<script type="text/javascript">
//	var openFileFromBase64 = function(data) {
//		var arr = base64ToArrayBuffer(data);
//		console.log(arr);
//		PDFViewerApplication.open(arr);
//	}
//
//	function base64ToArrayBuffer(base64) {
//		var binary_string = window.atob(base64);
//		var len = binary_string.length;
//		var bytes = new Uint8Array(len);
//		for (var i = 0; i < len; i++) {
//			bytes[i] = binary_string.charCodeAt(i);
//		}
//		return bytes.buffer;
//	}
//  </script>

	public static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}
}