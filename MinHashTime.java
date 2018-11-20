
public class MinHashTime {
	public void timer(String folder, int numPermutations) {
		//Constructing time
		long startTime = System.currentTimeMillis();
		MinHashSimilarities minHashSimi = new MinHashSimilarities(folder, numPermutations);
		long endTime = System.currentTimeMillis();
		long elapsedTime = (endTime - startTime);
		System.out.println("Time taken to construct an instance of MinHashSimilarities: " + elapsedTime +" milisec");
		
		//Exact Jacc for every pair of files
		String[] Files = minHashSimi.files;
		long elapsedTime2 = 0;
		for(int i = 0; i < Files.length - 1; i++) {
			for(int j = i + 1; j < Files.length; j++) {
				long startTime2 = System.currentTimeMillis();
				minHashSimi.exactJaccard(Files[i], Files[j]);
				long endTime2 = System.currentTimeMillis();
				elapsedTime2 += (endTime2 - startTime2);
			}
		}
		System.out.println("Time taken to calculate exact Jaccard Similarity for every pair of files: " + elapsedTime2 +" milisec");
		
		//Approximate Jacc for every pair of files
		long elapsedTime3 = 0;
		for(int i = 0; i < Files.length - 1; i++) {
			for(int j = i + 1; j < Files.length; j++) {
				long startTime3 = System.currentTimeMillis();
				minHashSimi.approximateJaccard(Files[i], Files[j]);
				long endTime3 = System.currentTimeMillis();
				elapsedTime3 += (endTime3 - startTime3);
			}
		}
		long els = elapsedTime3 + elapsedTime;
		System.out.println("Time taken to calculate approximate Jaccard Similarity of every pair of files when we have MinHashMatrix: " + elapsedTime3 +" milisec");
		System.out.println("Time taken to calculate approximate Jaccard Similarity of every pair of files + making MinHashMatrix first: " + els +" milisec");
		
		
	}
}
