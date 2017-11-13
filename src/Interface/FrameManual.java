package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.Reader;

public class FrameManual extends JFrame {

	
	
	private DefaultTableModel tableModel;
	
	public FrameManual(){
		setTitle("Modo Manual");
		setSize(500,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
		setVisible(true);
	}

	private void addComponentes() {
		setLayout(new BorderLayout());
		add(northPanel(), BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		
	}
	

	private JPanel northPanel(){
		JPanel panelNorth = new JPanel();
		
		List<String> visibleColumns = new ArrayList<String>();

        visibleColumns.add("Regras: ");
        visibleColumns.add("Pesos: ");

        tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
        JTable tabela = new JTable(tableModel);
        
        addRules();
        
		JScrollPane scrollArea = new JScrollPane(tabela);
		
		panelNorth.add(scrollArea);
		
		return panelNorth;
		
	}
	
	
	private JPanel southPanel(){
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridLayout(3,1));

		JPanel subPanel1 = new JPanel();
		JLabel calcular = new JLabel(" Calcular: ");
		JButton ok = new JButton("OK");
		
		
		subPanel1.add(calcular);
		subPanel1.add(ok);
		
		JPanel subPanel2 = new JPanel();
		JLabel fp = new JLabel(" Falsos Positivos:  ");
		JTextField textfield1 = new JTextField("   ");
		
		
		
		subPanel2.add(fp);
		subPanel2.add(textfield1);
		
		JPanel subPanel3 = new JPanel();
		JLabel fn = new JLabel(" Falsos Negativos:  ");
		JTextField textfield2 = new JTextField("   ");
		
		
		subPanel3.add(fn);
		subPanel3.add(textfield2);
		
		panelSouth.add(subPanel1);
		panelSouth.add(subPanel2);
		panelSouth.add(subPanel3);
		
		
		
		return panelSouth;
	}
	/**
	 * Obtenção da lista de regras lidas na classe Reader e listagem das mesmas na interface manual
	 */
	public void addRules() {
        List<String> rules = Reader.readRules();
        
		for(String s: rules) {
        	tableModel.addRow(new String[] {s,""});
        }
	}
}
