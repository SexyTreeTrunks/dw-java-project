package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import model.UserDAO;
import model.UserVO;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class LoginForm extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField txtid;
	private JPasswordField txtpassword;
	private JButton loginbtn, signbtn;
	private UserDAO userdao = new UserDAO();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginForm() {
		setTitle("ZZITALK");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 464);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		txtid = new JTextField();
		txtid.setBounds(203, 221, 116, 21);
		panel.add(txtid);
		txtid.setColumns(20);
		txtpassword = new JPasswordField();
		txtpassword.setBounds(203, 267, 116, 21);
		panel.add(txtpassword);
		txtpassword.setColumns(20);
		txtpassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					authLogin();
			}
		});
		

		JLabel logolabel = new JLabel("LOGO");
		logolabel.setIcon(new ImageIcon("img\\zzitalk.png"));
		logolabel.setBounds(167, 102, 208, 67);
		panel.add(logolabel);

		JLabel pwlabel = new JLabel("");
		pwlabel.setIcon(new ImageIcon("img\\pw.png"));
		pwlabel.setBounds(134, 270, 82, 18);
		panel.add(pwlabel);

		JLabel idlabel = new JLabel("");
		idlabel.setIcon(new ImageIcon("img\\id.png"));
		idlabel.setBounds(149, 224, 82, 15);
		panel.add(idlabel);

		signbtn = new JButton("s i g n u p");
		signbtn.setForeground(Color.BLACK);
		signbtn.setBackground(Color.WHITE);
		signbtn.setFont(new Font("휴먼고딕", Font.BOLD, 12));
		signbtn.setBounds(203, 308, 116, 31);
		panel.add(signbtn);

		loginbtn = new JButton("New button");
		loginbtn.setIcon(new ImageIcon("img\\login.png"));
		loginbtn.setBounds(344, 238, 142, 31);
		panel.add(loginbtn);

		signbtn.addActionListener(this);
		loginbtn.addActionListener(this);
	}

	@Override // 로그인 가능 여부
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(loginbtn)) {
			authLogin();
		} else if(e.getSource().equals(signbtn)){
			signupDialog sign = new signupDialog();
			sign.setVisible(true);
		}
	}
	
	public void authLogin() {
		String id = txtid.getText(); // id 와 password text 얻어오기
		String password = new String(txtpassword.getPassword());
		UserVO user = userdao.getUser(id, password);
		try {
			if (user != null) {
				JOptionPane.showMessageDialog(this, "로그인되었습니다.", "login", JOptionPane.OK_OPTION);
				
				new Main(user).setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 일치하지 않습니다.", "login", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// password 와 confirmpassword가 다르다면
	/*
	 * if(txtpassword.getText().isEmpty() == txtconfirm.getText().isEmpty()) {
	 * JOptionPane.showInputDialog("비밀번호가 일치하지않습니다.",JOptionPane.ERROR_MESSAGE); } }
	 */
	
	/*
	 * if(!nickname.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*") { //특수문자가 있을 경우 } else { //특수문자가 없을 경우 }
	 */
}