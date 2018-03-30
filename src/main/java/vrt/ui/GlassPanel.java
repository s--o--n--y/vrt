package vrt.ui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import vrt.ui.callback.StopCallback;

public class GlassPanel extends JComponent implements ActionListener {

	private ComponentAdapter shapeChangeListener;
	private final JComponent itemToCover;
	private long eventsToIntercept;
	private volatile boolean vrtRunning;
	private Thread t;
	Point point;
	private List<VrtPoint> hitList = new ArrayList<VrtPoint>();
	private VrtPoint currentPoint;
	private volatile boolean vrtPaused = false;
	private final StopCallback stopCallback;

	public GlassPanel(JComponent coveredArea, long eventsToIntercept,
			StopCallback stopCallback) {
		setOpaque(false);
		super.setVisible(false);
		this.stopCallback = stopCallback;
		itemToCover = coveredArea;
		this.eventsToIntercept = eventsToIntercept;
		// use the layered pane to show it above the normal controls...
		JLayeredPane layer = SwingUtilities.getRootPane(itemToCover)
				.getLayeredPane();

		layer.add(this, new Integer(layer.getLayer(itemToCover) + 1));
		// we must resize when the item we are "covering" resizes.
		createShapeChangeHandler();
	}

	// when the panel to cover is resized,
	// we must also resize to keep covering it.
	private void createShapeChangeHandler() {
		shapeChangeListener = new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				updateSize();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				updateSize();
			}

			@Override
			public void componentShown(ComponentEvent e) {
				updateSize();
			}
		};
		itemToCover.addComponentListener(shapeChangeListener);
	}

	@Override
	public void setVisible(boolean visible) {
		// make sure this covers the entire area
		if (visible) {
			updateSize();
		}
		toggleInterceptedEvents(visible);
		super.setVisible(visible);
	}

	private void updateSize() {
		setBounds(itemToCover.getBounds());
	}

	private void toggleInterceptedEvents(boolean turnOn) {
		if (turnOn) {
			enableEvents(eventsToIntercept);
		} else {
			disableEvents(eventsToIntercept);
		}
	}

	// we are only designed to "intercept" input events because these can
	// be consumed "stopping" them from being sent to components below.
	@Override
	protected void processEvent(AWTEvent e) {
		super.processEvent(e);
		updateSize();
		if (isVisible() && (e.getID() & eventsToIntercept) != 0
				&& e instanceof InputEvent) {
			((InputEvent) e).consume();
		}
	}

	public long getInterceptedEvents() {
		return eventsToIntercept;
	}

	public void setInterceptedEvents(long newInterceptedEvents) {
		disableEvents(eventsToIntercept);
		eventsToIntercept = newInterceptedEvents;
		if (isVisible()) {
			enableEvents(newInterceptedEvents);
		}
	}

	private void hit() {
		if (isVisible()) {
			// handle hit click
			// System.out.println("hit");
			if (currentPoint != null) {
				currentPoint.setHit(true);
			}
			t.interrupt();
		} else {
			// System.out.println("fault");
		}
	}

	private void generatePoint() {
		if (currentPoint != null) {
			hitList.add(currentPoint);
		}
		generateRandomPoint();
		// generateBorderPoint();
		// generateBlackZonePoint();

	}

	private void generateBlackZonePoint() {
		// TODO Auto-generated method stub

	}

	private void generateBorderPoint() {
		// TODO Auto-generated method stub

	}

	private void generateRandomPoint() {
		int x = (int) (Math.random() * this.getWidth());
		int y = (int) (Math.random() * this.getHeight());
		Point p = new Point(x, y);
		currentPoint = new VrtPoint(p);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// if (point != null) {
		// g.setColor(Color.red);
		// g.fillOval(point.x - 10, point.y - 10, 20, 20);
		// }
		paintCurrentPoint(g);
	}

	private void paintCurrentPoint(Graphics g) {
		if (currentPoint != null && !vrtPaused) {
			currentPoint.paintSearchPoint(g);
		} else if (vrtPaused) {
			g.setColor(Color.red);
			g.fillRect(0, (int) (getSize().getHeight() / 2) + 8,
					(int) getSize().getWidth(),
					(int) getSize().getHeight() / 20);
		}
	}

	public void setPoint(Point p) {
		point = p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		hit();
	}

	public List<VrtPoint> getHitList() {
		return hitList;
	}

	public void setHitList(List<VrtPoint> hitList) {
		this.hitList = hitList;
	}

	public void start(final int min) {
		vrtRunning = true;
		hitList = new ArrayList<VrtPoint>();
		t = new Thread() {
			long currentTime = System.currentTimeMillis();
			long endTime = currentTime + min * 60 * 1000;
			private long cycleTime;

			@Override
			public void run() {
				while (vrtRunning) {
					currentTime = System.currentTimeMillis();
					if (currentTime < endTime && !vrtPaused) {

						try {
							// wait some time for next point!
							Thread.sleep(generateSleepTime());
							generatePoint();
							setVisible(true);
							// System.out.println("running");
							repaint();
							// wait for user to hit point
							Thread.sleep(1000);
							setVisible(false);
							repaint();
						} catch (InterruptedException e) {
							setVisible(false);
							repaint();
						}
					} else if (vrtPaused) {
						endTime += cycleTime;
						try {
							setVisible(true);
							repaint();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// silent
							repaint();
							continue;
						}
					}

					else {
						// VrtFrame.renderResult(hitList, getSize());
						vrtRunning = false;
						stopCallback.stopVrt();
					}
					cycleTime = System.currentTimeMillis() - currentTime;
				}
			}

			private long generateSleepTime() {
				int x = 0;
				while (x < 800) {
					x = (int) (Math.random() * 2500);
				}
				return x;
			}
		};
		t.start();

	}

	public void stop() {
		vrtRunning = false;

	}

	public void pause() {
		vrtPaused = true;
		t.interrupt();
	}

	public void togglePause() {
		vrtPaused = !vrtPaused;
		t.interrupt();
	}

	/**
	 * hides a button till pause
	 * 
	 * @param hit
	 */
	public void togglePause(JButton hit) {
		togglePause();
		if (vrtPaused) {
			hit.setVisible(false);
		} else {
			hit.setVisible(true);
		}

	}
}