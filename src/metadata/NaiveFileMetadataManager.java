package metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NaiveFileMetadataManager implements MetadataManagerInterface {
	
	private String alias, path, separator;
	private File file;
	
	public NaiveFileMetadataManager() {}
	
	public NaiveFileMetadataManager(String alias, String path, String separator) {
		this.alias = alias;
		this.path = path;
		this.separator = separator;
		file = new File(path);
	}
	
	public NaiveFileMetadataManager(String alias, File file, String separator) throws Exception {
		this.alias = alias;
		this.separator = separator;
		this.file = file;
	}

	@Override
	public Map<String, Integer> getFieldPositions() {
		Map<String, Integer> fieldPositions = new HashMap<>();
		
		String[] columnNames = getColumnNames();
		for(int i=0; i<columnNames.length; i++)
			fieldPositions.put(columnNames[i], i);
		
		return fieldPositions;
	}

	@Override
	public File getDataFile() {
		return file;
	}

	@Override
	public String getSeparator() {
		return this.separator;
	}

	@Override
	public String[] getColumnNames() {		
		try {
			Scanner scanner = new Scanner(getDataFile());
			
			scanner.useDelimiter(separator);
			String colsLine = scanner.nextLine();
			
			return colsLine.split(separator);
		
		} catch (Exception e) {
			System.err.printf("File not found in path : %s\n", path);
			e.printStackTrace();
		}
		return null;
	}
	

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}
	

}
