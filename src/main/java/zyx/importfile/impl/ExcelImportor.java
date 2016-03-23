package zyx.importfile.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import zyx.importfile.FileImportor;
import zyx.importfile.domain.MapResult;
import zyx.importfile.domain.common.Configuration;
import zyx.importfile.domain.common.ImportCell;
import zyx.importfile.domain.common.ImportResult;
import zyx.importfile.exception.FileImportException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by stark.zhang on 2015/11/20.
 */
public class ExcelImportor extends FileImportor {
    private Configuration configuration;


    @Override
    public ImportResult getImportResult(File file, String fileName) throws FileImportException {
        if (configuration == null) {
            throw new FileImportException("configuration is null");
        }
        StringBuilder stringbuilder = new StringBuilder();
        Workbook workbook = null;
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
                .substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } catch (IOException e) {
                throw new FileImportException(e, e.getMessage());
            }
        } else if ("xlsx".equals(extension)) {
            try {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } catch (IOException e) {
                throw new FileImportException(e, e.getMessage());
            }
        } else {
            throw new FileImportException("unsupport file style");
        }
        List<Map> result = readExcel(workbook, configuration, stringbuilder);
        MapResult mapResult = new MapResult();
        mapResult.setResult(result);
        mapResult.setResMsg(stringbuilder.toString());
        return mapResult;
    }

    private List<Map> readExcel(Workbook workbook, Configuration configuration, StringBuilder sb) throws FileImportException {
        //选择第一个sheet
        Sheet sheet = workbook.getSheetAt(0);
        int startRow = configuration.getStartRowNo();
        List<ImportCell> lists = configuration.getImportCells();
        int phyRow = sheet.getPhysicalNumberOfRows();
        List<Map> results = Lists.newLinkedList();
        for (int t = startRow; t < phyRow; t++) {
            Row row = sheet.getRow(t);
            if (row == null) {
                continue;
            }
            //poi获取正确行数很难。这里约定，前三个值都为空时，自动放弃该行
            if (isCellEmpty(row.getCell(0)) && isCellEmpty(row.getCell(1)) && isCellEmpty(row.getCell(2))) {
                continue;
            }
            Map<String, Object> maps = Maps.newLinkedHashMap();
            maps.put(MapResult.IS_LINE_LEGAL_KEY, true);
            for (ImportCell importCell : lists) {
                setValue(maps, importCell, row, sb, t, startRow);
            }
            results.add(maps);
        }
        return results;
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null) {
            return true;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isEmpty(cell.getStringCellValue())) {
            return true;
        }
        return false;
    }

    private void setErrMsg(String errMsg, Map<String, Object> maps, StringBuilder sb) {
        sb.append(errMsg);
        maps.put(MapResult.IS_LINE_LEGAL_KEY, false);
    }

    private void setValue(Map<String, Object> maps, ImportCell importCell, Row row, StringBuilder sb, int line, int startRow) throws FileImportException {
        int num = importCell.getNumber();
        int showLine = line + startRow;
        int showColumn = num + startRow;
        maps.put(MapResult.LINE_NUM_KEY, showLine);
        ImportCell.CellType cellType = importCell.getCellType();
        ImportCell.NullAble nullable = importCell.getNullAble();
        String errMsg = null;
        String key = importCell.getKey();
        Cell cell = row.getCell(num);
        int rawCellType = Cell.CELL_TYPE_BLANK;
        if (cell != null) {
            rawCellType = cell.getCellType();
        }
        if (rawCellType == Cell.CELL_TYPE_BLANK ||
                cell == null ||
                rawCellType == Cell.CELL_TYPE_STRING && StringUtils.isEmpty(cell.getStringCellValue())) {
            if (nullable == ImportCell.NullAble.NULL_ALLOWED) {
                maps.put(key, Optional.absent());
            } else {
                errMsg = String.format("line:%d,column:%d is null,but null is not allowed by setting \n", showLine, showColumn);
                setErrMsg(errMsg, maps, sb);
            }
        } else {
            switch (cellType) {
                case INT:
                    if (rawCellType == Cell.CELL_TYPE_STRING) {
                        String temp = cell.getStringCellValue();
                        if (!StringUtils.isNumeric(temp)) {
                            errMsg = String.format("line:%d,column:%d is not number \n", showLine, showColumn);
                            setErrMsg(errMsg, maps, sb);
                        }
                        maps.put(key, Integer.valueOf(temp));
                    }
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        Double temp = cell.getNumericCellValue();
                        maps.put(key, temp.intValue());
                    }
                    break;

                case STRING:
                    String temp = null;
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        temp = String.valueOf(cell.getNumericCellValue());
                        maps.put(key, temp);
                        break;
                    }
                    if (rawCellType == Cell.CELL_TYPE_STRING) {
                        temp = cell.getStringCellValue();
                        maps.put(key, temp);
                        break;
                    }

                    errMsg = String.format("line:%d,column:%d is not string\n", showLine, showColumn);
                    setErrMsg(errMsg, maps, sb);
                    break;

                case FLOAT:
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        Double temp1 = cell.getNumericCellValue();
                        maps.put(key, temp1.floatValue());
                    } else {
                        errMsg = String.format("line:%d,column:%d is not float\n", showLine, showColumn);
                        setErrMsg(errMsg, maps, sb);
                    }
                    break;

                case DATE:
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                        maps.put(key, date);
                    } else {
                        errMsg = String.format("line:%d,column:%d is not date\n", showLine, showColumn);
                        setErrMsg(errMsg, maps, sb);
                    }
                    break;
                case BIGDECIMAL:
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        Double temp1 = cell.getNumericCellValue();
                        maps.put(key, BigDecimal.valueOf(temp1));
                    } else {
                        errMsg = String.format("line:%d,column:%d is not bigDecimal\n", showLine, showColumn);
                        setErrMsg(errMsg, maps, sb);
                    }
                    break;
                case DOUBLE:
                    if (rawCellType == Cell.CELL_TYPE_NUMERIC) {
                        Double temp1 = cell.getNumericCellValue();
                        maps.put(key, temp1);
                    } else {
                        errMsg = String.format("line:%d,column:%d is not double\n", showLine, showColumn);
                        setErrMsg(errMsg, maps, sb);
                    }
                    break;
            }


        }
    }

    public ExcelImportor(Configuration configuration) {
        this.configuration = configuration;
    }

}
