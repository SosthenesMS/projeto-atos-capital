import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DesafioDoLabirintoBk07 {

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
	private static String ultimaPosicao;

	public static void main(String[] args) {

		List<String> lines = new ArrayList<>();
		String[] dimensoes;

		// ler o arquivo
		String caminhoArquivoEntrada = "D:/ArquivosPessoais/Projects/projeto-atos-capital/minha-versao/template-solution-java/Template-Java/src/entrada-labirinto.txt";

		try {
			FileInputStream fileInputStream = new FileInputStream(caminhoArquivoEntrada);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

			String stringLine;
			while ((stringLine = bufferedReader.readLine()) != null) {
				lines.add(stringLine);
			}

			fileInputStream.close();

		} catch (Exception ex) {
			System.out.println("Não foi possível ler o arquivo de entrada!");
		}

		// Identificar a dimensão da matriz do labirinto:

		dimensoes = lines.get(0).split(" ");
		linhas = Integer.parseInt(dimensoes[0]);
		colunas = Integer.parseInt(dimensoes[1]);

		// Preencher o labirinto

		labirinto = new String[linhas][colunas];
		int lAtual = -1; // Posição inicial: linha
		int cAtual = -1; // Posiçãoo inicial: coluna
		int lSaida = -1; // Saída: linha
		int cSaida = -1; // Saída: coluna

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
		boolean moveu = false;
		boolean up = false, left = false, right = false, down = false;

		while (!achouSaida && iteracoes < maxIteracoes) {


			/* 1 - Verificar antes de tudo se a posição atual possue mais de um caminho possível
			 * 2 - Caso tenha mais de um caminho, salvar em uma Lista a possição e um boleano de mais de um caminho como true
			 * 3 - Após esta verificação, seguir com loop normalmente, esta informação será usada apenas quando ficar sem saída
			 * para poder retornar para a posição mais próxima que tenha mais de um caminho. Isso será no else, lá embaixo. 
			 */

			boolean proximaPosicaoVisitada = false;
			boolean moverParaCima = lAtual > 0 && labirinto[lAtual - 1][cAtual].equals(CAMINHO) && up == false;
			boolean moverParaEsquerda = cAtual > 0 && labirinto[lAtual][cAtual - 1].equals(CAMINHO) && left == false;
			boolean moverParaDireita = cAtual < extremidadeColuna && labirinto[lAtual][cAtual + 1].equals(CAMINHO) && right == false;
			boolean moverParaBaixo = lAtual < extremidadeLinha && labirinto[lAtual + 1][cAtual].equals(CAMINHO)	&& down == false;

			if (moverParaCima) {

				lAtual += CIMA;
				moveu = true;
				down = true;

				resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");


			} else if (moverParaEsquerda) {

				cAtual += ESQUERDA;
				moveu = true;
				right = true;

				resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");


			} else if (moverParaDireita) {

				cAtual += DIREITA;
				moveu = true;
				left = true;
				// down = false;
				up = false;
				// right = false;

				resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");


			} else if (moverParaBaixo) {

				// Pode ir para baixo
				lAtual += BAIXO;
				moveu = true;
				up = true;

				resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

				

			} else {




				/* 1 - Verificar a posição mais próxima que tenha mais de uma possibilidade de caminho
				 * 2 - Travar o caminho de onde eu vim, pois estava sem saída e tbm travar o caminho já visitado (confrontar com o histórico)
				 * 3 - Forçar a procura do caminho novo
				 */

				// Verifica qual foi a última posição e o seu movimento

				ultimaPosicao = resultado.get(resultado.size() - 1);
				var ultimoMovimento = ultimaPosicao.substring(0, 1);
				

				// Condicional da última posição
				switch (ultimoMovimento) {

					case "C":
						lAtual += BAIXO;
						moveu = true;
						up = true;
						left = true;
						right = false;
						down = false;
						resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						System.out.println("else1");
						break;

					case "E":
						cAtual += DIREITA;
						moveu = true;
						left = true;
						up = false;
						right = false;
						down = false;
						resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						System.out.println("else2");
						break;

					case "D":
						cAtual += ESQUERDA;
						moveu = true;
						right = true;
						up = false;
						left = false;
						down = false;
						resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						System.out.println("else3");
						break;

					case "B":
						lAtual += CIMA;
						moveu = true;
						down = true;
						up = false;
						left = false;
						right = false;
						resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
						System.out.println("else4");
						break;

				}

				System.out.println("ultimaPosicao = " + ultimaPosicao);
				System.out.println("ultimoMovimento = " + ultimoMovimento);
				//System.out.println("novaPosicao = " + novaPosicao);
				System.out.println("tamanho =" + resultado.size());
				System.out.println("Linha final " + lAtual);
				System.out.println("Coluna final " + cAtual);

			}

			iteracoes++;
			achouSaida = lAtual == lSaida && cAtual == cSaida;
		}

		// Gerar o arquivo de saída

		 resultado.forEach(n -> System.out.println(n + "\n"));
		 historicoDeMovimentos.forEach(n -> System.out.println("Hitórico completo: " + n + "\n"));
		
	}

	public static void verificadorDePossibilidades(String ultimaPosicao) {
		System.out.println("verificador " + ultimaPosicao);
	}

}

//
// System.out.println("\n historico");
//
// System.out.println(lAtual + 1 + " , " + (cAtual + 1));