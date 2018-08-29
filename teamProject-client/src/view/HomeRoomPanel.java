package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		setLayout(new BorderLayout(0, 0));
		
		lblRoom = new JLabel(room.roomName);
		lblChatCount = new JLabel("");
		lblRoom.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		lblChatCount.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		lblChatCount.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblRoom, BorderLayout.CENTER);
		add(lblChatCount, BorderLayout.EAST);
	}

}
