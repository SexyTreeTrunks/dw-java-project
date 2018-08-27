package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HomePanel extends JPanel {
	private Main main;
	private Variables var;
	private String lastPage;
	private JPanel homeCenterPanel;
	private HomeRoomPanel homeRoomPanel;
	private HomeNoRoomPanel homeNoRoomPanel;
	private CardLayout homeCards;
	private String homeCardName;

	public HomePanel(Main mainFrame) {
		main = mainFrame;
		var = main.getVar();
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
		homeRoomPanel = new HomeRoomPanel(main);

		homeCenterPanel = new JPanel();
		homeCenterPanel.setLayout(homeCards);
		homeCenterPanel.add("Exist", homeRoomPanel);
		homeCenterPanel.add("No", homeNoRoomPanel);

		homePanel.add(homeCenterPanel, BorderLayout.CENTER);
		setHomeCard();
	}

	public void setHomeCard() {
		String panel = "No";
		if (!homeRoomPanel.getRoomList().isEmpty())
			panel = "Exist";
		homeCards.show(homeCenterPanel, panel);
		homeCardName = panel;
	}

	public String gethomeCardName() {
		return homeCardName;
	}

	public void addRoom(String roomName) {
		if (!homeRoomPanel.getRoomList().contains(roomName)) {
			homeRoomPanel.addRoom(roomName);
			setHomeCard();
		}
	}
}
