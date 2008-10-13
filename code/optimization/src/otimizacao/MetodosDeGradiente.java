package otimizacao;

import Jama.Matrix;

/**
 * Permite comparar métodos de gradiente para minimização de uma função objetivo
 * H.
 * <p>
 * Os métodos em questão se diferenciam apenas em como eles calculam o
 * vetor-"passo" tnPnγn, que é subtraído de um ponto a cada iteração para obter
 * o próximo ponto (fórmula B.2.5(*)).
 * <p>
 * (*) Todas as fórmulas e páginas comentadas são referências a The theory and
 * practice of econometrics, O. G. Judge, W. E. Griffith, R. C. Hill, C. H. Lee,
 * and H. Lütkepohl, second edition, Wiley, New York, 1985.
 */
public class MetodosDeGradiente {

	/**
	 * Define como serão as classes para cada método numérico implementado
	 */
	abstract static class MetodoDeGradiente {
		/**
		 * Calcula o passo a ser subtraído do ponto atual para obter o novo
		 * ponto.
		 * <p>
		 * É a componente tnPnγn de (B.2.5).
		 * <p>
		 * Os métodos numéricos devem implementar esta função, usando as funções
		 * e dados oferecidos pela classe MetodosDeGradiente.
		 * 
		 * @param theta1
		 *            primeira coordenada do ponto atual
		 * @param theta2
		 *            segunda coordenada do ponto atual
		 * @return vetor que será subtraído do ponto atual
		 */
		abstract public double[] passo(double theta1, double theta2);
	}

	/**
	 * Método de Newton-Rhapson
	 * <p>
	 * passo = inverso do heissiano vezes gradiente (pag. 957)
	 */
	static class NewtonRhapson extends MetodoDeGradiente {
		@Override
		public double[] passo(double theta1, double theta2) {
			double[][] hess = hess(theta1, theta2);
			Matrix invHess = new Matrix(hess).inverse();
			Matrix grad = new Matrix(gradH(theta1, theta2), 2);
			Matrix passo = invHess.times(grad);
			return passo.getRowPackedCopy();
		}
	}

	/**
	 * Método de Gauss-Newton.
	 * <p>
	 * passo = -[Z(Θn)'Z(Θn)]⁻¹Z(Θn)[y-f(Θn)] (B.2.24a, negativada)
	 */
	static class GaussNewton extends MetodoDeGradiente {
		@Override
		public double[] passo(double theta1, double theta2) {
			// Elementos da fórmula
			Matrix Z = new Matrix(Z(theta1, theta2));
			Matrix Zt = Z.transpose();
			Matrix y = new Matrix(_y, 20);
			Matrix f = new Matrix(f(theta1, theta2), 20);
			// Calcula o passo e devolve negativo (pois será subtraído)
			Matrix passo = Zt.times(Z).inverse().times(Zt).times(y.minus(f));
			return passo.times(-1).getRowPackedCopy();
		}
	}

	static class Marquardt extends MetodoDeGradiente {

		@Override
		public double[] passo(double theta1, double theta2) {

			/*
			 * // Elementos da fórmula Matrix Z = new Matrix(Z(theta1, theta2));
			 * Matrix Zt = Z.transpose(); Matrix y = new Matrix(_y, 20); Matrix
			 * f = new Matrix(f(theta1, theta2), 20); Matrix lambda =
			 * Matrix.identity(2, 2).times(0.7); // Calcula o passo e devolve
			 * negativo (pois será subtraído) Matrix passo = Zt.times(Z).
			 * inverse().times(Zt).times(y.minus(f)); //return
			 * passo.times(-1).getRowPackedCopy();
			 */

			Matrix Y = new Matrix(_y, 20);

			double[] h = f(theta1, theta2);
			Matrix F = new Matrix(h, 20);
			Matrix z = Y.minus(F);
			double[][] b = gradF(theta1, theta2);
			Matrix gradient = new Matrix(b);
			Matrix gradientPrime = gradient.transpose();
			Matrix invHess1 = gradientPrime.times(gradient);
			Matrix I = Matrix.identity(2, 2);
			I = I.times(0.7); // diminuir isto com o tempo?
			invHess1 = invHess1.minus(I);
			Matrix invHess2 = invHess1.inverse();
			Matrix invHess3 = gradientPrime.times(z);
			Matrix invHess4 = invHess2.times(invHess3);
			return invHess4.times(-1).getRowPackedCopy();
		}

	}

