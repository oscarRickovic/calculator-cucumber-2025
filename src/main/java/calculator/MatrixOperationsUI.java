package calculator;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A user interface for performing matrix operations.
 */
public class MatrixOperationsUI {
    
    private Stage stage;
    private ComboBox<String> operationComboBox;
    private VBox matrixInputsContainer;
    private VBox resultContainer;
    private Button calculateButton;
    private Label statusLabel;
    private ProgressIndicator progressIndicator;
    
    // Matrix inputs
    private MatrixInputPanel matrixAPanel;
    private MatrixInputPanel matrixBPanel;
    
    // Matrix dimensions
    private int rowsA = 3;
    private int colsA = 3;
    private int rowsB = 3;
    private int colsB = 3;
    
    // Constants for UI design
    private static final String LIGHT_BG = "linear-gradient(to bottom right, #ffffff, #f5f5f5)";
    private static final String BUTTON_STYLE = "-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String TEXTFIELD_STYLE = "-fx-background-color: #ffffff; -fx-text-fill: #333333; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 4;";
    
    /**
     * Creates a new MatrixOperationsUI.
     */
    public MatrixOperationsUI() {
        // Initialize UI components
    }
    
    /**
     * Shows the matrix operations interface.
     */
    public void showMatrixOperations() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Matrix Operations");
            
            // Create the root layout
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: " + LIGHT_BG + ";");
            
            // Title and instructions
            Label titleLabel = new Label("Matrix Operations");
            titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
            
            // Operation selector
            Label operationLabel = new Label("Select Operation:");
            operationComboBox = new ComboBox<>();
            operationComboBox.getItems().addAll(
                "Matrix Addition (A + B)",
                "Matrix Subtraction (A - B)",
                "Matrix Multiplication (A × B)",
                "Scalar Multiplication (c × A)",
                "Matrix Transpose (Aᵀ)",
                "Matrix Inverse (A⁻¹)",
                "Matrix Determinant (|A|)"
            );
            operationComboBox.setValue("Matrix Addition (A + B)");
            operationComboBox.setOnAction(e -> updateInputPanels());
            
            HBox operationBox = new HBox(10, operationLabel, operationComboBox);
            operationBox.setAlignment(Pos.CENTER_LEFT);
            
            VBox headerBox = new VBox(10, titleLabel, operationBox);
            headerBox.setPadding(new Insets(0, 0, 10, 0));
            root.setTop(headerBox);
            
            // Matrix inputs container
            matrixInputsContainer = new VBox(20);
            
            // Setup initial matrix input panels
            matrixAPanel = new MatrixInputPanel("Matrix A", rowsA, colsA);
            matrixBPanel = new MatrixInputPanel("Matrix B", rowsB, colsB);
            
            matrixInputsContainer.getChildren().addAll(matrixAPanel, matrixBPanel);
            
            // Calculate button and progress indicator
            calculateButton = new Button("Calculate");
            calculateButton.setStyle(BUTTON_STYLE);
            calculateButton.setOnAction(e -> performCalculation());
            
            progressIndicator = new ProgressIndicator();
            progressIndicator.setVisible(false);
            progressIndicator.setPrefSize(30, 30);
            
            statusLabel = new Label("");
            statusLabel.setTextFill(Color.RED);
            
            HBox controlBox = new HBox(10, calculateButton, progressIndicator, statusLabel);
            controlBox.setAlignment(Pos.CENTER_LEFT);
            controlBox.setPadding(new Insets(10, 0, 10, 0));
            
            // Result container
            resultContainer = new VBox(10);
            resultContainer.setPadding(new Insets(10));
            resultContainer.setStyle("-fx-background-color: white; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 8;");
            
            Label resultLabel = new Label("Result:");
            resultLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            
            resultContainer.getChildren().add(resultLabel);
            
            // Combine all elements in center
            VBox centerBox = new VBox(10, matrixInputsContainer, controlBox, resultContainer);
            root.setCenter(centerBox);
            
            // Create the scene
            Scene scene = new Scene(root, 700, 600);
            stage.setScene(scene);
            stage.setMinWidth(600);
            stage.setMinHeight(500);
            
