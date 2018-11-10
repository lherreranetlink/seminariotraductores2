package mainpackage;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import fileutils.FileManager;
import parser.Parser;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;

public class MainClass extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame parent = this;
	private JLabel lblFileSelected;
	JTextArea txtInput, txtLog;
	private FileManager file_manager, error_log;

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
		
		txtInput = new JTextArea();
		txtInput.setEditable(false);
		txtLog = new JTextArea();
		txtLog.setEditable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 605, 612);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblFileSelected = new JLabel("No file selected");
		lblFileSelected.setFont(new Font("UnYetgul", Font.BOLD, 12));
		lblFileSelected.setBounds(447, 81, 131, 25);
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
						lblFileSelected.setText(selectedFile.getAbsolutePath());
						error_log = new FileManager("error_log", true);
						Parser parser = new Parser(selectedFile.getAbsolutePath(), error_log);
						file_manager = new FileManager(selectedFile.getAbsolutePath());
						showInput();
						parser.parse();
						printErrors();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(298, 81, 131, 25);
		contentPane.add(btnNewButton);
		
		JLabel lblInput = new JLabel("Input:");
		lblInput.setFont(new Font("UnYetgul", Font.BOLD, 18));
		lblInput.setBounds(31, 74, 115, 36);
		contentPane.add(lblInput);
		
		JLabel lblMessages = new JLabel("Messages:");
		lblMessages.setFont(new Font("UnYetgul", Font.BOLD, 18));
		lblMessages.setBounds(37, 383, 155, 30);
		contentPane.add(lblMessages);
		
		JLabel lblNewLabel = new JLabel("El compiLador del Roger");
		lblNewLabel.setFont(new Font("UnYetgul", Font.BOLD, 25));
		lblNewLabel.setBounds(143, 0, 343, 46);
		contentPane.add(lblNewLabel);
		
		JScrollPane sp = new JScrollPane(txtInput);
		sp.setBounds(35, 118, 530, 253);
		contentPane.add(sp);
		
		JScrollPane sp_1 = new JScrollPane(txtLog);
		sp_1.setBounds(47, 425, 518, 134);
		contentPane.add(sp_1);
	}
	
	private void showInput() {
		try {
			String inputContents = file_manager.get_file_contents();
			txtInput.setText(inputContents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printErrors() {
		try {
			this.error_log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
