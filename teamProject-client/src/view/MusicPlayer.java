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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Vector;

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
import sun.java2d.pipe.ValidatePipe;

public class MusicPlayer extends JPanel implements ActionListener {

	// ��ư
	public JButton start_btn, stop_btn, music_plus_btn, music_delete_btn;

	// �г�
	public JPanel panel, panel_1, panel_2;

	// ��� ��� (���� Ŭ���� ���)
	public JList list;
	public DefaultListModel playList = new DefaultListModel();
	public DefaultListModel absolutePath = new DefaultListModel();
	// public Vector<String> playList = new Vector<>();
	// public Vector<String> absolutePath = new Vector<>();

	// mp3 api
	public MP3Player mp3 = new MP3Player();

	// ���� Ž����
	public JFileChooser fileChooser = new JFileChooser();
	public FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("mp3파일", "mp3");

	// ������ (Ŭ���� ����)

	// ��� �� �Ͻ�����
	public boolean paused;
	public boolean playing;
	public boolean stoped;
	public boolean add = false;
	public String current_song = "";

	/**
	 * Create the panel.
	 */
	public MusicPlayer() {
		setBackground(Color.WHITE);

		setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		start_btn = new JButton("");
		start_btn.setIcon(new ImageIcon("img\\play.png"));
		panel.add(start_btn);

		stop_btn = new JButton("");
		stop_btn.setIcon(new ImageIcon("img\\stop.png"));
		panel.add(stop_btn);

		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_1.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));

		music_plus_btn = new JButton("");
		music_plus_btn.setIcon(new ImageIcon("img\\ipod_add.png"));
		panel_2.add(music_plus_btn);

		music_delete_btn = new JButton("");
		music_delete_btn.setIcon(new ImageIcon("img\\ipod_remove.png"));
		panel_2.add(music_delete_btn);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("\uC74C\uC545\uC7AC\uC0DD\uBAA9\uB85D");
		JPanel listHeaderPanel = new JPanel();
		listHeaderPanel.setLayout(new BorderLayout());
		listHeaderPanel.add(lblNewLabel, BorderLayout.CENTER);
		listHeaderPanel.setBackground(Color.white);
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("HY헤드라인M", Font.BOLD, 22));
		scrollPane.setColumnHeaderView(listHeaderPanel);
		scrollPane.setBackground(Color.white);

		list = new JList(playList);
		list.setBackground(Color.WHITE);
		scrollPane.setViewportView(list);

		// ��ư �̺�Ʈ (�׼� ������)
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
		music_delete_btn.addActionListener(this);
		music_plus_btn.addActionListener(this);

	}

	// 재생목록 추가 메서드
	public void addMp3PlayerListener() {
		fileChooser.setDialogTitle("Open Audio File");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));
		fileChooser.setMultiSelectionEnabled(true);
		int option = fileChooser.showOpenDialog(this);
		if (option == fileChooser.APPROVE_OPTION) {
			if (add == true) {
				playing = false;
				stoped = false;

				File selectedFile[] = fileChooser.getSelectedFiles();

				for (int x = 0; x < selectedFile.length; x++) {
					File file = selectedFile[x];
					mp3.addToPlayList(file);
					playList.addElement(file.getName());
				}
			} else {

			}

		}

	}

	// 재생목록 선택 및 전체 삭제
	public void removeMp3PlayerListener() {
		if (list.getSelectedIndex() >= 0) {
			try {
				mp3.remove(list.getSelectedIndex());
				playList.remove(list.getSelectedIndex());
			} catch (Exception e) {
			}
		} else {
			start_btn.setIcon(new ImageIcon("img/play.png"));
			mp3.stop();
			mp3.removeAll();
			playList.clear();
		}
	}

	// 재생 메서드
	public void PlayMp3PlayerListener() {
		// 처음 시작 시 재생목록 없으면 무반응, 있으면 첨부터 시작
		if (playing == false && stoped == false && add == true) {
			start_btn.setIcon(new ImageIcon("img/pause.png"));
			mp3.play();
			playing = true;
			paused = false;

			// 일시 정지 했을 때
		} else if (paused == false && playing == true) {
			start_btn.setIcon(new ImageIcon("img/play.png"));
			mp3.pause();
			paused = true;
			playing = false;
			// 일시 정지 후 재시작
		} else if (paused == true && playing == false) {
			mp3.play();
			paused = false;
			playing = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == music_delete_btn) {
			removeMp3PlayerListener();
		} else if (e.getSource() == start_btn) {
			PlayMp3PlayerListener();
		} else if (e.getSource() == music_plus_btn) {
			add = true;
			addMp3PlayerListener();
		} else if (e.getSource() == stop_btn) {
			start_btn.setIcon(new ImageIcon("img/play.png"));
			mp3.stop();
			playing = false;
			stoped = false;
		}

	}
}
