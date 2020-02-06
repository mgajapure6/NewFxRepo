
package app.provider_main;

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
import app.db.dto.ProviderProductDto;
import app.db.services.ProductService;
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

public class ProviderProductView implements Initializable {
	@FXML
	private StackPane root;
	@FXML
	private TableView<ProviderProductDto> tableView;
	@FXML
	private TableColumn<ProviderProductDto, ProviderProductDto> c1;
	@FXML
	private TableColumn<ProviderProductDto, String> c2;
	@FXML
	private TableColumn<ProviderProductDto, String> c3;
	@FXML
	private TableColumn<String, ProviderProductDto> c4;
	@FXML
	private TableColumn<ProviderProductDto, Double> c5;
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

	ObservableList<ProviderProductDto> productsObservableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lbl_pname_err.setVisible(false);
		lbl_pqty_err.setVisible(false);
		lbl_pprice_err.setVisible(false);
		ProductService productService = new ProductService();
		// ProductDao productDao = new ProductDao();
		List<ProviderProductDto> providerProducts = productService
				.getAllProviderProductsByProviderId(loggedUser.getProvider().getProviderId(), loggedUser.getProvider());

		for (ProviderProductDto providerProduct : providerProducts) {
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
				new Callback<TableColumn.CellDataFeatures<ProviderProductDto, ProviderProductDto>, ObservableValue<ProviderProductDto>>() {
					@Override
					public ObservableValue<ProviderProductDto> call(
							TableColumn.CellDataFeatures<ProviderProductDto, ProviderProductDto> p) {
						return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue()) + 1 + "");
					}
				});

		c2.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProductDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderProductDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getProductName());
					}

				});
		c3.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProductDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderProductDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getDescription());
					}

				});
		c5.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderProductDto, Double>, ObservableValue<Double>>() {
					@Override
					public ObservableValue<Double> call(TableColumn.CellDataFeatures<ProviderProductDto, Double> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getPrice());
					}

				});
		tableView.setItems(productsObservableList);

		FilteredList<ProviderProductDto> filteredData = new FilteredList<>(productsObservableList, p -> true);
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(productProp -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (productProp.getProductName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (productProp.getDescription().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (Double.toString(productProp.getPrice()).contains(newValue)) {
					return true;
				}
				return false; // Does not match.
			});
		});

		SortedList<ProviderProductDto> sortedData = new SortedList<>(filteredData);
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
			ProviderProductDto pe = tableView.getSelectionModel().selectedItemProperty().get();

			productNameField.setText(pe.getProductName());
			productDescField.setText(pe.getDescription());
			productQtyField.setText(Double.toString(pe.getQtyAvailable()));
			productPriceField.setText(Double.toString(pe.getPrice()));
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
				productService.saveProduct(product, loggedUser.getProvider(),
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
