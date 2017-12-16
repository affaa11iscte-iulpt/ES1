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

/*
 * FALTA
 * Melhorar javadoc
 */

public class Interface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected DefaultTableModel tableModel;
	private boolean isEditable=true;
	private List<Email> emails = new ArrayList<Email>();	
	private JLabel fp = new JLabel("Falsos Positivos Gerados: ");
	private JLabel fn = new JLabel("Falsos Negativos Gerados: ");
	private JButton loadFile1;
	private JButton loadFile2;
	private JButton loadFile3;
	private JLabel labelRules;
	private JLabel labelSpam;
	private JLabel labelHam;
	private Control c;
	private Object[][] data;
	private String[] colNomes={"Regras", "Pesos"};
	private String fileRules;
	private JCheckBox autoMode;
	private JCheckBox manualMode;


	public Interface(){
		c= new Control();
		setSize(700,700);
		setTitle("AntiSpam Filter");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addPanels();
		setVisible(true);
	}

	public void open(){
		setVisible(true);
	}

	/** 	
	 * Esta funcao junta todos os paines que constituem a interface, nomeadamente
	 * o painel para carregar os ficheiros, o painel onde é desenhada a tabela
	 * e os modos automatico e manual e por fim o painel onde e possivel guardar e fazer
	 * reset da configuracao.
	 * 
	 * 
	 **/

	public void addPanels(){
		setLayout(new BorderLayout());
		add(filePanel(), BorderLayout.NORTH);
		add(tablePanel(), BorderLayout.CENTER);
		add(savePanel(), BorderLayout.SOUTH);


	}


	/**
	 * Criacao do painel onde especifica a tabela e trata o modo, isto e, automatico e manual 
	 * return @JPanel
	 */

	@SuppressWarnings("serial")
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
		JLabel labelCalculate = new JLabel("Calcular: ");
		JButton calculate= new JButton("ok");

		tableSubPanel1.add(labelCalculate);
		tableSubPanel1.add(calculate);

		JPanel tableSubPanel2 = new JPanel();
		autoMode = new JCheckBox("Modo Automatico");
		manualMode = new JCheckBox ("Modo Manual");

		manualMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				autoMode.setSelected(false);
				isEditable=true;

			}
		});

		autoMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				isEditable=false;
				manualMode.setSelected(false);



			}
		});

		calculate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(loadFile1.isEnabled()){ 
					JOptionPane.showMessageDialog(null, "Falta leitura de ficheiro -  " + labelRules.getText());
				}
				else if(loadFile2.isEnabled()){
					JOptionPane.showMessageDialog(null, "Falta leitura de ficheiro -  " + labelSpam.getText());

				}
				else if(loadFile3.isEnabled()){
					JOptionPane.showMessageDialog(null, "Falta leitura de ficheiro -  " + labelHam.getText());
				}
				else{
					if(manualMode.isSelected()) {
						c = new Control();
						c.calculate(emails,getRules() );
						setFp(c.getFpos());
						setFn(c.getFneg());
					}else if(autoMode.isSelected()) {
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

		tableSubPanel2.add(manualMode);
		tableSubPanel2.add(autoMode);


		tablePanel.add(scrollArea, BorderLayout.NORTH);
		tablePanel.add(tableSubPanel2,BorderLayout.CENTER);
		tablePanel.add(tableSubPanel1, BorderLayout.SOUTH);

		return tablePanel;
	}


	/**
	 * Esta funcao cria um painel onde e possivel carregar os ficheiros pretendidos para
	 * o calculo dos falsos positivos e dos falsos negativos.
	 * 
	 * Por cada ficheiro e criado um sub painel onde e adicionado a label com o nome do
	 * ficheiro com o respetivo botao. Neste botao e feito o ActionListener onde define
	 * que por defeito esta o modo manual e que nesse modo e possivel editar a tabela da
	 * interface, para alem disso carrega o respetivo ficheiro ao invocar a funcao criada para
	 * esse efeito.
	 * 
	 * Por fim todos os sub paineis sao adicionados ao painel principal.
	 * 
	 * return @JPanel filePanel
	 */
	private JPanel filePanel(){
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(3,2));

		JPanel fileSubPanel1= new JPanel();
		labelRules = new JLabel("Ficheiro de Regras");
		loadFile1 = new JButton("ok");
		JTextField text1 = new JTextField("files\\rules.cf");
		fileSubPanel1.add(labelRules);
		fileSubPanel1.add(text1);
		fileSubPanel1.add(loadFile1);
		loadFile1.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileRules = text1.getText();
				addRules(fileRules);
				isEditable=true;
				loadFile1.setEnabled(false);
				manualMode.setSelected(true);
				autoMode.setSelected(false);


			}
		});

		JPanel fileSubPanel2= new JPanel();
		labelSpam = new JLabel("E-mails indesejados");
		loadFile2=	new JButton("ok");
		JTextField text2 = new JTextField("files\\spam.log");
		fileSubPanel2.add(labelSpam);
		fileSubPanel2.add(text2);
		fileSubPanel2.add(loadFile2);
		loadFile2.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> spamEmails = Control.readEmails(text2.getText(), true);
				emails.addAll(spamEmails);
				loadFile2.setEnabled(false);
			}
		});

		JPanel fileSubPanel3= new JPanel();
		labelHam = new JLabel("E-mails ");
		loadFile3= new JButton("ok");
		JTextField text3 = new JTextField("files\\ham.log");
		fileSubPanel3.add(labelHam);
		fileSubPanel3.add(text3);
		fileSubPanel3.add(loadFile3);
		loadFile3.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> hamEmails = Control.readEmails(text3.getText(), false);
				emails.addAll(hamEmails);
				loadFile3.setEnabled(false);
			}
		});

		filePanel.add(fileSubPanel1);
		filePanel.add(fileSubPanel2);
		filePanel.add(fileSubPanel3);

		return filePanel;

	}



	/**
	 * Criacao do painel onde mostra os falsos positivos/negativos gerados e as opcoes de guardar e apagar as configuracoes
	 * return @JPanel
	 */
	private JPanel savePanel(){
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new GridLayout(5,1));


		JPanel modeSubPanel1 = new JPanel();
		modeSubPanel1.add(fp);

		JPanel modeSubPanel2= new JPanel();
		modeSubPanel2.add(fn);

		JPanel modeSubPanel3 = new JPanel();
		JButton save = new JButton ("Guardar Configuração");
		modeSubPanel3.add(save);

		save.addActionListener(new ActionListener() {

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
				loadFile1.setEnabled(true);
				loadFile2.setEnabled(true);
				loadFile3.setEnabled(true);
				setFp(0);
				setFn(0);
				if(autoMode.isSelected() || manualMode.isSelected()){
					autoMode.setSelected(false);
					manualMode.setSelected(false);
				}
				removeAllRowsOfTable();
				c.removeRowFile(fileRules);


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
	 * Altera o valor das labels dos falsos positivos quando este e calculado, recebe um inteiro (numero de falsos positivos), que vai ser o valor a adicionar na label 
	 * @param i
	 */
	public void setFp(int i) {
		this.fp.setText("Falsos Positivos Gerados: "+i);
	}

	/**
	 * Altera o valor das labels dos falsos negativos quando este e calculado, recebe um inteiro (numero de falsos negativos), que vai ser o valor a adicionar na label 
	 * @param i
	 */
	public void setFn(int i) {
		this.fn.setText("Falsos Negativos Gerados: "+i);
	}


	/**
	 * 	Funcao que recebe o caminho do ficheiro em que se encontram listadas as regras
	 *  e cria um Map, que agrupa as regras com os respetivos pesos atraves da funcao readRules()
	 *  da classe control. Apos a criacacao do Map, este e dado como parametro a funcao putRulesOnTable()
	 *  para que as regras e respetivos pesos sejam adicionado a JTable
	 *  
	 * @param file - caminho do ficheiro onde se encontram listadas as regras
	 */
	public void addRules(String file) {
		Map<String,String> rules = Control.readRules(file);
		putRulesOnTable(rules);
	}

	/**
	 * Funcao que recebe um Map de regras com os respetivos pesos associados, removendo, em primeiro lugar,
	 * tudo o que esta contido na tableModel. Posteriormente adiciona linhas a tabela com as regras e pesos
	 * contidos no Map.
	 * 
	 * @param rules Mapa de regras <Regra,Peso>
	 */
	private void putRulesOnTable(Map<String, String> rules) {
		removeAllRowsOfTable();
		for(Map.Entry<String, String> entry: rules.entrySet()) {
			tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
		}
	}

	/** 
	 *Reescreve a tabela 
	 *
	 **/
	@SuppressWarnings("unused")
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
	 * Devolve as regras e os respetivos pesos num HashMap, sendo que a chave e a regra
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


	/**
	 * Devolve a lista de e-mails
	 * @return List<Email>
	 */
	public List<Email> getEmails(){
		return emails;
	}





	public static void main (String[] args){
		Interface t = new Interface();
		t.open();

	}

}
