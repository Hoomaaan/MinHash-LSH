import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;

public class MinHash {
	
	int npermu;
	String folderAddress;
	HashMap<String, HashSet<String>> termsofDoc;
	HashMap<String, Integer> allTerms;
	HashMap<String, int[]> minHashSignature;
	ArrayList<Integer> primes;
	int[][] hashFunctions;
	Random rand;
	
	public MinHash(String folder, int numPermutations){
		this.folderAddress = folder + "\\";
		this.npermu = numPermutations;
		termsofDoc = new HashMap<String, HashSet<String>>();
		allTerms = new HashMap<String, Integer>();
		minHashSignature = new HashMap<String, int[]>();
		primes = new ArrayList<Integer>();
		getAllTerms();
		generateHashFunctions(npermu);
	}
	
	public String[] allDocs() {
		File file = new File(this.folderAddress);
		File[] listOfFiles = file.listFiles();
		String[] res = new String[listOfFiles.length];
		int size = 0;
		for (File files : listOfFiles)
			if(files.isFile()) res[size ++] = files.getName();
		Arrays.sort(res);
		return res;
	}
	
	public int[][] minHashMatrix() {
		String[] allDocs = allDocs();
		int[][] minHashMatrix = new int [numPermutations()][allDocs.length];
		int d = 0;
		for (int i = 0; i < allDocs.length; i++) {
			int[] minHashsigature = minHashSig(allDocs[i]);
			for (int j = 0; j < minHashsigature.length; j++) {
				minHashMatrix[j][i] = minHashsigature[j];
			}
		}
		return minHashMatrix;
	}
	
	public int[] minHashSig(String fileName) {
		if (minHashSignature.containsKey(fileName))
			return minHashSignature.get(fileName);
		int[] minHash = new int[numPermutations()];
		HashSet<String> terms = distinctTerms(this.folderAddress + fileName);
		for (int i = 0; i < minHash.length; i++) {
			minHash[i] = 1 << 29;
			for(String term : terms)
				minHash[i] = Math.min(minHash[i], myHash(i, allTerms.get(term)));//finds the min value of the permutations for each term
		}
		minHashSignature.put(fileName, minHash);
		return minHash;
	}
	
	private int myHash (int hashFunction, int idxTerm) {
		long x = hashFunctions[0][hashFunction];
		x *= (long)idxTerm;
		x %= hashFunctions[2][hashFunction];
		x += hashFunctions[1][hashFunction];
		x %= hashFunctions[2][hashFunction];
		return (int)x;
	}
	
	public int[][] termDocumentMatrix() {
		String[] docNames = allDocs();
		int[][] termDoc = new int[numTerms()][docNames.length];
		
		for (int i = 0; i < docNames.length; i++) {
			HashSet<String> termsofDoc = distinctTerms(this.folderAddress + docNames[i]);
			int j = 0;
			for (String allTerm : allTerms.keySet()) {
				if (termsofDoc.contains(allTerm)) termDoc [j][i] = 1;
				else termDoc [j][i] = 0;
				j++;
			}
		}
		return termDoc;
	}
	
	public int numTerms() {
		return allTerms.size();
	}
	
	public int numPermutations() {
		return npermu;
	}
	
	private void getAllTerms() {
		String[] fileNames = allDocs();
		int sizeOfTerms = 0;
		for (String fileName : fileNames) {
			HashSet<String> terms = distinctTerms(this.folderAddress + fileName);
			for (String term : terms) {
				if(allTerms.containsKey(term) == false) {
					allTerms.put(term, sizeOfTerms++);
				}
			}
		}
	}
	
	private void generateHashFunctions (int npermu) {
		hashFunctions = new int[3][npermu];
		int M = numTerms();
		this.rand = new Random();
		int size_prime = 0;
		M *= Math.log((double)M);
		for(int i = 0; i < npermu; i++) {
			hashFunctions[0][i] = rand.nextInt(); //gives us a
			hashFunctions[1][i] = rand.nextInt(); //gives us b
			if (i == primes.size()) {
				M = nextPrime(M);
				primes.add(M);
			}
			else {
				M = primes.get(i);
			}
			hashFunctions[2][i] = M; //gives us p which is a prime number
		}
	}
	
	private HashSet<String> distinctTerms (String fileName){
		if (termsofDoc.containsKey(fileName)) {
			return termsofDoc.get(fileName);
		}
		HashSet<String> result = new HashSet<String>();
		String line = null, terms = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try {
			while((line = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line);
				while (token.hasMoreTokens()) {
					terms = token.nextToken();
					String new_terms = terms.replaceAll("[.;:,']", " ");
					terms = new_terms.toLowerCase();
					if (terms.equals("the") || terms.length() < 3) {
						continue;
					}
					result.add(terms);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		termsofDoc.put(fileName, result);
		return result;
	}
	
	private int nextPrime (int x) {
        while (!isPrime (++ x));
        return x ;
    }
	
	private boolean isPrime(int x) {
        if (x == 2) return true ;
        if (x % 2 == 0) return false ;
        for (long y = 3 ; y * y <= x ; y += 2) {
            if (x % y == 0) return false ;
        }
        return true ;
    }
}
