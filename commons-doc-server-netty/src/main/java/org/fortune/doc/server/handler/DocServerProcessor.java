package org.fortune.doc.server.handler;

import org.fortune.doc.common.domain.result.Result;
import org.fortune.doc.server.parse.RequestParam;

/**
 * @author: landy
 * @date: 2019/6/2 21:03
 * @description:
 */
public interface DocServerProcessor {

    Result process(RequestParam reqParams);

}
