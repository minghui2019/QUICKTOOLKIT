package com.baidu.translate.api;

import com.eplugger.network.IApiRequest;

public enum TransApi implements IApiRequest {
    TRANS_VIP("https://fanyi-api.baidu.com/api/trans/vip/translate")
    ;

    private String url;

    TransApi(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
