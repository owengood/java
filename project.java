import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Delayed;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class LaneDetection implements Runnable
{
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}
	
	private JFrame frame;
	private JLabel imageLabel;
	private String path = System.getProperty("user.dir");
	private VideoCapture videoplayer = new VideoCapture(path+"/src/ternal4.mp4");
	private ImageProcessor imageProcessor = new ImageProcessor();
	private Point Roi1_point1 = new Point(400,280);
	private Point Roi1_point2 = new Point(850,350);
	private Point Roi2_point1 = new Point(250,350);
	private Point Roi2_point2 = new Point(950,450);
	private Point Roi3_point1 = new Point(150,450);
	private Point Roi3_point2 = new Point(1200,650);
	private Rect r1 = new Rect(Roi1_point1, Roi1_point2);
	private Rect r2 = new Rect(Roi2_point1, Roi2_point2);
	private Rect r3 = new Rect(Roi3_point1, Roi3_point2);
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
		Mat originalimage = new Mat();
		Mat VideoMatImage = new Mat(); 
		Mat Roi1 = new Mat();
		Mat Roi2 = new Mat();
		Mat Roi3 = new Mat();
		int count = 0;
		if( videoplayer.isOpened()){  
			while(true){
				Mat lines = new Mat();
				Mat canny = new Mat();
				Mat grayscale = new Mat();
				videoplayer.read(originalimage);
				VideoMatImage = originalimage.clone();
				
				Roi1 = new Mat(originalimage, r1);
				Roi2 = new Mat(originalimage, r2);
				Roi3 = new Mat(originalimage, r3);
				
				Imgproc.cvtColor(Roi1, grayscale, Imgproc.COLOR_BGR2GRAY);
				Imgproc.Canny(grayscale, canny, 0, 110);
				Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 50, 0, 20);
				VideoMatImage = originalimage.clone();
				System.out.println("# of Line: "+lines.rows());
				for( int i = 0; i < lines.rows(); i++ )
				{
				
					double a = lines.get(i, 0)[0];
					double b = lines.get(i, 0)[1];
					double c = lines.get(i, 0)[2];
					double d = lines.get(i, 0)[3];
					double slope = (b-d)/(a-c);
					double length = Math.sqrt(pow(a-c) + pow(b-d));
					
					if((slope > 0.5 && slope < 3) || (slope < -0.5 && slope > -3)){
						Imgproc.line( VideoMatImage, new Point(a+Roi1_point1.x, b+Roi1_point1.y), new Point(c+Roi1_point1.x, d+Roi1_point1.y), new Scalar(255,0,0), 8, Core.LINE_AA,0);
					}
				}
				Imgproc.cvtColor(Roi2, grayscale, Imgproc.COLOR_BGR2GRAY);
				Imgproc.Canny(grayscale, canny, 0, 110);
				Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180,  100, 0, 10);
				for( int i = 0; i < lines.rows(); i++ )
				{	
					
					double a = lines.get(i, 0)[0];
					double b = lines.get(i, 0)[1];
					double c = lines.get(i, 0)[2];
					double d = lines.get(i, 0)[3];
					double slope = Math.abs((b-d)/(a-c));
					double length = Math.sqrt(pow(a-c) + pow(b-d));
					
					
					if(slope > 0.5 && slope < 3){
						Imgproc.line( VideoMatImage, new Point(a+Roi2_point1.x, b+Roi2_point1.y), new Point(c+Roi2_point1.x, d+Roi2_point1.y), new Scalar(0,255,0), 8, Core.LINE_AA,0);
					}
				}
				Imgproc.cvtColor(Roi3, grayscale, Imgproc.COLOR_BGR2GRAY);
				Imgproc.Canny(grayscale, canny, 0, 110);
				Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180,  100, 0, 10);

				for( int i = 0; i < lines.rows(); i++ )
				{	
					
					double a = lines.get(i, 0)[0];
					double b = lines.get(i, 0)[1];
					double c = lines.get(i, 0)[2];
					double d = lines.get(i, 0)[3];
					double slope = Math.abs((b-d)/(a-c));
					double length = Math.sqrt(pow(a-c) + pow(b-d));
					
					
					if(slope > 0.5 && slope < 3){
						Imgproc.line( VideoMatImage, new Point(a+Roi3_point1.x, b+Roi3_point1.y), new Point(c+Roi3_point1.x, d+Roi3_point1.y), new Scalar(0,0,255), 8, Core.LINE_AA,0);
					}
				}
				updateView(VideoMatImage);
				//updateView(canny);
				//updateView(Roi1);
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
}
