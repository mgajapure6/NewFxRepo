package test;

import java.util.Arrays;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import app.product.ProductEntity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Test extends Application {
	
	


	private static final String ITEM = "Item ";
	private int counter = 0;

	@Override
	public void start(Stage stage) throws Exception {

		JFXListView<ProductEntity> list = new JFXListView<>();
		for (int i = 0; i < 40; i++) {
			list.getItems().add(new ProductEntity(i, "productName "+i, "productDesc "+i, (double) i, 10.00));
		}
		list.getStyleClass().add("mylistview");


		list.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>() {
			@Override
			public ListCell<ProductEntity> call(ListView<ProductEntity> param) {
				System.out.println("param::"+param);
				return new XCell();
			}
		});

		
		list.depthProperty().set(++counter % 2);
		list.depthProperty().set(1);

		FlowPane pane = new FlowPane();
		pane.setStyle("-fx-background-color:WHITE");

		JFXButton button3D = new JFXButton("3D");
		button3D.setOnMouseClicked(e -> list.depthProperty().set(++counter % 2));

		JFXButton buttonExpand = new JFXButton("EXPAND");
		buttonExpand.setOnMouseClicked(e -> {
			list.depthProperty().set(1);
			list.setExpanded(true);
		});

		JFXButton buttonCollapse = new JFXButton("COLLAPSE");
		buttonCollapse.setOnMouseClicked(e -> {
			list.depthProperty().set(1);
			list.setExpanded(false);
		});

		pane.getChildren().add(button3D);
		pane.getChildren().add(buttonExpand);
		pane.getChildren().add(buttonCollapse);

		AnchorPane listsPane = new AnchorPane();
		listsPane.getChildren().add(list);
		//AnchorPane.setLeftAnchor(list, 20.0);
		//listsPane.getChildren().add(javaList);
		//AnchorPane.setLeftAnchor(javaList, 300.0);

		VBox box = new VBox();
		box.setPadding(new Insets(20, 0, 0, 20));
		box.getChildren().add(pane);
		box.getChildren().add(list);
		box.setSpacing(40);

		StackPane main = new StackPane();
		main.getChildren().add(box);
		main.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		StackPane.setMargin(pane, new Insets(20, 0, 0, 20));

		final Scene scene = new Scene(main, 600, 600, Color.WHITE);
		stage.setTitle("JFX ListView Demo ");
		scene.getStylesheets().add(Test.class.getResource("jfoenix-components.css").toExternalForm());
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	

	class XCell extends ListCell<ProductEntity> {
		HBox hbox = new HBox();
		Label label = new Label("(empty)");
		Pane pane = new Pane();
		JFXButton button = new JFXButton("Buy Now");
		String lastItem;

		public XCell() {
			super();
			button.getStyleClass().add("jfx-button");
			button.setStyle("-jfx-button-type: RAISED;-fx-background-color: blue;-fx-text-fill: white;");
			hbox.getChildren().addAll(label, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			button.setOnAction(event -> {
				System.out.println("click::"+event.getSource());
				Object node = event.getSource();
				System.out.println(node instanceof JFXButton);
				JFXButton b = (JFXButton)node;
				String itmId = null;
				for (String i : b.getStyleClass()) {
				    if (i.startsWith("itmId")) { // startsWith() returns boolean
				    	itmId = i.substring(i.lastIndexOf("d")+1, i.length());
				    }
				}
				System.out.println("btn itmId:"+itmId);
		        
			});
		}

		@Override
		protected void updateItem(ProductEntity item, boolean empty) {
			super.updateItem(item, empty);
			setText(null); // No text in label of super class
			System.out.println("item, empty::"+item+","+empty);
			if (empty) {
				lastItem = null;
				setGraphic(null);
			} else {
				button.getStyleClass().add(item == null ? "" :"itmId"+item.getId());
				lastItem = item.getProductName();
				label.setText(item != null ? "New "+item.getProductName() : "");
				setGraphic(hbox);
			}
		}
	}

}
