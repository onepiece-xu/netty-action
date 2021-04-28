package com.ydw.control.task;

import com.ydw.control.service.IControlService;
import com.ydw.control.service.IDeviceService;
import com.ydw.control.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/2814:38
 */
@Component
public class LogTask implements StartTask{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logger.debug("hello world!");
            }
        }, 10*1000, 15);
    }
}
