// Copyright (C) <2018> Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0

'use strict';

/**
 * @class SignalingChannel
 * @classDesc Signaling module for Open WebRTC Toolkit P2P chat
 */
function SignalingChannel() {
  this.onMessage = null;
  this.onServerDisconnected = null;

  const clientType = 'Web';
  const clientVersion = '4.2';

  let wsServer = null;

  const self = this;

  let connectPromise = null;

  const MAX_TRIALS = 10;
  let reconnectTimes = 0;

  /* TODO: Do remember to trigger onMessage when new message is received.
     if(this.onMessage)
       this.onMessage(from, message);
   */

  // message should a string.
  this.send = function(targetId, message) {
    const data = {
      data: message,
      to: targetId,
    };
    return new Promise((resolve, reject) => {
      wsServer.emit('owt-message', data, function(err) {
        if (err) {
          reject(err);
        } else {
          resolve();
        }
      });
    });
  };

  this.connect = function(loginInfo) {
    const serverAddress = loginInfo.host;
    const token = loginInfo.token;
    const parameters = [];
    let queryString = null;
    parameters.push('clientType=' + clientType);
    parameters.push('clientVersion=' + clientVersion);
    if (token) {
      parameters.push('token=' + encodeURIComponent(token));
    }
    if (parameters) {
      queryString = parameters.join('&');
    }
    console.log('Query string: ' + queryString);
    const opts = {
      'query': queryString,
      'reconnection': true,
      'reconnectionAttempts': MAX_TRIALS,
      'force new connection': true,
    };
    wsServer = io(serverAddress, opts);

    wsServer.on('connect', function() {
      reconnectTimes = 0;
      console.info('Connected to websocket server.');
    });

    wsServer.on('server-authenticated', function(data) {
      console.log('Authentication passed. User ID: ' + data.uid);
      if (connectPromise) {
        connectPromise.resolve(data.uid);
      }
      connectPromise = null;
    });

    wsServer.on('reconnecting', function() {
      reconnectTimes++;
    });

    wsServer.on('reconnect_failed', function() {
      if (self.onServerDisconnected) {
        self.onServerDisconnected();
      }
    });

    wsServer.on('server-disconnect', function() {
      reconnectTimes = MAX_TRIALS;
    });

    wsServer.on('disconnect', function() {
      console.info('Disconnected from websocket server.');
      // if(connected){
      //   $('#remote-video').get(0).srcObject = undefined;
      //   $('#remote-video').hide();
      //   $('#tips').show();
      // }
      if (reconnectTimes >= MAX_TRIALS && self.onServerDisconnected) {
        self.onServerDisconnected();
      }
    });

    wsServer.on('connect_failed', function(errorCode) {
      console.error('Connect to websocket server failed, error:' +
        errorCode + '.');
      if (connectPromise) {
        connectPromise.reject(parseInt(errorCode));
      }
      connectPromise = null;
    });

    wsServer.on('error', function(err) {
      console.error('Socket.IO error:' + err);
      if (err == '2103' && connectPromise) {
        connectPromise.reject(err);
        connectPromise = null;
      }
    });

    wsServer.on('owt-message', function(data) {
      console.info('Received owt message.');
      if (self.onMessage) {
        self.onMessage(data.from, data.data);
      }
    });

    return new Promise((resolve, reject) => {
      connectPromise = {
        resolve,
        reject,
      };
    });
  };

  this.disconnect = function() {
    reconnectTimes = MAX_TRIALS;
    if (wsServer) {
      wsServer.close();
    }
    return Promise.resolve();
  };
}