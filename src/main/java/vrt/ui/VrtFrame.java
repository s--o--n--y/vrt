package vrt.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import vrt.pdf.VrtReport;
import vrt.pdf.VrtReportInfo;
import vrt.ui.callback.StopCallback;
import vrt.utils.LabelFactory;

public class VrtFrame {

	private final VrtBackground background;
	private final GlassPanel glassPanel;
	private final JFrame frame = new JFrame("OpenVrt");
	private final JMenuItem newSession10;
	private final JMenuItem newSession20;
	private final JMenuItem newSession30;
	private final JMenuItem newSession40;
	private final JMenuItem endSession;
	private final JMenuItem pause;
	private final JButton hit;

	public VrtFrame() throws HeadlessException {

		// Create and set up the window.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Start creating and adding components.
		JCheckBox changeButton = new JCheckBox("Punkt gesehen");
		changeButton.setSelected(true);

		hit = new JButton("Punkt gesehen");

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout());
		// menuPanel.add(changeButton);
		// menuPanel.add(new JButton("Button 1"));
		// menuPanel.add(new JButton("Button 2"));
		// Set up the content pane, where the "main GUI" lives.
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		// contentPane.add(menuPanel, BorderLayout.NORTH);
		background = new VrtBackground();
		contentPane.add(background, BorderLayout.CENTER);
		glassPanel = new GlassPanel(background, 0, new StopCallback() {

			@Override
			public void stopVrt() {
				endSession.doClick();

			}
		});
		glassPanel.setVisible(true);
		JMenuBar menuBar = new JMenuBar();

		// Set up the glass pane, which appears over both menu bar
		// and content pane and is an item listener on the change
		// button.
		// glassPane = new VrtGlass(changeButton, menuBar,
		// frame.getContentPane());
		// changeButton.addItemListener(glassPane);
		// frame.setGlassPane(glassPane);
		frame.setSize(new Dimension(1024, 768));
		hit.addActionListener(glassPanel);

		frame.add(hit, BorderLayout.SOUTH);
		hit.setVisible(false);
		// Set up the menu bar, which appears above the content pane.
		JMenu menu = new JMenu("Menu");
		pause = new JMenuItem("Pause");
		pause.setVisible(false);
		newSession10 = new JMenuItem("Neue Sitzung 10 min");
		newSession20 = new JMenuItem("Neue Sitzung 20 min");
		newSession30 = new JMenuItem("Neue Sitzung 30 min");
		newSession40 = new JMenuItem("Neue Sitzung 40 min");
		endSession = new JMenuItem("Sitzung beenden");
		endSession.setVisible(false);
		pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.togglePause(hit);
			}
		});
		newSession10.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.start(10);
				hit.setVisible(true);
				hit.requestFocus();
				hit.doClick();
				hideNewSession();
			}
		});
		newSession20.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.start(20);
				hit.setVisible(true);
				hit.requestFocus();
				hideNewSession();
			}
		});
		newSession30.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.start(30);
				hit.setVisible(true);
				hit.requestFocus();
				hideNewSession();
			}
		});
		newSession40.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.start(40);
				hit.setVisible(true);
				hit.requestFocus();
				hideNewSession();
			}
		});
		endSession.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				glassPanel.stop();
				hit.setVisible(false);
				showResults();
				pause.setVisible(false);
				endSession.setVisible(false);
				viewNewSession();
			}

			private void showResults() {
				renderResult(glassPanel.getHitList(), frame.getSize());
			}

		});
		menu.add(pause);
		menu.add(newSession10);
		menu.add(newSession20);
		menu.add(newSession30);
		menu.add(newSession40);
		menu.add(endSession);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		menuBar.setVisible(true);

		// Show the window.
		// frame.pack();
		frame.setVisible(true);

	}

	private void hideNewSession() {
		newSession10.setVisible(false);
		newSession20.setVisible(false);
		newSession30.setVisible(false);
		newSession40.setVisible(false);
		pause.setVisible(true);
		endSession.setVisible(true);
	}

	private void viewNewSession() {
		newSession10.setVisible(true);
		newSession20.setVisible(true);
		newSession30.setVisible(true);
		newSession40.setVisible(true);
	}

	public void renderResult(List<VrtPoint> hitList, Dimension d) {
		JFrame resultFrame = new JFrame("VrtResults");
		resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		resultFrame.setLayout(new FlowLayout());
		resultFrame.setSize(d);
		resultFrame.getContentPane().setLayout(new BorderLayout());
		VrtResultPane vrtResult = new VrtResultPane(hitList, d);
		resultFrame.getContentPane().add(vrtResult, BorderLayout.CENTER);

		resultFrame.setVisible(true);
		File image = vrtResult.save();
		try {
			VrtReportInfo info = new VrtReportInfo();
			info.setHitList(glassPanel.getHitList());
			info.setImage(image);
			info.setDim(new Dimension(600, 400));
			info.setFaults(glassPanel.getFaults());
			LabelFactory.getInstance().saveJPEG(info);

			VrtReport.savePdf(info);
			info.cleanUp();
		} catch (Exception e) {
			System.out.println("Cannot save pdf Report");
			e.printStackTrace();
		}

	}
}
