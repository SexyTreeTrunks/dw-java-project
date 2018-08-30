package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.ClientVO;
import model.UserVO;

public class Main extends JFrame {
	private SocketChannel socketChannel;
	private Variables var;

	private JPanel contentPane;
	public SidePanel sidePanel;
	public MusicPlayer musicPanel;
	public JPanel mainCardPanel;
	public HomePanel homePanel;
	private CardLayout mainCards;
	private String mainCardName;

	private ArrayList<ChatPanel> chatPanelList;
	private ArrayList<ChatRoomPanel> chatRoomPanelList;

	public Main(UserVO user) {
		var = new Variables(new ClientVO(user.getID(), user.getUsername()));
		setTitle("ZZITALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1060, 700);
		contentPane = new JPanel();
		setResizable(false);
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(5, 5));
		mainCards = new CardLayout();
		chatPanelList = new ArrayList<ChatPanel>();
		chatRoomPanelList = new ArrayList<ChatRoomPanel>();
		setLocationRelativeTo(null);

		// Side Panel
		sidePanel = new SidePanel(this);
		contentPane.add(sidePanel, BorderLayout.WEST);

		// Main Panel (Music Panel, Chat Panel)
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.setPreferredSize(Variables.MAIN_PANEL_SIZE);

		// Music Panel
		musicPanel = new MusicPlayer();
		musicPanel.setPreferredSize(Variables.MUSIC_PANEL_SIZE);
		mainPanel.add(musicPanel, BorderLayout.NORTH);

		// Home & Chat Panel
		mainCardPanel = new JPanel();
		mainCardPanel.setPreferredSize(Variables.CHAT_PANEL_SIZE);
		mainCardPanel.setLayout(mainCards);
		homePanel = new HomePanel(this);

		mainCardPanel.add("Home", homePanel);
		mainPanel.add(mainCardPanel, BorderLayout.CENTER);
		setMainCard("Home");

		startClient();
	}

	private void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(true);
					socketChannel.connect(new InetSocketAddress(Variables.SERVER_IP, Variables.SERVER_PORT));

					// textArea_ChatView.append("서버에 접속하셨습니다.");

