
/* Student1Name - ID
 * Student2Name - ID
 */

package opsys_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//imports here
import java.util.Scanner;

public class MatrixMultiplier_MultiThread {
    // Any static members defined here
    public static int MATRIX_SIZE;
    public static int NUM_THREADS;
    public static int[][] matrixB;
    public static int[][] result;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the file path
        System.out.print("Enter the matrix input file path: ");
        String filePath = scanner.nextLine();

        // Get the matrix size
        System.out.print("Enter the matrix size (e.g., 1000 for 1000x1000): ");
        MATRIX_SIZE = scanner.nextInt();

        // Get the number of threads
        System.out.print("Enter the number of threads: ");
        NUM_THREADS = scanner.nextInt();

        long startTime = System.currentTimeMillis();

        // Implement multi-threaded matrix multiplication logic here
        try {
            loadMatrixB(filePath);
        } catch (IOException e) {
            System.err.println("Error loading matrix B: " + e.getMessage());
            return;
        }

        result = new int[MATRIX_SIZE][MATRIX_SIZE];

        int rowsPerThread = MATRIX_SIZE / NUM_THREADS;
        int remainingRows = MATRIX_SIZE % NUM_THREADS;

        Thread[] threads = new Thread[NUM_THREADS];
        int currentStart = 0;

        for (int i = 0; i < NUM_THREADS; i++) {
            int extra = (i < remainingRows) ? 1 : 0;
            int currentEnd = currentStart + rowsPerThread + extra;

            threads[i] = new Thread(new Worker(filePath, currentStart, currentEnd));
            threads[i].start();

            currentStart = currentEnd;
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        // Report duration
        System.out.println("Matrix multiplication completed.");
        System.out.println("Time taken (multithreaded): " + duration + " ms");
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
        line = br.readLine();  // başlık boşsa da atla
    }

    matrixB = new int[MATRIX_SIZE][MATRIX_SIZE];
    for (int i = 0; i < MATRIX_SIZE; i++) {
        // Satır oku ve boş satırsa bir sonrakine geç
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) {
            line = br.readLine();
        }

        if (line == null) {
            throw new IOException("Unexpected end of file while reading Matrix B at row " + i);
        }

        String[] tokens = line.trim().split("\\s+");
        if (tokens.length != MATRIX_SIZE) {
            throw new IOException("Incorrect number of elements in Matrix B row " + i + ": expected " + MATRIX_SIZE + ", got " + tokens.length);
        }

        for (int j = 0; j < MATRIX_SIZE; j++) {
            matrixB[i][j] = Integer.parseInt(tokens[j]);
        }
    }

    br.close();
}


    // Inner class is required
    static class Worker implements Runnable {
        // Fill with necessary logic for each thread
        private String filePath;
        private int startRow;
        private int endRow;

        @Override
        public void run() {
            try {
                int[][] partialA = readMatrixARows(filePath, startRow, endRow);
                for (int i = 0; i < partialA.length; i++) {
                    for (int j = 0; j < MATRIX_SIZE; j++) {
                        int sum = 0;
                        for (int k = 0; k < MATRIX_SIZE; k++) {
                            sum += partialA[i][k] * matrixB[k][j];
                        }
                        result[startRow + i][j] = sum;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Worker(String filePath, int startRow, int endRow) {
            this.filePath = filePath;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        public int[][] readMatrixARows(String filePath, int start, int end) throws IOException {
            // Read rows of matrix A here
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            // İlk satır "Matrix A ..." başlığı, atla
            br.readLine();

            // Start satırına kadar atla
            for (int i = 0; i < start; i++) {
                br.readLine();
            }

            int[][] rows = new int[end - start][MATRIX_SIZE];
            for (int i = 0; i < end - start; i++) {
                String[] tokens = br.readLine().trim().split("\\s+");

                for (int j = 0; j < MATRIX_SIZE; j++) {
                    rows[i][j] = Integer.parseInt(tokens[j]);
                }
            }

            br.close();
            return rows;
        }
    }
}
