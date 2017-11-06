package antiSpamFilter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InterfaceGrafica extends JFrame{
		
		public InterfaceGrafica(){
			setTitle("METER TITULO");
			setSize(250,100);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			addComponentes();
		}
		
		public void open(){
			setVisible(true);
		}
		
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
					//por para ir para a nova janela
					
				}
			});
			
			automatico.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// por para ir para outra janela
					
				}
			});
			
		}
		
		public void JanelaAutomatica(){
			
		}

		
		public static void main (String[] args){
			InterfaceGrafica ig = new InterfaceGrafica();
			ig.open();
			
		}
	}

