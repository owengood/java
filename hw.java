package hw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class GUI {


	private static final String blurString = "Average Filter";
	private static final String gaussianString = "Gaussian Filter";
	private static final String medianString = "Median Filter";
	private static final String bilateralString = "Bilateral Filter";
	private static final String erodeString = "Erode";
	private static final String dilateString = "Dilate";
	private static final String openString = "Open";
	private static final String closeString = "Close";
	private static final String noneString = "None";
	private static final String rectangleString = "Rectangle";
	private static final String resetString = "Reset";
	private static final String ellipseString = "Ellipse";
	private static final String crossString = "Cross";
	private JLabel imageView1 = new JLabel();
	private JLabel imageView2 = new JLabel();
	private String windowName;
	private JLabel imageView;
	private Mat originalImage;
	private Mat image;
	private Mat output;
	private int kernelSize = 0;
	private final ImageProcessor imageProcessor = new ImageProcessor();
	protected String filterMode = noneString;
	private JPanel panel1 = new JPanel();
	private JPanel title = new JPanel();
	private JPanel controlbar = new JPanel();
	private JPanel sub_controlbar1 = new JPanel();
	private JPanel sub_controlbar2 = new JPanel();
	private String currentOperation = erodeString;
	private int currentShape = Imgproc.CV_SHAPE_RECT;
	private JFrame frame = new JFrame(windowName);
	private FileDialog fd;

	public GUI(String windowName, Mat newImage) {
		super();
		this.windowName = windowName;
		this.image = newImage;
		originalImage = newImage.clone();
		processOperation_smoothing();
		updateView();
	}

	public void init() {
		setSystemLookAndFeel();
		initGUI();
	}

	private void initGUI() {
		JFrame frame = createJFrame(windowName);
		updateView();
		frame.setJMenuBar(creatMenu());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	private JFrame createJFrame(String windowName) {				//smoothing

		JFrame frame = new JFrame(windowName);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.setLayout(new GridLayout(3, 1));
		controlbar.setLayout(new GridLayout(1, 2));
		controlbar.setPreferredSize(new Dimension(1000,80));
	

		panel1.add(title);
		title.setBackground(Color.lightGray);

		panel1.add(controlbar);
		panel2.setLayout(new GridLayout(1, 2));
		
		contentPane.add(panel1, BorderLayout.NORTH);
		contentPane.add(panel2, BorderLayout.SOUTH);
		
		JPanel panel2_1 = new JPanel();
		JPanel panel2_2 = new JPanel();
		panel2_1.setLayout(new BorderLayout());
		panel2_2.setLayout(new BorderLayout());
		panel2.add(panel2_1);
		panel2.add(panel2_2);
		
		JPanel panel2_1_1 = new JPanel();
		JLabel la1 = new JLabel("Original Image");
		panel2_1_1.add(la1);
		JPanel panel2_1_2 = new JPanel();		
		JPanel panel2_2_1 = new JPanel();
		JLabel la2 = new JLabel("Transformed Image");
		panel2_2_1.add(la2);
		JPanel panel2_2_2 = new JPanel();

	    
		panel2_1.add(panel2_1_1, BorderLayout.NORTH);
		panel2_1.add(panel2_1_2, BorderLayout.CENTER);
		panel2_1.add(new JPanel(), BorderLayout.SOUTH);
		panel2_2.add(panel2_2_1, BorderLayout.NORTH);
		panel2_2.add(panel2_2_2, BorderLayout.CENTER);
		panel2_2.add(new JPanel(), BorderLayout.SOUTH);
		setupImage(panel2_1_2, panel2_2_2);
		imageView1.setIcon(new ImageIcon(imageProcessor.toBufferedImage(output)));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return frame;
	}
	
	
	
	
	private JMenuBar creatMenu(){
		JMenuBar mb = new JMenuBar();
		
			JMenu file = new JMenu("File");
			JMenuItem reset =  new JMenuItem("Reset");
			JMenuItem open = new JMenuItem("Open");
			JMenuItem save = new JMenuItem("Save");
			JMenuItem save_as = new JMenuItem("Save as");
			JMenuItem exit = new JMenuItem("Exit");
			file.add(reset);
			file.add(open);
			file.add(save);
			file.add(save_as);
			file.add(exit);
			
			reset.setActionCommand(resetString);
			open.setActionCommand(openString);
			
			
			JMenu imgproc = new JMenu("Image Processing");
			JMenu smoothing = new JMenu("Smoothing");
			
			imgproc.add(smoothing);
			
				
				JMenuItem Average_Filter = new JMenuItem("Average Filter");
				JMenuItem Gaussian_Filter = new JMenuItem("Gaussian Filter");
				JMenuItem Median_Filter = new JMenuItem("Median Filter");
				JMenuItem Bilateral_Filter = new JMenuItem("Bilateral Filter");
				smoothing.add(Average_Filter);
				smoothing.add(Gaussian_Filter);
				smoothing.add(Median_Filter);
				smoothing.add(Bilateral_Filter);
				
				

				Average_Filter.setActionCommand(blurString);

				Gaussian_Filter.setActionCommand(gaussianString);

				Median_Filter.setActionCommand(medianString);

				Bilateral_Filter.setActionCommand(bilateralString);
				
			JMenu morphological_operation = new JMenu("Morphological Operation");
			imgproc.add(morphological_operation);
			
			// Erode, Dilate, Open, Close
			
			JMenuItem erode = new JMenuItem("Erode");
			JMenuItem dilate = new JMenuItem("Dilate");
			JMenuItem Open = new JMenuItem("Open");
			JMenuItem close = new JMenuItem("Close");
			morphological_operation.add(erode);
			morphological_operation.add(dilate);
			morphological_operation.add(Open);
			morphological_operation.add(close);

			
		JMenu computer_vision = new JMenu("Computer Vision");
		
		
		JMenu option = new JMenu("Option");
		
		
		mb.add(file);
		mb.add(imgproc);
		mb.add(computer_vision);
		mb.add(option);
			
		ActionListener fileListener = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				title.removeAll();
				controlbar.removeAll();
				filterMode = noneString;
				openfile();
				updateView();
			}
		};
		
		ActionListener operationChangeListener_smoothing = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				title.removeAll();
				controlbar.removeAll();
				filterMode = event.getActionCommand();
				processOperation_smoothing();
				updateView();
				
			}
		};
		
		ActionListener operationChangeListener_morphological = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				title.removeAll();
				controlbar.removeAll();
				currentOperation = event.getActionCommand();
				setupSizeSlider(controlbar);
				setupShapeRadioButtons(controlbar);
				processOperation_morphological();	
				
			}
		};
		
		reset.addActionListener(operationChangeListener_smoothing);
		open.addActionListener(fileListener);
		Average_Filter.addActionListener(operationChangeListener_smoothing);
		Gaussian_Filter.addActionListener(operationChangeListener_smoothing);
		Median_Filter.addActionListener(operationChangeListener_smoothing);
		Bilateral_Filter.addActionListener(operationChangeListener_smoothing);

		erode.addActionListener(operationChangeListener_morphological);
		dilate.addActionListener(operationChangeListener_morphological);
		Open.addActionListener(operationChangeListener_morphological);
		close.addActionListener(operationChangeListener_morphological);
		
		return mb;
		
	}
	private void openfile(){
		JFrame frame = new JFrame();
		fd = new FileDialog(frame, "파일 열기", FileDialog.LOAD);
		fd.setDirectory("C:");
		fd.setVisible(true);
		image = Imgcodecs.imread(fd.getDirectory());
		originalImage = image.clone();

		updateView();
	}

	private void setupImage(JPanel panel1, JPanel panel2) {		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
		c.gridy = 3;
		c.gridx = 0;
		panel1.add(imageView1, c);
		panel2.add(imageView2, c);
	}

	private void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void updateView(){
		Image outputImage = imageProcessor.toBufferedImage(output);
		title.removeAll();
		title.add(new JLabel(filterMode));
		title.revalidate();
		controlbar.revalidate();

		imageView2.setIcon(new ImageIcon(outputImage));
	}

	private void updateView(Mat newMat) {
		Image outputImage = imageProcessor.toBufferedImage(newMat);
		title.removeAll();
		title.add(new JLabel(currentOperation));
		title.revalidate();
		controlbar.revalidate();
		imageView2.setIcon(new ImageIcon(outputImage));
	}

	
	private void processOperation_smoothing() {
		if(noneString.equals(filterMode) || resetString.equals(filterMode)){
			output = image.clone();
		}
		else {
			output = new Mat(image.rows(), image.cols(), image.type());
			Size size = new Size(3.0, 3.0);
			if(blurString.equals(filterMode)){
				Imgproc.blur(image, output, size);
			}
			else if(gaussianString.equals(filterMode)){
				Imgproc.GaussianBlur(image, output, size, 0);
			}
			else if(medianString.equals(filterMode)){
				Imgproc.medianBlur(image, output, 3);
			}
			else if(bilateralString.equals(filterMode)){
				Imgproc.bilateralFilter(image, output, 9, 100, 100);
			}			
			
		}
	}
	
	private void processOperation_morphological() {

		if (erodeString.equals(currentOperation)){

			output = imageProcessor.erode(image, kernelSize, currentShape );	
		}
		else if(dilateString.equals(currentOperation)){
			output = imageProcessor.dilate(image, kernelSize, currentShape );
		}
		else if(openString.equals(currentOperation)){
			output = imageProcessor.open(image, kernelSize, currentShape );
		}
		else if(closeString.equals(currentOperation)){
			output = imageProcessor.close(image, kernelSize, currentShape );

		}
		updateView(output);
	}



