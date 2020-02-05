package app.main;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import app.App;
import app.db.dao.BillDao;
import app.db.dao.ProductDao;
import app.db.domain.Bill;
import app.db.domain.BillProviderProduct;
import app.db.domain.ProviderProduct;
import app.db.domain.User;
import app.db.dto.ProductDto;
import app.global.Alerts;
import app.global.DateFormatUtil;
import app.global.StringCheckerUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	private VBox productListAppenderVbox;

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

	@FXML
	private JFXDialog placeOrderDialog;

	@FXML
	private JFXButton cancelPlaceOrderBtn;

	@FXML
	private VBox placeOrderListView;

	@FXML
	private TextField payingAmountField;

	@FXML
	private Text placeOrderBillTo;
	@FXML
	private Text placeOrderBillAddr;
	@FXML
	private Text placeOrderTotQty;
	@FXML
	private Text placeOrderTotAmt;
	@FXML
	private Text placeOrderBalAmt;
	@FXML
	private Label alertDialogTitle;
	@FXML
	private Button alertDialogBtn;

	@FXML
	private JFXDialog alertDialog;

	@FXML
	private ToggleGroup payNowLaterToggleGroup;

	User loggedUser = App.getUserDetail().getLoggedUser();

	ListView<ProviderProduct> productListView = new ListView<>();
	ListView<ProviderProduct> cartListView = new ListView<>();
	
	@FXML
	Tab myOrdersTab;
	
	@FXML
	TabPane mainTabPane;
	

	JFXSnackbar snackbar;

