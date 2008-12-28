package tf.helpers;

import java.util.ArrayList;

import Jama.Matrix;

public class MatrixHelper {

	public static Matrix stringToMatrix(String s) {
		String[] linhas = s.split("\\n");
		double[][] elementos = new double[linhas.length][];
		int l = 0;
		for (String linha : linhas) {
			System.out.println("CHESTER:linha:[" + linha + "]");
			String[] colunas = linha.split("[\\n\\t ]+");
			ArrayList<Double> al = new ArrayList<Double>();
			for (String elemento : colunas) {
				System.out.println("CHESTER:elemento:[" + elemento + "]");
				if (elemento.trim().length() > 0) {
					al.add(Double.parseDouble(elemento));
				}
			}
			elementos[l] = new double[al.size()];
			for (int i = 0; i < al.size(); i++)
				elementos[l][i] = al.get(i);

			l++;
		}
		// TODO lançar exceção amigável se der problema aqui
		return new Matrix(elementos);
	}

}
