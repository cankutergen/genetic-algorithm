# genetic-algorithm

The process involves performing some series of mathematical operations on the given diagram. 
An array of real numbers x = [x1, x2, x3] is given to some parameters called weights denoted as W; 
and after performing that operations illustrated in the figure below, system will produce a real valued output, 
called y_predict. Note that the color of the arrows has no functionality, it is just to make the process visually traceable. 

The value of each weight is represented by a gene. A chromosome will consist of 12 genes - corresponds the value of weights.
 
 y1 = mean(x1) &nbsp;&nbsp;
 y1 = mean(x2) &nbsp;&nbsp;
 ... &nbsp;&nbsp;
 yn = mean(xn) &nbsp;&nbsp;

![alt text](http://i67.tinypic.com/2mguom8.png)

Weights / genes have values in the range of [-1,1] <br />
Input matrix X have real values in the range of [-5,5] <br />

<br />

**PSEUDO CODE OF ALGORITHM**<br />
while iteration number i !=  generation size <br />
	&nbsp;&nbsp;while population iteration j != population size<br />
		&nbsp;&nbsp;&nbsp;&nbsp;if first iteration => generate random chromosome<br />
		&nbsp;&nbsp;&nbsp;&nbsp;else => get chromosome from population at index j<br />
		&nbsp;&nbsp;&nbsp;&nbsp;get weight1s and weight2s from chromosome<br />
		&nbsp;&nbsp;&nbsp;&nbsp;calculate y_pred<br />
		&nbsp;&nbsp;&nbsp;&nbsp;calculate fitness error<br />
	&nbsp;&nbsp;end<br />
	&nbsp;&nbsp;// generating crossed over and mutated population<br />
	&nbsp;&nbsp;select parent1 with tournament selection, parent2 randomly<br />
	&nbsp;&nbsp;get 2 childs with uniform crossover of 2 parents<br />
	&nbsp;&nbsp;apply mutation to parents and childs<br />
	&nbsp;&nbsp;add list to childs and parents  <br />
	&nbsp;&nbsp;calculate fitness errors of items at list and sort them ascending<br />
	&nbsp;&nbsp;hold individuals that has lowest fitness error with size of population<br />
	&nbsp;&nbsp;// stop criteria<br />
	&nbsp;&nbsp;if fitness error of any chromosome < 0.05 => stop iteration<br />
end<br />

**SELECTION**<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Selection is the one of the most important step of genetic algorithm, because in order to converge better results best parents should be selected while premature convergence is avoided. In my algorithm I select one of the parents with tournament selection. K individuals are selected randomly at population and individual with lowest fitness error is selected as parent. If I always select individual with lowest fitness value, next generations start to resemble that individual and that may cause premature convergence. Second parent is selected randomly in order to increase diversity of population, which move away my algorithm from premature convergence. <br />
**CROSSOVER**<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Crossover is an amazing step to transfer best genes from parent with lower fitness error to other parent. By this way fitness errors of chromosomes are starts decreasing. In my algorithm I use uniform crossover. In a uniform crossover, chromosomes are not divided into segments, rather each genes are treated separately. In this, coin is flipped for each chromosome to decide if or not it’ll be included in the child. <br />
**MUTATION**<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mutation is crucial step for move away algorithm from suboptimal solutions. Because with mutation random genes are added to chromosomes and this addition increases diversity of population. Also mutation can prevent algorithm from local minimum problems. In algorithm non-uniform mutation is used. At early generations, mutation will be applied to higher number of genes, because algorithm need to increase its diversity at early steps in order to prevent premature convergence or suboptimal solutions. On the contrary, lower mutation rates are required because algorithm should be converged. In my algorithm, instead of changing random value with selected gene, I calculate mean of value of selected gene and a random value which prevent wandering the algorithm from real solution.<br />
**RECOMBINATION**<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Children are inserted into the set of parents. After adding all children to the population, new population is sorted by fitness error and worst individuals are removed from population by this way population size N again. As a result of recombination worst individuals are removed from population and at each generation better children generated.<br />
