package model;

import view.Variables;

public class UserVO {
	
	// UserID
	private String userID;
	
	// UserName
	private String userName;
	
	// ConnectRoom
	private String connectRoom;
	
	
	
	public String getconnectRoom() {
		return connectRoom;
	}

	public void setconnectRoom(String connectRoom) {
		this.connectRoom = connectRoom;
	}

	public UserVO(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return Variables.CLIENT_USER_DATA+"|" + userID + "|" + userName + "|" + connectRoom;
	}
	
}
