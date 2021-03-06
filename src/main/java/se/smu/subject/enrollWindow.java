package se.smu.subject;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class enrollWindow extends JFrame {
	// 해당 클래스의 기본적인 변수를 설정한다.
	protected JTextField text_Sem, text_Subname, text_Profname;
	protected String[] starthour = { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" };
	protected String[] endhour = { "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" };
	protected JButton completeButton;
	protected JLabel sem_label, subname_label, today_label, time_label, profname_label;
	protected JRadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7;
	protected JRadioButton[] rbs;
	protected JComboBox start_comboBox, end_comboBox;
	protected ButtonGroup grouprb;
	protected String today[] = { "월", "화", "수", "목", "금", "토", "일" };

	public enrollWindow(final String id) {
		// 기본적인 크기와 위치를 설정한다.
		setLocation(300, 300);
		setSize(520, 400);
		getContentPane().setLayout(null);

		// 포함된 기본 Swing객체들을 선언하고 초기화 한다.
		JLabel lblNewLabel = new JLabel("ID : " + id);
		lblNewLabel.setBounds(28, 16, 117, 18);
		getContentPane().add(lblNewLabel);

		completeButton = new JButton("완료");
		completeButton.setBounds(384, 12, 105, 27);
		getContentPane().add(completeButton);

		sem_label = new JLabel("년도-학기");
		sem_label.setBounds(44, 90, 87, 27);
		getContentPane().add(sem_label);

		subname_label = new JLabel("과목명");
		subname_label.setBounds(44, 130, 87, 27);
		getContentPane().add(subname_label);

		today_label = new JLabel("요일");
		today_label.setBounds(42, 169, 87, 27);
		getContentPane().add(today_label);

		time_label = new JLabel("시간");
		time_label.setBounds(42, 213, 87, 27);
		getContentPane().add(time_label);

		profname_label = new JLabel("교수명");
		profname_label.setBounds(42, 252, 87, 27);
		getContentPane().add(profname_label);

		text_Sem = new JTextField();
		text_Sem.setBounds(145, 91, 214, 27);
		getContentPane().add(text_Sem);
		text_Sem.setColumns(10);

		text_Subname = new JTextField();
		text_Subname.setBounds(145, 131, 214, 26);
		getContentPane().add(text_Subname);
		text_Subname.setColumns(10);

		text_Profname = new JTextField();
		text_Profname.setColumns(10);
		text_Profname.setBounds(145, 252, 214, 26);
		getContentPane().add(text_Profname);

		start_comboBox = new JComboBox(starthour);
		start_comboBox.setBounds(156, 214, 67, 24);
		getContentPane().add(start_comboBox);

		end_comboBox = new JComboBox(endhour);
		end_comboBox.setBounds(289, 214, 65, 24);
		getContentPane().add(end_comboBox);

		JLabel lblNewLabel_2 = new JLabel("   ~");
		lblNewLabel_2.setBounds(239, 217, 36, 18);
		getContentPane().add(lblNewLabel_2);

	}

	// 선택된 라디오 버튼의 텍스트를 반환한다.
	protected String selectedRadioContents(ButtonGroup gr) {
		Enumeration<AbstractButton> enums = gr.getElements();
		String contents = "";
		while (enums.hasMoreElements()) {
			AbstractButton ab = enums.nextElement();
			JRadioButton jb = (JRadioButton) ab;
			if (jb.isSelected())
				contents = jb.getText();
		}
		return contents;
	}

	// 테이블에 해당 데이터의 중복을 검사한다.
	protected boolean checkDupl(DefaultTableModel tm, Object[] row) {
		Vector data = tm.getDataVector();
		for (int i = 0; i < data.size(); i++) {
			Vector tmp = (Vector) data.get(i);
			int count = 0;
			for (int j = 0; j < 5; j++) {
				if (tmp.get(j).equals(row[j])) {
					count++;
				}
			}
			if (count == 5)
				return true;

		}
		return false;
	}

	// 라디오 버튼과 라디오 그룹을 초기화 한다.
	protected void setRB(String[] name, boolean[] selected) {
		rbs = new JRadioButton[7];
		grouprb = new ButtonGroup();

		for (int i = 0; i < rbs.length; i++) {
			rbs[i] = new JRadioButton(name[i], selected[i]);
			grouprb.add(rbs[i]);
			rbs[i].setBounds(139 + i * 50, 172, 50, 20);
			getContentPane().add(rbs[i]);
		}
	}

	// 등록 전 기본적인 사항들을 확인한다.
	protected boolean beforeEnroll(Object[] input_data, DefaultTableModel subtableModel) {
		input_data[0] = text_Sem.getText();
		input_data[1] = text_Subname.getText();
		input_data[2] = selectedRadioContents(grouprb);
		if (start_comboBox.getSelectedIndex() > end_comboBox.getSelectedIndex()) {
			JOptionPane.showMessageDialog(null, "강의 시작 시간이 끝나는 시간보다 늦습니다", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (start_comboBox.getSelectedIndex() == end_comboBox.getSelectedIndex()) {
			JOptionPane.showMessageDialog(null, "강의 시작 시간과 끝나는 시간이 같습니다", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		} else 
			input_data[3] = start_comboBox.getSelectedItem() + " ~ " + end_comboBox.getSelectedItem();
		input_data[4] = text_Profname.getText();
		input_data[5] = "변경";
		input_data[6] = "삭제";
		return true;
	}

}
