package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
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
	private HomePanel homePanel;
	private CardLayout mainCards;
	private String mainCardName;

	private ArrayList<ChatPanel> chatPanelList;
	// login -> main
/*	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.startClient();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	public Main(UserVO user) {
		var = new Variables(new ClientVO(user.getID(), user.getUsername()));
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
					dataSend();
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

			// textArea_ChatView.append(System.lineSeparator() + "서버와 연결이 끊어졌습니다.");
		} catch (IOException e) {
		}

	}

	private void receive() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						ByteBuffer byteBuffer = ByteBuffer.allocate(100);

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
						else if (data.startsWith(Variables.CLIENT_USER_LIST))
							sidePanel.changeUserList(data);

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
				if (c.roomName.equals(message[1]))
					c.chatReceive(data);
		}
	}
	
	public void chatSend(String chat) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					if (var.getVO().getconnectRoom() != null && !chat.equals("")) {
						String data = Variables.CLIENT_TEXT_SEND + "|" + var.getVO().getUserName() + "|"
								+ var.getVO().getconnectRoom() + "|" + chat;

						Charset charset = Charset.forName("UTF-8");
						ByteBuffer byteBuffer = charset.encode(data);
						socketChannel.write(byteBuffer);
						// textArea_ChatView.append(System.lineSeparator() + "나(" + vo.getUserName() +
						// ") : " + chat);
					}
				} catch (Exception e) {
					stopClient();
				}
			}

		};

		thread.start();
	}

	private void dataSend() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(var.getVO().toString());
					socketChannel.write(byteBuffer);
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}

	public Variables getVar() {
		return var;
	}

	public void setMainCard(String panel) {
		mainCards.show(mainCardPanel, panel);
		mainCardName = panel;
	}

	public void addChatPanel(String roomName) {
		ChatPanel chatPanel = new ChatPanel(this, roomName);
		chatPanelList.add(chatPanel);
		mainCardPanel.add("Chat_" + roomName, chatPanel);
		homePanel.addRoom(roomName);
	}
	
	public void removeChatPanel(String roomName) {
		setMainCard("Home");
		Iterator<ChatPanel> iter = chatPanelList.iterator();
		while (iter.hasNext()) {
			ChatPanel c = iter.next();
			if (c.roomName.equals(roomName)) {
				iter.remove();
				homePanel.removeRoom(roomName);
			}
		}
	}
}
