package it.unibs.eclisse.codiceFiscale2;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class CodiceFiscaleMain {

	public static void main(String[] args) {
		
		Support support = new Support();
		
		support.getAllPersona();
		
		support.getAllCodici();
		
		support.getAllComuni();
		
		support.test();
		
		System.out.println("Tutto ok!");
		
	}

}
