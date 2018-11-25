package genetic_assignment;

import java.util.ArrayList;

import org.jblas.DoubleMatrix;

public final class Utilities {
	
	// Formats and prints inputs
	public static void printInputs(String header, DoubleMatrix matrix) {
		System.out.println(header);
		for(int i = 0; i < matrix.getRows(); i++) {
			System.out.print("[");
			for(int j = 0; j < matrix.getColumns(); j++) {
				if(matrix.get(i, j) < 0 ) {
					System.out.printf("%.5f ", matrix.get(i, j));
				}
				else {
					System.out.printf(" %.5f ", matrix.get(i, j));
				}
			}
			System.out.print("]\n");
		}
	}
	
	// Prints double matrix with header header
	public static void printFitnessErrors(String header, DoubleMatrix matrix) {
		System.out.println(header);
		System.out.print("[ ");
		for(int i = 0; i < matrix.getColumns(); i++) {
			for(int j = 0 ; j < matrix.getRows(); j++) {
				System.out.printf("%.3f ", matrix.get(i, j));
			}			
		}
		System.out.print("]\n");
	}

	// Formats and prints expected outputs and predicted outputs
	public static void printOutputs(String header, DoubleMatrix left, DoubleMatrix right) {	
		System.out.println(header);
		for(int i = 0; i < left.getRows(); i++) {
			if(left.get(i, 0) < 0) {
				System.out.printf("[%.3f ",left.get(i, 0));
			}
			else {
				System.out.printf("[ %.3f ",left.get(i, 0));
			}
			
			if(left.get(i, 0) < 0) {
				System.out.printf("%.3f ]\n",right.get(i, 0));
			}
			else {
				System.out.printf(" %.3f ]\n",right.get(i, 0));
			}
		}
	}

	// Format and prints weights and error matrix ascending order
	public static void printWeightsAndErrors(String header, ArrayList<DoubleMatrix> population, DoubleMatrix errorContainer, DoubleMatrix sortedErrorContainer) {
		System.out.println(header);
		
		for(int i = 0; i < 5; i++) {
			System.out.print("[");
			for(int j = 0; j < population.get(i).getColumns(); j++) {
				
				// Get real index of chromosome from sorted error container
				int index = Matrix.getIndexOfValue(errorContainer, sortedErrorContainer.get(0, i));
				
				if(population.get(index).get(0, j) < 0) {
					System.out.printf("%.3f ",population.get(index).get(0, j));
				}
				else {
					System.out.printf(" %.3f ",population.get(index).get(0, j));
				}
			}
			System.out.printf("] %.3f \n", sortedErrorContainer.get(0, i));
		}
	}
}
