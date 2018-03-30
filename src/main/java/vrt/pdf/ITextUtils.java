package vrt.pdf;

/*

 watermark_pdf, version 1.0

 http://www.pdfhacks.com/watermark/



 place a single "watermark" PDF page "underneath" all

 pages of the input document



 after compiling, invoke from the command line like this:



 java -classpath ./itext-paulo.jar:. \

 watermark_pdf doc.pdf watermark.pdf output.pdf



 only the first page of watermark.pdf is used

 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import vrt.exception.VrtResultException;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * Singleton used as toolbox for IText
 * 
 * @author sony
 * 
 */
public class ITextUtils {

	private static ITextUtils instance = null;

	/**
	 * private constructor
	 */
	private ITextUtils() {

	}

	/**
	 * get the instance
	 * 
	 * @return the instance
	 */
	public static ITextUtils getInstance() {
		if (instance == null) {
			instance = new ITextUtils();
		}
		return instance;
	}

	/**
	 * overlay to Files
	 * 
	 * @param overContent
	 *            the content over
	 * @param underContent
	 *            the template
	 * @param output
	 *            the file to write to
	 * @throws VrtResultException
	 */
	public void overlayPdfs(File overContent, File underContent, File output)
			throws VrtResultException {
		try {
			overlayPdfs(createByteArrayFromFile(overContent),
					createByteArrayFromFile(underContent),
					new FileOutputStream(output));
		} catch (IOException e) {
			e.printStackTrace();
			throw new VrtResultException(e);
		}
	}

	/**
	 * 
	 * @param overContent
	 * @param underContent
	 * @param outputStream
	 * @throws VrtResultException
	 */
	public void overlayPdfs(File overContent, File underContent,
			OutputStream outputStream) throws VrtResultException {
		try {
			overlayPdfs(createByteArrayFromFile(overContent),
					createByteArrayFromFile(underContent), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new VrtResultException(e);
		}
	}

	/**
	 * 
	 * @param overContent
	 * @param underContent
	 * @param outputStream
	 * @throws VrtResultException
	 */
	public void overlayPdfs(File overContent, byte[] underContent,
			OutputStream outputStream) throws VrtResultException {
		try {
			overlayPdfs(createByteArrayFromFile(overContent), underContent,
					outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new VrtResultException(e);
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private byte[] createByteArrayFromFile(File file)
			throws FileNotFoundException, IOException {
		byte[] ba = new byte[(int) file.length()];
		new FileInputStream(file).read(ba);
		return ba;
	}

	/**
	 * overlay two pdf in byte[] format
	 * 
	 * @param overContent
	 *            the overContent
	 * @param underContent
	 *            the template
	 * @param output
	 *            the file
	 */
	public void overlayPdfs(byte[] overContent, byte[] underContent,
			OutputStream outputStream) throws VrtResultException {

		try {

			// the document we're watermarking
			PdfReader document = new PdfReader(overContent);
			int num_pages = document.getNumberOfPages();

			// the watermark (or letterhead, etc.)
			PdfReader mark = new PdfReader(underContent);
			Rectangle mark_page_size = mark.getPageSize(1);

			// the output document
			PdfStamper writer = new PdfStamper(document, outputStream);

			// create a PdfTemplate from the first page of mark
			// (PdfImportedPage is derived from PdfTemplate)
			PdfImportedPage mark_page = writer.getImportedPage(mark, 1);
			for (int ii = 0; ii < num_pages;) {

				// iterate over document's pages, adding mark_page as
				// a layer 'underneath' the page content; scale mark_page
				// and move it so that it fits within the document's page;
				// if document's page is cropped, this scale might
				// not be small enough

				++ii;
				Rectangle doc_page_size = document.getPageSize(ii);
				float h_scale = doc_page_size.getWidth()
						/ mark_page_size.getWidth();
				float v_scale = doc_page_size.getHeight()
						/ mark_page_size.getHeight();
				float mark_scale = h_scale < v_scale ? h_scale : v_scale;
				float h_trans = (float) ((doc_page_size.getWidth() - mark_page_size
						.getWidth()
						* mark_scale) / 2.0);
				float v_trans = (float) ((doc_page_size.getHeight() - mark_page_size
						.getHeight()
						* mark_scale) / 2.0);
				PdfContentByte contentByte = writer.getUnderContent(ii);
				contentByte.addTemplate(mark_page, mark_scale, 0, 0,
						mark_scale, h_trans, v_trans);
			}

			writer.close();

		} catch (Exception ee) {
			ee.printStackTrace();
			throw new VrtResultException("error overlaying pdfs!", ee);
		}
	}
}