            // Update panels for initial operation
            updateInputPanels();
        }
        
        // Show the stage
        stage.show();
        stage.toFront();
    }
    
    /**
     * Updates the input panels based on the selected operation.
     */
    private void updateInputPanels() {
        String operation = operationComboBox.getValue();
        
        // Clear result
        clearResult();
        
        // Show or hide panels based on operation
        switch (operation) {
            case "Matrix Addition (A + B)":
            case "Matrix Subtraction (A - B)":
                matrixAPanel.setVisible(true);
                matrixBPanel.setVisible(true);
                matrixInputsContainer.getChildren().clear();
                matrixInputsContainer.getChildren().addAll(matrixAPanel, matrixBPanel);
                // For addition and subtraction, matrices must have the same dimensions
                matrixBPanel.setDimensions(matrixAPanel.getRows(), matrixAPanel.getCols());
                break;
                
            case "Matrix Multiplication (A × B)":
                matrixAPanel.setVisible(true);
                matrixBPanel.setVisible(true);
                matrixInputsContainer.getChildren().clear();
                matrixInputsContainer.getChildren().addAll(matrixAPanel, matrixBPanel);
                // For multiplication, number of columns in A must equal number of rows in B
                matrixBPanel.setDimensions(matrixAPanel.getCols(), matrixBPanel.getCols());
                break;
                
            case "Scalar Multiplication (c × A)":
                // Create scalar input if it doesn't exist
                TextField scalarField = new TextField("1.0");
                scalarField.setMaxWidth(100);
                scalarField.setStyle(TEXTFIELD_STYLE);
                Label scalarLabel = new Label("Scalar value:");
                HBox scalarBox = new HBox(10, scalarLabel, scalarField);
                scalarBox.setAlignment(Pos.CENTER_LEFT);
                
                matrixAPanel.setVisible(true);
                matrixBPanel.setVisible(false);
                matrixInputsContainer.getChildren().clear();
                matrixInputsContainer.getChildren().addAll(scalarBox, matrixAPanel);
                break;
                
            case "Matrix Transpose (Aᵀ)":
            case "Matrix Inverse (A⁻¹)":
            case "Matrix Determinant (|A|)":
                matrixAPanel.setVisible(true);
                matrixBPanel.setVisible(false);
                matrixInputsContainer.getChildren().clear();
                matrixInputsContainer.getChildren().add(matrixAPanel);
                
                // Square matrix is required for inverse and determinant
                if (!operation.equals("Matrix Transpose (Aᵀ)")) {
                    matrixAPanel.setDimensions(matrixAPanel.getRows(), matrixAPanel.getRows());
                }
                break;
        }
    }
    
    /**
     * Performs the selected matrix calculation.
     */
    private void performCalculation() {
        // Disable the calculate button and show progress indicator during calculation
        calculateButton.setDisable(true);
        progressIndicator.setVisible(true);
        
        // Clear previous results and status
        clearResult();
        statusLabel.setText("");
        
        // Get the selected operation
        String operation = operationComboBox.getValue();
        
        // Create and start the calculation thread
        Thread calculationThread = new Thread(() -> {
            try {
                // Get Matrix A
                Matrix matrixA = null;
                
                if (operation.equals("Scalar Multiplication (c × A)")) {
                    // Get the scalar value
                    TextField scalarField = null;
                    HBox scalarBox = (HBox) matrixInputsContainer.getChildren().get(0);
                    for (javafx.scene.Node node : scalarBox.getChildren()) {
                        if (node instanceof TextField) {
                            scalarField = (TextField) node;
                            break;
                        }
                    }
                    
                    double scalar = 1.0;
                    if (scalarField != null) {
                        try {
                            scalar = Double.parseDouble(scalarField.getText());
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid scalar value: " + scalarField.getText());
                        }
                    }
                    
                    matrixA = matrixAPanel.getMatrix();
                    
                    // Perform scalar multiplication
                    Matrix result = matrixA.multiply(scalar);
                    
                    // Show the result
                    updateUI(() -> showResult(result, "Scalar Multiplication Result"));
                } else {
                    // Get Matrix A for all other operations
                    matrixA = matrixAPanel.getMatrix();
                    
                    switch (operation) {
                        case "Matrix Addition (A + B)":
                            Matrix matrixB = matrixBPanel.getMatrix();
                            Matrix sum = matrixA.add(matrixB);
                            updateUI(() -> showResult(sum, "Matrix Addition Result"));
                            break;
                            
                        case "Matrix Subtraction (A - B)":
                            Matrix matrixB2 = matrixBPanel.getMatrix();
                            Matrix difference = matrixA.subtract(matrixB2);
                            updateUI(() -> showResult(difference, "Matrix Subtraction Result"));
                            break;
                            
                        case "Matrix Multiplication (A × B)":
                            Matrix matrixB3 = matrixBPanel.getMatrix();
                            Matrix product = matrixA.multiply(matrixB3);
                            updateUI(() -> showResult(product, "Matrix Multiplication Result"));
                            break;
                            
                        case "Matrix Transpose (Aᵀ)":
                            Matrix transpose = matrixA.transpose();
                            updateUI(() -> showResult(transpose, "Matrix Transpose Result"));
                            break;
                            
                        case "Matrix Inverse (A⁻¹)":
                            Matrix inverse = matrixA.inverse();
                            updateUI(() -> showResult(inverse, "Matrix Inverse Result"));
                            break;
                            
                        case "Matrix Determinant (|A|)":
                            double determinant = matrixA.determinant();
                            updateUI(() -> showDeterminant(determinant));
                            break;
                    }
                }
            } catch (Exception e) {
                // Show error message
                String errorMessage = e.getMessage();
                updateUI(() -> statusLabel.setText("Error: " + errorMessage));
            } finally {
                // Re-enable the calculate button and hide progress indicator
                updateUI(() -> {
                    calculateButton.setDisable(false);
                    progressIndicator.setVisible(false);
                });
            }
        });
        
        // Start the calculation thread
        calculationThread.setDaemon(true);
        calculationThread.start();
    }
    
    /**
     * Clears the result container.
     */
    private void clearResult() {
        // Keep only the first child (the label)
        if (resultContainer.getChildren().size() > 1) {
            resultContainer.getChildren().subList(1, resultContainer.getChildren().size()).clear();
        }
    }
    
    /**
     * Shows a matrix result in the result container.
     * 
     * @param matrix The matrix result to show
     * @param title The title for the result
     */
    private void showResult(Matrix matrix, String title) {
        clearResult();
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        
        GridPane resultGrid = new GridPane();
        resultGrid.setHgap(5);
        resultGrid.setVgap(5);
        resultGrid.setPadding(new Insets(10));
        
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                TextField cell = new TextField(String.format("%.5f", matrix.get(i, j)).trim());
                cell.setEditable(false);
                cell.setPrefWidth(80);
                cell.setStyle(TEXTFIELD_STYLE);
                resultGrid.add(cell, j, i);
            }
        }
        
        resultContainer.getChildren().addAll(titleLabel, resultGrid);
    }
    
    /**
     * Shows a determinant result in the result container.
     * 
     * @param determinant The determinant value to show
     */
    private void showDeterminant(double determinant) {
        clearResult();
        
        Label titleLabel = new Label("Matrix Determinant Result");
        titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        
        Label resultLabel = new Label("Determinant: " + String.format("%.10f", determinant).trim());
        resultLabel.setFont(Font.font("Monospace", 14));
        
        resultContainer.getChildren().addAll(titleLabel, resultLabel);
    }
    
    /**
     * Updates the UI safely from a background thread.
     */
    private void updateUI(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }
    
    /**
     * A panel for inputting a matrix.
     */
    private class MatrixInputPanel extends VBox {
        private GridPane matrixGrid;
        private Label titleLabel;
        private int rows;
        private int cols;
        private TextField[][] cells;
        
        /**
         * Creates a new matrix input panel.
         * 
         * @param title The title for the panel
         * @param rows The number of rows
         * @param cols The number of columns
         */
        public MatrixInputPanel(String title, int rows, int cols) {
            super(10); // Spacing between elements
            setPadding(new Insets(10));
            setStyle("-fx-background-color: white; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 8;");
            
            this.rows = rows;
            this.cols = cols;
            this.cells = new TextField[rows][cols];
            
            // Title
            titleLabel = new Label(title);
            titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
            
            // Dimension controls
            Spinner<Integer> rowSpinner = new Spinner<>(1, 10, rows);
            Spinner<Integer> colSpinner = new Spinner<>(1, 10, cols);
            
            Label dimensionLabel = new Label("Dimensions:");
            Label xLabel = new Label("×");
            
            HBox dimensionBox = new HBox(5, dimensionLabel, rowSpinner, xLabel, colSpinner);
            dimensionBox.setAlignment(Pos.CENTER_LEFT);
            
            // Action buttons
            Button fillZerosButton = new Button("Fill with Zeros");
            Button fillOnesButton = new Button("Fill with Ones");
            Button fillIdentityButton = new Button("Identity Matrix");
            Button clearButton = new Button("Clear");
            
            fillZerosButton.setOnAction(e -> fillWithZeros());
            fillOnesButton.setOnAction(e -> fillWithOnes());
            fillIdentityButton.setOnAction(e -> fillWithIdentity());
            clearButton.setOnAction(e -> clearMatrix());
            
            HBox buttonBox = new HBox(5, fillZerosButton, fillOnesButton, fillIdentityButton, clearButton);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            
            // Matrix grid
            matrixGrid = new GridPane();
            matrixGrid.setHgap(5);
            matrixGrid.setVgap(5);
            matrixGrid.setPadding(new Insets(10));
            
            // Create matrix cells
            createMatrixCells();
            
            // Update dimensions when spinners change
            rowSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                this.rows = newVal;
                updateMatrixCells();
            });
            
            colSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                this.cols = newVal;
                updateMatrixCells();
            });
            
            // Add all components
            getChildren().addAll(titleLabel, dimensionBox, buttonBox, matrixGrid);
        }
        
        /**
         * Creates the matrix input cells.
         */
        private void createMatrixCells() {
            matrixGrid.getChildren().clear();
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    TextField cell = new TextField("0");
                    cell.setPrefWidth(60);
                    cell.setStyle(TEXTFIELD_STYLE);
                    
                    // Store in array
                    cells[i][j] = cell;
                    
                    // Add to grid
                    matrixGrid.add(cell, j, i);
                }
            }
        }
        
        /**
         * Updates the matrix cells after a dimension change.
         */
        private void updateMatrixCells() {
            // Create new array with new dimensions
            TextField[][] newCells = new TextField[rows][cols];
            
            // Clear the grid
            matrixGrid.getChildren().clear();
            
            // Create new cells or reuse existing ones
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    TextField cell;
                    
                    if (i < cells.length && j < cells[0].length) {
                        // Reuse existing cell
                        cell = cells[i][j];
                    } else {
                        // Create new cell
                        cell = new TextField("0");
                        cell.setPrefWidth(60);
                        cell.setStyle(TEXTFIELD_STYLE);
                    }
                    
                    // Store in new array
                    newCells[i][j] = cell;
                    
                    // Add to grid
                    matrixGrid.add(cell, j, i);
                }
            }
            
            // Update the cells array
            cells = newCells;
        }
        
        /**
         * Gets the number of rows in this matrix.
         *
         * @return Number of rows
         */
        public int getRows() {
            return rows;
        }
        
        /**
         * Gets the number of columns in this matrix.
         *
         * @return Number of columns
         */
        public int getCols() {
            return cols;
        }
        
        /**
         * Sets the dimensions of this matrix.
         *
         * @param rows Number of rows
         * @param cols Number of columns
         */
        public void setDimensions(int rows, int cols) {
            if (this.rows == rows && this.cols == cols) {
                return; // No change
            }
            
            this.rows = rows;
            this.cols = cols;
            
            // Update spinners
            for (javafx.scene.Node node : ((HBox) getChildren().get(1)).getChildren()) {
                if (node instanceof Spinner<?>) {
                    @SuppressWarnings("unchecked")
                    Spinner<Integer> spinner = (Spinner<Integer>) node;
                    
                    if (spinner.getValue() == this.rows || spinner.getValue() == this.cols) {
                        if (spinner.getValue() == this.rows) {
                            spinner.getValueFactory().setValue(rows);
                        } else {
                            spinner.getValueFactory().setValue(cols);
                        }
                    }
                }
            }
            
            updateMatrixCells();
        }
        
        /**
         * Gets the matrix from the input cells.
         *
         * @return A Matrix object
         * @throws IllegalArgumentException If any cell contains an invalid value
         */
        public Matrix getMatrix() {
            double[][] data = new double[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String value = cells[i][j].getText().trim();
                    try {
                        data[i][j] = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid value in matrix " + 
                            titleLabel.getText() + " at position [" + (i+1) + ", " + (j+1) + "]: " + value);
                    }
                }
            }
            
            return new Matrix(data);
        }
        
        /**
         * Fills the matrix with zeros.
         */
        private void fillWithZeros() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cells[i][j].setText("0");
                }
            }
        }
        
        /**
         * Fills the matrix with ones.
         */
        private void fillWithOnes() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cells[i][j].setText("1");
                }
            }
        }
        
        /**
         * Fills the matrix as an identity matrix (ones on the diagonal, zeros elsewhere).
         */
        private void fillWithIdentity() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cells[i][j].setText(i == j ? "1" : "0");
                }
            }
        }
        
        /**
         * Clears the matrix (fills with empty strings).
         */
        private void clearMatrix() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    cells[i][j].setText("");
                }
            }
        }
    }
}