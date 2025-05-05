package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a mathematical matrix and provides operations for matrix manipulation.
 */
public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;

    /**
     * Constructs a new matrix with the given data.
     *
     * @param data The 2D array representing the matrix elements
     */
    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = (rows > 0) ? data[0].length : 0;
        
        // Create a deep copy of the input data
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }
    
    /**
     * Creates a new matrix with the specified dimensions, filled with zeros.
     *
     * @param rows Number of rows
     * @param cols Number of columns
     * @return A new zero matrix
     */
    public static Matrix zeros(int rows, int cols) {
        double[][] data = new double[rows][cols];
        return new Matrix(data);
    }
    
    /**
     * Creates a new identity matrix with the specified size.
     *
     * @param size The size (rows and columns) of the identity matrix
     * @return A new identity matrix
     */
    public static Matrix identity(int size) {
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            data[i][i] = 1.0;
        }
        return new Matrix(data);
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
     * Gets the value at the specified position.
     *
     * @param row Row index (0-based)
     * @param col Column index (0-based)
     * @return The value at the specified position
     */
    public double get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds: [" + row + ", " + col + "]");
        }
        return data[row][col];
    }
    
    /**
     * Sets the value at the specified position.
     *
     * @param row Row index (0-based)
     * @param col Column index (0-based)
     * @param value The value to set
     */
    public void set(int row, int col, double value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds: [" + row + ", " + col + "]");
        }
        data[row][col] = value;
    }
    
    /**
     * Returns a copy of the internal data array.
     *
     * @return A copy of the matrix data
     */
    public double[][] getData() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }
    
    /**
     * Adds this matrix to another matrix.
     *
     * @param other The matrix to add
     * @return A new matrix representing the sum
     * @throws IllegalArgumentException If the matrices have different dimensions
     */
    public Matrix add(Matrix other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException(
                "Matrix dimensions mismatch: [" + rows + "x" + cols + "] + [" + 
                other.rows + "x" + other.cols + "]");
        }
        
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j] + other.data[i][j];
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Subtracts another matrix from this matrix.
     *
     * @param other The matrix to subtract
     * @return A new matrix representing the difference
     * @throws IllegalArgumentException If the matrices have different dimensions
     */
    public Matrix subtract(Matrix other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException(
                "Matrix dimensions mismatch: [" + rows + "x" + cols + "] - [" + 
                other.rows + "x" + other.cols + "]");
        }
        
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j] - other.data[i][j];
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Multiplies this matrix by another matrix.
     *
     * @param other The matrix to multiply by
     * @return A new matrix representing the product
     * @throws IllegalArgumentException If the matrices have incompatible dimensions
     */
    public Matrix multiply(Matrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException(
                "Matrix dimensions mismatch for multiplication: [" + rows + "x" + cols + "] * [" + 
                other.rows + "x" + other.cols + "]");
        }
        
        double[][] result = new double[rows][other.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < cols; k++) {
                    sum += data[i][k] * other.data[k][j];
                }
                result[i][j] = sum;
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Multiplies this matrix by a scalar value.
     *
     * @param scalar The scalar value to multiply by
     * @return A new matrix with each element multiplied by the scalar
     */
    public Matrix multiply(double scalar) {
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j] * scalar;
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Computes the transpose of this matrix.
     *
     * @return A new matrix that is the transpose of this matrix
     */
    public Matrix transpose() {
        double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = data[i][j];
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Calculates the determinant of this matrix.
     *
     * @return The determinant of this matrix
     * @throws IllegalArgumentException If the matrix is not square
     */
    public double determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Determinant can only be calculated for square matrices");
        }
        
        if (rows == 1) {
            return data[0][0];
        }
        
        if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        
        // For larger matrices, use cofactor expansion
        double det = 0.0;
        for (int j = 0; j < cols; j++) {
            det += data[0][j] * cofactor(0, j);
        }
        
        return det;
    }
    
    /**
     * Calculates the cofactor of the element at the specified position.
     *
     * @param row Row index (0-based)
     * @param col Column index (0-based)
     * @return The cofactor of the element
     */
    private double cofactor(int row, int col) {
        return Math.pow(-1, row + col) * minor(row, col).determinant();
    }
    
    /**
     * Creates a submatrix by removing the specified row and column.
     *
     * @param excludeRow Row to exclude
     * @param excludeCol Column to exclude
     * @return A new matrix with the row and column removed
     */
    private Matrix minor(int excludeRow, int excludeCol) {
        if (rows <= 1 || cols <= 1) {
            throw new IllegalArgumentException("Cannot create a minor for a 1x1 or smaller matrix");
        }
        
        double[][] result = new double[rows - 1][cols - 1];
        int r = 0;
        for (int i = 0; i < rows; i++) {
            if (i == excludeRow) continue;
            
            int c = 0;
            for (int j = 0; j < cols; j++) {
                if (j == excludeCol) continue;
                
                result[r][c] = data[i][j];
                c++;
            }
            r++;
        }
        
        return new Matrix(result);
    }
    
    /**
     * Calculates the inverse of this matrix.
     *
     * @return A new matrix that is the inverse of this matrix
     * @throws IllegalArgumentException If the matrix is not invertible
     */
    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Only square matrices can be inverted");
        }
        
        double det = determinant();
        if (Math.abs(det) < 1e-10) {
            throw new IllegalArgumentException("Matrix is singular (determinant is zero)");
        }
        
        // For 1x1 matrix
        if (rows == 1) {
            return new Matrix(new double[][]{{1.0 / data[0][0]}});
        }
        
        // For larger matrices, calculate the adjugate and divide by determinant
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = cofactor(i, j) / det; // Note the swapped indices for transpose
            }
        }
        
        return new Matrix(result);
    }
    
    /**
     * Returns a formatted string representation of this matrix.
     *
     * @return A string representation of the matrix
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("% .5f", data[i][j]).trim());
                if (j < cols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
    
    /**
     * Parses a string representation of a matrix.
     * Format: [[a, b, c], [d, e, f], [g, h, i]]
     *
     * @param matrixStr The matrix string to parse
     * @return A new Matrix object
     * @throws IllegalArgumentException If the input string cannot be parsed
     */
    public static Matrix parse(String matrixStr) {
        // Remove spaces and validate overall format
        String cleaned = matrixStr.trim().replaceAll("\\s+", "");
        if (!cleaned.startsWith("[[") || !cleaned.endsWith("]]")) {
            throw new IllegalArgumentException("Invalid matrix format. Expected format: [[a,b], [c,d]]");
        }
        
        // Remove outer brackets and split by row
        String content = cleaned.substring(1, cleaned.length() - 1);
        String[] rowStrings = content.split("\\],\\[");
        
        // Remove remaining brackets
        rowStrings[0] = rowStrings[0].replaceFirst("\\[", "");
        rowStrings[rowStrings.length - 1] = rowStrings[rowStrings.length - 1].replaceFirst("\\]$", "");
        
        // Parse rows and columns
        List<List<Double>> matrix = new ArrayList<>();
        int cols = -1;
        
        for (String rowStr : rowStrings) {
            String[] elements = rowStr.split(",");
            List<Double> row = new ArrayList<>();
            
            for (String element : elements) {
                if (!element.isEmpty()) {
                    row.add(Double.parseDouble(element));
                }
            }
            
            if (cols == -1) {
                cols = row.size();
            } else if (cols != row.size()) {
                throw new IllegalArgumentException("All rows must have the same number of columns");
            }
            
            matrix.add(row);
        }
        
        // Convert to array
        double[][] data = new double[matrix.size()][cols];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = matrix.get(i).get(j);
            }
        }
        
        return new Matrix(data);
    }
    
    /**
     * Determines whether this matrix is equal to another object.
     *
     * @param obj The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Matrix other = (Matrix) obj;
        if (rows != other.rows || cols != other.cols) return false;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(data[i][j] - other.data[i][j]) > 1e-10) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Returns a hash code for this matrix.
     *
     * @return A hash code value for this matrix
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(data);
        return result;
    }
}