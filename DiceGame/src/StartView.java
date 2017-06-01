import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StartView extends JFrame{
	private TextField nametf = null;
	private String name = null;
	private DiceGame dicegame = null;
	private ConfigureView configureview = null;
	private StartView startview;
	Panel p1;
	Panel p2;
	
	public StartView(String title){
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		startview = this;
		Button configBut = new Button("Configure");
		Button playBut = new Button("Play");
		Button exitBut = new Button("Exit");
		p1 = new Panel();
		p2 = new Panel();
		nametf = new TextField(20);
		setLayout(new FlowLayout());
		p1.add(nametf);
		p1.add(playBut);
		this.add(p1);
		p2.add(configBut);
		p2.add(exitBut);
		this.add(p2);
		
		playBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(dicegame == null) dicegame = new DiceGame();
				name = nametf.getText();
				new DiceView(name, dicegame);
				setVisible(false);
				dispose();
			}
		});
		
		configBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(dicegame == null) dicegame = new DiceGame();
				configureview = new ConfigureView(startview, dicegame);
				setVisible(false);
			}
		});
		
		exitBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		setSize(300, 150);
		setVisible(true);
	}
}
