
package app.product;

import java.net.URL;
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

public class ProductController implements Initializable {
	@FXML
	private StackPane root;
	@FXML
	private TableView<ProductEntity> tableView;
	@FXML
	private TableColumn<String, ProductEntity> c1;
	@FXML
	private TableColumn<String, ProductEntity> c2;
	@FXML
	private TableColumn<String, ProductEntity> c3;
	@FXML
	private TableColumn<String, ProductEntity> c4;
	@FXML
	private TableColumn<String, ProductEntity> c5;
	@FXML
	private Button addProductBtn;
	@FXML
	private Button ediProductBtn;
	@FXML
	private Button deleteProductBtn;
	@FXML
	private TextField searchField;//
	
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
	
	ObservableList<ProductEntity> productsObservableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		for (int i = 1; i <= 100; i++) {
			productsObservableList.add(new ProductEntity(i, "Product "+i, "Product Desc "+i,(i+1.00), (i*10.5)));
		}
		
		c1.setCellValueFactory(new PropertyValueFactory<>("id"));
		c2.setCellValueFactory(new PropertyValueFactory<>("productName"));
		c3.setCellValueFactory(new PropertyValueFactory<>("productDesc"));
		c4.setCellValueFactory(new PropertyValueFactory<>("qty"));
		c5.setCellValueFactory(new PropertyValueFactory<>("price"));
		tableView.setItems(productsObservableList);
		
		
		FilteredList<ProductEntity> filteredData = new FilteredList<>(productsObservableList, p -> true);
		searchField.textProperty().addListener((observable, oldValue, newValue)->{
			filteredData.setPredicate(productProp -> {
				if (newValue == null || newValue.isEmpty()) {
	                return true;
	            }
	            String lowerCaseFilter = newValue.toLowerCase();
	            if (productProp.productName.toLowerCase().contains(lowerCaseFilter)) {
	                return true;
	            } else if (productProp.productDesc.toLowerCase().contains(lowerCaseFilter)) {
	                return true;
	            } else if (Double.toString(productProp.price).contains(newValue)) {
	                return true;
	            } 
	            return false; // Does not match.
	        });
		});
		
		SortedList<ProductEntity> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        
        productDialog.setDialogContainer(root);
        productDeleteConfirmDialog.setDialogContainer(root);
        
        addProductBtn.setOnAction((e) -> {
        	productNameField.setText("");
        	productDescField.setText("");   
        	productQtyField.setText(""); 
        	productPriceField.setText("");  
			productDialog.setTransitionType(DialogTransition.CENTER);
			productDialog.show();
		});
        
        ediProductBtn.setOnAction((e) -> {
        	ProductEntity pe = tableView.getSelectionModel().selectedItemProperty().get();
        	
        	productNameField.setText(pe.productName);
        	productDescField.setText(pe.productDesc);   
        	productQtyField.setText(Double.toString(pe.qty)); 
        	productPriceField.setText(Double.toString(pe.price));  
        	productDialog.setTransitionType(DialogTransition.CENTER);
			productDialog.show();
        	//productsObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		});
        
        deleteProductBtn.setOnAction((e) -> {
        	if(!tableView.getSelectionModel().isEmpty()) {
        		productDeleteConfirmDialog.setTransitionType(DialogTransition.CENTER);
            	productDeleteConfirmDialog.show();
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
		productDeleteConfirmDialog.close();
	}
	
	@FXML
	public void deleteConfirmFromDialog(){
		productsObservableList.remove(tableView.getSelectionModel().selectedItemProperty().get());
		productDeleteConfirmDialog.close();
	}
	
}
