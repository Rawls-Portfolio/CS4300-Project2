import java.util.List;
import java.util.Scanner;

public class SimpleGenetic {

	public static void main(String[] args) {
		/* GET USER INPUT for population size and size of string 
		   and algorithm choice */
		Scanner scanner = new Scanner(System.in);  /* reading from System.in */
		
		System.out.println("Enter a population size ( as an integer. Odd numbers will automatically be rounded up): ");
		int popSize = scanner.nextInt();
		if (popSize % 2 == 1)
			popSize++;
		
		System.out.println("Enter a size of the binary string (as an integer): ");
		int dnaLen = scanner.nextInt();
		
		System.out.println("Enter the menu number for which algorithm you would like to use: \n" +
							"1. OneMax\n2. TrapFive\n");
		int choice = scanner.nextInt();
		GeneticAlgorithm algorithm = null;
		while (algorithm == null){
			switch(choice){
				case 1: 
					algorithm = new OneMax();
					break;
				case 2:
					algorithm = new TrapFive();
					if (!(dnaLen % 5 == 0)){
						System.out.println("Adjusting binary string size to be valid for TrapFive...");
						while (!(dnaLen % 5 == 0))
							dnaLen++;
						System.out.println("New binary string size is " + dnaLen + ".\n");
					}
					break;
				default:
					System.out.println("Invalid choice. Enter 1 or 2. Try again: \n");
					choice = scanner.nextInt();
					break;
			} // end switch
		} // end while
		
		/* get input from user about run quantity */
		System.out.println("Do you want to run the program 100 times and get some data? \n" +
							"1. Yes\n2. No\n");
		choice = scanner.nextInt();
		boolean runOnce = true;
		boolean invalid = true;
		while (invalid){
			switch(choice){
				case 1: 
					runOnce = false;
					invalid = false;
					break;
				case 2:
					runOnce = true;
					invalid = false;
					break;
				default:
					System.out.println("Invalid choice. Enter 1 or 2. Try again: \n");
					choice = scanner.nextInt();
					break;
			} // end switch
		} // end while
		scanner.close();
		
		
		/* RUN PROGRAM */
		if (runOnce)
			runProgram(popSize, dnaLen, algorithm);
		else{ /* gather data on 100 attempts to find optimum solution*/
			int r, maxHit = 0, maxValue = 0;
			double avgs = 0, gens = 0;
			
			for (r = 0; r < 100; r++){
				System.out.println("Run " + (r + 1) + ": \n");
				Generation run = runProgram(popSize, dnaLen, algorithm);
				avgs += run.getAverage();
				gens += run.getGen();
				if (run.getMax() > maxValue)
					maxValue = run.getMax();
				if (run.getMax() == dnaLen)
					maxHit++; } // end for
			
			System.out.println("Out of 100 runs, population size of " + popSize + 
								", binary string size of " + dnaLen + 
								", optimum solution success rate: " + maxHit + "%");
			if (maxValue < dnaLen)
				System.out.println("The highest fitness found was " + maxValue + ".");
			System.out.println("The average number of generations it took before completing the search was " + (gens/100.0) + ".");
			System.out.println("The average of last generations' averages was " + (avgs/100.0) + ".");
			} // end else: run 100 times
	} // end main

	
	private static Generation runProgram(int popSize, int dnaLen, GeneticAlgorithm algorithm) {
		/* initialize instance of Generation */
		Generation currentGen = new Generation(popSize, dnaLen, algorithm);
		currentGen.displayStatistics();
		
		/* replace currentGen */
		while ((currentGen.getMax() != dnaLen) && currentGen.averageImproved()){ 
			currentGen = buildnextGen(algorithm, currentGen);
			currentGen.displayStatistics(); } // end while 
		
	return currentGen;
		
	} // end runProgram


	/* runs a tournament for a given generation with the given algorithm, 
	 * until the new generation is filled to n-2. adds the best parents
	 * and returns the descendants, effectively replacing parents' population with children's population */
	private static Generation buildnextGen(GeneticAlgorithm algorithm, Generation parents) {
		Generation descendants = parents.descendants();
		Individual parentOne, parentTwo;
		List<Individual> children = null;
		List<Individual> bestParents = null;
		do {	// select parents and reproduce
			parentOne = parents.tournament();
			parentTwo = parents.tournament();
			
			children = algorithm.crossover(parentOne, parentTwo);
			descendants.addIndividual(children.get(0));
			descendants.addIndividual(children.get(1));
			
		} while (descendants.getPopSize() < (parents.getPopSize() - 2));
		
		// elitism
		bestParents = parents.getBestParents();
		descendants.addIndividual(bestParents.get(0));
		descendants.addIndividual(bestParents.get(1));
		return descendants;
	} // end buildnextGen
} // end SimpleGenetic
