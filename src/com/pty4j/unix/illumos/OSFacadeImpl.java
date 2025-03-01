/*
 * JPty - A small PTY interface for Java.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.pty4j.unix.illumos;

import com.pty4j.unix.PtyHelpers;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import jtermios.JTermios;

/**
 * Provides a {@link PtyHelpers.OSFacade} implementation for Illumos.
 */
public class OSFacadeImpl implements PtyHelpers.OSFacade {
  // INNER TYPES

  private interface Illumos_C_lib extends Library {
    int posix_openpt(int oflag);

    int kill(int pid, int signal);

    int waitpid(int pid, int[] stat, int options);

    int sigprocmask(int how, IntByReference set, IntByReference oldset);

    String strerror(int errno);

    int grantpt(int fdm);

    int unlockpt(int fdm);

    int close(int fd);

    String ptsname(int fd);

    int open(String pts_name, int o_rdwr);

    int killpg(int pid, int sig);

    int fork();

    int setsid();

    int getpid();

    int setpgid(int pid, int pgid);

    void dup2(int fd, int fileno);

    int getppid();

    void unsetenv(String s);

    int login_tty(int fd);

    void chdir(String dirpath);
  }

  private static final Illumos_C_lib m_Clib = Native.loadLibrary("c", Illumos_C_lib.class);

  // CONSTUCTORS

  /**
   * Creates a new {@link OSFacadeImpl} instance.
   */
  public OSFacadeImpl() {
    PtyHelpers.ONLCR = 4;

    PtyHelpers.VERASE = 2;
    PtyHelpers.VWERASE = 14;
    PtyHelpers.VKILL = 3;
    PtyHelpers.VREPRINT = 12;
    PtyHelpers.VINTR = 0;
    PtyHelpers.VQUIT = 1;
    PtyHelpers.VSUSP = 10;

    PtyHelpers.ECHOKE = 2048;
    PtyHelpers.ECHOCTL = 512;
  }

  // METHODS

  @Override
  public int kill(int pid, int signal) {
    return m_Clib.kill(pid, signal);
  }

  @Override
  public int waitpid(int pid, int[] stat, int options) {
    return m_Clib.waitpid(pid, stat, options);
  }

  @Override
  public int sigprocmask(int how, IntByReference set, IntByReference oldset) {
    return m_Clib.sigprocmask(how, set, oldset);
  }

  @Override
  public String strerror(int errno) {
    return m_Clib.strerror(errno);
  }

  @Override
  public int getpt() {
    return m_Clib.posix_openpt(JTermios.O_RDWR | JTermios.O_NOCTTY);
  }

  @Override
  public int grantpt(int fd) {
    return m_Clib.grantpt(fd);
  }

  @Override
  public int unlockpt(int fd) {
    return m_Clib.unlockpt(fd);
  }

  @Override
  public int close(int fd) {
    return m_Clib.close(fd);
  }

  @Override
  public String ptsname(int fd) {
    return m_Clib.ptsname(fd);
  }

  @Override
  public int killpg(int pid, int sig) {
    return m_Clib.killpg(pid, sig);
  }

  @Override
  public int fork() {
    return m_Clib.fork();
  }

  @Override
  public int pipe(int[] pipe2) {
    return JTermios.pipe(pipe2);
  }

  @Override
  public int setsid() {
    return m_Clib.setsid();
  }

  @Override
  public int getpid() {
    return m_Clib.getpid();
  }

  @Override
  public int setpgid(int pid, int pgid) {
    return m_Clib.setpgid(pid, pgid);
  }
  
  @Override
  public void dup2(int fds, int fileno) {
    m_Clib.dup2(fds, fileno);
  }

  @Override
  public int getppid() {
    return m_Clib.getppid();
  }

  @Override
  public void unsetenv(String s) {
    m_Clib.unsetenv(s);
  }

  @Override
  public int login_tty(int fd) {
    return m_Clib.login_tty(fd);
  }

  @Override
  public void chdir(String dirpath) {
    m_Clib.chdir(dirpath);
  }
}
