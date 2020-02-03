
package app.product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;

import animatefx.animation.Flash;
import animatefx.animation.SlideInLeft;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import app.App;
import app.db.dao.ProductDao;
import app.db.domain.Product;
import app.db.domain.ProviderProduct;
import app.db.domain.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

public class ProductController implements Initializable {
	@FXML
	private StackPane root;
	@FXML
	private TableView<ProviderProduct> tableView;
	@FXML
	private TableColumn<ProviderProduct, ProviderProduct> c1;
	@FXML
	private TableColumn<ProviderProduct, String> c2;
	@FXML
	private TableColumn<ProviderProduct, String> c3;
	@FXML
	private TableColumn<String, ProviderProduct> c4;
	@FXML
	private TableColumn<ProviderProduct, Double> c5;
	@FXML
	private Button addProductBtn;
	@FXML
	private Button ediProductBtn;
	@FXML
	private Button deleteProductBtn;
	@FXML
	private TextField searchField;//
	@FXML
	private Button saveProductButton;

	@FXML
	private JFXDialog productDialog;
	@FXML
	private JFXTextField productNameField;
	@FXML
	private JFXTextArea productDescField;
	@FXML
	private JFXTextField productQtyField;
	@FXML
	private JFXTextField productPriceField;

	@FXML
	private JFXDialog productDeleteConfirmDialog;
	@FXML
	private VBox btnVbox;

	@FXML
	private Label lbl_pname_err;

	@FXML
	private Label lbl_pqty_err;

	@FXML
	private Label lbl_pprice_err;

	User loggedUser = App.getUserDetail().getLoggedUser();

	ObservableList<ProviderProduct> productsObservableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lbl_pname_err.setVisible(false);
		lbl_pqty_err.setVisible(false);
		lbl_pprice_err.setVisible(false);

		ProductDao productDao = new ProductDao();
		List<ProviderProduct> providerProducts = productDao
				.getAllProviderProductsByProviderId(loggedUser.getProvider().getProviderId(), loggedUser.getProvider());

		for (ProviderProduct providerProduct : providerProducts) {
			productsObservableList.add(providerProduct);
		}
//		for (int i = 1; i <= 100; i++) {
//			productsObservableList
//					.add(new ProductEntity(i, "Product " + i, "Product Desc " + i, (i + 1.00), (i * 10.5)));
//		}

		// c1.setCellValueFactory(new PropertyValueFactory<>("product.productId"));

		c4.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
		c5.setCellValueFactory(new PropertyValueFactory<>("product.price"));
		c1.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProduct, ProviderProduct>, ObservableValue<ProviderProduct>>() {
					@Override
					public ObservableValue<ProviderProduct> call(
							TableColumn.CellDataFeatures<ProviderProduct, ProviderProduct> p) {
						return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue()) + 1 + "");
					}
				});

		c2.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProduct, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderProduct, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getProduct().getProductName());
					}

				});
		c3.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProduct, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderProduct, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getProduct().getDescription());
					}

				});
		c5.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProduct, Double>, ObservableValue<Double>>() {
					@Override
					public ObservableValue<Double> call(TableColumn.CellDataFeatures<ProviderProduct, Double> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getProduct().getPrice());
					}

				});
		tableView.setItems(productsObservableList);

		FilteredList<ProviderProduct> filteredData = new FilteredList<>(productsObservableList, p -> true);
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(productProp -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (productProp.getProduct().getProductName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (productProp.getProduct().getDescription().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (Double.toString(productProp.getProduct().getPrice()).contains(newValue)) {
					return true;
				}
				return false; // Does not match.
			});
		});

		SortedList<ProviderProduct> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);

		productDialog.setDialogContainer(root);
		productDeleteConfirmDialog.setDialogContainer(root);

		addProductBtn.setOnAction((e) -> {

			System.out.println("loggedUser data:" + loggedUser);
			productNameField.setText("");
			productDescField.setText("");
			productQtyField.setText("");
			productPriceField.setText("");
			productDialog.setTransitionType(DialogTransition.CENTER);
			productDialog.show();
		});

		ediProductBtn.setOnAction((e) -> {
			ProviderProduct pe = tableView.getSelectionModel().selectedItemProperty().get();

			productNameField.setText(pe.getProduct().getProductName());
			productDescField.setText(pe.getProduct().getDescription());
			productQtyField.setText(Double.toString(pe.getQtyAvailable()));
			productPriceField.setText(Double.toString(pe.getProduct().getPrice()));
			productDialog.setTransitionType(DialogTransition.CENTER);
			productDialog.show();
			// productsObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		});

		deleteProductBtn.setOnAction((e) -> {
			if (!tableView.getSelectionModel().isEmpty()) {
				productDeleteConfirmDialog.setTransitionType(DialogTransition.CENTER);
				productDeleteConfirmDialog.show();
			}

			///
		});

		root.setOnMouseClicked((e) -> {
			tableView.getSelectionModel().clearSelection();
		});

		btnVbox.setOnMouseClicked((e) -> {
			tableView.getSelectionModel().clearSelection();
		});

		saveProductButton.setOnMouseClicked((e) -> {
			if (validProductName() && validProductPrice() && validProductQty()) {

				Product product = new Product(null, productNameField.getText(), productDescField.getText(),
						Double.parseDouble(productPriceField.getText()), null);
				productDao.saveProduct(product, loggedUser.getProvider(),
						productQtyField.getText().equals("") ? 0 : Integer.parseInt(productQtyField.getText()));
			}
		});

		productNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!validProductName()) {
				if (!newValue) {
					lbl_pname_err.setVisible(true);
					new SlideInLeft(lbl_pname_err).play();
				} else {
					lbl_pname_err.setVisible(false);
				}
			} else {
				lbl_pname_err.setVisible(false);
			}
		});

		productQtyField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!validProductQty()) {
				if (!newValue) {
					lbl_pqty_err.setVisible(true);
					new SlideInLeft(lbl_pqty_err).play();
				} else {
					lbl_pqty_err.setVisible(false);
				}
			} else {
				lbl_pqty_err.setVisible(false);
			}
		});

		productPriceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!validProductQty()) {
				if (!newValue) {
					lbl_pprice_err.setVisible(true);
					new SlideInLeft(lbl_pprice_err).play();
				} else {
					lbl_pprice_err.setVisible(false);
				}
			} else {
				lbl_pprice_err.setVisible(false);
			}
		});

	}

	@FXML
	public void closeConfirmDialog() {
		productDeleteConfirmDialog.close();
	}

	@FXML
	public void deleteConfirmFromDialog() {
		productsObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		productDeleteConfirmDialog.close();
	}

	private boolean validProductName() {
		return !productNameField.getText().isEmpty() && productNameField.getLength() > 2;
	}

	private boolean validProductPrice() {
		return !productPriceField.getText().isEmpty() && isDouble(productPriceField.getText());
	}

	private boolean validProductQty() {
		return !productQtyField.getText().isEmpty() && isInteger(productQtyField.getText());
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
}
