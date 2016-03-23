package zyx.importfile;

import zyx.importfile.domain.common.Configuration;
import zyx.importfile.exception.FileImportException;
import zyx.importfile.impl.XmlConfigParser;

/**
 * Created by stark.zhang on 2015/11/28.
 */
public class ConfigurationParserFactory {
    public static ConfigParser getConfigParser(Configuration.ParserType parserType) throws FileImportException {
        if (parserType == null) {
            throw new FileImportException("parserType is null");
        }
        if (parserType == Configuration.ParserType.XML) {
            return new XmlConfigParser();
        }
        return new XmlConfigParser();
    }


}
