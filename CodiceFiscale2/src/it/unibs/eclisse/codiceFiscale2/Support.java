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
	

	public static String creacod(String nome, String cognome,String data , String comune, String sesso ) {	
		StringBuffer code = new StringBuffer(); 
		code.append(nome_cognome(cognome));
		code.append(nome_cognome(nome));
		code.append(cod_anno(data));
		code.append(cod_mese_giorno(data, sesso));
		return code.toString().toUpperCase();
		
		
	}
	
	public static char[] nome_cognome(String dato){
		int i=0;
		int n_lett=dato.length();
		int counter=0;
		char name[] =dato.toUpperCase().toCharArray();
		char cod[]=new char[3];
		do {
			if(name[i] != 'A' && name[i] != 'E' && name[i] != 'I' && name[i] != 'O' && name[i] != 'U' ) {
				cod[counter]=name[i];
				counter ++;
			}
			i++;
		}while(counter<3 && i<n_lett);
		i=0;
		if (counter<3) {
			do {
				if(name[i] == 'A' && name[i] == 'E' && name[i] == 'I' && name[i] == '0' && name[i] == 'U' ) {
					cod[counter]=name[i];
					counter ++;
				}
				i++;
			}while((counter<3 && i<n_lett));
		}
		if(counter<3) {
			do {
				cod[counter]='X';
				counter ++;
			}while(counter<3
					);
		}
		return cod;
	}
	

	
	public static char[] cod_anno(String data) {
		char anno[] =data.toCharArray();
		char cod[]=new char[2];
		cod[0]=anno[2];
		cod[1]=anno[3];
		return cod;
	}
	
	public  static StringBuffer cod_mese_giorno(String data, String sesso) {
		
		int mese=Integer.parseInt(data.substring(5, 7));
		int giorno=Integer.parseInt(data.substring(8, 10));
		StringBuffer cod = new StringBuffer();
		
		
		
		int giornilimite;
		switch(mese){
		case 1:
			;
			cod.append('A');
			giornilimite=31;
			break;
		case 2:
			cod.append('B');
			giornilimite=28;
			break;
		case 3:
			cod.append('C');
			giornilimite=31;
			break;
		case 4:
			cod.append('D');
			giornilimite=30;
			break;
		case 5:
			cod.append('E');
			giornilimite=31;
			break;
		case 6:
			cod.append('H');
			giornilimite=30;
			break;
		case 7:
			cod.append('L');
			giornilimite=31;
			break;
		case 8:
			cod.append('M');
			giornilimite=31;
			break;
		case 9:
			cod.append('P');
			giornilimite=30;
			break;
		case 10:
			cod.append('R');
			giornilimite=31;
			break;
		case 11:
			cod.append('S');
			giornilimite=30;
			break;
		case 12:
			cod.append('T');
			giornilimite=31;
			break;
		default:
			giornilimite=0;
			break;
		}
		if (sesso.toUpperCase() == "F") {
			giornilimite=giornilimite+40;
			giorno=giorno+40;
		}
		if(giorno<giornilimite) {
			cod.append(giorno);
			
		}
		
		return cod;
	}

	public void generaXml() {
		try {
			xmlof = XMLOutputFactory.newInstance();
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
			xmlw.writeAttribute("numero", String.valueOf(persone.size()));
			for (int i = 0; i < persone.size(); i++) {
				xmlw.writeStartElement("persona");
				xmlw.writeAttribute("id", String.valueOf(persone.get(i).getId()));

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
				boolean ctrl = true;
				for (int j=0; j<codiciImportati.size(); j++) {
				    ctrl = true;
                    if (persone.get(i).getCodice_fiscale()==codiciImportati.get(j)) {
                        xmlw.writeCharacters(persone.get(i).getCodice_fiscale());
                        ctrl = false;
                        break;
                    }
				}
				if (ctrl) {
                    xmlw.writeCharacters("ASSENTE");
				}
				xmlw.writeEndElement();
				xmlw.writeEndElement();
			}

			xmlw.writeStartElement("codici");
			xmlw.writeStartElement("invalidi");
			xmlw.writeAttribute("numero", String.valueOf(invalidi.size()));
			for (int i = 0; i< invalidi.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(invalidi.get(i));
				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();
			xmlw.writeStartElement("spaiati");
			xmlw.writeAttribute("numero", String.valueOf(spaiati.size()));
			for (int i = 0; i< spaiati.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(spaiati.get(i));
				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();
			xmlw.writeEndElement();
			xmlw.writeEndElement();
			xmlw.writeEndDocument();
			xmlw.flush();
			xmlw.close();

		} catch (Exception e) {
			System.out.println("Errore nella scrittura");
		}
	}

	public void calcolaSpaiati() {
    boolean ctrl = true;
        for (int i = 0; i < codiciImportati.size(); i++) {
            ctrl = true;
            for (int j = 0; j < persone.size(); j++) {
                if (codiciImportati.get(i)==persone.get(j).getCodice_fiscale()) {
                    ctrl = false;
                    break;
                }
            }
            if (ctrl) {
                spaiati.add(codiciImportati.get(i));
            }
        }
    }

	public void calcolaInvalidi() {
	    for (int i = 0; i < codiciImportati.size(); i++){
	        if (isInvalido(codiciImportati.get(i))){
	            invalidi.add(codiciImportati.get(i));
                codiciImportati.remove(i);
            }
        }
    }

}
