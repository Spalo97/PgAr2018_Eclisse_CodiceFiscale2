package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Support {
	
	private String filePersone="inputPersone.xml";
	private String fileCodici="codiciFiscali.xml";
	private String fileComuni="comuni.xml";
	private XMLInputFactory xmlif = null;
	private XMLStreamReader xmlr = null;
	private ArrayList<Persona> persone = new ArrayList();
	private ArrayList<String> invalidi = new ArrayList();
	//private ArrayList<String> spaiati = new ArrayList();
	private LinkedList<String> codiciImportati = new LinkedList(); //utilizzata anche per spaiati, Lange toglie invalidi e li mette in altro array, Nick toglie i validi e restano spaiati!
	
	private int id;
	private String nome;
	private String cognome;
	private char sesso;
	private String comune_nascita;
	private String data_nascita;
 	
	private Persona p=new Persona();
	
	public Support(){
	}
	
	/*public XMLStreamReader openFile(XMLInputFactory xmlif) {
		
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(filePersone, new FileInputStream(filePersone));
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
		
		return this.xmlr;
	}*/
	
	public void getAllPersona() {
		
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(filePersone, new FileInputStream(filePersone));
			while (xmlr.hasNext()) { // continua a leggere finché ha eventi a disposizione
				switch (xmlr.getEventType()) { // switch sul tipo di evento
					case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
						break;
					case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
						for (int i = 0; i < xmlr.getAttributeCount(); i++) {
							switch(xmlr.getAttributeLocalName(i)) {
							case "persona": break;
							case "nome": 	p.setNome(xmlr.getAttributeValue(i));
											break;
							case "cognome": p.setCognome(xmlr.getAttributeValue(i));
											break;
							case "sesso":	p.setSesso(xmlr.getAttributeValue(i));
											break;
							case "comune_nascita":  p.setComune_nascita(xmlr.getAttributeValue(i));
													break;
							case "data_nascita":	p.setData_nascita(xmlr.getAttributeValue(i));
													if(!persone.add(p)) {
														System.out.println("Errore!");
													}
													break;
							}
						}
						break;
					case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
						//System.out.println("Fine lettura ed aggiunta dati da " + xmlr.getLocalName()); break;
					case XMLStreamConstants.COMMENT:
						System.out.println("// commento " + xmlr.getText()); break; // commento: ne stampa il contenuto
					case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
						if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
						//System.out.println("-> " + xmlr.getText());
						break;
					}
				
				
				xmlr.next();
			}
		} catch (XMLStreamException e) {
			System.out.println("Errore nella lettura del file " + filename);
			System.out.println(e.getMessage());
		}
	}
	
	public void getAllCodici() {
		
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(fileCodici, new FileInputStream(fileCodici));
			while (xmlr.hasNext()) { 
				switch (xmlr.getEventType()) { 
					case XMLStreamConstants.START_DOCUMENT:
						System.out.println("Inizio lettura codici fiscali");
						break;
					case XMLStreamConstants.START_ELEMENT: 
						for (int i = 0; i < xmlr.getAttributeCount(); i++) {
									
							}
						}
						break;
					case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
						//System.out.println("Fine lettura ed aggiunta dati da " + xmlr.getLocalName()); break;
					case XMLStreamConstants.COMMENT:
						System.out.println("// commento " + xmlr.getText()); break; // commento: ne stampa il contenuto
					case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
						if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
						//System.out.println("-> " + xmlr.getText());
						break;
					}
				
				
				xmlr.next();
			}
		} catch (XMLStreamException e) {
			System.out.println("Errore nella lettura del file " + filename);
			System.out.println(e.getMessage());
		}
	}
	
	public void test() {
		int i=(int) (Math.random()*1000);
		Persona c=persone.get(i);
		String s= c.getNome();
		System.out.println("Il nome del numero "+i+ " è "+s);
	}
}
