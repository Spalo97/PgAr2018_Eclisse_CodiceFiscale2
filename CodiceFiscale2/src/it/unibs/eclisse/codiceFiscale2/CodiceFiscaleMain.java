package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class CodiceFiscaleMain {

	public static void main(String[] args) {

		Support support = new Support(); //istanziamento oggetto di supporto
		
		support.getAllPersona(); //prelievo dei dati delle persone
		
		support.getAllCodici(); //prelievo dei codici fiscali
		
		support.getAllComuni(); //prelievo dei comuni
		
		support.creacod(); //creazione dei codici fiscali
		
		support.calcolaInvalidi(); //identificazione dei codici invalidi
		
		support.calcolaSpaiati(); //identificazione dei codici spaiati
		
		support.generaXml(); //creazione del file XML
		
		System.out.println("Tutto ok!"); //messaggio di conferma che l'operazione è andata a buon fine
		
	}

}
