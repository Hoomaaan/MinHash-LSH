
public class MinHashAccuracy {
	public void accuracy(String folder, int numPermutations, double error) {
		MinHashSimilarities minhashSimi = new MinHashSimilarities(folder, numPermutations);
		String[] Files = minhashSimi.files;
		int violationNumber = 0;
		
		for(int i = 0; i < Files.length - 1; i++) {
			for(int j = i + 1; j < Files.length; j++) {
				double exactJacc = minhashSimi.exactJaccard(Files[i], Files[j]);
				double appJacc = minhashSimi.approximateJaccard(Files[i], Files[j]);
				double diff = Math.abs(exactJacc - appJacc);
				if (error <= diff)
					violationNumber++;
			}
		}
		System.out.printf("For #Permutations = %d, there are %d pairs of files that their exact and approximate similarities differ by more than %.2f\n\n", numPermutations, violationNumber, error);
	}
}
