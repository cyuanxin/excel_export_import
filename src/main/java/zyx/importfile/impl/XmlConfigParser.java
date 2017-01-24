package zyx.importfile.impl;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import zyx.importfile.ConfigParser;
import zyx.importfile.domain.common.Configuration;
import zyx.importfile.domain.common.ImportCell;
import zyx.importfile.exception.FileImportException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stark.zhang on 2015/11/21.
 */
public class XmlConfigParser extends ConfigParser {

    private static List<ImportCell> initElement(NodeList elements) throws FileImportException {

        List<ImportCell> importCells = new ArrayList<ImportCell>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            ImportCell importCell = new ImportCell();
            Element node = (Element) elements.item(i);
            String numberText = getNodeText(node, "number");
            if (StringUtils.isEmpty(numberText) || !StringUtils.isNumeric(numberText)) {
                throw new FileImportException("用于导入的xml文档 <number> 为空 或者非数字");
            }
            importCell.setNumber(Integer.valueOf(numberText));


            String keyText = getNodeText(node, "key");
            if (StringUtils.isEmpty(keyText)) {
                throw new FileImportException("用于导入的xml文档 <key> 为空");
            }
            importCell.setKey(keyText);

            String cellTypeText = getNodeText(node, "cellType");
            importCell.setCellType(getCellType(cellTypeText));

            String nullbleText = getNodeText(node, "nullble");
            if (StringUtils.isEmpty(nullbleText) || !StringUtils.isNumeric(nullbleText)) {
                throw new FileImportException("用于导入的xml文档 <nullble> 为空 或者非数字");
            }
            importCell.setNullAble(ImportCell.NullAble.getNullble(Integer.valueOf(nullbleText)));

            importCells.add(importCell);
        }

        if (importCells.isEmpty()) {
            throw new FileImportException("用于导入的xml文档 解析内容为空");
        }

        return importCells;
    }

    private static ImportCell.CellType getCellType(String typeStr) throws FileImportException {
        if (StringUtils.isEmpty(typeStr)) {
            throw new FileImportException("import config xml cellType is empty");
        }
        if (StringUtils.isNumeric(typeStr)) {
            return ImportCell.CellType.getCellType(Integer.valueOf(typeStr));
        } else {
            return ImportCell.CellType.getCellType(typeStr);

        }

    }



    public final static String XML_BASE_PATH = "/properties/framework/import_file/";
//    /**
//     * 该方法是兼容交易系统
//     * 在 /properties/framework/import_file/ 放入相应的配置xml
//     * @param xmlName 文件name
//     * @throws zyx.importfile.exception.FileImportException
//     */
//    public Configuration getConfig(String xmlName) throws FileImportException {
//        try {
//            InputStream input = Resources.getResourceAsStream(XML_BASE_PATH.concat(xmlName));
//            return getConfig(input);
//        } catch (IOException e) {
//            throw new FileImportException(e, "FileImportorFactoryBuilder 生成 inputstream 出错" + e);
//        }
//    }

    /**
     * 获得configuration
     *
     * @param configStream
     * @return
     * @throws FileImportException
     */
    @Override
    public Configuration getConfig(InputStream configStream) throws FileImportException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document document = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(configStream);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new FileImportException(e, "pares xml error");
        }
//        Puts all Text nodes in the full depth of the sub-tree underneath this Node
        document.getDocumentElement().normalize();
        Configuration configuration = new Configuration();

        Element root = document.getDocumentElement();
        NodeList nList = document.getElementsByTagName("cell");
        List<ImportCell> importCells = initElement(nList);

        String startRowNoText = getNodeText(root, "startRowNo");
        if (StringUtils.isEmpty(startRowNoText) || !StringUtils.isNumeric(startRowNoText)) {
            throw new FileImportException("用于导入的xml文档 <number> 为空 或者非数字");
        }
        configuration.setStartRowNo(Integer.valueOf(startRowNoText));

//        Element resultType = root.element("resultType");
//        String resultTypeText = resultType.getText().trim();
//        if (StringUtils.isEmpty(resultTypeText) || !StringUtils.isNumeric(resultTypeText)) {
//            throw new FileImportException("用于导入的xml文档 <number> 为空 或者非数字");
//        }
//        configuration.setResultType(Integer.valueOf(resultTypeText));

        configuration.setImportCells(importCells);
        //默认返回excel
        configuration.setImportFileType(Configuration.ImportFileType.EXCEL);
        return configuration;
    }

}
