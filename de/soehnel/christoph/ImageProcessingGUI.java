package de.soehnel.christoph;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

public class ImageProcessingGUI extends JFrame implements ActionListener,
		MouseListener, MouseMotionListener, MenuListener {

	private static final long serialVersionUID = 1L;

	private JDesktopPane desk;

	private JMenuBar menubar;

	private File dest;

	private ImageInputOutput imgio;

	private int mouse_x;

	private int mouse_y;

	private int mouse_last_x;

	private int mouse_last_y;

	private JMenu fileMenu = null;

	private JMenu actionMenu = null;

	private JMenu extraMenu = null;

	private JPanel busybar_panel = null;

	private JProgressBar busybar = null;

	private int fenster_nummer;

	private boolean projTrafoFlag;

	private int projTrafoCount;

	private Point projTrafoPoint1;

	private Point projTrafoPoint2;

	private Point projTrafoPoint3;

	private Point projTrafoPoint4;

	public ImageProcessingGUI() {
		super("Bildverarbeitung 2010/2011 - Christoph Söhnel");
		this.desk = new JDesktopPane();
		this.menubar = new JMenuBar();
		fileMenu = createFileMenu();
		actionMenu = createActionMenu();
		extraMenu = createExtraMenu();
		menubar.add(fileMenu);
		menubar.add(actionMenu);
		menubar.add(extraMenu);
		busybar = new JProgressBar();
		busybar.setIndeterminate(true);
		busybar.setVisible(false);
		busybar_panel = new JPanel();
		busybar_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		busybar_panel.add(busybar);
		menubar.add(busybar_panel);
		desk.setDesktopManager(new DefaultDesktopManager());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setContentPane(desk);
		setJMenuBar(menubar);
		setIconImage(Toolkit.getDefaultToolkit().getImage("brb.jpg"));
		dest = null;
		imgio = new ImageInputOutput(this);
		fenster_nummer = 0;
		projTrafoFlag = false;
		projTrafoPoint1 = new Point(0, 0);
		projTrafoPoint2 = new Point(0, 0);
		projTrafoPoint3 = new Point(0, 0);
		projTrafoPoint4 = new Point(0, 0);
		projTrafoCount = 0;
	}

	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
		File filename = null;
		String filenamestr = null;
		if (action.equals("Öffnen")) {
			filename = fileDialog(action);
			if (filename != null) {
				filenamestr = filename.toString().substring(
						filename.toString().lastIndexOf("\\") + 1);
				if (filenamestr == null)
					filenamestr = filename.toString();
				addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ filenamestr), 10, 10, imgio.loadImage(filename));
			}
		}
		if (action.equals("Speichern")) {
			filename = fileDialog(action);
			if (filename != null)
				imgio.saveImage(
						filename,
						((ChildFrame) desk.getSelectedFrame()).viewComponent.image);
		}
		if (action.equals("Beenden")) {
			System.exit(0);
		}
		if (action.equals("Graukeil")) {
			showGrayWedge();
		}
		if (action.equals("Histogramm(e) anzeigen")) {
			showHistogram();
		}
		if (action.equals("Akk. Histogramm(e) anzeigen")) {
			showAkkHistogram();
		}
		if (action.equals("Histogrammausgleich")) {
			showEnhancedImage();
		}
		if (action.equals("Binärbild erzeugen (schwächster Anstieg)")) {
			showBinarizedImage();
		}
		if (action.equals("Binärbild erzeugen (GDS)")) {
			showBinarizedImageGDS();
		}
		if (action.equals("Binärbild erzeugen (C-Means/gefenstert)")) {
			showBinarizedImageWindowed();
		}
		if (action.equals("Binärbild erzeugen (C-Means)")) {
			showBinarizedImageCMeans();
		}
		if (action.equals("Lichtschnitt(e) anzeigen")) {
			ViewComponent vc = ((ChildFrame) desk.getSelectedFrame()).viewComponent;
			vc.addMouseListener(this);
			vc.addMouseMotionListener(this);
			vc.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		if (action.equals("Gauß")) {
			applicateBinFil();
		}
		if (action.equals("Median")) {
			applicateMedFil();
		}
		if (action.equals("Custom")) {
			applicateCusFil();
		}
		if (action.equals("Laplace")) {
			applicateLapFil();
		}
		if (action.equals("Sobel")) {
			applicateSobFil();
		}
		if (action.equals("8-Bit-Graustufenbild erzeugen")) {
			showGrayScaleImage();
		}
		if (action.equals("Differenzbild anzeigen")) {
			showDifferenceImage();
		}
		if (action.equals("Akk. Intensity-Histogramm anzeigen")) {
			showAkkHSIIntensityHistogram();
		}
		if (action.equals("Intensity-Histogramm anzeigen")) {
			showHSIIntensityHistogram();
		}
		if (action.equals("Projektiv-Transformation")) {
			ViewComponent vc = ((ChildFrame) desk.getSelectedFrame()).viewComponent;
			vc.addMouseListener(this);
			vc.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			projTrafoFlag = true;
			projTrafoCount = 0;
		}
		if (action.equals("HoughLines")) {
			applicateHoughLines();
		}
	}

	public File fileDialog(String mode) {
		int result = 0;
		JFileChooser d = new JFileChooser(dest);
		d.setAcceptAllFileFilterUsed(false);
		d.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".bmp")
						|| f.getName().toLowerCase().endsWith(".jpg")
						|| f.getName().toLowerCase().endsWith(".raw");
			}

			public String getDescription() {
				return "*.bmp;*.jpg;*.raw";
			}
		});
		if (mode.equals("Öffnen"))
			result = d.showOpenDialog(null);
		else
			result = d.showSaveDialog(null);

		if (result == JFileChooser.CANCEL_OPTION)
			dest = null;
		else
			dest = d.getSelectedFile();
		return dest;
	}

	public JMenu createFileMenu() {
		JMenu ret = new JMenu("Datei");
		ret.setMnemonic('D');
		ret.addMenuListener(this);
		JMenuItem mi;

		// ÷ffnen
		mi = new JMenuItem("Öffnen", 'f');
		setCtrlAccelerator(mi, 'O');
		mi.addActionListener(this);
		ret.add(mi);

		// Speichern
		mi = new JMenuItem("Speichern", 'p');
		setCtrlAccelerator(mi, 'S');
		mi.addActionListener(this);
		ret.add(mi);

		// Seperator
		ret.addSeparator();

		// Beenden
		mi = new JMenuItem("Beenden", 'e');
		mi.addActionListener(this);
		ret.add(mi);

		return ret;
	}

	public JMenu createActionMenu() {
		JMenu ret = new JMenu("Aktion");
		ret.setMnemonic('A');
		ret.addMenuListener(this);
		JMenuItem mi;

		// Graustufen
		mi = new JMenuItem("8-Bit-Graustufenbild erzeugen", 'r');
		mi.addActionListener(this);
		ret.add(mi);

		// Binarisieren mittels C-Means
		mi = new JMenuItem("Binärbild erzeugen (C-Means)", 'n');
		mi.addActionListener(this);
		ret.add(mi);

		// Gefenstert binarisieren mittels C-Means
		mi = new JMenuItem("Binärbild erzeugen (C-Means/gefenstert)", 'g');
		mi.addActionListener(this);
		ret.add(mi);

		// Binarisieren mittels GDS
		mi = new JMenuItem("Binärbild erzeugen (GDS)", 'i');
		mi.addActionListener(this);
		ret.add(mi);

		// Differenzbild
		mi = new JMenuItem("Differenzbild anzeigen", 'f');
		mi.addActionListener(this);
		ret.add(mi);

		// Projektiv-Transformation
		mi = new JMenuItem("Projektiv-Transformation", 'p');
		mi.addActionListener(this);
		ret.add(mi);

		// Hough-Transformation (Lines)
		mi = new JMenuItem("HoughLines", 'h');
		mi.addActionListener(this);
		ret.add(mi);

		// Filter
		mi = createFilterSubMenu();
		mi.addActionListener(this);
		ret.add(mi);

		// Histogrammoperationen
		mi = createHistogramOperationsSubMenu();
		mi.addActionListener(this);
		ret.add(mi);

		return ret;
	}

	public JMenu createExtraMenu() {
		JMenu ret = new JMenu("Extras");
		ret.setMnemonic('E');
		JMenuItem mi;

		// Graukeil
		mi = new JMenuItem("Graukeil", 'r');
		setCtrlAccelerator(mi, 'G');
		mi.addActionListener(this);
		ret.add(mi);

		return ret;
	}

	public JMenu createHistogramOperationsSubMenu() {
		JMenu ret = new JMenu("Histogramm-Operationen");
		JMenuItem mi;
		ret.setMnemonic('H');
		mi = new JMenuItem("Histogramm(e) anzeigen", 'i');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Akk. Histogramm(e) anzeigen", 'k');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Intensity-Histogramm anzeigen", 't');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Akk. Intensity-Histogramm anzeigen", 'y');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Lichtschnitt(e) anzeigen", 'c');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Histogrammausgleich", 's');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Binärbild erzeugen (schwächster Anstieg)", 'n');
		mi.addActionListener(this);
		ret.add(mi);
		return ret;
	}

	public JMenu createFilterSubMenu() {
		JMenu ret = new JMenu("Filter");
		JMenuItem mi;
		ret.setMnemonic('T');
		mi = new JMenuItem("Gauß", 'G');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Laplace", 'L');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Sobel", 'S');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Median", 'M');
		mi.addActionListener(this);
		ret.add(mi);
		mi = new JMenuItem("Custom", 'C');
		mi.addActionListener(this);
		ret.add(mi);
		return ret;
	}

	private void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}

	public void addChild(ChildFrame child, int x, int y, BufferedImage image) {
		int width = 0, height = 0;
		child.viewComponent.setImage(image);
		child.setLocation(x, y);
		width = child.viewComponent.getWidth();
		if (width >= 800)
			width = 800;
		height = child.viewComponent.getHeight();
		if (height >= 600)
			height = 600;
		child.setSize(width + 13, height + 36);
		child.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		desk.add(child);
		child.setVisible(true);
	}

	public void showGrayWedge() {
		busybar.setVisible(true);
		addChild(
				new ChildFrame("Bild " + ++fenster_nummer + " - " + "Graukeil"),
				10, 10, new GrayWedge(2).image);
		busybar.setVisible(false);
	}

	// Histogramm anzeigen
	public void showHistogram() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (ImageInputOutput.is1ByteGray(bimage))
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Graustufen-Histogramm: " + path), 10, 10,
					new Histogram().getHistogram(bimage, 0, Color.GRAY, false,
							false));
		else {
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Rotkanal-Histogramm: " + path), 10, 10,
					new Histogram().getHistogram(bimage, 1, Color.RED, false,
							false));
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Grünkanal-Histogramm: " + path), 30, 30,
					new Histogram().getHistogram(bimage, 2, Color.GREEN, false,
							false));
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Blaukanal-Histogramm: " + path), 50, 50,
					new Histogram().getHistogram(bimage, 0, Color.BLUE, false,
							false));
		}
		busybar.setVisible(false);
	}

	// Akkumuliertes Histogramm anzeigen
	public void showAkkHistogram() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (ImageInputOutput.is1ByteGray(bimage))
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Akk. Graustufen-Histogramm: " + path), 10, 10,
					new Histogram().getHistogram(bimage, 0, Color.GRAY, false,
							true));
		else {
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Akk. Rotkanal-Histogramm: " + path), 10, 10,
					new Histogram().getHistogram(bimage, 1, Color.RED, false,
							true));
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Akk. Grünkanal-Histogramm: " + path), 30, 30,
					new Histogram().getHistogram(bimage, 2, Color.GREEN, false,
							true));
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Akk. Blaukanal-Histogramm: " + path), 50, 50,
					new Histogram().getHistogram(bimage, 0, Color.BLUE, false,
							true));
		}
		busybar.setVisible(false);
	}

	// Akkumuliertes Intensity-Histogramm (HSI) anzeigen
	public void showAkkHSIIntensityHistogram() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Akk. Intensity-Histogramm: " + path), 10, 10,
				new Histogram().getHistogram(bimage, 0, Color.CYAN, true, true));
		busybar.setVisible(false);
	}

	// Intensity-Histogramm (HSI) anzeigen
	public void showHSIIntensityHistogram() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Intensity-Histogramm: " + path), 10, 10,
				new Histogram()
						.getHistogram(bimage, 0, Color.CYAN, true, false));
		busybar.setVisible(false);
	}

	// Gauﬂfilter anwenden
	public void applicateBinFil() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		FilterSizeDialog filterdlg = new FilterSizeDialog(this,
				"Wählen der Kerneldimension", true);
		filterdlg.setSize(230, 90);
		filterdlg.setLocation(400, 400);
		filterdlg.setResizable(false);
		filterdlg.setVisible(true);
		BinomialFilter bf = new BinomialFilter(bimage.getWidth(),
				bimage.getHeight());
		if (filterdlg.getDimension() > 0) {
			bf.bfInit(filterdlg.getDimension());
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Gauﬂfilter: " + path), 10, 10,
					bf.bfApp(bimage, filterdlg.getDimension()));
		}
		busybar.setVisible(false);
	}

	// Medianfilter anwenden
	public void applicateMedFil() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		FilterSizeDialog filterdlg = new FilterSizeDialog(this,
				"Wählen der Kerneldimension", true);
		filterdlg.setSize(230, 90);
		filterdlg.setLocation(400, 400);
		filterdlg.setResizable(false);
		filterdlg.setVisible(true);
		MedianFilter mf = new MedianFilter(bimage.getWidth(),
				bimage.getHeight());
		if (filterdlg.getDimension() > 0) {
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Medianfilter: " + path), 10, 10,
					mf.applicateFilter(bimage, filterdlg.getDimension()));
		}
		busybar.setVisible(false);
	}

	// Benutzerdefinierten Filter anwenden
	public void applicateCusFil() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		FilterSizeDialog filterdlg = new FilterSizeDialog(this,
				"Wählen der Kerneldimension", true);
		filterdlg.setSize(230, 90);
		filterdlg.setLocation(400, 400);
		filterdlg.setResizable(false);
		filterdlg.setVisible(true);
		if (filterdlg.getDimension() > 0) {
			CustomFilterDialog cfdlg = new CustomFilterDialog(this,
					"Filterkernel eingeben", true, filterdlg.getDimension(),
					filterdlg.getDimension());
			cfdlg.setSize(200, 250);
			cfdlg.setLocation(400, 400);
			cfdlg.setResizable(false);
			cfdlg.setVisible(true);
			if (cfdlg.getWeight() > 0) {
				CustomFilter cf = new CustomFilter(bimage.getWidth(),
						bimage.getHeight(), cfdlg.getFilterKernel(),
						cfdlg.getWeight());
				addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Custom-Filter: " + path), 10, 10,
						cf.applicateFilter(bimage, filterdlg.getDimension()));
			}
		}
		busybar.setVisible(false);
	}

	// Laplace-Filter anwenden
	public void applicateLapFil() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		FilterSizeDialog filterdlg = new FilterSizeDialog(this,
				"Wählen der Kerneldimension", true);
		filterdlg.setSize(230, 90);
		filterdlg.setLocation(400, 400);
		filterdlg.setResizable(false);
		filterdlg.setVisible(true);
		LaplaceFilter lf = new LaplaceFilter(bimage.getWidth(),
				bimage.getHeight());
		if (filterdlg.getDimension() > 0) {
			lf.initFilter(filterdlg.getDimension());
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Laplace: " + path), 10, 10, lf.applicateFilter(bimage));
		}
		busybar.setVisible(false);
	}

	// Sobel-Filter anwenden
	public void applicateSobFil() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		FilterSizeDialog filterdlg = new FilterSizeDialog(this,
				"Wählen der Kerneldimension", true);
		filterdlg.setSize(230, 90);
		filterdlg.setLocation(400, 400);
		filterdlg.setResizable(false);
		filterdlg.setVisible(true);
		SobelFilter sf = new SobelFilter(bimage.getWidth(), bimage.getHeight());
		if (filterdlg.getDimension() > 0) {
			sf.initFilter(filterdlg.getDimension());
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Sobel: " + path), 10, 10, sf.applicateFilter(bimage));
		}
		busybar.setVisible(false);
	}

	// Projektivtransformation anwenden
	public void applicateProjTrafo() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		ProjectiveTransform pt = new ProjectiveTransform(bimage.getWidth(),
				bimage.getHeight());
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Projektiv-Transformation: " + path), 10, 10,
				pt.applicateProjTrafo(bimage, projTrafoPoint1, projTrafoPoint2,
						projTrafoPoint3, projTrafoPoint4));
		busybar.setVisible(false);
	}

	// HoughLines
	public void applicateHoughLines() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		HoughLines hl = new HoughLines();
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "HoughLines: " + path), 10, 10, hl.transform(bimage));
		busybar.setVisible(false);
	}

	public void mouseClicked(MouseEvent arg0) {
		if (projTrafoFlag == true) {
			switch (projTrafoCount) {
			case 0:
				projTrafoPoint1.setLocation(arg0.getPoint());
				projTrafoCount++;
				break;
			case 1:
				projTrafoPoint2.setLocation(arg0.getPoint());
				projTrafoCount++;
				break;
			case 2:
				projTrafoPoint3.setLocation(arg0.getPoint());
				projTrafoCount++;
				break;
			case 3:
				projTrafoPoint4.setLocation(arg0.getPoint());
				projTrafoCount++;
				projTrafoFlag = false;
				ViewComponent vc = ((ChildFrame) desk.getSelectedFrame()).viewComponent;
				vc.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				vc.removeMouseListener(this);
				applicateProjTrafo();
				break;
			default:
			}
		}
	}

	// Anfangskoordinaten f¸r Lichtschnitt
	public void mousePressed(MouseEvent arg0) {
		if (projTrafoFlag == false) {
			// TODO Auto-generated method stub
			mouse_x = arg0.getX();
			mouse_y = arg0.getY();
			mouse_last_x = arg0.getX();
			mouse_last_y = arg0.getY();
		}
	}

	// Lichtschnitt erzeugen
	public void mouseReleased(MouseEvent arg0) {
		if (projTrafoFlag == false) {
			// TODO Auto-generated method stub
			busybar.setVisible(true);
			ViewComponent vc = ((ChildFrame) desk.getSelectedFrame()).viewComponent;
			String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
			vc.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			vc.removeMouseListener(this);
			vc.removeMouseMotionListener(this);
			Graphics2D gfx = (Graphics2D) ((ChildFrame) desk.getSelectedFrame()).viewComponent
					.getRootPane().getGlassPane().getGraphics();
			gfx.setXORMode(new Color(238, 238, 238));
			gfx.drawLine(mouse_x, mouse_y, mouse_last_x, mouse_last_y);
			BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
			if (ImageInputOutput.is1ByteGray(bimage))
				addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Graukanal-Lichtschnitt"), 10, 10,
						new LineHistogram().getLineHistogram(bimage, mouse_x,
								mouse_y, arg0.getX(), arg0.getY(), 0,
								Color.GRAY));
			else {
				addChild(
						new ChildFrame("Bild " + ++fenster_nummer + " - "
								+ "Rotkanal-Lichtschnitt: " + path),
						10,
						10,
						new LineHistogram().getLineHistogram(bimage, mouse_x,
								mouse_y, arg0.getX(), arg0.getY(), 1, Color.RED));
				addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Grünkanal-Lichtschnitt " + path), 20, 20,
						new LineHistogram().getLineHistogram(bimage, mouse_x,
								mouse_y, arg0.getX(), arg0.getY(), 2,
								Color.GREEN));
				addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Blaukanal-Lichtschnitt " + path), 30, 30,
						new LineHistogram().getLineHistogram(bimage, mouse_x,
								mouse_y, arg0.getX(), arg0.getY(), 0,
								Color.BLUE));
			}
			busybar.setVisible(false);
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	// Linie f¸r Lichtschnitt
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Graphics2D gfx = (Graphics2D) ((ChildFrame) desk.getSelectedFrame()).viewComponent
				.getRootPane().getGlassPane().getGraphics();
		gfx.setXORMode(new Color(238, 238, 238));
		gfx.drawLine(mouse_x, mouse_y, mouse_last_x, mouse_last_y);
		mouse_last_x = arg0.getX();
		mouse_last_y = arg0.getY();
		gfx.drawLine(mouse_x, mouse_y, mouse_last_x, mouse_last_y);
		gfx.dispose();
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	// Histogrammausgleich
	public void showEnhancedImage() {
		busybar.setVisible(true);
		String luttype = "Bild " + ++fenster_nummer + " - Look Up Table - ";
		Histogram histo = new Histogram();
		Histogram2EnhancedImage histo_ei = null;
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (ImageInputOutput.is1ByteGray(bimage)) {
			histo.generateHistogram(bimage, 1, true);
			histo_ei = new Histogram2EnhancedImage(histo.getHistogramValues());
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Histogrammausgleich (Akk.): " + path), 10, 10,
					histo_ei.enhanceImage(bimage));
			luttype += "Grau";
		} else {
			histo.generateIntensityHistogram(bimage, true);
			histo_ei = new Histogram2EnhancedImage(histo.getHistogramValues());
			addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
					+ "Histogrammausgleich (Akk.): " + path), 10, 10,
					histo_ei.enhanceRGBImage(bimage));
			luttype += "Intensity (HSI)";
		}
		addChild(new ChildFrame(luttype), 10, 10,
				histo.visualizeLUT(histo_ei.getLUT()));
		busybar.setVisible(false);
	}

	// Binärbild anzeigen (Schwellwert)
	public void showBinarizedImage() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (!ImageInputOutput.is1ByteGray(bimage))
			bimage = imgio.to1ByteGray(bimage);
		BinaryImage bi = new BinaryImage();
		Histogram histo = new Histogram();
		histo.generateHistogram(bimage, 0, false);
		HistogramSmallestGrowingDialog histosgdlg = new HistogramSmallestGrowingDialog(
				this, "Umwandlung zu Binärbild", true,
				histo.getSmallestGrowing(histo.getHistogramValues()));
		histosgdlg.setSize(310, 70);
		histosgdlg.setLocation(400, 400);
		histosgdlg.setResizable(false);
		histosgdlg.setVisible(true);
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Binärbild, Schwellwert: " + histosgdlg.getThreshold() + ": "
				+ path), 10, 10,
				bi.binarizeImage(bimage, histosgdlg.getThreshold()));
		busybar.setVisible(false);
	}

	// Binärbild anzeigen (Globaler Dynamischer Schwellwert)
	public void showBinarizedImageGDS() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (!ImageInputOutput.is1ByteGray(bimage))
			bimage = imgio.to1ByteGray(bimage);
		BinaryImage bi = new BinaryImage();
		GlobalDynamicThresholdDialog gdtdlg = new GlobalDynamicThresholdDialog(
				this, "Umwandlung zu Binärbild (GDS)", true);
		gdtdlg.setSize(300, 140);
		gdtdlg.setLocation(400, 400);
		gdtdlg.setResizable(false);
		gdtdlg.setVisible(true);
		addChild(
				new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Bin‰rbild, GDS: " + path),
				10,
				10,
				bi.binarizeImageGDT(bimage, gdtdlg.getParameterR(),
						gdtdlg.getNeighbours(), gdtdlg.getOverlap()));
		busybar.setVisible(false);
	}

	// Binärbild anzeigen (C-Means)
	public void showBinarizedImageCMeans() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (!ImageInputOutput.is1ByteGray(bimage))
			bimage = imgio.to1ByteGray(bimage);
		BinaryImage bi = new BinaryImage();
		CMeansWindowedDialog cmdlg = new CMeansWindowedDialog(this,
				"Umwandlung zu Binärbild", true);
		cmdlg.setSize(300, 160);
		cmdlg.setLocation(400, 400);
		cmdlg.setResizable(false);
		cmdlg.setNumClusterChangeDisabled(true);
		cmdlg.setMChangeDisabled(true);
		cmdlg.setNChangeDisabled(true);
		cmdlg.setVisible(true);
		addChild(
				new ChildFrame("Bild " + ++fenster_nummer + " - "
						+ "Binärbild, CMeans: " + path),
				10,
				10,
				bi.binarizeImageCMeans(bimage, cmdlg.getNumCluster(),
						cmdlg.getErrorThresh()));
		busybar.setVisible(false);
	}

	// Graustufenbild anzeigen
	public void showGrayScaleImage() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Graustufenbild: " + path), 10, 10, imgio.to1ByteGray(bimage));
		busybar.setVisible(false);
	}

	// Gefenstertes Bin‰rbild anzeigen (C-Means)
	public void showBinarizedImageWindowed() {
		busybar.setVisible(true);
		BufferedImage bimage = ((ChildFrame) desk.getSelectedFrame()).viewComponent.image;
		String path = ((ChildFrame) desk.getSelectedFrame()).getTitle();
		if (!ImageInputOutput.is1ByteGray(bimage))
			bimage = imgio.to1ByteGray(bimage);
		BinaryImage bi = new BinaryImage();
		CMeansWindowedDialog cmdlg = new CMeansWindowedDialog(this,
				"Umwandlung zu Binärbild (gefenstert)", true);
		cmdlg.setSize(300, 160);
		cmdlg.setLocation(400, 400);
		cmdlg.setResizable(false);
		cmdlg.setNumClusterChangeDisabled(true);
		cmdlg.setVisible(true);
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Binärbild, gefenstert: " + path), 10, 10,
				bi.binarizeImageWindowed(bimage, cmdlg.getM(), cmdlg.getN(),
						cmdlg.getNumCluster(), cmdlg.getErrorThresh()));
		busybar.setVisible(false);
	}

	// Differenzbild anzeigen
	public void showDifferenceImage() {
		Vector<String> frametitles = new Vector<String>();
		Vector<JInternalFrame> frames = new Vector<JInternalFrame>();
		String pathA = null;
		String pathB = null;
		frames.addAll(Arrays.asList(desk.getAllFrames()));
		Enumeration e1 = frames.elements();
		while (e1.hasMoreElements()) {
			frametitles.add(((ChildFrame) e1.nextElement()).title);
		}
		DifferenceDialog difdlg = new DifferenceDialog(this,
				"Differenzbild A-B", true, frametitles);
		difdlg.setSize(250, 130);
		difdlg.setLocation(400, 400);
		difdlg.setResizable(false);
		difdlg.setVisible(true);
		Enumeration e2 = frames.elements();
		BufferedImage image_a = null;
		BufferedImage image_b = null;
		ChildFrame tempframe;
		while (e2.hasMoreElements()) {
			tempframe = (ChildFrame) e2.nextElement();
			if (tempframe.title.equals(difdlg.getFirstImageTitle())) {
				image_a = tempframe.viewComponent.image;
				pathA = tempframe.getTitle();
			}
			if (tempframe.title.equals(difdlg.getSecondImageTitle())) {
				image_b = tempframe.viewComponent.image;
				pathB = tempframe.getTitle();
			}
		}
		Difference diff = new Difference(image_a, image_b);
		addChild(new ChildFrame("Bild " + ++fenster_nummer + " - "
				+ "Differenzbild: " + pathA + " / " + pathB), 10, 10,
				diff.getDifferenceImage());
	}

	public void menuSelected(MenuEvent arg0) {
		// TODO Auto-generated method stub
		if ((ChildFrame) desk.getSelectedFrame() == null)
			for (int i = 0; i < actionMenu.getItemCount(); i++) {
				fileMenu.getItem(1).setEnabled(false);
				actionMenu.getItem(i).setEnabled(false);
			}
		else
			for (int i = 0; i < actionMenu.getItemCount(); i++) {
				fileMenu.getItem(1).setEnabled(true);
				actionMenu.getItem(i).setEnabled(true);
			}
	}

	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}
}
