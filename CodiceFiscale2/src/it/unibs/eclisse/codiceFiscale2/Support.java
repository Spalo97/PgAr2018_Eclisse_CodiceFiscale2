package it.unibs.eclisse.codiceFiscale2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.stream.*;

public class Support {

	//dichiarazione degli attributi riferiti ai file XML
	private String filePersone="inputPersone.xml";
	private String fileCodici="codiciFiscali.xml";
	private String fileComuni="comuni.xml";
	private String output = "codiciPersone.xml";

	// attributi utilizzati per lavorare con i file xml (lettura e scrittura)
	private XMLInputFactory xmlif = null;
	private XMLStreamReader xmlr = null;
	private XMLOutputFactory xmlof = null;
	private XMLStreamWriter xmlw = null;
	
	private ArrayList<Persona> persone = new ArrayList(); //dichiarazione dell'array contenete i dati delle persone
	private ArrayList<Comune> comuni = new ArrayList(); //dichiarazione dell'array contenete i dati dei comuni
	private ArrayList<String> invalidi = new ArrayList(); //dichiarazione dell'array contenete i codici fiscali invalidi
	private ArrayList<String> spaiati = new ArrayList(); //dichiarazione dell'array contenete i codici fiscali spaiati
	private LinkedList<String> codiciImportati = new LinkedList(); //dichiarazione dell'array contenete i codici fiscali importati

	//dichiarazione degli attributi per la data
	private int anno;
	private int mese;
	private int giorno;

	//costruttore dell'oggetto Support
	public Support(){
	}

