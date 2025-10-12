import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeradorCSV {

    public static void salvarResultados(String nomeArquivo, String tipoTabelaHash, int tamanhoTabela, int quantidadeRegistros, double tempoInsercaoMs, double tempoBuscaMs, int colisoes) {
        File arquivo = new File(nomeArquivo);
        boolean novoArquivo = !arquivo.exists();

        try (FileWriter fw = new FileWriter(arquivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            // Se o arquivo acabou de ser criado, escreve o cabeçalho
            if (novoArquivo) {
                pw.println("TabelaHash,tamanho da tabela,tamanho do conjunto,tempo de inserção (ms),tempo de busca (ms), colisoes");
            }
            String linhaDeDados = String.format("\"%s\",%d,%d,%.4f,%.4f, %d",
                    tipoTabelaHash,
                    tamanhoTabela,
                    quantidadeRegistros,
                    tempoInsercaoMs,
                    tempoBuscaMs,
                    colisoes);
            
            pw.println(linhaDeDados);

            System.out.println("\nResultados salvos com sucesso em " + nomeArquivo);

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao escrever no arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}