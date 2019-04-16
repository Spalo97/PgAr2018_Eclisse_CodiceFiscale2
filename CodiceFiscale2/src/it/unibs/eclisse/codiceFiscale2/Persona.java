package it.unibs.eclisse.codiceFiscale2;

public class Persona {
	
	private int id;
	private String nome;
	private String cognome;
	private char sesso;
	private String comune_nascita;
	private String data_nascita;
	
	public Persona() {
		
	}
	
	public Persona(int id,String firstName,String lastName,char gender,String birthplace,String birthday) {
		this.id=id;
		this.nome=firstName;
		this.cognome=lastName;
		this.sesso=gender;
		this.comune_nascita=birthplace;
		this.data_nascita=birthday;
	}
	
	public String getFirstName() {
		return nome;
	}
}
