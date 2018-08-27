package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HomeRoomPanel extends JPanel{

	private Main main;
	private ArrayList<JLabel> roomLabelList;
	private ArrayList<String> roomList;
	private int roomCount;

	public HomeRoomPanel(Main mainFrame) {
		main = mainFrame;
		roomList = new ArrayList<>();
		init();
	}

	public HomeRoomPanel(Main mainFrame, ArrayList<String> roomList) {
		main = mainFrame;
		this.roomList = roomList;
		init();
	}

	private void init() {
		setBackground(Color.WHITE);
		setLayout(new CardLayout(0, 0));
		setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));
		roomLabelList = new ArrayList<JLabel>(10);
		roomCount = 0;

		JPanel homeRoomListPanel = new JPanel();
		homeRoomListPanel.setBackground(Color.WHITE);
		add(homeRoomListPanel);
		homeRoomListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		homeRoomListPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel homeRoomLeftPanel = new JPanel();
		homeRoomLeftPanel.setBackground(Color.WHITE);
		homeRoomListPanel.add(homeRoomLeftPanel);
		homeRoomLeftPanel.setLayout(new GridLayout(5, 1, 0, 0));
		homeRoomLeftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.lightGray));

		JPanel homeRoomRightPanel = new JPanel();
		homeRoomRightPanel.setBackground(Color.WHITE);
		homeRoomListPanel.add(homeRoomRightPanel);
		homeRoomRightPanel.setLayout(new GridLayout(5, 1, 0, 0));

		for (int i = 0; i < 10; i++) {
			JLabel room = new JLabel();
			room.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			room.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						main.setMainCard("Chat_"+room.getText().replaceAll("님과의 채팅방", ""));
					}
				}
			});
			roomLabelList.add(room);
			if (i < 5)
				homeRoomLeftPanel.add(roomLabelList.get(i));
			else
				homeRoomRightPanel.add(roomLabelList.get(i));
		}
	}

	public void addRoom(String roomName) {
		if (roomCount < 10) {
			roomList.add(roomName + "_" + roomCount);
			roomLabelList.get(roomCount++).setText(roomName + "님과의 채팅방");
		}
	}

	public void removeRoom(String roomName) {
		Iterator<String> iter = roomList.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			String[] st = s.split("_");
			if (st[0].equals(roomName)) {
				iter.remove();
				roomLabelList.get(Integer.parseInt(st[1])).setText("");
			}
		}
	}
	
	public ArrayList<String> getRoomList(){
		return roomList;
	}
}
