package app.provider_main;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import app.App;
import app.db.dao.BillDao;
import app.db.dao.ProductDao;
import app.db.domain.Bill;
import app.db.domain.ProviderProduct;
import app.db.domain.User;
import app.db.dto.ProductDto;
import app.db.dto.ProviderBillDto;
import app.db.services.BillService;
import app.db.services.ProductService;
import app.db.services.ProviderService;
import app.global.Alerts;
import app.global.DateFormatUtil;
import app.global.StringCheckerUtil;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

public class ProviderBillView implements Initializable {

	@FXML
	private StackPane spRoot;

	@FXML
	private VBox billListAppenderVbox;

	@FXML
	private Label alertDialogTitle;

	@FXML
	private Button alertDialogBtn;

	@FXML
	private JFXDialog alertDialog;

	@FXML
	private TextField searchField;

	@FXML
	private TableView<ProviderBillDto> tableView;
	@FXML
	private TableColumn<ProviderBillDto, ProviderBillDto> c1;
	@FXML
	private TableColumn<ProviderBillDto, Integer> c2;
	@FXML
	private TableColumn<ProviderBillDto, String> c3;
	@FXML
	private TableColumn<ProviderBillDto, String> c4;
	@FXML
	private TableColumn<ProviderBillDto, String> c5;
	@FXML
	private TableColumn<ProviderBillDto, String> c6;
	@FXML
	private TableColumn<ProviderBillDto, String> c7;
	@FXML
	private TableColumn<ProviderBillDto, Integer> c8;
	@FXML
	private TableColumn<ProviderBillDto, Integer> c9;
	@FXML
	private TableColumn<ProviderBillDto, Double> c10;
	@FXML
	private TableColumn<ProviderBillDto, Double> c11;

	User loggedUser = App.getUserDetail().getLoggedUser();

	ObservableList<ProviderBillDto> billsObservableList = FXCollections.observableArrayList();

	JFXSnackbar snackbar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				initData();
			}
		});
	}

	public void initData() {
		ProviderService providerService = new ProviderService();
		List<ProviderBillDto> providerBills = providerService
				.getProviderBillsById(loggedUser.getProvider().getProviderId());

		for (ProviderBillDto pbdto : providerBills) {
			billsObservableList.add(pbdto);
		}

		c1.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, ProviderBillDto>, ObservableValue<ProviderBillDto>>() {
					@Override
					public ObservableValue<ProviderBillDto> call(
							TableColumn.CellDataFeatures<ProviderBillDto, ProviderBillDto> p) {
						return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue()) + 1 + "");
					}
				});
		c2.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ProviderBillDto, Integer> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getBillId());
					}

				});
		c3.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderBillDto, String> pp) {
						return new SimpleObjectProperty<>(
								DateFormatUtil.dateToString(pp.getValue().getBillDate(), "dd-MM-YYYY"));
					}

				});

		c4.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderBillDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getStatus());
					}

				});
		c5.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderBillDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getCustomerName());
					}

				});
		c6.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderBillDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getAddress());
					}

				});
		c7.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ProviderBillDto, String> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getProductName());
					}

				});
		c8.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ProviderBillDto, Integer> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getQtyRequested());
					}

				});
		c9.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, Integer>, ObservableValue<Integer>>() {
					@Override
					public ObservableValue<Integer> call(TableColumn.CellDataFeatures<ProviderBillDto, Integer> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getQtyAvailable());
					}

				});
		c10.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, Double>, ObservableValue<Double>>() {
					@Override
					public ObservableValue<Double> call(TableColumn.CellDataFeatures<ProviderBillDto, Double> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getPrice());
					}

				});
		c11.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ProviderBillDto, Double>, ObservableValue<Double>>() {
					@Override
					public ObservableValue<Double> call(TableColumn.CellDataFeatures<ProviderBillDto, Double> pp) {
						return new SimpleObjectProperty<>(pp.getValue().getBillAmount());
					}

				});

		FilteredList<ProviderBillDto> filteredData = new FilteredList<>(billsObservableList, p -> true);
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(pb -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (pb.getProductName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (pb.getDescription().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (pb.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (pb.getAddress().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (pb.getStatus().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (DateFormatUtil.dateToString(pb.getBillDate(), "dd-MM-YYYY").contains(lowerCaseFilter)) {
					return true;
				} else if (Double.toString(pb.getPrice()).contains(newValue)) {
					return true;
				} else if (Double.toString(pb.getBillAmount()).contains(newValue)) {
					return true;
				} else if (Integer.toString(pb.getBillId()).contains(newValue)) {
					return true;
				} else if (Integer.toString(pb.getQtyAvailable()).contains(newValue)) {
					return true;
				} else if (Integer.toString(pb.getQtyRequested()).contains(newValue)) {
					return true;
				}
				return false; // Does not match.
			});
		});

		SortedList<ProviderBillDto> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedData);

		snackbar = new JFXSnackbar((Pane) spRoot);
		snackbar.setStyle("-jfx-background-color: #f44336");

		alertDialog.setDialogContainer(spRoot);

		// billListAppenderVbox.

		alertDialogBtn.setOnAction(event -> {
			alertDialog.close();
		});

	}

}
