import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.util.Elements.Origin;
import javax.swing.JOptionPane;

public class DesafioDoLabirintoBk {

	private static final int CIMA = -1;
	private static final int ESQUERDA = -1;
	private static final int DIREITA = 1;
	private static final int BAIXO = 1;

	private static final String PAREDE = "1";
	private static final String CAMINHO = "0";
	private static final String ORIGEM = "X";
	private static final char SAIDA = 'O';

	private static int linhas;
	private static int colunas;
	private static String[][] labirinto;
	private static List<String> resultado = new ArrayList<>();
	private static List<String> historicoDeMovimentos = new ArrayList<>();

	private static final int[] ROW_OFFSETS = { -1, 0, 0, 1 };
	private static final int[] COL_OFFSETS = { 0, -1, 1, 0 };
	private static final char[] DIRECTIONS = { 'C', 'E', 'D', 'B' };

	public static void main(String[] args) {
		List<String> lines = new ArrayList<>();
		String[] dimensoes;

		// ler o arquivo
		String caminhoArquivoEntrada = "D:/ArquivosPessoais/Projects/projeto-atos-capital/minha-versao/template-solution-java/Template-Java/src/entrada-labirinto.txt";
		String caminhoArquivoSaida = "D:/ArquivosPessoais/Projects/projeto-atos-capital/minha-versao/template-solution-java/Template-Java/src/saida-entrada-labirinto.txt";

		/* verifica se o caminho de entrada nulo ou se é uma strng vazia */
		if (caminhoArquivoEntrada == null || caminhoArquivoEntrada.trim().equals("")) {
			System.out.println("O Caminho do arquivo deve ser informado!");
			return;
		}

		File file = new File(caminhoArquivoEntrada);
		if (!file.exists() || file.isDirectory()) {
			System.out.println("O caminho do arquivo informado é invélido!");
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
			System.out.println("Não foi possível ler o arquivo de entrada!");
		}

		System.out.println("Arquivo lido pelo BufferedReader e armazenado em lines 06 elementos = " + lines.toString());

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

		System.out.println(lAtual + " - " + cAtual);

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

		int maxIteracoes = linhas * colunas; // Definindo um número máximo de iterações permitidas
		int iteracoes = 0;

		while (!achouSaida && iteracoes < maxIteracoes) {

			boolean proximaPosicaoVisitada = false;
    		boolean moveu = false;

			if (lAtual > 0 && labirinto[lAtual - 1][cAtual].equals("0") && !visitados[lAtual - 1][cAtual]) {
				// Pode ir para cima
				lAtual--;
				resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
				historicoDeMovimentos.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

				proximaPosicaoVisitada = visitados[lAtual][cAtual];
        		moveu = true;

				} else if (cAtual > 0 && labirinto[lAtual][cAtual - 1].equals("0") && !visitados[lAtual][cAtual - 1]) {
					// Pode ir para a esquerda
					cAtual--;
					resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					historicoDeMovimentos.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

					proximaPosicaoVisitada = visitados[lAtual][cAtual];
        			moveu = true;

				} else if (cAtual < extremidadeColuna && labirinto[lAtual][cAtual + 1].equals("0") && !visitados[lAtual][cAtual + 1]) {
					// Pode ir para a direita
					cAtual++;
					resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					historicoDeMovimentos.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

					proximaPosicaoVisitada = visitados[lAtual][cAtual];
       		 		moveu = true;

				} else if (lAtual < extremidadeLinha && labirinto[lAtual + 1][cAtual].equals("0") && !visitados[lAtual + 1][cAtual]) {
					// Pode ir para baixo
					lAtual++;
					resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					historicoDeMovimentos.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

					proximaPosicaoVisitada = visitados[lAtual][cAtual];
        			moveu = true;

				} 
				// else {

				// 	// Tem que voltar para a posição anterior
				// 	resultado.remove(resultado.size() - 1);

				// 	// if ()
				// 	String[] ultimaPosicao = resultado.get(resultado.size() - 1).split(" ");
				// 	lAtual = Integer.parseInt(ultimaPosicao[1]);
				// 	cAtual = Integer.parseInt(ultimaPosicao[2].substring(0, ultimaPosicao[2].length() - 1));
				// }

				iteracoes++;

				achouSaida = lAtual == lSaida && cAtual == cSaida;
			}

			// Gerar o arquivo de saída
			resultado.forEach(n -> System.out.println(n + "\n"));
		}

	}

