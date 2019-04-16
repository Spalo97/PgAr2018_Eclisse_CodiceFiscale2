package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class Support {
	
	private String filename=null;
	private XMLStreamReader xmlr = null;
	
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
}
