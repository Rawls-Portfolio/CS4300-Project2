import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrapFive implements GeneticAlgorithm {

	@Override
	/*given two Individuals, execute two point crossover
 	  return two Individuals with crossoverDNA variations*/
	public List<Individual> crossover(Individual one, Individual two) {
		List<Individual> children = new ArrayList<Individual>(2);
		Random rand = new Random();
		int len = one.getDNALen();
		int chance = rand.nextInt(100);
		
		if (chance <= 60) { /* 60% chance of crossover success*/
			List<Integer> childOneDNA = new ArrayList<Integer>(len);
			List<Integer> childTwoDNA = new ArrayList<Integer>(len);
			int l, left = rand.nextInt(len), right = rand.nextInt(len);
			
			for (l = 0; l < len; l++){ /* build two point crossoverDNA*/
				if (l < left || l > right){ /* if copy point is before or after the crossover section */
					childOneDNA.add(one.getDNA().get(l));
					childTwoDNA.add(two.getDNA().get(l)); } // end if
				else { /* if copy point is within the crossover section */
					childOneDNA.add(two.getDNA().get(l));
					childTwoDNA.add(one.getDNA().get(l)); } /* end else */  } // end for
			
			children.add(Individual.createChild(childOneDNA, this));
			children.add(Individual.createChild(childTwoDNA, this));
		} // end if crossover successful
		else { /* 40% chance of crossover failure, copy parents */
			children.add(one);
			children.add(two); } // end else, copy parents
		
		// regardless of crossover success, mutate children
		children.get(0).mutate();
		children.get(1).mutate();
		
		return children;
	} //end crossover

	@Override
	/*evaluates person's fitness and returns its fitness value
	  according to the trap5 fitness specification
	  TrapFive partitions the bits in the string into 5-bit partitions
	  Each 5-bit group (0-4, 5-9, etc) has its own fitness:
	  5: if all bits are 1
	  4 - (sum of 1s): if less than 5 1s
	  The total fitness is the sum of these partitions*/
	public int evaluateFitness(Individual person) {
		int partition, partsize = 5, l, sum = 0, fitnessValue = 0, len = person.getDNALen();
		for (partition = 0; partition < len; partition += partsize){ 	/* divide the string into partitions*/
			for (l = 0; l < partsize; l++){ 							/* evaluate each bit in the partition */
				if (person.getDNA().get(partition+l) == 1)
				sum++; } // end for each bit in partition
			if (sum == 5)
				fitnessValue += sum;
			else 
				fitnessValue += (4 - sum);
			sum = 0; } //end for each partition
		return fitnessValue;
	} // end evaluateFitness
} // end TrapFive
