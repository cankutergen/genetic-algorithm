package genetic_assignment;

import java.util.ArrayList;

import org.jblas.DoubleMatrix;
import org.jfree.ui.RefineryUtilities;

public class Drawing {
	
	// Initialize and draw chart
	// This part is referenced from https://www.tutorialspoint.com/jfreechart/jfreechart_xy_chart.htm
	public static void drawChart(int inputRowSize, int populationSize, double  mutationFader, ArrayList<DoubleMatrix> fitnessList, ArrayList<Integer> lastIndexes) {
		XYLineChart chart = new XYLineChart(
	    		 "Fitness Error / # of Generations Chart",
	    		 "Input row size: " + inputRowSize 
	    		 + " | Population size: " + populationSize 
	    		 + " | Initial mutation rate: " + Math.round(((float) 1/mutationFader) * 100.0) / 100.0  
	    		 + "\nEach line with different color represents a chromosome", 
	    		 fitnessList, 
	    		 lastIndexes);
		
	        chart.pack();          
	        RefineryUtilities.centerFrameOnScreen(chart);          
	        chart.setVisible(true); 
	}
}
