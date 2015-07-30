/**
 * This sample code is made available as part of the book "Digital Image
 * Processing - An Algorithmic Introduction using Java" by Wilhelm Burger
 * and Mark J. Burge, Copyright (C) 2005-2008 Springer-Verlag Berlin, 
 * Heidelberg, New York.
 * Note that this code comes with absolutely no warranty of any kind.
 * See http://www.imagingbook.com for details and licensing conditions.
 * 
 * Date: 2007/11/10
 */

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import morphology.BinMorpher;
import morphology.BinMorpher.Operation;


/** This plugin implements a binary morphology filter using an arbitrary
 * structuring element.
 * Version 2 (requires Java 5)
*/

public class Filter_BinMorph_Free implements PlugInFilter {
	
	static final int size = 9;
	static boolean[] freefilter = new boolean[size*size];
	static boolean showfilter = false;
	static String  opstring = null;
	
	static { // initializer: mark hot spot when loaded
		freefilter[(size*size)/2]= true;
	}

    public int setup(String arg, ImagePlus imp) {
        if (arg.equals("about")) {
            showAbout();
            return DONE;
        }
        return DOES_8G;
    }

    public void run(ImageProcessor orig) {
		if (showDialog()) { //sets FILTER and OP			
			BinMorpher m = new BinMorpher(makeFilter());
			Operation op = Operation.valueOf(opstring);
			m.apply(orig,op);
			if (showfilter)
				m.showFilter();
		}
    }

    void showAbout() {
        String cn = getClass().getName();
        IJ.showMessage("About "+cn+" ...",
            "Binary dilation for arbitrary structuring elements."
        );
    }
    
    
	boolean showDialog() {
		GenericDialog gd = new GenericDialog("Structuring Element");
		String[] labels = makeFilterLabels();
		
		gd.addCheckboxGroup(size,size,labels,freefilter);
		
		String[] ops = BinMorpher.getOpNames();
		gd.addChoice("Operation", ops, ops[0]);
		
		gd.addCheckbox("Display filter", false);
		
		gd.showDialog();
		if (gd.wasCanceled()) 
			return false;
		else {
			for (int i=0; i<size*size; i++){
				freefilter[i] = gd.getNextBoolean();
			}
			showfilter = gd.getNextBoolean();
			opstring = gd.getNextChoice();
			return true;
		}
	}
	
	int[][] makeFilter(){
		int[][] filter = new int[size][size];
		int i=0;
		for (int v=0;v<size; v++){
			for (int u=0;u<size; u++){
				if (freefilter[i])
					filter[v][u] = 1;
				else
					filter[v][u] = 0;
				i++;
			}
		}
		return filter;
	}
	
	String[] makeFilterLabels(){
		String[] labels = new String[size*size];	
		for (int i=0; i<labels.length; i++){
			labels[i] = "";
		}
		labels[(size*size)/2] = "0";
		return labels;
	}

}







