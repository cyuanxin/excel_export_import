package zyx.export.domain.common;

/**
 * Created by stark.zhang on 2015/11/12.
 */
public enum ExportType {
    EXCEL2007(0, "xlsx", "application/vnd.ms-excel"),
    CSV(1, "csv", "application/csv");
    private final String fileType;
    private final int number;
    private final String contentType;

    public static ExportType getExportType(int number) {
        for (ExportType exportType : ExportType.values()) {
            if (exportType.getNumber() == number) {
                return exportType;
            }
        }
        return null;
    }

    ExportType(Integer number, String fileType, String contentType) {
        this.fileType = fileType;
        this.number = number;
        this.contentType = contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public int getNumber() {
        return number;
    }

    public String getContentType() {
        return contentType;
    }


}