//	my orders tab
	ListView<Bill> myOrderListView = new ListView<>();
	@FXML
	Label myOrdersTotalOrders;
	@FXML
	Label myOrderPendingBills;
	@FXML
	Label myOrderPendingAmt;
	@FXML
	Label myOrderPaidAmt;
	@FXML
	Label myOrderNumberTitle;
	@FXML
	BorderPane myOrderDetailListAppenderBorderPane;
	@FXML
	Label myOrderTotalAmt;
	@FXML
	Button myOrderOkBtn;

	@FXML
	VBox billListAppenderVbox;
	
	@FXML
	private JFXDialog myOrderDetailDialog;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				tabOne();
			}
		});
		//tabOne();
		tabTwo();
	}

	public void tabTwo() {

	}

	public void tabOne() {
		ProductDao productDao = new ProductDao();
		BillDao billDao = new BillDao();
		List<ProviderProduct> providerProducts = productDao.getAllProviderProducts();
		List<Bill> bills = billDao.getBillsByCustomerId(loggedUser.getCustomer().getCustomerId());

		for (ProviderProduct providerProduct : providerProducts) {
			productListView.getItems().add(providerProduct);
		}

		for (Bill bill : bills) {
			myOrderListView.getItems().add(bill);
		}

		productListView.setCellFactory(prodListView -> new ProductListViewCell());

		cartListView.setCellFactory(prodListView -> new CartListViewCell());

		myOrderListView.setCellFactory(prodListView -> new MyOrderListViewCell());

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
		VBox.setVgrow(myOrderListView, Priority.ALWAYS);
		
		productListAppenderVbox.getChildren().add(productListView);
		cartListAppenderBorderPane.setCenter(cartListView);
		billListAppenderVbox.getChildren().add(myOrderListView);

		snackbar = new JFXSnackbar((Pane) spRoot);
		snackbar.setStyle("-jfx-background-color: #f44336");

		cartDialog.setDialogContainer(spRoot);
		cartDialog.prefWidth(700);

		placeOrderDialog.setDialogContainer(spRoot);
		
		myOrderDetailDialog.setDialogContainer(spRoot);

		alertDialog.setDialogContainer(spRoot);

		payingAmountField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (StringCheckerUtil.isInteger(newValue) || StringCheckerUtil.isDouble(newValue)) {

					if (Double.parseDouble(newValue) > Double
							.parseDouble(cartTotalAmt.getText().trim().split(" ")[1])) {
						payingAmountField.getStyleClass().add("tferror");
						placeOrderBtn.setDisable(true);
						placeOrderBalAmt.setText(
								"$ " + String.valueOf(Double.parseDouble(cartTotalAmt.getText().trim().split(" ")[1])));
					} else {
						payingAmountField.getStyleClass().remove("tferror");
						placeOrderBtn.setDisable(false);
						placeOrderBalAmt.setText(
								"$ " + String.valueOf(Double.parseDouble(cartTotalAmt.getText().trim().split(" ")[1])
										- Double.parseDouble(newValue)));
					}
				} else if (!newValue.isEmpty()) {
					System.out.println("adding class:: err");
					payingAmountField.getStyleClass().add("tferror");
					placeOrderBtn.setDisable(true);
					placeOrderBalAmt.setText(
							"$ " + String.valueOf(Double.parseDouble(cartTotalAmt.getText().trim().split(" ")[1])));
				} else {
					placeOrderBalAmt.setText(
							"$ " + String.valueOf(Double.parseDouble(cartTotalAmt.getText().trim().split(" ")[1])));
				}

			}
		});

		cartBuyNowBtn.setOnAction(event -> {
			Integer itmsCount = 0;
			for (ProviderProduct providerProduct : cartListView.getItems()) {
				itmsCount = itmsCount + providerProduct.getQtyAvailable();
			}
			placeOrderBillTo.setText(loggedUser.getCustomer().getCustomerName());
			placeOrderBillAddr.setText(loggedUser.getCustomer().getAddress());
			placeOrderTotQty.setText(itmsCount.toString());
			placeOrderTotAmt.setText(cartTotalAmt.getText());
			placeOrderBalAmt.setText(cartTotalAmt.getText());
			placeOrderDialog.setTransitionType(DialogTransition.CENTER);
			placeOrderDialog.show();
		});

		Alerts.success("Success", "Order Placed Successfully");

		placeOrderBtn.setOnAction(event -> {
			ObservableList<ProviderProduct> list = cartListView.getItems();
			Set<BillProviderProduct> billProviderProductSet = new HashSet<>();
			for (ProviderProduct providerProduct : list) {
				BillProviderProduct bpp = new BillProviderProduct();
				bpp.setQtyRequested(providerProduct.getQtyAvailable());
				bpp.setProviderProduct(providerProduct);
				billProviderProductSet.add(bpp);
			}
			Double balAmt = Double.parseDouble(placeOrderBalAmt.getText().trim().split(" ")[1]);
			boolean isSaved = billDao.saveBill(loggedUser.getCustomer(), billProviderProductSet, balAmt);
			if (isSaved) {
				cartListView.getItems().clear();
				placeOrderDialog.close();
				cartDialog.close();
				cartTotalAmt.setText("$ 0.00");
				cartBadge.setText("0");
				alertDialogTitle.setText("Order Placed Successfully");
				alertDialog.setTransitionType(DialogTransition.CENTER);
				alertDialogBtn.getStyleClass().remove("btn-danger");
				alertDialogBtn.getStyleClass().add("btn-success");
				alertDialog.show();

				// Alerts.success("Success", "Order Placed Successfully");
			} else {
				// Alerts.error("Error", "Something went worng.. Unable to save this order.");
				alertDialogTitle.setText("Something went worng.. Unable to save this order");
				alertDialog.setTransitionType(DialogTransition.CENTER);
				alertDialogBtn.getStyleClass().remove("btn-success");
				alertDialogBtn.getStyleClass().add("btn-danger");
				alertDialog.show();
			}
			// bill.setBillProviderProducts(set);

		});

		cancelPlaceOrderBtn.setOnAction(event -> {
			placeOrderDialog.close();
		});

		alertDialogBtn.setOnAction(event -> {
			alertDialog.close();
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

	class MyOrderListViewCell extends ListCell<Bill> {

		@FXML
		private Button moCellViewOrderBtn;

		@FXML
		private HBox hboxMyOrderListCell;

		@FXML
		Label moCellOrderId;
		@FXML
		Label moCellDate;
		@FXML
		Label moCellOrderAmt;
		@FXML
		Label myCellStatus;

		private FXMLLoader mLLoader;

		@Override
		protected void updateItem(Bill bill, boolean empty) {
			super.updateItem(bill, empty);

			if (empty || bill == null) {
				setText(null);
				setGraphic(null);
				setOpacity(0);
			} else {
				setOpacity(1);
				if (mLLoader == null) {
					mLLoader = new FXMLLoader(getClass().getResource("myOderCell.fxml"));
					mLLoader.setController(this);
					try {
						mLLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				moCellOrderId.setText("# Order Number " + bill.getBillId());

				moCellDate.setText("# Order Date " + DateFormatUtil.dateToString(bill.getBillDate(), "dd-MM-YYYY"));

				moCellOrderAmt.setText("$ " + bill.getBillAmount());

				myCellStatus.setText(bill.getIsPaid() ? "Paid" : "Unpaid");

				moCellViewOrderBtn.setOnAction(event -> {
					BillDao billDao = new BillDao();
					ListView<ProductDto> myOrderDetialListView = new ListView<>();
					
					List<ProductDto> pdts = billDao.getBillDetailById(bill.getBillId());
					
					for (ProductDto productDto : pdts) {
						myOrderDetialListView.getItems().add(productDto);
					}
					
					myOrderDetialListView.setPrefHeight(200);
					
					VBox.setVgrow(myOrderDetialListView, Priority.ALWAYS);
					myOrderDetailListAppenderBorderPane.setCenter(myOrderDetialListView);
					myOrderDetialListView.setCellFactory(prodListView -> new MyOrderDetailListViewCell());
					myOrderNumberTitle.setText("# Order number "+bill.getBillId());
					myOrderTotalAmt.setText("$ "+bill.getBillAmount());
					myOrderDetailDialog.setTransitionType(DialogTransition.CENTER);
					myOrderDetailDialog.show();
				});
				setGraphic(hboxMyOrderListCell);
			}

		}

	}

	class MyOrderDetailListViewCell extends ListCell<ProductDto> {

		@FXML
		private HBox hboxOrderDetailListCell;

		@FXML
		Label moDetailCellProductName;
		
		@FXML
		Label moDetailCellProductDesc;
		@FXML
		Label moDetailCellProductQty;
		@FXML
		Label moDetailCellProductUnitPrice;
		@FXML
		Label moDetailCellProductTotAmt;
		

		private FXMLLoader mLLoader;

		@Override
		protected void updateItem(ProductDto pd, boolean empty) {
			super.updateItem(pd, empty);

			if (empty || pd == null) {
				setText(null);
				setGraphic(null);
				setOpacity(0);
			} else {
				setOpacity(1);
				if (mLLoader == null) {
					mLLoader = new FXMLLoader(getClass().getResource("MyOrderDetailListCell.fxml"));
					mLLoader.setController(this);
					try {
						mLLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				moDetailCellProductName.setText("" + pd.getProductName());

				moDetailCellProductDesc.setText("" +pd.getDescription());

				moDetailCellProductQty.setText("" + pd.getQty());

				moDetailCellProductUnitPrice.setText(""+pd.getRate());
				
				moDetailCellProductTotAmt.setText("" + pd.getAmount());

//				moCellViewOrderBtn.setOnAction(event -> {
//
//				});
				setGraphic(hboxOrderDetailListCell);
			}

		}

	}

}
