import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Generation {
	
	private int genNumber;
	private List<Double> history;
	private List<Individual> population;
	private int popSize;
	private int min;
	private int max;
	private double average;

	
	/* constructor for generation Zero */
	public Generation(int popSize, int dnaLen, GeneticAlgorithm algorithm) {
		int s;
		this.genNumber = 0;
		this.popSize = 0;
		this.population = new ArrayList<Individual>(popSize);
		this.history = new ArrayList<Double>();		

		/* initialize generation Zero */
		for (s = 0; s < popSize; s++)
			this.addIndividual(new Individual(dnaLen, algorithm));	/* this.popSize is updated in addIndividual() */
		
		this.calculateStatistics();
	} // end public constructor


	/* empty constructor, only to be called from descendants()
	 * inherits history from parent, sets most recent entry to ancestor.average */
	private Generation(Generation ancestor) {
		this.genNumber = ancestor.genNumber + 1;
		this.popSize = 0;
		this.population = new ArrayList<Individual>(ancestor.popSize);
		this.history = ancestor.history;
		
	} // end private constructor

	
	/* creates an empty Generation and returns the Generation */
	public Generation descendants() {
		Generation descendants = new Generation(this);
		return descendants;
	} // end descendants
	
	
	/* add the Individual to the population and update popSize */
	public void addIndividual(Individual person) {
		this.population.add(person);
		this.popSize = this.population.size();
	} // end addIndividual
	
	
	/* once the population has been established, call this function to calculate statistics */
	private void calculateStatistics() {
		int p, v = 0, sum = 0;
		List<Integer> values = new ArrayList<Integer>(this.popSize);
		
		for (p = 0; p < this.popSize; p++){
			v = this.population.get(p).getFitness();
			values.add(v);
			sum += v; } // end for
	
			Collections.sort(values);
			this.min = values.get(0);
			this.max = values.get(this.popSize-1);
			this.average = (double)sum/this.popSize;
			
			this.history.add(this.average);
			
			/* maintain history length of 3 entries */
			while (this.history.size() > 3){
				this.history.remove(0); } // end while
	} // end calculateStatistics
	
	
	/*displays the average, best, and worst fitness found in the population */
	public void displayStatistics() {
		if (this.min == 0 && this.max == 0)
			this.calculateStatistics();
		System.out.println("Generation " + genNumber + ":\nminimum fitness: " + this.min + "\n" +
							"maximum fitness: " + this.max + "\n" +
							"average fitness: " + this.average + "\n");
	} // end displayStatistics
	

	/* return true if the average fitness has improved over the last three generations */
	public boolean averageImproved() {
		/* if 3 generations have not yet been seen, assume improvement is true */
		if (this.history.size() >= 3 && 							/* there is enough data */
				(( this.history.get(2) <= this.history.get(1) ) &&	/* no improvement in the last two generations */
					this.history.get(1) <= this.history.get(0) ))	/* no improvement in the previous two generations */
				return false;
			else 													/* else assume room for improvement */
				return true;
	} // end averageImproved

	
	/* returns a random Individual of the population */
	public Individual getRandParent() {					
		int random = (int)(Math.random() * this.popSize);
		return population.get(random);
	} // end getRandParent


	/* picks two parents at random from the population, returns the best fit 
	 * or randomly if fitness is equal */
	public Individual tournament() {
		Individual parentOne = this.getRandParent();
		Individual parentTwo = this.getRandParent();
		
		if (parentOne.getFitness() > parentTwo.getFitness())	
			return parentOne;
		else if (parentOne.getFitness() < parentTwo.getFitness())
			return parentTwo;
		else { /* unbiased tie breaker */
			int random = (int)Math.random() * 2;
			if (random == 1)
				return parentOne;
			else
				return parentTwo; } // end else
	} // end tournament

	/* return a list of two Individuals with the best fitness */
	public List<Individual> getBestParents(){
		List<Individual> best = new ArrayList<Individual>(2);
		Individual first = this.population.get(0);
		Individual second = this.population.get(1);
		int p;
		
		for (p = 1; p < this.popSize; p++){
			/* if the current Individual has a better fitness than the running first, 
			 * bump running first to second place, set current Individual to first */
			if (this.population.get(p).getFitness() > first.getFitness()){
				second = first;
				first = this.population.get(p);	} /* end if */ } // end for
			
		
		best.add(first);
		best.add(second);
		return best;
	} /// end getBestParents

	
	public double getAverage() {
		return this.average;
	} // end getAverage

	public int getPopSize() {
		return this.popSize;
	} // end getPopSize

	public int getMax() {
		return this.max;
	} // end getMax

	public double getGen() {
		return (double) this.genNumber;
	} // end getGen
} // end Generation
