package it.unibs.eclisse.codiceFiscale2;

public class Comune {

    private String nome; // attributo nome comune
    private String codice; //attributo codice, è alfanumerico perciò uso String

    public Comune(String nome, String codice) {
        this.nome=nome;
        this.codice=codice;
    }

    public String getNome() {
        return nome;
    }

    public String getCodice() {
        return codice;
    }
}
