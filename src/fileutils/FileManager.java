package fileutils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
	
	private String file_name;
	private FileInputStream input_stream;
	private BufferedReader buffered_reader;
	public static int EOF_MARK = 65535;
	public static int BUFFSIZ = 50;
	private int buffpos;
	private char[] buffer;
	
	public FileManager() {
		try {
			this.file_name = "default";
			this.input_stream = new FileInputStream(file_name);
			this.buffered_reader = new BufferedReader( new FileReader(file_name));
			this.buffer = new char[FileManager.BUFFSIZ];
			this.buffpos = -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public FileManager(String fileName) throws FileNotFoundException {
		this.file_name = fileName;
		this.input_stream = new FileInputStream(file_name);
		this.buffered_reader = new BufferedReader( new FileReader(file_name));
		this.buffer = new char[FileManager.BUFFSIZ];
		this.buffpos = -1;
	}
	
	public String get_file_contents() throws IOException {
		
		String contents = "";
		String line = "";
		for (line = this.get_line(); line != null; line = this.get_line()) 
			contents += line + "\n";
		
		this.buffered_reader.close();
		
		return contents ;
	}
	
	public char get_byte() throws IOException {
		return (buffpos >= 0) ? buffer[buffpos--] : (char) this.input_stream.read(); 
	}
	
	public void ungetchar(char c) {
		this.buffer[++buffpos] = c;
	}
	
	public String get_line() throws IOException {
		return buffered_reader.readLine();
	}
	
	public void close() throws IOException {
		this.buffered_reader.close();
	}
	
	
}
