package mainpackage;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lex.Constants;
import lex.Lex;
import lex.Token;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MainClass extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame parent = this;
	private Lex lexicalAnalyzer;
	private JLabel lblFileSelected;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClass frame = new MainClass();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainClass() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 541, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblFileSelected = new JLabel("No file selected");
		lblFileSelected.setBounds(156, 12, 275, 25);
		contentPane.add(lblFileSelected);
		
		JButton btnNewButton = new JButton("Choose file");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				
				int result = fileChooser.showOpenDialog(parent);
				
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				    lblFileSelected.setText(selectedFile.getAbsolutePath());
				    lexicalAnalyzer = new Lex(selectedFile.getAbsolutePath());
				    Token test;
				    while ((test = lexicalAnalyzer.getTokenFromFile()).key != Constants.EOF_SIGN)
				    	System.out.println("Lala: " + test.value);
				    
				}
				} catch (FileNotFoundException ex) {
					JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(12, 12, 131, 25);
		contentPane.add(btnNewButton);
	}
}