	//metodo per il prelievo dei dati persone
	public void getAllPersona() {
		
		try {
			xmlif = XMLInputFactory.newInstance(); //si istanzia la variabile per la lettura dei dati
			xmlr = xmlif.createXMLStreamReader(filePersone, new FileInputStream(filePersone)); //assegnamento del file da leggere alla variabile xmlr
			
			while (xmlr.hasNext()) {	//finche c'è da leggere, rimane nel loop
				if(xmlr.getEventType()==XMLStreamConstants.START_ELEMENT) { //se viene letto la costante di inizio
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

	//metodo che preleva i codici dal file xml dei codici fiscali
	public void getAllCodici() {
		try {
			xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(fileCodici, new FileInputStream(fileCodici));
			
			while (xmlr.hasNext()) {								
				if(xmlr.getEventType()==XMLStreamConstants.START_ELEMENT) {		//perché se uso CHARACTERS e getText() mi crea righe di spazi vuoti?
					if(xmlr.getLocalName().equals("codice"))							//perché se non uso getLocalName non posso fare getElementtext?
						codiciImportati.add(xmlr.getElementText());
				}
				xmlr.next();
				}
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
	}

	//metodo che preleva il nome del comune e il codice del comune dal file xml dei codici fiscali
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


	//metodo che crea una buffer di stringhe contenente il codice fiscale per ogni persona
	public void creacod() {
		
		for(int i=0;i<persone.size();i++) {
			StringBuffer code = new StringBuffer(); 
			code.append(nome_cognome(persone.get(i).getCognome()));	
			code.append(nome_cognome(persone.get(i).getNome()));	
			code.append(cod_anno(persone.get(i).getData_nascita()));
			code.append(cod_mese_giorno(persone.get(i).getData_nascita(), persone.get(i).getSesso()));
			code.append(cod_com(persone.get(i).getComune_nascita()));
			code.append(car_controllo(code.toString()));
			persone.get(i).setCodice_fiscale(code.toString().toUpperCase());
		}
	}

	//metodo che crea il codice del nome e cognome da aggiungere al codice fiscale
	public static char[] nome_cognome(String dato){
		int i=0;
		int n_lett=dato.length();
		int counter=0;
		char name[] =dato.toUpperCase().toCharArray();
		char cod[]=new char[3];
		while(counter<3 && i<n_lett){
			if(name[i] != 'A' && name[i] != 'E' && name[i] != 'I' && name[i] != 'O' && name[i] != 'U' ) {
				cod[counter]=name[i];
				counter ++;
			}
			i++;
		}
		i=0;
		if (counter<3) {
			while((counter<3 && i<n_lett)) {
				if(name[i] == 'A' || name[i] == 'E' || name[i] == 'I' || name[i] == '0' || name[i] == 'U' ) {
					cod[counter]=name[i];
					counter ++;
				}
				i++;
			}
		}
		
		if(counter<3) {
			while(counter<3) {
				cod[counter]='X';
				counter ++;
			}
		}
		return cod;
	}

	//metodo che crea il codice per l'anno da aggiungere al codice fiscale
	public static char[] cod_anno(String data) {
		char anno[] =data.toCharArray();
		char cod[]=new char[2];
		cod[0]=anno[2];
		cod[1]=anno[3];
		return cod;
	}

	//metodo che crea il codice da aggiungere al codice fiscale per il mese e il giorno in base al sesso della persona
public  static StringBuffer cod_mese_giorno(String data, String sesso) {
		
		int anno=Integer.parseInt(data.substring(0, 4));
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
			if(anno%4==0) {
				giornilimite=29;
			}else {
				giornilimite=28;
			}
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
		if (sesso.equals("F")) {
			giornilimite=giornilimite+40;
			giorno=giorno+40;
		}
		if(giorno<=giornilimite) {
			if(giorno<10) {
				cod.append(0);
				cod.append(giorno);
			}else {
				cod.append(giorno);
			}
		}
		return cod;
	}

	//metodo che crea il codice da aggiungere al codice fiscale del comune di nascita
	public  StringBuffer cod_com(String com){
		StringBuffer cod=new StringBuffer();
		for(int i=0; i<comuni.size(); i++) {
			
			if (comuni.get(i).getNome().equals(com)) {
				cod.append(comuni.get(i).getCodice());
				return cod;
			}
		}
		
		System.out.println(cod);
		return cod;
	}

	//metodo che restituisce il codice di controllo del codice fiscale da aggiungere al codice fiscale
	public String car_controllo(String code) {
		char cod[] =code.toUpperCase().toCharArray();
		int somma=0;
		int controllo;
		String cod_contr = null;
		for(int i=0, n=1; i<15; i++, n++){
			if (n%2==0) {
				switch (cod[i]) {
				case '0': {somma+=0;break;}
				case '1': {somma+=1;break;}
				case '2': {somma+=2;break;}
				case '3': {somma+=3;break;}
				case '4': {somma+=4;break;}
				case '5': {somma+=5;break;}
				case '6': {somma+=6;break;}
				case '7': {somma+=7;break;}
				case '8': {somma+=8;break;}
				case '9': {somma+=9;break;}
				case 'A': {somma+=0;break;}
				case 'B': {somma+=1;break;}
				case 'C': {somma+=2;break;}
				case 'D': {somma+=3;break;}
				case 'E': {somma+=4;break;}
				case 'F': {somma+=5;break;}
				case 'G': {somma+=6;break;}
				case 'H': {somma+=7;break;}
				case 'I': {somma+=8;break;}
				case 'J': {somma+=9;break;}
				case 'K': {somma+=10;break;}
				case 'L': {somma+=11;break;}
				case 'M': {somma+=12;break;}
				case 'N': {somma+=13;break;}
				case 'O': {somma+=14;break;}
				case 'P': {somma+=15;break;}
				case 'Q': {somma+=16;break;}
				case 'R': {somma+=17;break;}
				case 'S': {somma+=18;break;}
				case 'T': {somma+=19;break;}
				case 'U': {somma+=20;break;}
				case 'V': {somma+=21;break;}
				case 'W': {somma+=22;break;}
				case 'X': {somma+=23;break;}
				case 'Y': {somma+=24;break;}
				case 'Z': {somma+=25;break;}	
				}
			}else {
				switch (cod[i]) {
				case '0': {somma+=1;break;}
				case '1': {somma+=0;break;}
				case '2': {somma+=5;break;}
				case '3': {somma+=7;break;}
				case '4': {somma+=9;break;}
				case '5': {somma+=13;break;}
				case '6': {somma+=15;break;}
				case '7': {somma+=17;break;}
				case '8': {somma+=19;break;}
				case '9': {somma+=21;break;}
				case 'A': {somma+=1;break;}
				case 'B': {somma+=0;break;}
				case 'C': {somma+=5;break;}
				case 'D': {somma+=7;break;}
				case 'E': {somma+=9;break;}
				case 'F': {somma+=13;break;}
				case 'G': {somma+=15;break;}
				case 'H': {somma+=17;break;}
				case 'I': {somma+=19;break;}
				case 'J': {somma+=21;break;}
				case 'K': {somma+=2;break;}
				case 'L': {somma+=4;break;}
				case 'M': {somma+=18;break;}
				case 'N': {somma+=20;break;}
				case 'O': {somma+=11;break;}
				case 'P': {somma+=3;break;}
				case 'Q': {somma+=6;break;}
				case 'R': {somma+=8;break;}
				case 'S': {somma+=12;break;}
				case 'T': {somma+=14;break;}
				case 'U': {somma+=16;break;}
				case 'V': {somma+=10;break;}
				case 'W': {somma+=22;break;}
				case 'X': {somma+=25;break;}
				case 'Y': {somma+=24;break;}
				case 'Z': {somma+=23;break;}
				}
			}
			
		}
		controllo=somma%26;
		switch(controllo) {
			case 0:{cod_contr="A";break;}
			case 1:{cod_contr="B";break;}
			case 2:{cod_contr="C";break;}
			case 3:{cod_contr="D";break;}
			case 4:{cod_contr="E";break;}
			case 5:{cod_contr="F";break;}
			case 6:{cod_contr="G";break;}
			case 7:{cod_contr="H";break;}
			case 8:{cod_contr="I";break;}
			case 9:{cod_contr="J";break;}
			case 10:{cod_contr="K";break;}
			case 11:{cod_contr="L";break;}
			case 12:{cod_contr="M";break;}
			case 13:{cod_contr="N";break;}
			case 14:{cod_contr="O";break;}
			case 15:{cod_contr="P";break;}
			case 16:{cod_contr="Q";break;}
			case 17:{cod_contr="R";break;}
			case 18:{cod_contr="S";break;}
			case 19:{cod_contr="T";break;}
			case 20:{cod_contr="U";break;}
			case 21:{cod_contr="V";break;}
			case 22:{cod_contr="W";break;}
			case 23:{cod_contr="X";break;}
			case 24:{cod_contr="Y";break;}
			case 25:{cod_contr="Z";break;}
		}
		return cod_contr;
	}

	//metodo che si occupa della creazione del file XML
	public void generaXml() {
		
		int depth=0;
		
		try {
			xmlof = XMLOutputFactory.newInstance();
			xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(output), "utf-8");
			xmlw.writeStartDocument("UTF-8", "1.0");
			addDepth(xmlw,depth);
			xmlw.writeStartElement("output");
			depth++;
			xmlw.writeComment("INIZIO LISTA");
			addDepth(xmlw,depth);
			xmlw.writeStartElement("persone");
			depth++;
			xmlw.writeAttribute("numero", String.valueOf(persone.size()));
			for (int i = 0; i < persone.size(); i++) {
				addDepth(xmlw,depth);
				xmlw.writeStartElement("persona");
				depth++;
				xmlw.writeAttribute("id", String.valueOf(persone.get(i).getId()));
				addDepth(xmlw,depth);

				xmlw.writeStartElement("nome");
				xmlw.writeCharacters(persone.get(i).getNome());
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
				xmlw.writeStartElement("cognome");
				xmlw.writeCharacters(persone.get(i).getCognome());
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
				xmlw.writeStartElement("sesso");
				xmlw.writeCharacters(persone.get(i).getSesso());
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
				xmlw.writeStartElement("comune_nascita");
				xmlw.writeCharacters(persone.get(i).getComune_nascita());
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
				xmlw.writeStartElement("data_nascita");
				xmlw.writeCharacters(persone.get(i).getData_nascita());
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
				xmlw.writeStartElement("codice_fiscale");
				boolean ctrl = true;
				for (int j=0; j<codiciImportati.size(); j++) {
				    ctrl = true;
                    if (persone.get(i).getCodice_fiscale().equals(codiciImportati.get(j))) {
                        xmlw.writeCharacters(persone.get(i).getCodice_fiscale());
                        ctrl = false;
                        break;
                    }
				}
				if (ctrl) {
                    xmlw.writeCharacters("ASSENTE");
				}
				xmlw.writeEndElement();
				depth--;
				addDepth(xmlw,depth);
				xmlw.writeEndElement();
				addDepthClosePersona(xmlw,depth);
			}
			
			depth--;
			addDepth(xmlw,depth);
			xmlw.writeEndElement();
			addDepth(xmlw,depth);
			xmlw.writeStartElement("codici");
			depth++;
			addDepth(xmlw,depth);
			xmlw.writeStartElement("invalidi");
			xmlw.writeAttribute("numero", String.valueOf(invalidi.size()));
			depth++;
			addDepth(xmlw,depth);
			for (int i = 0; i< invalidi.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(invalidi.get(i));
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
			}
			xmlw.writeEndElement();
			depth--;
			addDepth(xmlw,depth);
			xmlw.writeStartElement("spaiati");
			xmlw.writeAttribute("numero", String.valueOf(spaiati.size()));
			depth++;
			addDepth(xmlw,depth);
			for (int i = 0; i< spaiati.size(); i++) {
				xmlw.writeStartElement("codice");
				xmlw.writeCharacters(spaiati.get(i));
				xmlw.writeEndElement();
				addDepth(xmlw,depth);
			}
			xmlw.writeEndElement();
			depth--;
			addDepth(xmlw,depth);
			xmlw.writeEndElement();
			depth--;
			addDepth(xmlw,depth);
			xmlw.writeEndElement();
			xmlw.writeEndDocument();
			xmlw.flush();
			xmlw.close();

		} catch (Exception e) {
			System.out.println("Errore nella scrittura");
		}
	}

	//metodo che controlla quali codici sono validi ma non corrispondono a nessuna persona
	public void calcolaSpaiati() {
    boolean ctrl = true;
        for (int i = 0; i < codiciImportati.size(); i++) {
            ctrl = true;
            for (int j = 0; j < persone.size(); j++) {
               if (codiciImportati.get(i).equals(persone.get(j).getCodice_fiscale())) {
                    ctrl = false;
                    break;
                }
            }
            if (ctrl) {
                spaiati.add(codiciImportati.get(i));
            }
        }
    }

	//metodo che verifica quali codici non sono validi
	public void calcolaInvalidi() {
	    for (int i = 0; i < codiciImportati.size(); i++){
	        if (isInvalido(codiciImportati.get(i))){
	            invalidi.add(codiciImportati.get(i));
                codiciImportati.remove(i);
            }
        }
    }

    //metdo che stabilisce se il codice dato è valido oppure no
	public boolean isInvalido(String codice){
		if (codice.length()!=16) {
			return true;
		}
		int giorno=Integer.parseInt(codice.substring(9, 11));
		String mese=codice.substring(8, 9);
		String cod_comune=codice.substring(11, 15);
		char cod[]= codice.toCharArray();
		int i;
		for(i=0;i<6;i++) {
			if(cod[i]<'a' && cod[i]>'Z') {
				return true;
			}
		}
		for(i=7;i<9;i++) {
			if (cod[i]>='a' && cod[i]<='Z') {
				return true;
			}
		}
		
		int giornilimite;
		switch(mese){
			case "A":
				giornilimite=31;
				break;
			case "B":
				giornilimite=28;
				break;
			case "C":
				giornilimite=31;
				break;
			case "D":
				giornilimite=30;
				break;
			case "E":
				giornilimite=31;
				break;
			case "H":
				giornilimite=30;
				break;
			case "L":
				giornilimite=31;
				break;
			case "M":
				giornilimite=31;
				break;
			case "P":
				giornilimite=30;
				break;
			case "R":
				giornilimite=31;
				break;
			case "S":
				giornilimite=30;
				break;
			case "T":
				giornilimite=31;
				break;
			default:
				giornilimite=0;
				return true;
				
			}
		giorno=giorno+40;
		giornilimite=giornilimite+40;
		if (giorno>giornilimite) {
			return true;
		}
		int verifica=0;
		for(Comune c:comuni) {
			if (c.getCodice().equals(cod_comune)) {
				verifica=1;
			}
		}
		if (verifica==0) {
			return true;
		}
		if (codice.substring(15).equals(car_controllo(codice))) {
			return false;
		}else {
			return true;
		}
	}

	//metodo utile per l'indentazione del file XML, aggiunge "a capo"
	private void addDepth(XMLStreamWriter xmlw, int depth) {
		try {
			xmlw.writeCharacters("\n");
			for(int x=0; x<depth; x++) {
		   		xmlw.writeCharacters("    ");
		   	}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	//altro metodo utile per l'indentazione
	private void addDepthClosePersona(XMLStreamWriter xmlw, int depth) {
		try {
			for(int x=0; x<depth; x++) {
		   		xmlw.writeCharacters("    ");
		   	}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}	

