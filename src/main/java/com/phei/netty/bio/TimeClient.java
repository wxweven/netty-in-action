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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeClient.class);

    public static void main(String[] args) throws Exception {
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        socket = new Socket(CommonConstants.HOST, CommonConstants.PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println("QUERY TIME ORDER");
        LOGGER.info("Send order to server succeed.");
        String resp = in.readLine();
        LOGGER.info("Now is : {}", resp);

        socket.close();
        in.close();
        out.close();
    }
}
