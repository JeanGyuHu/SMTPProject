import javax.swing.*;

public class Start {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("SMTP PROGRAM");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PrimaryPanel primaryPanel = new PrimaryPanel();
		frame.getContentPane().add(primaryPanel);
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}

}
