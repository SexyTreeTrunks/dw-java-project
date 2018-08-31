package view;

import java.util.ArrayList;

public class Room {
	public String roomName;
	public int roomNumber;
	public int personLimit;
	public int personCurrent;
	public ArrayList<String> persons;
	
	public Room() {
		this.roomName = "";
	}
	
	public Room(String roomName) {
		this.roomName = roomName;
		this.roomNumber = 0;
	}
	
	
	public Room(int roomNumber, String roomName, int personCurrent, int personLimit, ArrayList<String> persons) {
		this.roomName = roomName;
		this.personLimit = personLimit;
		this.roomNumber = roomNumber;
		this.persons = persons;
		this.personCurrent = personCurrent;
	}
}
