package se.smu.todolist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import se.smu.db.DBConnection;
import se.smu.memo.Memo;

import javax.swing.JScrollBar;
import java.awt.Scrollbar;

public class TodoPanel extends JPanel implements ActionListener {
	private Vector<Object> todoData = new Vector<Object>();
	private String id;
	
	private JButton btnEnroll;
	private JTable tblTodo;
	private Vector<Object> columns;
	private DefaultTableModel todoModel;
	public Vector<TodoInfo> rowdata = new Vector<TodoInfo>();
	private JButton hidebutton;
	

	public TodoPanel(String _id) {
		setLayout(null);
		setSize(1255,400);
		JLabel lblTitle = new JLabel("To Do List");
		lblTitle.setBounds(514, 17, 121, 18);
		add(lblTitle);
		id = "admin";
		DBConnection db = new DBConnection();
		Vector<Object> obj = db.getLogIn("admin", "1111");
		System.out.println(obj.get(0));
		db.close();

		columns = initColumn();
		todoModel = new DefaultTableModel(columns, 0); 
		
		refreshTable(id);
		
		tblTodo = new JTable(todoModel);
		tblTodo.getColumnModel().getColumn(0).setCellEditor(new TodoTableCell(id, "중요도", tblTodo));
		tblTodo.getColumnModel().getColumn(0).setCellRenderer(new TodoTableCell(id, "중요도", tblTodo));
		tblTodo.getColumnModel().getColumn(6).setCellEditor(new TodoTableCell(id, "변경", tblTodo));
		tblTodo.getColumnModel().getColumn(6).setCellRenderer(new TodoTableCell(id, "변경", tblTodo));
		tblTodo.getColumnModel().getColumn(7).setCellEditor(new TodoTableCell(id, "삭제", tblTodo));
		tblTodo.getColumnModel().getColumn(7).setCellRenderer(new TodoTableCell(id, "삭제", tblTodo));
		tblTodo.getColumnModel().getColumn(8).setCellEditor(new TodoTableCell(id, "메모", tblTodo));
		tblTodo.getColumnModel().getColumn(8).setCellRenderer(new TodoTableCell(id, "메모", tblTodo));
		JScrollPane sp = new JScrollPane(tblTodo);
		sp.setBounds(502, 47, 750, 350);
		add(sp);

		btnEnroll = new JButton("등록");
		btnEnroll.setBounds(1170, 8, 71, 33);
		btnEnroll.addActionListener(this);
		add(btnEnroll);
		
		hidebutton = new JButton("숨기기");
		hidebutton.setBounds(1081, 8, 75, 33);
		hidebutton.addActionListener(this);
		add(hidebutton);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnEnroll) {
			Frame fs = new TodoEnroll(id, todoModel, todoData);
			fs.setVisible(true);
		}
		if(e.getActionCommand() == "숨기기") {
			hidebutton.setText("보이기");
			int k = 0;
			while(true) {
				int row = todoModel.getRowCount();
				if(k >= row)
					break;
				if(todoModel.getValueAt(k, 4) == "해결") {
					TodoInfo info = new TodoInfo(todoModel.getValueAt(k, 0), todoModel.getValueAt(k, 1), todoModel.getValueAt(k, 2),
							todoModel.getValueAt(k, 3), todoModel.getValueAt(k, 4), todoModel.getValueAt(k, 5));
					rowdata.add(info);
					todoModel.removeRow(k);
					k = 0;
				}
				else
					k++;
			}
		}	
		if(e.getActionCommand() == "보이기") {
			hidebutton.setText("숨기기");
			if(rowdata.isEmpty())
				return;
			for(int i = 0; i < rowdata.size(); i++) {
				Object input[] = new Object[9];
				input[0] = "중요도";
				input[1] = rowdata.get(i).getTfSubject();
				input[2] = rowdata.get(i).getCbDeadMonth();
				input[3] = rowdata.get(i).getCbRDeadMonth();
				input[4] = rowdata.get(i).getCbState();
				input[5] = rowdata.get(i).getTfWTD();
				input[6] = "변경";
				input[7] = "삭제";
				input[8] = "메모";
				todoModel.addRow(input);
			}
			rowdata.removeAllElements();
		}
	}
	
	private Vector<Object> initColumn() {
		Vector<Object> cols = new Vector<Object>();
		cols.add("중요도");
		cols.add("과목명");
		cols.add("마감일");
		cols.add("실제마감일");
		cols.add("상태");
		cols.add("WHAT TO DO");
		cols.add("변경");
		cols.add("삭제");
		cols.add("메모");
		return cols;
		//{ "중요도", "과목명", "마감일", "실제 마감일", "상태", "WHAT TO DO", "변경", "삭제", "메모" };
	}

	private void refreshTable(String _id) {
		DBConnection db = new DBConnection();
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		data = db.getTodo(_id);
		todoModel.setDataVector(data, columns);
		db.close();
	}
	
}

class TodoTableCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
	private JButton btn;
	private String id;

	public TodoTableCell(String _id, final String label, final JTable table) {
		id = _id;
		btn = new JButton(label);
		btn.setToolTipText(label);
		if("중요도".equals(label)) {
			btn.setText("");
		}
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String type = btn.getToolTipText();
				int row = table.getSelectedRow();
				
				if ("변경".equals(type)) {
					Vector<Object> rowData = new Vector<Object>();
					rowData = getRows(table, row);
					Frame fr = new TodoModify(id, (DefaultTableModel) table.getModel(), rowData, row);
				}else if("삭제".equals(type)) {
					DefaultTableModel tm = (DefaultTableModel) table.getModel();
					tm.removeRow(row);
				}else if("메모".equals(type)) {
					Vector<Object> rowData = new Vector<Object>();
					rowData = getRows(table, row);
					Frame fr = new Memo(id, rowData, table, row);
				}				
			}
		});
	}

	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		String label = btn.getToolTipText();
		if("중요도".equals(label)) {
			Color[] colors = {Color.lightGray, Color.YELLOW, Color.RED};
			int color = (Integer)table.getValueAt(row, 0);
			btn.setBackground(colors[color]);
		}
		return btn;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		return btn;
	}

	private Vector<Object> getRows(JTable tbl, int row) {
		Vector<Object> rows = new Vector<Object>();
		for (int i = 0; i < tbl.getColumnCount(); i++) {
			Object obj = tbl.getValueAt(row, i);
			rows.add(obj);
		}
		return rows;
	}
	
}