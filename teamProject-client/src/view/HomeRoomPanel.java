package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class HomeRoomPanel extends JPanel {

	public Room room;
	public JLabel lblRoom, lblChatCount;
	public int roomSequence;
	public int roomChatCount;

	public HomeRoomPanel(int roomSequence) {
		this.roomSequence = roomSequence;
		this.room = new Room();
		init();
	}

	public HomeRoomPanel(Room room, int roomSequence) {
		this.roomSequence = roomSequence;
		this.room = room;
		init();
	}

	public void init() {
		setBackground(Color.white);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		setLayout(new BorderLayout(0, 0));
		roomChatCount= 0;
		lblRoom = new JLabel(room.roomName);

		lblChatCount = new JLabel("");
		lblRoom.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		lblChatCount.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lblRoom.setBackground(Color.white);
		lblChatCount.setOpaque(true);
		lblChatCount.setBackground(Color.white);
		lblChatCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoom.setBorder(new EmptyBorder(0, 30, 0, 0));

		add(lblRoom, BorderLayout.CENTER);

		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.white);
		panel.add(lblChatCount, BorderLayout.CENTER);
		panel.setBorder(new EmptyBorder(30, 0, 30, 50));
		lblChatCount.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(panel, BorderLayout.EAST);
	}

}
