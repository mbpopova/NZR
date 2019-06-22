package mycompany.nzr.dc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mycompany.nzr.common.ConfiguredSystem;
import mycompany.nzr.common.FileFormat;
import mycompany.nzr.common.FileLocation;
import mycompany.nzr.common.Output;
import mycompany.nzr.dc.dao.DataCaptureRepoDAO;
import mycompany.nzr.dc.dao.DataCaptureSrcDAO;
import mycompany.nzr.dc.dao.DataCaptureTransferDAO;
import mycompany.nzr.dc.sqlbuilder.DataCaptureRepoSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureSrcSqlBuilder;
import mycompany.nzr.dc.sqlbuilder.DataCaptureTransferSqlBuilder;
import mycompany.nzr.sm.StorageManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TransferManager {

	@Autowired
	private StorageManager storageManager;

	@Autowired
	private DataCaptureTransferSqlBuilder sqlBuilder;

	@Autowired
	private DataCaptureTransferDAO transferDAO;

	@Autowired
	private DataCaptureSrcDAO srcDAO;

	@Autowired
	private DataCaptureSrcSqlBuilder srcSqlBuilder;

	@Autowired
	private DataCaptureRepoDAO repoDAO;

	@Autowired
	private DataCaptureRepoSqlBuilder repoSqlBuilder;

	@Autowired
	DataCaptureRepoDAO dcRepoDAO;

	@Autowired
	DataCaptureRepoSqlBuilder dcRepoSqlBuilder;

	private static String FS = System.getProperty("file.separator");
	private String logDirectory;
	private int parallelism;

	Logger logger = Logger.getLogger(this.getClass());

	public long transferDeletes(long batchSkey, long tableSkey, long objectId,
			String tableName, FileFormat format, FileLocation location,
			long lowxid, long highxid, long batchFolderSkey, long batchDtlSkey,
			long hostSkey) throws Exception {

		String sql = repoSqlBuilder.getPkColsSql();
		List<Map<String, Object>> pkCols = repoDAO.getPkCols(sql, tableSkey);

		if (pkCols.size() > 1) // returned as comma-separated, should be exactly
								// 1 per table;
			throw new IllegalStateException("Incorrect size of pk cols list");

		if (pkCols.size() == 0) { // A table without a PK;
			return 0;
		}
		String pkColsString = (String) pkCols.get(0).get("cols");
		sql = getDeletesCaptureSql(tableName, format, location, pkColsString);
		String dir = storageManager.getOrCreateDirectory(batchSkey, tableName,
				Output.DELETES, location);
		int cnt = transferDAO.execDataCaptureDeletesSql(sql, dir
				+ Output.DELETES.getFileName(), logDirectory, lowxid, highxid,
				hostSkey);
		if (cnt == 0) {
			storageManager.deleteDirectoryAndFiles(dir);
		}
		if (cnt > 0) {
			dcRepoDAO.insertBatchFile(
					dcRepoSqlBuilder.getInsertBatchFile_Sql(), batchSkey,
					batchFolderSkey, batchDtlSkey, tableName + FS
							+ Output.DELETES.getFolderName() + FS
							+ Output.DELETES.getFileName(), format.name(),
					Output.DELETES, location.name());
		}
		logger.info(cnt + " deleted rows captured");
		return cnt;
	}

	public long transferInserts(final long batchSkey, long objectId,
			final String tableName, final FileFormat format,
			final FileLocation location, final long lowxid, final long highxid,
			final long batchFolderSkey, final long batchDtlSkey,
			final ConfiguredSystem system) throws Exception {
		transferDAO.createAndStoreTemplate(system);

		final String sql = getInsertsCaptureSql(tableName, format, location);
		final String dir = storageManager.getOrCreateDirectory(batchSkey,
				tableName, Output.INSERTS, location);

		// List<CountingThread> threads = new
		// ArrayList<CountingThread>(parallelism);
		int resultCnt = 0;
		final List<Exception> threadExceptions = new ArrayList<Exception>();

		for (int i = 0; i < parallelism; i++) {
			final int curThread = i;

			CountingRunnable runnable = new CountingRunnable() {

				@Override
				public void run() {
					// TODO: change sqls to pass hi/lo xids
					try {
						setCnt(transferDAO.execDataCaptureInsertsSql(sql, dir
								+ curThread + Output.INSERTS.getFileName(),
								logDirectory, parallelism, curThread, lowxid,
								highxid, system.getHostSkey()));
						if (getCnt() > 0) {
							dcRepoDAO.insertBatchFile(
									dcRepoSqlBuilder.getInsertBatchFile_Sql(),
									batchSkey,
									batchFolderSkey,
									batchDtlSkey,
									tableName + FS
											+ Output.INSERTS.getFolderName()
											+ FS + curThread
											+ Output.INSERTS.getFileName(),
									format.name(), Output.INSERTS,
									location.name());
						}
						// setCnt(cnt);

					} catch (Exception e) {
						e.printStackTrace();
						threadExceptions.add(e);
					}

					// System.out.print("Thread: " + curThread + " is created");
				}
			};

			if (!threadExceptions.isEmpty()) {
				throw new Exception("An exception in a thread");
			}

			Thread t = new Thread(runnable);
			// threads.add(t);
			t.start();
			t.join();
			resultCnt += runnable.getCnt();
		}
		// TODO-interval!

		logger.info(resultCnt + " inserted row(s) captured");

		if (resultCnt == 0) {
			storageManager.deleteDirectoryAndFiles(dir);
		}

		return resultCnt;
	}

	private String getInsertsCaptureSql(String srcTable, FileFormat format,
			FileLocation location) {
		if (format == FileFormat.Z)
			throw new IllegalStateException(
					"Transfer with Compression is currently not supported");

		String sql_ = null;
		if (format == FileFormat.I) {
			if (location == FileLocation.LOCAL) {
				sql_ = sqlBuilder
						.getDataCaptureLocalInternalInsertsSql(srcTable);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = sqlBuilder
						.getDataCaptureRemoteInternalInsertsSql(srcTable);
			}
		} else if (format == FileFormat.T) {
			if (location == FileLocation.LOCAL) {
				sql_ = sqlBuilder.getDataCaptureLocalTextInsertsSql(srcTable);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = sqlBuilder.getDataCaptureRemoteTextInsertsSql(srcTable);
			}
		}

		return sql_;
	}

	private String getDeletesCaptureSql(String srcTable, FileFormat format,
			FileLocation location, String pkColsString) {
		if (format == FileFormat.Z)
			throw new IllegalStateException(
					"Transfer with Compression is currently not supported");

		String sql_ = null;
		if (format == FileFormat.I) {
			if (location == FileLocation.LOCAL) {
				sql_ = sqlBuilder.getDataCaptureLocalInternalDeletesSql(
						srcTable, pkColsString);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = sqlBuilder.getDataCaptureRemoteInternalDeletesSql(
						srcTable, pkColsString);
			}
		} else if (format == FileFormat.T) {
			if (location == FileLocation.LOCAL) {
				sql_ = sqlBuilder.getDataCaptureLocalTextDeletesSql(srcTable,
						pkColsString);
			}
			if (location == FileLocation.REMOTE) {
				sql_ = sqlBuilder.getDataCaptureRemoteTextDeletesSql(srcTable,
						pkColsString);
			}
		}

		return sql_;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	class CountingRunnable implements Runnable {
		private int cnt;

		public synchronized int getCnt() {
			return cnt;
		}

		public synchronized void setCnt(int cnt) {
			this.cnt = cnt;
		}

		public synchronized void run() {
		}

	}
}
