
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Labirinto06 {
    private static final int[] ROW_OFFSETS = { -1, 0, 0, 1 };
    private static final int[] COL_OFFSETS = { 0, -1, 1, 0 };
    private static final char[] DIRECTIONS = { 'C', 'E', 'D', 'B' };

    public static void main(String[] args) {
        String inputFile = "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt"; 
        String outputFile = "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/saida06-entrada-labirinto.txt";
        try {
            // Leitura do arquivo de entrada
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String dimensionsLine = reader.readLine();
            String[] dimensions = dimensionsLine.split(" ");
            int numRows = Integer.parseInt(dimensions[0]);
            int numCols = Integer.parseInt(dimensions[1]);
            char[][] labirinto = new char[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                String line = reader.readLine();
                labirinto[i] = line.toCharArray();
            }
            reader.close();

            // Encontrar a posição de origem (ponto O)
            int startRow = -1;
            int startCol = -1;
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if (labirinto[i][j] == 'X') {
                        startRow = i;
                        startCol = j;
                        break;
                    }
                }
                if (startRow != -1) {
                    break;
                }
            }

            // Executar a busca no labirinto
            boolean[][] visited = new boolean[numRows][numCols];
            List<Character> movements = new ArrayList<>();
            List<int[]> positions = new ArrayList<>();

            positions.add(new int[] { startRow, startCol });

            while (!positions.isEmpty()) {
                int[] position = positions.get(positions.size() - 1);
                int row = position[0];
                int col = position[1];
                visited[row][col] = true;

                if (labirinto[row][col] == '0') {
                    labirinto[row][col] = 'V'; // Marcador de posição visitada

                    // Verificar se chegou à saída
                    if (row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1) {
                        movements.remove(movements.size() - 1);
                        positions.remove(positions.size() - 1);
                        break;
                    }

                    // Tentar mover-se nas quatro direções prioritárias
                    boolean moved = false;
                    for (int i = 0; i < 4; i++) {
                        int newRow = row + ROW_OFFSETS[i];
                        int newCol = col + COL_OFFSETS[i];

                        if (isValidMove(newRow, newCol, numRows, numCols, labirinto, visited)) {
                            positions.add(new int[] { newRow, newCol });
                            movements.add(DIRECTIONS[i]);
                            moved = true;
                            break;
                        }
                    }

                    // Caso não seja possível mover-se, remove a posição atual
                    if (!moved) {
                        positions.remove(positions.size() - 1);
                        movements.remove(movements.size() - 1);
                    }
                } else {

                    // Posição é uma parede, remove da lista
                    positions.remove(positions.size() - 1);
                    movements.remove(movements.size() - 1);
                }
            }

            // Gerar arquivo de saída
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write("O [" + (startRow + 1) + ", " + (startCol + 1) + "]\n"); // Escreve a posição inicial

            for (int i = 0; i < movements.size() && i < positions.size(); i++) {
                char direction = movements.get(i);
                int[] position = positions.get(i);
                int row = position[0];
                int col = position[1];

                writer.write(direction + " [" + (row + 1) + ", " + (col + 1) + "]\n"); // Escreve a direção e a posição

                labirinto[row][col] = '.'; // Marcador de posição percorrida
            }

            // Escreve o labirinto com as posições percorridas no arquivo de saída
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    writer.write(labirinto[i][j]);
                }
                writer.newLine();
            }

            writer.close();
            System.out.println("Labirinto solucionado. Arquivo de saída gerado: " + outputFile);
        } catch (IOException e) {
            System.err.println("Erro ao ler/escrever o arquivo: " + e.getMessage());
        }
    }

    private static boolean isValidMove(int row, int col, int numRows, int numCols, char[][] labirinto,
            boolean[][] visited) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols && labirinto[row][col] == '0'
                && !visited[row][col];
    }
}
