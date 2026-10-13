package chen.study.mall.service;


import java.io.IOException;

/**
 * @author 25986
 * @description 
 * @createTime 2026/10/13 18:36
 */
public interface ILoginService {

    String createQrCodeTicket() throws Exception;

    String checkLogin(String ticket);

    void saveLoginState(String ticket, String openid) throws IOException;

}

