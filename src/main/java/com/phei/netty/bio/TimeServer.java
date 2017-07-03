/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phei.netty.bio;

import com.phei.netty.constant.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServer.class);

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(CommonConstants.PORT);
        LOGGER.info("The time server is start in port: {} ", CommonConstants.PORT);
        Socket socket;
        while (true) {
            socket = server.accept();
            new Thread(new TimeServerHandler(socket)).start();
        }
    }
}
