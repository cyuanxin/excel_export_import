package zyx.export;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import zyx.export.domain.common.ExportCell;
import zyx.export.domain.common.ExportConfig;
import zyx.export.domain.common.ExportType;
import zyx.export.exception.FileExportException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyuanxin on 2016/3/25.
 */
public class ExportConfigFactory {
    public static ExportConfig getExportConfig(InputStream inputStream) throws FileExportException {
        return getExportCells(inputStream);
    }

    private static ExportConfig getExportCells(InputStream inputStream) throws FileExportException {

        ExportConfig exportConfig = new ExportConfig();
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

        exportConfig.setFileName(fileName);
        ExportType exportType = ExportType.getExportType(Integer.valueOf(exportType1));
        if (exportType == null) {
            throw new FileExportException("找不到相应的ExportType 解析xml得到的exportType 是" + exportType1);
        }
        exportConfig.setExportType(exportType);
        exportConfig.setExportCells(exportCells);

        return exportConfig;
    }

    private static List<ExportCell> initElement(List<Element> elements) throws FileExportException {

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
