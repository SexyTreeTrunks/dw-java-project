package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.fabric.xmlrpc.base.Array;

public class HomeRoomListPanel extends JPanel {

	private Main main;
	private Variables var;
	private ArrayList<String> roomListOne;
	private ArrayList<Room> roomList;
	private ArrayList<HomeRoomPanel> roomPanelList;

	private JPanel homeRoomLeftPanel, homeRoomRightPanel;

	public int roomListNumber;

	public HomeRoomListPanel() {
		roomList = new ArrayList<>();

		init();
	}

	public HomeRoomListPanel(Main mainFrame) {
		main = mainFrame;
		var = main.getVar();
		roomList = new ArrayList<>();
		init();
	}

	public HomeRoomListPanel(Main mainFrame, ArrayList<Room> roomList) {
		main = mainFrame;
		var = main.getVar();
		this.roomList = roomList;
		init();
	}

	private void init() {
		setBackground(Color.WHITE);
		setLayout(new CardLayout(0, 0));
		setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black));

		JPanel homeRoomListPanel = new JPanel();
		homeRoomListPanel.setBackground(Color.WHITE);
		add(homeRoomListPanel);
		homeRoomListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		homeRoomListPanel.setLayout(new GridLayout(0, 2, 0, 0));

		homeRoomLeftPanel = new JPanel();
		homeRoomLeftPanel.setBackground(Color.WHITE);
		homeRoomListPanel.add(homeRoomLeftPanel);
		homeRoomLeftPanel.setLayout(new GridLayout(5, 1, 0, 0));
		homeRoomLeftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.lightGray));

		homeRoomRightPanel = new JPanel();
		homeRoomRightPanel.setBackground(Color.WHITE);
		homeRoomListPanel.add(homeRoomRightPanel);
		homeRoomRightPanel.setLayout(new GridLayout(5, 1, 0, 0));

		roomPanelList = new ArrayList<>();

		for (int i = 0; i < 10; i++)
			createRoomPanel(i);
	}

	public void createRoomPanel(int roomSequence) {
		HomeRoomPanel panel = new HomeRoomPanel(roomSequence);

		if (roomSequence < 5)
			homeRoomLeftPanel.add(panel);
		else
			homeRoomLeftPanel.add(panel);

		roomPanelList.add(panel);

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					String roomName = panel.room.roomName;
					if (!roomName.equals("")) {
						var.getVO().setconnectRoom(roomName);
						main.setMainCard("Chat_" + roomName);
					}
				}
			}
		});
	}

	public int addRoom(Room room) {
		if (!roomList.contains(room)) {
			roomList.add(room);
			setRoomPanel(room);
			return 1;
		}

		else
			return 0;
	}

	public void addRoom(String roomName) {
		if (!roomListOne.contains(roomName))
			roomListOne.add(roomName);

	}

	public void removeRoom(String roomName) {
		String roomN;
		HomeRoomPanel roomPanel;

		Iterator<Room> iterRoom = roomList.iterator();
		Iterator<HomeRoomPanel> iterPanel = roomPanelList.iterator();

		while (iterRoom.hasNext()) {
			roomN = iterRoom.next().roomName;
			if (roomN.equals(roomName)) {
				iterRoom.remove();
				break;
			}

		}

		while (iterPanel.hasNext()) {
			roomPanel = iterPanel.next();
			if (roomPanel.room.roomName.equals(roomName)) {
				iterPanel.remove();

				if (roomPanel.roomSequence < 5)
					homeRoomLeftPanel.remove(roomPanel);

				else
					homeRoomRightPanel.remove(roomPanel);

				createRoomPanel(roomPanel.roomSequence);
				break;
			}
		}
	}

	public void setRoomPanel(Room room) {
		for (HomeRoomPanel panel : roomPanelList) {
			if (panel.room.roomName.equals("")) {
				panel.room = room;
				panel.lblRoom.setText(panel.room.roomName);
			}
		}
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}
}
