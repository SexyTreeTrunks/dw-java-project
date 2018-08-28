package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class ChatDisconnectDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public ChatDisconnectDialog(String user) {
		setBackground(Color.WHITE);
		setTitle("연결 끊김");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblChatDisconnect = new JLabel(user+"님의 연결이 끊어졌습니다.");
			lblChatDisconnect.setBackground(Color.WHITE);
			lblChatDisconnect.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			lblChatDisconnect.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblChatDisconnect, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("확인");
				okButton.setBackground(Color.WHITE);
				okButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(okButton);
			}
		}
	}

}
