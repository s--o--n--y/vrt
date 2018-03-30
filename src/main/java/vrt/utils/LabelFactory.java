package vrt.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import vrt.pdf.VrtReportInfo;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class LabelFactory {

	private static LabelFactory labelFactory;
	private static GraphicsConfiguration gc = null;
	private static Logger log = Logger.getLogger(LabelFactory.class);

	private LabelFactory() {

	}

	public static LabelFactory getInstance() {
		if (labelFactory == null) {
			labelFactory = new LabelFactory();
		}
		return labelFactory;
	}

	// public ScrollablePicture loadLabel(URL url){
	// try{
	// return new ScrollablePicture(getImageIcon(url));
	// }catch(Exception e){
	// log.error("could not load resource", e);
	// }
	// return null;
	// }

	public BufferedImage loadBufferedImage(URL url) {
		try {
			return toCompatibleImage(ImageIO.read(url),
					getDefaultConfiguration());
		} catch (IOException e) {
			log.error("could not find file", e);
			e.printStackTrace();
		}
		return null;
	}

	public ImageIcon getImageIcon(URL url, double scale) {
		BufferedImage image = LabelFactory.getInstance().loadBufferedImage(url);
		ImageIcon icon = null;
		int w = (int) (scale / 100 * image.getWidth());
		int h = (int) (scale / 100 * image.getHeight());
		if (scale == 100) {
			icon = new ImageIcon(url);
		} else if (scale > 100) {
			icon = new ImageIcon(getScaledInstance(image, w, h, gc));
		} else if (scale < 100) {
			;
			// final BufferedImage resize = getScaledInstanceAWT(image,
			// this.scale, Image.SCALE_AREA_AVERAGING);
			icon = new ImageIcon(image.getScaledInstance(w, h,
					Image.SCALE_AREA_AVERAGING));
		}
		return icon;
	}

	public BufferedImage getScaledBufferedImage(URL url, double scale) {
		BufferedImage image = LabelFactory.getInstance().loadBufferedImage(url);
		int w = (int) (scale / 100 * image.getWidth());
		int h = (int) (scale / 100 * image.getHeight());
		if (scale == 100) {
			return LabelFactory.getInstance().loadBufferedImage(url);
		} else if (scale > 100) {
			return getScaledInstance(image, w, h, gc);
		} else if (scale < 100) {
			;
			// final BufferedImage resize = getScaledInstanceAWT(image,
			// this.scale, Image.SCALE_AREA_AVERAGING);
			return (BufferedImage) image.getScaledInstance(w, h,
					Image.SCALE_AREA_AVERAGING);
		}
		return null;
	}

	public BufferedImage getScaledBufferedImage(URL url, int w, int h) {
		BufferedImage image = LabelFactory.getInstance().loadBufferedImage(url);
		if (image.getWidth() == w && image.getHeight() == h) {
			return LabelFactory.getInstance().loadBufferedImage(url);
		} else if (image.getWidth() > w) {
			return getScaledInstance(image, w, h, gc);
		} else if (image.getWidth() > w) {
			;
			// final BufferedImage resize = getScaledInstanceAWT(image,
			// this.scale, Image.SCALE_AREA_AVERAGING);
			return (BufferedImage) image.getScaledInstance(w, h,
					Image.SCALE_AREA_AVERAGING);
		}
		return null;
	}

	public static GraphicsConfiguration getDefaultConfiguration() {
		if (gc == null) {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			gc = gd.getDefaultConfiguration();
		}
		return gc;
	}

	public static BufferedImage getScaledInstance(BufferedImage image,
			int width, int height, GraphicsConfiguration gc) {
		if (gc == null) {
			gc = getDefaultConfiguration();
		}
		int transparency = image.getColorModel().getTransparency();
		return copy(image, gc
				.createCompatibleImage(width, height, transparency));
	}

	public static Image getScaledInstanceAWT(BufferedImage source,
			double scale, int hint) {
		int w = (int) (source.getWidth() * scale);
		int h = (int) (source.getHeight() * scale);

		return source.getScaledInstance(w, h, hint);
	}

	public static BufferedImage toCompatibleImage(BufferedImage image,
			GraphicsConfiguration gc) {
		if (gc == null) {
			gc = getDefaultConfiguration();
		}
		int w = image.getWidth();
		int h = image.getHeight();
		int transparency = image.getColorModel().getTransparency();
		BufferedImage result = gc.createCompatibleImage(w, h, transparency);
		Graphics2D g2 = result.createGraphics();
		g2.drawRenderedImage(image, null);
		g2.dispose();
		return result;
	}

	// returns target
	public static BufferedImage copy(BufferedImage source, BufferedImage target) {
		Graphics2D g2 = target.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		double scalex = (double) target.getWidth() / source.getWidth();
		double scaley = (double) target.getHeight() / source.getHeight();
		AffineTransform xform = AffineTransform
				.getScaleInstance(scalex, scaley);
		g2.drawRenderedImage(source, xform);
		g2.dispose();
		return target;
	}

	public VrtReportInfo saveJPEG(VrtReportInfo info) {
		try {
			int width = (int) info.getDim().getWidth();
			int height = (int) info.getDim().getHeight();
			BufferedImage bi = getScaledBufferedImage(info.getImage().toURL(),
					width, height);

			// convert the image to a BufferedImage

			Graphics g = bi.getGraphics();
			g.drawImage(bi, 0, 0, width, height, null);
			g.dispose();

			// Overlay current time on top of the image
			g.setColor(Color.RED);
			g.setFont(new Font("Helvetica", Font.BOLD, 12));
			g.drawString("timeNow()", 5, 14);

			boolean saved = false;
			FileOutputStream out = null;

			out = new FileOutputStream(info.getImage().getAbsolutePath()
					.replace(".jpg", "_min.jpg"));

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1.0f, false); // 100% high quality setting, no
			// compression
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bi);
			out.close();
			saved = true;
			info.setImageMin(new File(info.getImage().getAbsolutePath()
					.replace(".jpg", "_min.jpg")));
			return info;
		} catch (Exception ex) {
			System.out.println("Error saving JPEG : " + ex.getMessage());
		}
		return info;
	}
}
