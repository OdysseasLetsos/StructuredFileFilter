package application.naive.client;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JSeparator;
import java.awt.CardLayout;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.event.ListSelectionListener;

import metadata.MetadataManagerInterface;

import javax.swing.event.ListSelectionEvent;
import javax.swing.JTextArea;

public class NaiveApplicationMainUI {

	private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu, filterMenu;
    private JMenuItem registerFileMenuItem, metadataMenuItem, createFilterMenuItem;
    
    private NaiveApplicationController applicationController;
    private static int documentAlias;
    private JLabel lblNewLabel;
    private JSeparator separator;
    private JList registeredFilesList;
    private DefaultListModel<String> filesModel;
    private JLabel lblMetadata;
    private JPanel panel_2;
    private JSeparator separator_1;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel aliasLabel;
    private JLabel fileNameLabel;
    private JLabel pathLabel;
    private JTextArea columnsInfoTextArea;
    private JLabel lblNewLabel_3;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NaiveApplicationMainUI window = new NaiveApplicationMainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NaiveApplicationMainUI() {
		applicationController = new NaiveApplicationController();
		documentAlias = 1;
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		// create a frame
        frame = new JFrame("Menu demo");
  
        filesModel = new DefaultListModel<>();
        
        // create a menubar
        menuBar = new JMenuBar();
  
        // create a menu
        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
  
        // create menuitems
        registerFileMenuItem = new JMenuItem("Register File");
        registerFileMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerFileMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int chooseFileResult = fileChooser.showOpenDialog(frame);
        		if (chooseFileResult == JFileChooser.APPROVE_OPTION) {
        			File selectedFile = fileChooser.getSelectedFile();
        			int approveFileRegister = JOptionPane.showConfirmDialog(null, "Are you sure you want to register file \n"+selectedFile.getAbsolutePath()+ "\ninto the system ?", 
        					"Approve File Registration", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        			if (approveFileRegister == 0) {
        				String validGivenDelimeter = giveValidDelimeter();
        				
        				// Register file into the system by calling NaiveApplicationController's method registerFile
        				applicationController.registerFile("DOC"+documentAlias, selectedFile.getAbsolutePath(), validGivenDelimeter);

        				// Update JList with the registered file
        				filesModel.add(filesModel.size(), "DOC"+documentAlias);
        				registeredFilesList.setModel(filesModel);
        				
        				// Generate next Alias
        				setNextDocumentAlias();
        			}
        		}
        	}
        });
        metadataMenuItem = new JMenuItem("Show Metadata");
        metadataMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  
        // add menu items to menu
        fileMenu.add(registerFileMenuItem);
        fileMenu.add(metadataMenuItem);
        
        filterMenu = new JMenu("Filtering");
        filterMenu.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        createFilterMenuItem = new JMenuItem("Create Filter");
        createFilterMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        createFilterMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Get the selected alias from the registered files and get its columns names
				String selectedFileAlias = filesModel.get(registeredFilesList.getSelectedIndex());
				String[] selectedFileColumns = applicationController.getColumnsMetadataByAlias(selectedFileAlias);

				
				
			}
        	
        });
        filterMenu.add(createFilterMenuItem);
        
        
        // add menu to menu bar
        menuBar.add(fileMenu);
        menuBar.add(filterMenu);
    
        // add menubar to frame
        frame.setJMenuBar(menuBar);
  
        // set the size of the frame
        frame.setSize(900, 550);
        frame.getContentPane().setLayout(null);
        
        JPanel filesPanel = new JPanel();
        filesPanel.setBounds(10, 53, 184, 344);
        frame.getContentPane().add(filesPanel);
        filesPanel.setLayout(null);
        
        lblNewLabel = new JLabel("REGISTERED FILES");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        lblNewLabel.setBounds(21, 11, 138, 14);
        filesPanel.add(lblNewLabel);
        
        separator = new JSeparator();
        separator.setBounds(10, 36, 164, 2);
        filesPanel.add(separator);
        
        JPanel metadataPanel = new JPanel();
        metadataPanel.setLayout(null);
        metadataPanel.setBounds(204, 91, 310, 306);
        metadataPanel.setVisible(false);
        frame.getContentPane().add(metadataPanel);
        
        registeredFilesList = new JList();
        registeredFilesList.addListSelectionListener((ListSelectionEvent e) -> {
        	if (!e.getValueIsAdjusting()) {
        		metadataPanel.setVisible(true);
        		
        		String seletedAlias = filesModel.get(registeredFilesList.getSelectedIndex());
        		aliasLabel.setText(seletedAlias);
        		
        		String[] selectedFileColumns = applicationController.getColumnsMetadataByAlias(seletedAlias);
        		for (String colName : selectedFileColumns)
        			columnsInfoTextArea.append(colName + "\n");
        	}
        });
        registeredFilesList.setBounds(10, 49, 164, 295);
        filesPanel.add(registeredFilesList);
        

        
        lblMetadata = new JLabel("METADATA");
        lblMetadata.setHorizontalAlignment(SwingConstants.CENTER);
        lblMetadata.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        lblMetadata.setBounds(93, 11, 138, 14);
        metadataPanel.add(lblMetadata);
        
        panel_2 = new JPanel();
        panel_2.setBounds(10, 43, 290, 263);
        metadataPanel.add(panel_2);
        panel_2.setLayout(null);
        
        lblNewLabel_1 = new JLabel("File Name :");
        lblNewLabel_1.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
        lblNewLabel_1.setBounds(10, 21, 68, 14);
        panel_2.add(lblNewLabel_1);
        
        lblNewLabel_2 = new JLabel("Path :");
        lblNewLabel_2.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
        lblNewLabel_2.setBounds(10, 46, 68, 14);
        panel_2.add(lblNewLabel_2);
        
        aliasLabel = new JLabel("");
        aliasLabel.setForeground(Color.RED);
        aliasLabel.setHorizontalAlignment(SwingConstants.CENTER);
        aliasLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        aliasLabel.setBounds(116, 0, 68, 14);
        panel_2.add(aliasLabel);
        
        fileNameLabel = new JLabel("");
        fileNameLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
        fileNameLabel.setBounds(88, 22, 192, 14);
        panel_2.add(fileNameLabel);
        
        pathLabel = new JLabel("");
        pathLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
        pathLabel.setBounds(88, 47, 192, 14);
        panel_2.add(pathLabel);
        
        columnsInfoTextArea = new JTextArea();
        columnsInfoTextArea.setEditable(false);
        columnsInfoTextArea.setFont(new Font("Segoe UI Historic", Font.BOLD, 13));
        columnsInfoTextArea.setWrapStyleWord(true);
        columnsInfoTextArea.setLineWrap(true);
        columnsInfoTextArea.setBounds(10, 89, 270, 174);
        panel_2.add(columnsInfoTextArea);
        
        lblNewLabel_3 = new JLabel("Columns:");
        lblNewLabel_3.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
        lblNewLabel_3.setBounds(116, 72, 68, 14);
        panel_2.add(lblNewLabel_3);
        
        separator_1 = new JSeparator();
        separator_1.setBounds(10, 36, 290, 2);
        metadataPanel.add(separator_1);
        frame.setVisible(true);
	}
	
	private void setNextDocumentAlias() {
		documentAlias++;
	}
	
	/**
	 * @return a String with the delimeter that the user had selected from the dialog box
	 */
	private String giveValidDelimeter() {
		String[] delimOptions = {"Comma (',')", "Tab (' ')", "Vertical ('|')"}; 
		int delimChooseResult = JOptionPane.showOptionDialog(frame,
				"Choose file delimeter",
				"Delimeter Options",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, delimOptions, delimOptions[0]);
		if (delimChooseResult == JOptionPane.YES_OPTION)
			return ",";
		else if (delimChooseResult == JOptionPane.NO_OPTION)
			return "\t";
		else if (delimChooseResult == JOptionPane.CANCEL_OPTION)
			return "|";
			
		return null;	
	}
	
	private void giveFilter(ArrayList<String> fileColNames) {
		String[] cols = fileColNames.toArray(new String[0]);
		
	    for (int i = 0; i <cols.length; i++) {
	        cols[i] = Integer.toString(i);
	      }
		
		JOptionPane.showInputDialog(frame,
				"Choose column to apply filter",
				"Column picker",
				JOptionPane.QUESTION_MESSAGE,
		        null, cols, "Titan");
	}
}
