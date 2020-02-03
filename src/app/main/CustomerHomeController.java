package app.main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import app.App;
import app.db.dao.ProductDao;
import app.db.domain.Product;
import app.db.domain.ProviderProduct;
import app.db.domain.User;
import app.product.ProductEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

	@FXML
	private Button cartBuyNowBtn;

	@FXML
	private Label cartTotalAmt;

	@FXML
	private Button placeOrderBtn;
	
	User loggedUser = App.getUserDetail().getLoggedUser();

	ListView<ProviderProduct> productListView = new ListView<>();
	ListView<ProviderProduct> cartListView = new ListView<>();

	JFXSnackbar snackbar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ProductDao productDao = new ProductDao();
		List<ProviderProduct> providerProducts = productDao.getAllProviderProducts();

		for (ProviderProduct providerProduct : providerProducts) {
			productListView.getItems().add(providerProduct);
		}

//		for (int i = 1; i <= 50; i++) {
//			productListView.getItems()
//					.add(new ProductEntity(i, "productName " + i, "productDesc " + i, (double) i, 10.00));
//		}

		productListView.setCellFactory(prodListView -> new ProductListViewCell());

		cartListView.setCellFactory(prodListView -> new CartListViewCell());

		productListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
				} else {
					productListView.getSelectionModel().clearSelection();
				}
			}
		});

		cartListView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (newPropertyValue) {
				} else {
					cartListView.getSelectionModel().clearSelection();
				}
			}
		});

		VBox.setVgrow(productListView, Priority.ALWAYS);
		VBox.setVgrow(cartListView, Priority.ALWAYS);
		listAppenderVbox.getChildren().add(productListView);
		cartListAppenderBorderPane.setCenter(cartListView);

		snackbar = new JFXSnackbar((Pane) spRoot);
		snackbar.setStyle("-jfx-background-color: #f44336");

		cartDialog.setDialogContainer(spRoot);
		cartDialog.prefWidth(700);

		placeOrderBtn.setOnAction(event -> {

		});

	}

	@FXML
	public void openCart() {
		calculateCartValue();
		cartDialog.setTransitionType(DialogTransition.CENTER);
		cartDialog.show();
	}

	void calculateCartValue() {
		Double totAmt = 0.0;
		for (ProviderProduct providerProduct : cartListView.getItems()) {
			Double itmsTotAmt = providerProduct.getProduct().getPrice() * providerProduct.getQtyAvailable();
			System.out.println("itmsTotAmt: " + itmsTotAmt);
			totAmt = totAmt + itmsTotAmt;
		}
		System.out.println("totAmt: " + totAmt);
		cartTotalAmt.setText("$ " + String.valueOf(totAmt));
	}

	class ProductListViewCell extends ListCell<ProviderProduct> {
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
		protected void updateItem(ProviderProduct pe, boolean empty) {
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

				addToCartBtn.getStyleClass().add(pe == null ? "" : "itmId" + pe.getProduct().getProductId());
				addToCartBtn.getStyleClass().add(pe == null ? "" : "listItemIndex" + getIndex());

				addToCartBtn.setOnAction(event -> {
					pe.setQtyAvailable(1);
					cartListView.getItems().add(pe);
					cartBadge.setText(String.valueOf(cartListView.getItems().size()));
					JFXSnackbarLayout sbl = new JFXSnackbarLayout(
							pe.getProduct().getProductName() + " is added to cart ");

					snackbar.enqueue(new SnackbarEvent(sbl, Duration.millis(3000)));

				});

				productName.setText(String.valueOf(pe.getProduct().getProductName()));
				productDesc.setText(String.valueOf(pe.getProduct().getDescription()));
				productPrice.setText("$ " + pe.getProduct().getPrice().toString());
				setGraphic(hboxProcudtListCell);
			}

		}

	}

	class CartListViewCell extends ListCell<ProviderProduct> {
		@FXML
		private Label cartProductName;

		@FXML
		private Label cartProductDesc;

		@FXML
		private Spinner<Integer> cartProductQty;

		@FXML
		private Label cartProductUnitPrice;

		@FXML
		private Label cartProductTotAmt;

		@FXML
		private Button removeCartItmBtn;

		@FXML
		private HBox hboxCartListCell;

		private FXMLLoader mLLoader;

		IntegerSpinnerValueFactory spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
				Integer.MAX_VALUE, 1, 1);

		@Override
		protected void updateItem(ProviderProduct pe, boolean empty) {
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

				cartProductQty.setValueFactory(spinnerValueFactory);
				spinnerValueFactory.setValue(pe.getQtyAvailable());
				cartProductTotAmt.setText("$ " + String.valueOf((1 * pe.getProduct().getPrice())));

				cartProductQty.valueProperty().addListener((obs, oldValue, newValue) -> {
					pe.setQtyAvailable(newValue);
					cartProductTotAmt.setText("$ " + newValue * pe.getProduct().getPrice());
					calculateCartValue();
				});

				removeCartItmBtn.getStyleClass().add(pe == null ? "" : "itmId" + pe.getProduct().getProductId());
				removeCartItmBtn.getStyleClass().add(pe == null ? "" : "listItemIndex" + getIndex());

				removeCartItmBtn.setOnAction(event -> {

					cartListView.getItems().remove(getIndex());

					cartBadge.setText(String.valueOf(cartListView.getItems().size()));

					if (cartListView.getItems().size() <= 0) {
						cartBuyNowBtn.setDisable(true);
					} else {
						cartBuyNowBtn.setDisable(false);
					}

					calculateCartValue();

					snackbar.fireEvent(new SnackbarEvent(
							new JFXSnackbarLayout(pe.getProduct().getProductName() + " is remove from cart ", "CLOSE",
									action -> snackbar.close()),
							Duration.millis(2000), null));
				});

				if (cartListView.getItems().size() <= 0) {
					cartBuyNowBtn.setDisable(true);
				} else {
					cartBuyNowBtn.setDisable(false);
				}

				cartProductName.setText(String.valueOf(pe.getProduct().getProductName()));
				cartProductDesc.setText(String.valueOf(pe.getProduct().getDescription()));
				cartProductUnitPrice.setText("$ " + pe.getProduct().getPrice().toString());
				setGraphic(hboxCartListCell);
			}

		}

	}

}
