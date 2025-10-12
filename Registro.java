// Classe para manipular o registro

public class Registro{
    int codigo;
    Registro proximo;

    public Registro(int codigo){
        this.codigo = codigo;
        this.proximo = null;
    }

    public int getCodigo(){
        return codigo;
    }

    public void inserirProximo(Registro proximo){
        this.proximo = proximo;
    }

    public Registro obterProximo(){
        return this.proximo;
    }
}