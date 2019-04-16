package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Support {
	
	private String filename=null;
	private XMLStreamReader xmlr = null;
	private ArrayList<Persona> persone = new ArrayList();
	private ArrayList<String> invalidi = new ArrayList();
	private ArrayList<String> spaiati = new ArrayList();
	
	private int id;
	private String nome;
	private String cognome;
	private char sesso;
	private String comune_nascita;
	private String data_nascita;
 	
	public Support(String filename){
		this.filename=filename;
	}
	
	public XMLStreamReader openFile(XMLInputFactory xmlif) {
		
		try {
			this.xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
		
		return this.xmlr;
	}
	
	public void getAllFromXML() {
		
		try {
			while (xmlr.hasNext()) { // continua a leggere finché ha eventi a disposizione
				switch (xmlr.getEventType()) { // switch sul tipo di evento
					case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
						break;
					case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
						for (int i = 0; i < xmlr.getAttributeCount(); i++)
							System.out.printf(" => attributo %s->%s%n", xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));
							
						break;
					case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
						System.out.println("END-Tag " + xmlr.getLocalName()); break;
					case XMLStreamConstants.COMMENT:
						System.out.println("// commento " + xmlr.getText()); break; // commento: ne stampa il contenuto
					case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
						if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
						System.out.println("-> " + xmlr.getText());
						break;
					}
				
				
				xmlr.next();
			}
		} catch (XMLStreamException e) {
			System.out.println("Errore nella lettura del file " + filename);
			System.out.println(e.getMessage());
		}
	}
}
