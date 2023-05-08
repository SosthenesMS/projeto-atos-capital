import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Labirinto07 {
    private static final char PAREDE = '1';
    private static final char CAMINHO = '0';
    private static final char ORIGEM = 'X';
    private static final char SAIDA = 'O';

    private static final int CIMA = 0;
    private static final int ESQUERDA = 1;
    private static final int DIREITA = 2;
    private static final int BAIXO = 3;

    private static final int[] LINHA_MOVIMENTO = {-1, 0, 0, 1};
    private static final int[] COLUNA_MOVIMENTO = {0, -1, 1, 0};
    private static final char[] DIRECOES = {'C', 'E', 'D', 'B'};

    private static int linhas;
    private static int colunas;
    private static char[][] labirinto;

    public static void main(String[] args) {
        String arquivoEntrada = "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt";
        String arquivoSaida = "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/saida07-entrada-labirinto.txt";

        lerLabirinto(arquivoEntrada);
        int[] posicaoOrigem = encontrarOrigem();
        int linhaOrigem = posicaoOrigem[0];
        int colunaOrigem = posicaoOrigem[1];

        StringBuilder trajeto = new StringBuilder();
        trajeto.append("O [").append(linhaOrigem).append(", ").append(colunaOrigem).append("]\n");

        boolean[][] visitado = new boolean[linhas][colunas];
        boolean encontrouSaida = encontrarSaida(linhaOrigem, colunaOrigem, trajeto, visitado);

        if (encontrouSaida) {
            escreverSaida(arquivoSaida, trajeto.toString());
            System.out.println("Labirinto resolvido. O trajeto foi salvo no arquivo " + arquivoSaida);
        } else {
            System.out.println("Não há saída para o labirinto.");
        }
    }

    private static void lerLabirinto(String arquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String dimensoes = br.readLine();
            String[] partes = dimensoes.split(" ");
            linhas = Integer.parseInt(partes[0]);
            colunas = Integer.parseInt(partes[1]);
            labirinto = new char[linhas][colunas];

            for (int i = 0; i < linhas; i++) {
                String linha = br.readLine();
                for (int j = 0; j < colunas; j++) {
                    labirinto[i][j] = linha.charAt(j);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] encontrarOrigem() {
        int[] posicaoOrigem = new int[2];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (labirinto[i][j] == ORIGEM) {
                    posicaoOrigem[0] = i;
                    posicaoOrigem[1] = j;
                    return posicaoOrigem;
                }
            }
        }
        return null;
    }

    private static boolean encontrarSaida(int linha, int coluna, StringBuilder trajeto, boolean[][] visitado) {
        visitado[linha][coluna] = true;

        for (int i = 0; i < 4; i++) {
            int novaLinha = linha + LINHA_MOVIMENTO[i];
            int novaColuna = coluna + COLUNA_MOVIMENTO[i];

            if (posicaoValida(novaLinha, novaColuna) && !visitado[novaLinha][novaColuna] && (labirinto[novaLinha][novaColuna] == CAMINHO || labirinto[novaLinha][novaColuna] == SAIDA)) {
                trajeto.append(DIRECOES[i]).append(" [").append(novaLinha + 1).append(", ").append(novaColuna + 1).append("]\n");

                if (labirinto[novaLinha][novaColuna] == SAIDA) {
                    return true;
                }

                if (encontrarSaida(novaLinha, novaColuna, trajeto, visitado)) {
                    return true;
                }

                trajeto.append(DIRECOES[getDirecaoOposta(i)]).append(" [").append(linha + 1).append(", ").append(coluna + 1).append("]\n");
            }
        }

        return false;
    }

    private static boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }

    private static int getDirecaoOposta(int direcao) {
        if (direcao == CIMA) {
            return BAIXO;
        } else if (direcao == ESQUERDA) {
            return DIREITA;
        } else if (direcao == DIREITA) {
            return ESQUERDA;
        } else if (direcao == BAIXO) {
            return CIMA;
        }
        return -1;
    }

    private static void escreverSaida(String arquivo, String conteudo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            bw.write(conteudo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
