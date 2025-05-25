/* Cagri CIBUK - 21COMP1066
 * Student2Name - ID
 */
package opsys_project;

//imports here
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MatrixMultiplier_SingleThread {
  // Any static members defined here
  public static int MATRIX_SIZE;
  public static int[][] matrixA;
  public static int[][] matrixB;
  public static int[][] result;

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    // Get the file path
    System.out.print("Enter the matrix input file path: ");
    String filePath = scanner.nextLine();

    // Get the matrix size
    System.out.print("Enter the matrix size (e.g., 1000 for 1000x1000): ");
    MATRIX_SIZE = scanner.nextInt();

    // Start timer for multiplication
    long startTime = System.currentTimeMillis();

    loadMatrixA(filePath);
    loadMatrixB(filePath);

    // Allocate result matrix
    result = new int[MATRIX_SIZE][MATRIX_SIZE];

    // Perform multiplication
    for (int i = 0; i < MATRIX_SIZE; i++) {
      for (int j = 0; j < MATRIX_SIZE; j++) {
        for (int k = 0; k < MATRIX_SIZE; k++) {
          result[i][j] += matrixA[i][k] * matrixB[k][j];
        }
      }
    }

    // End timer and calculate duration
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;

    // Report duration
    System.out.println("Matrix multiplication completed (single-threaded).");
    System.out.println("Time taken (single-threaded): " + duration + " ms");
  }

  public static void loadMatrixA(String filePath) throws IOException {
    // Fill with necessary logic here
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    // İlk satırı atla (ör: "Matrix A 1000x1000:")
    br.readLine();

    matrixA = new int[MATRIX_SIZE][MATRIX_SIZE];
    for (int i = 0; i < MATRIX_SIZE; i++) {
      String[] tokens = br.readLine().trim().split("\\s+");

      for (int j = 0; j < MATRIX_SIZE; j++) {
        matrixA[i][j] = Integer.parseInt(tokens[j]);
      }
    }
    br.close();
  }

  public static void loadMatrixB(String filePath) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filePath));

    // Matrix A başlığını atla
    br.readLine();

    // Matrix A içeriğini atla
    for (int i = 0; i < MATRIX_SIZE; i++) {
      br.readLine();
    }

    // Matrix B başlığı varsa atla
    String line = br.readLine();
    while (line != null && line.trim().isEmpty()) {
      line = br.readLine(); // boş veya başlıksız satır
    }

    matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
    for (int i = 0; i < MATRIX_SIZE; i++) {
      line = br.readLine();
      while (line != null && line.trim().isEmpty()) {
        line = br.readLine(); // boş satırları atla
      }

      if (line == null) {
        throw new IOException("Matrix B verisi eksik. Satır: " + i);
      }

      String[] tokens = line.trim().split("\\s+");
      if (tokens.length != MATRIX_SIZE) {
        throw new IOException(
            "Matrix B satır uzunluğu hatalı. Beklenen: " + MATRIX_SIZE + ", Gerçek: " + tokens.length);
      }

      for (int j = 0; j < MATRIX_SIZE; j++) {
        matrixB[i][j] = Integer.parseInt(tokens[j]);
      }
    }

    br.close();
  }

}