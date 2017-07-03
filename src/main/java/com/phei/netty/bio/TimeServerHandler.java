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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServerHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServerHandler.class);

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in;
            PrintWriter out;
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime;
            String body;
            while (true) {
                body = in.readLine();
                if (body == null)
                    break;
                LOGGER.info("The time server receive order : " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println(currentTime);
            }

            in.close();
            out.close();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
