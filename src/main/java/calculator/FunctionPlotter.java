package calculator;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A utility class to plot mathematical functions using JavaFX charts.
 */
public class FunctionPlotter {
    
    private Stage stage;
    private LineChart<Number, Number> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private FunctionEvaluator functionEvaluator;
    private String functionStr;
    
    // Plot range
    private double minX = -10;
    private double maxX = 10;
    private double minY = -10; 
    private double maxY = 10;
    private double step = 0.1;
    
    /**
     * Creates a new function plotter for the given function expression.
     * 
     * @param functionStr The function string to plot (e.g., "x^2", "sin(x)", etc.)
     */
    public FunctionPlotter(String functionStr) {
        this.functionStr = functionStr;
        this.functionEvaluator = new FunctionEvaluator(functionStr);
        setupPlotter();
    }
    
    /**
     * Set up the plotter with axes and chart
     */
    private void setupPlotter() {
        // Create the x and y axes
        xAxis = new NumberAxis("X", minX, maxX, 1);
        yAxis = new NumberAxis("Y", minY, maxY, 1);
        
        // Create the chart with the axes
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Plot of " + functionStr);
        lineChart.setCreateSymbols(false); // No markers on the line
        lineChart.setLegendVisible(true);
    }
    
    /**
     * Plot the function
     */
    public void plot() {
        // Create a data series for the function
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(functionStr);
        
        // Calculate points for the function
        for (double x = minX; x <= maxX; x += step) {
            try {
                double y = evaluateFunction(x);
                // Only add the point if it's within our y range
                if (y >= minY && y <= maxY && !Double.isNaN(y) && !Double.isInfinite(y)) {
                    series.getData().add(new XYChart.Data<>(x, y));
                }
            } catch (Exception e) {
                // Skip points where function can't be evaluated
                //System.out.println("Skipping point at x=" + x + ": " + e.getMessage());
            }
        }
        
        // Add the series to the chart
        lineChart.getData().clear();
        lineChart.getData().add(series);
        
        // Show the chart in a new window
        showPlot();
    }
    
    /**
     * Evaluate the function at a given x value
     * 
     * @param x The x value
     * @return The y value
     */
    private double evaluateFunction(double x) throws Exception {
        return functionEvaluator.evaluate(x);
    }
    
    /**
     * Show the plot in a new window
     */
    public void showPlot() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Function Plot: " + functionStr);
            
            // Create controls for adjusting the plot range
            VBox controls = createControls();
            
            BorderPane root = new BorderPane();
            root.setCenter(lineChart);
            root.setBottom(controls);
            root.setPadding(new Insets(10));
            
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        }
        
        // Show the stage
        stage.show();
        stage.toFront();
        
        // Plot the function
        plot();
    }
    
    /**
     * Create controls for adjusting the plot
     */
    private VBox createControls() {
        // X range controls
        Label xRangeLabel = new Label("X Range:");
        TextField minXField = new TextField(String.valueOf(minX));
        minXField.setPrefWidth(60);
        TextField maxXField = new TextField(String.valueOf(maxX));
        maxXField.setPrefWidth(60);
        
        // Y range controls
        Label yRangeLabel = new Label("Y Range:");
        TextField minYField = new TextField(String.valueOf(minY));
        minYField.setPrefWidth(60);
        TextField maxYField = new TextField(String.valueOf(maxY));
        maxYField.setPrefWidth(60);
        
        // Step size control
        Label stepLabel = new Label("Step Size:");
        TextField stepField = new TextField(String.valueOf(step));
        stepField.setPrefWidth(60);
        
        // Update button
        Button updateButton = new Button("Update Plot");
        updateButton.setOnAction(e -> {
            try {
                // Parse the new range values
                minX = Double.parseDouble(minXField.getText());
                maxX = Double.parseDouble(maxXField.getText());
                minY = Double.parseDouble(minYField.getText());
                maxY = Double.parseDouble(maxYField.getText());
                step = Double.parseDouble(stepField.getText());
                
                // Update the axes
                xAxis.setLowerBound(minX);
                xAxis.setUpperBound(maxX);
                yAxis.setLowerBound(minY);
                yAxis.setUpperBound(maxY);
                
                // Re-plot
                plot();
            } catch (NumberFormatException ex) {
                System.err.println("Invalid number format: " + ex.getMessage());
            }
        });
        
        // Layout the controls
        HBox xRange = new HBox(10, xRangeLabel, new Label("Min:"), minXField, new Label("Max:"), maxXField);
        HBox yRange = new HBox(10, yRangeLabel, new Label("Min:"), minYField, new Label("Max:"), maxYField);
        HBox stepSize = new HBox(10, stepLabel, stepField, updateButton);
        
        VBox controls = new VBox(10, xRange, yRange, stepSize);
        controls.setPadding(new Insets(10));
        
        return controls;
    }
    
    /**
     * Close the plot window
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}