// morphorogical
	private void setupShapeRadioButtons(JPanel controlbar) {
		JRadioButton rectangleButton = new JRadioButton(rectangleString);
		rectangleButton.setActionCommand(rectangleString);
		rectangleButton.setSelected(true);

		JRadioButton ellipseButton = new JRadioButton(ellipseString);
		ellipseButton.setActionCommand(ellipseString);

		JRadioButton crossButton = new JRadioButton(crossString);
		crossButton.setActionCommand(crossString);

		ButtonGroup group = new ButtonGroup();
		group.add(rectangleButton);
		group.add(ellipseButton);
		group.add(crossButton);

		ActionListener shapeChangeListener = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				String currentShapeString = event.getActionCommand();
				if(rectangleString.equals(currentShapeString)){
					currentShape = Imgproc.CV_SHAPE_RECT;
				}
				else if(ellipseString.equals(currentShapeString)){
					currentShape = Imgproc.CV_SHAPE_ELLIPSE;
				}
				else if(crossString.equals(currentShapeString)){
					currentShape = Imgproc.CV_SHAPE_CROSS;
				}
				processOperation_morphological();	
			}
		};

		rectangleButton.addActionListener(shapeChangeListener);
		ellipseButton.addActionListener(shapeChangeListener);
		crossButton.addActionListener(shapeChangeListener);

		GridLayout gridRowLayout = new GridLayout(1,0);
		JPanel shapeRadioPanel = new JPanel(gridRowLayout);

		JLabel shapeLabel = new JLabel("Shape:", JLabel.CENTER);

		shapeRadioPanel.add(rectangleButton);
		shapeRadioPanel.add(ellipseButton);
		shapeRadioPanel.add(crossButton);

		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;

		controlbar.add(shapeLabel,c);

		c.gridx = 1;
		c.gridy = 2;

		controlbar.add(shapeRadioPanel,c);
	}

private void setupSizeSlider(JPanel controlbar) {
	JLabel sliderLabel = new JLabel("Kernel size:", JLabel.CENTER);
	sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	int minimum = 0;
	int maximum = 25;
	int initial =0;

	JSlider levelSlider = new JSlider(JSlider.HORIZONTAL,
			minimum, maximum, initial);
	levelSlider.setPreferredSize(new Dimension(400, 50));
	levelSlider.setMajorTickSpacing(2);
	levelSlider.setMinorTickSpacing(1);
	levelSlider.setPaintTicks(true);
	levelSlider.setPaintLabels(true);
	levelSlider.addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			kernelSize = (int)source.getValue();
			processOperation_morphological();			
		}
	});


	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;

	c.gridx = 0;
	c.gridy = 1;


	controlbar.add(sliderLabel,c);

	c.gridx = 1;
	c.gridy = 1;

	controlbar.add(levelSlider,c);
	frame.revalidate();	
	}
}
