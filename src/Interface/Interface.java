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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.Control;
import components.Email;


//falta por as excepções nos modos (não se pode escolher o modo sem ficheiro)
//tratar o modo por defeito
//pesquisar regras

public class Interface extends JFrame {
	protected DefaultTableModel tableModel;
	private List<Email> emails = new ArrayList<Email>();	
	private JLabel fp = new JLabel("Falsos Positivos Gerados: ");
	private JLabel fn = new JLabel("Falsos Negativos Gerados: ");
	private Control c;
	
	public Interface(){
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

	private JPanel filePanel(){
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(3,2));

		JPanel fileSubPanel1= new JPanel();
		JLabel rules = new JLabel("rules.cf");
		JButton ok1 = new JButton("ok");
		JTextField text1 = new JTextField("files\\rules.cf");
		fileSubPanel1.add(rules);
		fileSubPanel1.add(text1);
		fileSubPanel1.add(ok1);
		ok1.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addRules(text1.getText());
				ok1.setEnabled(false);
			}
		});

		JPanel fileSubPanel2= new JPanel();
		JLabel spam = new JLabel("spam.log");
		JTextField text2 = new JTextField("files\\spam.log");
		JButton ok2 = new JButton("ok");
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
		JButton ok3 = new JButton("ok");
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

	private JPanel tablePanel(){

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		List<String> visibleColumns = new ArrayList<String>();

		visibleColumns.add("Regras: ");
		visibleColumns.add("Pesos: ");

		tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
		JTable tabela = new JTable(tableModel);

		JScrollPane scrollArea = new JScrollPane(tabela);

		scrollArea.setPreferredSize(new Dimension(400,300));

		tablePanel.add(scrollArea);

		JPanel tableSubPanel1 = new JPanel();
		JLabel label = new JLabel("Calcular: ");
		JButton calcular= new JButton("ok");
		calcular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c = new Control();
				c.calculateFP(getEmails(),getRules() );
				setFp(c.getFpos());
				setFn(c.getFneg());
				
			}
		});
		tableSubPanel1.add(label);
		tableSubPanel1.add(calcular);

		JPanel tableSubPanel2 = new JPanel();
		JCheckBox modoManual= new JCheckBox("Modo Manual");
		JCheckBox modoAutomatico= new JCheckBox("Modo Automatico");
		modoManual.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modoAutomatico.setSelected(false);

			}
		});

		modoAutomatico.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				tabela.setEnabled(false);
				modoManual.setSelected(false);


			}
		});

		tableSubPanel2.add(modoManual);
		tableSubPanel2.add(modoAutomatico);


		tablePanel.add(scrollArea, BorderLayout.NORTH);
		tablePanel.add(tableSubPanel2,BorderLayout.CENTER);
		tablePanel.add(tableSubPanel1, BorderLayout.SOUTH);

		return tablePanel;
	}

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

		JPanel modeSubPanel4 = new JPanel();
		JButton reset = new JButton ("Reset Configuração");
		modeSubPanel4.add(reset);


		modePanel.add(modeSubPanel1);
		modePanel.add(modeSubPanel2);
		modePanel.add(modeSubPanel3);
		modePanel.add(modeSubPanel4);


		return modePanel;
	}


	public void setFp(int i) {
		this.fp = new JLabel("Falsos Positivos Gerados: " + i);
	}
	
	public void setFn(int i) {
		this.fn = new JLabel("Falsos Negativos Gerados: " + i);;
	}
	/**
	 * Obtenção do HashMap de regras (com pesos associados) lidas na classe Reader e listagem das mesmas na interface manual
	 */
	public void addRules(String file) {
		Map<String,String> rules = Control.readRules(file);

		for(Map.Entry<String, String> entry: rules.entrySet()) {
			tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
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
			String value = (String)tableModel.getValueAt(line, 1);
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


