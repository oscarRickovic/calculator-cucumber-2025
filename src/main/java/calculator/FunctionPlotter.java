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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
 * Includes debugging capabilities to diagnose plot issues.
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
    private TextArea debugLog;
    private Button clearLogButton;
    private ExecutorService executor;
    private AtomicBoolean isPlotting = new AtomicBoolean(false);
    
    // Plot range - with reasonable defaults
    private double minX = -10;
    private double maxX = 10;
    private double minY = -10; 
    private double maxY = 10;
    private double step = 0.1;
    
    // Maximum number of points to plot
    private static final int MAX_POINTS = 5000;
    
    // Maximum time for a single point evaluation (ms)
    private static final long MAX_EVAL_TIME = 50;
    
    // Debug settings
    private boolean debugMode = true;
    
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
     * Add a debug message to the log
     */
    private void logDebug(String message) {
        if (debugMode && debugLog != null) {
            Platform.runLater(() -> {
                debugLog.appendText(message + "\n");
                // Auto-scroll to bottom
                debugLog.setScrollTop(Double.MAX_VALUE);
            });
        }
        // Also print to console for additional debugging
        System.out.println("[DEBUG] " + message);
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
        
        // Create debug log
        debugLog = new TextArea();
        debugLog.setEditable(false);
        debugLog.setPrefHeight(100);
        debugLog.setWrapText(true);
        
        // Create clear log button
        clearLogButton = new Button("Clear Log");
        clearLogButton.setOnAction(e -> debugLog.clear());
        
        logDebug("FunctionPlotter initialized for expression: " + functionStr);
    }
    
    /**
     * Plot the function in a background thread
     */
    public void plot() {
        // Don't start plotting if another plot operation is in progress
        if (isPlotting.get()) {
            statusLabel.setText("Plotting already in progress...");
            logDebug("Plot request ignored - plotting already in progress");
            return;
        }
        
        // Update UI to show we're starting to plot
        isPlotting.set(true);
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(-1); // Indeterminate progress
        statusLabel.setText("Calculating plot points...");
        logDebug("Starting plot calculation with range: X[" + minX + ", " + maxX + "], Y[" + minY + ", " + maxY + "], step=" + step);
        
        // Calculate how many points to plot
        int numPoints = (int)Math.min(MAX_POINTS, (maxX - minX) / step + 1);
        double adjustedStep = (maxX - minX) / (numPoints - 1);
        logDebug("Using " + numPoints + " points with adjusted step size of " + adjustedStep);
        
        Task<XYChart.Series<Number, Number>> task = new Task<>() {
            @Override
            protected XYChart.Series<Number, Number> call() throws Exception {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(functionStr);
                
                List<XYChart.Data<Number, Number>> points = new ArrayList<>();
                int completedPoints = 0;
                int successfulPoints = 0;
                int failedPoints = 0;
                int skippedPoints = 0;
                
                try {
                    // Calculate points for the function
                    for (double x = minX; x <= maxX && !isCancelled(); x += adjustedStep) {
                        try {
                            // Timeout mechanism for each function evaluation
                            long startTime = System.currentTimeMillis();
                            
                            // Try to evaluate the function at this x
                            double y = evaluateFunction(x);
                            
                            long endTime = System.currentTimeMillis();
                            long evalTime = endTime - startTime;
                            
                            // Skip point if evaluation took too long
                            if (evalTime > MAX_EVAL_TIME) {
                                logDebug("Point evaluation at x=" + x + " took too long (" + evalTime + "ms) - skipping");
                                skippedPoints++;
                                continue;
                            }
                            
                            // Only add the point if it's within our y range
                            if (y >= minY && y <= maxY && !Double.isNaN(y) && !Double.isInfinite(y)) {
                                points.add(new XYChart.Data<>(x, y));
                                successfulPoints++;
                                
                                // Log every 100th point to avoid excessive logging
                                if (successfulPoints % 100 == 0) {
                                    logDebug("Added point: (" + x + ", " + y + ")");
                                }
                            } else {
                                // Log points that are out of range
                                if (Double.isNaN(y)) {
                                    logDebug("Point at x=" + x + " produced NaN - skipping");
                                } else if (Double.isInfinite(y)) {
                                    logDebug("Point at x=" + x + " produced Infinity - skipping");
                                } else {
                                    logDebug("Point at x=" + x + " with y=" + y + " is outside y-range - skipping");
                                }
                                skippedPoints++;
                            }
                        } catch (Exception e) {
                            // Log function evaluation errors
                            logDebug("Error evaluating function at x=" + x + ": " + e.getMessage());
                            failedPoints++;
                        }
                        
                        // Update progress
                        completedPoints++;
                        updateProgress(completedPoints, numPoints);
                    }
                    
                    // Add all collected points to the series
                    for (XYChart.Data<Number, Number> point : points) {
                        series.getData().add(point);
                    }
                    
                    // Log overall results
                    logDebug("Plot calculation complete: " + successfulPoints + " points plotted, " + 
                             failedPoints + " failures, " + skippedPoints + " skipped");
                    
                    return series;
                    
                } catch (Exception e) {
                    logDebug("Critical error during plotting: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            }
        };
        
        task.setOnSucceeded(event -> {
            XYChart.Series<Number, Number> series = task.getValue();
            
            // Update UI on JavaFX thread
            Platform.runLater(() -> {
                try {
                    lineChart.getData().clear();
                    
                    if (series.getData().isEmpty()) {
                        logDebug("WARNING: No valid points to plot!");
                        statusLabel.setText("No valid points to plot. Try adjusting range.");
                    } else {
                        lineChart.getData().add(series);
                        logDebug("Added " + series.getData().size() + " points to chart");
                        statusLabel.setText("Plot complete with " + series.getData().size() + " points");
                    }
                } catch (Exception e) {
                    logDebug("Error updating chart: " + e.getMessage());
                    statusLabel.setText("Error updating chart: " + e.getMessage());
                } finally {
                    progressIndicator.setVisible(false);
                    isPlotting.set(false);
                }
            });
        });
        
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            logDebug("Plot task failed: " + (exception != null ? exception.getMessage() : "Unknown error"));
            if (exception != null) {
                exception.printStackTrace();
            }
            
            // Update UI on JavaFX thread to show error
            Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                statusLabel.setText("Error plotting function: " + 
                                   (exception != null ? exception.getMessage() : "Unknown error"));
                isPlotting.set(false);
            });
        });
        
        task.setOnCancelled(event -> {
            logDebug("Plot task was cancelled");
            
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
     * Evaluate the function at a given x value
     * 
     * @param x The x value
     * @return The y value
     * @throws Exception If evaluation fails
     */
    private double evaluateFunction(double x) throws Exception {
        try {
            return functionEvaluator.evaluate(x);
        } catch (Exception e) {
            logDebug("Function evaluation error at x=" + x + ": " + e.getMessage());
            throw e;
        }
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
            
            // Create a container for the debug log and clear button
            VBox debugContainer = new VBox(5);
            HBox debugHeader = new HBox(10, new Label("Debug Log:"), clearLogButton);
            debugContainer.getChildren().addAll(debugHeader, debugLog);
            
            BorderPane root = new BorderPane();
            root.setCenter(lineChart);
            
            // Combine all controls and debug panel in a VBox
            VBox bottomPanel = new VBox(10, statusBar, controls, debugContainer);
            bottomPanel.setPadding(new Insets(10));
            root.setBottom(bottomPanel);
            
            Scene scene = new Scene(root, 800, 750); // Increased height to accommodate debug panel
            stage.setScene(scene);
            
            // Clean up resources when window is closed
            stage.setOnCloseRequest(e -> {
                if (executor != null) {
                    executor.shutdownNow();
                }
            });
            
            logDebug("Plot window created");
        }
        
        // Show the stage
        stage.show();
        stage.toFront();
        logDebug("Plot window displayed");
        
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
                double newMinX = Double.parseDouble(minXField.getText());
                double newMaxX = Double.parseDouble(maxXField.getText());
                double newMinY = Double.parseDouble(minYField.getText());
                double newMaxY = Double.parseDouble(maxYField.getText());
                double newStep = Double.parseDouble(stepField.getText());
                
                // Validate inputs
                if (newMinX >= newMaxX) {
                    statusLabel.setText("Error: Min X must be less than Max X");
                    logDebug("Invalid range: minX >= maxX");
                    return;
                }
                
                if (newMinY >= newMaxY) {
                    statusLabel.setText("Error: Min Y must be less than Max Y");
                    logDebug("Invalid range: minY >= maxY");
                    return;
                }
                
                if (newStep <= 0) {
                    statusLabel.setText("Error: Step size must be positive");
                    logDebug("Invalid step: step <= 0");
                    return;
                }
                
                // Calculate number of points
                int expectedPoints = (int)((newMaxX - newMinX) / newStep) + 1;
                if (expectedPoints > MAX_POINTS * 2) {
                    statusLabel.setText("Warning: Too many points (" + expectedPoints + "). Increasing step size.");
                    newStep = (newMaxX - newMinX) / MAX_POINTS;
                    stepField.setText(String.valueOf(newStep));
                    logDebug("Step size adjusted to " + newStep + " to limit points");
                }
                
                // Update the values
                minX = newMinX;
                maxX = newMaxX;
                minY = newMinY;
                maxY = newMaxY;
                step = newStep;
                
                // Update the axes
                xAxis.setLowerBound(minX);
                xAxis.setUpperBound(maxX);
                yAxis.setLowerBound(minY);
                yAxis.setUpperBound(maxY);
                
                logDebug("Plot parameters updated: X[" + minX + ", " + maxX + "], Y[" + minY + ", " + maxY + "], step=" + step);
                
                // Re-plot
                plot();
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid number format: " + ex.getMessage());
                logDebug("Number format error: " + ex.getMessage());
            }
        });
        
        // Debug controls
        Button testPointButton = new Button("Test Point");
        testPointButton.setOnAction(e -> {
            try {
                double testX = Double.parseDouble(minXField.getText());
                logDebug("Testing function at x=" + testX);
                try {
                    double y = evaluateFunction(testX);
                    logDebug("Result at x=" + testX + ": y=" + y);
                    statusLabel.setText("f(" + testX + ") = " + y);
                } catch (Exception ex) {
                    logDebug("Error evaluating test point: " + ex.getMessage());
                    statusLabel.setText("Error evaluating function at x=" + testX);
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid X value for test");
            }
        });
        
        // Layout the controls
        HBox xRange = new HBox(10, xRangeLabel, new Label("Min:"), minXField, new Label("Max:"), maxXField);
        HBox yRange = new HBox(10, yRangeLabel, new Label("Min:"), minYField, new Label("Max:"), maxYField);
        HBox stepAndTest = new HBox(10, stepLabel, stepField, updateButton, testPointButton);
        
        VBox controls = new VBox(10, xRange, yRange, stepAndTest);
        controls.setPadding(new Insets(10));
        
        return controls;
    }
    
    /**
     * Close the plot window and clean up resources
     */
    public void close() {
        logDebug("Closing plotter and cleaning up resources");
        if (stage != null) {
            stage.close();
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }
    
    /**
     * Force garbage collection for testing
     */
    public void forceGC() {
        logDebug("Forcing garbage collection");
        System.gc();
    }
}