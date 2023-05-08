import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class Labirinto22 {

    private static final char PAREDE = '1';
    private static final char CAMINHO = '0';
    private static final char INICIO = 'X';
    private static final char VISITADO = '*';

    private static final int[] MOVIMENTOS_LINHA = { -1, 0, 0, 1 }; // Cima, Esquerda, Direita, Baixo
    private static final int[] MOVIMENTOS_COLUNA = { 0, -1, 1, 0 };

    private static int linhas;
    private static int colunas;
    private static char[][] labirinto;

    private static StringBuilder passos;

    public static void main(String[] args) {
        lerLabirinto(
                "D:/ArquivosPessoais/Projects/projeto-atos-capital/Teste VSCode - VAGA 01/PROVA VAGA 1/Avaliação técnica - Atos Capital/template-solution-java/Template-Java/src/entrada-labirinto.txt");
        passos = new StringBuilder();

        if (encontrarSaida()) {
            System.out.println("Labirinto resolvido:");
            imprimirLabirinto();
            System.out.println("Passos percorridos:");
            System.out.println(passos.toString());
        } else {
            System.out.println("Não foi possível encontrar uma solução para o labirinto.");
        }
    }

    private static void lerLabirinto(String arquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine();
            String[] dimensoes = linha.split(" ");
            linhas = Integer.parseInt(dimensoes[0]);
            colunas = Integer.parseInt(dimensoes[1]);
            labirinto = new char[linhas][colunas];

            for (int i = 0; i < linhas; i++) {
                linha = br.readLine();
                String[] elementos = linha.split(" ");
                for (int j = 0; j < colunas; j++) {
                    labirinto[i][j] = elementos[j].charAt(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean encontrarSaida() {
        int inicioLinha = -1;
        int inicioColuna = -1;

        // Encontrar a posição inicial (ponto de partida)
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (labirinto[i][j] == INICIO) {
                    inicioLinha = i;
                    inicioColuna = j;
                    break;
                }
            }
        }

        if (inicioLinha == -1 || inicioColuna == -1) {
            System.out.println("Ponto de partida não encontrado.");
            return false;
        }

        Deque<Posicao> pilha = new ArrayDeque<>();
        pilha.push(new Posicao(inicioLinha, inicioColuna));

        while (!pilha.isEmpty()) {
            Posicao posicaoAtual = pilha.peek();
            int linhaAtual = posicaoAtual.getLinha();
            int colunaAtual = posicaoAtual.getColuna();

            // Verificar se encontrou a saída
            if (linhaAtual == 0 || linhaAtual == linhas - 1 || colunaAtual == 0 || colunaAtual == colunas - 1) {
                return true;
            }

            boolean movimentoPossivel = false;

            // Tentar movimentos na ordem de prioridade
            int indiceMovimento = -1;
            for (int i = 0; i < MOVIMENTOS_LINHA.length; i++) {
                int novaLinha = linhaAtual + MOVIMENTOS_LINHA[i];
                int novaColuna = colunaAtual + MOVIMENTOS_COLUNA[i];

                if (posicaoValida(novaLinha, novaColuna)) {
                    indiceMovimento = i;
                    pilha.push(new Posicao(novaLinha, novaColuna));
                    labirinto[novaLinha][novaColuna] = VISITADO;

                    // Adicionar o movimento atual aos passos
                    char movimento = getMovimentoChar(indiceMovimento);
passos.append(movimento).append(" [").append(novaLinha).append(", ").append(novaColuna).append("]").append(System.lineSeparator());

                    movimentoPossivel = true;
                    break;
                }
            }

            // Se não foi possível fazer nenhum movimento, volta ao ponto anterior
            if (!movimentoPossivel) {
                Posicao posicaoAnterior = pilha.pop();
                // Remover o movimento anterior dos passos
                passos.setLength(passos.length() - 13);
                // Adicionar o movimento de retorno aos passos
                char movimentoRetorno = getMovimentoChar(indiceMovimento);
passos.append(movimentoRetorno).append(" [").append(posicaoAnterior.getLinha()).append(", ").append(posicaoAnterior.getColuna()).append("]").append(System.lineSeparator());
            }
        }
        return false;
    }

    private static boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas
                && (labirinto[linha][coluna] == CAMINHO || labirinto[linha][coluna] == INICIO);
    }

    private static void imprimirLabirinto() {
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(labirinto[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static char getMovimentoChar(int index) {
        switch (index) {
            case 0:
                return 'C';
            case 1:
                return 'E';
            case 2:
                return 'D';
            case 3:
                return 'B';
            default:
                return '-';
        }
    }

    private static class Posicao {
        private int linha;
        private int coluna;

        public Posicao(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
        }

        public int getLinha() {
            return linha;
        }

        public int getColuna() {
            return coluna;
        }
    }
}