					// userID, userName
					userDataSend();
				} catch (Exception e) {
					System.out.println(e);
					if (socketChannel.isOpen())
						stopClient();

					return;
				}
				receive();
			}
		};
		thread.start();

	}

	private void stopClient() {
		try {
			if (socketChannel != null && socketChannel.isOpen())
				socketChannel.close();

		} catch (IOException e) {
		}

	}

	private void receive() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						ByteBuffer byteBuffer = ByteBuffer.allocate(1000);

						int byteCount = socketChannel.read(byteBuffer);
						if (byteCount == -1)
							throw new IOException();

						byteBuffer.flip();
						Charset charset = Charset.forName("UTF-8");
						String data = charset.decode(byteBuffer).toString();

						// 텍스트 채팅 수신
						if (data.startsWith(Variables.CLIENT_TEXT_SEND))
							chatReceive(data);

						// 유저 목록 수신
						else if (data.startsWith(Variables.CLIENT_USER_LIST)) {
							sidePanel.changeUserList(data);
							send(Variables.CLIENT_ROOM_REQUEST);
						}

						else if (data.startsWith(Variables.CLIENT_ROOM_SEND))
							chatRoomReceive(data);

						else if (data.startsWith(Variables.CLIENT_ROOM_LIST)
								|| data.startsWith(Variables.CLIENT_ROOM_UPDATE)
								|| data.startsWith(Variables.CLIENT_ROOM_ADD)
								|| data.startsWith(Variables.CLIENT_ROOM_REMOVE)) {
							changeRoomList(data);
						}
					} catch (Exception e) {
						e.printStackTrace();
						stopClient();
						break;
					}
				}
			}
		};
		thread.start();
	}

	private void chatReceive(String data) {
		String[] message = data.split("\\|");
		boolean isExist = false;
		for (ChatPanel c : chatPanelList)
			if (c.roomName.equals(message[1])) {
				c.chatReceive(data);
				isExist = true;
			}

		if (!isExist) {
			addChatPanel(message[1]);
			for (ChatPanel c : chatPanelList)
				if (c.roomName.equals(message[1])) {
					c.chatReceive(data);
				}
		}
		toFront();
		
		for (HomeRoomPanel panel : homePanel.homeRoomListPanel.roomPanelList) {
			if (!panel.room.roomName.equals(mainCardName.replace("Chat_", "")) && !panel.room.roomName.equals("")
					&& panel.room.roomNumber == 0) {
				unReadCount(panel);
				break;
			}
		}
	}

	private void chatRoomReceive(String data) {
		String[] message = data.split("\\|");
		boolean isExist = false;
		for (ChatRoomPanel c : chatRoomPanelList) {
			if (c.roomName.equals(message[1])) {
				c.chatReceive(data);		
				isExist = true;
			}
		}

		if (isExist) {
			toFront();
			for (HomeRoomPanel panel : homePanel.homeRoomListPanel.roomPanelList) {
				if (!panel.room.roomName.equals(mainCardName.replace("Chat_", ""))
						&& panel.room.roomName.equals(message[1])
						&& !panel.room.roomName.equals("")
						&& panel.room.roomNumber != 0) {
					unReadCount(panel);
					break;
				}
			}
		}
	}

	private void unReadCount(HomeRoomPanel panel) {
		panel.roomChatCount += 1;
		panel.lblChatCount.setText(panel.roomChatCount + "");
		panel.lblChatCount.setBackground(new Color(255, 91, 73));
		panel.lblChatCount.setVisible(true);
	}

	public void chatSend(String chat) {
		if (var.getVO().getconnectRoom() != null && !chat.equals("")) {
			String data = Variables.CLIENT_TEXT_SEND + "|" + var.getVO().getUserName() + "|"
					+ var.getVO().getconnectRoom() + "|" + chat;

			send(data);
		}
	}

	private void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {

					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(data);
					socketChannel.write(byteBuffer);
				} catch (Exception e) {
					stopClient();
				}
			}

		};

		thread.start();
	}

	private void userDataSend() {
		send(var.getVO().toString());
	}

	public Variables getVar() {
		return var;
	}

	public void setMainCard(String panel) {
		mainCards.show(mainCardPanel, panel);
		mainCardName = panel;
	}

	public void addChatRoomPanel(Room room) {
		boolean contain = false;
		for (ChatRoomPanel c : chatRoomPanelList) {
			if (c.roomName.equals(room.roomName)) {
				contain = true;
				break;
			}
		}

		if (!contain) {
			ChatRoomPanel chatRoomPanel = new ChatRoomPanel(this, room);
			chatRoomPanelList.add(chatRoomPanel);
			mainCardPanel.add("Chat_" + room.roomName, chatRoomPanel);
			enterRoom(room);
		}
	}

	public void removeChatRoomPanel(Room room) {

		Iterator<ChatRoomPanel> iter = chatRoomPanelList.iterator();
		while (iter.hasNext()) {
			ChatRoomPanel c = iter.next();
			if (c.roomName.equals(room.roomName)) {
				iter.remove();
				leaveRoom(room);
			}
		}
	}

	public void addChatPanel(String roomName) {
		boolean contain = false;

		for (ChatPanel c : chatPanelList) {
			if (c.roomName.equals(roomName)) {
				contain = true;
				break;
			}
		}

		// Already Exist
		if (!contain) {
			ChatPanel chatPanel = new ChatPanel(this, roomName);
			chatPanelList.add(chatPanel);
			mainCardPanel.add("Chat_" + roomName, chatPanel);
			homePanel.addRoom(roomName);
		}
	}

	public void removeChatPanel(String user) {

		Iterator<ChatPanel> iter = chatPanelList.iterator();
		while (iter.hasNext()) {
			ChatPanel c = iter.next();
			if (c.roomName.equals(user)) {
				iter.remove();
				homePanel.removeRoom(user);
			}
		}

		if (mainCardName.replaceAll("Chat_", "").equals(user)) {
			ChatDisconnectDialog dialog = new ChatDisconnectDialog(user);
			dialog.setVisible(true);
			setMainCard("Home");
		}
	}

	public void changeRoomList(String data) {
		String[] dataArr = data.split("\\|");

		if (dataArr[0].equals(Variables.CLIENT_ROOM_LIST))
			homePanel.addRoomList(dataArr);

		else if (dataArr[0].equals(Variables.CLIENT_ROOM_ADD))
			homePanel.addRoom(dataArr);

		else if (dataArr[0].equals(Variables.CLIENT_ROOM_REMOVE))
			homePanel.removeRoom(dataArr);

		else if (dataArr[0].equals(Variables.CLIENT_ROOM_UPDATE))
			homePanel.updateRoom(dataArr);
	}

	public void createRoom(String roomName, int roomLimit) {
		String data = Variables.CLIENT_ROOM_DATA;
		data += "|" + roomName + "|" + roomLimit;
		send(data);
	}

	public void enterRoom(Room room) {
		String data = Variables.CLIENT_ROOM_ENTER;
		data += "|" + room.roomNumber + "|" + room.roomName + "|" + var.getVO().getUserName();
		send(data);
	}

	public void leaveRoom(Room room) {
		String data = Variables.CLIENT_ROOM_LEAVE;
		data += "|" + room.roomNumber + "|" + room.roomName + "|" + var.getVO().getUserName();
		send(data);
	}

	public void openChat(String roomName) {
		var.getVO().setconnectRoom(roomName);
		addChatPanel(roomName);
		setMainCard("Chat_" + roomName);
	}

	public void openChat(Room room) {
		String roomName = room.roomName;
		if (!roomName.equals("")) {
			var.getVO().setconnectRoom("Room_" + roomName);
			addChatRoomPanel(room);
			setMainCard("Chat_" + roomName);
		}
	}
}
