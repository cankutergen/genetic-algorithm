package genetic_assignment;

import org.jblas.DoubleMatrix;
import java.util.Random;

public final class DataGeneration {
	
	// Generates random integer between minX and maxX
	public static int randomInt(int maxX, int minX) {	
		Random random = new Random();	
		return random.nextInt(maxX + 1 - minX) + minX;
	}
	
	// Generates random float between minX and maxX
	public static float randomFloat(float maxX, float minX) {	
		Random random = new Random();	
		return random.nextFloat() * (maxX - minX) + minX;
	}

	// Generates output array by taking mean of row vector
	public static DoubleMatrix generateOutputs(DoubleMatrix inputs){
		return inputs.rowMeans();
	}

	// Generates 2 dimensional array with rowSize and colSize 
	// by generating random variables between minX and maxX 
	public static DoubleMatrix generateInputs(int maxX, int minX, int rowSize, int colSize){	
		double[][] array = new double[rowSize][colSize]; 	
		
		for(int i = 0 ; i < rowSize; i++) {
			for(int j = 0; j < colSize; j++) {
				array[i][j] = DataGeneration.randomFloat(maxX, minX);
			}
		}
		return new DoubleMatrix(array);
	}
	
	// Generates weight array with row size weightRowSize and weightColSize 
	// by generating random variables between minWeight and maxWeight 
	public static DoubleMatrix generateWeightArray(float maxX, float minX, int rowSize, int colSize){
		double[][] array = new double[rowSize][colSize]; 	
		
		for(int i = 0 ; i < rowSize; i++) {
			for(int j = 0; j < colSize; j++) {
				array[i][j] = DataGeneration.randomFloat(maxX, minX);
			}
		}
		return new DoubleMatrix(array);
	}

}


