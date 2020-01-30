package app.main;

import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import app.product.ProductEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.Duration;

public class CustomerHomeController implements Initializable {

	@FXML
	private StackPane spRoot;

	@FXML
	private Tab productsTab;

	@FXML
	private FlowPane productFlowPane;

	@SuppressWarnings("rawtypes")
	@FXML
	private JFXListView productListView;
	
	JFXSnackbar snackbar;

	@FXML
	private JFXBadge cartBadge;

	ObservableList<ProductEntity> productsObservableList = FXCollections.observableArrayList();
	ObservableList<ProductEntity> cartProductsObservableList = FXCollections.observableArrayList();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		for (int i = 1; i <= 50; i++) {
			productsObservableList.add(new ProductEntity(i, "productName " + i, "productDesc " + i, (double) i, 10.00));
		}

		productListView.getItems().addAll(productsObservableList);

		productListView.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>() {
			@Override
			public ListCell<ProductEntity> call(ListView<ProductEntity> param) {
				return new XCell();
			}
		});

		productListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("tableView on focus");
				} else {
					productListView.getSelectionModel().clearSelection();
					System.out.println("tableView out focus");
				}
			}
		});
		
		snackbar = new JFXSnackbar((Pane) spRoot);

		// productListView.setPrefHeight(150.00);
	}

	class XCell extends ListCell<ProductEntity> {
		HBox hbox = new HBox();
		VBox vb = new VBox();
		Label labelTitle = new Label();
		Label labelDesc = new Label();
		Label labelPrice = new Label();
		Pane pane = new Pane();
		JFXButton button = new JFXButton("+ Add To Cart");
		String lastItem;

		public XCell() {
			super();
			vb.getChildren().addAll(labelTitle, labelDesc, labelPrice);
			button.getStyleClass().addAll("btn-secondary");
			button.setMaxHeight(30);
			pane.setMaxHeight(300);
			hbox.getChildren().addAll(vb, pane, button);
			hbox.setAlignment(Pos.CENTER_RIGHT);
			HBox.setHgrow(pane, Priority.ALWAYS);
			button.setOnAction(event -> {
				System.out.println("click::" + event.getSource());
				Object node = event.getSource();
				System.out.println(node instanceof JFXButton);
				JFXButton b = (JFXButton) node;
				String itmId = null;
				for (String i : b.getStyleClass()) {
					if (i.startsWith("itmId")) {
						itmId = i.substring(i.lastIndexOf("d") + 1, i.length());
					}
				}

				int value = Integer.parseInt(cartBadge.getText());
				value = value + 1;
				if (value == 0) {
					cartBadge.setEnabled(false);
				} else {
					cartBadge.setEnabled(true);
				}
				cartBadge.setText(String.valueOf(value));
				// snackbar.fireEvent(new SnackbarEvent("Snackbar Message Persistent " +
				// value));
				// snackbar.fireEvent(new SnackbarEvent("Snackbar Message Persistent"));

				for (ProductEntity productEntity : productsObservableList) {
					if (productEntity.getId() == Integer.parseInt(itmId)) {
						cartProductsObservableList.add(productEntity);
						snackbar.close();
						snackbar.setPrefWidth(300);
						snackbar.fireEvent(new SnackbarEvent(
								new JFXSnackbarLayout(productEntity.getProductName() + " is added to cart ", "CLOSE",
										action -> snackbar.close()),
								Duration.millis(5000), null));
					}
				}
				System.out.println("cartBadge::" + cartBadge.getText());
				System.out.println("btn itmId:" + itmId);

			});
		}

		@Override
		protected void updateItem(ProductEntity item, boolean empty) {
			super.updateItem(item, empty);
			setText(null); // No text in label of super class
			System.out.println("item, empty::" + item + "," + empty);
			if (empty) {
				lastItem = null;
				setGraphic(null);
			} else {

				hbox.prefHeight(400);
				// labelTitle.setFont(Font.font(null, FontWeight.BOLD, 15));
				// labelPrice.setFont(Font.font(null, FontWeight.BOLD, 12));
				button.getStyleClass().add(item == null ? "" : "itmId" + item.getId());
				lastItem = item.getProductName();
				labelTitle.setText(item != null ? item.getProductName() : "");
				labelDesc.setText(item != null ? item.getProductDesc() : "");
				labelPrice.setText(item != null ? "$ " + String.valueOf(item.getPrice()) : "");
				setStyle(" -fx-border-color : #ccc;-fx-border-width : 0 0 1 0;-fx-fixed-cell-size : 100px;");
				setGraphic(hbox);
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}
		}
	}

	@FXML
	public void openCart() {

	}

}
