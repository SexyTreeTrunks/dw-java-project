package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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

	public SidePanel(Main mainFrame) {
		main = mainFrame;
		var = main.getVar();
		vo = var.getVO();
		
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

		JLabel lblchannelList = new JLabel("채널 접속자");
		panel_1.add(lblchannelList);
		lblchannelList.setFont(var.getFont(16));
		lblchannelList.setHorizontalAlignment(SwingConstants.CENTER);
		lblchannelList.setPreferredSize(new Dimension(90, 15));

		JLabel lbluser = new JLabel(var.getVO().getUserName());
		lbluser.setFont(var.getFont(16));
		lbluser.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lbluser);

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
					main.setMainCard("Chat_"+roomName);
					list.clearSelection();
				}
			}
		});
		list.setFixedCellHeight(30);
		list.setFont(var.getFont(14));
		list.setBorder(new EmptyBorder(10,10, 10, 10));
		list.setCellRenderer(getRenderer());
		panel.add(list, BorderLayout.CENTER);
	}
	
    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.GRAY));
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
}
