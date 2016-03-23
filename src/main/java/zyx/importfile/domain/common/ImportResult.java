package zyx.importfile.domain.common;


import zyx.domain.BaseModel;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/19.
 */
public abstract class ImportResult<E> extends BaseModel {
    public abstract List<E> getResult();
    private String resMsg;//返回的信息
    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

}
