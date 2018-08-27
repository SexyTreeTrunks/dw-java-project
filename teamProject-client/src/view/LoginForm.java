package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Font;

public class LoginForm extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField txtid;
	private JPasswordField txtpassword;
	private JButton loginbtn, signbtn;

	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://35.190.228.94:3306/teamdb?useSSL=false";
			con = DriverManager.getConnection(url, "root", "12345");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * Launch the application.
	 */
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

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		txtid = new JTextField();
		txtid.setBounds(203, 221, 116, 21);
		panel.add(txtid);
		txtid.setColumns(10);

		txtpassword = new JPasswordField();
		txtpassword.setBounds(203, 267, 116, 21);
		panel.add(txtpassword);
		txtpassword.setColumns(10);

		JLabel logolabel = new JLabel("LOGO");
		logolabel.setIcon(new ImageIcon("C:\\Users\\admin\\Desktop\\이미지\\zzitalk.png"));
		logolabel.setBounds(167, 102, 208, 67);
		panel.add(logolabel);

		JLabel pwlabel = new JLabel("New label");
		pwlabel.setIcon(new ImageIcon("C:\\Users\\admin\\Desktop\\이미지\\pw.png"));
		pwlabel.setBounds(134, 270, 82, 18);
		panel.add(pwlabel);

		JLabel idlabel = new JLabel("New label");
		idlabel.setIcon(new ImageIcon("C:\\Users\\admin\\Desktop\\이미지\\ddi.png"));
		idlabel.setBounds(149, 224, 57, 15);
		panel.add(idlabel);

		signbtn = new JButton("s i g n u p");
		signbtn.setForeground(Color.BLACK);
		signbtn.setBackground(Color.WHITE);
		signbtn.setFont(new Font("휴먼고딕", Font.BOLD, 12));
		signbtn.setBounds(213, 298, 116, 31);
		panel.add(signbtn);

		loginbtn = new JButton("New button");
		loginbtn.setIcon(new ImageIcon("C:\\Users\\admin\\Desktop\\이미지\\login.png"));
		loginbtn.setBounds(350, 239, 142, 31);
		panel.add(loginbtn);

		signbtn.addActionListener(this);
		loginbtn.addActionListener(this);
	}

	@Override // 로그인 가능 여부
	public void actionPerformed(ActionEvent e) {

		String id = txtid.getText(); // id 와 password text 얻어오기
		String password = txtpassword.getText();
		String sql = "";

		if (e.getSource().equals(loginbtn)) {
			con = getConnection();
			sql = "select * from usertbl where id='"+ id +"' and password = '" + password +"'";
			try {pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs.next() == true) {
					JOptionPane.showMessageDialog(this, "로그인되었습니다.", "login", JOptionPane.OK_OPTION);
				} else {
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 일치하지 않습니다.", "login", JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		else {
			signupDialog sign = new signupDialog();
			sign.setVisible(true);
		}
	}

	// password 와 confirmpassword가 다르다면
	/*
	 * if(txtpassword.getText().isEmpty() == txtconfirm.getText().isEmpty()) {
	 * JOptionPane.showInputDialog("비밀번호가 일치하지않습니다.",JOptionPane.ERROR_MESSAGE); } }
	 */

}