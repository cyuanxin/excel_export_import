package zyx.export;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import zyx.export.domain.common.ExportCell;
import zyx.export.domain.common.ExportConfig;
import zyx.export.domain.common.ExportType;
import zyx.export.exception.FileExportException;
import zyx.importfile.ConfigParser;
import zyx.importfile.exception.FileImportException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document document = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(inputStream);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new FileExportException(e, "pares xml error");
        }


        Element root = document.getDocumentElement();
        NodeList elements = root.getElementsByTagName("cell");
        List<ExportCell> exportCells = initElement(elements);

        String fileName = "";
        String exportType1 = "";
        try {
            fileName = ConfigParser.getNodeText(root, "fileName");
            exportType1 = ConfigParser.getNodeText(root, "exportType");
        } catch (FileImportException e) {
            throw new FileExportException(e);
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new FileExportException("用于导出的xml文档 <fileName> 为空");
        }

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

    private static List<ExportCell> initElement(NodeList elements) throws FileExportException {

        List<ExportCell> exportCells = new ArrayList<ExportCell>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            ExportCell exportCell = new ExportCell();
            Element node = (Element) elements.item(i);
            String titleText = "";
            String aliasText = "";
            try {
                titleText = ConfigParser.getNodeText(node, "title");
                aliasText = ConfigParser.getNodeText(node, "alias");
            } catch (FileImportException e) {
                throw new FileExportException(e);
            }
            if (StringUtils.isEmpty(titleText)) {
                throw new FileExportException("用于导出的xml文档 <title> 为空");
            }
            exportCell.setTitle(titleText);

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
