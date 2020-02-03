package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.gn.decorator.GNDecorator;
import com.gn.decorator.options.ButtonType;
import com.sun.javafx.application.LauncherImpl;

import app.db.dao.UserDao;
import app.db.domain.User;
import app.db.util.HibernateUtil;
import app.global.Section;
import app.global.SectionManager;
import app.global.UserDetail;
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
		HibernateUtil.getSessionFactory().openSession().close();
		section = SectionManager.get();
		UserDao userDao = new UserDao();
		if (section.isLogged()) {
			user = userDao.getByUsername(section.getUserLogged());
			if (user != null) {
				userDetail = new UserDetail(user);
			} else {
				userDetail = new UserDetail();
			}

		} else {
			userDetail = new UserDetail();
		}

		float total = 43; // the difference represents the views not loaded
		increment = 100f / total;

		load("login", "login");
		load("login", "account");
		load("main", "customerHomeView");
		load("main", "main");
		load("dashboard", "dashboard");
		load("product", "productView");
		load("customer", "customerView");
		load("bill", "billView");
		try {
			wait(1000);
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

		decorator.setTitle("MMC Store");
//        decorator.setIcon(null);
		decorator.addButton(ButtonType.FULL_EFFECT);
		decorator.initTheme(GNDecorator.Theme.CUSTOM);
//        decorator.fullBody();

//		String log = logged();
//		assert log != null;

		decorator.setContent(ViewManager.getInstance().get("login"));

//		if (log.equals("account") || log.equals("login")) {
//			decorator.setContent(ViewManager.getInstance().get(log));
//		} else {
//			System.out.println("initialScene else");
//			App.decorator.addCustom(userDetail);
//			userDetail.setProfileAction(event -> {
//				// Main.ctrl.title.setText("Profile");
//				// Main.ctrl.body.setContent(ViewManager.getInstance().get("profile"));
//				userDetail.getPopOver().hide();
//			});
//
//			userDetail.setSignAction(event -> {
//				App.decorator.setContent(ViewManager.getInstance().get("login"));
//				section.setLogged(false);
//				SectionManager.save(section);
//				userDetail.getPopOver().hide();
//				if (Main.popConfig.isShowing())
//					Main.popConfig.hide();
//				if (Main.popup.isShowing())
//					Main.popup.hide();
//				App.decorator.removeCustom(userDetail);
//			});
//
//			decorator.setContent(ViewManager.getInstance().get("customerHomeView"));
//		}

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
