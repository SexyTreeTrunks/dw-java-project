package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class ChatPanel extends JPanel {

	private Main main;
	private Variables var;
	public String roomName;
	private int unreadChat;

	private JTextPane chatViewTextPane;
	private JTextArea textAreaChatSend;
	private JLabel lblChatName;

	public ChatPanel(Main mainFrame, String connectRoom) {
		main = mainFrame;
		var = main.getVar();
		var.getVO().setconnectRoom(connectRoom);
		roomName = connectRoom;
		unreadChat = 0;
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(Variables.CHAT_PANEL_SIZE);

		JPanel chatInfoPanel = new JPanel();
		chatInfoPanel.setBackground(Color.WHITE);
		add(chatInfoPanel, BorderLayout.NORTH);
		chatInfoPanel.setLayout(new BoxLayout(chatInfoPanel, BoxLayout.X_AXIS));

		JLabel lblClose = new JLabel();
		lblClose.setIcon(new ImageIcon("img/close_room.png"));
		lblClose.setBorder(new EmptyBorder(10, 15, 10, 15));
		chatInfoPanel.add(lblClose);
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				main.setMainCard("Home");
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		lblChatName = new JLabel(connectRoom);
		lblChatName.setFont(var.getFont(14));
		chatInfoPanel.add(lblChatName);
		chatInfoPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.lightGray));
		JPanel chatViewPanel = new JPanel();
		chatViewPanel.setBackground(Color.WHITE);
		chatViewPanel.setLayout(new BorderLayout(0, 0));
		add(chatViewPanel, BorderLayout.CENTER);

		chatViewTextPane = new JTextPane();
		chatViewTextPane.setEditable(false);
		chatViewTextPane.setFont(var.getFont(14));
		chatViewTextPane.setEditorKit(new WrapEditorKit());
		JScrollPane chatViewScrollPane = new JScrollPane(chatViewTextPane);

		chatViewPanel.add(chatViewScrollPane, BorderLayout.CENTER);
		chatViewScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatViewScrollPane.setPreferredSize(Variables.CHAT_VIEW_SIZE);
		chatViewScrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.lightGray));
		
		JPanel chatSendPanel = new JPanel();
		chatSendPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		chatSendPanel.setBackground(Color.WHITE);
		add(chatSendPanel, BorderLayout.SOUTH);
		chatSendPanel.setLayout(new BorderLayout(0, 0));

		textAreaChatSend = new JTextArea();
		textAreaChatSend.setLineWrap(true);
		textAreaChatSend.setColumns(50);
		textAreaChatSend.setRows(5);

		textAreaChatSend.setFont(var.getFont(14));
		
		JScrollPane chatSendScrollPane = new JScrollPane(textAreaChatSend);
		chatSendPanel.add(chatSendScrollPane, BorderLayout.CENTER);

		JLabel lblSend = new JLabel();
		lblSend.setPreferredSize(new Dimension(100, 100));
		lblSend.setIcon(new ImageIcon("img/chat_send.png"));
		lblSend.setBorder(new EmptyBorder(0, 25, 0, 0));
		lblSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		});
		chatSendPanel.add(lblSend, BorderLayout.EAST);

		textAreaChatSend.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.isShiftDown() || e.isControlDown())
						textAreaChatSend.append(System.lineSeparator());
					else {
						chatSend();
						e.consume();
					}
				}
			}
		});

		lblSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chatSend();
			}
		});
	}

	public void connectRoom() {
		lblChatName.setText(var.getVO().getconnectRoom());
	}

	private void chatViewAdd(String chat) {
		StyledDocument document = (StyledDocument) chatViewTextPane.getDocument();
		try {
			document.insertString(document.getLength(), chat, null);
			document.insertString(document.getLength(), System.lineSeparator(), null);
			chatViewTextPane.select(document.getLength(), document.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void chatClear() {
		chatViewTextPane.setText("");
		textAreaChatSend.setText("");
	}

	
	private void chatSend() {
		String chat = textAreaChatSend.getText();
		if (!chat.equals("")) {
			chatViewAdd(var.getVO().getUserName() + " : " + chat);
			textAreaChatSend.setText("");
			main.chatSend(chat);
		}
	}

	public void chatReceive(String data) {

		String[] message = data.split("\\|");
		String sendUser = message[1];

		String realData = "";
		if(message.length > 3) {
			for(int i =2 ; i<message.length; i++)
				realData += message[i] + "|";
			realData = realData.substring(0, realData.length()-1);
		}
		else
			realData += message[2];
		
		chatViewAdd(sendUser + " : " + realData);
	}
	
	// JTextPane Line Wrap
	class WrapEditorKit extends StyledEditorKit {
        ViewFactory defaultFactory=new WrapColumnFactory();
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

    }

    class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }
}
