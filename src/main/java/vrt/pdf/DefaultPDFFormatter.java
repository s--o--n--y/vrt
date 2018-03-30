package vrt.pdf;

import java.awt.Color;

import org.apache.log4j.Logger;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;

/**
 * Default implementation of CvFormatter
 * 
 * @author sony
 * 
 */
public class DefaultPDFFormatter extends AbstractPdfFormatter {

	Logger log = Logger.getLogger(DefaultPDFFormatter.class);

	private static final long serialVersionUID = -9035217671763782415L;

	private static Font headerBold = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
	private static Font headerBoldBlue = new Font(Font.TIMES_ROMAN, 14,
			Font.BOLD, Color.BLUE);
	private static Font body = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
	private static Font bodyBold = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
	private static final String DOUBLEDOT = ":";
	private static final String SPACE = " ";
	private static final String COMMA = ",";
	private Cell[] cells;

	/**
	 * 
	 */
	public DefaultPDFFormatter() {
		super();
	}

	/**
	 * 
	 * @param individual
	 * @param profile
	 */
	public DefaultPDFFormatter(byte[] template) {
		super(template);
		// super.setLogger(log);
		System.out.println("DefaultCvFormatter()");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addContent(Document document) {
		try {

			addIndividualData(document);
			addSummary(document);
			document.newPage();
			addProjectData(document);
			document.newPage();
			addEduData(document);
			if (isCompetences()) {
				document.newPage();
				addCompetences(document);
			}
		} catch (BadElementException e) {
			log.error(e);
		} catch (DocumentException e) {
			log.error(e);
		}
	}

	private void addSummary(Document document) throws DocumentException {
		insertHeader(rb.getString("cv.header.summary"), document);
		addEmptyParagraph(document);

		// for (Summary sum : this.individual.getSummaries()) {
		// if (sum.getProfile().equals(this.profile)) {
		// document.add(new Paragraph(sum.getContent(),
		// DefaultPDFCvFormatter.body));
		// System.out.println(sum.getContent());
		// }
		// }
	}

	private void addCompetences(Document document) throws DocumentException {
		Table t = prepareTableCompetences();
		insertHeader(rb.getString("cv.header.competences"), document);
		addEmptyParagraph(document);

		// Map<String, List<String[]>> skills = new TreeMap<String,
		// List<String[]>>();
		// for (Competence c : this.individual.getCompetences()) {
		// if (c.getVisible()) {
		// for (Competencemt cmt : c.getCompetencemts()) {
		// if (cmt.getProfile().equals(this.profile)) {
		// if (!skills.containsKey(cmt.getSkill()
		// .getSkillclassifier().getName())) {
		// skills.put(cmt.getSkill().getSkillclassifier()
		// .getName(), new ArrayList<String[]>());
		// }
		// String[] content = new String[2];
		// content[0] = cmt.getSkill().getName();
		// content[1] = cmt.getScore().toString();
		// skills.get(
		// cmt.getSkill().getSkillclassifier().getName())
		// .add(content);
		// }
		// }
		//
		// insertDataHeaderCellsCompetence(rb
		// .getString("cv.competence.category"), rb
		// .getString("cv.competence.skill"), rb
		// .getString("cv.competence.score"), t);
		// for (String key : skills.keySet()) {
		// insertDataCellsCompetence(key, "", "", t);
		// for (String[] buf : skills.get(key)) {
		// insertDataCellsCompetence("", buf[0], buf[1], t);
		// }
		// }
		// }
		// }
		addTableToDocument(document, t);

	}

	private void insertDataHeaderCellsCompetence(String title, String skill,
			String score, Table t) throws BadElementException {
		this.cells = new Cell[3];
		Paragraph p = new Paragraph(title, DefaultPDFFormatter.bodyBold);
		cells[0] = new Cell(p);
		p = new Paragraph(skill, DefaultPDFFormatter.body);
		cells[1] = new Cell(p);
		p = new Paragraph(score, DefaultPDFFormatter.body);
		cells[2] = new Cell(p);
		// cells[0].setBorder(Cell.BOTTOM);
		// cells[1].setBorder(Cell.BOTTOM);
		// cells[2].setBorder(Cell.BOTTOM);
		cells[0].setGrayFill(0.95f);
		cells[1].setGrayFill(0.95f);
		cells[2].setGrayFill(0.95f);
		cells[0].normalize();
		cells[1].normalize();
		cells[2].normalize();
		t.addCell(cells[0]);
		t.addCell(cells[1]);
		t.addCell(cells[2]);

	}

	private Table prepareTableCompetences() throws BadElementException {
		Table t = new Table(3, 2);
		// t.setBorderColor(Color.WHITE);
		t.setPadding(1);
		t.setSpacing(0);
		t.setBorderWidth(0);
		t.setWidth(100);
		try {
			t.setWidths(new int[] { 20, 40, 40 });
		} catch (DocumentException e) {
			log.error(e);
		}
		return t;
	}

	private boolean isCompetences() {
		// for (Competence c : this.individual.getCompetences()) {
		// if (c.getVisible()) {
		// return true;
		// }
		// }
		return false;
	}

	/**
	 * adds all personal data to a document
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	private void addIndividualData(Document document) throws DocumentException {
		//
		// Table t = prepareTable();
		//
		// insertHeader(rb.getString("cv.header.personal.data"), document);
		// addEmptyParagraph(document);
		//
		// insertDataCells(rb.getString("cv.personal.name"), this.individual
		// .getTitle()
		// + SPACE
		// + this.individual.getFName()
		// + SPACE
		// + this.individual.getLName(), t);
		// insertDataCells(rb.getString("cv.personal.address"), this.individual
		// .getStreet(), t);
		// insertDataCells("", this.individual.getZip().toString() + SPACE
		// + this.individual.getCity(), t);
		// insertDataCells(rb.getString("cv.personal.dob"), sdfLong
		// .format(this.individual.getDob()), t);
		// insertDataCells(rb.getString("cv.personal.email"), this.individual
		// .getEmail(), t);
		//
		// addTableToDocument(document, t);

	}

	/**
	 * adds all educational data to a document
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	private void addEduData(Document document) throws DocumentException {
		Table t = prepareTable();

		insertHeader(rb.getString("cv.header.edu"), document);

		addEmptyParagraph(document);

		// for (Edu edu : this.individual.getEdus()) {
		// // find the correct Profile
		// for (Eduprofile ep : edu.getEduprofiles()) {
		// if (ep.getProfile().equals(this.profile)) {
		// // should this edu be visible?
		// if (ep.getVisible()) {
		// // check if an eduName is set on profile - if yes -
		// // override
		// if (ep.getEduName() != null && ep.getEduName() != "") {
		// insertDataCells(rb.getString("cv.edu.edu.name"), ep
		// .getEduName(), t);
		// } else {
		// insertDataCells(rb.getString("cv.edu.edu.name"),
		// edu.getName(), t);
		// }
		//
		// insertDataCells(rb.getString("cv.edu.institute"), ep
		// .getInstitute().getName(), t);
		// insertDataCells(rb.getString("cv.edu.graduation"), ep
		// .getGraduation().getTitle(), t);
		// insertDataCells(rb.getString("cv.edu.date"), sdfShort
		// .format(edu.getDfrom())
		// + SPACE
		// + rb.getString("cv.string.to")
		// + SPACE
		// + sdfShort.format(edu.getDto()), t);
		// insertDataCells(rb.getString("cv.edu.details"), ep
		// .getContent(), t);
		// insertDataCellsNoColor("", "", t);
		// }
		// }
		// }
		// }
		//
		// addTableToDocument(document, t);

	}

	/**
	 * adds all project data to a document
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	private void addProjectData(Document document) throws DocumentException {
		Table t = prepareTable();

		insertHeader(rb.getString("cv.header.project"), document);

		addEmptyParagraph(document);

		// for (Project project : this.individual.getProjects()) {
		// // find the correct Profile
		// for (Projectprofile pp : project.getProjectprofiles()) {
		// if (pp.getProfile().equals(this.profile)) {
		// // should this Project be visible?
		// if (pp.getVisible()) {
		// insertDataCells(rb.getString("cv.project.date"),
		// sdfShort.format(project.getDfrom()) + SPACE
		// + rb.getString("cv.string.to") + SPACE
		// + sdfShort.format(project.getDto()), t);
		// insertDataCells(rb.getString("cv.project.client"),
		// project.getClient().getName(), t);
		// insertDataCells(rb.getString("cv.project.industry"), pp
		// .getIndustry().getName(), t);
		//
		// // check if an eduName is set on profile - if yes -
		// // override
		// if (pp.getProjectName() != null
		// && pp.getProjectName() != "") {
		// insertDataCells(rb.getString("cv.project.name"), pp
		// .getProjectName(), t);
		// } else {
		// insertDataCells(rb.getString("cv.project.name"),
		// project.getName(), t);
		// }
		//
		// // fields of activity
		// StringBuffer sb = new StringBuffer();
		// for (Foamtproject foamt : pp.getFoamtprojects()) {
		// sb.append(foamt.getFoa().getName());
		// sb.append(COMMA);
		// sb.append(SPACE);
		// }
		// // strip the last comma
		// if (sb.length() != 0) {
		// sb.replace((sb.length() - (COMMA.length() + SPACE
		// .length())), sb.length(), "");
		// insertDataCells(rb.getString("cv.project.foa"), sb
		// .toString(), t);
		// }
		// insertDataCells(rb.getString("cv.project.details"), pp
		// .getContent(), t);
		// insertDataCellsNoColor("", "", t);
		// }
		// }
		// }
		// }
		// addTableToDocument(document, t);

	}

	/**
	 * prepares a table body for data
	 * 
	 * @return
	 * @throws BadElementException
	 */
	private Table prepareTable() throws BadElementException {
		Table t = new Table(2, 2);
		// t.setBorderColor(Color.WHITE);
		t.setPadding(1);
		t.setSpacing(0);
		t.setBorderWidth(0);
		t.setWidth(100);
		try {
			t.setWidths(new int[] { 20, 80 });
		} catch (DocumentException e) {
			log.error(e);
		}
		return t;

	}

	/**
	 * inserts data to cells on a table
	 * 
	 * @param title
	 * @param content
	 * @param t
	 * @throws BadElementException
	 */
	private void insertDataCells(String title, String content, Table t)
			throws BadElementException {
		this.cells = new Cell[2];
		Paragraph p = new Paragraph(title, DefaultPDFFormatter.bodyBold);
		cells[0] = new Cell(p);
		p = new Paragraph(content, DefaultPDFFormatter.body);
		cells[1] = new Cell(p);
		cells[0].setBorder(0);
		cells[1].setBorder(0);
		cells[0].setGrayFill(0.95f);
		cells[0].normalize();
		cells[1].normalize();
		t.addCell(cells[0]);
		t.addCell(cells[1]);
	}

	private void insertDataCellsNoColor(String title, String content, Table t)
			throws BadElementException {
		this.cells = new Cell[2];
		Paragraph p = new Paragraph(title, DefaultPDFFormatter.bodyBold);
		cells[0] = new Cell(p);
		p = new Paragraph(content, DefaultPDFFormatter.body);
		cells[1] = new Cell(p);
		cells[0].setBorder(0);
		cells[1].setBorder(0);
		cells[0].normalize();
		cells[1].normalize();
		t.addCell(cells[0]);
		t.addCell(cells[1]);
	}

	private void insertDataCellsCompetence(String title, String skill,
			String score, Table t) throws BadElementException {
		this.cells = new Cell[3];
		Paragraph p = new Paragraph(title, DefaultPDFFormatter.bodyBold);
		cells[0] = new Cell(p);
		p = new Paragraph(skill, DefaultPDFFormatter.body);
		cells[1] = new Cell(p);
		p = new Paragraph(score, DefaultPDFFormatter.body);
		cells[2] = new Cell(p);
		cells[0].setBorder(0);
		cells[1].setBorder(0);
		cells[2].setBorder(0);
		cells[0].setGrayFill(0.95f);
		cells[0].normalize();
		cells[1].normalize();
		cells[2].normalize();
		t.addCell(cells[0]);
		t.addCell(cells[1]);
		t.addCell(cells[2]);
	}

	/**
	 * inserts a headline to a document
	 * 
	 * @param title
	 * @param document
	 * @throws BadElementException
	 */
	private void insertHeader(String title, Document document)
			throws BadElementException {
		Table t = new Table(1, 1);
		// t.setBorderColor(Color.WHITE);
		t.setPadding(2);
		t.setSpacing(0);
		t.setBorderWidth(0);
		t.setWidth(100);

		Paragraph p = new Paragraph(title, DefaultPDFFormatter.headerBold);
		p.setAlignment(Paragraph.ALIGN_TOP);
		Cell c1 = new Cell(p);

		c1.setHeader(true);
		// c1.setBorder(Cell.BOX);
		c1.setBorder(Cell.NO_BORDER);
		// c1.setBorderWidth(2);
		// c1.setBorderColor(Color.black);
		// c1.setBackgroundColor(new Color(255,255,100));
		// c1.setVerticalAlignment(Cell.ALIGN_TOP);
		c1.setHorizontalAlignment(Cell.ALIGN_CENTER);

		t.addCell(c1);
		t.endHeaders();
		addTableToDocument(document, t);
	}

	/**
	 * sets the logger for abstract methods
	 */
	@Override
	public void setLogger() {
		super.setLogger(log);
	}
}
