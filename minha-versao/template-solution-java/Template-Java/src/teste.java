import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class teste {

	public static void main(String[] args) {
		// LE O ARQUIVO
		String filePath = "D:/ArquivosPessoais/Projects/projeto-atos-capital/minha-versao/template-solution-java/Template-Java/src/entrada-labirinto.txt";

		if (filePath == null || filePath.trim().equals("")) {
			JOptionPane.showMessageDialog(null, "Caminho do arquivo deve ser informado", "Alerta",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		File f = new File(filePath);
		if (!f.exists() || f.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Caminho do arquivo informado é inválido", "Alerta",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<String> lines = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			while ((strLine = br.readLine()) != null)
				lines.add(strLine);

			fstream.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Não foi possível ler o arquivo de entrada", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] dimensoes = lines.get(0).split(" ");
		int linhas = Integer.parseInt(dimensoes[0]);
		int colunas = Integer.parseInt(dimensoes[1]);

		// Preenche matriz do labirinto
		String[][] matriz = new String[linhas][colunas];
		int lAtual = -1; // Posição inicial: linha
		int cAtual = -1; // Posição inicial: coluna
		int lSaida = -1; // Saída: linha
		int cSaida = -1; // Saída: coluna

		// percorre toda a matriz (a partir da segunda linha do arquivo texto) para
		// identificar a posição inicial e a saída
		for (int l = 1; l < lines.size(); l++) {
			String[] line = lines.get(l).split(" ");
			for (int c = 0; c < line.length; c++) {
				String ll = line[c];
				matriz[l - 1][c] = ll;

				if (ll.equals("X")) {
					// Posição inicial
					lAtual = l - 1;
					cAtual = c;
				} else if (ll.equals("0") && (l == 1 || c == 0 || l == lines.size() - 1 || c == line.length - 1)) {
					// Saída
					lSaida = l - 1;
					cSaida = c;
				}
			}
		}

		// Posição máxima de linha e coluna que pode ser movida (borda)
		int extremidadeLinha = linhas - 1;
		int extremidadeColuna = colunas - 1;

		// Guarda o trajeto em uma list de string e já inicia com a posição de origem
		List<String> resultado = new ArrayList<String>();
		resultado.add("O [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");

		// Percorre a matriz (labirinto) até encontrar a saída, usando as regras de
		// prioridade e posições não visitadas, e vai armazenando
		// Percorre a matriz (labirinto) até encontrar a saída, usando as regras de
		// prioridade e posições não visitadas, e vai armazenando o trajeto na lista
		// "resultado"
		boolean achouSaida = lAtual == lSaida && cAtual == cSaida;
		while (!achouSaida) {
			if (lAtual > 0 && matriz[lAtual - 1][cAtual].equals("0")) {
				// Pode ir para cima
				lAtual--;
				resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
			} else if (cAtual < extremidadeColuna && matriz[lAtual][cAtual + 1].equals("0")) {
				// Pode ir para a direita
				cAtual++;
				resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
			} else if (cAtual > 0 && matriz[lAtual][cAtual - 1].equals("0")) {
				// Pode ir para a esquerda
				cAtual--;
				resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
			} else if (lAtual < extremidadeLinha && matriz[lAtual + 1][cAtual].equals("0")) {
				// Pode ir para baixo
				lAtual++;
				resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
			} else {
				// Tem que voltar para a posição anterior
				resultado.remove(resultado.size() - 1);
				if (resultado.isEmpty()) {
					// Não há mais movimentos possíveis e não encontrou a saída
					JOptionPane.showMessageDialog(null, "Não foi possível encontrar a saída do labirinto", "Alerta",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				String ultimoMovimento = resultado.get(resultado.size() - 1);
				char direcao = ultimoMovimento.charAt(0);
				switch (direcao) {
					case 'C':
						lAtual++;
						break;
					case 'D':
						cAtual--;
						break;
					case 'E':
						cAtual++;
						break;
					case 'B':
						lAtual--;
						break;
				}
			}

			// Achou a saída?
			achouSaida = lAtual == lSaida && cAtual == cSaida;
		}

		// Escreve no arquivo texto a saída
		String folderPath = f.getParent();
		String fileName = f.getName();
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
			JOptionPane.showMessageDialog(null, "Saída do labirinto escrita no arquivo: " + outputPath, "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Não foi possível escrever o arquivo de saída", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
