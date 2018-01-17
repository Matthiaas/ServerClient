/*
 * Server.h
 *
 *  Created on: Dec 11, 2017
 *      Author: matthias
 */

#ifndef SRC_SERVER_H_
#define SRC_SERVER_H_

#include <iostream>
#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <pthread.h>


namespace Network {

class Server {
public:
	Server(int port);
	virtual ~Server();
	int acceptNext();
	void closeServer();
	void openServerSocket();

private:
	int port;
	int sockfd = -1;
	struct sockaddr_in serverAddress;
};

} /* namespace Network */

#endif /* SRC_SERVER_H_ */
