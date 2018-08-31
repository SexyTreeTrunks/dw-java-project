package main;

import java.util.ArrayList;
import java.util.Iterator;

public class Room {
	String roomName;
	int roomNumber;
	int personLimit;
	int personCurrent;
	ArrayList<String> persons;
	
	public Room() {
		roomName = "";
		persons = new ArrayList<>();
	}
	

	public Room(String roomName, int personLimit, String userName, int cRoomNumber) {
		persons = new ArrayList<>();
		this.roomName = roomName;
		this.personCurrent = 1;
		this.roomNumber = cRoomNumber + 1;
		persons.add(userName);

		if (personLimit == 0)
			this.personLimit = 1000;

		else
			this.personLimit = personLimit;
	}

	void enter(String userName) {
		persons.add(userName);
		personCurrent = persons.size();
	}

	void leave(String userName) {
		Iterator<String> iterator = persons.iterator();

		while (iterator.hasNext()) {
			String room = iterator.next();
			if (room.equals(userName))
				iterator.remove();
		}

		personCurrent = persons.size();
	}
}
