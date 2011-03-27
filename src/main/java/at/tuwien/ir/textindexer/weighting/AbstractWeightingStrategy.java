package at.tuwien.ir.textindexer.weighting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Text;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

abstract class AbstractWeightingStrategy implements WeightingStrategy {

	protected String filePrefix;
	protected IndexOutputCollector collector;
	
	private FastVector featureVector;
	
	public void generateOutput(String dir) throws IOException {
		featureVector = createFeatureVector();
		Instances dataSet = createDataSet(featureVector);
		writeOutput(dataSet, dir + File.separator + filePrefix + "output.arff");
	}
			
	private FastVector createFeatureVector() {
		FastVector fv = new FastVector();
		Attribute word = new Attribute("word", (FastVector)null);
		fv.addElement(word);
		
		String[] splitted;
		List<String> classes = new ArrayList<String>();
		List<String> docs = new ArrayList<String>();
		for(String s : IndexOutputCollector.getInstance().getInputFiles().keySet())
		{
			splitted = s.split(File.separator);
			classes.add(splitted[0]);
			docs.add(splitted[1]);
		}
				
		fv.addElement(new Attribute("class", createClassAttribute(classes)));
				
		for(String s : docs) {
			fv.addElement(new Attribute(s));
		}
		return fv;
	}
		
	private FastVector createClassAttribute(List<String> classnames) {
		FastVector fv = new FastVector();
		for(String s : classnames)
			fv.addElement(s);
		return fv;
	}
	
	private Instances createDataSet(FastVector featureVector) {
		Instances data = new Instances("MyInstances", featureVector, 0);
		Map<String, IndexCount> input = collector.getOutputMap();
		String actualClass = null;
		for(String word : input.keySet()) {
			Instance instance = null;
			for(Text doc : input.get(word).getTermFrequency().keySet())
			{
				String[] classAndFile = doc.toString().split(File.separator);
				if(actualClass == null) {
					actualClass = classAndFile[0];
					instance = createInstance(word, doc.toString());
				}
				else if(!actualClass.equals(classAndFile[0])) {
					actualClass = classAndFile[0];
					instance =createInstance(word, doc.toString());
				}
				instance.setValue(new Attribute(classAndFile[1], (FastVector)null), calcWeight(word, classAndFile[1]));
			}
			data.add(new SparseInstance(instance));
		}			
		return data;
	}
	
	private Instance createInstance(String word, String doc) {
		Instance instance = new Instance(featureVector.size());
		instance.setValue(new Attribute("word", (FastVector)null), word);
		instance.setValue(new Attribute("class", (FastVector)null), doc.split(File.separator)[0]);
		return instance;
	}
	
	private void writeOutput(Instances dataSet, String filename) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
}
