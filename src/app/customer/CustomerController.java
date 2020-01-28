
package app.customer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CustomerController implements Initializable {
	@FXML
	private StackPane root;
	@FXML
	private TableView<CustomerEntity> tableView;
	@FXML
	private TableColumn<String, CustomerEntity> c1;
	@FXML
	private TableColumn<String, CustomerEntity> c2;
	@FXML
	private TableColumn<String, CustomerEntity> c3;
	@FXML
	private Button addCustomerBtn;
	@FXML
	private Button ediCustomerBtn;
	@FXML
	private Button deleteCustomerBtn;
	@FXML
	private TextField searchField;//
	
	@FXML
	private JFXDialog customerDialog;
	@FXML
	private JFXTextField customerNameField;
	@FXML
	private JFXTextArea customerAddressField;
	@FXML
	private JFXTextField customerQtyField;
	@FXML
	private JFXTextField customerPriceField;
	
	@FXML
	private JFXDialog customerDeleteConfirmDialog;
	@FXML
	private VBox btnVbox;
	
	ObservableList<CustomerEntity> customersObservableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		for (int i = 1; i <= 50; i++) {
			CustomerEntity cu = new CustomerEntity();
			cu.setId(i);
			cu.setCustomerName("Customer "+i);
			cu.setCustomerAddress("Address "+i);
			
			List<AccountsEntity> accs = new ArrayList<>();
			for (int j = 1; j <= 3; j++) {
				accs.add(new AccountsEntity(j, j*24.50, i));
			}
			
			List<BillEntity> bills = new ArrayList<>();
			for (int j = 1; j <= 4; j++) {
				bills.add(new BillEntity(j, new Date(), "B"+j, 1, i));
			}
			cu.setAccounts(accs);
			cu.setBills(bills);
			customersObservableList.add(cu);
		}
		
		c1.setCellValueFactory(new PropertyValueFactory<>("id"));
		c2.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		c3.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
		tableView.setItems(customersObservableList);
		
		
		FilteredList<CustomerEntity> filteredData = new FilteredList<>(customersObservableList, p -> true);
		searchField.textProperty().addListener((observable, oldValue, newValue)->{
			filteredData.setPredicate(customerProp -> {
				if (newValue == null || newValue.isEmpty()) {
	                return true;
	            }
	            String lowerCaseFilter = newValue.toLowerCase();
	            if (customerProp.customerName.toLowerCase().contains(lowerCaseFilter)) {
	                return true;
	            } else if (customerProp.customerAddress.toLowerCase().contains(lowerCaseFilter)) {
	                return true;
	            }
	            return false; // Does not match.
	        });
		});
		
		SortedList<CustomerEntity> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        
        customerDialog.setDialogContainer(root);
        customerDeleteConfirmDialog.setDialogContainer(root);
        
        addCustomerBtn.setOnAction((e) -> {
        	customerNameField.setText("");
        	customerAddressField.setText(""); 
			customerDialog.setTransitionType(DialogTransition.CENTER);
			customerDialog.show();
		});
        
        ediCustomerBtn.setOnAction((e) -> {
        	CustomerEntity ce = tableView.getSelectionModel().selectedItemProperty().get();
        	
        	customerNameField.setText(ce.customerName);
        	customerAddressField.setText(ce.customerAddress);     
        	customerDialog.setTransitionType(DialogTransition.CENTER);
			customerDialog.show();
        	//customersObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		});
        
        deleteCustomerBtn.setOnAction((e) -> {
        	if(!tableView.getSelectionModel().isEmpty()) {
        		customerDeleteConfirmDialog.setTransitionType(DialogTransition.CENTER);
            	customerDeleteConfirmDialog.show();
        	}
        	
        	///
		});
        
        
        root.setOnMouseClicked((e)->{
        	tableView.getSelectionModel().clearSelection();
        });
        
        btnVbox.setOnMouseClicked((e)->{
        	tableView.getSelectionModel().clearSelection();
        });
        
	}

	@FXML
	public void closeConfirmDialog(){
		customerDeleteConfirmDialog.close();
	}
	
	@FXML
	public void deleteConfirmFromDialog(){
		customersObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		customerDeleteConfirmDialog.close();
	}
	
}
