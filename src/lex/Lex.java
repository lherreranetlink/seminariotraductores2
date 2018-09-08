package lex;

import java.io.FileNotFoundException;
import java.io.IOException;

import fileutils.FileManager;

public class Lex {
	
	private FileManager file_manager;

	public Lex(String filename) throws FileNotFoundException {
		this.file_manager = new FileManager(filename);
	}
	
	public Lex() {
		this.file_manager = new FileManager();
	}
	
	public void analyze() {
		try {
			
			int c;
			while ((c = file_manager.get_byte()) != -FileManager.EOF_MARK)
				System.out.println("Byte read: " + (char) c);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
