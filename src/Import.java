import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import org.w3c.dom.Document;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextField;

public class Import extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser chooser; 
	Document document = null;
	private JTextField textField;
	private File file;

	private static ArrayList<Argument> argArray = new ArrayList<Argument>();
	private static ArrayList<Relation> relArray = new ArrayList<Relation>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Import frame = new Import();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	 /**
	 * Create the frame.
	 */
	public Import() {
		chooser = new JFileChooser();
		setTitle("TEA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 671, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		textField = new JTextField();
		textField.setBounds(14, 162, 443, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		JButton btnImportXml = new JButton("Browse");
		btnImportXml.setFont(new Font("Arial", Font.PLAIN, 20));
		btnImportXml.setBounds(471, 159, 168, 39);
		btnImportXml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnImportXml.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int value = chooser.showOpenDialog(Import.this);
				chooser.setMultiSelectionEnabled(true);
				if(value == JFileChooser.APPROVE_OPTION){
					file = chooser.getSelectedFile();
					try {
						document = ProcessXML.parse(file.getAbsolutePath()); //get the path of the input xml file
						textField.setText(file.getAbsolutePath());
					} catch (DocumentException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnImportXml);
		
		
		
		
		JButton btnImport = new JButton("Next");
		btnImport.setFont(new Font("Arial", Font.PLAIN, 20));
		btnImport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				argArray = ProcessXML.getArgument(document); //construct the arrayList consists of all arguments
				relArray = ProcessXML.getRelation(document);//construct the arrayList consists of all relations
				ArrayList<Argument>argArrayCopy = Actions.copyArgArrayList(argArray);//make the copy of relations and arguments
				ArrayList<Relation>relArrayCopy = Actions.copyRelArrayList(relArray);
				String subject = ProcessXML.getSubject(document);
				String subjectSummary = ProcessXML.getSummarySubject(document);
				Framework framework = new Framework(subject, subjectSummary, argArrayCopy, relArrayCopy);
				Actions actions = new Actions(argArrayCopy,relArrayCopy,framework); 
				Import.this.dispose();
				actions.setVisible(true);
			}
		});
		btnImport.setBounds(471, 250, 168, 39);
		contentPane.add(btnImport);
		
		JLabel lblNewLabel = new JLabel("Import XML:");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(14, 113, 162, 36);
		contentPane.add(lblNewLabel);
		
	}
		
}
