import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLabirinto02 {

	private static final String WALL_CHAR = "X";
	private static final String PATH_CHAR = "0";

	public static void main(String[] args) {
		String filePath = "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt";

		if (filePath == null || filePath.trim().isEmpty()) {
			System.out.println("Caminho do arquivo deve ser informado");
			return;
		}

		File file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {
			System.out.println("Caminho do arquivo informado é inválido");
			return;
		}

		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			System.out.println("Não foi possível ler o arquivo de entrada");
			e.printStackTrace();
			return;
		}

		String[] dimensions = lines.get(0).split(" ");
		int rows = Integer.parseInt(dimensions[0]);
		int columns = Integer.parseInt(dimensions[1]);

		String[][] maze = new String[rows][columns];
		int startRow = -1;
		int startColumn = -1;
		int exitRow = -1;
		int exitColumn = -1;

		for (int i = 1; i < lines.size(); i++) {
			String[] line = lines.get(i).split(" ");
			for (int j = 0; j < line.length; j++) {
				String currentCell = line[j];
				maze[i - 1][j] = currentCell;

				if (currentCell.equals(WALL_CHAR)) {
					// Posição inicial
					startRow = i - 1;
					startColumn = j;
				} else if (currentCell.equals(PATH_CHAR)
						&& (i == 1 || j == 0 || i == lines.size() - 1 || j == line.length - 1)) {
					// Saída
					exitRow = i - 1;
					exitColumn = j;
				}

				int maxRow = rows - 1;
				int maxColumn = columns - 1;

				List<String> path = new ArrayList<>();
				path.add("O [" + (startRow + 1) + ", " + (startColumn + 1) + "]");

				boolean foundExit = startRow == exitRow && startColumn == exitColumn;

				while (!foundExit) {
					if (startRow > 0 && maze[startRow - 1][startColumn].equals(PATH_CHAR)) {
						// Pode ir para cima
						startRow--;
						path.add("C [" + (startRow + 1) + ", " + (startColumn + 1) + "]");
					} else if (startColumn < maxColumn && maze[startRow][startColumn + 1].equals(PATH_CHAR)) {
						// Pode ir para a direita
						startColumn++;
						path.add("D [" + (startRow + 1) + ", " + (startColumn + 1) + "]");
					} else if (startColumn > 0 && maze[startRow][startColumn - 1].equals(PATH_CHAR)) {
						// Pode ir para a esquerda
						startColumn--;
						path.add("E [" + (startRow + 1) + ", " + (startColumn + 1) + "]");
					} else if (startRow < maxRow && maze[startRow + 1][startColumn].equals(PATH_CHAR)) {
						// Pode ir para baixo
						startRow++;
						path.add("B [" + (startRow + 1) + ", " + (startColumn + 1) + "]");
					} else {
						// Tem que voltar para a posição anterior
						path.remove(path.size() - 1);
						String[] lastPosition = path.get(path.size() - 1).split(" ");
						startRow = Integer.parseInt(lastPosition[1]);
						startColumn = Integer.parseInt(lastPosition[2].substring(0, lastPosition[2].length() - 1));
					}

					// Encontrou a saída?
					foundExit = startRow == exitRow && startColumn == exitColumn;
				}

				String outputPath = file.getParent() + "/saida-" + file.getName();

				try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
					for (String step : path) {
						bw.write(step);
						bw.newLine();
					}
				} catch (IOException e) {
					System.out.println("Não foi possível escrever o arquivo de saída");
					e.printStackTrace();
					return;
				}

				System.out.println("Arquivo de saída gerado com sucesso: " + outputPath);
			}
		}
	}
}
