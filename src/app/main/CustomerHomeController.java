package app.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import app.product.ProductEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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

	@FXML
	private JFXBadge cartBadge;

	@FXML
	private VBox listAppenderVbox;

	@FXML
	private JFXDialog cartDialog;
	
	@FXML
	private BorderPane cartListAppenderBorderPane;
	
	ObservableList<ProductEntity> productsObservableList = FXCollections.observableArrayList();
	ObservableList<ProductEntity> cartProductsObservableList = FXCollections.observableArrayList();
	ListView<ProductEntity> productListView = new ListView<>();
	ListView<ProductEntity> cartListView = new ListView<>();
	
	JFXSnackbar snackbar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		

		for (int i = 1; i <= 50; i++) {
			productsObservableList.add(new ProductEntity(i, "productName " + i, "productDesc " + i, (double) i, 10.00));
		}

		productListView.getItems().addAll(productsObservableList);

		cartListView.getItems().addAll(cartProductsObservableList);

		productListView.setCellFactory(prodListView -> new ProductListViewCell());
		
		cartListView.setCellFactory(prodListView -> new CartListViewCell());

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
		
		cartListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
					System.out.println("tableView on focus");
				} else {
					cartListView.getSelectionModel().clearSelection();
					System.out.println("tableView out focus");
				}
			}
		});

		VBox.setVgrow(productListView, Priority.ALWAYS);
		listAppenderVbox.getChildren().add(productListView);
		cartListAppenderBorderPane.setCenter(cartListView);

		snackbar = new JFXSnackbar((Pane) spRoot);
		snackbar.setStyle("-jfx-background-color: #f44336");

		cartDialog.setDialogContainer(spRoot);
		cartDialog.prefWidth(700);

	}

	@FXML
	public void openCart() {
		cartDialog.setTransitionType(DialogTransition.CENTER);
		cartDialog.show();
	}

	class ProductListViewCell extends ListCell<ProductEntity> {
		@FXML
		private Label productName;

		@FXML
		private Label productDesc;

		@FXML
		private Label productPrice;

		@FXML
		private Button addToCartBtn;

		@FXML
		private HBox hboxProcudtListCell;

		private FXMLLoader mLLoader;

		@Override
		protected void updateItem(ProductEntity pe, boolean empty) {
			super.updateItem(pe, empty);

			if (empty || pe == null) {
				setText(null);
				setGraphic(null);
				setOpacity(0);
			} else {
				setOpacity(1);
				if (mLLoader == null) {
					mLLoader = new FXMLLoader(getClass().getResource("ProductListCell.fxml"));
					mLLoader.setController(this);

					try {
						mLLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				

				addToCartBtn.getStyleClass().add(pe == null ? "" : "itmId" + pe.getId());

				addToCartBtn.setOnAction(event -> {
					Object node = event.getSource();
					System.out.println(node instanceof Button);
					Button b = (Button) node;
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
					cartProductsObservableList.add(pe);
					cartListView.getItems().add(pe);

					for (ProductEntity productEntity : productsObservableList) {
						if (productEntity.getId() == Integer.parseInt(itmId)) {
							cartProductsObservableList.add(productEntity);
							//snackbar.close();
							JFXSnackbarLayout sbl = new JFXSnackbarLayout(productEntity.getProductName() + " is added to cart ");
						
							
							snackbar.enqueue(new SnackbarEvent(
									sbl,
									Duration.millis(3000)));
							//snackbar.fireEvent();
						}
					}
					System.out.println("cartBadge::" + cartBadge.getText());
					System.out.println("btn itmId:" + itmId);

				});

				productName.setText(String.valueOf(pe.getProductName()));
				productDesc.setText(String.valueOf(pe.getProductDesc()));
				productPrice.setText("$ " + pe.getPrice().toString());

				// setText(null);
				setGraphic(hboxProcudtListCell);
			}

		}

	}
	
	class CartListViewCell extends ListCell<ProductEntity> {
		@FXML
	    private Label cartProductName;

	    @FXML
	    private Label cartProductDesc;

	    @FXML
	    private Spinner<Double> cartProductQty;

	    @FXML
	    private Label cartProductUnitPrice;

	    @FXML
	    private Label cartProductTotAmt;
		
	    @FXML
	    private Button removeCartItmBtn;
	    
	    @FXML
	    private HBox hboxCartListCell;

		private FXMLLoader mLLoader;

		@Override
		protected void updateItem(ProductEntity pe, boolean empty) {
			super.updateItem(pe, empty);

			if (empty || pe == null) {
				setText(null);
				setGraphic(null);
				setOpacity(0);
			} else {
				setOpacity(1);
				if (mLLoader == null) {
					mLLoader = new FXMLLoader(getClass().getResource("CartListCell.fxml"));
					mLLoader.setController(this);

					try {
						mLLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				//cartProductsObservableList.add(pe);


				removeCartItmBtn.setOnAction(event -> {
					Object node = event.getSource();
					System.out.println(node instanceof Button);
					Button b = (Button) node;
					String itmId = null;
					for (String i : b.getStyleClass()) {
						if (i.startsWith("itmId")) {
							itmId = i.substring(i.lastIndexOf("d") + 1, i.length());
						}
					}

					int value = Integer.parseInt(cartBadge.getText());
					value = value - 1;
					if (value == 0) {
						cartBadge.setEnabled(false);
					} else {
						cartBadge.setEnabled(true);
					}
					cartBadge.setText(String.valueOf(value));

					for (ProductEntity productEntity : productsObservableList) {
						if (productEntity.getId() == Integer.parseInt(itmId)) {
							cartProductsObservableList.add(productEntity);
							snackbar.close();
							snackbar.setPrefWidth(300);
							snackbar.fireEvent(new SnackbarEvent(
									new JFXSnackbarLayout(productEntity.getProductName() + " is remove from cart ",
											"CLOSE", action -> snackbar.close()),
									Duration.millis(5000), null));
						}
					}
					System.out.println("cartBadge::" + cartBadge.getText());
					System.out.println("btn itmId:" + itmId);

				});

				cartProductName.setText(String.valueOf(pe.getProductName()));
				cartProductDesc.setText(String.valueOf(pe.getProductDesc()));
				cartProductUnitPrice.setText("$ " + pe.getPrice().toString());


				// setText(null);
				setGraphic(hboxCartListCell);
			}

		}

	}

}
