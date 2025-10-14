import java.util.Scanner;

public class TabelaHashEncadeada {
    private Registro[] vetorHash;
    private final double A = 0.6180339887;
    private int tamanho; // tamanhos: 1000 - 10000 - 100000

    public TabelaHashEncadeada (int tamanho) {
        this.tamanho = tamanho;
        this.vetorHash = new Registro[tamanho];
    }

    // hash de multiplicação
    private int hash(int chave){
        double parteFrac = (chave * A) - Math.floor(chave * A);
        return (int) Math.floor(tamanho * parteFrac);
    }

    // metodo inserir
    public long inserir(Registro registro) {
        int chave = registro.obterCodigo();
        int indice = hash(chave);
        long colisao = 0;

        // verificar se da pra inserir no vetor
        if (vetorHash[indice] == null) {
            vetorHash[indice] = registro;
        } else {
            // caso haja colisão, insere na lista encadeada
            colisao++;
            Registro atual = vetorHash[indice];
            while (atual.obterProximo() != null) {
                atual = atual.obterProximo();
            }
            atual.inserirProximo(registro);
        }
        return colisao;
    }

    // metodo buscar
    public int buscar(int chave) {
        // coletar o indice da chave passada
        int indice = hash(chave);
        int encontrado = 0;

        // encontrar valor
        if (vetorHash[indice] != null) {
            Registro atual = vetorHash[indice];
            while (atual.obterCodigo() != chave) {
                atual = atual.obterProximo();
            }

            if (atual.obterCodigo() == chave) {
                encontrado = 1;
            }
        }
        return encontrado;
    }

    public static void main(String[] args) {
        long semente = 12345L;
        int[] arrayQuantidadeRegistro = {100_000, 1_000_000, 10_000_000};
        int[] arrayTamanhos = {1_000_000, 10_000_000, 100_000_000};
        long colisoes = 0; // contador de colisões
        int quantidadeRegistros = -1;
        int tamanhoTabelaHash = -1;

        System.out.println("Hashing Encadeado com método multiplicativo:");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("1: " + arrayQuantidadeRegistro[0] + " registros em uma tabela de tamanho " + arrayTamanhos[0]);
        System.out.println("2: " + arrayQuantidadeRegistro[1] + " registros em uma tabela de tamanho " + arrayTamanhos[1]);
        System.out.println("3: " + arrayQuantidadeRegistro[2] + " registros em uma tabela de tamanho " + arrayTamanhos[2]);
        System.out.println("Escolha a combinação que deseja usar: ");

        Scanner teclado = new Scanner(System.in);
        int escolha = teclado.nextInt();
        teclado.close();

        switch (escolha) {
            case 1:
                quantidadeRegistros = arrayQuantidadeRegistro[0];
                tamanhoTabelaHash = arrayTamanhos[0];
                break;
            case 2:
                quantidadeRegistros = arrayQuantidadeRegistro[1];
                tamanhoTabelaHash = arrayTamanhos[1];
                break;
            case 3:
                quantidadeRegistros = arrayQuantidadeRegistro[2];
                tamanhoTabelaHash = arrayTamanhos[2];
                break;
            default:
                System.out.println("Opção inválida, Encerrando programa...");
        }

        // inicializar tabela
        TabelaHashEncadeada tabela = new TabelaHashEncadeada(tamanhoTabelaHash);

        // gerar dados automaticamente para inserção
        Registro[] dados = GeradorDeDados.gerar(quantidadeRegistros, semente);

        long tempoInsercaoInicial = System.nanoTime();
        // inserir dados na tabela
        for (Registro dado : dados) {
            colisoes += tabela.inserir(dado);
        }
        long tempoInsercaoFinal = System.nanoTime();
        double tempoInsercaoTotal = (tempoInsercaoFinal - tempoInsercaoInicial) / 1_000_000.0;


        // exibir colisões
        System.out.println("Total de colisões: " + colisoes);
        System.out.println("Tempo de inserção: " + tempoInsercaoTotal);

        // realizar buscas
        System.out.println("\nIniciando buscas de todos os elementos: ");

        long tempoBuscaInicial = System.nanoTime();

        for (Registro dado : dados) {
            int codigo = dado.obterCodigo();
            int encontrado = tabela.buscar(codigo);

            if (encontrado == 0) {
                System.out.println("Registro " + codigo + " não encontrado");
            }
        }

        long tempoBuscaFinal = System.nanoTime();
        double tempoBuscaTotal = (tempoBuscaFinal - tempoBuscaInicial) / 1_000_000.0;

        //

        System.out.println("Busca concluída!");
        System.out.println("Tempo total de busca, em ms: " + tempoBuscaTotal / 1_000_000.0);
        System.out.println();

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

        //gerar as 3 maiores listas encadeadas, ou seja, as que tiveram mais colisões
        Registro[] tresMaioresListas = new Registro[3];
        int[] tresMaioresListaQuantidade = {0, 0, 0};
        for(Registro dado: tabela.vetorHash){
            int colisoesTemp = 0;
            if(dado == null){
                continue; //caso houver um gap na tabela e o dado ser nulo
            }
            while(dado.obterProximo() != null){
                colisoesTemp++;
                dado = dado.obterProximo();
            }
            if(colisoesTemp > tresMaioresListaQuantidade[0]){
                int numeroTemporario = tresMaioresListaQuantidade[0];
                Registro registroTemporario = tresMaioresListas[0];

                //atualizando o array que sincroniza o tamanho da lista encadeada com sua respectiva posicao, a primeira posicao comeca o maior valor
                tresMaioresListaQuantidade[0] = colisoesTemp;
                tresMaioresListaQuantidade[2] = tresMaioresListaQuantidade[1];
                tresMaioresListaQuantidade[1] = numeroTemporario;

                tresMaioresListas[0] = dado;
                tresMaioresListas[2] = tresMaioresListas[1];
                tresMaioresListas[1] = registroTemporario;

            } else if (colisoesTemp > tresMaioresListaQuantidade[1]){

                tresMaioresListaQuantidade[2] = tresMaioresListaQuantidade[1];
                tresMaioresListaQuantidade[1] = colisoesTemp;
                tresMaioresListas[2] = tresMaioresListas[1];
                tresMaioresListas[1] = dado;
            } else if (colisoesTemp > tresMaioresListaQuantidade[2]){
                tresMaioresListaQuantidade[2] = colisoesTemp;
                tresMaioresListas[2] = dado;
            }
        }

        System.out.println("3 maiores listas encadeadas da tabela:");
        System.out.println("Primeiro lugar: " + tresMaioresListas[0] + " - " + tresMaioresListaQuantidade[0]);
        System.out.println("Segundo lugar: " + tresMaioresListas[1] + " - " + tresMaioresListaQuantidade[1]);
        System.out.println("Terceiro lugar: " + tresMaioresListas[2] + " - " + tresMaioresListaQuantidade[2]);

        // salvar metricas em CSV
        String tipoTabela = "Hash Encadeado Multiplicativo";
        String nomeArquivo = "resultado_hash_encadeado_multiplicativo.csv";

        // nomeDoArquivo, tipoTabela, tamanhoTabelaHash, quantidadeRegistros, tempoInsercaoMs, tempoBuscaMs, colisoes_totais
        GeradorCSV.salvarResultados(nomeArquivo, tipoTabela, tamanhoTabelaHash, quantidadeRegistros, tempoInsercaoTotal, tempoBuscaTotal, colisoes);
    }
}
