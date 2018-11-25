package genetic_assignment;

import java.util.ArrayList;
import java.util.Scanner;

import org.jblas.DoubleMatrix;

public class Main {

	public static void main(String[] args) {
					
		int maxInputValue = 5; int minInputValue = -5; 
		int inputRowSize = 25; int inputColSize = 3;
		int populationSize = 100; int generationSize = 10000; 
		int totalWeights = 12;
		boolean validInput = false;
		
		// lower the mutationFader, higher the mutation rate 
		// 1 means mutation will be added to all genes
		// 2 means mutation will be added to 50% of genes
		double mutationFader = 2;
		double successThreshold = 0.05;
		
		// Geting inputs from user
		Scanner input = new Scanner(System.in);	
		while(!validInput) {
			System.out.println("Enter input row size please");
			System.out.println("Input row size should be at least 10");
			try {
				inputRowSize = Integer.parseInt(input.nextLine());
				validInput = true;
				if(inputRowSize < 10){
					validInput = false;
				}
			}
			catch(Exception e){
				System.out.println("Please enter valid input row size");
			}
		}
		validInput = false;

		while(!validInput) {
			System.out.println("Enter population size please");
			System.out.println("Population size should be at least 50");
			System.out.println("Higher population size is recommended in order to converge faster");
			try {			
				populationSize = Integer.parseInt(input.nextLine());
				validInput = true;
				if(populationSize < 50){
					validInput = false;
				}
			}
			catch(Exception e){
				System.out.println("Please enter valid population size");
			}
		}
		validInput = false;

		while(!validInput) {
			System.out.println("Enter initial mutation rate please");
			System.out.println("Initial mutation rate should be between 1 - 100");
			System.out.println("Lower initial mutation rate is recommended in order to converge faster");
			try {		
				double rate = Double.parseDouble(input.nextLine());
				mutationFader = 100 / rate;
				validInput = false;
				if(mutationFader >= 0.01 && mutationFader >= 1 && rate != 0) {
					validInput = true;
				}
			}
			catch(Exception e){
				System.out.println("Please enter valid initial mutation rate");
			}
		}
		validInput = false;
		
		DoubleMatrix errorContainer = new DoubleMatrix(new double[1][populationSize]);
		DoubleMatrix chromosome = new DoubleMatrix(new double[1][totalWeights]);
		ArrayList<DoubleMatrix> population = new ArrayList<DoubleMatrix>();
		ArrayList<DoubleMatrix> fitnessList = new ArrayList<DoubleMatrix>();
		ArrayList<Integer> lastIndexes = new ArrayList<Integer>();
		
		// Generates x inputs
		DoubleMatrix inputs = DataGeneration.generateInputs(maxInputValue, 
																 minInputValue, 
																 inputRowSize, 
																 inputColSize);	
		Utilities.printInputs("Input Matrix:", inputs);
		
		// Generates y outputs
		DoubleMatrix realOutputs = DataGeneration.generateOutputs(inputs);	

		for(int i = 0; i < generationSize; i++) {
			ArrayList<DoubleMatrix> tempPopulation = new ArrayList<DoubleMatrix>();
			ArrayList<DoubleMatrix> predictedOutputs = new ArrayList<DoubleMatrix>();
			
			for(int j = 0; j < populationSize; j++) {
				if(i == 0) {
					chromosome = DataGeneration.generateWeightArray(1, -1, 1, totalWeights);
				}		
				else {			
					chromosome = population.get(j);
				}
				
				// Generation and resizing weights from chromosome
				DoubleMatrix weights1 = chromosome.getColumnRange(0, 0, totalWeights/2).reshape(3, 2);
				DoubleMatrix weights2 = chromosome.getColumnRange(0, 6, totalWeights).reshape(2, 3);
				
				// Calculation of y_pred and fitness error
				DoubleMatrix yPred = Matrix.calculateYPred(inputs, weights1, weights2);
				double fitness = Matrix.calculateFitnessMeasure(realOutputs, yPred);
				
				errorContainer.put(0, j, fitness);
				tempPopulation.add(j, chromosome);
				predictedOutputs.add(j, yPred);
			}
			
			// In order to copy errorContainer immutably
			DoubleMatrix copyError = errorContainer.dup();
			DoubleMatrix sortedErrorContainer = errorContainer.dup();
			
			// Adaptive mutation rate according to generation number
			// In order to get rid of sub optimal solution, higher mutation rates are required at early levels
			// On the contrary, in order not to lose good chromosomes, lower mutation rates are required at next phases
			double mutationRate = ((float) generationSize - i) / ((float) generationSize * mutationFader);			
			population = Genetic.generateNewPopulation(errorContainer, tempPopulation, inputs, realOutputs, mutationRate);
					
			sortedErrorContainer = sortedErrorContainer.sort();
			
			if(i%500 == 0) {		
				
				// ErrorContainer is added to fitnessList in order to draw fitness chart 
				fitnessList.add(copyError);
				lastIndexes.add(i);
				Utilities.printFitnessErrors("\nGeneration: " + i + "\nFitness errors of " + populationSize + " chromosomes" , sortedErrorContainer);
			}
			if(errorContainer.sort().get(0) < successThreshold) {
				
				// ErrorContainer is added to fitnessList in order to draw fitness chart
				fitnessList.add(copyError);
				lastIndexes.add(i);
				
				// In order to get chromosome with lowest error value
				int bestIndex = Matrix.getIndexOfValue(errorContainer, errorContainer.sort().get(0));
				
				Utilities.printFitnessErrors("\nGeneration: " + i + "\nFitness errors of " + populationSize + " chromosomes", sortedErrorContainer);
				Utilities.printOutputs("\n[ y | y_pred of fittest ]", realOutputs, predictedOutputs.get(bestIndex));
				Utilities.printWeightsAndErrors("\n Best 5 Weights/Chromosomes Matrix | fitness errors", population, errorContainer, sortedErrorContainer);
				
				Drawing.drawChart(inputRowSize, populationSize, mutationFader, fitnessList, lastIndexes);
				
				break;
			}
		}		
	}
}
