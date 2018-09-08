package fileutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {
	
	private String file_name;
	private FileInputStream input_stream;
	public static int EOF_MARK = -1;
	
	public FileManager() {
		try {
			this.file_name = "default";
			this.input_stream = new FileInputStream(file_name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public FileManager(String fileName) throws FileNotFoundException {
		this.file_name = fileName;
		this.input_stream = new FileInputStream(file_name);
	}
	
	public int get_byte() throws IOException {
		return this.input_stream.read();
	}
	
	
}
