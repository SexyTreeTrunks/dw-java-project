package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MusicPanel extends JPanel {
	public MusicPanel() {
		setBackground(Color.WHITE);
		setPreferredSize(Variables.MUSIC_PANEL_SIZE);
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
	}
}
