package mydata.studio.op;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DataBean {
    private int cid;
    private String parentName;
    private String name;
    private String ddl;
}
