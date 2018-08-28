package view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;

import model.UserDAO;
import model.UserVO;

import java.awt.Font;

public class signupDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtid, txtnick;
	private JPasswordField txtpassword, txtconfirm;
	private JLabel nicklabel;
	private JButton okbtn, cancelbtn, confirmbtn_id, confirmbtn_nick;

	private boolean bool_id = false;
	private boolean bool_nick = false;
	private UserDAO userdao = new UserDAO();

	public static void main(String[] args) {
		try {
			signupDialog dialog = new signupDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public signupDialog() {
		setTitle("sign up");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBounds(0, 0, 434, 205);
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);

		JLabel idlabel = new JLabel("ID");
		idlabel.setBounds(55, 27, 57, 15);
		contentPanel.add(idlabel);

		JLabel passwordlabel = new JLabel("password");
		passwordlabel.setBounds(33, 60, 57, 15);
		contentPanel.add(passwordlabel);

		JLabel confirmlabel = new JLabel("Confirm password ");
		confirmlabel.setBounds(12, 98, 113, 15);
		contentPanel.add(confirmlabel);

		nicklabel = new JLabel("nickname");
		nicklabel.setBounds(33, 138, 57, 15);
		contentPanel.add(nicklabel);

		txtid = new JTextField();
		txtid.setBounds(139, 24, 116, 21);
		contentPanel.add(txtid);
		txtid.setColumns(10);
		txtid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				JTextField src = (JTextField) e.getSource();
				if (src.getText().length() > 20)
					e.consume();
			}
		});

		confirmbtn_id = new JButton("c o n f i r m");
		confirmbtn_id.setFont(new Font("휴먼고딕", Font.BOLD, 12));
		confirmbtn_id.setBackground(Color.WHITE);
		confirmbtn_id.setBounds(290, 21, 119, 28);
		contentPanel.add(confirmbtn_id);

		txtpassword = new JPasswordField();
		txtpassword.setBounds(139, 57, 116, 21);
		contentPanel.add(txtpassword);

		txtconfirm = new JPasswordField();
		txtconfirm.setBounds(137, 95, 118, 21);
		contentPanel.add(txtconfirm);

		txtnick = new JTextField();
		txtnick.setBounds(139, 135, 116, 21);
		contentPanel.add(txtnick);
		txtnick.setColumns(10);

		confirmbtn_nick = new JButton("c o n f i r m");
		confirmbtn_nick.setFont(new Font("휴먼고딕", Font.BOLD, 12));
		confirmbtn_nick.setBackground(Color.WHITE);
		confirmbtn_nick.setBounds(290, 134, 119, 28);
		contentPanel.add(confirmbtn_nick);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new LineBorder(new Color(0, 0, 0)));
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setBounds(0, 205, 434, 57);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
			{
				cancelbtn = new JButton("Cancel");
				cancelbtn.setFont(new Font("휴먼고딕", Font.BOLD, 12));
				cancelbtn.setBackground(Color.WHITE);

				cancelbtn.setBounds(238, 15, 79, 30);
				cancelbtn.setActionCommand("Cancel");
				buttonPane.add(cancelbtn);

				okbtn = new JButton("OK");
				okbtn.setBackground(Color.WHITE);
				okbtn.setFont(new Font("휴먼고딕", Font.BOLD, 12));
				okbtn.setBounds(117, 17, 79, 28);
				buttonPane.add(okbtn);

			}
		}
		confirmbtn_id.addActionListener(this);
		confirmbtn_nick.addActionListener(this);
		okbtn.addActionListener(this);
		cancelbtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String id = txtid.getText();
		char[] password = txtpassword.getPassword();
		char[] confirmpassword = txtconfirm.getPassword();
		String nickname = txtnick.getText();
		// 아이디 중복체크 여부
		if (e.getSource().equals(confirmbtn_id)) {
			UserVO user = userdao.getUserById(id);
			try {
				if (user != null) {
					JOptionPane.showMessageDialog(txtid, "해당 아이디는 현재 사용중입니다. 다시 작성해주세요.", "sign up",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(txtid, "사용가능한 아이디 입니다.", "sign up", JOptionPane.OK_OPTION);
					bool_id = true;
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		else if (e.getSource().equals(confirmbtn_nick)) {
			// 닉네임 중복체크 여부
			UserVO user = userdao.getUserByUsername(nickname);
			try {
				if (user != null) {
					JOptionPane.showMessageDialog(txtnick, "해당 닉네임은 현재 사용중입니다. 다시 작성해주세요.", "sign up",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showConfirmDialog(this, "사용가능한 닉네임입니다.", "sign up", JOptionPane.OK_OPTION);

					bool_nick = true;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		// ok버튼 클릭시
		// 모든 textfield에 빈칸이 있는지
		if (e.getSource().equals(okbtn)) {
			if ((txtid.getText().isEmpty() == true || txtpassword.getText().isEmpty() == true)
					|| (txtconfirm.getText().isEmpty() == true || txtnick.getText().isEmpty() == true)) {
				JOptionPane.showMessageDialog(this, "빈칸이 있습니다.", "sign up", JOptionPane.ERROR_MESSAGE);
			} else if (!bool_id) {
				JOptionPane.showMessageDialog(this, "아이디 중복 확인을 해주세요.", "sign up", JOptionPane.ERROR_MESSAGE);
			} else if (!bool_nick) {
				JOptionPane.showMessageDialog(this, "닉네임 중복 확인을 해주세요.", "sign up", JOptionPane.ERROR_MESSAGE);
			}

			else {
				try {
					int result = userdao.user_insert(txtid.getText(), txtnick.getText(),
							new String(txtpassword.getPassword()));
					dispose();
					if (result > 0) {
						JOptionPane.showConfirmDialog(this, "회원가입이 성공적으로 이루어졌습니다.", "sign up", JOptionPane.OK_OPTION);
						dispose();
					} else {
						JOptionPane.showMessageDialog(this, "다시 시도해주세요.", "sign up", JOptionPane.OK_OPTION);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		} else if (e.getSource() == cancelbtn) {
			this.setVisible(false);

		}
	}
}
