package dicegameview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import diceService.DiceGame;
import dicegame.Entry;
import dicegame.Scores;
import dicegame.WinningStatus;

public class DiceView extends JFrame{
	private TextField dice1, dice2;
	private Label cell1, cell2;
	private DiceGame dicegame;
	private Panel p1, p2;
	
	public DiceView(final String playerName, final DiceGame dicegame){
		super("Dice View");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.dicegame = dicegame;
		dice1 = new TextField(5);
		dice2 = new TextField(5);
		cell1 = new Label();
		cell2 = new Label();
		
		p1 = new Panel();
		p2 = new Panel();
		
		final Button rollBut = new Button("Roll");
		p1.add(rollBut);
		Button cancelBut = new Button("Exit");
		setLayout(new FlowLayout());
		p1.add(new Label(playerName+"face value"));
		p1.add(dice1);
		p1.add(new Label("AlphaDice face value"));
		p1.add(dice2);
		p1.add(rollBut);
		p1.add(cancelBut);
		
		p2.setLayout(new GridLayout(2,2));
		p2.add(new Label(playerName));
		p2.add(cell1);
		p2.add(new Label("AlphaDice"));
		p2.add(cell2);
		
		setLayout(new BorderLayout());
		add(p1, BorderLayout.EAST);
		add(p2, BorderLayout.WEST);
		
		rollBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinningStatus ws = dicegame.roll();
				dice1.setText(String.valueOf(dicegame.getFaceValue1()));
				dice2.setText(String.valueOf(dicegame.getFaceValue2()));
				if(ws == WinningStatus.NotYet){
					cell1.setText(String.valueOf(dicegame.getCurCellPos1()));
					cell2.setText(String.valueOf(dicegame.getCurCellPos2()));
				}else{
					String message;
					Scores scores = dicegame.load();
					Entry entry;
					if(scores == null){
						scores = new Scores();
						entry = new Entry(playerName, 0, 0, 0);
						scores.addScore(entry);
					}
					entry = scores.getEntry(playerName);
					if(ws == WinningStatus.Draw){
						entry.setDraw(entry.getDraw()+1);
						message = "Draw";
					}else if(ws == WinningStatus.Player){
						entry.setWin(entry.getWin()+1);
						message = playerName + "wins";
					}else{
						entry.setLose(entry.getLose()+1);
						message = "AlphaDice wins";
					}
					dicegame.save(scores);
					new ResultView(message, entry);
					setVisible(false);
					dispose();
				}
			}
		});
		cancelBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		setSize(600, 100);
		setVisible(true);
		
	}
}