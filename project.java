import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
	private Point Roi1_point1 = new Point(400,280);
	private Point Roi1_point2 = new Point(850,350);
	private Point Roi2_point1 = new Point(250,350);
	private Point Roi2_point2 = new Point(950,450);
	private Point Roi3_point1 = new Point(150,450);
	private Point Roi3_point2 = new Point(1190,650);
	private Point Roi4_point1 = new Point(150,280);
	private Point Roi4_point2 = new Point(1190,650);
	private Rect r1 = new Rect(Roi1_point1, Roi1_point2);
	private Rect r2 = new Rect(Roi2_point1, Roi2_point2);
	private Rect r3 = new Rect(Roi3_point1, Roi3_point2);
	private Rect r4 = new Rect(Roi4_point1, Roi4_point2);
	private Mat originalimage = new Mat();
	private Mat VideoMatImage = new Mat(); 
	private Scalar yellow = new Scalar(50,255,255);
	private Scalar green = new Scalar(100,255,0);
	private Scalar blue = new Scalar(255,50,0);
	private Scalar red = new Scalar(0,0,255);
	void initGUI_Video() {
		frame = new JFrame("LaneDetection");  
		frame.setSize(1000,500);
		imageLabel = new JLabel();
		JMenuBar menu1 = new JMenuBar();
		JMenu Exit_menu = new JMenu("Exit");
		JMenuItem Exit_item = new JMenuItem("Exit->");
		Exit_menu.add(Exit_item);
		menu1.add(Exit_menu);
		frame.setJMenuBar(menu1);
		frame.add(imageLabel);
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
					findLane(originalimage, VideoMatImage, r1, Roi1_point1, Roi1_point2, yellow);
					findLane(originalimage, VideoMatImage, r2, Roi2_point1, Roi2_point2, green);
					findLane(originalimage, VideoMatImage, r3, Roi3_point1, Roi3_point2, blue);
					findLane(originalimage, VideoMatImage, r4, Roi4_point1, Roi4_point2, red);
					updateView(VideoMatImage);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
		Mat equal_histo = new Mat();
		Mat Roi = new Mat(originalimage, r);
		//Imgproc.cvtColor(originalimage, grayscale, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(Roi, grayscale, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(grayscale, equal_histo);
		Imgproc.Canny(grayscale, canny, 0, 95);
		//updateView(canny);
		if(color.equals(yellow)){
			bold = 5;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 50, 0, 50);
		}else if(color.equals(green)){
			bold = 7;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 60, 0, 50);
		}else if(color.equals(blue)){
			bold = 10;
			Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 70, 0, 50);
		}else{
			bold = 1;
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
			
			
			if((slope > 0.55 && slope < 3) || (slope < -0.55 && slope > -3)){
				Imgproc.line( VideoMatImage, new Point(a+Roi_point1.x, b+Roi_point1.y), new Point(c+Roi_point1.x, d+Roi_point1.y), color, bold, Core.LINE_AA,0);
			}
		}		
	}
}
