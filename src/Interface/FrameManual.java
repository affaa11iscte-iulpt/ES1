package Interface;

import java.awt.BorderLayout;
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

public class FrameManual extends JFrame {

	
	
	private DefaultTableModel tableModel;
	
	public FrameManual(){
		setTitle("Modo Manual");
		setSize(500,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
		//setLocationRelativeTo(null);
		setVisible(true);
	}
	/** Função auxiiar com os componentes gráficos que compõe a FrameManual 
	 * 
	 * Esta Frame foi dividida em dois panéis: Norte e Sul
	 */

	private void addComponentes() {
		setLayout(new BorderLayout());
		add(filePanel(), BorderLayout.NORTH);
		add(northPanel(), BorderLayout.CENTER);
		add(southPanel(), BorderLayout.SOUTH);
		
	}
	

	/**
	 * Função auxliar com o Painel Norte
	 * Este painel é composto pela tabela onde vão ser listadas as regras e os respetivos pesos associados
	 */
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

	
	/**
	 * Função auxliar com o Painel Sul
	 * Este painel tem os componentes gráficos que permite que seja possivel introduzir, através de JTextField, os pesos para as regras
	 */
	private JPanel southPanel(){
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridLayout(4,1));

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
