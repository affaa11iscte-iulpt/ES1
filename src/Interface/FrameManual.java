package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.Reader;

public class FrameManual extends Frame {

	
	


	public FrameManual(){
		super();
		setTitle("Modo Manual");
		add(northPanel());
	}

	
	

	public JPanel northPanel() {

		JPanel panelNorth = new JPanel();
		List<String> visibleColumns = new ArrayList<String>();

		visibleColumns.add("Regras: ");
		visibleColumns.add("Pesos: ");

		tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
		JTable tabela = new JTable(tableModel);
	
		JScrollPane scrollArea = new JScrollPane(tabela);
		
		scrollArea.setPreferredSize(new Dimension(400,300));
		
		panelNorth.add(scrollArea);

		return panelNorth;
	}

	

}
