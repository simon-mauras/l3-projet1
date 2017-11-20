import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Exercice3 {
    
    private static Scanner sc;

    public static int[][] multiply(int[][] m1, int[][] m2) {
	int n = m1.length;
	int[][] res = new int[n][n];
	for (int i=0; i<n; i++) {
	    for (int j=0; j<n; j++) {
		res[i][j] = 0;
		for (int k=0; k<n; k++)
		    res[i][j] += m1[i][k]  * m2[k][j];
	    }
	}

	return res;
    }

    public static int[][] matrixPower(int[][] m, int p, int z) {
	int[][] res;

	if (p == 1)
	    res = m;
	else if (p % 2 == 1)
	    res = multiply(m, matrixPower(m, p-1, z));
	else {
	    int[][] tmp = matrixPower(m, p/2, z);
	    res = multiply(tmp, tmp);
	}

	for (int i=0; i<res.length; i++)
	    for (int j=0; j<res[i].length; j++)
		res[i][j] %= z;
	
	return res;
    }
    
    public static void printMatrix(int[][] m) {
	for (int i=0; i<m.length; i++) {
	    for (int j=0; j<m[i].length; j++) {
		System.out.print(m[i][j]);
		System.out.print("\t");
	    }
	    System.out.println();
	}
    }
    
    private static void q_3()
    {
	int m1[][] = {{1, 2, 4, 1},
		      {3, 1, 4, 2},
		      {8, 3, 1, 3},
		      {2, 1, 6, 8}};
	int m2[][] = {{0, 1, 0, 0},
		      {0, 0, 1, 0},
		      {0, 0, 0, 1},
		      {1, 0, 0, 0}};

	printMatrix(multiply(m1, m2));
	System.out.println();
	printMatrix(multiply(m2, m1));
	System.out.println();
	printMatrix(matrixPower(m1, 1, 3)); // Modulo marche bien =)
	System.out.println();
	printMatrix(matrixPower(m2, 4, 10)); // Matrice de permutation d'un 4cycle
	System.out.println();
	printMatrix(matrixPower(m2, (1 << 31) - 1, 1001)); // 2^31 - 1 = 3 (mod 4)
	System.out.println((1 << 31) - 1);
	System.out.println(1 << 31);
    }

    public static void main(String[] args) {
	sc = new Scanner(System.in);	
	q_3();
    }
}