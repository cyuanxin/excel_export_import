package zyx.importfile.domain.common;


import zyx.domain.BaseModel;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/19.
 */
public class Configuration extends BaseModel {
    private Integer startRowNo;//读取的起始行 起始为0
    private ImportFileType importFileType;
    private Integer resultType;//返回数据类型 返回maps 该方法暂时无用
    private List<ImportCell> importCells;

    public ImportFileType getImportFileType() {
        return importFileType;
    }

    public void setImportFileType(ImportFileType importFileType) {
        this.importFileType = importFileType;
    }

    public Integer getStartRowNo() {
        return startRowNo;
    }

    public void setStartRowNo(Integer startRowNo) {
        this.startRowNo = startRowNo;
    }

    public List<ImportCell> getImportCells() {
        return importCells;
    }

    public void setImportCells(List<ImportCell> importCells) {
        this.importCells = importCells;
    }

    public Integer getResultType() {
        return resultType;
    }

    public void setResultType(Integer resultType) {
        this.resultType = resultType;
    }

    public enum ParserType {
        XML
    }

    public enum ImportFileType {
        EXCEL
    }
}
