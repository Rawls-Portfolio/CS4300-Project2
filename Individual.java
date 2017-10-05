import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
	
	private List<Integer> dna;
	private int dnaLen;
	private int fitness;
	private GeneticAlgorithm algorithm;

	
	/*constructor, randomly initialize dna of length s*/
	public Individual(int dnaLen, GeneticAlgorithm algorithm) {
		int d, value;
		this.dnaLen = dnaLen;					/* used by GeneticAlgorithm */
		this.dna = new ArrayList<Integer>(dnaLen);
		for (d = 0; d < dnaLen; d++){
			value = (int)(Math.random() * 2);
			dna.add(value); } // end for
		
		this.algorithm = algorithm;
		this.fitness = this.algorithm.evaluateFitness(this);
	} // end public constructor
	

	/* constructor, supplied with DNA*/
	private Individual(List<Integer> crossoverDNA, GeneticAlgorithm algorithm) {
		this.dna = crossoverDNA;
		this.dnaLen = crossoverDNA.size();
		this.algorithm = algorithm;
		this.fitness = -1; 				/* mark fitness as unevaluated; this child will evaluate fitness after mutation */
	} // end private constructor

	
	/*calls the private constructor and returns a new Individual */
	public static Individual createChild(List<Integer> crossoverDNA, GeneticAlgorithm algorithm) {
		Individual child = new Individual(crossoverDNA, algorithm);
		return child;
	} // end createChild
	
	
	/* iterates through each value in dna and mutates according to the probability of 1/dnaLen */
	public void mutate() {
		Random rand = new Random();
		int l;
		for (l = 0; l < this.dnaLen; l++){
			if (rand.nextInt(this.dnaLen) == 0){ /* (1/dnaLen) chance of mutating dna at this location */
				if(this.dna.get(l) == 1)
					this.dna.set(l, 0);
				else
					this.dna.set(l,  1); } /* end if*/ } // end for 
		
		this.fitness = this.algorithm.evaluateFitness(this);
	} // end mutate


	/* calls the evaluateFitness function from the algorithm
	 * and sets the current individual's fitness */
	public int setFitness(GeneticAlgorithm algorithm) {
		fitness = algorithm.evaluateFitness(this);
		return fitness;
	} // end setFitness
	
	public int getFitness() {
		return fitness;
	} // end getFitness

	public int getDNALen() {
		return dnaLen;
	} // end getDNALen
	
	public List<Integer> getDNA(){
		return dna;
	} // end getDNA
} // end Individual
