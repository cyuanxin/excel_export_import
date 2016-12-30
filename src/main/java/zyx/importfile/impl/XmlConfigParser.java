package zyx.importfile.impl;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import zyx.importfile.ConfigParser;
import zyx.importfile.domain.common.Configuration;
import zyx.importfile.domain.common.ImportCell;
import zyx.importfile.exception.FileImportException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stark.zhang on 2015/11/21.
 */
public class XmlConfigParser implements ConfigParser {

    private static List<ImportCell> initElement(List<Element> elements) throws FileImportException {

        List<ImportCell> importCells = new ArrayList<ImportCell>(elements.size());
        for (Element element : elements) {
            ImportCell importCell = new ImportCell();
            Element number = element.element("number");
            String numberText = number.getText().trim();
            if (StringUtils.isEmpty(numberText) || !StringUtils.isNumeric(numberText)) {
                throw new FileImportException("用于导入的xml文档 <number> 为空 或者非数字");
            }
            importCell.setNumber(Integer.valueOf(numberText));

            Element key = element.element("key");
            String keyText = key.getText().trim();
            if (StringUtils.isEmpty(keyText)) {
                throw new FileImportException("用于导入的xml文档 <key> 为空");
            }
            importCell.setKey(keyText);

            Element cellType = element.element("cellType");
            String cellTypeText = cellType.getText().trim();
            importCell.setCellType(getCellType(cellTypeText));

            Element nullble = element.element("nullble");
            String nullbleText = nullble.getText().trim();
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

        Configuration configuration = new Configuration();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(configStream);
        } catch (DocumentException e) {
            throw new FileImportException(e, "读取xml文档出错 msg is " + e);
        }

        Element root = document.getRootElement();
        List<Element> elements = root.elements("cell");
        List<ImportCell> importCells = initElement(elements);

        Element startRowNo = root.element("startRowNo");
        String startRowNoText = startRowNo.getText().trim();
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
