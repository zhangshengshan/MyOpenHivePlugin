package mydata.studio.op;

import mydata.studio.service.SessionService;
import openbrowser.GenCodeOp$;

/**
 * @author nobody
 */
public class GenOp {

    public static String getSelectPart(String param) {
        return GenCodeOp$.MODULE$.genSelectCodePiece(param, SessionService.getSessionId());
    }
}
