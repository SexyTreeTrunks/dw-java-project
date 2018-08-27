package view;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import model.ClientVO;

public class Variables {
	// Server IP, Port
	// private static final String SERVER_IP = "35.190.228.94";
	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 3000;
	
	public static String CLIENT_USER_DATA = "001";
	public static String CLIENT_USER_LIST = "002";
	public static String CLIENT_TEXT_SEND = "100";
	
	
	public static Dimension SIDE_PANEL_SIZE = new Dimension(200, 600);
	public static Dimension MAIN_PANEL_SIZE = new Dimension(760, 600);
	public static Dimension MUSIC_PANEL_SIZE = new Dimension(760, 150);
	public static Dimension CHAT_PANEL_SIZE = new Dimension(760, 450);
	public static Dimension CHAT_VIEW_SIZE = new Dimension(760, 370);
	public static Dimension CHAT_SEND_SIZE = new Dimension(760, 80);
	
	private ClientVO vo;
	private ArrayList<String> roomList;
	
	public Variables(ClientVO vo) {
		this.vo = vo;
		roomList = new ArrayList<>();
	}
	
	public ClientVO getVO() {
		return vo;
	}
	
	public void setVO(ClientVO vo) {
		this.vo = vo;
	}
	
	public Font getFont(int size) {
		return new Font("맑은 고딕", Font.PLAIN, size);
	}
	
	public ArrayList<String> getroomList(){
		return roomList;
	}
	
	public void setroomList(ArrayList<String> roomList) {
		this.roomList = roomList;
	}
}
