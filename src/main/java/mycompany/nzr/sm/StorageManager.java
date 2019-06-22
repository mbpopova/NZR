package mycompany.nzr.sm;

import java.io.File;
import java.util.Map;

import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;

import org.springframework.beans.factory.annotation.Autowired;

public class StorageManager {

	private static String FS = System.getProperty("file.separator");

	@Autowired
	private StorageMgmtRepoDAO smRepoDAO;
	
	public final static String SRC_HOST_COL = "src_host";
	public final static String DC_SET_COL = "dc_set_name";
	private String root ;
	
	public void setRoot(String root) {
		this.root = root;
	}
	public String getRoot() {
		return root;
	}

	public Map<String, Object> getHostAndDataCaptureSet(long batchSkey) {

		Map<String, Object> map = smRepoDAO.getHostAndDatacaptureSet(batchSkey);
		// The map's keys: src_host, rep_set_name

		if (map.size() != 2) {
			throw new IllegalStateException(
					"Either Host or DC Set name are not retrieved");
		}
		return map;
		
	}
	
	public synchronized String getOrCreateDirectory(long batchSkey, String tableName,
			Output output, FileLocation location) throws Exception {
		if (output == Output.INSERTS && location == FileLocation.LOCAL) { //Target location - Netezza box

			if (root.contains("\\"))
				throw new IllegalStateException(
						"Incorrect root for the file target location");
			FS = "/";
		}
		Map<String, Object> map = getHostAndDataCaptureSet(batchSkey);
		
		//TODO: get root from db:
		//String root = "C:\\nzr";
		String srcHost = (String) map.get(SRC_HOST_COL);
		String dcSet = (String) map.get(DC_SET_COL);

		if (tableName == null && output == null )
			return root + FS + srcHost + FS + dcSet + FS + batchSkey + FS;
		/*
		 * String root = "C:\\root"; String srcHost = "srchost"; String dcSet =
		 * "dcset";
		 */
		File outputdir = new File(root + FS + srcHost + FS + dcSet + FS + batchSkey + FS +
				tableName + FS + output.getFolderName());

		if (outputdir.exists()) {
			return outputdir.getAbsolutePath()+ FS;
		}

		File objdir = new File(root + FS + srcHost + FS + dcSet + FS + batchSkey + FS
				+ tableName);

		if (objdir.exists()) {
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " + outputdir.getName() + " could not be created");
			return outputdir.getAbsolutePath()+ FS;
		}

		File batchdir = new File(root + FS + srcHost + FS + dcSet + FS + batchSkey);

		if (batchdir.exists()) {
			if (!objdir.mkdir()) throw new IllegalStateException ("Directory " + objdir.getName() + " could not be created");;
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " + outputdir.getName() + " could not be created");;
			return outputdir.getAbsolutePath()+ FS;
		}
		
		File dcsetdir = new File(root + FS + srcHost + FS + dcSet);

		if (dcsetdir.exists()) {
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
			if (!objdir.mkdir()) throw new IllegalStateException ("Directory " + objdir.getName() + " could not be created");;
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " + outputdir.getName() + " could not be created");;
			return outputdir.getAbsolutePath()+ FS;
		}
		
		
		File hostdir = new File(root + FS + srcHost);

		if (hostdir.exists()) {			
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " + dcsetdir.getName() + " could not be created");;
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
			if (!objdir.mkdir()) throw new IllegalStateException ("Directory " + objdir.getName() + " could not be created");;
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " + outputdir.getName() + " could not be created");;
			return outputdir.getAbsolutePath()+ FS;
		}

		File rootdir = new File(root);

		if (rootdir.exists()) {
			if (!hostdir.mkdir()) throw new IllegalStateException ("Directory " + hostdir.getName() + " could not be created");;
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " + dcsetdir.getName() + " could not be created");;
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
			if (!objdir.mkdir()) throw new IllegalStateException ("Directory " + objdir.getName() + " could not be created");;
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " + outputdir.getName() + " could not be created");;
			return outputdir.getAbsolutePath()+ FS;
		} else {
			if (!rootdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + rootdir.getName() + "\"" + " could not be created");
			if (!hostdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + hostdir.getName() + "\"" + " could not be created");
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + dcsetdir.getName() + "\"" + " could not be created");
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + "\"" +batchdir.getName() +  "\"" + " could not be created");;
			if (!objdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + objdir.getName() + "\"" + " could not be created");
			if (!outputdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + outputdir.getName() + "\"" + " could not be created");
			return outputdir.getAbsolutePath() + FS;
		}
	}
	
	
	public synchronized String getOrCreateDirectory(long batchSkey) throws Exception {

		Map<String, Object> map = getHostAndDataCaptureSet(batchSkey);
		
		//TODO: get root from db:
		//String root = "C:\\nzr";
		String srcHost = (String) map.get(SRC_HOST_COL);
		String dcSet = (String) map.get(DC_SET_COL);

		File batchdir = new File(root + FS + srcHost + FS + dcSet + FS + batchSkey);

		if (batchdir.exists()) {
				return batchdir.getAbsolutePath()+ FS;
		}
		
		File dcsetdir = new File(root + FS + srcHost + FS + dcSet);

		if (dcsetdir.exists()) {
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
				return batchdir.getAbsolutePath()+ FS;
		}
		
		File hostdir = new File(root + FS + srcHost);

		if (hostdir.exists()) {			
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " + dcsetdir.getName() + " could not be created");;
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
				return batchdir.getAbsolutePath()+ FS;
		}

		File rootdir = new File(root);

		if (rootdir.exists()) {
			if (!hostdir.mkdir()) throw new IllegalStateException ("Directory " + hostdir.getName() + " could not be created");;
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " + dcsetdir.getName() + " could not be created");;
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + batchdir.getName() + " could not be created");;
				return batchdir.getAbsolutePath()+ FS;
		} else {
			if (!rootdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + rootdir.getName() + "\"" + " could not be created");
			if (!hostdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + hostdir.getName() + "\"" + " could not be created");
			if (!dcsetdir.mkdir()) throw new IllegalStateException ("Directory " +  "\"" + dcsetdir.getName() + "\"" + " could not be created");
			if (!batchdir.mkdir()) throw new IllegalStateException ("Directory " + "\"" +batchdir.getName() +  "\"" + " could not be created");;
				return batchdir.getAbsolutePath() + FS;
		}
	}
	
	
	public void deleteDirectoryAndFiles(String dirFullName) {
		File dir = new File (dirFullName);
		
		if (dir.isDirectory()) {
			String[] fileNames = dir.list();			
			
			for (int i = 0 ; i < fileNames.length; i++) {
				File file  = new File(dirFullName + "\\" + fileNames[i]);
				if (file.exists()) {
					file.delete();	
				}				
			}
		}
		dir.delete();
	}
	
	public void deleteFile (String fileFullName) {
		File file = new File(fileFullName);
		if (file.exists()) {
			file.delete();
		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		StorageManager sm = new StorageManager();
		//System.out.print(sm.getOrCreateDirectory(213, "Customers", Output.INSERTS, FileLocation.REMOTE));
		
		sm.deleteDirectoryAndFiles("D:\\toBeDeleted");
		System.out.print("DONE!!!!!!!!!!");
	}

}
