package chatroom.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SignUpWindow {
	public static void main(String[] args) {
		SignUpWindow signup = new SignUpWindow();
	}
	public SignUpWindow() {
		JFrame frame = new JFrame();
		
		frame.setTitle("SignUp");
		frame.setSize(320, 200);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setFont(new Font("����",Font.PLAIN,14));

		FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);

		frame.setLayout(fl);

		Dimension textSize = new Dimension(200,30);
		
		JLabel labelAccount = new JLabel("�� �� ����");
		JLabel labelPassword = new JLabel("��    �룺");
		JLabel labelConfirmPassword = new JLabel("ȷ�����룺");
		
		JTextField textAccount = new JTextField();
		JPasswordField textPassword = new JPasswordField();
		JPasswordField textConfirmPassword = new JPasswordField();
		
		labelAccount.setFont(new Font("����",Font.PLAIN,14));
		labelPassword.setFont(new Font("����",Font.PLAIN,14));
		labelConfirmPassword.setFont(new Font("����",Font.PLAIN,14));
		
		textAccount.setPreferredSize(textSize);
		textPassword.setPreferredSize(textSize);
		textConfirmPassword.setPreferredSize(textSize);
		
		frame.add(labelAccount);
		frame.add(textAccount);
		frame.add(labelPassword);
		frame.add(textPassword);
		frame.add(labelConfirmPassword);
		frame.add(textConfirmPassword);
		
		Dimension buttonSize = new Dimension(200,30);

		JButton confirm = new JButton();
		JButton cancel = new JButton();
		
		confirm.setText("ȷ��");
		confirm.setFont(new Font("����",Font.PLAIN,14));
		confirm.setSize(buttonSize);
		cancel.setText("ȡ��");
		cancel.setFont(new Font("����",Font.PLAIN,14));
		cancel.setSize(buttonSize);
		
		frame.add(confirm);
		frame.add(cancel);
		
		frame.setVisible(true);
		
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
	}
}
