package at.tuwien.ir.textindexer.weighting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	protected IndexOutputCollector collector = IndexOutputCollector.getInstance();
	
	private FastVector featureVector;
	private List<String> docs;
	
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
		Set<String> classes = new HashSet<String>();
		docs = new ArrayList<String>();
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
		
	private FastVector createClassAttribute(Set<String> classnames) {
		FastVector fv = new FastVector();
		for(String s : classnames) {
		    fv.addElement(s);
		}
		return fv;
	}
	
	//TODO fix... 
	private Instances createDataSet(FastVector featureVector) {
		Instances data = new Instances("MyInstances", featureVector, 0);
		Map<String, IndexCount> input = collector.getOutputMap();
		for(String word : input.keySet()) {
		    String actualClass = null;
			Instance instance = null;
			for(Text doc : input.get(word).getTermFrequency().keySet())
			{
				String[] classAndFile = doc.toString().split(File.separator);
				if(actualClass == null) {
					actualClass = classAndFile[0];
					instance = createInstance(word, classAndFile[0]);
				}
				else if(!actualClass.equals(classAndFile[0])) {
					actualClass = classAndFile[0];
					instance =createInstance(word, classAndFile[0]);
				}
				instance.setValue(new Attribute(classAndFile[1], (FastVector)null), calcWeight(word, classAndFile[1]));
				int pos = 2 + docs.indexOf(classAndFile[1]) ; // TODO
				instance.setValue((Attribute)featureVector.elementAt(pos), calcWeight(word, classAndFile[1]));
			}
			data.add(new SparseInstance(instance));
		}			
		return data;
	}
	
	private Instance createInstance(String word, String clas) {
		Instance instance = new Instance(featureVector.size());
		instance.setValue((Attribute)featureVector.elementAt(0), word);
		instance.setValue((Attribute)featureVector.elementAt(1), clas);
		//instance.setValue(new Attribute("word", (FastVector)null), word);
		//instance.setValue(new Attribute("class", (FastVector)null), doc.split(File.separator)[0]);
		return instance;
	}
	
	private void writeOutput(Instances dataSet, String filename) throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(filename));
		saver.writeBatch();
	}
}
