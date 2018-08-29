package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.ClientVO;

public class SidePanel extends JPanel {
	private Main main;
	private Variables var;
	private Vector<String> userList;
	private JList<String> list;
	private ClientVO vo;
	public String roomName;
	public int roomLimit;
	public boolean roomCreate;

	public SidePanel(Main mainFrame) {
		main = mainFrame;
		var = main.getVar();
		vo = var.getVO();
		roomCreate = false;

		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(Variables.SIDE_PANEL_SIZE);

		userList = new Vector<String>();

		JPanel ChannelUserListPanel = new JPanel();
		add(ChannelUserListPanel);
		ChannelUserListPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		ChannelUserListPanel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		panel_1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		JLabel lblchannelList = new JLabel("채널 접속자");
		panel_1.add(lblchannelList);
		lblchannelList.setFont(var.getFont(18));
		lblchannelList.setHorizontalAlignment(SwingConstants.CENTER);
		lblchannelList.setPreferredSize(new Dimension(90, 15));

		JLabel lbluser = new JLabel(var.getVO().getUserName());
		lbluser.setFont(var.getFont(16));
		lbluser.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.white);
		panel_2.add(lbluser);
		panel_2.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
		panel_1.add(panel_2);

		JPanel panel = new JPanel();
		ChannelUserListPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		list = new JList<String>();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					String roomName = list.getSelectedValue();
					var.getVO().setconnectRoom(roomName);
					main.addChatPanel(roomName);
					main.setMainCard("Chat_" + roomName);
					list.clearSelection();
				}
			}
		});
		list.setFixedCellHeight(30);
		list.setFont(var.getFont(14));
		list.setBorder(new EmptyBorder(10, 10, 10, 10));
		list.setCellRenderer(getRenderer());
		panel.add(list, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		ChannelUserListPanel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton btnCreateRoom = new JButton("채팅방 만들기");
		btnCreateRoom.setBackground(Color.WHITE);
		panel_3.add(btnCreateRoom, BorderLayout.NORTH);
		btnCreateRoom.setFont(var.getFont(18));
		btnCreateRoom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CreateRoomDialog dialog = new CreateRoomDialog(vo.getUserName());
				dialog.setVisible(true);
				createRoom();
			}
		});
	}
	public void createRoom() {
		if(roomCreate) {
			main.createRoom(roomName, roomLimit);
			roomName = "";
			roomLimit = -1;
			roomCreate = false;
		}
		else
			return ;
	}
	

	private ListCellRenderer<? super String> getRenderer() {
		return new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
				return listCellRendererComponent;
			}
		};
	}

	public void changeUserList(String data) {
		// userList = Client's User List
		// clientList = Server's User List (Lastest)

		String[] dataSplitArr = data.split("\\|");
		ArrayList<String> clientList = new ArrayList<>();
		for (int i = 1; i < dataSplitArr.length; i++) {
			if (!vo.getUserName().equals(dataSplitArr[i]))
				clientList.add(dataSplitArr[i]);
		}
		// No User List
		if (userList.isEmpty())
			for (String client : clientList)
				userList.add(client);

		// Exist User List
		else {
			// Disconnected Remove
			Iterator<String> iter = userList.iterator();
			while (iter.hasNext()) {
				String s = iter.next();
				if (!clientList.contains(s)) {
					iter.remove();
					main.removeChatPanel(s);
				}
			}

			// Connected Add
			for (String client : clientList)
				if (!userList.contains(client))
					userList.add(client);
		}
		list.setListData(userList);
	}

	class CreateRoomDialog extends JDialog implements ActionListener {

		private final JPanel contentPanel = new JPanel();
		private JButton okBtn, cancelBtn;
		private JTextField textRoomName;
		private JTextField textLimit;

		public CreateRoomDialog(String userName) {
			setBackground(Color.WHITE);
			setTitle("채팅방 만들기");
			setBounds(100, 100, 368, 175);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBackground(Color.WHITE);
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				contentPanel.add(panel);
				panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				{
					JLabel lblRoomName = new JLabel("채팅방 이름");
					lblRoomName.setBackground(Color.WHITE);
					lblRoomName.setBorder(new EmptyBorder(20, 0, 0, 0));
					panel.add(lblRoomName);
				}
				{
					textRoomName = new JTextField("");
					textRoomName.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							JTextField j = (JTextField) e.getSource();
							if (j.getText().length() > 20)
								e.consume();
						}
					});
					panel.add(textRoomName);
					textRoomName.setColumns(10);
				}
			}
			{
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				contentPanel.add(panel);
				panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				{
					JLabel lblLimit = new JLabel("방 최대 인원");
					lblLimit.setBackground(Color.WHITE);
					lblLimit.setBorder(new EmptyBorder(20, 30, 0, 30));
					panel.add(lblLimit);
				}
				{
					textLimit = new JTextField();
					panel.add(textLimit);
					textLimit.setColumns(4);
					textLimit.addKeyListener(new KeyAdapter() {
						
						@Override
						public void keyTyped(KeyEvent e) {
							char c = e.getKeyChar();

							if (!Character.isDigit(c)) {
								e.consume();
								return;
							}
							
							if (!textLimit.getText().equals("") && Integer.parseInt(textLimit.getText()) > 100)
								textLimit.setText("99");
							
							super.keyTyped(e);
						}
					});
				}
			}
			setLocationRelativeTo(null);
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setBackground(Color.WHITE);
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
				{
					okBtn = new JButton("만들기");
					okBtn.setBackground(Color.WHITE);
					okBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
					okBtn.addActionListener(this);
					buttonPane.add(okBtn);

					cancelBtn = new JButton("취소");
					cancelBtn.setBackground(Color.WHITE);
					cancelBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
					buttonPane.add(cancelBtn);
					cancelBtn.addActionListener(this);
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();

			if (btn.equals(okBtn)) {
				if (textRoomName.getText().length() == 0 || textLimit.getText().length() == 0) {
					JOptionPane.showMessageDialog(this, "빈칸이 있습니다.", "확인", JOptionPane.ERROR_MESSAGE);
				}

				else {
					JOptionPane.showMessageDialog(this, "정상적으로 생성되었습니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
					roomName = textRoomName.getText();
					roomLimit = Integer.parseInt(textLimit.getText());
					if(roomLimit == 0)
						roomLimit = 999;
					
					roomCreate = true;
					this.dispose();
				}
			}

			else if (btn.equals(cancelBtn))
				this.dispose();
		}

	}
}
