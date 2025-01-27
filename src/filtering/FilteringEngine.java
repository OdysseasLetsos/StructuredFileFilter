package filtering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import metadata.MetadataManagerInterface;

public class FilteringEngine implements FilteringEngineInterface{

	private File fileToFilter;
	private String fileSeparator;
	private String[] columnNames;
	private Map<String, Integer> fieldPositions;
	private List<List<String>> filterStringList;
	
	private Map<String, Integer> fieldsToBeFiltered;
	private MetadataManagerInterface metadatamanager;

	
	public FilteringEngine() {
		fieldsToBeFiltered = new HashMap<>();
	}
	
	public FilteringEngine(Map<String, List<String>> pAtomicFilters, MetadataManagerInterface pMetadataManager) {
		fieldsToBeFiltered = new HashMap<>();
		metadatamanager = pMetadataManager;
		setupFilteringEngine(pAtomicFilters, pMetadataManager);
	}


	@Override
	public int setupFilteringEngine(Map<String, List<String>> pAtomicFilters,
			MetadataManagerInterface pMetadataManager) {
		

		
		fileSeparator = pMetadataManager.getSeparator();
		fileToFilter = pMetadataManager.getDataFile();
		columnNames = pMetadataManager.getColumnNames();
		fieldPositions = pMetadataManager.getFieldPositions();
		filterStringList = new ArrayList<>();	
		
		for (Map.Entry<String, List<String>> atomicFilter : pAtomicFilters.entrySet()) {
			filterStringList.add(atomicFilter.getValue());
			
			if (fieldPositions.containsKey(atomicFilter.getKey())){
				fieldsToBeFiltered.put(atomicFilter.getKey(),
						fieldPositions.get(atomicFilter.getKey()));
			}
		}
		
		System.out.print("\n[INFO] SETUP FILTER ENGINE"
				+"\n[INFO] FILTERS -> ");
		for (List<String> x : filterStringList)
			System.out.print(x.toString());
		
		
		return 0;
	}

	@Override
	public List<String[]> workWithFile() {
		try {
			Scanner sc = new Scanner(metadatamanager.getDataFile());
			sc.useDelimiter(metadatamanager.getSeparator());
			
			List<String[]> listOfArrays = new ArrayList<String[]>();
			
			// Parsing its record into listOfArrays
			sc.nextLine();
			while(sc.hasNext()) {
				String[] recordArray = {sc.nextLine()};
				listOfArrays.add(recordArray);
			}
			
			// List with the records after filtering
			for (int i=0; i<filterStringList.size(); i++) {
				if (filterStringList.size()>1) {
					List<String[]> newList = new ArrayList<>();
					for (int j=0; j<filterStringList.get(i).size(); j++) {
						listOfArrays.addAll(processFilters(listOfArrays, filterStringList.get(i).get(j)));
					}
				}else {
					listOfArrays = processFilters(listOfArrays, filterStringList.get(i).get(i));
				}
			}
			
			
			// Printing each record
			System.out.println("\n***FILTERED RECORDS***");
			int recordNum = 0;
			for (String[] x : listOfArrays) {
				System.out.print("REC"+recordNum+": " + Arrays.toString(x)+"\n");
				recordNum++;
			}
			
			
			return listOfArrays;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private List<String[]> processFilters(List<String[]> recordsListToFilter, String filter) {
		List<String[]> filteredList = new ArrayList<>();
		
		for (int j=0; j<recordsListToFilter.size(); j++) {
			if (Arrays.toString(recordsListToFilter.get(j)).contains(filter)) {
				filteredList.add(recordsListToFilter.get(j));
			}
		}
		return filteredList;
	}
	

}
