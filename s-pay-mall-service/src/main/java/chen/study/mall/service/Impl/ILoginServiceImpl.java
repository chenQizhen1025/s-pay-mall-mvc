package chen.study.mall.service.Impl;


import chen.study.mall.service.ILoginService;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/13 18:37
 */
@Service
public class ILoginServiceImpl implements ILoginService {
    @Override
    public String createQrCodeTicket() throws Exception {
        return "";
    }

    @Override
    public String checkLogin(String ticket) {
        return "";
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws IOException {

    }
}
