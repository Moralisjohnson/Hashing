import java.util.Scanner;

public class TabelaHashLinear {

    private Registro[] vetorHash;
    private final double A = 0.6180339887;
    private int tamanho;
    private int numeroElementos = 0;

    public TabelaHashLinear(int tamanho){
        this.tamanho = tamanho;
        this.vetorHash = new Registro[tamanho];
    }

    private int hash(int chave){
        return chave % tamanho;
    }

    public long inserirSondagemLinear(Registro registro, int indice) {
        long colisoes = 0;

        while (vetorHash[indice] != null){
            colisoes++;
            indice = (indice + 1) % tamanho;
        }

        System.out.println("Espaço encontrado, inserindo registro");
        vetorHash[indice] = registro;

        return colisoes;
    }

    public long inserir(Registro registro){
        if(numeroElementos >= tamanho){
            System.out.println("Tabela hash cheia!");
            return 0;
        }
        int chave = registro.obterCodigo();
        int indice = hash(chave);
        long colisoes = 0;
        System.out.println("Tentando inserir -> " + chave + " no índice -> " + indice);
        if(vetorHash[indice] == null){
            vetorHash[indice] = registro;
            System.out.println("Item adicionado com sucesso!");
            numeroElementos++;
            return colisoes;
        }
        System.out.println("Colisão detectada, resolvendo por sondagem linear");
        colisoes = this.inserirSondagemLinear(registro, indice);

        numeroElementos++;
        return colisoes;
    }

    public Registro buscar(int chaveDesejada) {
        int indiceOriginal = hash(chaveDesejada);
        int indiceAtual = indiceOriginal;

        while (vetorHash[indiceAtual] != null) {

            if (vetorHash[indiceAtual].obterCodigo() == chaveDesejada) {
                return vetorHash[indiceAtual];
            }

            indiceAtual = (indiceAtual + 1) % tamanho;

            if (indiceAtual == indiceOriginal) {
                return null;
            }
        }

        return null;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        long seed = 12345L;
        int[] arrayQuantidadeRegistros = {100_000, 1_000_000, 10_000_000};
        int[] arrayTamanhos = {1_000_000, 10_000_000, 100_000_000};
        long colisoes_totais = 0; // Inícia com zero colisões
        int quantidadeRegistros = -1;
        int tamanhoTabelaHash = -1;


        System.out.println("Escolha a combinação de teste para Hashing(Multiplicativo) com Sondagem Linear:");
        System.out.println("----------------------------------------------------------------");
        System.out.println("1: " + arrayQuantidadeRegistros[0] + " registros em uma tabela de tamanho " + arrayTamanhos[0]);
        System.out.println("2: " + arrayQuantidadeRegistros[1] + " registros em uma tabela de tamanho " + arrayTamanhos[1]);
        System.out.println("3: " + arrayQuantidadeRegistros[2] + " registros em uma tabela de tamanho " + arrayTamanhos[2]);
        System.out.print("Digite sua opção (1, 2 ou 3): ");

        int escolha = scanner.nextInt();

        switch (escolha){
            case 1:
                quantidadeRegistros = arrayQuantidadeRegistros[0];
                tamanhoTabelaHash = arrayTamanhos[0];
                break;
            case 2:
                quantidadeRegistros = arrayQuantidadeRegistros[1];
                tamanhoTabelaHash = arrayTamanhos[1];
                break;
            case 3:
                quantidadeRegistros = arrayQuantidadeRegistros[2];
                tamanhoTabelaHash = arrayTamanhos[2];
                break;
            default:
                System.out.println("Opção inválida. Encerrando...");
                scanner.close();
                return;
        }
        scanner.close();

        TabelaHashLinear tabela = new TabelaHashLinear(tamanhoTabelaHash);
        Registro[] dados = GeradorDeDados.gerar(quantidadeRegistros, seed);

        long tempoInicialInsercao = System.nanoTime();
        System.out.println("Iniciando inserção");
        for(Registro reg : dados) {
            colisoes_totais += tabela.inserir(reg);
        }

        System.out.println("Total de colisões no experimento: " + colisoes_totais);

        System.out.println("\nIniciando a busca de todos os elementos...");

        long tempoFinalInsercao = System.nanoTime();
        long tempoTotalInsercao = tempoFinalInsercao - tempoInicialInsercao;

        long tempoInicialBusca = System.nanoTime();

        for (Registro registro : dados) {
            int codigoParaBuscar = registro.obterCodigo();
            Registro encontrado = tabela.buscar(codigoParaBuscar);

            if (encontrado == null) {
                System.out.println("Não foi possível encontrar o registro " + codigoParaBuscar);
            }
        }

        long tempoFinalBusca = System.nanoTime();
        long tempoTotalBusca = tempoFinalBusca - tempoInicialBusca;

        System.out.println("Busca concluída!");
        System.out.println("Tempo total de busca (ms): " + tempoTotalBusca / 1_000_000.0);
        System.out.println("Tempo total de inserção (ms): " + tempoTotalInsercao / 1_000_000.0);

        double tempoInsercaoMs = tempoTotalInsercao / 1_000_000.0;
        double tempoBuscaMs = tempoTotalBusca / 1_000_000.0;

        int espacoMaior = 0;
        int espacoMenor = tamanhoTabelaHash;
        int espacoTemporario = 0;
        int somaEspacos = 0;
        int contagemEspacos = 0;

        for(Registro dado: tabela.vetorHash){

            if (dado == null){
                espacoTemporario ++;
            } else {
                if (espacoTemporario > espacoMaior) {
                    espacoMaior = espacoTemporario;
                }
                if (espacoTemporario < espacoMenor) {
                    espacoMenor = espacoTemporario;
                }
                somaEspacos += espacoTemporario;
                contagemEspacos ++;
                espacoTemporario = 0;
            }
        }
        float media = (float)somaEspacos/contagemEspacos;

        System.out.println("Calculando espaçamentos/gap:");
        System.out.println("Maior espaçamento da tabela hash encadeada: " + espacoMaior);
        System.out.println("Menor espaçamento da tabela hash encadeada: " + espacoMenor);
        System.out.println("Media dos espaçamentos encontrados: " + media);
        System.out.println();

        String tipoTabela = "Hash resto da divisão, com rehasing linear";
        String nomeDoArquivo = "resultados_tabela_linear.csv";

        GeradorCSV.salvarResultados(nomeDoArquivo, tipoTabela, tamanhoTabelaHash, quantidadeRegistros, tempoInsercaoMs, tempoBuscaMs, colisoes_totais);
    }
}