/*
 * Copyright (c) 2009, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package top.fols.box.net.base.ftp9.impl;

/**
 * Default FtpClientProvider.
 * Uses sun.net.ftp.FtpCLient.
 */
public class DefaultFtpClientProvider extends top.fols.box.net.base.ftp9.FtpClientProvider {

    @Override
    public top.fols.box.net.base.ftp9.FtpClient createFtpClient() {
        return top.fols.box.net.base.ftp9.impl.FtpClient.create();
    }

}
