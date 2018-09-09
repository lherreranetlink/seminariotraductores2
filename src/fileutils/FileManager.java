package fileutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {
	
	private String file_name;
	private FileInputStream input_stream;
	public static int EOF_MARK = -1;
	private static int BUFFSIZ = 50;
	private int buffpos;
	private char[] buffer;
	
	public FileManager() {
		try {
			this.file_name = "default";
			this.input_stream = new FileInputStream(file_name);
			this.buffer = new char[FileManager.BUFFSIZ];
			this.buffpos = -1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public FileManager(String fileName) throws FileNotFoundException {
		this.file_name = fileName;
		this.input_stream = new FileInputStream(file_name);
		this.buffer = new char[FileManager.BUFFSIZ];
		this.buffpos = -1;
	}
	
	public char get_byte() throws IOException {
		return (buffpos >= 0) ? buffer[buffpos--] : (char) this.input_stream.read(); 
	}
	
	public void ungetchar(char c) {
		this.buffer[++buffpos] = c;
	}
	
	
}
