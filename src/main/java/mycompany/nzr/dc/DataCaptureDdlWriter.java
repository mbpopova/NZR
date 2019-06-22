package mycompany.nzr.dc;

import java.io.File;
import java.io.FileWriter;

import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;
import mycompany.nzr.sm.StorageManager;

import org.springframework.beans.factory.annotation.Autowired;

public class DataCaptureDdlWriter {

	@Autowired
	DataCaptureRepoDAO dcRepoDAO;

	@Autowired
	DataCaptureRepoSqlBuilder dcRepoSqlBuilder;

	@Autowired
	StorageManager sm;

	private static String FS = System.getProperty("file.separator");

	public void write(long dcBatchSkey, long batchDtlSkey,
			long batchFolderSkey, String tableName, String data,
			boolean cleanup, Output output, FileLocation fileLocation,
			FileFormat fileFormat) throws Exception {
		if (data.trim().length() == 0) {
			return;
		}
		String dir = sm.getOrCreateDirectory(dcBatchSkey, tableName, output,
				fileLocation);
		File filename = new File(dir + FS + output.getFileName());

		if (cleanup && filename.exists()) {
			filename.delete();
		}
		FileWriter fw = null;

		try {
			fw = new FileWriter(filename, true);
			fw.write(data);
		} finally {
			fw.close();
		}

		dcRepoDAO.insertBatchFile(dcRepoSqlBuilder.getInsertBatchFile_Sql(),
				dcBatchSkey, batchFolderSkey, batchDtlSkey, tableName + FS
						+ output.getFolderName() + FS + output.getFileName(),
				"SQL", output, "NZR");

	}

}
