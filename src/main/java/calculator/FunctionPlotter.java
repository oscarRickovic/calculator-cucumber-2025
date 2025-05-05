package calculator;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.Calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A utility class to plot mathematical functions using JavaFX charts.
 * Optimized to prevent UI freezing and excessive resource consumption.
 */
public class FunctionPlotter {
    
    private Stage stage;
    private LineChart<Number, Number> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private FunctionEvaluator functionEvaluator;
    private String functionStr;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private ExecutorService executor;
    private AtomicBoolean isPlotting = new AtomicBoolean(false);
    
    // Plot range - with reasonable defaults
    private double minX = -10;
    private double maxX = 10;
    private double minY = -10; 
    private double maxY = 10;
    private double step = 0.1;
    
    // Maximum number of points to plot
    private static final int MAX_POINTS = 10000;
    
    // Maximum time for a single point evaluation (ms)
    private static final long MAX_EVAL_TIME = 50;
    
    /**
     * Creates a new function plotter for the given function expression.
     * 
     * @param functionStr The function string to plot (e.g., "x^2", "sin(x)", etc.)
     */
    public FunctionPlotter(String functionStr) {
        this.functionStr = functionStr;
        this.functionEvaluator = new FunctionEvaluator(functionStr);
        this.executor = Executors.newSingleThreadExecutor();
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
        
        // Create progress indicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        
        // Create status label
        statusLabel = new Label("Ready to plot");
    }
    
    /**
     * Plot the function in a background thread
     */
    public void plot() {
        // Don't start plotting if another plot operation is in progress
        if (isPlotting.get()) {
            statusLabel.setText("Plotting already in progress...");
            return;
        }
        
        // Update UI to show we're starting to plot
        isPlotting.set(true);
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(-1); // Indeterminate progress
        statusLabel.setText("Calculating plot points...");
        
        // Calculate how many points to plot
        int numPoints = (int)Math.min(MAX_POINTS, (maxX - minX) / step + 1);
        double adjustedStep = (maxX - minX) / (numPoints - 1);
        
        Task<XYChart.Series<Number, Number>> task = new Task<>() {
            @Override
            protected XYChart.Series<Number, Number> call() throws Exception {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(functionStr);
                
                List<XYChart.Data<Number, Number>> points = new ArrayList<>();
                int completedPoints = 0;
                
                // Calculate points for the function
                for (double x = minX; x <= maxX && !isCancelled(); x += adjustedStep) {
                    try {
                        // Timeout mechanism for each function evaluation
                        long startTime = System.currentTimeMillis();
                        double y = evaluateFunction(x);
                        long endTime = System.currentTimeMillis();
                        
                        // Skip point if evaluation took too long
                        if (endTime - startTime > MAX_EVAL_TIME) {
                            continue;
                        }
                        
                        // Only add the point if it's within our y range
                        if (y >= minY && y <= maxY && !Double.isNaN(y) && !Double.isInfinite(y)) {
                            points.add(new XYChart.Data<>(x, y));
                        }
                        
                        // Update progress
                        completedPoints++;
                        updateProgress(completedPoints, numPoints);
                    } catch (Exception e) {
                        // Skip points where function can't be evaluated
                    }
                }
                
                return series;
            }
        };
        
        task.setOnSucceeded(event -> {
            XYChart.Series<Number, Number> series = task.getValue();
            
            // Update UI on JavaFX thread
            Platform.runLater(() -> {
                lineChart.getData().clear();
                lineChart.getData().add(series);
                progressIndicator.setVisible(false);
                statusLabel.setText("Plot complete");
                isPlotting.set(false);
            });
        });
        
        task.setOnFailed(event -> {
            // Update UI on JavaFX thread to show error
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                statusLabel.setText("Error plotting function: " + task.getException().getMessage());
                isPlotting.set(false);
            });
        });
        
        task.setOnCancelled(event -> {
            // Update UI on JavaFX thread to show cancellation
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                statusLabel.setText("Plotting cancelled");
                isPlotting.set(false);
            });
        });
        
        // Bind progress indicator to task progress
        progressIndicator.progressProperty().bind(task.progressProperty());
        
        // Execute the task
        executor.submit(task);
    }
    
    /**
     * Evaluate the function at a given x value with a timeout mechanism
     * 
     * @param x The x value
     * @return The y value
     * @throws Exception If evaluation fails or times out
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
            
            // Create a container for the progress indicator and status label
            HBox statusBar = new HBox(10, progressIndicator, statusLabel);
            statusBar.setPadding(new Insets(5));
            
            BorderPane root = new BorderPane();
            root.setCenter(lineChart);
            root.setBottom(new VBox(statusBar, controls));
            root.setPadding(new Insets(10));
            
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            
            // Clean up resources when window is closed
            stage.setOnCloseRequest(e -> {
                if (executor != null) {
                    executor.shutdownNow();
                }
            });
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
                
                // Validate inputs
                if (minX >= maxX || minY >= maxY || step <= 0) {
                    statusLabel.setText("Invalid range values");
                    return;
                }
                
                // Update the axes
                xAxis.setLowerBound(minX);
                xAxis.setUpperBound(maxX);
                yAxis.setLowerBound(minY);
                yAxis.setUpperBound(maxY);
                
                // Re-plot
                plot();
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number format: " + ex.getMessage());
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
     * Close the plot window and clean up resources
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}