package zyx.export.service;

import zyx.export.domain.common.ExportCell;
import zyx.export.exception.FileExportException;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/6.
 */
public interface IFileExportor {
    /**
     * 数据导出
     * @param data
     * @param exportCells
     * @return
     * @throws zyx.export.exception.FileExportException
     */
    public Object getExportResult(List<?> data, List<ExportCell> exportCells) throws FileExportException;


}