	/**
	 * Executa o método para os pontos do livro, i.e.:
	 * <p>
	 * - (3,2); (0, 2) e (1.5,0.5) para Newton-Rhapson
	 * <p>
	 * - (3,2); (3,-1) e (1.5,0.5) para Gauss-Newton
	 */
	public static void main(String[] args) {
		MetodoDeGradiente mn = new NewtonRhapson();
		executa(mn, 8, 3, 2);
		executa(mn, 8, 0, 2);
		executa(mn, 8, 1.5, 0.5);

		MetodoDeGradiente mg = new GaussNewton();
		executa(mg, 9, 3, 2);
		executa(mg, 13, 3, -1);
		executa(mg, 9, 1.5, 0.5);

		MetodoDeGradiente mm = new Marquardt();
		executa(mm, 9, 3, 2);
		executa(mm, 13, 3, -1);
		executa(mm, 9, 1.5, 0.5);

	}

	/**
	 * Executa um método numérico a partir de um ponto inicial
	 * 
	 * @param metodo
	 *            instância do método numérico a executar
	 * @param n
	 *            número de iterações a executar
	 * @param theta1
	 *            primeira coordenada do ponto inicial
	 * @param theta2
	 *            segunda coordenada do ponto inicial
	 */
	private static void executa(MetodoDeGradiente metodo, int n, double theta1,
			double theta2) {
		System.out.println("Métdodo: " + metodo.getClass().getSimpleName()
				+ " para " + theta1 + "," + theta2 + " com " + n
				+ " iterações.");
		int iteracao = 1;
		while (iteracao <= n) {
			System.out.format("  [%2d] H(%10.6f,%10.6f) = %10.4f\n", iteracao,
					theta1, theta2, H(theta1, theta2));
			// Obtém o passo para o método em questão
			double[] P = metodo.passo(theta1, theta2);
			// Subtrai este passo das variáveis atuais
			theta1 -= P[0];
			theta2 -= P[1];
			iteracao++;
		}
		System.out.println();
	}

	// Dados do problema, cf. página 956 (x1 não está aqui porque x1[t] = 1 para
	// qualquer t)

	private static double[] _y = { 4.284, 4.149, 3.877, 0.533, 2.211, 2.389,
			2.145, 3.231, 1.998, 1.379, 2.106, 1.428, 1.011, 2.179, 2.858,
			1.388, 1.651, 1.593, 1.046, 2.152 };

	private static double[] _x2 = { 0.286, 0.973, 0.384, 0.276, 0.973, 0.543,
			0.957, 0.948, 0.543, 0.797, 0.936, 0.889, 0.006, 0.828, 0.399,
			0.617, 0.939, 0.784, 0.072, 0.889 };

	private static double[] _x3 = { 0.645, 0.585, 0.310, 0.058, 0.455, 0.779,
			0.259, 0.202, 0.028, 0.099, 0.142, 0.296, 0.175, 0.180, 0.842,
			0.039, 0.103, 0.620, 0.158, 0.704 };

	// Funções de apoio (podem ser chamadas pelos métodos numéricos).

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
			f[t] = theta1 + theta2 * _x2[t] + (theta2 * theta2) * _x3[t];
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
			double y_menos_f = _y[t] - f[t];
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
			gradH[0] += -2 * _y[t] + 2 * f;
			gradH[1] += 2 * (_x2[t] + 2 * theta2 * _x3[t]) * (f - _y[t]);
		}
		return gradH;
	}

	/**
	 * Retorna o vetor gradiente de F para um ponto (theta1, theta2)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return array 20x2, com os dois componentes do gradiente para cada linha
	 *         do vetor F
	 */
	private static double[][] gradF(double theta1, double theta2) {
		double[][] gradF = new double[20][2];
		for (int t = 0; t < 20; t++) {
			double f = f(theta1, theta2)[t];
			gradF[t][0] = 1;
			gradF[t][1] = _x2[t] + 2 * theta2 * _x3[t];
		}
		return gradF;
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
			hess[0][1] += 2 * (_x2[t] + 2 * theta2 * _x3[t]);
			hess[1][0] += 2 * _x2[t] + 4 * theta2 * _x3[t];
			hess[1][1] += 4 * _x3[t] * (f - _y[t])
					+ (_x2[t] + 2 * theta2 * _x3[t])
					* (2 * _x2[t] + 4 * theta2 * _x3[t]);

		}
		return hess;
	}

	/**
	 * Calcula a função Z aplicada em um ponto, onde Z(Θ) = df/dΘ'|Θ (pag. 960)
	 * 
	 * @param theta1
	 *            primeira variável
	 * @param theta2
	 *            segunda variável
	 * @return vetor com 20 linhas e 2 colunas com o resultado
	 */
	private static double[][] Z(double theta1, double theta2) {
		double[][] Z = new double[20][2];
		for (int t = 0; t <= 19; t++) {
			Z[t][0] = 1;
			Z[t][1] = _x2[t] + 2 * theta2 * _x3[t];
		}
		return Z;
	}

}
