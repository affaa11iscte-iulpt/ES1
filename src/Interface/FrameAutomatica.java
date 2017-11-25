package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;


public class FrameAutomatica extends Frame {





	public FrameAutomatica(){
		super();
		setTitle("Modo Automatico");
		add(centerPanel(), BorderLayout.CENTER);

	}



	public JPanel centerPanel() {

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		List<String> visibleColumns = new ArrayList<String>();

		visibleColumns.add("Regras: ");
		visibleColumns.add("Pesos: ");

		tableModel = new DefaultTableModel(visibleColumns.toArray(),0);
		JTable tabela = new JTable(tableModel);

		JScrollPane scrollArea = new JScrollPane(tabela);

		scrollArea.setPreferredSize(new Dimension(400,300));
		tabela.setEnabled(false);

		JPanel subPanel = new JPanel();
		JLabel label = new JLabel("Gerar Pesos com o Algoritmo: ");
		JButton gerar = new JButton("ok");
		
		gerar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					AntiSpamFilterAutomaticConfiguration.start(getEmails(), getRules());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		subPanel.add(label);
		subPanel.add(gerar);


		centerPanel.add(scrollArea, BorderLayout.CENTER);
		centerPanel.add(subPanel, BorderLayout.SOUTH);

		return centerPanel;
	}







}
