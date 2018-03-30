package vrt.pdf;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vrt.ui.VrtPoint;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class VrtReport {

	/**
	 * adds empty lines to a document
	 * 
	 * @param paragraph
	 * @param number
	 */
	protected static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public static void savePdf(VrtReportInfo info) throws Exception {

		Document document = new Document(PageSize.A4.rotate());

		PdfWriter.getInstance(document, new FileOutputStream(info.getImage()
				.getName().replaceAll(".jpg", ".pdf")));
		document.open();

		int hitCount = 0;
		int missedCount = 0;

		for (VrtPoint p : info.getHitList()) {
			if (p.isHit()) {
				hitCount++;
			} else {
				missedCount++;
			}
		}

		// Code 1

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		document.add(new Paragraph("OpenVrt Report - Sitzung vom "
				+ df.format(new Date())));
		addEmptyLine(new Paragraph(), 2);
		document.add(new Paragraph("Getroffene Punkte: " + hitCount + " von "
				+ info.getHitList().size()));
		document.add(new Paragraph("Nicht getroffene Punkte: " + missedCount));
		document.add(new Paragraph("Gedrückt ohne Punkt: " + info.getFaults()));
		if (info.getImageMin() != null) {
			com.lowagie.text.Image image = com.lowagie.text.Image
					.getInstance(info.getImageMin().getAbsolutePath());
			document.add(image);
		}

		// // Code 2
		// document.add(new Paragraph("\n" + "AWT Image"));
		// java.awt.Image awtImg = java.awt.Toolkit.getDefaultToolkit()
		// .createImage(info.getImage().getAbsolutePath());
		// com.lowagie.text.Image image2 = com.lowagie.text.Image.getInstance(
		// awtImg, null);
		// document.add(image2);
		// document.newPage();

		// // Code 3
		// document.add(new Paragraph("Multipages tiff file"));
		// RandomAccessFileOrArray ra = new RandomAccessFileOrArray(
		// "multipage.tif");
		// int pages = TiffImage.getNumberOfPages(ra);
		// for (int i = 1; i <= pages; i++) {
		// document.add(TiffImage.getTiffImage(ra, i));
		// }
		// document.newPage();

		// // Code 4
		// document.add(new Paragraph("Animated Gifs"));
		// GifImage img = new GifImage("bee.gif");
		// int frame_count = img.getFrameCount();
		// for (int i = 1; i <= frame_count; i++) {
		// document.add(img.getImage(i));
		// }
		document.close();

	}
}
