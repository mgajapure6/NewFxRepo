
package app.dashboard;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class Dashboard implements Initializable {

	@FXML
	private AreaChart<String, Number> areaChart;

	@FXML
	private PieChart pieChart;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("Sun", 20),
				new PieChart.Data("IBM", 12), new PieChart.Data("HP", 25), new PieChart.Data("Dell", 22),
				new PieChart.Data("Apple", 30));
		pieChart.setData(pieChartData);
		pieChart.setClockwise(false);

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Legend 1");
		series.getData().add(new XYChart.Data<>("0", 2D));
		series.getData().add(new XYChart.Data<>("1", 8D));
		series.getData().add(new XYChart.Data<>("2", 5D));
		series.getData().add(new XYChart.Data<>("3", 3D));
		series.getData().add(new XYChart.Data<>("4", 6D));
		series.getData().add(new XYChart.Data<>("5", 8D));
		series.getData().add(new XYChart.Data<>("6", 5D));
		series.getData().add(new XYChart.Data<>("7", 6D));
		series.getData().add(new XYChart.Data<>("8", 5D));

		areaChart.getData().setAll(series);
		areaChart.setCreateSymbols(true);
	}
}
