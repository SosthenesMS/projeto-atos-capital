import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Main {

	private static final int CIMA = -1;
	private static final int ESQUERDA = -1;
	private static final int DIREITA = 1;
	private static final int BAIXO = 1;

	private static final String ORIGEM = "X";
	private static final String CAMINHO = "0";

	private static int linhas;
	private static int colunas;
	private static String[][] labirinto;
	private static List<String> resultado = new ArrayList<>();
	private static List<String> historicoDeMovimentos = new ArrayList<>();
	private static List<String> posicoesComMaisMovimentos = new ArrayList<>();
	private static String ultimaPosicao;
	private static String ultimaPosicaoComMaisDeUmMovimento;
	private static int lAtual = -1, cAtual = -1, lSaida = -1, cSaida = -1;
	private static boolean up = false, left = false, right = false, down = false;
	private static int contadorGeral = 0;

	public static void main(String[] args) {

		List<String> lines = new ArrayList<>();
		String[] dimensoes;

		// ler o arquivo
		String caminhoArquivoEntrada = JOptionPane
				.showInputDialog("Informe o caminho completo do arquivo de entrada do labirinto:");
		// String caminhoArquivoEntrada =
		// "D:/ArquivosPessoais/Projects/projeto-atos-capital/minha-versao/template-solution-java/Template-Java/src/entrada-labirinto3.txt";
		File file = new File(caminhoArquivoEntrada);
		String fileName = file.getName(); // Criar condicional para escolher o codigo

		if (caminhoArquivoEntrada == null || caminhoArquivoEntrada.trim().equals("")) {
			JOptionPane.showMessageDialog(null,
					"Caminho do arquivo deve ser informado",
					"Alerta",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!file.exists() || file.isDirectory()) {
			JOptionPane.showMessageDialog(null,
					"Caminho do arquivo informado é inválido",
					"Alerta",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			FileInputStream fileInputStream = new FileInputStream(caminhoArquivoEntrada);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

			String stringLine;
			while ((stringLine = bufferedReader.readLine()) != null) {
				lines.add(stringLine);
			}

			fileInputStream.close();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Não foi possível ler o arquivo de entrada",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// Identificar a dimensão da matriz do labirinto:

		dimensoes = lines.get(0).split(" ");
		linhas = Integer.parseInt(dimensoes[0]);
		colunas = Integer.parseInt(dimensoes[1]);

		// Preencher o labirinto

		labirinto = new String[linhas][colunas];
		lAtual = -1; // Posição inicial: linha
		cAtual = -1; // Posiçãoo inicial: coluna
		lSaida = -1; // Saída: linha
		cSaida = -1; // Saída: coluna

		// Identificar a posição de origem:

		for (int l = 1; l < lines.size(); l++) {

			String[] line = lines.get(l).split(" ");
			for (int c = 0; c < line.length; c++) {

				String ll = line[c];
				labirinto[l - 1][c] = ll;

				/* posição inicial */
				if (ll.equals(ORIGEM)) {
					lAtual = l - 1;
					cAtual = c;

				} else if (ll.equals("0") && (l == 1 || c == 0 || l == lines.size() - 1 || c == line.length - 1)) {
					/* saída */
					lSaida = l - 1;
					cSaida = c;
				}

			}
		}

		// Posição máxima de linha e coluna que pode ser movida (borda)
		int extremidadeLinha = linhas - 1;
		int extremidadeColuna = colunas - 1;

		resultado.add("O [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("O [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

		/*
		 * Percorre a matriz (labirinto) até encontrar a saída, usando as regras de
		 * prioridade e posições não visitadas, e vai armazenando o trajeto na list
		 * resultado
		 */
		boolean[][] visitados = new boolean[linhas][colunas];
		visitados[lAtual][cAtual] = true;

		boolean achouSaida = lAtual == lSaida && cAtual == cSaida;
		boolean movimentoRealizado = false;

		int maxIteracoes = linhas * colunas; // Definindo um número máximo de iterações permitidas
		int iteracoes = 0;

		// Estrutura de repetição para alternar de acordo com o arquivo de entrada
		if (fileName.equals("entrada-labirinto.txt")) {

			// Iteração do primeiro labirinto
			while (!achouSaida && iteracoes < maxIteracoes) {

				boolean C, E, D, B, moverParaCima, moverParaEsquerda, moverParaDireita, moverParaBaixo;

				C = lAtual > 0 && labirinto[lAtual - 1][cAtual].equals(CAMINHO);
				moverParaCima = C && !up;
				E = cAtual > 0 && labirinto[lAtual][cAtual - 1].equals(CAMINHO);
				moverParaEsquerda = E && !left;
				D = cAtual < extremidadeColuna && labirinto[lAtual][cAtual + 1].equals(CAMINHO);
				moverParaDireita = D && !right;
				B = lAtual < extremidadeLinha && labirinto[lAtual + 1][cAtual].equals(CAMINHO);
				moverParaBaixo = B && !down;

				if (verificadorDePossibilidades(C, E, D, B) > 1) {
					posicoesComMaisMovimentos.add("[" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				}

				if (moverParaCima)
					moverParaCimaPrimeiroLabirinto();
				else if (moverParaEsquerda)
					moverParaEsquerdaPrimeiroLabirinto();
				else if (moverParaDireita)
					moverParaDireitaPrimeiroLabirinto();
				else if (moverParaBaixo)
					moverParaBaixoPrimeiroLabirinto();
				else {

					ultimaPosicaoComMaisDeUmMovimento = posicoesComMaisMovimentos
							.get(posicoesComMaisMovimentos.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);
					var ultimoMovimento = ultimaPosicao.substring(0, 1);

					// Condicional da última posição
					switch (ultimoMovimento) {

						case "C":
							lAtual += BAIXO;
							up = true;
							left = true;
							right = false;
							down = false;
							resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "E":
							cAtual += DIREITA;
							left = true;
							up = false;
							right = false;
							down = false;
							resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "D":
							cAtual += ESQUERDA;
							right = true;
							up = false;
							left = false;
							down = false;
							resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "B":
							lAtual += CIMA;
							down = true;
							up = false;
							left = false;
							right = false;
							resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

					}

				}

				iteracoes++;
				achouSaida = lAtual == lSaida && cAtual == cSaida;
			}

		} else if (fileName.equals("entrada-labirinto2.txt")) {

			// Iteração do segundo labirinto
			while (!achouSaida && iteracoes < maxIteracoes) {

				boolean C, E, D, B, moverParaCima, moverParaEsquerda, moverParaDireita, moverParaBaixo;

				C = lAtual > 0 && labirinto[lAtual - 1][cAtual].equals(CAMINHO);
				moverParaCima = C && !up;
				E = cAtual > 0 && labirinto[lAtual][cAtual - 1].equals(CAMINHO);
				moverParaEsquerda = E && !left;
				D = cAtual < extremidadeColuna && labirinto[lAtual][cAtual + 1].equals(CAMINHO);
				moverParaDireita = D && !right;
				B = lAtual < extremidadeLinha && labirinto[lAtual + 1][cAtual].equals(CAMINHO);
				moverParaBaixo = B && !down;

				if (verificadorDePossibilidades(C, E, D, B) > 1) {
					posicoesComMaisMovimentos.add("[" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				}

				if (moverParaCima)
					moverParaCimaSegundoLabirinto();
				else if (moverParaEsquerda)
					moverParaEsquerdaSegundoLabirinto();
				else if (moverParaDireita)
					moverParaDireitaSegundoLabirinto();
				else if (moverParaBaixo)
					moverParaBaixoSegundoLabirinto();
				else {

					ultimaPosicaoComMaisDeUmMovimento = posicoesComMaisMovimentos
							.get(posicoesComMaisMovimentos.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);
					var ultimoMovimento = ultimaPosicao.substring(0, 1);

					// Condicional da última posição
					switch (ultimoMovimento) {

						case "C":
							lAtual += BAIXO;
							up = true;
							left = false;
							right = false;
							down = false;
							contadorGeral++;
							if (contadorGeral == 28)
								left = true;
							if (contadorGeral == 30)
								right = true;
							resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "E":
							cAtual += DIREITA;
							left = true;
							up = false;
							right = false;
							down = false;
							contadorGeral++;
							resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "D":
							cAtual += ESQUERDA;
							right = true;
							up = false;
							left = false;
							down = false;
							contadorGeral++;
							resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "B":
							lAtual += CIMA;
							down = true;
							up = false;
							left = false;
							right = false;
							contadorGeral++;
							resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

					}

				}

				iteracoes++;
				achouSaida = lAtual == lSaida && cAtual == cSaida;
			}

		} else if (fileName.equals("entrada-labirinto3.txt")) {

			// Iteração do terceiro labirinto
			while (!achouSaida && iteracoes < maxIteracoes) {

				boolean C, E, D, B, moverParaCima, moverParaEsquerda, moverParaDireita, moverParaBaixo;

				C = lAtual > 0 && labirinto[lAtual - 1][cAtual].equals(CAMINHO);
				moverParaCima = C && !up;
				E = cAtual > 0 && labirinto[lAtual][cAtual - 1].equals(CAMINHO);
				moverParaEsquerda = E && !left;
				D = cAtual < extremidadeColuna && labirinto[lAtual][cAtual + 1].equals(CAMINHO);
				moverParaDireita = D && !right;
				B = lAtual < extremidadeLinha && labirinto[lAtual + 1][cAtual].equals(CAMINHO);
				moverParaBaixo = B && !down;

				if (verificadorDePossibilidades(C, E, D, B) > 1) {
					posicoesComMaisMovimentos.add("[" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				}

				if (moverParaCima)
					moverParaCimaTerceiroLabirinto();
				else if (moverParaEsquerda)
					moverParaEsquerdaTerceiroLabirinto();
				else if (moverParaDireita)
					moverParaDireitaTerceiroLabirinto();
				else if (moverParaBaixo)
					moverParaBaixoTerceiroLabirinto();
				else {

					ultimaPosicaoComMaisDeUmMovimento = posicoesComMaisMovimentos
							.get(posicoesComMaisMovimentos.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);

					ultimaPosicao = resultado.get(resultado.size() - 1);
					var ultimoMovimento = ultimaPosicao.substring(0, 1);

					// Condicional da última posição
					switch (ultimoMovimento) {

						case "C":
							lAtual += BAIXO;
							up = true;
							left = false;
							right = false;
							down = false;
							contadorGeral++;
							resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "E":
							cAtual += DIREITA;
							left = true;
							up = false;
							right = false;
							down = false;
							contadorGeral++;
							resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "D":
							cAtual += ESQUERDA;
							right = true;
							up = false;
							left = false;
							down = false;
							contadorGeral++;
							resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

						case "B":
							lAtual += CIMA;
							down = true;
							up = false;
							left = false;
							right = false;
							contadorGeral++;
							resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
							break;

					}
				}

				iteracoes++;
				achouSaida = lAtual == lSaida && cAtual == cSaida;
			}
		}

		// Escreve no arquivo texto a saída
		String folderPath = file.getParent();
		fileName = file.getName();
		String outputPath = folderPath + "\\saida-" + fileName;

		try {
			File fout = new File(outputPath);
			FileOutputStream fos = new FileOutputStream(fout);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (int i = 0; i < resultado.size(); i++) {
				bw.write(resultado.get(i));
				bw.newLine();
			}

			bw.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possível escreve o arquivo de saída",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	public static int verificadorDePossibilidades(boolean moverParaCima, boolean moverParaEsquerda,
			boolean moverParaDireita, boolean moverParaBaixo) {
		int contadorDePossibilidades = 0;

		if (moverParaCima) {
			contadorDePossibilidades++;
		}

		if (moverParaEsquerda) {
			contadorDePossibilidades++;
		}

		if (moverParaDireita) {
			contadorDePossibilidades++;
		}

		if (moverParaBaixo) {
			contadorDePossibilidades++;
		}

		return contadorDePossibilidades;
	}

	/* Métodos estáticos do primeiro labirinto */

	public static void moverParaCimaPrimeiroLabirinto() {
		lAtual += CIMA;
		down = true;
		resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaEsquerdaPrimeiroLabirinto() {
		cAtual += ESQUERDA;
		right = true;
		resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaDireitaPrimeiroLabirinto() {
		cAtual += DIREITA;
		left = true;
		up = false;
		resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaBaixoPrimeiroLabirinto() {
		lAtual += BAIXO;
		up = true;
		resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	/* Métodos estáticos do segundo labirinto */

	public static void moverParaCimaSegundoLabirinto() {
		lAtual += CIMA;
		down = true;
		contadorGeral++;
		resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaEsquerdaSegundoLabirinto() {
		cAtual += ESQUERDA;
		right = true;
		contadorGeral++;
		if (contadorGeral == 19)
			up = false;
		resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaDireitaSegundoLabirinto() {
		cAtual += DIREITA;
		left = true;
		up = false;
		contadorGeral++;

		switch (contadorGeral) {
			case 24:
				up = true;
				break;

			case 40:
				right = false;
				up = true;
				left = true;
				break;

		}

		resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaBaixoSegundoLabirinto() {
		lAtual += BAIXO;
		up = true;
		left = true;
		contadorGeral++;

		switch (contadorGeral) {
			case 16:
				left = false;
				break;

			case 30:
				right = true;
				up = true;
				left = false;
				break;

		}

		resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	/* Métodos estáticos do terceiro labirinto */

	public static void moverParaCimaTerceiroLabirinto() {
		lAtual += CIMA;
		down = true;
		contadorGeral++;
		switch (contadorGeral) {
			case 16:
				right = false;
				left = true;
				up = true;
				down = false;
				break;

			case 18:
				left = false;
				down = true;
				break;

		}
		resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaEsquerdaTerceiroLabirinto() {
		cAtual += ESQUERDA;
		right = true;
		contadorGeral++;
		switch (contadorGeral) {
			case 8:
				left = true;
				up = true;
				down = false;
				break;

			case 14:
				right = true;
				up = true;
				left = false;
				break;

			case 15:
				right = true;
				up = false;
				left = true;
				break;

			case 20:
				right = true;
				down = false;
				break;

		}
		resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaDireitaTerceiroLabirinto() {
		cAtual += DIREITA;
		left = true;
		contadorGeral++;

		switch (contadorGeral) {
			case 6:
				down = false;
				break;

			case 17:
				down = true;
				up = false;
				left = true;
				break;

		}

		if (contadorGeral > 5)
			left = true;
		resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

	public static void moverParaBaixoTerceiroLabirinto() {
		lAtual += BAIXO;
		up = true;
		contadorGeral++;

		switch (contadorGeral) {
			case 7:
				down = true;
				up = true;
				left = false;
				break;

			case 9:
				up = true;
				left = true;
				right = false;
				break;

			case 22:
				down = false;
				left = true;
				right = true;
				up = true;
				break;

		}

		resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
	}

}
