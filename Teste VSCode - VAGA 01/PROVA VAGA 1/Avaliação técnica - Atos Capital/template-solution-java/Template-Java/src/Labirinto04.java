import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Labirinto04 {
    public static void main(String[] args) {
        try {
            File file = new File("D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt");
            Scanner scanner = new Scanner(file);

            // Ler as dimensões do labirinto
            String dimensions = scanner.nextLine();
            String[] dimArray = dimensions.split(" ");
            int rows = Integer.parseInt(dimArray[0]);
            int cols = Integer.parseInt(dimArray[1]);

            // Ler o labirinto
            char[][] labirinto = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < cols; j++) {
                    labirinto[i][j] = line.charAt(j);
                }
            }

            // Fechar o scanner
            scanner.close();

            // Encontrar a posição de origem
            int startRow = -1; // Linha de origem
            int startCol = -1; // Coluna de origem

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
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

            // Verificar se a posição de origem foi encontrada
            if (startRow == -1 || startCol == -1) {
                System.out.println("Posição de origem não encontrada!");
                return;
            }

            // Criar uma matriz auxiliar para marcar as posições visitadas
            boolean[][] visited = new boolean[rows][cols];

            // Resolver o labirinto
            solveLabirinto(labirinto, visited, startRow, startCol, "");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solveLabirinto(char[][] labirinto, boolean[][] visited, int row, int col, String path) {
        int rows = labirinto.length;
        int cols = labirinto[0].length;

        // Verificar se a posição atual é a saída
        if (labirinto[row][col] == '0' && (row == 0 || row == rows - 1 || col == 0 || col == cols - 1)) {
            // Gerar o arquivo de saída com o trajeto percorrido
            generateOutputFile(path);
            return;
        }

        // Marcar a posição atual como visitada
        visited[row][col] = true;

                // Tentar mover para cima
                if (row > 0 && labirinto[row - 1][col] == '0' && !visited[row - 1][col]) {
                    solveLabirinto(labirinto, visited, row - 1, col, path + "C");
                }
        
                // Tentar mover para a esquerda
                if (col > 0 && labirinto[row][col - 1] == '0' && !visited[row][col - 1]) {
                    solveLabirinto(labirinto, visited, row, col - 1, path + "E");
                }
        
                // Tentar mover para a direita
                if (col < cols - 1 && labirinto[row][col + 1] == '0' && !visited[row][col + 1]) {
                    solveLabirinto(labirinto, visited, row, col + 1, path + "D");
                }
        
                // Tentar mover para baixo
                if (row < rows - 1 && labirinto[row + 1][col] == '0' && !visited[row + 1][col]) {
                    solveLabirinto(labirinto, visited, row + 1, col, path + "B");
                }
        
                // Desmarcar a posição atual após explorar todas as direções
                visited[row][col] = false;
            }
        
            private static void generateOutputFile(String path) {
                try {
                    File inputFile = new File("D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt");
                    String outputFileName = "saida-" + inputFile.getName();
                    File outputFile = new File(outputFileName);
                    FileWriter writer = new FileWriter(outputFile);
                    writer.write("O " + path);
                    writer.close();
                    System.out.println("Arquivo de saída gerado: " + outputFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        