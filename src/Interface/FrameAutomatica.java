package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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

public class FrameAutomatica extends JFrame {

	
	
	private DefaultTableModel tableModel;
	
	public FrameAutomatica(){
		setTitle("Modo Automatico");
		setSize(500,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
		//setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponentes() {
		setLayout(new BorderLayout());
		add(northPanel(), BorderLayout.CENTER);
		add(filePanel(), BorderLayout.NORTH);
		add(southPanel(), BorderLayout.SOUTH);
		
	}
	
	
	private JPanel filePanel(){
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(1,3));
		JLabel file = new JLabel(" Procure o ficheiro");
		JButton button = new JButton("ok");
		JTextField pesquisa = new JTextField();
	
		filePanel.add(file);
		filePanel.add(pesquisa);
		filePanel.add(button);
		
		return filePanel;
	}

	private JPanel northPanel(){
		JPanel panelNorth = new JPanel();
		List<String> visibleColumns = new ArrayList<String>();

        visibleColumns.add("Regras: ");
        visibleColumns.add("Pesos: ");

        tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
        JTable tabela = new JTable(tableModel);
        tabela.setEnabled(false);
        addRules();
        
		JScrollPane scrollArea = new JScrollPane(tabela);
		
		panelNorth.add(scrollArea);
		
		return panelNorth;
		
	}
	
	private JPanel southPanel(){
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridLayout(4,1));

		JPanel subPanel1 = new JPanel();
		JLabel calcular = new JLabel(" Gerar Pesos com o Algoritmo: ");
		JButton ok = new JButton("OK");
		
		
		subPanel1.add(calcular);
		subPanel1.add(ok);
		
		JPanel subPanel2 = new JPanel();
		JLabel fp = new JLabel(" Falsos Positivos Gerados:  ");
		//JTextField textfield1 = new JTextField("   ");
		
		
		
		subPanel2.add(fp);
		//subPanel2.add(textfield1);
		
		JPanel subPanel3 = new JPanel();
		JLabel fn = new JLabel(" Falsos Negativos Gerados:  ");
	//	JTextField textfield2 = new JTextField("   ");
		
		
		subPanel3.add(fn);
		//subPanel3.add(textfield2);
		
		JPanel subPanel4 = new JPanel();
		JButton guardar = new JButton ("Guardar Configuração");
		subPanel4.add(guardar);
		
		panelSouth.add(subPanel1);
		panelSouth.add(subPanel2);
		panelSouth.add(subPanel3);
		panelSouth.add(subPanel4);
		
		
		return panelSouth;
	}
	/**
	 * Obtenção do HashMap de regras (com pesos associados) lidas na classe Reader e listagem das mesmas na interface manual
	 */
	public void addRules() {
        Map<String,String> rules = Reader.readRules("files/rules.cf");
        
		for(Map.Entry<String, String> entry: rules.entrySet()) {
        	tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
        }
	}
}
