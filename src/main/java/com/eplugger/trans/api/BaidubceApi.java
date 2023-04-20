package com.eplugger.trans.api;

import com.eplugger.network.IApiRequest;

public enum BaidubceApi implements IApiRequest {
    TEXT_TRANS("https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1?access_token=%s"),
    OAUTH_TOKEN("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s")
    ;

    private String url;

    BaidubceApi(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
