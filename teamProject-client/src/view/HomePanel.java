package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HomePanel extends JPanel {
	private Main main;
	private Variables var;
	private String lastPage;
	private JPanel homeCenterPanel;
	private HomeRoomListPanel homeRoomListPanel;
	private HomeNoRoomPanel homeNoRoomPanel;
	private CardLayout homeCards;
	private String homeCardName;
	private ArrayList<Room> roomList;

	public HomePanel(Main mainFrame) {
		main = mainFrame;
		var = main.getVar();
		roomList = new ArrayList<>();
		lastPage = "1";
		setPreferredSize(Variables.CHAT_PANEL_SIZE);
		setLayout(new BorderLayout(0, 0));
		homeCards = new CardLayout();

		JPanel homePanel = new JPanel();
		homePanel.setPreferredSize(new Dimension(760, 450));
		add(homePanel);
		homePanel.setLayout(new BorderLayout(0, 0));

		JPanel homeBottomPanel = new JPanel();
		homeBottomPanel.setBackground(Color.WHITE);
		homeBottomPanel.setPreferredSize(new Dimension(760, 35));
		homePanel.add(homeBottomPanel, BorderLayout.SOUTH);
		homeBottomPanel.setLayout(new BorderLayout(0, 0));

		JPanel chatRoomListPanel = new JPanel();
		chatRoomListPanel.setBackground(Color.WHITE);
		homeBottomPanel.add(chatRoomListPanel, BorderLayout.CENTER);

		JLabel lblPrePage = new JLabel();
		lblPrePage.setIcon(new ImageIcon("img/pre_page_enable.png"));
		lblPrePage.setDisabledIcon(new ImageIcon("img/pre_page_disable.png"));
		chatRoomListPanel.add(lblPrePage);

		JLabel lblCurPage = new JLabel("");
		chatRoomListPanel.add(lblCurPage);
		JLabel lblNextPage = new JLabel();
		lblNextPage.setIcon(new ImageIcon("img/next_page_enable.png"));
		lblNextPage.setDisabledIcon(new ImageIcon("img/next_page_disable.png"));
		chatRoomListPanel.add(lblNextPage);
		lblPrePage.setBorder(new EmptyBorder(5, 0, 0, 0));
		lblCurPage.setBorder(new EmptyBorder(5, 0, 0, 0));
		lblNextPage.setBorder(new EmptyBorder(5, 0, 0, 5));

		lblCurPage.addPropertyChangeListener("text", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (lblCurPage.getText() == "1")
					lblPrePage.setEnabled(false);

				else
					lblPrePage.setEnabled(true);

				if (lblCurPage.getText() == lastPage)
					lblNextPage.setEnabled(false);

				else
					lblPrePage.setEnabled(true);
			}
		});
		lblCurPage.setText("1");

		homeNoRoomPanel = new HomeNoRoomPanel();
		homeRoomListPanel = new HomeRoomListPanel(main);

		homeCenterPanel = new JPanel();
		homeCenterPanel.setLayout(homeCards);
		homeCenterPanel.add("Exist", homeRoomListPanel);
		homeCenterPanel.add("No", homeNoRoomPanel);

		homePanel.add(homeCenterPanel, BorderLayout.CENTER);
		setHomeCard();
	}

	public void setHomeCard() {
		String panel = "No";
		if (homeRoomListPanel.getRoomList().size() != 0)
			panel = "Exist";
		homeCards.show(homeCenterPanel, panel);
		homeCardName = panel;
	}

	public String gethomeCardName() {
		return homeCardName;
	}

	public void addRoom(String roomName) {
		homeRoomListPanel.addRoom(roomName);
		setHomeCard();
	}

	public void removeRoom(String roomName) {
		homeRoomListPanel.removeRoom(roomName);
	}

	public void addRoom(String[] dataArr) {
		Room room = getRoomByData(dataArr[1].split("_"));
		roomList.add(room);
		homeRoomListPanel.addRoom(room);
	}

	public void removeRoom(String[] dataArr) {
		Room newRoom = getRoomByData(dataArr[1].split("_"));
		Iterator<Room> iter = roomList.iterator();
		while (iter.hasNext()) {
			Room room = iter.next();
			if (room.roomNumber == newRoom.roomNumber) {
				room = newRoom;
				
				break;
			}
		}
	}

	public void updateRoom(String[] dataArr) {
		int roomNumber = Integer.parseInt(dataArr[1].split("_")[0]);
		String roomName = dataArr[1].split("_")[1];
		Iterator<Room> iter = roomList.iterator();
		while (iter.hasNext()) {
			Room room = iter.next();
			if (room.roomName.equals(roomName) && room.roomNumber == roomNumber) {
				iter.remove();
				
				break;
			}
		}
	}

	public void addRoomList(String[] dataArr) {
		for (int i = 1; i < dataArr.length; i++) {
			Room room = getRoomByData(dataArr[i].split("_"));
			roomList.add(room);
			homeRoomListPanel.addRoom(room);
		}
	}

	public Room getRoomByData(String[] roomData) {
		int roomNumber = Integer.parseInt(roomData[0]);
		String roomName = roomData[1];
		int personCurrent = Integer.parseInt(roomData[2].trim());
		int personLimit = Integer.parseInt(roomData[3].trim());
		ArrayList<String> persons = new ArrayList<>();
		String[] personList = roomData[4].split("-");
		for (int i = 1; i < personList.length; i++)
			persons.add(personList[i]);

		return new Room(roomNumber, roomName, personCurrent, personLimit, persons);
	}
	
	public ArrayList<Room> getrRoomList(){
		return homeRoomListPanel.getRoomList();
	}
}	
