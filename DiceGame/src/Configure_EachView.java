import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class Configure_EachView extends JFrame{
	private JPanel InputValue_Panel = null;
	private JPanel ResultValue_Panel = null;
	private JLabel InputValue_Label = null;
	private JLabel ResultValue_Label = null;
	private JTextField InputValue_TextField = null;
	private JTextField ResultValue_TextField = null;
	private JButton ChangeBtn = null;
	private int[] cell = null;
	private String value;
	public Configure_EachView(String value, int[] cell) {
		super("Configure_Setting");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300,100);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		this.value = value;
		this.cell = cell;
		InputValue_Panel = new JPanel();
		InputValue_Panel.setLayout(new FlowLayout());
		InputValue_Label = new JLabel("InputValue : ");
		InputValue_TextField = new JTextField(4);
		InputValue_TextField.setText(value);
		InputValue_TextField.setEditable(false);
		InputValue_Panel.add(InputValue_Label);
		InputValue_Panel.add(InputValue_TextField);
		
		ResultValue_Panel = new JPanel();
		ResultValue_Panel.setLayout(new FlowLayout());
		ResultValue_Label = new JLabel("ResultValue : ");
		ResultValue_TextField = new JTextField(4);
		ResultValue_TextField.setText(Integer.toString(cell[Integer.parseInt(value)]));
		ResultValue_Panel.add(ResultValue_Label);
		ResultValue_Panel.add(ResultValue_TextField);
		
		ChangeBtn = new JButton("Change");
		ChangeBtn.addActionListener(new ChangeBtn_ActionListener());
		c.add(ChangeBtn, BorderLayout.SOUTH);
		c.add(InputValue_Panel, BorderLayout.WEST);
		c.add(ResultValue_Panel, BorderLayout.CENTER);
		
		setVisible(true);
		
	}
	
	public int getValue(){
		return Integer.parseInt(this.value);
	}
	
	class ChangeBtn_ActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int index = Integer.parseInt(InputValue_TextField.getText());
			cell[index] = Integer.parseInt(ResultValue_TextField.getText());
			setVisible(false);
		}
	}
}
