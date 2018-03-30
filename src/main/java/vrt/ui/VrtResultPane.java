package vrt.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class VrtResultPane extends VrtBackground {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6217791089918682156L;
	private final List<VrtPoint> hitList;
	private final Dimension dimension;

	public VrtResultPane(List<VrtPoint> hitList, Dimension size) {
		super();
		this.hitList = hitList;
		this.dimension = size;
		this.setSize(dimension);
		this.setVisible(true);
		// save();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (VrtPoint p : hitList) {
			if (p.isHit()) {
				p.paintHitPoint(g);
			} else {
				p.paintMissedPoint(g);
			}
		}
	}

	public File save() {
		int w = getWidth();
		int h = getHeight();
		int type = BufferedImage.TYPE_INT_RGB; // see api for options
		BufferedImage bi = new BufferedImage(w, h, type);
		Graphics2D g2 = bi.createGraphics();
		paint(g2);
		g2.dispose();
		String ext = "jpg";
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_hh-mm");
		File file = new File("OpenVrt_Report_" + df.format(new Date()) + "."
				+ ext);
		try {
			ImageIO.write(bi, ext, file);
		} catch (IOException ioe) {
			System.out.println("Could not write image to disk!");
			ioe.printStackTrace();
		}
		return file;
	}
}
