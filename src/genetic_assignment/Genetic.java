package genetic_assignment;

import java.util.ArrayList;
import java.util.Comparator;

import org.jblas.DoubleMatrix;

public final class Genetic {
	
	// Selection method is Tournament Selection
	// 4 competitors are randomly selected at population
	// Parent is selected by best fitness function 
	private static int tournamentSelection(DoubleMatrix errorContainer, int tournamentSize) {	
		ArrayList<Double> competitors = new ArrayList<Double>();
		
		// Generating random competitors
		for(int i = 0 ; i < tournamentSize; i++) {
        	int rand = DataGeneration.randomInt(errorContainer.getColumns() - 1, 0); 	
        	double data = errorContainer.get(0, rand);
			competitors.add(i, data); 
		}
        
        //  Get best fitness value and return it to selection
        competitors.sort(Comparator.naturalOrder());
            
        return Matrix.getIndexOfValue(errorContainer, competitors.get(0));
	}
	
	// Uniform Crossover is done
	// Flip a coin for each chromosome to decide whether or not it’ll be included in the off-spring
	// returns array with index 0 => parent 1 after crossover, index 1 => parent 2 after crossover
	private static ArrayList<DoubleMatrix> crossover(DoubleMatrix parent1, DoubleMatrix parent2, double crossoverRate) {
		
		ArrayList<DoubleMatrix> list = new ArrayList<DoubleMatrix>();
		DoubleMatrix child1 = new DoubleMatrix(new double[1][parent1.getColumns()]);
		DoubleMatrix child2 = new DoubleMatrix(new double[1][parent1.getColumns()]);
		
		for(int i = 0; i < parent1.getColumns(); i++) {
			
			// Flipping a coin
			int rand = DataGeneration.randomInt(100, 0);
			
			double temp1 = parent1.get(0, i);
			double temp2 = parent2.get(0, i);
			
			// Exchange genes between two parents 
			if(rand < crossoverRate) {			
				child1.put(0, i, temp2);
				child2.put(0, i, temp1);			
			}
			else {			
				// Remains unchanged
				child1.put(0, i, temp1);
				child2.put(0, i, temp2);
			}
		}

		list.add(child1);
		list.add(child2);
		
		return list;	
	}

	// Iterate population with crossover, mutation and recombination
	public static ArrayList<DoubleMatrix> generateNewPopulation(DoubleMatrix errorContainer, 

		ArrayList<DoubleMatrix> childSet = new ArrayList<DoubleMatrix>();
		ArrayList<DoubleMatrix> mutatedParents = new ArrayList<DoubleMatrix>();
		
		for(int i = 0; i < population.size(); i = i + 2) {
			
			DoubleMatrix parent1;
			DoubleMatrix parent2;
			DoubleMatrix child1;
			DoubleMatrix child2;
			double crossoverRate = 50;
			int tournamentSize = population.size() / 10 <= 1 ? 2:population.size() / 10;
			
			// Getting parents, one is from tournament selection, other is random
			int best = tournamentSelection(errorContainer, tournamentSize);
			int rand = DataGeneration.randomInt(population.size() - 1, 0);
			
			// In order to prevent crossover between the same chromosomes
			while(best == rand) {
				rand = DataGeneration.randomInt(population.size() - 1, 0);
			}
			
			parent1 = population.get(best);
			parent2 = population.get(rand);
					
			// Crossover generates 2 children
			ArrayList<DoubleMatrix> parents = crossover(parent1, parent2, crossoverRate);
			child1 = parents.get(0);
			child2 = parents.get(1);	

			// Mutation is applied to children and parents
			childSet.add(mutation(child1, mutationRate));
			childSet.add(mutation(child2, mutationRate));
			mutatedParents.add(mutation(parent1, mutationRate));
			mutatedParents.add(mutation(parent2, mutationRate));
		}
				
		ArrayList<DoubleMatrix> combinedList = new ArrayList<DoubleMatrix>();
		
		// Combining parent and child sets for recombination
		combinedList.addAll(childSet);
		combinedList.addAll(mutatedParents);
		
		// Recalculate the fitnesses for recombined population
		// Sort fitness values
		// Remove worst fitness valued chromosomes from list in order to fix population size
		DoubleMatrix combinedErrorContainer = recalculateErrorContainer(combinedList, inputs, outputs);
		combinedErrorContainer = sortRecombinedErrorContainer(combinedErrorContainer, population.size());
		return recombination(combinedErrorContainer, combinedList);	
	}

	// Non-uniform mutaion with rate of mutation rate
	private static DoubleMatrix mutation(DoubleMatrix parent, double mutationRate) {		
		DoubleMatrix mutated = parent.dup() ;
		
		for(int i = 0; i < mutated.getColumns() * mutationRate; i++) {
			int randIndex = DataGeneration.randomInt(mutated.getColumns() - 1, 0);
			
			double parentValue = parent.get(randIndex);
			double value = DataGeneration.randomFloat((float) 0.5, (float) -0.5);
			
			// Adding mean of old value and new random value to gene 
			mutated.put(0, randIndex, (value + parentValue) / 2);
		}
		
		return mutated;
	}

	// Recombination of parents and children lists
	private static ArrayList<DoubleMatrix> recombination(DoubleMatrix combinedErrorContainer, ArrayList<DoubleMatrix> combinedList) {
		
		ArrayList<DoubleMatrix> newPop = new ArrayList<DoubleMatrix>();
		
		// Get best fitness values from recombined list
		// And assign it to population
		// So population size remains same
		for(int i = 0; i < combinedErrorContainer.getColumns(); i++) {
			
			// Find index of fitness value of index i
			int index = Matrix.getIndexOfValue(combinedErrorContainer, combinedErrorContainer.get(i));
			
			// Get chromosome with index of index and set population i with that
			newPop.add(combinedList.get(index));
		}
		
		return newPop;
	}

	// Ascending sorting of fitness errors of recombined list 
	private static DoubleMatrix sortRecombinedErrorContainer(DoubleMatrix combinedErrorContainer, int size) {
		combinedErrorContainer = combinedErrorContainer.sort();
		return combinedErrorContainer.getColumnRange(0, 0, size);
	}
	
	// Recalculate fitness errors of recombined list
	private static DoubleMatrix recalculateErrorContainer(ArrayList<DoubleMatrix> population, DoubleMatrix inputs, DoubleMatrix outputs) {
		
		DoubleMatrix errorContainer = new DoubleMatrix(new double[1][population.size()]);
		
		for(int i = 0 ; i < population.size(); i++) {
			DoubleMatrix weights1 = population.get(i).getColumnRange(0, 0, population.get(i).getColumns() / 2).reshape(3, 2);
			DoubleMatrix weights2 = population.get(i).getColumnRange(0, population.get(i).getColumns() / 2, population.get(i).getColumns()).reshape(2, 3);
			
			DoubleMatrix yPred = Matrix.calculateYPred(inputs, weights1, weights2);
			double fitness = Matrix.calculateFitnessMeasure(outputs, yPred);
			
			errorContainer.put(0, i, fitness);
		}
		
		return errorContainer;
	}
}
