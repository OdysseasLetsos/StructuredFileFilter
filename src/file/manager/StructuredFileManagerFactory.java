package file.manager;

public class StructuredFileManagerFactory {
	
	public StructuredFileManagerFactory() {}
	
	/**
	 * @return a StructuredFileManagerInterface object
	 */
	public StructuredFileManagerInterface createStructuredFileManager() {
		return new StructuredFileManager();
	}
	
}
