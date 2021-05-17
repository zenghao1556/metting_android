package com.taiyi.metting.inter;

public interface IReceiveMessage {
    void onConnectSuccess();// 连接成功

    void onConnectFailed();// 连接失败

    void onClose(); // 关闭

    void onMessage(String text);
}
