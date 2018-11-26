package genetic_assignment;

import org.jblas.DoubleMatrix;

public final class Matrix {
	
	// Executes matrix multiplications and returns mean of y pred matrix
	// y pred matrix includes mean of sum of each f outputs
	public static DoubleMatrix calculateYPred(DoubleMatrix inputs, DoubleMatrix weights1, DoubleMatrix weights2) {		
		DoubleMatrix hMatrix = inputs.mmul(weights1);
		DoubleMatrix fMatrix = hMatrix.mmul(weights2);
		
		return fMatrix.rowSums();
	}

	// Calculates error function
	// Error function: rmse
	public static double calculateFitnessMeasure(DoubleMatrix realOutputs, DoubleMatrix yPred) {
		
		DoubleMatrix subs = realOutputs.sub(yPred);
		DoubleMatrix pow2 = subs.mul(subs);
		return Math.sqrt(pow2.mean());	
	}
	
	// Getting index of given value at given matrix
	public static int getIndexOfValue(DoubleMatrix matrix, double value) {		
		int index = 0;
		
		for(int j = 0; j < matrix.getColumns(); j++) {
			if(matrix.get(0, j) == value) {
				index = j;
			}
		}
		
		return index;
	}
}
