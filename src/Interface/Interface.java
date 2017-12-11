package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.Control;
import components.Email;

//pesquisar regras
//rever as variaveis

public class Interface extends JFrame {
	protected DefaultTableModel tableModel;
	private boolean isEditable=true;
	private List<Email> emails = new ArrayList<Email>();	
	private JLabel fp = new JLabel("Falsos Positivos Gerados: ");
	private JLabel fn = new JLabel("Falsos Negativos Gerados: ");
	private JButton ok1 = new JButton("ok");
	private JButton ok2 = new JButton("ok");
	private JButton ok3 = new JButton("ok");
	private Control c;
	private Object[][] data;
	private String[] colNomes={"Regras", "Pesos"};
	private String fileRules;
	private JCheckBox modoManual= new JCheckBox("Modo Manual");
	private JCheckBox modoAutomatico= new JCheckBox("Modo Automatico");


	public Interface(){
		c= new Control();
		setSize(700,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
		setVisible(true);
	}

	public void open(){
		setVisible(true);
	}
	public void addComponentes(){
		setLayout(new BorderLayout());
		add(filePanel(), BorderLayout.NORTH);
		add(tablePanel(), BorderLayout.CENTER);
		add(modePanel(), BorderLayout.SOUTH);


	}

	/**
	 * Criação do painel onde carrega os ficheiros escolhidos
	 * return @JPanel
	 */
	private JPanel filePanel(){
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(3,2));

		JPanel fileSubPanel1= new JPanel();
		JLabel rules = new JLabel("rules.cf");
		JTextField text1 = new JTextField("files\\rules.cf");
		fileSubPanel1.add(rules);
		fileSubPanel1.add(text1);
		fileSubPanel1.add(ok1);
		ok1.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileRules = text1.getText();
				addRules(fileRules);
				ok1.setEnabled(false);
				modoManual.setSelected(true);
				modoAutomatico.setSelected(false);

			}
		});

		JPanel fileSubPanel2= new JPanel();
		JLabel spam = new JLabel("spam.log");
		JTextField text2 = new JTextField("files\\spam.log");
		fileSubPanel2.add(spam);
		fileSubPanel2.add(text2);
		fileSubPanel2.add(ok2);
		ok2.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> spamEmails = Control.readEmails(text2.getText(), true);
				emails.addAll(spamEmails);
				ok2.setEnabled(false);
			}
		});

		JPanel fileSubPanel3= new JPanel();
		JLabel ham = new JLabel("ham.log");
		JTextField text3 = new JTextField("files\\ham.log");
		fileSubPanel3.add(ham);
		fileSubPanel3.add(text3);
		fileSubPanel3.add(ok3);
		ok3.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> hamEmails = Control.readEmails(text3.getText(), false);
				emails.addAll(hamEmails);
				ok3.setEnabled(false);
			}
		});



		filePanel.add(fileSubPanel1);
		filePanel.add(fileSubPanel2);
		filePanel.add(fileSubPanel3);

		return filePanel;

	}

	/**
	 * Criação do painel onde especifica a tabela e trata o modo, isto é, automático e manual 
	 * return @JPanel
	 */

	private JPanel tablePanel(){
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tableModel = new DefaultTableModel(data, colNomes);
		JTable tabela = new JTable(tableModel){

			@Override
			public boolean isCellEditable(int row, int column){
				if(column==1 && isEditable==true){
					return true;
				}
				else{
					return false;
				}
			}
		};

		JScrollPane scrollArea = new JScrollPane(tabela);
		scrollArea.setPreferredSize(new Dimension(400,300));
		tablePanel.add(scrollArea);

		JPanel tableSubPanel1 = new JPanel();
		JLabel label = new JLabel("Calcular: ");
		JButton calcular= new JButton("ok");

		tableSubPanel1.add(label);
		tableSubPanel1.add(calcular);

		JPanel tableSubPanel2 = new JPanel();

		modoManual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modoAutomatico.setSelected(false);
				isEditable=true;

			}
		});

		modoAutomatico.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				isEditable=false;
				modoManual.setSelected(false);



			}
		});

		calcular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(ok1.isEnabled() || ok2.isEnabled() || ok3.isEnabled()){
					JOptionPane.showMessageDialog(null, "Falta leitura de ficheiro");
				}
				else{
					if(modoManual.isSelected()) {
						c = new Control();
						c.calculate(emails,getRules() );
						setFp(c.getFpos());
						setFn(c.getFneg());
					}else if(modoAutomatico.isSelected()) {
						c = new Control();
						c.automaticMode(emails, getRules());
						Map<String, String> rules = c.readAutomatic(getRules(), "experimentBaseDirectory\\referenceFronts\\AntiSpamFilterProblem.rf", 
								"experimentBaseDirectory\\referenceFronts\\AntiSpamFilterProblem.rs");
						putRulesOnTable(rules);
						System.out.println(rules.size());
						setFp(c.getFpos());
						setFn(c.getFneg());

					}
				}
			}
		});

		tableSubPanel2.add(modoManual);
		tableSubPanel2.add(modoAutomatico);


		tablePanel.add(scrollArea, BorderLayout.NORTH);
		tablePanel.add(tableSubPanel2,BorderLayout.CENTER);
		tablePanel.add(tableSubPanel1, BorderLayout.SOUTH);

		return tablePanel;
	}


	/**
	 * Criação do painel onde mostra os falsos positivos/negativos gerados e as opções de guardar e apagar as configurações
	 * return @JPanel
	 */
	private JPanel modePanel(){
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new GridLayout(5,1));


		JPanel modeSubPanel1 = new JPanel();
		modeSubPanel1.add(fp);

		JPanel modeSubPanel2= new JPanel();
		modeSubPanel2.add(fn);

		JPanel modeSubPanel3 = new JPanel();
		JButton guardar = new JButton ("Guardar Configuração");
		modeSubPanel3.add(guardar);
		
		guardar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				c.saveConfigurations(fileRules, getRules());
				
			}
		});

		JPanel modeSubPanel4 = new JPanel();
		JButton reset = new JButton ("Reset Configuração");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ok1.setEnabled(true);
				ok2.setEnabled(true);
				ok3.setEnabled(true);
				setFp(0);
				setFn(0);
				if(modoAutomatico.isSelected() || modoManual.isSelected()){
					modoAutomatico.setSelected(false);
					modoManual.setSelected(false);
				}
			removeAllRowsOfTable();
			c.removeRowFile();
			

			}
		});

		modeSubPanel4.add(reset);


		modePanel.add(modeSubPanel1);
		modePanel.add(modeSubPanel2);
		modePanel.add(modeSubPanel3);
		modePanel.add(modeSubPanel4);


		return modePanel;
	}

	

		
	


	/**
	 * Altera o valor das labels dos falsos positivos quando este é calculado, recebe um inteiro (numero de falsos positivos), que vai ser o valor a adicionar na label 
	 * @param i
	 */
	public void setFp(int i) {
		this.fp.setText("Falsos Positivos Gerados: "+i);
	}

	/**
	 * Altera o valor das labels dos falsos negativos quando este é calculado, recebe um inteiro (numero de falsos negativos), que vai ser o valor a adicionar na label 
	 * @param i
	 */
	public void setFn(int i) {
		this.fn.setText("Falsos Negativos Gerados: "+i);
	}


	/**
	 * Obtenção do HashMap de regras (com pesos associados) lidas na classe Reader
	 */
	public void addRules(String file) {
		Map<String,String> rules = Control.readRules(file);
		putRulesOnTable(rules);
	}
	
	/**
	 * Listagem das regras na tabela da interface
	 * @param rules Mapa de regras <Regra,Peso>
	 */
	private void putRulesOnTable(Map<String, String> rules) {
		removeAllRowsOfTable();
		for(Map.Entry<String, String> entry: rules.entrySet()) {
			tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
		}
	}
	
	private void cleanTable(Map<String, String> rules) {
		removeAllRowsOfTable();
		for(Map.Entry<String, String> entry: rules.entrySet()) {
			tableModel.addRow(new String[] {entry.getKey(), ""});
		}
	}
	
	/**
	 * Remove todas as linhas da tabela
	 */
	private void removeAllRowsOfTable() {
		if(tableModel.getRowCount() > 0)
		for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
	        tableModel.removeRow(i);
	    }
	}

	/**
	 * Devolve as regras e os respetivos pesos num HashMap, sendo que a chave é a regra
	 * e o valor o peso.
	 * @return Lista de regras com os pesos
	 */

	public Map<String, String> getRules(){
		Map<String,String> rules = new HashMap<String, String>();
		for(int line=0; line<tableModel.getRowCount(); line++) {
			String rule = (String)tableModel.getValueAt(line, 0);
			String value;
			if(((String)tableModel.getValueAt(line, 1)).equals(""))
				value = "0";
			else
				value = (String)tableModel.getValueAt(line, 1);
			rules.put(rule, value);
		}

		return rules;
	}


	public List<Email> getEmails(){
		return emails;
	}





	public static void main (String[] args){
		Interface t = new Interface();
		t.open();

	}

}
