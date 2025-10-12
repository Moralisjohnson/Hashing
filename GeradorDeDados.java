import java.util.Random;

public class GeradorDeDados {

    public static Registro[] gerar(int quantidade, long seed) {
        Registro[] dados = new Registro[quantidade];
        Random random = new Random(seed);

        System.out.println("Gerando " + quantidade + " registros\n");
        for (int i = 0; i < quantidade; i++) {
            // Gera um código de 9 dígitos entre 100.000.000 e 999.999.999
            int codigo = 100_000_000 + random.nextInt(900_000_000);
            dados[i] = new Registro(codigo);
        }
        System.out.println("Dados gerados com sucesso!");
        return dados;
    }
}