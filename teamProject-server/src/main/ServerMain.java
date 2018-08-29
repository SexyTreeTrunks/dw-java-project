package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

	public static String CLIENT_USER_DATA = "001";
	public static String CLIENT_USER_LIST = "002";
	public static String CLIENT_ROOM_DATA = "011";
	public static String CLIENT_ROOM_LIST = "012";
	public static String CLIENT_ROOM_ENTER = "013";
	public static String CLIENT_ROOM_LEAVE = "014";
	public static String CLIENT_ROOM_ADD = "015";
	public static String CLIENT_ROOM_REMOVE = "016";
	public static String CLIENT_ROOM_UPDATE = "017";
	public static String CLIENT_TEXT_SEND = "100";
	public static String CLIENT_ROOM_SEND = "200";
	public ArrayList<Room> roomList;

	ExecutorService exeService;
	ServerSocketChannel serverSocketChannel;
	ServerSocketChannel serverSocketDataChannel;
	List<Client> connections = new Vector<Client>();

	void startServer() {
		// ThreadPool Create
		roomList = new ArrayList<Room>();
		exeService = Executors.newCachedThreadPool();
		// ServerSocketChannel Create;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.bind(new InetSocketAddress(3000));
		} catch (Exception e) {
			if (serverSocketChannel.isOpen())
				stopServer();
		}

		// Thread Create
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				System.out.println("Start Server");
				while (true) {
					try {
						// SocketChannel Accept by ServerSocketChannel
						SocketChannel socketChannel = serverSocketChannel.accept();

						System.out.println("Connection Accepted : " + socketChannel.getRemoteAddress() + " : "
								+ Thread.currentThread().getName());
						new Client(socketChannel);

					} catch (Exception e) {
						if (serverSocketChannel.isOpen())
							stopServer();
					}
				}
			}
		};

		// Thread Start on TreadPool
		exeService.submit(runnable);
	}

	void stopServer() {
		try {
			System.out.println("Stop Server");
			Iterator<Client> iterator = connections.iterator();

			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.sc.close();
				iterator.remove();
			}

			if (serverSocketChannel != null && serverSocketChannel.isOpen())
				serverSocketChannel.close();

			if (exeService != null && exeService.isShutdown())
				exeService.shutdown();

		} catch (Exception e) {
		}
	}

	class Client {
		SocketChannel sc;
		String userID;
		String userName;
		String connectRoom;

		public Client(SocketChannel sc) {
			this.sc = sc;
			receive();
		}

		private void receive() {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
							int byteCount = sc.read(byteBuffer);

							if (byteCount == -1)
								throw new IOException();

							System.out.println("Requset process : " + sc.getRemoteAddress() + " : "
									+ Thread.currentThread().getName());

							byteBuffer.flip();
							Charset charset = Charset.forName("UTF-8");
							String data = charset.decode(byteBuffer).toString();

							// Chat Text Send
							if (data.startsWith(CLIENT_TEXT_SEND))
								chatSend(data);

							// Client Add & Client List Refresh && Room List Refresh
							else if (data.startsWith(CLIENT_USER_DATA))
								clientListAdd(data);

							// Room Add
							else if (data.startsWith(CLIENT_ROOM_DATA))
								roomListAdd(data);

							// Room Enter
							else if (data.startsWith(CLIENT_ROOM_ENTER))
								roomEnter(data);

							// Room Leave
							else if (data.startsWith(CLIENT_ROOM_LEAVE))
								roomLeave(data);

						} catch (Exception e) {
							try {
								connections.remove(Client.this);

								System.out.println("Client Not Connected" + sc.getRemoteAddress() + " : "
										+ Thread.currentThread().getName());
								clientListRefresh();
								sc.close();
							} catch (Exception e2) {
							}
							break;
						}
					}
				}
			};
			exeService.submit(runnable);
		}

		void send(String data) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					try {
						Charset charset = Charset.forName("UTF-8");
						ByteBuffer byteBuffer = charset.encode(data);
						sc.write(byteBuffer);
					} catch (Exception e) {
						try {
							System.out.println("Client Not Connected" + sc.getRemoteAddress() + " : "
									+ Thread.currentThread().getName());

							connections.remove(Client.this);
							sc.close();
							clientListRefresh();

						} catch (Exception e2) {
						}
					}
				}
			};

			exeService.submit(runnable);
		}

		void chatSend(String data) {
			String[] dataArr = data.split("\\|");
			
			if (dataArr[0].equals(CLIENT_TEXT_SEND)) {
				String sender = dataArr[1];
				String receiver = dataArr[2];
				String realData = "";
				if (dataArr.length > 4)
					for (int i = 3; i < dataArr.length; i++)
						realData += "|" + dataArr[i];
				else
					realData += "|" + dataArr[3];
				String message = dataArr[0] + "|" + sender + realData;

				// 1:1 chat
				if (!receiver.startsWith("Room_"))
					clientSend(message, receiver);
				
				// 1:n chat
				else
					roomSend(message, Integer.parseInt(receiver.replaceAll("Room_", "")));
			}
		}

		void clientListAdd(String data) {

			// Client add
			String[] userData = data.split("\\|");
			if (userData[0].equals(CLIENT_USER_DATA)) {
				userID = userData[1];
				userName = userData[2];
				connectRoom = userData[3];
			}
			if (!connections.contains(this))
				connections.add(this);

			System.out.println("Connections : " + connections.size());

			// Client List Refresh
			clientListRefresh();
		}

		void clientListRefresh() {
			String data = CLIENT_USER_LIST;
			for (Client client : connections)
				data += "|" + client.userName;

			for (Client client : connections)
				client.send(data);
		}

		void roomListAdd(String data) {
			// Room List add
			String[] roomData = data.split("\\|");

			if (roomData[0].equals(CLIENT_ROOM_DATA)) {
				String roomName = roomData[1];

				for (Room r : roomList)
					if (r.roomName.equals(roomName))
						return;

				Room room = new Room(roomName, Integer.parseInt(roomData[2]), this.userName, roomList.size());

				roomList.add(room);
				roomChange(CLIENT_ROOM_ADD, room);
			}
		}

		void roomListRefresh() {
			String data = CLIENT_ROOM_LIST;
			if(roomList.size() == 0)
				return;
			// roomNumber_roomName_roomCurrent_roomLimit_roomPersonList
			for (Room room : roomList)
				data += getRoomData(data, room);
		

			send(data);
		}

		void roomEnter(String data) {
			String[] roomData = data.split("\\|");

			if (roomData[0].equals(CLIENT_ROOM_ENTER)) {
				int roomNumber = Integer.parseInt(roomData[1]);
				String roomName = roomData[2];

				for (Room room : roomList) {

					// Room Change
					if (room.roomName.equals(roomName) && room.roomNumber == roomNumber) {
						room.enter(userName);
						roomChange(CLIENT_ROOM_UPDATE, room);
					}
				}
			}
		}

		void roomLeave(String data) {
			String[] roomData = data.split("\\|");

			if (roomData[0].equals(CLIENT_ROOM_LEAVE)) {
				int roomNumber = Integer.parseInt(roomData[1]);
				String roomName = roomData[2];

				Iterator<Room> iterator = roomList.iterator();
				while (iterator.hasNext()) {
					Room room = iterator.next();
					if (room.roomName.equals(roomName) && room.roomNumber == roomNumber) {
						room.leave(userName);

						// Room Person : 0 -> Room Remove
						if (room.personCurrent == 0) {
							roomChange(CLIENT_ROOM_REMOVE, room);
							iterator.remove();
						}

						// Room Update
						else
							roomChange(CLIENT_ROOM_UPDATE, room);

					}
				}
			}
		}

		void roomChange(String code, Room room) {
			clientSend(getRoomData(code, room));
		}

		String getRoomData(String data, Room room) {
			data += "|" + room.roomNumber + "_" + room.roomName + "_" + room.personCurrent + "_ " + room.personLimit
					+ "_";
			for (String userName : room.persons)
				data += "-" + userName;

			return data;
		}
		
		Room getRoomByNumber(int roomNumber) {
			for(Room room : roomList)
				if(room.roomNumber == roomNumber)
					return room;
			
			return null;
		}

		void roomSend(String message, int roomNumber) {
			clientSend(message, getRoomByNumber(roomNumber).persons);
		}
		

		// Client All Send
		void clientSend(String data) {
			for (Client client : connections)
				client.send(data);
		}

		// Client Room Send
		void clientSend(String data, ArrayList<String> persons) {
			for (Client client : connections)
				if (persons.contains(client.userName))
					client.send(data);
		}

		// Client 1:1 Send
		void clientSend(String data, String userName) {
			for (Client client : connections)
				if (client.userName.equals(userName))
					client.send(data);
		}
	}

	public static void main(String[] args) {
		ServerMain main = new ServerMain();
		main.startServer();
	}
}
