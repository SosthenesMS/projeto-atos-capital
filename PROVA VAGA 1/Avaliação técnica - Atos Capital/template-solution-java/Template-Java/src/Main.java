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

    private static int linhas;
    private static int colunas;
    private static String[][] matriz;
    private static int lAtual;
    private static int cAtual;
    private static int lSaida;
    private static int cSaida;
    private static List<String> resultado;

    public static void main(String[] args) {
        // LE O ARQUIVO
        String filePath = JOptionPane.showInputDialog("Informe o caminho completo do arquivo de entrada do labirinto:");

        if (filePath == null || filePath.trim().equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Caminho do arquivo deve ser informado",
                    "Alerta",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        File f = new File(filePath);
        if (!f.exists() || f.isDirectory()) {
            JOptionPane.showMessageDialog(null,
                    "Caminho do arquivo informado é inválido",
                    "Alerta",
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
            JOptionPane.showMessageDialog(null,
                    "Não foi possível ler o arquivo de entrada",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] dimensoes = lines.get(0).split(" ");
        linhas = Integer.parseInt(dimensoes[0]);
        colunas = Integer.parseInt(dimensoes[1]);

        // Preenche matriz do labirinto
        matriz = new String[linhas][colunas];
        lAtual = -1; // Posição inicial: linha
        cAtual = -1; // Posição inicial: coluna
        lSaida = -1; // Saída: linha
        cSaida = -1; // Saída: coluna

        // Percorre toda a matriz (a partir da segunda linha do arquivo texto) para identificar a posição inicial e a saída
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

                // Guarda o trajeto em uma lista de string e já inicia com a posição de origem
				resultado = new ArrayList<String>();
				resultado.add("O [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
		
				// Percorre a matriz (labirinto) até encontrar a saída, usando as regras de prioridade e posições não visitadas, e vai armazenando o trajeto na lista resultado
				boolean achouSaida = lAtual == lSaida && cAtual == cSaida;
		
				while (!achouSaida) {
					if (canMoveUp(lAtual, cAtual)) {
						moveUp();
						resultado.add("C [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					} else if (canMoveLeft(lAtual, cAtual)) {
						moveLeft();
						resultado.add("E [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					} else if (canMoveRight(lAtual, cAtual)) {
						moveRight();
						resultado.add("D [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					} else if (canMoveDown(lAtual, cAtual)) {
						moveDown();
						resultado.add("B [" + (lAtual + 1) + ", " + (cAtual + 1) + "]");
					} else {
						// Tem que voltar para a posição anterior
						int[] previousPosition = getPreviousPosition(resultado.get(resultado.size() - 1));
						lAtual = previousPosition[0];
						cAtual = previousPosition[1];
						resultado.remove(resultado.size() - 1);
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
					JOptionPane.showMessageDialog(null,
							"Arquivo de saída gerado: " + outputPath,
							"Informação",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Não foi possível escrever o arquivo de saída",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		
			private static boolean canMoveUp(int l, int c) {
				return l > 0 && matriz[l - 1][c].equals("0");
			}
		
			private static void moveUp() {
				lAtual--;
			}
		
			private static boolean canMoveLeft(int l, int c) {
				return c > 0 && matriz[l][c - 1].equals("0");
			}
		
			private static void moveLeft() {
				cAtual--;
			}
		
			private static boolean canMoveRight(int l, int c) {
				return c < colunas - 1 && matriz[l][c + 1].equals("0");
			}
		
			private static void moveRight() {
				cAtual++;
			}
		
			private static boolean canMoveDown(int l, int c) {
				return l < linhas - 1 && matriz[l + 1][c].equals("0");
			}
		
			private static void moveDown() {
				lAtual++;
			}
		
			private static int[] getPreviousPosition(String position) {
				int[] prevPos = new int[2];
				position = position.substring(3, position.length() - 1);
				String[] coordinates = position.split(", ");
				prevPos[0] = Integer.parseInt(coordinates[0]) - 1;
				prevPos[1] = Integer.parseInt(coordinates[1]) - 1;
				return prevPos;
			}
		}
				