package otimizacao; //GAUSS

import Jama.Matrix;

public class Gauss_Prof {

	private static double[] y = { 4.284, 4.149, 3.877, 0.533, 2.211, 2.389,
			2.145, 3.231, 1.998, 1.379, 2.106, 1.428, 1.011, 2.179, 2.858,
			1.388, 1.651, 1.593, 1.046, 2.152 };

	private static double[] x2 = { 0.286, 0.973, 0.384, 0.276, 0.973, 0.543,
			0.957, 0.948, 0.543, 0.797, 0.936, 0.889, 0.006, 0.828, 0.399,
			0.617, 0.939, 0.784, 0.072, 0.889 };

	private static double[] x3 = { 0.645, 0.585, 0.310, 0.058, 0.455, 0.779,
			0.259, 0.202, 0.028, 0.099, 0.142, 0.296, 0.175, 0.180, 0.842,
			0.039, 0.103, 0.620, 0.158, 0.704 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Executa o método para os pontos do livro, i.e., (3,2); (0,2) e
		// (1.5,0.5):
		executaMetodo(3, 2);
		// executaMetodo(0, 2);
		// executaMetodo(1.5, 0.5);
	}

	private static void executaMetodo(double theta1, double theta2) {
		int iteracao = 1;
		Matrix P = null;
		Matrix grad = null;
		Matrix invHess = null;
		double[][] hess = null;
		Matrix Y = null;
		Matrix F = null;
		Matrix invHess1 = null;
		Matrix diff = null;
		Matrix invHess2 = null;
		Matrix invHess3 = null;
		Matrix invHess4 = null;
		Matrix invHess5 = null;
		Matrix gradient = null;
		Matrix gradientPrime = null;

		double[] h = f(theta1, theta2);
		Y = new Matrix(y, 20);
		F = new Matrix(h, 20);
		diff = Y.minus(F);

		while (iteracao <= 8) {
			System.out.println("[" + iteracao + "] H(" + theta1 + "," + theta2
					+ ") = " + H(theta1, theta2));
			// Calcula o passo P (inverso do heissiano vezes gradiente)

			double[][] b = gradHH(theta1, theta2);
			gradient = new Matrix(b);
			gradientPrime = gradient.transpose();
			invHess1 = gradientPrime.times(gradient);
			invHess2 = invHess1.inverse();

			invHess3 = gradientPrime.times(diff);
			invHess4 = invHess2.times(invHess3);
			theta1 += invHess4.get(0, 0);
			theta2 += invHess4.get(1, 0);

			iteracao++;
		}

	}

	// Dados do problema, cf. página 956 (x1 não está aqui porque x1[t] = 1 para
	// qualquer t)

	/**
	 * Calcula a função f para um par (theta1, theta2)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return vetor com 20 posições com o resultado
	 */
	private static double[] f(double theta1, double theta2) {
		double[] f = new double[20];
		for (int t = 0; t <= 19; t++) {
			f[t] = theta1 + theta2 * x2[t] + (theta2 * theta2) * x3[t];
		}
		return f;
	}

	/**
	 * Calcula a função H para um par (theta1, theta2)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return valor de H
	 */
	private static double H(double theta1, double theta2) {
		double H = 0;
		double[] f = f(theta1, theta2);
		for (int t = 0; t <= 19; t++) {
			double y_menos_f = y[t] - f[t];
			H += y_menos_f * y_menos_f;
		}
		return H;
	}

	/**
	 * Retorna o vetor gradiente de H para um ponto (theta1, theta2)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return array cujos membros são as componentes do vetor
	 */
	private static double[] gradH(double theta1, double theta2) {
		double[] gradH = new double[2];
		for (int t = 0; t <= 19; t++) {
			double f = f(theta1, theta2)[t];
			gradH[0] += (-1) * 2 * y[t] + 2 * f;
			gradH[1] += 2 * (x2[t] + 2 * theta2 * x3[t]) * (f - y[t]);
		}
		return gradH;
	}

	private static double[][] gradHH(double theta1, double theta2) {
		double[][] gradHH = new double[20][2];
		for (int t = 0; t <= 19; t++) {
			double f = f(theta1, theta2)[t];
			gradHH[t][0] = (-1) * 2 * y[t] + 2 * f;
			gradHH[t][1] = 2 * (x2[t] + 2 * theta2 * x3[t]) * (f - y[t]);
		}
		return gradHH;
	}

	/**
	 * Retorna a matriz hessiana de H para um ponto (theta1, theta2)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return array 2x2 correspondente à matriz Hessiana
	 */
	private static double[][] hess(double theta1, double theta2) {
		double[][] hess = new double[2][2];
		for (int t = 0; t <= 19; t++) {
			double f = f(theta1, theta2)[t];
			hess[0][0] += 2;
			hess[0][1] += 2 * x2[t] + 4 * theta2 * x3[t];
			hess[1][0] += 2 * x2[t] + 4 * theta2 * x3[t];// hess[0][1];
			hess[1][1] += 4 * x3[t] * (f - y[t]) + (x2[t] + 2 * theta2 * x3[t])
					* (2 * x2[t] + 4 * theta2 * x3[t]);

		}
		return hess;
	}

}
