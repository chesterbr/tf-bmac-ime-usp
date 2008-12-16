package tf.helpers;

import Jama.Matrix;

public class MatrixHelper {

	public static Matrix stringToMatrix(String s) {
		String[] linhas = s.split("\\n");
		double[][] elementos = new double[linhas.length][];
		int l = 0;
		for(String linha: linhas) {
			String[] colunas = linha.split("[\\n\\t ]");
			elementos[l] = new double[colunas.length];
			int c = 0;
			for (String elemento: colunas) {
				elementos[l][c] = Double.parseDouble(elemento);
				c++;
			}
			l++;
		}
		// TODO lançar exceção amigável se der problema aqui
		return new Matrix(elementos);
	}
	
}
