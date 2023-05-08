import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Labirinto03 {
    public static void main(String[] args) {
        try {
            File file = new File(
                    "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt");
            Scanner scanner = new Scanner(file);

            // Ler as dimensões do labirinto
            String dimensions = scanner.nextLine();
            String[] dimArray = dimensions.split(" ");
            int rows = Integer.parseInt(dimArray[0]);
            int cols = Integer.parseInt(dimArray[1]);

            // Ler o labirinto
            int[][] labirinto = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = scanner.nextLine();
                String[] cells = line.split(" ");
                for (int j = 0; j < cols; j++) {
                    labirinto[i][j] = Integer.parseInt(cells[j]);
                }
            }

            // Fechar o scanner
            scanner.close();

            // Resto do código para resolver o labirinto...

            // Após ler o labirinto do arquivo de entrada...
            int startRow = -1; // Linha de origem
            int startCol = -1; // Coluna de origem

            // Encontrar a posição de origem
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

            
                

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        


    }

}
