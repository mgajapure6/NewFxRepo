package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.gn.decorator.GNDecorator;
import com.gn.decorator.options.ButtonType;
import com.sun.javafx.application.LauncherImpl;

import app.global.Section;
import app.global.SectionManager;
import app.global.User;
import app.global.UserDetail;
import app.global.UserManager;
import app.global.ViewManager;
import app.loader.Loader;
import app.main.Main;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	public static final GNDecorator decorator = new GNDecorator();
	public static final Scene scene = decorator.getScene();

	public static ObservableList<String> stylesheets;
	public static HostServices hostServices;
	private static UserDetail userDetail = null;

	private float increment = 0;
	private float progress = 0;
	private Section section;
	private User user;

	public static GNDecorator getDecorator() {
		return decorator;
	}

	public static UserDetail getUserDetail() {
		return userDetail;
	}

	@Override
	public synchronized void init() {
		section = SectionManager.get();

		if (section.isLogged()) {
			user = UserManager.get(section.getUserLogged());
			userDetail = new UserDetail(section.getUserLogged(), user.getFullName(), "subtitle");
		} else {
			userDetail = new UserDetail();
		}

		float total = 43; // the difference represents the views not loaded
		increment = 100f / total;

//		load("jfoenix", "jfx-text-field");
//
//		load("designer", "cards");
//		load("designer", "banners");
//		load("designer", "carousel");
//		load("designer", "animated-button");
//		load("designer", "alerts");
//
//		load("controls", "button");
//		load("controls", "toggle");
//		load("controls", "textfield");
//		load("controls", "text-area");
//		load("controls", "datepicker");
//		load("controls", "checkbox");
//		load("controls", "radiobutton");
//		load("controls", "combobox");
//		load("controls", "choicebox");
//		load("controls", "splitmenubutton");
//		load("controls", "menubutton");
//		load("controls", "menubar");
//		load("controls", "colorpicker");
//		load("controls", "slider");
//		load("controls", "spinner");
//		load("controls", "progressbar");
//		load("controls", "progressindicator");
//		load("controls", "pagination");
//		load("controls", "mediaview");
//		load("controls", "listview");
//		load("controls", "label");
//		load("controls", "hyperlink");
//		load("controls", "imageview");
//		load("controls", "tableview");
//		load("controls", "scrollbar");
//		load("controls", "passwordfield");
//		load("controls", "treeview");
//		load("controls", "treetableview");

		//load("dashboard", "dashboard");

//		load("charts", "piechart");
//		load("charts", "areachart");
//		load("charts", "barchart");
//		load("charts", "bubblechart");
//		load("charts", "linechart");
//		load("charts", "stackedbarchart");
//		load("charts", "stackedareachart");
//		load("charts", "scatterchart");

		//load("main", "main");

//		load("profile", "profile");

		load("login", "login");
		load("login", "account");
		try {
			wait(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		configServices();
		initialScene();
		stylesheets = decorator.getScene().getStylesheets();
		stylesheets.addAll(getClass().getResource("/com/gn/theme/css/fonts.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/material-color.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/skeleton.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/light.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/bootstrap.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/shape.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/typographic.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/helpers.css").toExternalForm(),
				getClass().getResource("/com/gn/theme/css/master.css").toExternalForm());

		decorator.setMaximized(true);
		decorator.getStage().getIcons().add(new Image("/com/gn/module/media/logo2.png"));
		decorator.show();
	}

	private void initialScene() {

		decorator.setTitle("DashboardFx");
//        decorator.setIcon(null);
		decorator.addButton(ButtonType.FULL_EFFECT);
		decorator.initTheme(GNDecorator.Theme.DEFAULT);
//        decorator.fullBody();

		String log = logged();
		assert log != null;

		if (log.equals("account") || log.equals("login")) {
			decorator.setContent(ViewManager.getInstance().get(log));
		} else {
			App.decorator.addCustom(userDetail);
			userDetail.setProfileAction(event -> {
				Main.ctrl.title.setText("Profile");
				Main.ctrl.body.setContent(ViewManager.getInstance().get("profile"));
				userDetail.getPopOver().hide();
			});

			userDetail.setSignAction(event -> {
				App.decorator.setContent(ViewManager.getInstance().get("login"));
				section.setLogged(false);
				SectionManager.save(section);
				userDetail.getPopOver().hide();
				if (Main.popConfig.isShowing())
					Main.popConfig.hide();
				if (Main.popup.isShowing())
					Main.popup.hide();
				App.decorator.removeCustom(userDetail);
			});
			decorator.setContent(ViewManager.getInstance().get("main"));
		}

		decorator.getStage().setOnCloseRequest(event -> {
			App.getUserDetail().getPopOver().hide();
			if (Main.popConfig.isShowing())
				Main.popConfig.hide();
			if (Main.popup.isShowing())
				Main.popup.hide();
			Platform.exit();
		});
	}

	private void load(String module, String name) {
		try {
			ViewManager.getInstance().put(name,
					FXMLLoader.load(getClass().getResource("/app/" + module + "/" + name + ".fxml")));
			preloaderNotify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void preloaderNotify() {
		progress += increment;
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
	}

	public static void main(String[] args) {
		LauncherImpl.launchApplication(App.class, Loader.class, args);
	}

	private String logged() {
		try {
			File file = new File("dashboard.properties");
			Properties properties = new Properties();

			if (!file.exists()) {
				file.createNewFile();
				return "account";
			} else {
				FileInputStream fileInputStream = new FileInputStream(file);
				properties.load(fileInputStream);
				properties.putIfAbsent("logged", "false");
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				properties.store(fileOutputStream, "Dashboard properties");

				File directory = new File("user/");
				properties.load(fileInputStream);
				if (directory.exists()) {
					if (properties.getProperty("logged").equals("false"))
						return "login";
					else
						return "main";
				} else
					return "account";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void configServices() {
		hostServices = getHostServices();
	}

}
