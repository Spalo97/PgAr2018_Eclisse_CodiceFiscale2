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
	private int anno;
	private int mese;
	private int giorno;
	private String cod_fisc;
 	
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
	
	public void creacod(String nome, String cognome,String data , String comune, char sesso ){
		int counter=0;
		nome_cognome(cognome);
		nome_cognome(nome);
		data_nascita(data);
		cod_data(anno, mese, giorno, sesso);
		
	}
	
	public void nome_cognome(String dato){
		int i=0;
		int counter=0;
		do {
			if(dato.substring(i) != "A" && dato.substring(i) != "E" && dato.substring(i) != "I" && dato.substring(i) != "O" && dato.substring(i) != "U" ) {
				cod_fisc=cod_fisc+dato.substring(i);
				counter ++;
			}
			i++;
		}while(counter<3 || i<dato.length());
		i=0;
		if (counter<3) {
			do {
				if(dato.substring(i) == "A" && dato.substring(i) == "E" && dato.substring(i) == "I" && dato.substring(i) == "O" && dato.substring(i) == "U" ) {
					cod_fisc=cod_fisc+dato.substring(i);
					counter ++;
				}
				i++;
			}while(counter<3 || i<dato.length());
		}
		if(counter<3) {
			do {
				cod_fisc=cod_fisc+"X";
				counter ++;
			}while(counter<3);
		}
	}
	
	
	public void data_nascita(String data) {

		anno=Integer.parseInt(data.substring(0, 4));
		mese=Integer.parseInt(data.substring(6, 7));
		giorno=anno=Integer.parseInt(data.substring(9, 10));
	}
	
	public void cod_data(int anno, int mese, int giorno, char sesso) {
		int anno2c=anno%100;
		int giornilimite;
		//aggiunta dello 0 davanti ai numeri <10, Es: nati nel 2000
		
		
		switch(mese){
		case 1:
			cod_fisc=cod_fisc+"A";;
			giornilimite=31;
			break;
		case 2:
			cod_fisc=cod_fisc+"B";
			giornilimite=28;
			break;
		case 3:
			cod_fisc=cod_fisc+"C";
			giornilimite=31;
			break;
		case 4:
			cod_fisc=cod_fisc+"D";
			giornilimite=30;
			break;
		case 5:
			cod_fisc=cod_fisc+"E";
			giornilimite=31;
			break;
		case 6:
			cod_fisc=cod_fisc+"H";
			giornilimite=30;
			break;
		case 7:
			cod_fisc=cod_fisc+"L";
			giornilimite=31;
			break;
		case 8:
			cod_fisc=cod_fisc+"M";
			giornilimite=31;
			break;
		case 9:
			cod_fisc=cod_fisc+"P";
			giornilimite=30;
			break;
		case 10:
			cod_fisc=cod_fisc+"R";
			giornilimite=31;
			break;
		case 11:
			cod_fisc=cod_fisc+"S";
			giornilimite=30;
			break;
		case 12:
			cod_fisc=cod_fisc+"T";
			giornilimite=31;
			break;
		default:
			cod_fisc=cod_fisc+"";
			break;
		}
		if (sesso == 'F') {
			giornilimite=+40;
			if(giorno>giornilimite) {
				//errore
			} else cod_fisc=cod_fisc+(giorno+40);		
		}
		else {
			if(giorno>giornilimite) {
				//errore
			} else cod_fisc=cod_fisc+giorno;
		}	
	}
}
