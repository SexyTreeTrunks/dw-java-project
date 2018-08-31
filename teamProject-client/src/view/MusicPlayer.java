package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.List;
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
import jaco.mp3.player.c;
import sun.java2d.pipe.ValidatePipe;

public class MusicPlayer extends JPanel implements ActionListener {
	
	// 占쏙옙튼
	public JButton start_btn, stop_btn, music_plus_btn, music_delete_btn;

	// 占싻놂옙
	public JPanel panel, panel_1, panel_2;

	// 占쏙옙占� 占쏙옙占� (占쏙옙占쏙옙 클占쏙옙占쏙옙 占쏙옙占�)
	public JList list;
	public DefaultListModel playList = new DefaultListModel();
	public DefaultListModel absolutePath = new DefaultListModel();
	// public Vector<String> playList = new Vector<>();
	// public Vector<String> absolutePath = new Vector<>();

	// mp3 api
	public MP3Player mp3 = new MP3Player();

	// 占쏙옙占쏙옙 탐占쏙옙占쏙옙
	public JFileChooser fileChooser = new JFileChooser();
	public FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("mp3�뙆�씪", "mp3");

	// 占쏙옙占쏙옙占쏙옙 (클占쏙옙占쏙옙 占쏙옙占쏙옙)

	// 占쏙옙占� 占쏙옙 占싹쏙옙占쏙옙占쏙옙
	public boolean paused;
	public boolean playing = false;
	public boolean stoped;
	public boolean add;
	public boolean addPlayList;
	public String current_song = "";
	public boolean deleteAfterStart;

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
		lblNewLabel.setFont(new Font("HY�뿤�뱶�씪�씤M", Font.BOLD, 22));
		scrollPane.setColumnHeaderView(listHeaderPanel);
		scrollPane.setBackground(Color.white);

		list = new JList(playList);
		list.setBackground(Color.WHITE);
		scrollPane.setViewportView(list);

		// 占쏙옙튼 占싱븝옙트 (占쌓쇽옙 占쏙옙占쏙옙占쏙옙)
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
		music_delete_btn.addActionListener(this);
		music_plus_btn.addActionListener(this);
		
		
	}
	
	
	
	// �옱�깮紐⑸줉 異붽� 硫붿꽌�뱶
	public void addMp3PlayerListener() {
		fileChooser.setDialogTitle("Open Audio File");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));
		fileChooser.setMultiSelectionEnabled(true);
		int option = fileChooser.showOpenDialog(this);
		if (option == fileChooser.APPROVE_OPTION) {
			
			File selectedFile[] = fileChooser.getSelectedFiles();

			for (int x = 0; x < selectedFile.length; x++) {
				File file = selectedFile[x];
				mp3.addToPlayList(file);
				playList.addElement(file.getName());

			}
		} else {

		}

	}

	// �옱�깮紐⑸줉 �꽑�깮 諛� �쟾泥� �궘�젣
	public void removeMp3PlayerListener() {
		if (playList.getSize() > 0) {
			if (list.getSelectedIndex() >= 0) {
				try {
					mp3.stop();
					
					start_btn.setIcon(new ImageIcon("img\\play.png"));
					
					int selected = list.getLeadSelectionIndex();
					
					mp3.getPlayList().remove(selected);
					playList.removeElementAt(selected);
					
					deleteAfterStart = true;
					
					try {
						SkipForword();
						
						playing = true;
						
						pauseImg();
						
					} catch (ArrayIndexOutOfBoundsException e) {
						
					} catch (Exception e) {
						
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO: handle exception
				} catch (Exception e) {
					
				}
				
			} else if (list.getSelectedIndex() == -1) {
				mp3.stop();
				stopImg();
				playList.removeAllElements();
				mp3.removeAll();
				playing = false;
				mp3 = new MP3Player();
			}
		} else if (playList.getSize() < 0) {
			
		}

	}

	// �옱�깮 硫붿꽌�뱶
	public void PlayMp3PlayerListener() {
		if (playList.getSize() > 0) {
			if (playing == false) {
				mp3.play();
				pauseImg();
				playing = true;
			} else if (playing == true) {
				mp3.pause();
				pauseImg();
				playing = false;
			}
			
		} else if (playList.getSize() < 0) {
			
		}
	}
	
	public void stopImg() {
		if(mp3.isStopped() == true) {
			start_btn.setIcon(new ImageIcon("img\\play.png"));
		}
	}
	
	public void pauseImg() {
		if(mp3.isPaused() == true) {
			start_btn.setIcon(new ImageIcon("img\\play.png"));
		}else if(mp3.isPaused() == false || mp3.isStopped() == false) {
			start_btn.setIcon(new ImageIcon("img\\pause.png"));
		}
	}
	
	public void SkipForword() {
		mp3.skipForward();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// �옱�깮 踰꾪듉
		if (e.getSource() == start_btn) {
			PlayMp3PlayerListener();
		}

		// 硫덉땄 踰꾪듉
		if (e.getSource() == stop_btn) {
			playing = false;
			mp3.stop();
			stopImg();
		}

		// �옱�깮 紐⑸줉 異붽�
		if (e.getSource() == music_plus_btn) {
			addMp3PlayerListener();
		}

		// �옱�깮 紐⑸줉 �궘�젣
		if (e.getSource() == music_delete_btn) {
			removeMp3PlayerListener();
		}

	}

}