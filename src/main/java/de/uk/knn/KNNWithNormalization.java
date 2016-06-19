package de.uk.knn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.math.NumberUtils;

public class KNNWithNormalization {

	private static final String PATH_1 = "src/main/resources/IRIS.csv";
	private static final String PATH_2 = "src/main/resources/iris.data";
	private List<Entry> trainingData;
	private Map<Integer, Map<String, Double>> minMaxData;
	String classifiedType = "";
	private int k = 10;
	
	public static void main(String[] args){
		
		// KNN with a dataset with 8 attributes
		KNNWithNormalization knn = new KNNWithNormalization();
		knn.loadTrainingData(PATH_1);
		knn.getMinMax();
		knn.normalizeTrainingData();
		/* setosa */
		List<Double> data = Arrays.asList(5.10, 0.22, 3.50, 0.63, 1.40, 0.07, 0.20, 0.04); 
		/* virginica*/
		//List<Double> data = Arrays.asList(6.3, 0.555555556, 2.8, 0.333333333, 5.1, 0.694915254, 1.5, 0.583333333);
		Entry unknownEntry = new Entry();
		unknownEntry.setData(data);
		Entry nomalizedEntry = knn.normalizeEntry(unknownEntry);

		List<Entry> nearestNeighbors = knn.findNearestNeighbors(nomalizedEntry);
		knn.classifyEntry(nearestNeighbors, unknownEntry);
		
		// KNN with a dataset with 4 attributes
		knn.loadTrainingData(PATH_2);
		knn.getMinMax();
		knn.normalizeTrainingData();
		/* setosa */
		//List<Double> data2 = Arrays.asList(5.1, 3.5, 1.4, 0.2);
		/* fantasy */
		//List<Double> data2 = Arrays.asList(6.2, 2.7, 4.3, 1.9);
		/* fantasy */
		List<Double> data2 = Arrays.asList(6.2, 2.7, 4.3, 1.4);
		Entry unknownEntry2 = new Entry();
		unknownEntry2.setData(data2);
		Entry nomalizedEntry2 = knn.normalizeEntry(unknownEntry2);

		List<Entry> nearestNeighbors2 = knn.findNearestNeighbors(nomalizedEntry2);
		knn.classifyEntry(nearestNeighbors2, unknownEntry2);
	}
	
	public void loadTrainingData(String path){
		File csvFile = new File(path);
		trainingData = new ArrayList<Entry>();
		try {
			Scanner input = new Scanner(csvFile);
			System.out.println("Training Data");
			while(input.hasNextLine()){
				
				Entry entry = new Entry();
				String line = input.nextLine();
				String[] parts = line.split(",");
				if(parts.length > 1){
					for(String part : parts){					
						if(NumberUtils.isNumber(part)){
							Double d = Double.valueOf(part);
							entry.getData().add(d);
							System.out.printf("%.2f ", d);
						}
						else{
							entry.setType(part);
							System.out.println(part);
						}
						
					}
					
					trainingData.add(entry);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getMinMax(){
		minMaxData = new HashMap<>();
		int size = trainingData.get(0).getData().size();
		for(int i = 0; i < size; i++){
			Map<String, Double> minMax = new HashMap<>();
			
			ArrayList<Double> verticalEntries = new ArrayList<>();
			for(Entry entry : trainingData){
				
				verticalEntries.add(entry.getData().get(i));
			}
			minMax.put("min", Collections.min(verticalEntries));
			minMax.put("max", Collections.max(verticalEntries));
			minMaxData.put(i, minMax);
					
		}
	}
	
	
	public void normalizeTrainingData(){
		
		System.out.println("Normalized Data");
		for(Entry entry : trainingData){
			
			normalizeEntry(entry);
			System.out.println(entry.getType());
		}
		
	}
	public Entry normalizeEntry(Entry entry){
	
		int i = 0;
		for(Double dataEntry : entry.getData()){
			
			Double nextEntry = (dataEntry - minMaxData.get(i).get("min")) / (minMaxData.get(i).get("max") - minMaxData.get(i).get("min"));
			entry.getData().set(i, nextEntry);
			i++;
			System.out.printf("%.2f ", nextEntry);
		}
		return entry;
	}
	public List<Entry> findNearestNeighbors(Entry unknownEntry){
		
		List<Entry> nearestNeighbours = new ArrayList<Entry>();
		
		for(Entry entry : trainingData){
			List<Double> interimResults = new ArrayList<Double>();
			int i = 0; 
			for(Double attribute : entry.getData()){
				Double ud = unknownEntry.getData().get(i);
				Double ir = attribute - unknownEntry.getData().get(i);
				ir *= ir;
				interimResults.add(ir);
				i++;
			}
			Double result = 0.0;
			for(Double ir : interimResults){
				result = result + ir;
			}
			double distance = Math.sqrt(result);
			entry.setDistance(distance);
		}
		Collections.sort(trainingData);
		for(int i=0; i<k; i++ ){
			nearestNeighbours.add(trainingData.get(i));
		}
		return nearestNeighbours;
	}
	
	public void classifyEntry(List<Entry> nearestNeighbors, Entry unknownEntry){
		Map<String, List<Entry>> classifyMap = new HashMap<>();
		/* Get the amount of entries per type. */
		for(Entry entry : nearestNeighbors){
			if(classifyMap.containsKey(entry.getType())){				
				
				classifyMap.get(entry.getType()).add(entry);
			}
			else{
				List<Entry> entryList = new ArrayList<>();
				entryList.add(entry);
				classifyMap.put(entry.getType(), entryList);
			}
		}
		/* The type which has the most entries wins. */
		int maxSize = 0;
		
		Map<String, List<Entry>> equalCountsOfEntries = new HashMap<>();
		List<Entry> maxEntries = new ArrayList<>();
		for(String key : classifyMap.keySet()){
			int count = classifyMap.get(key).size();
			if(count > maxSize){
				maxSize = count;
				classifiedType = key;
				maxEntries = classifyMap.get(key);
			}
			else if(count == maxSize){
				equalCountsOfEntries.put(classifiedType, maxEntries);
				equalCountsOfEntries.put(key, classifyMap.get(key));
			}
		}
		/* If there are equal amount of entries for different types, add the distances and find the lower distance. */
		if(!equalCountsOfEntries.isEmpty()){
			
			String equalTypes = findTypeIfAmountIsEqual(equalCountsOfEntries);
			
			if(!equalTypes.isEmpty()){
				classifiedType = " There are equal distances :" + equalTypes;
			}
		}
		System.out.println("The entry is classified as: " + classifiedType);
		
	}
	/* If types has equal amounts the distances of the types will be summarized and the lower sum wins. If
	 * If sums are equal both types are printed */
	public String findTypeIfAmountIsEqual(Map<String, List<Entry>> equalCountsOfEntries){
		Map<String, Double>  distances= new HashMap<>();
		
		for(String key : equalCountsOfEntries.keySet()){
			
			Double distance = 0.0;
			for(Entry entry : equalCountsOfEntries.get(key)){
				distance += entry.getDistance();
			}
			distances.put(key, distance);
			
		}
		Double minDistance = null;
		String equalTypes = "";
		for(String type : distances.keySet()){
			if(minDistance == null){
				minDistance = distances.get(type);
				classifiedType = type;
			}
			else if(minDistance > distances.get(type)){
				minDistance = distances.get(type);
				classifiedType = type;
			}
			else if(minDistance == distances.get(type)){
				equalTypes = equalTypes + classifiedType + ", " + type;
			}
		}
		return equalTypes;
	}
	
}
