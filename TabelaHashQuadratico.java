import java.util.Scanner;

public class TabelaHashQuadratico {
    private Registro[] vetorHash;
    private int tamanho;
    private int numeroElementos = 0;

    public TabelaHashQuadratico(int tamanho){
        this.tamanho = tamanho;
        this.vetorHash = new Registro[tamanho];
    }

    // função hash de divisao usando primos para distribuir melhor as chaves
    private int hashDivisao(int chave){
        int R = 0;

        if (tamanho == 10_000) {
            R = 9973; // Primo < 10000
        } else if (tamanho == 100_000) {
            R = 99989; // Primo < 100000
        } else if (tamanho == 1_000_000) {
            R = 999983; // Primo < 1000000
        } else {
            R = 97;
        }

        int passo = R - (chave % R);
        return passo;
    }

    public int inserirHashDuplo(Registro registro, int indice){
        int colisoes = 0;
        int i = 1;
        int passo = hashDivisao(indice);

        while (vetorHash[indice] != null){
            colisoes++;
            // Formula do Hash Duplo: indice = (h1(chave) + i * h2(chave)) % tamanhoTabela
            indice = (indice + i * passo) % tamanho;

            i++;

            // Impedir loop infinito
            if (i > tamanho){
                System.out.println("Tabela hash cheia!");
                return colisoes;
            }
        }

        // Encontrou espaco livre, insere o registro
        System.out.println("Espaço encontrado, inserindo registro");
        vetorHash[indice] = registro;
        return colisoes;
    }

    public int inserirSondagemQuadratica(Registro registro, int indice){
        int colisoes = 0;
        int i = 1;
        int indiceAtual = indice;

        while (vetorHash[indiceAtual] != null){
            colisoes++;

            // Formula da Sondagem Quadratica: indice = (h1(chave) + i^2) % tamanho
            indiceAtual = (indice + i * i) % tamanho;

            i++;

            if (i > tamanho){
                System.out.println("Tabela hash cheia!");
                return colisoes;
            }
        }

        // Encontrou um espaço livre, insere o registro
        System.out.println("Espaço encontrado, inserindo registro");
        vetorHash[indiceAtual] = registro;

        return colisoes;
    }

    public int inserir(Registro registro){
        if(numeroElementos >= tamanho){
            System.out.println("Tabela hash cheia!");
            return 0;
        }

        int chave = registro.obterCodigo();
        int indice = hashDivisao(chave);
        int colisoes = 0;
        System.out.println("Tentando inserir -> " + chave + " no indice -> " + indice);
        if(vetorHash[indice] == null){
            vetorHash[indice] = registro;
            System.out.println("Item adicionado com sucesso!");
            numeroElementos++;
            return colisoes;
        }
        // Temos colisão
        System.out.println("Colisão detectada, resolvendo por sondagem quadratica");
        //colisoes = this.inserirHashDuplo(registro, indice);
        colisoes = this.inserirSondagemQuadratica(registro, indice);

        numeroElementos++;
        return colisoes;
    }

    public Registro buscar(int chaveDesejada) {
        int indiceOriginal = hashDivisao(chaveDesejada);
        int indiceAtual = indiceOriginal;

        // A sondagem (loop) continua enquanto a posição atual não estiver vazia.
        while (vetorHash[indiceAtual] != null) {

            if (vetorHash[indiceAtual].obterCodigo() == chaveDesejada) { // Sucesso de primeira (0 Colisões para esta chave)
                return vetorHash[indiceAtual]; // Devolve o registro
            }

            // Caso não seja o registro, devemos continuar procurando
            indiceAtual = (indiceAtual + 1) % tamanho;

            // Este if serve para que não tenhamos um while infinito se a tabela estiver cheia
            if (indiceAtual == indiceOriginal) {
                return null;
            }
        }

        // Elemento não existe
        return null;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        long seed = 12345L;
        int[] arrayQuantidadeRegistros = {100_000, 1_000_000, 10_000_000};
        int[] arrayTamanhos = {1_000_000, 10_000_000, 100_000_000};
        int colisoes_totais = 0; // Inícia com zero colisões
        int quantidadeRegistros = -1;
        int tamanhoTabelaHash = -1;


        System.out.println("Escolha a combinação de teste para Hashing(Meio Quadratico) com Quadratico(Rehashing):");
        System.out.println("----------------------------------------------------------------");
        System.out.println("1: " + arrayQuantidadeRegistros[0] + " registros em uma tabela de tamanho " + arrayTamanhos[0]);
        System.out.println("2: " + arrayQuantidadeRegistros[1] + " registros em uma tabela de tamanho " + arrayTamanhos[1]);
        System.out.println("3: " + arrayQuantidadeRegistros[2] + " registros em uma tabela de tamanho " + arrayTamanhos[2]);
        System.out.print("Digite sua opção (1, 2 ou 3): ");

        int escolha = scanner.nextInt();

        switch (escolha){
            case 1:
                quantidadeRegistros = arrayQuantidadeRegistros[0]; // 100.000
                tamanhoTabelaHash = arrayTamanhos[0];              // 10.000
                break;
            case 2:
                quantidadeRegistros = arrayQuantidadeRegistros[1]; // 1.000.000
                tamanhoTabelaHash = arrayTamanhos[1];              // 100.000
                break;
            case 3:
                quantidadeRegistros = arrayQuantidadeRegistros[2]; // 10.000.000
                tamanhoTabelaHash = arrayTamanhos[2];              // 1.000.000
                break;
            default:
                System.out.println("Opção inválida. Encerrando...");
                scanner.close();
                return;
        }
        scanner.close();

        TabelaHashQuadratico tabela = new TabelaHashQuadratico(tamanhoTabelaHash);
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

        // Itere
        for (Registro registro : dados) {
            int codigoParaBuscar = registro.obterCodigo();
            Registro encontrado = tabela.buscar(codigoParaBuscar);

            // Verificação opcional
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

        //calculando os gaps/espaçamentos
        for(Registro dado: tabela.vetorHash){

            if (dado == null){
                espacoTemporario ++;
            } else { //se tiver algo no dado, significa que a sequencia de espacos/gaps acabou
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

        String tipoTabela = "Hash Meio do Quadrado com rehashing quadrático";
        String nomeArquivo = "resultados_hashquadratico.csv";

        GeradorCSV.salvarResultados(nomeArquivo, tipoTabela, tamanhoTabelaHash, quantidadeRegistros, tempoInsercaoMs, tempoBuscaMs, colisoes_totais);
    }
}
