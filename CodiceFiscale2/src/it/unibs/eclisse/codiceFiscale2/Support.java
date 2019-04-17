package it.unibs.eclisse.codiceFiscale2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.stream.*;

public class Support {
	
	private String filePersone="inputPersone.xml";
	private String fileCodici="codiciFiscali.xml";
	private String fileComuni="comuni.xml";
	private String output = "codiciPersone.xml";
	private XMLInputFactory xmlif = null;
	private XMLStreamReader xmlr = null;
	private XMLOutputFactory xmlof = null;
	private XMLStreamWriter xmlw = null;
	private ArrayList<Persona> persone = new ArrayList();
	private ArrayList<Comune> comuni = new ArrayList();
	private ArrayList<String> invalidi = new ArrayList();
	private ArrayList<String> spaiati = new ArrayList();
	private LinkedList<String> codiciImportati = new LinkedList(); //utilizzata anche per spaiati, Lange toglie invalidi e li mette in altro array, Nick toglie i validi e restano spaiati!
	
	
	private int anno;
	private int mese;
	private int giorno;
	
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
			
			while (xmlr.hasNext()) {								
				if(xmlr.getEventType()==XMLStreamConstants.START_ELEMENT) {
					switch(xmlr.getLocalName()) {
					case "persona":
						Persona p = new Persona();
						persone.add(p);
						persone.get(persone.size()-1).setId(xmlr.getAttributeValue(0));
						break;
					case "nome":
						persone.get(persone.size()-1).setNome(xmlr.getElementText());
						break;
					case "cognome":
						persone.get(persone.size()-1).setCognome(xmlr.getElementText());
						break;
					case "sesso":
						persone.get(persone.size()-1).setSesso(xmlr.getElementText());
						break;
					case "comune_nascita":
						persone.get(persone.size()-1).setComune_nascita(xmlr.getElementText());
						break;	
					case "data_nascita":
						persone.get(persone.size()-1).setData_nascita(xmlr.getElementText());
						break;
					}
				}
				xmlr.next();
				}
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
	}
	
	public void getAllCodici() {
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(fileCodici, new FileInputStream(fileCodici));
			
			while (xmlr.hasNext()) {								
				if(xmlr.getEventType()==XMLStreamConstants.CHARACTERS) {
					
					System.out.println(xmlr.getText());
				}
				xmlr.next();
				}
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
	}
	
	public void getAllComuni() {
		
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(fileComuni, new FileInputStream(fileComuni));
			
			while (xmlr.hasNext()) {								
				if(xmlr.getEventType()==XMLStreamConstants.START_ELEMENT) {
					switch(xmlr.getLocalName()) {
					case "comune":
						Comune c = new Comune();
						comuni.add(c);
						break;
					case "nome":
						comuni.get(comuni.size()-1).setNome(xmlr.getElementText());
						break;
					case "codice":
						comuni.get(comuni.size()-1).setCodice(xmlr.getElementText());
						break;
					}
				}
				xmlr.next();
				}
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
	}
	
	public void test() {
		int i=(int) (Math.random()*1000);
		Persona p=persone.get(i);
		System.out.println(p.getId());
		System.out.println(p.getNome());
		System.out.println(p.getCognome());
		System.out.println(p.getSesso());
		System.out.println(p.getComune_nascita());
		System.out.println(p.getData_nascita());
		
		int j=(int) (Math.random()*20);
		Comune c=comuni.get(j);
		System.out.println(c.getNome());
		System.out.println(c.getCodice());
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

	public void generaXml() {
		try {
			xmlof = XMLinputFactory.newInstance();
			xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(output), "utf-8");
			xmlw.writeStartDocument("utf-8", "1.0");
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del writer:");
			System.out.println(e.getMessage());
		}

		try {
			xmlw.writeStartElement("output");
			xmlw.writeComment("INIZIO LISTA");
			xmlw.writeStartElement("persone");
			xmlw.writeAttribute("numero", persone.size());
			for (int i = 0; i < persone.size(); i++) {
				xmlw.writeStartElement("persona");
				xmlw.writeAttribute("id", persone.get(i).getId());

				xmlw.writeStartElement("nome");
				xmlw.writeCharacters(persone.get(i).getNome());
				xmlw.writeEndElement();

				xmlw.writeStartElement("cognome");
				xmlw.writeCharacters(persone.get(i).getCognome());
				xmlw.writeEndElement();

				xmlw.writeStartElement("sesso");
				xmlw.writeCharacters(persone.get(i).getSesso());
				xmlw.writeEndElement();

				xmlw.writeStartElement("comune_nascita");
				xmlw.writeCharacters(persone.get(i).getComune_nascita());
				xmlw.writeEndElement();

				xmlw.writeStartElement("data_nascita");
				xmlw.writeCharacters(persone.get(i).getData_nascita());
				xmlw.writeEndElement();

				xmlw.writeStartElement("codice_fiscale");
				xmlw.writeCharacters(persone.get(i).getCodice_fiscale());
				xmlw.writeEndElement();

				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();
			xmlw.writeStartElement("codici");
			xmlw.writeStartElement("invalidi");
			xmlw.writeAttribute("numero", invalidi.size());
			for (int i = 0; i< invalidi.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(invalidi.get(i));
				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();
			xmlw.writeStartElement("spaiati");
			xmlw.writeAttribute("numero", spaiati.size());
			for (int i = 0; i< spaiati.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(spaiati.get(i));
				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();
			xmlw.writeEndElement();
			xmlw.writeEndDocument();
			xmlw.flush();
			xmlw.close();

		} catch (Exception e) {
			System.out.println("Errore nella scrittura");
		}
	}

}
