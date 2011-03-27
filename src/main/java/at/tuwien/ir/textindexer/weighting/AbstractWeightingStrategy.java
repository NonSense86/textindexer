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
	
	/*
	private FastVector<Attribute> createFeatureVector(Map<String, IndexCount> input) {
		FastVector<Attribute> fv = new FastVector<Attribute>();
		Attribute word = new Attribute("word", (FastVector)null);
		fv.add(word);
		
		List<String> classes = null;
		fv.add(new Attribute("class", createClassAttribute(classes)));
		
		List<String> docs = null;
		for(String s : docs) {
			fv.add(new Attribute(s, (FastVector)null));
		}
		return fv;
		
	}
	*/
	
	private List<String> createClassAttribute(List<String> classnames) {
		List<String> fv = new ArrayList<String>();
		for(String s : classnames)
			fv.add(s);
		return fv;
	}
	
	private void writeOutput(Instances dataSet, String filename) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
}
