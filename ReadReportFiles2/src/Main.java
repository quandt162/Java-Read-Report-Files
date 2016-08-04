import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.jws.Oneway;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import Entity.objectLog;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Main {

	private JFrame frmReadLogFiles;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel panel_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_1;
	private JTable table;
	private JTable table_1;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;
	
	DefaultTableModel dtm; 
	Vector cols = new Vector();
	Vector rows = new Vector();
	Vector rows2 = new Vector();
	List<objectLog> listobject = new LinkedList<objectLog>();
	List<objectLog> listImei;
	List<objectLog> listImeiTime;
	List<objectLog> listTime;
	private JLabel lblNumberRecords;
	private JLabel lblNewLabel_2;
	JButton btnSearch;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmReadLogFiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		
	    cols.addElement("TIME");
	    cols.addElement("IMEI");
	    cols.addElement("IP");
	    table_1.getTableHeader().setReorderingAllowed(false);
	    table_1.getTableHeader().setReorderingAllowed(false);
	    table_1.getTableHeader().setResizingAllowed(false);
	    
		//t1.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ })
	private void initialize() {
		frmReadLogFiles = new JFrame();
		frmReadLogFiles.setTitle("Read Log Files");
		frmReadLogFiles.setResizable(false);
		frmReadLogFiles.setBounds(100, 100, 1039, 627);
		frmReadLogFiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.setBounds(823, 32, 68, 26);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("applocker", "applocker");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				    textField.setText(chooser.getSelectedFile().toString());
				    try {
						readData(textField.getText());
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
			//	if(!textPane_2.getText().equals("")){
					lblNewLabel_1.setText("");
					lblNewLabel_2.setText("" + table_1.getRowCount());
			//	}
			}
		});
		
		textField = new JTextField();
		textField.setBounds(174, 29, 615, 30);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(new File(textField.getText().toString()).exists()){
						try {
							readData(textField.getText());
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else{
			//			textPane_2.setText("");
					}
				//	if(!textPane_2.getText().equals("")){
						lblNewLabel_1.setText("");
						lblNewLabel_2.setText("" + table_1.getRowCount());
				//	}
				}
			}
		});
	
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(19, 92, 484, 470);
		
		JLabel lblNewLabel = new JLabel("Enter Path");
		lblNewLabel.setBounds(79, 29, 85, 27);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		textField_1 = new JTextField();
		textField_1.setBounds(590, 463, 142, 30);
		textField_1.setToolTipText("imei");
		textField_1.setHorizontalAlignment(SwingConstants.LEFT);
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_1.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(900, 463, 91, 29);
		btnSearch.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				((DefaultTableModel)table.getModel()).setNumRows(0);
				
				if(table_1.getRowCount() <1){
					lblNewLabel_1.setText("Database Null");
					return;
				}
				if(isNumeric(textField_1.getText()) == true || textField_1.getText().equals("") ){	
					lblNewLabel_1.setText("");
					if(!textField_1.getText().equals("") && textField_2.getText().equals("")){
						listImei = findObjectByImei(textField_1.getText());
						for(objectLog obj : listImei){
							Vector dataObject = new Vector<>();
							dataObject.addElement(obj.getTime());
							dataObject.addElement(obj.getImei());
							dataObject.addElement(obj.getIp());
							
							rows2.add(dataObject);
						}
						dtm = new  DefaultTableModel(rows2, cols);
						table.setModel(dtm);
					}else if(!textField_1.getText().equals("") && !textField_2.getText().equals("")){
						listImeiTime = findObjectByImeiAndTime(textField_1.getText(), textField_2.getText());
						for(objectLog obj : listImeiTime){
							Vector dataObject = new Vector<>();
							dataObject.addElement(obj.getTime());
							dataObject.addElement(obj.getImei());
							dataObject.addElement(obj.getIp());
							
							rows2.add(dataObject);
						}
						dtm = new  DefaultTableModel(rows2, cols);
						table.setModel(dtm);
					}else if(textField_1.getText().equals("") && !textField_2.getText().equals("")){
						listTime = findObjectByTime(textField_2.getText());
						for(objectLog obj : listTime){
							Vector dataObject = new Vector<>();
							dataObject.addElement(obj.getTime());
							dataObject.addElement(obj.getImei());
							dataObject.addElement(obj.getIp());
							
							rows2.add(dataObject);
						}
						dtm = new  DefaultTableModel(rows2, cols);
						table.setModel(dtm);
					}
				}
				lblNumberRecords.setText("Number record(s): " + table.getRowCount());
			}
		});
		
		panel_1 = new JPanel();
		panel_1.setBounds(530, 92, 485, 336);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		lblNewLabel_3 = new JLabel("@Copyright RAJ");
		lblNewLabel_3.setBounds(10, 573, 109, 14);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		frmReadLogFiles.getContentPane().setLayout(null);
		frmReadLogFiles.getContentPane().add(panel);
		
		table_1 = new JTable();
		table_1.setFillsViewportHeight(true);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				((DefaultTableModel)table.getModel()).setNumRows(0);
				int row = table_1.getSelectedRow();
				if(row != -1){
					textField_1.setText(table_1.getValueAt(row, 1).toString());
					List<objectLog> listImei = findObjectByImei(textField_1.getText());
					for(objectLog obj : listImei){
						Vector dataObject = new Vector<>();
						dataObject.addElement(obj.getTime());
						dataObject.addElement(obj.getImei());
						dataObject.addElement(obj.getIp());
						
						rows2.add(dataObject);
					}
					dtm = new  DefaultTableModel(rows2, cols);
					table.setModel(dtm);
					lblNumberRecords.setText("Number record(s): " + table.getRowCount());
		        }
			}
		});
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		scrollPane2 = new  JScrollPane(table_1);
		scrollPane2.setToolTipText("Data");
		panel.add(scrollPane2);
		frmReadLogFiles.getContentPane().add(panel_1);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				
				if(row != -1){
					if(textField_1.getText().equals("")){
						textField_1.setText(table.getValueAt(row,1 ).toString());
						btnSearch.doClick();
					}else{
						textField_2.setText(SplitString(table.getValueAt(row, 0).toString()));
						((DefaultTableModel)table.getModel()).setNumRows(0);
						List<objectLog> listImei = findObjectByImeiAndTime(textField_1.getText(), textField_2.getText());
						for(objectLog obj : listImei){
							Vector dataObject = new Vector<>();
							dataObject.addElement(obj.getTime());
							dataObject.addElement(obj.getImei());
							dataObject.addElement(obj.getIp());
							
							rows2.add(dataObject);
						}
						dtm = new  DefaultTableModel(rows2, cols);
						table.setModel(dtm);
						lblNumberRecords.setText("Number record(s): " + table.getRowCount());
					}
		        }
			}
		});
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		scrollPane = new  JScrollPane(table);
		scrollPane.setToolTipText("Result");
		panel_1.add(scrollPane);
		frmReadLogFiles.getContentPane().add(textField_1);
		frmReadLogFiles.getContentPane().add(btnSearch);
		frmReadLogFiles.getContentPane().add(lblNewLabel);
		frmReadLogFiles.getContentPane().add(textField);
		frmReadLogFiles.getContentPane().add(btnNewButton);
		frmReadLogFiles.getContentPane().add(lblNewLabel_3);
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(590, 439, 151, 14);
		frmReadLogFiles.getContentPane().add(lblNewLabel_1);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("Time");
		textField_2.setHorizontalAlignment(SwingConstants.LEFT);
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField_2.setColumns(10);
		textField_2.setBounds(755, 463, 136, 30);
		frmReadLogFiles.getContentPane().add(textField_2);
		
		JButton btnExportData = new JButton("Export Data");
		btnExportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveTable(table_1,"data.txt");
					if(saveTable(table_1,"data.txt") == true){
						JOptionPane.showMessageDialog(null, "Write Data Successed!");
					}else{
						JOptionPane.showMessageDialog(null, "Transaction Faild!");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnExportData.setBounds(590, 525, 142, 37);
		frmReadLogFiles.getContentPane().add(btnExportData);
		
		JButton btnExportResult = new JButton("Export Result");
		btnExportResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveTable(table,"result.txt");
					if(saveTable(table,"result.txt") == true){
						JOptionPane.showMessageDialog(null, "Write Result Successed!");
					}else{
						JOptionPane.showMessageDialog(null, "Transaction Faild!");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnExportResult.setBounds(755, 525, 136, 37);
		frmReadLogFiles.getContentPane().add(btnExportResult);
		
		JLabel label = new JLabel("");
		label.setBounds(755, 438, 166, 14);
		frmReadLogFiles.getContentPane().add(label);
		
		lblNumberRecords = new JLabel("");
		lblNumberRecords.setBounds(855, 69, 160, 14);
		frmReadLogFiles.getContentPane().add(lblNumberRecords);
		
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(19, 69, 46, 14);
		frmReadLogFiles.getContentPane().add(lblNewLabel_2);
}
	
	//Get all Object same Imei
	public List<objectLog> findObjectByImei(String imei){
		List<objectLog> listImei = new LinkedList<objectLog>();
		for(objectLog obj : listobject){
			if(obj.getImei() == Long.parseLong(imei)){
				listImei.add(obj);
			}
		}
		return listImei;
	}
	//Get all Object same Imei and time
	public List<objectLog> findObjectByImeiAndTime(String imei, String time){
		List<objectLog> listImeiTime = new LinkedList<objectLog>();
		for(objectLog obj : listobject){
			if(obj.getImei() == Long.parseLong(imei) && obj.getTime().contains(time)){
				listImeiTime.add(obj);
			}
		}
		return listImeiTime;
	}
	//get All object same time
	public List<objectLog> findObjectByTime(String time){
		List<objectLog> listTime = new LinkedList<objectLog>();
		for(objectLog obj : listobject){
			if(obj.getTime().contains(time)){
				listTime.add(obj);
			}
		}
		return listTime;
	}
	//Read file and convert data to String
	@SuppressWarnings("unused")
	public final void readData(String full_path) throws FileNotFoundException {
		
        
		
		
		String data = "";
		@SuppressWarnings("resource")
		RandomAccessFile randomAccessFile= new RandomAccessFile(full_path, "r");

		if(randomAccessFile==null)
		return;
		try {
		int max_row = (int) (randomAccessFile.length()/24);

		for(int row=0;row<max_row;row++){

		randomAccessFile.seek(row*24);

		long timeMili = randomAccessFile.readLong();
		long imei = randomAccessFile.readLong();
		long ipLong = randomAccessFile.readLong();

		data += "Time : "+convertTimeToString(timeMili) + "\timei: "+imei + "\tip : "+LongToIpByInetAddress(ipLong)+"\n";
		objectLog obj = new objectLog(convertTimeToString(timeMili),imei,LongToIpByInetAddress(ipLong));
		
		Vector datatable = new Vector();
        
		datatable.addElement(convertTimeToString(timeMili));
		datatable.addElement(imei);
		datatable.addElement(LongToIpByInetAddress(ipLong));
        
        rows.add(datatable);
        
		listobject.add(obj);
		}

		} catch (IOException e) {
			e.printStackTrace();
		}
		dtm = new DefaultTableModel(rows,cols);
		table_1.setModel(dtm);
	}
	public static final String convertTimeToString(long time) {
	   	 int dd, mm, yyyy, hour, minus;
	   	 Date date = new Date(time);
	   	 Calendar calendar = Calendar.getInstance();
	   	 calendar.setTime(date);
	   	 dd = calendar.get(Calendar.DATE);
	   	 mm = calendar.get(Calendar.MONTH);
	   	 yyyy = calendar.get(Calendar.YEAR);
	   	 hour = calendar.get(Calendar.HOUR_OF_DAY);
	   	 minus = calendar.get(Calendar.MINUTE);
	   	 return dd + "/" + (mm + 1) + "/" + yyyy + " (" + hour + ":" + minus + ")";
	    }
	
	public static String LongToIpByInetAddress(Long ip){
		String address = "";
		try {
		    // Convert from integer to an IPv4 address
		    InetAddress foo = InetAddress.getByName(ip.toString());
		    address = foo.getHostAddress();

		    // Convert from an IPv4 address to an integer
//		    InetAddress bar = InetAddress.getByName("127.0.0.1");
//		    int value = ByteBuffer.wrap(bar.getAddress()).getInt();
//		    System.out.println(value);

		} catch (Exception e) {
		    e.printStackTrace();
		}
		return address;
	}

	public static String LongToIp2ByShift(Long ip) {
	    return ((ip >>> 24 ) & 0xFF) + "." +

	           ((ip >>> 16 ) & 0xFF) + "." +

	           ((ip >>>  8 ) & 0xFF) + "." +

	           ( ip        & 0xFF);
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    long l = Long.parseLong(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static String SplitString(String string){
		String[] parts = string.split(" ");
		String part1 = parts[0]; // 004
		String part2 = parts[1];
		return part1;
	}
	
	public Boolean saveTable(JTable table, String filename)throws Exception
	{
	  BufferedWriter bfw = new BufferedWriter(new FileWriter(filename));
	  for(int i = 0 ; i < table.getColumnCount() ; i++)
	  {
		  bfw.write(table.getColumnName(i));
		  bfw.write("\t");
	  }

	  for (int i = 0 ; i < table.getRowCount(); i++)
	  {
	    bfw.newLine();
	    for(int j = 0 ; j < table.getColumnCount();j++)
	    {
	    	bfw.write((table.getValueAt(i,j).toString()));
	    	bfw.write("\t");;
	    }
	  }
	  if(new File(filename).length() >0){
		  return true;
	  }
	  bfw.close();
	  return false;
	}
}
