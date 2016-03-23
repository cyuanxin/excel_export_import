package zyx.export.domain.common;


import zyx.domain.BaseModel;

/**
 * Created by stark.zhang on 2015/11/2.
 */
public class ExportCell extends BaseModel {
    private String name;
    private String title;//导出的标题中文
    private String alias;//对应的别名，mybatis映射的字段名

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
