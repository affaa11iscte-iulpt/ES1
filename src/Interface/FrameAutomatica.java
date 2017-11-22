package Interface;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class FrameAutomatica extends Frame {





	public FrameAutomatica(){
		super();
		setTitle("Modo Automatico");
		add(northPanel());
	//	southPanel();
	}


//	public void southPanel(){
//		JPanel panel= super.southPanel();
//
//		JPanel subPanel1 = new JPanel();
//		JLabel calcular = new JLabel(" Gerar Pesos com o Algoritmo: ");
//		JButton ok = new JButton("OK");
//
//
//		subPanel1.add(calcular);
//		subPanel1.add(ok);
//		
//		panel.add(subPanel1);
//		
//
//	}




	public JPanel northPanel() {

		JPanel panelNorth = new JPanel();
		List<String> visibleColumns = new ArrayList<String>();

		visibleColumns.add("Regras: ");
		visibleColumns.add("Pesos: ");

		tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
		JTable tabela = new JTable(tableModel);

		JScrollPane scrollArea = new JScrollPane(tabela);

		scrollArea.setPreferredSize(new Dimension(400,300));
		tabela.setEnabled(false);
		panelNorth.add(scrollArea);

		return panelNorth;
	}







}
