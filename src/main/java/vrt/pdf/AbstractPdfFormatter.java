package vrt.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import vrt.exception.VrtResultException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class represents the base class for all iText formatter classes
 * 
 * @author sony
 * 
 */

public abstract class AbstractPdfFormatter implements Serializable {

	private File out;
	private File outMerged;

	/**
	 * 
	 */
	private static final long serialVersionUID = 9173014138042991117L;
	protected ResourceBundle rb;
	private Logger logger = Logger.getLogger(AbstractPdfFormatter.class);
	protected byte[] template;
	/** dd MMM YYYY used for DOB */
	protected SimpleDateFormat sdfLong = new SimpleDateFormat("dd MMM yyyy");
	/** dd-MM-YYYY used for Edus & Projects */
	protected SimpleDateFormat sdfShort = new SimpleDateFormat("dd-MM-yyyy");

	public AbstractPdfFormatter() {
	}

	/**
	 * constructor passing the individual to create a cv for
	 * 
	 * @param individual
	 */
	public AbstractPdfFormatter(byte[] template) {
		this.template = template;
	}

	/**
	 * main template method to create a cv
	 */
	public byte[] createCv() throws VrtResultException {
		try {
			// check if everything is fine
			init();

			// start the creation
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(out));
			document.setMargins(60, 40, 100, 100);
			document.open();
			addMetaData(document);
			addTitlePage(document);
			addContent(document);
			document.close();

			// merge content and template
			ITextUtils.getInstance().overlayPdfs(out, template,
					new FileOutputStream(outMerged));
			byte[] b = new byte[(int) outMerged.length()];
			new FileInputStream(outMerged).read(b);

			// cleanup
			out.delete();
			outMerged.delete();

			return b;
		} catch (Exception e) {
			throw new VrtResultException("unable to create cv", e);
		}

	}

	private void init() throws VrtResultException {

		rb = ResourceBundle.getBundle("cv",
				new Locale(Locale.GERMAN.toString()));
		// init the temp files
		out = new File("out");
		outMerged = new File("outMerged");
	}

	public abstract void setLogger();

	/**
	 * adds main content to a document
	 * 
	 * @param document
	 */
	protected abstract void addContent(Document document);

	/**
	 * adds a title page to a document
	 * 
	 * @param document
	 */
	protected void addTitlePage(Document document) {

	}

	/**
	 * adds methadata to a document
	 * 
	 * @param document
	 */
	protected void addMetaData(Document document) {

	}

	/**
	 * adds a table to a document
	 * 
	 * @param document
	 * @param t
	 */
	protected void addTableToDocument(Document document, Table t) {
		try {
			document.add(t);
		} catch (DocumentException e) {
			logger.error(e);
		}
	}

	/**
	 * adds an empty paragraph to a document
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	protected void addEmptyParagraph(Document document)
			throws DocumentException {
		Paragraph p = new Paragraph();
		addEmptyLine(p, 1);
		document.add(p);
	}

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

	protected void setLogger(Logger log) {
		this.logger = log;

	}

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

}
