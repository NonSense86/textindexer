package at.tuwien.ir.textindexer.weighting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.tuwien.ir.textindexer.utils.IndexCount;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

abstract class AbstractWeightingStrategy implements WeightingStrategy {

	protected String filePrefix;
	
	public void generateOutput(String dir) {
		
	}
	
	public void generateOutput(Map<String, IndexCount> input, String dir) {
		
	}
	
	
	private FastVector createFeatureVector(Map<String, IndexCount> input) {
		FastVector fv = new FastVector();
		Attribute word = new Attribute("word", (FastVector)null);
		fv.addElement(word);
		
		List<String> classes = null;
		fv.addElement(new Attribute("class", createClassAttribute(classes)));
		
		List<String> docs = null;
		for(String s : docs) {
			fv.addElement(new Attribute(s, (FastVector)null));
		}
		return fv;
		
	}
	
	
	private FastVector createClassAttribute(List<String> classnames) {
		FastVector fv = new FastVector();
		for(String s : classnames)
			fv.addElement(s);
		return fv;
	}
	
	private void writeOutput(Instances dataSet, String filename) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
}
