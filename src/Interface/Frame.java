package Interface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.Control;
import components.Email;

public abstract class Frame extends JFrame {

	protected DefaultTableModel tableModel;
	private List<Email> emails = new ArrayList<Email>();	
	
	public Frame(){
		setSize(650,650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
		setVisible(true);
	}

	private void addComponentes() {
		setLayout(new BorderLayout());
		add(filePanel(), BorderLayout.NORTH);
		add(southPanel(), BorderLayout.SOUTH);

	}


	private JPanel filePanel(){
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new GridLayout(3,2));

		JPanel subPanel1= new JPanel();
		JLabel rules = new JLabel("rules.cf");
		JButton carregar = new JButton("ok");
		JTextField text1 = new JTextField("files\\rules.cf");
		subPanel1.add(rules);
		subPanel1.add(text1);
		subPanel1.add(carregar);
		carregar.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addRules(text1.getText());
				carregar.setEnabled(false);
			}
		});

		JPanel subPanel2= new JPanel();
		JLabel spam = new JLabel("spam.log");
		JTextField text2 = new JTextField("files\\spam.log");
		JButton carregar2 = new JButton("ok");
		subPanel2.add(spam);
		subPanel2.add(text2);
		subPanel2.add(carregar2);
		carregar2.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> spamEmails = Control.readEmails(text2.getText(), true);
				emails.addAll(spamEmails);
				carregar2.setEnabled(false);
			}
		});



		JPanel subPanel3= new JPanel();
		JLabel ham = new JLabel("ham.log");
		JButton carregar3 = new JButton("ok");
		JTextField text3 = new JTextField("files\\ham.log");
		subPanel3.add(ham);
		subPanel3.add(text3);
		subPanel3.add(carregar3);
		carregar3.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Email> hamEmails = Control.readEmails(text3.getText(), false);
				emails.addAll(hamEmails);
				carregar3.setEnabled(false);
			}
		});



		filePanel.add(subPanel1);
		filePanel.add(subPanel2);
		filePanel.add(subPanel3);

		return filePanel;

	}




	public JPanel southPanel(){
		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new GridLayout(5,1));


		JPanel subPanel2 = new JPanel();
		JLabel fp = new JLabel(" Falsos Positivos Gerados:  ");

		subPanel2.add(fp);

		JPanel subPanel3 = new JPanel();
		JLabel fn = new JLabel(" Falsos Negativos Gerados:  ");

		subPanel3.add(fn);


		JPanel subPanel4 = new JPanel();
		JButton guardar = new JButton ("Guardar Configuração");
		subPanel4.add(guardar);

		JPanel subPanel5 = new JPanel();
		JButton reset = new JButton ("Reset Configuração");
		subPanel5.add(reset);

		panelSouth.add(subPanel2);
		panelSouth.add(subPanel3);
		panelSouth.add(subPanel4);
		panelSouth.add(subPanel5);


		return panelSouth;
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


}
