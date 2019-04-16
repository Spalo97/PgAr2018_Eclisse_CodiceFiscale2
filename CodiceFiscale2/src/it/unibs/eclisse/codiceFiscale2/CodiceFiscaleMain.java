package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class CodiceFiscaleMain {

	public static void main(String[] args) {
		
		final String filename = "inputPersone.xml";
		
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		Support support = new Support(filename);
		
		xmlif = XMLInputFactory.newInstance();
		
		xmlr = support.openFile(xmlif);
		
		System.out.println("Tutto ok!");
	}

}
