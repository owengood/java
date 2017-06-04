package dicegameview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dicegame.Entry;

public class ResultView extends JFrame{
	private Label message;
	public ResultView(final String msg, final Entry entry){
		super("Result View");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		message = new Label(msg);
		Button scorebut = new Button("Score");
		Button cancelbut = new Button("Exit");
		setLayout(new FlowLayout());
		add(message);
		add(scorebut);
		add(cancelbut);
		message.setText(msg);
		scorebut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ScoreView();
			}
		});
		
		cancelbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		setSize(500, 100);
		setVisible(true);
	}
}
