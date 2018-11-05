import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrimaryPanel extends JPanel{
	
	private SocketNetwork smtp;
	private JTextField txtFromId;
	private JTextField txtToId;
	private JLabel lblToId;
	private JLabel lblFromId;
	private JLabel lblPassword;
	private JPasswordField password;
	private JButton btnConfirm;
	private JTextArea taData;
	private JScrollPane scrollPane;
	
	private ButtonListener bL;
	
	
	public PrimaryPanel() {
		setPreferredSize(new Dimension(600, 400));
		setBackground(Color.WHITE);
		setLayout(null);
		
		bL = new ButtonListener();
		
		lblFromId = new JLabel("From :");
		lblFromId.setBounds(5,20,40,30);
		
		lblPassword = new JLabel("pw :");
		lblPassword.setBounds(310,20,40,30);
		
		password = new JPasswordField(10);
		password.setBounds(350,20,150,30);
		
		lblToId = new JLabel("To :");
		lblToId.setBounds(5,55,40,30);
		
		txtFromId = new JTextField(10);
		txtFromId.setBounds(50,20,250,30);
		
		txtToId = new JTextField(10);
		txtToId.setBounds(50,55,250,30);
		
		btnConfirm = new JButton("Send!");
		btnConfirm.setBounds(350,55,80,30);
		btnConfirm.addActionListener(bL);
		taData = new JTextArea();
		
		scrollPane = new JScrollPane(taData);
		scrollPane.setBounds(10,90,580,300);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVisible(true);
		
		add(lblFromId);
		add(lblPassword);
		add(password);
		add(lblToId);
		add(txtFromId);
		add(txtToId);
		add(btnConfirm);
		add(scrollPane);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object obj = event.getSource();
			
			if(obj == btnConfirm) {
				try {
					smtp = new SocketNetwork(taData.getText(), txtFromId.getText(), txtToId.getText());
					smtp.send();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}
	
}
