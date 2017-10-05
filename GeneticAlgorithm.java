import java.util.List;

public interface GeneticAlgorithm {
	
	/* given two Individuals, create a new DNA string and return two Individuals */	
	public List<Individual> crossover(Individual one, Individual two);
	
	/* given an Individual, evaluate its fitness and return its fitness value */
	public int evaluateFitness(Individual person);
} // end GeneticAlgorithm
