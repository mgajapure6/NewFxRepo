package app.main;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import app.product.ProductEntity;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CustomerHomeController implements Initializable {
	
	@FXML
	private Tab productsTab;
	
//	@FXML
//	private HBox productCard;
	
	@FXML
	private FlowPane productFlowPane; 
	
	ObservableList<ProductEntity> productsObservableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		for (int i = 2; i <= 5; i++) {
			
			//productsObservableList.add(new ProductEntity(i, "Product "+i, "Product Desc "+i,(i+1.00), (i*10.5)));
			//productCard
			try {
//				Button b = new Button("Button "+i);
//				HBox hb = new HBox();
//				hb.setFillHeight(false);
//				//hb.getChildren().addAll(productCard.getChildren());
//				Label l = new Label();
//				l.getStyleClass().add("h3");
//				l.setText("Product "+i);
//				hb.getChildren().addAll(l);
//				System.out.println("i:"+i);
				productFlowPane.getChildren().add(FXMLLoader.load(getClass().getResource("productCard.fxml")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private HBox createProductCard(String title, String desc, String price) {
		HBox hb = new HBox();
		hb.setFillHeight(false);
		
		VBox vb = new VBox();
		vb.prefHeight(300.00);
		vb.prefWidth(200.00);
		vb.setStyle("-fx-border-color: -separator-color; -fx-background-color: -background-color;");
		vb.getStyleClass().add("raised");
		
		
		ImageView iv = new ImageView(new Image("@../../com/gn/module/media/foreground.jpg"));
		iv.setFitHeight(272.00);
		iv.setFitWidth(200.00);
		iv.setPickOnBounds(true);
		iv.setPreserveRatio(true);
		vb.getChildren().add(iv);
		
		Label l = new Label(title);
		l.getStyleClass().add("h3");
		l.setPadding(new Insets(10.00, 0.0, 0.0, 10.00));
		vb.getChildren().add(l);
		
		VBox vb1 = new VBox();
		
		Text t = new Text(desc);
		t.setStyle("-fx-fill: -text-color");
		TextFlow tf = new TextFlow();
		tf.getChildren().add(t);
		tf.setPadding(new Insets(10.00, 10.0, 10.0, 10.00));
		vb1.getChildren().add(tf);
		vb.getChildren().add(vb1);
		
		HBox hb1 = new HBox();
		
		
		HBox hb2 = new HBox();
		hb2.setAlignment(Pos.CENTER_RIGHT);
		Label l1 = new Label("$ "+price);
		l1.getStyleClass().add("h3");
		hb2.getChildren().add(l1);
		hb2.setPadding(new Insets(0.00, 0.0, 0.0, 5.00));
		hb1.getChildren().add(hb2);
		
		HBox hb3 = new HBox();
		hb3.setAlignment(Pos.CENTER_RIGHT);
		
		JFXButton btn = new JFXButton();
		btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		btn.prefWidth(50.00);
		btn.setRipplerFill(Paint.valueOf("BLACK"));
		btn.getStyleClass().add("btn-transparent");
		btn.getStyleClass().add("round");
		btn.getStyleClass().add("btn-large");
		
		MaterialDesignIconView mdi = new MaterialDesignIconView();
		mdi.setGlyphName("CART_PLUS");
		mdi.setSize("35");
		mdi.getStyleClass().add("btn-large");
		
		btn.setGraphic(mdi);
		
		hb3.getChildren().add(btn);
		hb3.setPadding(new Insets(0.00, 5.0, 5.0, 0.00));
		hb1.getChildren().add(hb3);
		
		vb.getChildren().add(hb1);
		
		vb.setPadding(new Insets(0.00, 5.0, 5.0, 0.00));
		
		hb.getChildren().add(vb);
		return hb;
	}

}
