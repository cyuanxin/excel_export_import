package zyx.importfile.domain;

import zyx.importfile.domain.common.ImportResult;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/20.
 */
public class MapResult<Map> extends ImportResult {
    public final static String LINE_NUM_KEY = "lineNum";
    public final static String IS_LINE_LEGAL_KEY = "isLineLegal";
    List<Map> result;

    @Override
    public List<Map> getResult() {
        return result;
    }

    public void setResult(List<Map> result) {
        this.result = result;
    }
}
