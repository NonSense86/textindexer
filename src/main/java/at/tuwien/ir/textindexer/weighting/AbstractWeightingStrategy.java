package at.tuwien.ir.textindexer.weighting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
	private FastVector<Attribute> createFeatureVector(Map<String, IndexCount> input) {
		FastVector<Attribute> fv = new FastVector<Attribute>();
		Attribute word = new Attribute("word", (FastVector)null);
		
		return fv;
		
	}
	
	private void writeOutput(Instances dataSet, String filename) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
}
