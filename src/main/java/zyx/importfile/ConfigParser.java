package zyx.importfile;


import zyx.importfile.domain.common.Configuration;
import zyx.importfile.exception.FileImportException;

import java.io.InputStream;

/**
 * Created by stark.zhang on 2015/11/22.
 * 解析config接口
 */
public interface ConfigParser {
    public Configuration getConfig(InputStream configStream) throws FileImportException;
}
