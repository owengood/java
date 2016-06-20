import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class LaneDetection implements Runnable
{
	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}
	
	private JFrame frame;
	private JLabel imageLabel; 
	private String path = System.getProperty("user.dir");
	private VideoCapture videoplayer = new VideoCapture(path+"/src/ternal.mp4");
	private ImageProcessor imageProcessor = new ImageProcessor();
	private Point Roi1_left_point1 = new Point(400,280);
	private Point Roi1_left_point2 = new Point(725,350);	
	private Point Roi2_left_point1 = new Point(200,350);
	private Point Roi2_left_point2 = new Point(725,450);
	private Point Roi3_left_point1 = new Point(100,450);
	private Point Roi3_left_point2 = new Point(725,650);
	private Point Roi1_right_point1 = new Point(575,280);
	private Point Roi1_right_point2 = new Point(900,350);
	private Point Roi2_right_point1 = new Point(575,350);
	private Point Roi2_right_point2 = new Point(1100,450);
	private Point Roi3_right_point1 = new Point(575,450);
	private Point Roi3_right_point2 = new Point(1200,650);
	private Point Roi4_point1 = new Point(100,280);
	private Point Roi4_point2 = new Point(1200,650);
	private Rect r1_L = new Rect(Roi1_left_point1, Roi1_left_point2);
	private Rect r2_L = new Rect(Roi2_left_point1, Roi2_left_point2);
	private Rect r3_L = new Rect(Roi3_left_point1, Roi3_left_point2);
	private Rect r1_R = new Rect(Roi1_right_point1, Roi1_right_point2);
	private Rect r2_R = new Rect(Roi2_right_point1, Roi2_right_point2);
	private Rect r3_R = new Rect(Roi3_right_point1, Roi3_right_point2);
	private Rect r4 = new Rect(Roi4_point1, Roi4_point2);
	private Mat originalimage = new Mat();
	private Mat VideoMatImage = new Mat(); 
	private Scalar yellow = new Scalar(50,255,255);
	private Scalar green = new Scalar(100,255,0);
	private Scalar blue = new Scalar(255,50,0);
	private Scalar red = new Scalar(0,0,255);
	private Scalar white = new Scalar(255,255,255);
	private double[] left_equation = null;
	private double[] right_equation = null;
	private JLabel console_content_direction = new JLabel();
	private JLabel console_content_length1 = new JLabel();
	private JLabel console_content_length2 = new JLabel();
	private JLabel console_content_slope1 = new JLabel();
	private JLabel console_content_slope2 = new JLabel();
	private double leftslope = 0;
	private double rightslope = 0;
	protected int lowThreshold = 100;
	protected int highThreshold = 240;
	void initGUI_Video() {
		frame = new JFrame("LaneDetection");
		frame.setLayout(new BorderLayout());
		JPanel console = new JPanel();
		JPanel subconsole = new JPanel();
		console.setPreferredSize(new Dimension(1000, 50));
		console.setBackground(Color.YELLOW);
		subconsole.setPreferredSize(new Dimension(1000, 150));
		subconsole.setLayout(new GridLayout(4,1));
		subconsole.setBackground(Color.LIGHT_GRAY);
		frame.setSize(1000,500);
		imageLabel = new JLabel();
		JMenuBar menu1 = new JMenuBar();
		JMenu Exit_menu = new JMenu("Exit");
		JMenuItem Exit_item = new JMenuItem("Exit->");
		Exit_menu.add(Exit_item);
		menu1.add(Exit_menu);
		frame.setJMenuBar(menu1);
		console_content_direction.setFont(new Font("Null", Font.BOLD, 20));
		console_content_length1.setFont(new Font("Null", Font.BOLD, 15));
		console_content_length2.setFont(new Font("Null", Font.BOLD, 15));
		console_content_slope1.setFont(new Font("Null", Font.BOLD, 15));
		console_content_slope2.setFont(new Font("Null", Font.BOLD, 15));
		console.add(console_content_direction);
		subconsole.add(console_content_length1);
		subconsole.add(console_content_length2);
		subconsole.add(console_content_slope1);
		subconsole.add(console_content_slope2);
		frame.add(console, BorderLayout.NORTH);
		frame.add(imageLabel, BorderLayout.CENTER);
		frame.add(subconsole, BorderLayout.SOUTH);
		frame.setVisible(true); 
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				videoplayer.release();
				frame.dispose();
			}
		};
		Exit_item.addActionListener(exitListener);
	}

	public void run() {
		if( videoplayer.isOpened()){  
			while(true){
				if( videoplayer.read(originalimage)){ 
					videoplayer.read(originalimage);
					VideoMatImage = originalimage.clone();
					findLane(originalimage, VideoMatImage, r1_L, Roi1_left_point1, Roi1_left_point2, yellow);
					findLane(originalimage, VideoMatImage, r2_L, Roi2_left_point1, Roi2_left_point2, green);
					findLane(originalimage, VideoMatImage, r3_L, Roi3_left_point1, Roi3_left_point2, blue);
					findLane(originalimage, VideoMatImage, r1_R, Roi1_right_point1, Roi1_right_point2, yellow);
					findLane(originalimage, VideoMatImage, r2_R, Roi2_right_point1, Roi2_right_point2, green);
					findLane(originalimage, VideoMatImage, r3_R, Roi3_right_point1, Roi3_right_point2, blue);
					findLane(originalimage, VideoMatImage, r4, Roi4_point1, Roi4_point2, white);
					//관심영역 화면에 출력하는  code
//					Imgproc.rectangle(VideoMatImage, Roi1_left_point1, Roi1_left_point2, yellow,2);			
//					Imgproc.rectangle(VideoMatImage, Roi2_left_point1, Roi2_left_point2, green,2);
//					Imgproc.rectangle(VideoMatImage, Roi3_left_point1, Roi3_left_point2, blue,2);
//					Imgproc.rectangle(VideoMatImage, Roi1_right_point1, Roi1_right_point2, yellow,2);
//					Imgproc.rectangle(VideoMatImage, Roi2_right_point1, Roi2_right_point2, green,2);
//					Imgproc.rectangle(VideoMatImage, Roi3_right_point1, Roi3_right_point2, blue,2);
//					Imgproc.rectangle(VideoMatImage, Roi4_point1, Roi4_point2, (white),4);
					updateView(VideoMatImage);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					
					}
				}else{
					System.out.println("The End");
					break;				
				}
			}		
		}
		else{
			System.out.println("Couldn't open video file.");
		}		
	}
	
	private double pow(double a){
		return a*a;
	}
	
	private void updateView(Mat newMat) {
		Image outputImage = imageProcessor.toBufferedImage(newMat);
		imageLabel.setIcon(new ImageIcon(outputImage));
		frame.pack();
	}
	
	private void findLane(Mat originalimage, Mat VideoMatImage, Rect r, Point Roi_point1, Point Roi_point2, Scalar color){
		int bold = 0;
		Mat lines = new Mat();
		Mat canny = new Mat();
		Mat grayscale = new Mat();	
		Mat Roi = new Mat(originalimage, r);
		int count = 0;
		//Imgproc.cvtColor(originalimage, grayscale, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(Roi, grayscale, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayscale, canny, 0, 95);
		//updateView(morphology);
		//updateView(canny);
		if(color.equals(yellow)){
			bold = 3;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 30, 0, 50);
		}else if(color.equals(green)){
			bold = 6;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 50, 0, 50);
		}else if(color.equals(blue)){
			bold = 9;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 100, 0, 50);
		}else{
			bold = 2;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 70, 0, 50);
		}

		for( int i = 0; i < lines.rows(); i++ )
		{
		
			double a = lines.get(i, 0)[0];
			double b = lines.get(i, 0)[1];
			double c = lines.get(i, 0)[2];
			double d = lines.get(i, 0)[3];
			double slope = (b-d)/(a-c);
			double length = Math.sqrt(pow(a-c) + pow(b-d));
			
			if( count >= 2 ){
				break;
			}
			
			if((slope > 0.5 && slope < 3) || (slope < -0.5 && slope > -3)){
				count++;
				if(color.equals(white)){
					if(slope < 0){
						left_equation = new double[] {-slope, 1 ,-a*slope+b};
						leftslope = slope;
						String temp = "(WHITE)Right line 기울기 : " + slope;
						console_content_slope1.setText(temp);
						temp = "(WHITE)Right line Length : " + length + "(px)";
						console_content_length1.setText(temp);
					}
					else{
						right_equation = new double[] {-slope, 1 ,-a*slope+b};
						rightslope = slope;
						String temp = "(WHITE)Left line 기울기 : " + slope;
						console_content_slope2.setText(temp);
						temp = "(WHITE)Left line Length : " + length + "(px)";
						console_content_length2.setText(temp);
					}

					Imgproc.line( VideoMatImage, new Point(a+Roi_point1.x, b+Roi_point1.y), new Point(c+Roi_point1.x, d+Roi_point1.y), color, bold, Core.LINE_AA,0);
				}
				
				if( Math.abs(rightslope) >  Math.abs(leftslope))
					console_content_direction.setText("오른쪽 차선으로으로 치우침 -->");
				else
					console_content_direction.setText("<-- 왼쪽 차선으로 치추침");
				Imgproc.line( VideoMatImage, new Point(a+Roi_point1.x, b+Roi_point1.y), new Point(c+Roi_point1.x, d+Roi_point1.y), color, bold, Core.LINE_AA,0);
				if(!(left_equation == null) && !(right_equation == null)){
					double x = 0;
					double y = 0;
					double def = 1 / (left_equation[0] * right_equation[1] - left_equation[1] * right_equation[0]);
					x = def*(right_equation[1]*left_equation[2] - left_equation[1] * right_equation[2])+100;
					y = def*(-right_equation[0]*left_equation[2] + left_equation[0] * right_equation[2])+275;
					Imgproc.line( VideoMatImage, new Point(x, y), new Point(x, y), red, bold, Core.LINE_AA,0);	
					System.out.println("a = " + a + "b = " + b + "c = " + c + "d = " + d + "x = " + x + ", y = " + y);
				}
			}
		}
	}
}
