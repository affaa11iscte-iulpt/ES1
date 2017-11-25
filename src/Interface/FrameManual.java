package Interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;
import components.Control;

public class FrameManual extends Frame {

	public FrameManual(){
		super();
		setTitle("Modo Manual");
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

		centerPanel.add(scrollArea);

		JPanel subPanel = new JPanel();
		JLabel label = new JLabel("Calcular: ");
		JButton calcular= new JButton("ok");
		calcular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Control c = new Control();
				c.calculateFP(getEmails(), getRules());
				setFN(c.getFneg());
				setFP(c.getFpos());
			
				
			}
		});
		subPanel.add(label);
		subPanel.add(calcular);


		centerPanel.add(scrollArea, BorderLayout.CENTER);
		centerPanel.add(subPanel, BorderLayout.SOUTH);

		return centerPanel;
	}



}
