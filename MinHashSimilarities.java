import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class MinHashSimilarities {
	
	private int[][] termDocMatrix;
	private int[][] minHahsMatrix;
	String folderName;
	MinHash minhash;
	String[] files;
	
	public MinHashSimilarities(String folder, int numPermutations) {
		this.folderName = folder + "/";
		minhash = new MinHash(folder, numPermutations);
		termDocMatrix = minhash.termDocumentMatrix();
		minHahsMatrix = minhash.minHashMatrix();
		files = minhash.allDocs();
	}
	
	public double exactJaccard(String file1, String file2){
		HashMap<String, Integer> numTerms1 = distinctTerms(this.folderName + file1);
		HashMap<String, Integer> numTerms2 = distinctTerms(this.folderName + file2);
		HashSet<String> setTerm1 = new HashSet<String>();
		HashSet<String> setTerm2 = new HashSet<String>(); 
		HashSet<String> Union = new HashSet<String>();
		setTerm1.addAll(numTerms1.keySet());
		setTerm2.addAll(numTerms2.keySet());
		HashSet<String> Intersect = new HashSet<String>(setTerm1);
		Intersect.retainAll(setTerm2);
		Union.addAll(setTerm1);
		Union.addAll(setTerm2);
		
		int I = 0;
		int U = 0;
		for (String str : Intersect) {
			I += Math.min(numTerms1.get(str), numTerms2.get(str));
		}
		for (String str : Union) {
			if (numTerms1.containsKey(str) == false)
				U += numTerms2.get(str);
			else if (numTerms2.containsKey(str) == false)
				U += numTerms1.get(str);
			else 
				U += Math.max(numTerms1.get(str), numTerms2.get(str));
		}
		
		return (double)I/U;
	}
	
	private HashMap<String, Integer> distinctTerms (String fileName){
		HashMap<String, Integer> res = new HashMap<String, Integer>();
		String line = null, term = null;
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(fileName));
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while ((line = bf.readLine()) != null) {
				StringTokenizer tk = new StringTokenizer(line);
				while(tk.hasMoreTokens()) {
					term = tk.nextToken();
					String new_term = term.replaceAll("[.,:;']", " ");
					term = new_term.toLowerCase();
					if (term.equals("the") || term.length() < 3) {
						continue ;
					}
					if (res.containsKey(term)) res.put(term, res.get(term) + 1);
					else res.put(term, 1);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public double approximateJaccard(String file1, String file2) {
		int file1idx = 0;
		int file2idx = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].equals(file1)) 
				file1idx = i;
			if(files[i].equals(file2))
				file2idx = i;			
		}
		int matched = 0;
		int k = minhash.numPermutations();
		for (int i = 0; i < k; i++) {
			if (minHahsMatrix[i][file1idx] == minHahsMatrix[i][file2idx])
				matched++;
		}
		return (double)matched/k;
	}
	
	public int[] minHashSig(String fileName) {
		return minhash.minHashSig(fileName);
	}
}
