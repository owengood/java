package dicegameview;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import diceService.DiceGame;
import dicegame.Scores;

public class ScoreView extends JFrame{
	ScrollPane scroll;
	DiceGame dicegame;
	Panel p1, p2;
	Button exitbut;
	public ScoreView(){
		super("Score View");
		dicegame = new DiceGame();
		p1 = new Panel();
		p2 = new Panel();
		scroll = new ScrollPane();
		exitbut = new Button("Exit");
		Scores scores = dicegame.load();
		final List list = scores.getList();
		scroll.add(list);
		p1.add(scroll);
		p2.add(exitbut);
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		scroll.setSize(250, 100);
		setSize(300, 200);
		setVisible(true);
		
		exitbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
	}
}
