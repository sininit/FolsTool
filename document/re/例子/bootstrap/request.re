

import java.net.URL;
import top.fols.box.net.Requests;
import top.fols.atri.net.HttpURLBuilder;
import top.fols.atri.net.MessageHeader;


function request(req) {
    method  = req.method;
    url     = req.url;
    params  = req.param;
    headers = req.header;
    
    data    = if (null == req.data) {null} else {str(req.data)};
    
    if (null == method) { method= "GET" };
    if (null != data)   { method= "POST" };
    
    ub = URLBuilder(url);
    if ((null != params) && (lenattr(params) > 0)) {
        foreach(k, v, params) {
            if (not(is_str(k))) {
                continue;
            };
            ub.setParam(k, str(v));
        };
    };
    
    request = Requests.on(ub.build());
    request.method(method);

    if (is_str(headers)) {
        h  = MessageHeader(headers);
    } else {
        h  = MessageHeader();
        if (lenattr(headers) > 0) {
            foreach(k, v, headers) {
                if (not(is_str(k))) {
                    continue;
                };
                h.add(k, str(v));
            };
        };
    };
    
    
    request.headers(h);
    request.start();
    request.await();
    
    r = request.request();
    
    return(r);
};
