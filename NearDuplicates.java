import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NearDuplicates {
	int[][] minhashMatrix;
	String[] res;
	int bestR, bestB;
	LSH lsh;
	double s;
	public NearDuplicates(String folder, int numPermutations, double s) {
		MinHash minhash = new MinHash(folder, numPermutations);
		this.minhashMatrix = minhash.minHashMatrix();
		this.res = minhash.allDocs();
		this.s = s;
		
		double mn = 1e20 ;
        bestR = 0; bestB = 0;
        for (int r = 1 ; r <= numPermutations ; r ++) {
            int b = (numPermutations / r) ;
            if (b > 0 && b <= numPermutations) {
                double guessS = Math.pow(1. / b, 1. / r) ;
                if (Math.abs(guessS - s) < mn) {
                    mn = Math.abs(guessS - s) ;
                    bestR = r ;
                    bestB = b ;
                }
            }
        }
		
		lsh = new LSH(minhashMatrix, res, bestB);
	}
	
	public ArrayList<String> nearDuplicatDetector(String docName){
		ArrayList<String> neardocs = new ArrayList<String>();
		neardocs = lsh.nearDuplicatesOf(docName);
		return neardocs;
	}
}
