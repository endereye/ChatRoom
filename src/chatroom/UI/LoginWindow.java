package chatroom.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LoginWindow {
	public static void main(String[] args) {
		LoginWindow login = new LoginWindow();
	}
	public LoginWindow() {
		JFrame frame = new JFrame();

		frame.setTitle("Login");
		frame.setSize(300, 170);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setFont(new Font("����",Font.PLAIN,14));

		FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);

		frame.setLayout(fl);

		Dimension textSize = new Dimension(200,30);
		
		JLabel labelAccount = new JLabel("�û�����");
		JLabel labelPassword = new JLabel("��  �룺");
		
		JTextField textAccount = new JTextField();
		JPasswordField textPassword = new JPasswordField();
		
		labelAccount.setFont(new Font("����",Font.PLAIN,14));
		labelPassword.setFont(new Font("����",Font.PLAIN,14));
		
		textAccount.setPreferredSize(textSize);
		textPassword.setPreferredSize(textSize);
		
		frame.add(labelAccount);
		frame.add(textAccount);
		frame.add(labelPassword);
		frame.add(textPassword);
	
		Dimension buttonSize = new Dimension(200,30);
		
		JButton login  = new JButton();
		JButton signUp = new JButton();
		
		login.setText("��¼");
		login.setFont(new Font("����",Font.PLAIN,14));
		login.setSize(buttonSize);
		
		signUp.setText("ע��");
		signUp.setFont(new Font("����",Font.PLAIN,14));
		signUp.setSize(buttonSize);
		
		frame.add(login);
		frame.add(signUp);
		
		frame.setVisible(true);
		login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
			}
		});
		signUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}
}

