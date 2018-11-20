import java.awt.image.BandCombineOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LSH {
	
	int nxtPrime = -1;
	HashMap<Integer, HashSet<String>> [] T;
	HashMap<String, Integer>[] hashSig; 
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		int k = minHashMatrix.length; //#of rows = # of hash functions
		int r = k / bands;
		nxtPrime = nextPrime (docNames.length * 2);
		
		T = new HashMap[bands];
		hashSig = new HashMap[bands];
		
		for (int i = 0; i < bands; i++) {
			T[i] = new HashMap<Integer, HashSet<String>>();
			hashSig[i] = new HashMap<String, Integer>();
		}
		for (int j = 0; j <docNames.length; j++) {
			for(int b = 0; b < bands; b++) {
				ArrayList<Integer> bandOfSig = new ArrayList<Integer>();
				for(int x = b * r; x < Math.min(k, (b + 1) * r); x++)
						bandOfSig.add(minHashMatrix[x][j]);
			
				int t = hashSig(bandOfSig);
				HashSet<String> set;
				if(T[b].containsKey(t) == false) {
					set = new HashSet<String>();
				}
				else {
					set = T[b].get(t);
				}
				set.add(docNames[j]);
				T[b].put(t, set);
				hashSig[b].put(docNames[j], t);
			}
		}
	}
	
	public ArrayList<String> nearDuplicatesOf(String docName){
		HashSet<String> nearDupSet = new HashSet<String>();
		for(int i = 0; i < hashSig.length; i++) {
			int hashCode = hashSig[i].get(docName);
			for(String str : T[i].get(hashCode)) {
				if(str.equals(docName) == false)
					nearDupSet.add(str);
			}
		}
		ArrayList<String> nears = new ArrayList<String>(nearDupSet);
		return nears;
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
	 
	 private int hashSig (ArrayList <Integer> bandOfSig) {
	        StringBuilder str = new StringBuilder("") ;
	        for (Integer x : bandOfSig) {
	            str.append(x.toString() + ",") ;
	        }
	        if (str.length() > 0)
	            str.deleteCharAt(str.length() - 1) ;
	        String ss = str.toString() ;
	        int hashCode = ss.hashCode() % nxtPrime ;
	        return hashCode ;
	    }
}
