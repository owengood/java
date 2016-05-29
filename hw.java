package hw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
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
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class GUI {

	private static final String laplaceString = "Laplace";
	private static final String sobelString = "Sobel";
	private static final String cannyString = "Canny";
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
	private static final String binaryInvString = "Binary inverted";
	private static final String binaryString = "Binary";
	private static final String truncateString = "Truncate";
	private static final String thresholdToZeroString = "Threshold to zero";
	private static final String invThresholdToZeroString = "Inverted threshold to zero";
	private static final String adaptiveMeanString = "Adaptive Mean Binary";
	
	private Map<String, Integer> modeMap = new HashMap<String, Integer>();
	{		
		modeMap.put(binaryString, Imgproc.THRESH_BINARY);
		modeMap.put(binaryInvString, Imgproc.THRESH_BINARY_INV);
		modeMap.put(truncateString,	 Imgproc.THRESH_TRUNC);
		modeMap.put(thresholdToZeroString, Imgproc.THRESH_TOZERO);
		modeMap.put(invThresholdToZeroString, Imgproc.THRESH_TOZERO_INV);
	}
	private JLabel imageView;
	private JLabel originalImageLabel; 
	private Mat grayImage;
	private JLabel imageView1 = new JLabel();
	private JLabel imageView2 = new JLabel();
	private String windowName;
	private Mat originalImage;
	private Mat image;
	private Mat originalImage_clone;
	private Mat output;
	private int kernelSize = 0;
	private final ImageProcessor imageProcessor = new ImageProcessor();
	
	protected String filterMode = noneString;
	private String currentOperation = noneString;
	private String thresholdMode = noneString;
	
	private int currentShape = Imgproc.CV_SHAPE_RECT;
	private JFrame frame = new JFrame(windowName);
	
	JPanel panel2_2_2 = new JPanel();
	JPanel panel2_1_2 = new JPanel();
	
	
	private JFrame toolbox_edge = new JFrame();
	private JFrame toolbox_morphological = new JFrame();
	private JFrame toolbox_threshold = new JFrame();
	private JFrame toolbox_pyramid = new JFrame();
	private JPanel title = new JPanel();
	private JPanel controlbar = new JPanel();
	private FileDialog fd;
	private int aperture = 3;
	private int xOrder = 1;
	private int yOrder = 1;
	protected int lowThreshold = 10;
	protected int highThreshold = 50;
	private JSlider apertureSlider;
	private JSlider xorderSlider;
	private JSlider yOrderSlider;
	private JSlider lowThresholdSlider;
	private JSlider highThresholdSlider;
	private JLabel highThresholdLabel;
	private JLabel apertureSliderLabel;
	private JLabel xOrderSliderLabel;
	private JLabel yOrderSliderLabel;
	private JLabel lowThresholdSliderLabel;
	
	private int level = 110;
	private double maxval = 255;
	private int blockSize = 3;
	protected int constantC = 5;
	private JSlider levelSlider;
	private JSlider maxSlider;
	private JSlider blockSlider;
	private JSlider constantSlider;
	
	public GUI(String windowName, Mat newImage) {
		super();
		this.windowName = windowName;
		this.image = newImage;
		originalImage = newImage.clone();
		this.grayImage = newImage.clone();
		this.originalImage = newImage.clone();
		this.originalImage_clone = newImage.clone();
		processOperation_smoothing();
		updateView();
	}
	public void init() {
		setSystemLookAndFeel_edge();
		initGUI();
	}
	private void initGUI() {
		JFrame frame = createJFrame(windowName);
		frame.setPreferredSize(new Dimension(1500, 1000));
		createtoolbox1();
		createtoolbox2();
		createtoolbox3();
		updateView();
		frame.setJMenuBar(creatMenu());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private JFrame createJFrame(String windowName) { // smoothing
		JFrame frame = new JFrame(windowName);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new GridLayout(2,1));
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel1.setLayout(new GridLayout(3, 1));
		controlbar.setLayout(new GridLayout(1, 2));
		controlbar.setPreferredSize(new Dimension(700, 80));
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
		
		panel2_2.setSize(500,500);
		JPanel panel2_2_1 = new JPanel();
		JLabel la2 = new JLabel("Transformed Image");
		panel2_2_1.add(la2);
		
		panel2_2_2.setSize(1000,1000);
		panel2_1.add(panel2_1_1, BorderLayout.NORTH);
		panel2_1.add(panel2_1_2, BorderLayout.CENTER);

		panel2_2.add(panel2_2_1, BorderLayout.NORTH);
		panel2_2.add(panel2_2_2, BorderLayout.CENTER);

		setupImage(panel2_1_2, panel2_2_2);
		imageView1.setIcon(new ImageIcon(imageProcessor.toBufferedImage(output)));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return frame;
	}

	private void createtoolbox1(){

		toolbox_edge.setTitle("ToolBox");
		toolbox_edge.setSize(500,350);
		toolbox_edge.setResizable(false);
		toolbox_edge.setLayout(new GridBagLayout());
		setupTypeRadio_edge(toolbox_edge);
		setupApertureSlider(toolbox_edge);
		setupXOrderSlider(toolbox_edge);
		setupYOrderSlider(toolbox_edge);
		setupLowThresholdSlider(toolbox_edge);
		setupHighThresholdSlider(toolbox_edge);
		enableDisableSliders();
		toolbox_edge.setVisible(false);

	}
	
	private void createtoolbox2(){
		toolbox_threshold.setTitle("ToolBox");
		toolbox_threshold.setSize(1400,400);
		toolbox_threshold.setResizable(false);
		toolbox_threshold.setLayout(new GridBagLayout());
		setupTypeRadio_threshold(toolbox_threshold);
		setupThresholdSlider(toolbox_threshold);
		setupMaxSlider(toolbox_threshold);
		setupBlockSlider(toolbox_threshold);
		setupCSlider(toolbox_threshold);
		
		enableDisableSliders();
		toolbox_threshold.setVisible(false);

	}
	
	private void createtoolbox3(){

		toolbox_pyramid.setTitle("ToolBox");
		toolbox_pyramid.setSize(450,150);
		toolbox_pyramid.setResizable(false);
		toolbox_pyramid.setLayout(new GridBagLayout());
		setupButton(toolbox_pyramid);
		enableDisableSliders();
		toolbox_edge.setVisible(false);

	}

	private JMenuBar creatMenu() {
		
		JMenuBar mb = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem reset = new JMenuItem("Reset");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(reset);
		file.add(open);
		file.add(save);
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
		JMenuItem histogram_equalization = new JMenuItem("Histogram Equalization");
		JMenuItem thresholding = new JMenuItem("Thresholding");
		JMenuItem edge_detection = new JMenuItem("Edge Detection");
		JMenuItem image_pyramids = new JMenuItem("Image Pyramids");
		imgproc.add(histogram_equalization);
		imgproc.add(thresholding);
		imgproc.add(edge_detection);
		imgproc.add(image_pyramids);
		JMenu computer_vision = new JMenu("Computer Vision");
		JMenu option = new JMenu("Option");
		mb.add(file);
		mb.add(imgproc);
		mb.add(computer_vision);
		mb.add(option);
		
		ActionListener openfileListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				title.removeAll();
				controlbar.removeAll();
				filterMode = noneString;
				openfile();
				updateView();
			}
		};
		ActionListener savefileListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				title.removeAll();
				controlbar.removeAll();
				filterMode = noneString;
				savefile();
				updateView();
			}
		};
		ActionListener exitListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		
		
		ActionListener operationChangeListener_smoothing = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				title.removeAll();
				controlbar.removeAll();
				filterMode = event.getActionCommand();
				processOperation_smoothing();
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
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
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
				updateView(output);
			}
		};
		
		ActionListener operationChangeListener_histo = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				
				title.removeAll();
				controlbar.removeAll();
				currentOperation = event.getActionCommand();
				System.out.println(currentOperation);
				processOperation_histogram();
				setSystemLookAndFeel();
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
			}
		};
	
		ActionListener operationChangeListener_thresholding = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				
				title.removeAll();
				controlbar.removeAll();
				toolbox_threshold.setVisible(true);
				currentOperation = event.getActionCommand();
				processOperation_threshold();
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
				
			}
		};
		
		ActionListener operationChangeListener_edge = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				
				title.removeAll();
				controlbar.removeAll();
				toolbox_edge.setVisible(true);

				currentOperation = event.getActionCommand();
				processOperation_edge();
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
				
			}
		};
		ActionListener operationChangeListener_pyramids = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				
				title.removeAll();
				controlbar.removeAll();
				toolbox_pyramid.setVisible(true);
				currentOperation = event.getActionCommand();
				frame.getContentPane().revalidate();
				frame.getContentPane().setSize(frame.getWidth(), frame.getHeight());
			}
		};
		
		reset.addActionListener(operationChangeListener_smoothing);
		open.addActionListener(openfileListener);
		save.addActionListener(savefileListener);
		exit.addActionListener(exitListener);
		Average_Filter.addActionListener(operationChangeListener_smoothing);
		Gaussian_Filter.addActionListener(operationChangeListener_smoothing);
		Median_Filter.addActionListener(operationChangeListener_smoothing);
		Bilateral_Filter.addActionListener(operationChangeListener_smoothing);
		erode.addActionListener(operationChangeListener_morphological);
		dilate.addActionListener(operationChangeListener_morphological);
		Open.addActionListener(operationChangeListener_morphological);
		close.addActionListener(operationChangeListener_morphological);
		histogram_equalization.addActionListener(operationChangeListener_histo);
		thresholding.addActionListener(operationChangeListener_thresholding);
		edge_detection.addActionListener(operationChangeListener_edge);
		image_pyramids.addActionListener(operationChangeListener_pyramids);
		return mb;
	}
	private void openfile() {
		 JFrame frame = new JFrame();
		 fd = new FileDialog(frame, "파일 열기", FileDialog.LOAD);
		 fd.setDirectory("C:\\");
		 fd.setVisible(true);
		 String filePath = fd.getDirectory();
		 String filename = fd.getFile();
		 Mat newImage = Imgcodecs.imread(filePath+filename);
		 System.out.println("Image resulation: "+newImage.rows() + "x"+newImage.cols());
		 this.image = newImage;
		 
		 originalImage = newImage.clone();
		 this.grayImage = newImage.clone();
		 this.originalImage = newImage.clone();
		 this.originalImage_clone = newImage.clone();
		
		 processOperation_smoothing();
		 setupImage(panel2_1_2, panel2_2_2);
		 imageView1.setIcon(new ImageIcon(imageProcessor.toBufferedImage(output)));
		 updateView();
		
	}
	private void savefile() {
		JFrame frame = new JFrame();
		fd = new FileDialog(frame, "파일 저장", FileDialog.SAVE);
		fd.setDirectory("C:");
		fd.setVisible(true);
	}
	private void setupImage(JPanel panel1, JPanel panel2) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		c.gridy = 3;
		c.gridx = 0;
		panel1.add(imageView1, c);
		panel2.add(imageView2, c);
	}
	private void setSystemLookAndFeel_edge() {
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
	private void updateView() {
		Image outputImage = imageProcessor.toBufferedImage(output);
		title.removeAll();
		title.add(new JLabel(filterMode));
		title.revalidate();
		controlbar.revalidate();
		output = originalImage.clone();
		imageView2.setIcon(new ImageIcon(outputImage));
	}
	private void updateView(Mat newMat) {
		Image outputImage = imageProcessor.toBufferedImage(newMat);
		title.removeAll();
		title.add(new JLabel(currentOperation));
		title.revalidate();
		controlbar.revalidate();
		output = originalImage.clone();
		title.setSize(title.getWidth(),title.getHeight());
		imageView2.setIcon(new ImageIcon(outputImage));
	}
	private void processOperation_smoothing() {
		if (noneString.equals(filterMode) || resetString.equals(filterMode)) {
			output = originalImage.clone();
		}
		else {
			output = new Mat(image.rows(), image.cols(), image.type());
			Size size = new Size(3.0, 3.0);
			if (blurString.equals(filterMode)) {
				Imgproc.blur(image, output, size);
			}
			else if (gaussianString.equals(filterMode)) {
				Imgproc.GaussianBlur(image, output, size, 0);
			}
			else if (medianString.equals(filterMode)) {
				Imgproc.medianBlur(image, output, 3);
			}
			else if (bilateralString.equals(filterMode)) {
				Imgproc.bilateralFilter(image, output, 9, 100, 100);
			}
		}
	}
	private void processOperation_morphological() {
		if (erodeString.equals(currentOperation)) {
			output = imageProcessor.erode(image, kernelSize, currentShape);
		}
		else if (dilateString.equals(currentOperation)) {
			output = imageProcessor.dilate(image, kernelSize, currentShape);
		}
		else if (openString.equals(currentOperation)) {
			output = imageProcessor.open(image, kernelSize, currentShape);
		}
		else if (closeString.equals(currentOperation)) {
			output = imageProcessor.close(image, kernelSize, currentShape);
		}
		updateView(output);
	}
	
	protected void processOperation_edge() {
		Imgproc.cvtColor(originalImage_clone, grayImage, Imgproc.COLOR_RGB2GRAY);
		if(sobelString.equals(currentOperation)){
			Imgproc.Sobel(grayImage, image, -1, xOrder,yOrder,aperture,1.0, 0.0);
//			Core.convertScaleAbs(image, image);
		}
		else if(laplaceString.equals(currentOperation)){
			Imgproc.Laplacian(grayImage, image, -1, aperture, 1.0, 0.0);
//			Core.convertScaleAbs(image, image);
//			Imgproc.threshold(image, image, 1, 255, Imgproc.THRESH_BINARY_INV);
		}
		else if(cannyString.equals(currentOperation)){
			Imgproc.Canny(grayImage, image, lowThreshold, highThreshold, aperture, false);
		}
		else if(noneString.equals(currentOperation)){
			image = originalImage.clone();
		}

		updateView(image);
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
				if (rectangleString.equals(currentShapeString)) {
					currentShape = Imgproc.CV_SHAPE_RECT;
				}
				else if (ellipseString.equals(currentShapeString)) {
					currentShape = Imgproc.CV_SHAPE_ELLIPSE;
				}
				else if (crossString.equals(currentShapeString)) {
					currentShape = Imgproc.CV_SHAPE_CROSS;
				}
				processOperation_morphological();
			}
		};
		rectangleButton.addActionListener(shapeChangeListener);
		ellipseButton.addActionListener(shapeChangeListener);
		crossButton.addActionListener(shapeChangeListener);
		GridLayout gridRowLayout = new GridLayout(1, 0);
		JPanel shapeRadioPanel = new JPanel(gridRowLayout);
		JLabel shapeLabel = new JLabel("Shape:", JLabel.CENTER);
		shapeRadioPanel.add(rectangleButton);
		shapeRadioPanel.add(ellipseButton);
		shapeRadioPanel.add(crossButton);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		controlbar.add(shapeLabel, c);
		c.gridx = 1;
		c.gridy = 2;
		controlbar.add(shapeRadioPanel, c);
	}
	private void setupSizeSlider(JPanel controlbar) {
		JLabel sliderLabel = new JLabel("Kernel size:", JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		int minimum = 0;
		int maximum = 25;
		int initial = kernelSize;
		JSlider levelSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);
		levelSlider.setPreferredSize(new Dimension(400, 50));
		levelSlider.setMajorTickSpacing(2);
		levelSlider.setMinorTickSpacing(1);
		levelSlider.setPaintTicks(true);
		levelSlider.setPaintLabels(true);
		levelSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				kernelSize = (int) source.getValue();
				processOperation_morphological();
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		controlbar.add(sliderLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		controlbar.add(levelSlider, c);
		frame.revalidate();
	}
	//2번째 추가
	
	private void setupHighThresholdSlider(JFrame subframe) {
		highThresholdLabel = new JLabel("High threshold:", JLabel.CENTER);
		highThresholdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int minimum = 0;
		int maximum = 255;
		int initial = 50;

		highThresholdSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		highThresholdSlider.setMajorTickSpacing(20);
		highThresholdSlider.setMinorTickSpacing(2);
		highThresholdSlider.setPaintTicks(true);
		highThresholdSlider.setPaintLabels(true);
		highThresholdSlider.setSnapToTicks(true);
		highThresholdSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				highThreshold  = (int)source.getValue();

				processOperation_edge();

				updateView(image);

			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;

		subframe.add(highThresholdLabel,c);
		c.gridx = 1;
		c.gridy = 5;
		subframe.add(highThresholdSlider,c);


	}

	private void setupTypeRadio_edge(JFrame subframe) {
		JRadioButton noneButton = new JRadioButton(noneString);
		noneButton.setActionCommand(noneString);
		noneButton.setSelected(true);
		JRadioButton binaryButton = new JRadioButton(sobelString);
		binaryButton.setActionCommand(sobelString);
		JRadioButton binaryInvButton = new JRadioButton(laplaceString);
		binaryInvButton.setActionCommand(laplaceString);
		JRadioButton truncateButton = new JRadioButton(cannyString);
		truncateButton.setActionCommand(cannyString);



		ButtonGroup group = new ButtonGroup();
		group.add(noneButton);
		group.add(binaryButton);
		group.add(binaryInvButton);
		group.add(truncateButton);

		ActionListener operationChangeListener = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				currentOperation = event.getActionCommand();
				enableDisableSliders();
				processOperation_edge();
			}
		};

		noneButton.addActionListener(operationChangeListener);
		binaryButton.addActionListener(operationChangeListener);
		binaryInvButton.addActionListener(operationChangeListener);
		truncateButton.addActionListener(operationChangeListener);			

		GridLayout gridRowLayout = new GridLayout(1,0);
		JPanel radioOperationPanel = new JPanel(gridRowLayout);

		JLabel modeLabel = new JLabel("Mode:", JLabel.RIGHT);

		radioOperationPanel.add(noneButton);
		radioOperationPanel.add(binaryButton);
		radioOperationPanel.add(binaryInvButton);
		radioOperationPanel.add(truncateButton);



		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		subframe.add(modeLabel,c);
		c.gridx = 1;
		c.gridy = 0;
		subframe.add(radioOperationPanel,c);

	}
	private void setupTypeRadio_threshold(JFrame frame) {
		JRadioButton noneButton = new JRadioButton(noneString);
		noneButton.setMnemonic(KeyEvent.VK_O);
		noneButton.setActionCommand(noneString);
		noneButton.setSelected(true);
		
		
		JRadioButton binaryButton = new JRadioButton(binaryString);
		binaryButton.setMnemonic(KeyEvent.VK_B);
		binaryButton.setActionCommand(binaryString);
		
		JRadioButton binaryInvButton = new JRadioButton(binaryInvString);
		binaryInvButton.setMnemonic(KeyEvent.VK_I);
		binaryInvButton.setActionCommand(binaryInvString);
		
		JRadioButton truncateButton = new JRadioButton(truncateString);
		truncateButton.setMnemonic(KeyEvent.VK_T);
		truncateButton.setActionCommand(truncateString);
		
		JRadioButton thresholdToZeroButton = new JRadioButton(thresholdToZeroString);
		thresholdToZeroButton.setMnemonic(KeyEvent.VK_Z);
		thresholdToZeroButton.setActionCommand(thresholdToZeroString);
		
		JRadioButton invThresholdToZeroButton = new JRadioButton(invThresholdToZeroString);
		invThresholdToZeroButton.setMnemonic(KeyEvent.VK_N);
		invThresholdToZeroButton.setActionCommand(invThresholdToZeroString);
		
		JRadioButton adaptiveMeanButton = new JRadioButton(adaptiveMeanString);
		adaptiveMeanButton.setMnemonic(KeyEvent.VK_A);
		adaptiveMeanButton.setActionCommand(adaptiveMeanString);
		
		ButtonGroup group = new ButtonGroup();
		group.add(noneButton);
		group.add(binaryButton);
		group.add(binaryInvButton);
		group.add(truncateButton);
		group.add(thresholdToZeroButton);
		group.add(invThresholdToZeroButton);
		group.add(adaptiveMeanButton);
		
		

		ActionListener operationChangeListener = new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				thresholdMode = event.getActionCommand();
				enableDisableSliders();
				processOperation_threshold();
			}
		};
		
		noneButton.addActionListener(operationChangeListener);
		binaryButton.addActionListener(operationChangeListener);
		binaryInvButton.addActionListener(operationChangeListener);
		truncateButton.addActionListener(operationChangeListener);			
		thresholdToZeroButton.addActionListener(operationChangeListener);
        invThresholdToZeroButton.addActionListener(operationChangeListener);
        adaptiveMeanButton.addActionListener(operationChangeListener);
        
		GridLayout gridRowLayout = new GridLayout(1,0);
		JPanel radioOperationPanel = new JPanel(gridRowLayout);

		JLabel modeLabel = new JLabel("Mode:", JLabel.RIGHT);

		radioOperationPanel.add(noneButton);
		radioOperationPanel.add(binaryButton);
		radioOperationPanel.add(binaryInvButton);
		radioOperationPanel.add(truncateButton);
		radioOperationPanel.add(thresholdToZeroButton);
		radioOperationPanel.add(invThresholdToZeroButton);
		radioOperationPanel.add(adaptiveMeanButton);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		frame.add(modeLabel,c);
		c.gridx = 1;
		c.gridy = 0;
		frame.add(radioOperationPanel,c);
	}


	protected void enableDisableSliders() {
		apertureSlider.setMinimum(1);
		apertureSlider.setMaximum(15);

		if(noneString.equals(currentOperation)){
			apertureSliderLabel.setEnabled(false);
			apertureSlider   .setEnabled(false);
			xOrderSliderLabel.setEnabled(false);
			xorderSlider     .setEnabled(false);
			yOrderSliderLabel.setEnabled(false);
			yOrderSlider   .setEnabled(false);
			lowThresholdSliderLabel.setEnabled(false);
			lowThresholdSlider.setEnabled(false);
			highThresholdLabel.setEnabled(false);
			highThresholdSlider.setEnabled(false);
		}
		else if(sobelString.equals(currentOperation)){
			apertureSliderLabel.setEnabled(true);
			apertureSlider   .setEnabled(true);
			xOrderSliderLabel.setEnabled(true);
			xorderSlider     .setEnabled(true);
			yOrderSliderLabel.setEnabled(true);
			yOrderSlider   .setEnabled(true);
			lowThresholdSliderLabel.setEnabled(false);
			lowThresholdSlider.setEnabled(false);
			highThresholdLabel.setEnabled(false);
			highThresholdSlider.setEnabled(false);

		}

		else if(laplaceString.equals(currentOperation)){
			apertureSliderLabel.setEnabled(true);
			apertureSlider   .setEnabled(true);
			xOrderSliderLabel.setEnabled(false);
			xorderSlider     .setEnabled(false);
			yOrderSliderLabel.setEnabled(false);
			yOrderSlider   .setEnabled(false);
			lowThresholdSliderLabel.setEnabled(false);
			lowThresholdSlider.setEnabled(false);
			highThresholdLabel.setEnabled(false);
			highThresholdSlider.setEnabled(false);
		}
		else if(cannyString.equals(currentOperation)){
			if(aperture<3){
				aperture = 3;
			}
			else if(aperture>7){
				aperture = 7;
			}
			apertureSlider.setValue(aperture);
			apertureSliderLabel.setEnabled(true);
			apertureSlider   .setEnabled(true);
			apertureSlider.setMinimum(3);
			apertureSlider.setMaximum(7);
			xOrderSliderLabel.setEnabled(false);
			xorderSlider     .setEnabled(false);
			yOrderSliderLabel.setEnabled(false);
			yOrderSlider   .setEnabled(false);
			lowThresholdSliderLabel.setEnabled(true);
			lowThresholdSlider.setEnabled(true);
			highThresholdLabel.setEnabled(true);
			highThresholdSlider.setEnabled(true);
		}
	}
	protected void processOperation_threshold() {
		Imgproc.cvtColor(originalImage_clone, grayImage, Imgproc.COLOR_RGB2GRAY);
		if(adaptiveMeanString.equals(thresholdMode)){
			Imgproc.adaptiveThreshold(grayImage, image, maxval, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blockSize, constantC);	
		}
		else if(noneString.equals(thresholdMode)){
			image = originalImage.clone();
		}
		else{
			Imgproc.threshold(grayImage, image, level, maxval, modeMap.get(thresholdMode));
		}
		
		updateView(image);
	}

	private void setupApertureSlider(JFrame subframe) {
		apertureSliderLabel = new JLabel("Aperture size:", JLabel.CENTER);
		apertureSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int minimum = 1;
		int maximum = 15;
		int initial =3;

		apertureSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		apertureSlider.setSnapToTicks(true);
		apertureSlider.setMinorTickSpacing(2);

		apertureSlider.setMajorTickSpacing(2);

		apertureSlider.setPaintTicks(true);
		apertureSlider.setPaintLabels(true);
		apertureSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if(apertureSlider.getValueIsAdjusting())
					return;

				JSlider source = (JSlider)e.getSource();
				aperture = (int)source.getValue();


				processOperation_edge();

				updateView(image);

			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;

		subframe.add(apertureSliderLabel,c);
		c.gridx = 1;
		c.gridy = 1;
		subframe.add(apertureSlider,c);
	}


	private void setupXOrderSlider(JFrame subframe) {
		xOrderSliderLabel = new JLabel("Sobel X Order:", JLabel.CENTER);
		xOrderSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int minimum = 0;
		int maximum = 2;
		int initial =1;

		xorderSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		xorderSlider.setMajorTickSpacing(1);
		xorderSlider.setMinorTickSpacing(1);
		xorderSlider.setPaintTicks(true);
		xorderSlider.setPaintLabels(true);
		xorderSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				xOrder = (int)source.getValue();
				processOperation_edge();

				updateView(image);

			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;

		subframe.add(xOrderSliderLabel,c);
		c.gridx = 1;
		c.gridy = 2;
		subframe.add(xorderSlider,c);

	}

	private void setupYOrderSlider(JFrame subframe) {
		yOrderSliderLabel = new JLabel("Sobel Y order:", JLabel.CENTER);
		yOrderSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int minimum = 0;
		int maximum = 2;
		int initial =1;

		yOrderSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		yOrderSlider.setMajorTickSpacing(1);
		yOrderSlider.setMinorTickSpacing(1);
		yOrderSlider.setPaintTicks(true);
		yOrderSlider.setPaintLabels(true);
		yOrderSlider.setSnapToTicks(true);
		yOrderSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				yOrder = (int)source.getValue();
				processOperation_edge();
				updateView(image);
			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;

		subframe.add(yOrderSliderLabel,c);
		c.gridx = 1;
		c.gridy = 3;
		subframe.add(yOrderSlider,c);

	}

	private void setupLowThresholdSlider(JFrame subframe) {
		lowThresholdSliderLabel = new JLabel("Low threshold:", JLabel.CENTER);
		lowThresholdSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		int minimum = 0;
		int maximum = 255;
		int initial = 10;

		lowThresholdSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);
		lowThresholdSlider.setMajorTickSpacing(20);
		lowThresholdSlider.setMinorTickSpacing(2);
		lowThresholdSlider.setPaintTicks(true);
		lowThresholdSlider.setPaintLabels(true);
		lowThresholdSlider.setSnapToTicks(true);
		lowThresholdSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();

				lowThreshold  = (int)source.getValue();

				processOperation_edge();

				updateView(image);

			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;

		subframe.add(lowThresholdSliderLabel,c);
		c.gridx = 1;
		c.gridy = 4;
		subframe.add(lowThresholdSlider,c);

	}

	
	protected void enableDisableSliders_threshold() {
		if(noneString.equals(thresholdMode)){
			levelSlider   .setEnabled(false);
			maxSlider     .setEnabled(false);
			blockSlider   .setEnabled(false);
			constantSlider.setEnabled(false);
		}
		else if(adaptiveMeanString.equals(thresholdMode)){
			levelSlider   .setEnabled(false);
			maxSlider     .setEnabled(true);
			blockSlider   .setEnabled(true);
			constantSlider.setEnabled(true);
		}
		else{
			if(binaryString.equals(thresholdMode)|| 
					binaryInvString.equals(thresholdMode)){
				levelSlider   .setEnabled(true);
				maxSlider     .setEnabled(true);
				blockSlider   .setEnabled(false);
				constantSlider.setEnabled(false);
				
			}
			else{
				levelSlider   .setEnabled(true);
				maxSlider     .setEnabled(false);
				blockSlider   .setEnabled(false);
				constantSlider.setEnabled(false);
			}
		}
		
	}

	private void setupThresholdSlider(JFrame frame) {
		JLabel sliderLabel = new JLabel("Threshold:", JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		int minimum = 0;
		int maximum = 255;
		int initial =110;

		levelSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		levelSlider.setMajorTickSpacing(20);
		levelSlider.setMinorTickSpacing(5);
		levelSlider.setPaintTicks(true);
		levelSlider.setPaintLabels(true);
		levelSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				level = (int)source.getValue();
				processOperation_threshold();
				
				updateView(image);
				
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;

		frame.add(sliderLabel,c);
		c.gridx = 1;
		c.gridy = 1;
		frame.add(levelSlider,c);
	}
	

	private void setupMaxSlider(JFrame frame) {
		JLabel sliderLabel = new JLabel("Max value:", JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		int minimum = 0;
		int maximum = 255;
		int initial =255;

		maxSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		maxSlider.setMajorTickSpacing(20);
		maxSlider.setMinorTickSpacing(5);
		maxSlider.setPaintTicks(true);
		maxSlider.setPaintLabels(true);
		maxSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				maxval = (int)source.getValue();
				processOperation_threshold();
				
				updateView(image);
				
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;

		frame.add(sliderLabel,c);
		c.gridx = 1;
		c.gridy = 2;
		frame.add(maxSlider,c);
		
	}
	
	private void setupBlockSlider(JFrame frame) {
		JLabel blockLabel = new JLabel("Block size:", JLabel.CENTER);
		blockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		int minimum = 3;
		int maximum = 255;
		int initial =3;

		blockSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		blockSlider.setMajorTickSpacing(20);
		blockSlider.setMinorTickSpacing(2);
		blockSlider.setPaintTicks(true);
		blockSlider.setPaintLabels(true);
		blockSlider.setSnapToTicks(true);
		blockSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				
				if(blockSlider.getValueIsAdjusting()){
					int maybeEven = (int)source.getValue();					
					blockSize  = ((maybeEven%2)==0)? (maybeEven+1) :maybeEven;
				}
				else{
					blockSize = (int)source.getValue();
				}
				
				processOperation_threshold();
				
				updateView(image);
				
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;

		frame.add(blockLabel,c);
		c.gridx = 1;
		c.gridy = 3;
		frame.add(blockSlider,c);
		
	}
	
	private void setupCSlider(JFrame frame) {
		JLabel constantLabel = new JLabel("C constant:", JLabel.CENTER);
		constantLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		int minimum = -100;
		int maximum = 100;
		int initial =5;

		constantSlider = new JSlider(JSlider.HORIZONTAL,
				minimum, maximum, initial);

		constantSlider.setMajorTickSpacing(20);
		constantSlider.setMinorTickSpacing(2);
		constantSlider.setPaintTicks(true);
		constantSlider.setPaintLabels(true);
		constantSlider.setSnapToTicks(true);
		constantSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				
				constantC  = (int)source.getValue();
				
				processOperation_threshold();
				
				updateView(image);
				
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;

		frame.add(constantLabel,c);
		c.gridx = 1;
		c.gridy = 4;
		frame.add(constantSlider,c);
		
	}
	//pyramid
	private void setupButton(JFrame frame) {
		final JPanel buttonsPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		buttonsPanel.setLayout(flowLayout);

		JButton restoreButton = new JButton("Restore");
		restoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				image = originalImage.clone();
				updateView(originalImage);
			}
		});
		
		JButton pyramidDown = new JButton("Pyramid Down");
		pyramidDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Imgproc.pyrDown(image, image);
				updateView(image);
			}
		});
		
		JButton pyramidUp = new JButton("Pyramid Up");
		pyramidUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Imgproc.pyrUp(image, image);
				updateView(image);
			}
		});
		
		JButton laplacian = new JButton("Laplacian");
		laplacian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Mat gp1 = new Mat();
				Imgproc.pyrDown(image, gp1);
				Imgproc.pyrUp(gp1, gp1);
				Core.subtract(image, gp1, gp1);
				updateView(gp1);
			}
		});
		
		buttonsPanel.add(restoreButton);
		buttonsPanel.add(pyramidDown);
		buttonsPanel.add(pyramidUp);
		buttonsPanel.add(laplacian);
		frame.add(buttonsPanel);
	}
	
	// Histogram Equalization
	protected void processOperation_histogram() {
		Imgproc.cvtColor(originalImage_clone, grayImage, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(grayImage, image);
		updateView_histogram();
		
		
		
	}
	private void updateView_histogram() {
		Mat newMat = image;
		Image outputImage1 = imageProcessor.toBufferedImage(newMat);
		output = originalImage.clone();
		imageView2.setIcon(new ImageIcon(outputImage1));
		title.removeAll();
		title.add(new JLabel(currentOperation));
		title.revalidate();
		controlbar.revalidate();
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
	//histo
	
}
