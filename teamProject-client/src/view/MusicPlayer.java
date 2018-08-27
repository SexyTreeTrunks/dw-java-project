package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import jaco.mp3.player.MP3Player;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;

public class MusicPlayer extends JPanel implements ActionListener {

	// 버튼
	public JButton start_btn, stop_btn, music_plus_btn, music_delete_btn, paused_btn;

	// 패널
	public JPanel panel, panel_1, panel_2;

	// 재생 목록 (현재 클래스 사용)
	public JList list;
	public DefaultListModel playlist = new DefaultListModel();
	public DefaultListModel absolute_path = new DefaultListModel();

	// mp3 api
	public MP3Player mp3 = new MP3Player();

	// 파일 탐색기
	public JFileChooser fileChooser = new JFileChooser();
	public FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("mp3파일", "mp3");

	// 스레드 (클래스 구현)
	PlayerThread playerThread;

	// 재상 및 일시정지
	public boolean paused = false;
	public boolean playing = false;
	public boolean stoped = false;
	public String current_song = "";

	/**
	 * Create the panel.
	 */
	public MusicPlayer() {

		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		start_btn = new JButton("");
		start_btn.setIcon(new ImageIcon("..\\MusicPlayer\\src\\images\\play.png"));
		panel.add(start_btn);

		paused_btn = new JButton("");
		paused_btn.setIcon(new ImageIcon("..\\MusicPlayer\\\\src\\\\images\\pause.png"));
		panel.add(paused_btn);

		stop_btn = new JButton("");
		stop_btn.setIcon(new ImageIcon("..\\MusicPlayer\\src\\images\\stop.png"));
		panel.add(stop_btn);

		panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		music_plus_btn = new JButton("");
		music_plus_btn.setIcon(new ImageIcon("..\\MusicPlayer\\src\\images\\ipod_add.png"));
		panel_2.add(music_plus_btn);

		music_delete_btn = new JButton("");
		music_delete_btn.setIcon(new ImageIcon("..\\MusicPlayer\\src\\images\\ipod_remove.png"));
		panel_2.add(music_delete_btn);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("\uC74C\uC545\uC7AC\uC0DD\uBAA9\uB85D");
		lblNewLabel.setToolTipText("");
		lblNewLabel.setLabelFor(lblNewLabel);
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("HY헤드라인M", Font.BOLD, 22));
		scrollPane.setColumnHeaderView(lblNewLabel);

		list = new JList(playlist);
		scrollPane.setViewportView(list);

		// 버튼 이벤트 (액션 리스너)
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
		paused_btn.addActionListener(this);
		music_delete_btn.addActionListener(this);
		music_plus_btn.addActionListener(this);
		list.addMouseListener(new MouseDoubleClick_List());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start_btn) {
			if(playing == false && paused == false) {
				try {
					playIt();
				} catch (IndexOutOfBoundsException e2) {}
			}else if(playing == false && paused == true) {
				mp3.play();
				playerThread = null;
			}else if(playing == true && paused == false) {
				stopIt();
				playIt();
			}
		} else if (e.getSource() == stop_btn) {
			stopIt();
		} else if (e.getSource() == paused_btn) {
			playing = false;
			paused = true;
			mp3.pause();
		} else if (e.getSource() == music_plus_btn) {
			fileChooser.setDialogTitle("Open Audio File");
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));
			fileChooser.setMultiSelectionEnabled(true);
			int option = fileChooser.showOpenDialog(this);
			if (option == fileChooser.APPROVE_OPTION) {
				playlist.addElement(fileChooser.getSelectedFile().getName());
				absolute_path.addElement(fileChooser.getSelectedFile().getAbsolutePath());

			}
		} else if (e.getSource() == music_delete_btn) {
			if (list.getSelectedIndex() > 0) {
				try {
					stopIt();
					playlist.remove(list.getSelectedIndex());
					absolute_path.remove(list.getSelectedIndex());
				} catch (Exception e2) {
				}

			} else {
				stopIt();
				playlist.clear();
				absolute_path.clear();
			}

		}

	}

	// 음악 플레이 했을 때
	public void playIt() {
		current_song = (String) absolute_path.getElementAt(list.getSelectedIndex());
		playerThread = new PlayerThread();
		playerThread.start();
		playing = true;
	}

	// 음악 정지 했을 때
	public void stopIt() {
		if (playing == true || paused == true) {
			mp3.stop();
			playing = false;
			paused = false;
			playerThread = null;
		}
	}

	// 쓰레드 (플레이시 사용할 스레드)
	public class PlayerThread extends Thread {
		public void run() {
			try {
				mp3 = new MP3Player(new File(current_song));
				mp3.play();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	// 마우스 더블클릭 이벤트
	class MouseDoubleClick_List extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 2) {
				stopIt();
				playIt();
			}
		}

	}

	// 파일 형식 제한
	public class MP3FileFilter implements FileFilter {
		public final String[] mp3_fileFormat = new String[] { "mp3" };

		@Override
		public boolean accept(File file) {
			for (String fileFormat : mp3_fileFormat) {
				if (file.getName().toLowerCase().endsWith(fileFormat)) {
					return true;
				}
			}
			return false;
		}
	}
}
