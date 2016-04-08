package zyx.export;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import zyx.export.domain.common.ExportConfig;
import zyx.export.domain.common.ExportResult;
import zyx.export.domain.common.ExportType;
import zyx.export.domain.excel.ExportCSVResult;
import zyx.export.domain.excel.ExportExcelResult;
import zyx.export.exception.FileExportException;
import zyx.export.impl.CSVExportor;
import zyx.export.impl.ExcelExportor;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/7.
 */
public class FileExportor {
    public final static String EXPORT_XML_BASE_PATH = "/properties/framework/export/";

    /**
     * 通过list<T> T可为bean或者map<String, Object>  导出文件
     *
     * @param exportConfig
     * @param data
     * @return
     * @throws zyx.export.exception.FileExportException
     */
    public static ExportResult getExportResult(ExportConfig exportConfig, List<?> data) throws FileExportException {
        ExportType exportType = exportConfig.getExportType();
        switch (exportType) {
            case EXCEL2007:
                HSSFWorkbook workbook = new ExcelExportor().getExportResult(data, exportConfig.getExportCells());
                ExportExcelResult exportExcelResult = new ExportExcelResult();
                exportExcelResult.setWorkbook(workbook);
                exportExcelResult.setFileName(exportConfig.getFileName());
                return exportExcelResult;
            case CSV:
                StringBuilder stringBuilder = new CSVExportor().getExportResult(data, exportConfig.getExportCells());
                ExportCSVResult exportCSVResult = new ExportCSVResult();
                exportCSVResult.setResult(stringBuilder.toString());
                exportCSVResult.setFileName(exportConfig.getFileName());
                return exportCSVResult;
        }
        throw new FileExportException("找不到对应的export type, export type is " + exportType.getNumber());
    }



}
