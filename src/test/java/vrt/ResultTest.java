package vrt;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.junit.Test;

import vrt.ui.VrtPoint;
import vrt.ui.VrtResultPane;

import junit.framework.TestCase;

public class ResultTest extends TestCase {

	@Test
	public void testResultFrame() throws Exception {
		JFrame resultFrame = new JFrame();
		resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		resultFrame.setLayout(new FlowLayout());
		resultFrame.setSize(new Dimension(600, 800));
		List<VrtPoint> hitlist = new ArrayList<VrtPoint>();
		VrtResultPane vrtResultPane = new VrtResultPane(hitlist, new Dimension(
				600, 800));
		resultFrame.getContentPane().setLayout(new FlowLayout());
		resultFrame.getContentPane().add(vrtResultPane);

		resultFrame.setVisible(true);
		vrtResultPane.setVisible(true);

		Thread.sleep(2000);
	}

}
