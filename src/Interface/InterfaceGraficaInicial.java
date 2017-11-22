package Interface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InterfaceGraficaInicial extends JFrame{
	/**
	 * Classe onde est� implementada a Interface Gr�fica
	 * 
	 * Foi criada de forma a poder selecionar a forma como vamos fazer o tratamento das regras, isto �, de forma manual ou autom�tica.
	 * @return Interface Gr�fica
	 */
	public InterfaceGraficaInicial(){
		setTitle("METER TITULO");
		setSize(250,100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentes();
	}

	public void open(){
		setVisible(true);
	}

	/** Fun��o auxiiar com os componentes gr�ficos que comp�e a interface 
	 * 
	 * Consoante o tipo de tratamento a ser escolhido, autom�tico ou manual, a frame inicial d� origem a outra frame (inst�ncia das classes FrameManual e FrameAutomatica)
	 */

	public void addComponentes(){
		setLayout(new BorderLayout());
		JPanel painel = new JPanel();
		JLabel label1 = new JLabel("            Escolha o tipo de tratamento");
		JButton automatico = new JButton("Automatico");
		JButton manual = new JButton("Manual");
		painel.add(automatico);
		painel.add(manual);
		add(label1,BorderLayout.NORTH);
		add(painel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		manual.addActionListener(new ActionListener(
				) {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new FrameManual();
			}
		});

		automatico.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameAutomatica();





			}
		});

	}



	public static void main (String[] args){
		InterfaceGraficaInicial ig = new InterfaceGraficaInicial();
		ig.open();

	}
}

