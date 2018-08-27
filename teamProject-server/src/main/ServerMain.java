package main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

	public static String CLIENT_USER_DATA = "001";
	public static String CLIENT_USER_LIST = "002";
	public static String CLIENT_TEXT_SEND = "100";
	ExecutorService exeService;
	ServerSocketChannel serverSocketChannel;
	ServerSocketChannel serverSocketDataChannel;
	List<Client> connections = new Vector<Client>();

	void startServer() {
		// ThreadPool Create
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
		String connectUser;

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
							if (data.startsWith(CLIENT_TEXT_SEND)) {
								String[] dataArr = data.split("\\|");
								if (data.startsWith(CLIENT_TEXT_SEND) && dataArr[0].equals(CLIENT_TEXT_SEND)) {
									String sendUser = dataArr[1];
									String receiveRoom = dataArr[2];
									String realData = "";
									if (dataArr.length > 4)
										for (int i = 3; i < dataArr.length; i++)
											realData += "|" + dataArr[i];
									else
										realData += "|" + dataArr[3];
									String message = dataArr[0] + "|" + sendUser + realData;
									if (!receiveRoom.startsWith("Room_")) {
										for (Client client : connections)
											if (client.userName.equals(receiveRoom)) {
												client.send(message);
											}
									}

									else
										roomSend(message);
								}
							}

							// Client Add & Client List Refresh
							else if (data.startsWith(CLIENT_USER_DATA))
								clientListAdd(data);

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

		void roomSend(String data) {
			
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

		void clientListAdd(String data) {

			// Client add
			String[] userData = data.split("\\|");
			if (data.startsWith(CLIENT_USER_DATA) && userData[0].equals(CLIENT_USER_DATA)) {
				userID = userData[1];
				userName = userData[2];
				connectUser = userData[3];
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
	}

	public static void main(String[] args) {
		ServerMain main = new ServerMain();
		main.startServer();
	}

}
