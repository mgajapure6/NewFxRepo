
package app.global;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Alerts {

	public static void warning(String title, String content) {
		DialogCustom.createAlert(DialogCustom.Type.WARNING, title, content);
	}

	@SafeVarargs
	public static void warning(String title, String content, EventHandler<MouseEvent>... confirm) {
		DialogCustom.createAlert(DialogCustom.Type.WARNING, title, content, confirm);
	}

	public static void error(String title, String content) {
		DialogCustom.createAlert(DialogCustom.Type.ERROR, title, content);
	}

	@SafeVarargs
	public static void error(String title, String content, EventHandler<MouseEvent>... confirm) {
		DialogCustom.createAlert(DialogCustom.Type.ERROR, title, content, confirm);
	}

	public static void info(String title, String content) {
		DialogCustom.createAlert(DialogCustom.Type.INFO, title, content);
	}

	@SafeVarargs
	public static void info(String title, String content, EventHandler<MouseEvent>... confirm) {
		DialogCustom.createAlert(DialogCustom.Type.INFO, title, content, confirm);
	}

	public static void success(String title, String content) {
		DialogCustom.createAlert(DialogCustom.Type.SUCCESS, title, content);
	}

	@SafeVarargs
	public static void success(String title, String content, EventHandler<MouseEvent>... confirm) {
		DialogCustom.createAlert(DialogCustom.Type.SUCCESS, title, content, confirm);
	}
}
