package file.manager;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Patch;

import metadata.NaiveFileMetadataManager;

public class StructuredFileManager implements StructuredFileManagerInterface {
	
	// Arraylist containing the NaiveFileMetadataManager objects
	// Each of these objects are responsible for every registered file
	private ArrayList<NaiveFileMetadataManager> metadataFileManagerList;
	
	
	public StructuredFileManager() {
		metadataFileManagerList = new ArrayList<>();
	}

	@Override
	public File registerFile(String pAlias, String pPath, String pSeparator) throws IOException, NullPointerException {
		System.out.printf("[INFO] REGISTER FILE INTO THE SYSTEM\n"
				+ "[INFO] ALIAS : %s\n"
				+ "[INFO] PATH  : %s\n"
				+ "[INFO] DELIM : %s\n\n",
				pAlias, pPath, pSeparator);
		NaiveFileMetadataManager newlyMetadataFileManager = new NaiveFileMetadataManager(pAlias, pPath, pSeparator);
		metadataFileManagerList.add(newlyMetadataFileManager);
		
		return newlyMetadataFileManager.getDataFile();
	}

	@Override
	public String[] getFileColumnNames(String pAlias) {
		NaiveFileMetadataManager fileMetaManager = searchFileMetadataManagerByAlias(pAlias);
		if (fileMetaManager!=null)
			return fileMetaManager.getColumnNames();
		return null;
	}

	@Override
	public List<String[]> filterStructuredFile(String pAlias, Map<String, List<String>> pAtomicFilters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int printResultsToPrintStream(List<String[]> recordList, PrintStream pOut) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	/**
	 * @param pAlias Alias to search for
	 * @return NaiveFileMetadataManager object that handles the registered file with the specific Alias
	 */
	private NaiveFileMetadataManager searchFileMetadataManagerByAlias(String pAlias) {
		for (NaiveFileMetadataManager fileMetaManager : this.metadataFileManagerList) {
			if (fileMetaManager.getAlias().equals(pAlias)) {
				System.out.printf("[ %s ] found as a registered file\n", pAlias);
				return fileMetaManager;
			}
		}
		System.err.printf("No registered file find for Alias : %s\n", pAlias);
		return null;
	}
	
	/**
	 * @param pAlias Alias of the document to be searched
	 * @return String with the path of the registered file with the given alias
	 */
	public String getFilePathByAlias(String pAlias) {
		return searchFileMetadataManagerByAlias(pAlias).getPath();
	}
	
	public void emptyFileMetadataManagerArrayList() {
		this.metadataFileManagerList.removeAll(metadataFileManagerList);
	}

	public ArrayList<NaiveFileMetadataManager> getMetadataFileManagerList() {
		return metadataFileManagerList;
	}
	

}
