package zyx.export;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import zyx.export.domain.common.ExportCell;
import zyx.export.domain.common.ExportFile;
import zyx.export.domain.common.ExportResult;
import zyx.export.domain.common.ExportType;
import zyx.export.domain.excel.ExportCSVResult;
import zyx.export.domain.excel.ExportExcelResult;
import zyx.export.exception.FileExportException;
import zyx.export.impl.CSVExportor;
import zyx.export.impl.ExcelExportor;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stark.zhang on 2015/11/7.
 */
public class FileExportor {
    public final static String EXPORT_XML_BASE_PATH = "/properties/framework/export/";
    @Resource
    private ExcelExportor excelExportor;
    @Resource
    private CSVExportor csvExportor;

    /**
     * 通过list<T> T可为bean或者map<String, Object>  导出文件
     *
     * @param inputStream
     * @param data
     * @return
     * @throws zyx.export.exception.FileExportException
     */
    public ExportResult getExportResult(InputStream inputStream, List<?> data) throws FileExportException {
        ExportFile exportFile = getExportCells(inputStream);
        ExportType exportType = exportFile.getExportType();
        switch (exportType) {
            case EXCEL2007:
                HSSFWorkbook workbook = excelExportor.getExportResult(data, exportFile.getExportCells());
                ExportExcelResult exportExcelResult = new ExportExcelResult();
                exportExcelResult.setWorkbook(workbook);
                exportExcelResult.setFileName(exportFile.getFileName());
                return exportExcelResult;
            case CSV:
                StringBuilder stringBuilder = csvExportor.getExportResult(data, exportFile.getExportCells());
                ExportCSVResult exportCSVResult = new ExportCSVResult();
                exportCSVResult.setResult(stringBuilder.toString());
                exportCSVResult.setFileName(exportFile.getFileName());
                return exportCSVResult;
        }
        throw new FileExportException("找不到对应的export type, export type is " + exportType.getNumber());
    }


    private ExportFile getExportCells(InputStream inputStream) throws FileExportException {

        ExportFile exportFile = new ExportFile();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            throw new FileExportException("读取xml文档出错 msg is " + e);
        }

        Element root = document.getRootElement();
        List<Element> elements = root.elements("cell");
        List<ExportCell> exportCells = initElement(elements);

        String fileName = root.element("fileName").getTextTrim();
        if (StringUtils.isEmpty(fileName)) {
            throw new FileExportException("用于导出的xml文档 <fileName> 为空");
        }

        String exportType1 = root.element("exportType").getTextTrim();
        if (StringUtils.isEmpty(exportType1) || !StringUtils.isNumeric(exportType1)) {
            throw new FileExportException("用于导出的xml文档 <exportType> 为空");
        }

        exportFile.setFileName(fileName);
        ExportType exportType = ExportType.getExportType(Integer.valueOf(exportType1));
        if (exportType == null) {
            throw new FileExportException("找不到相应的ExportType 解析xml得到的exportType 是" + exportType1);
        }
        exportFile.setExportType(exportType);
        exportFile.setExportCells(exportCells);

        return exportFile;
    }

    private List<ExportCell> initElement(List<Element> elements) throws FileExportException {

        List<ExportCell> exportCells = new ArrayList<ExportCell>(elements.size());
        for (Element element : elements) {
            ExportCell exportCell = new ExportCell();
            Element title = element.element("title");
            String titleText = title.getText().trim();
            if (StringUtils.isEmpty(titleText)) {
                throw new FileExportException("用于导出的xml文档 <title> 为空");
            }
            exportCell.setTitle(titleText);

            Element alias = element.element("alias");
            String aliasText = alias.getText().trim();
            if (StringUtils.isEmpty(aliasText)) {
                throw new FileExportException("用于导出的xml文档 <alias> 为空");
            }
            exportCell.setAlias(aliasText);

            exportCells.add(exportCell);
        }

        if (exportCells.isEmpty()) {
            throw new FileExportException("用于导出的xml文档解析内容为空");
        }

        return exportCells;
    }
}
