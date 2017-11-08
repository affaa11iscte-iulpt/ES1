package Interfaces;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FrameManual extends JFrame {


	
	public FrameManual(){
		setTitle("Modo Manual");
		setSize(250,250);
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
		//panelNorth.setLayout(mgr);
		JTable tabela = new JTable(5, 2);
		JLabel regra = new JLabel("Regras:");
		JLabel peso = new JLabel("Pesos:");
		
		panelNorth.add(regra);
		panelNorth.add(peso);
		panelNorth.add(tabela);
		
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
}
