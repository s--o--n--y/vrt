package vrt.pdf;

import java.awt.Dimension;
import java.io.File;
import java.util.List;

import vrt.ui.VrtPoint;

public class VrtReportInfo {

	private File image;
	private File imageMin;
	private List<VrtPoint> hitList;
	private Dimension dim;
	private int faults;

	public int getFaults() {
		return faults;
	}

	public void setFaults(int faults) {
		this.faults = faults;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public List<VrtPoint> getHitList() {
		return hitList;
	}

	public void setHitList(List<VrtPoint> hitList) {
		this.hitList = hitList;
	}

	public Dimension getDim() {
		return dim;
	}

	public void setDim(Dimension dim) {
		this.dim = dim;
	}

	public File getImageMin() {
		return imageMin;
	}

	public void setImageMin(File imageMin) {
		this.imageMin = imageMin;
	}

	public void cleanUp() {
		if (image != null) {
			image.delete();
		}
		if (imageMin != null) {
			imageMin.delete();
		}
	}

}
