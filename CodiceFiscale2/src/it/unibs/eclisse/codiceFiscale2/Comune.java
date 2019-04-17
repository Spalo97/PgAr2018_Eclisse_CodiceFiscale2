package it.unibs.eclisse.codiceFiscale2;

public class Comune {

    private String nome; // attributo nome comune
    private String codice; //attributo codice, è alfanumerico perciò uso String

    public Comune() {
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome)  {
    	this.nome=nome;
    }

    public String getCodice() {
        return codice;
    }
    
    public void setCodice(String codice) {
    	this.codice=codice;
    }
}
