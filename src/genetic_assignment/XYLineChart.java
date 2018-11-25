package genetic_assignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.chart.plot.XYPlot;
import org.jblas.DoubleMatrix;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.util.ArrayList;

public class XYLineChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	// Initialize line object
	// This part is referenced from https://www.tutorialspoint.com/jfreechart/jfreechart_xy_chart.htm
	public XYLineChart(String applicationTitle, String chartTitle, ArrayList<DoubleMatrix> fitnessList, ArrayList<Integer> lastIndexes) {
	      super(applicationTitle);
	      JFreeChart xylineChart = ChartFactory.createXYLineChart(
	         chartTitle ,
	         "# of Generations" ,
	         "Fitness Errors" ,
	         createDataset(fitnessList, lastIndexes) ,
	         PlotOrientation.VERTICAL ,
	         false , true , false);
	         
	      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      double width = screenSize.getWidth();
	      double height = screenSize.getHeight();
	      
	      ChartPanel chartPanel = new ChartPanel(xylineChart);
	      chartPanel.setPreferredSize(new java.awt.Dimension((int) (width / 1.5) ,(int) (height / 1.5)));
	      XYPlot plot = xylineChart.getXYPlot();
	      
	      XYLineAndShapeRenderer renderer = setRenderer(fitnessList);
	      plot.setRenderer(renderer); 
	      setContentPane(chartPanel); 
	   }
	   
	// Sets series of dataset from list of fitnesses
    private XYDataset createDataset(ArrayList<DoubleMatrix> fitnessList, ArrayList<Integer> lastIndexes) {
	   
	   ArrayList<XYSeries> series = new ArrayList<XYSeries>();
	   
	   // Generating series for each column of fitnessList
	   // Each value at column of fitnessList represents one fitness value of chromosome at a generation
	   // Column size of fitnessList equals to generation size
	   for(int j = 0; j < fitnessList.get(0).getColumns(); j++) {
		  series.add(new XYSeries("_" + j));
	   }
	   
	   // Adding fitness values to series of each chromosome
	   for(int i = 0; i < fitnessList.size(); i++) {
		   for(int j = 0; j < fitnessList.get(i).getColumns(); j++) {		   
			   double value = fitnessList.get(i).get(0, j);
			   series.get(j).add((int) lastIndexes.get(i), value);
		   }
	   }
	                
	   // Generating data set
       XYSeriesCollection dataset = new XYSeriesCollection();    
       for(int i = 0; i < series.size(); i++) {
    	  dataset.addSeries(series.get(i));
       }
      
      return dataset;
    }

    // Sets color and thickness of each chromosome line 
    private XYLineAndShapeRenderer setRenderer(ArrayList<DoubleMatrix> fitnessList) {
	   XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	     
	   for(int j = 0; j < fitnessList.get(0).getColumns(); j++) {
		   
		   // At each iteration 3 random RGB values are generated
		   // This provides different colors for each chromosome
		   int rand1 = DataGeneration.randomInt(255, 0);
		   int rand2 = DataGeneration.randomInt(255, 0);
		   int rand3 = DataGeneration.randomInt(255, 0);
		   
	      renderer.setSeriesPaint(j , new Color(rand1, rand2, rand3));
	      renderer.setSeriesStroke(j , new BasicStroke(1.0f));
	   }
	   
	   return renderer;
    }
}
