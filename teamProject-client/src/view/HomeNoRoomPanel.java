package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class HomeNoRoomPanel extends JPanel {

	public HomeNoRoomPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblNoRoom = new JLabel("생성된 채팅 방이 없습니다.");
		lblNoRoom.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		lblNoRoom.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNoRoom, BorderLayout.CENTER);
		setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
	}
}
