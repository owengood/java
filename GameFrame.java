package experience1;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame{
	private GamePanel gamePanel = new GamePanel();
	
	public GameFrame(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("PingPong");
	    setVisible(true);
		setSize(800, 400);
		setContentPane(gamePanel);
		gamePanel.GameStart();
		gamePanel.setVisible(true);
	}
	
	class GamePanel extends JPanel{
		private PingPongBar pingpongbar;
		private PingPongBall pingpongball;
		
		public GamePanel(){
			setBackground(Color.BLACK);
			setLayout(null);
		}
		
		public void GameStart(){
			pingpongbar = new PingPongBar();
			pingpongball = new PingPongBall();
			pingpongbar.setLocation(gamePanel.getWidth() / 2 - pingpongbar.getWidth() / 2, gamePanel.getHeight() - pingpongbar.getHeight());
			pingpongball.setLocation(pingpongbar.getX() + pingpongbar.getWidth() / 2 - pingpongball.getWidth(), gamePanel.getHeight() - pingpongbar.getHeight() - pingpongball.getHeight());
			add(pingpongbar);
			add(pingpongball);
			pingpongbar.Create();
		}
		
		class PingPongBar extends JPanel{
			
			public PingPongBar(){
				setSize(150,15);
				setOpaque(true);
				setBackground(Color.white);
			}
			
			private void Create(){
				thread th = new thread(pingpongbar);	
				th.start();
			}
			
			class thread extends Thread{
				private int x, y;
				private PingPongBar bar;
				boolean R_L;
				public thread(PingPongBar bar){
					R_L = true;
					this.bar = bar;
					this.x = bar.getX();
					this.y = bar.getY();
				}
				public void run(){
					try {
						while(true){
							if(this.x + bar.getWidth() >= gamePanel.getWidth())
								R_L = false;
							else if(this.x <= gamePanel.getX())
								R_L = true;
							if(R_L){
								x = getX() + 5;
							}
							else{
								x = getX() - 5;
							}
							
							pingpongbar.setLocation(x, y);
							sleep(100);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		class PingPongBall extends JPanel{
			
			public PingPongBall(){
				setSize(10,10);
				setOpaque(true);
				setBackground(Color.RED);
			}
			
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameFrame gameframe = new GameFrame();
	}
	
}

