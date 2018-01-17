/*
 * Server.cpp
 *
 *  Created on: Dec 11, 2017
 *      Author: matthias
 */

#include "Server.h"

namespace Network {

Server::Server(int port) {
	this->port = port;

}

Server::~Server() {
	// TODO Auto-generated destructor stub
}



void Server::openServerSocket() {

	sockfd=socket(AF_INET,SOCK_STREAM,0);
	memset(&serverAddress,0,sizeof(serverAddress));
	serverAddress.sin_family=AF_INET;
	serverAddress.sin_addr.s_addr=htonl(INADDR_ANY);
	serverAddress.sin_port=htons(port);
	std::cout << port <<std::endl;
	bind(sockfd,(struct sockaddr *)&serverAddress, sizeof(serverAddress));
	listen(sockfd,5);

}


int Server::acceptNext() {
	struct sockaddr_in clientAddress;
	socklen_t sosize  = sizeof(clientAddress);
	int	cfd = accept(sockfd,(struct sockaddr*)&clientAddress,&sosize);
	std::string str = inet_ntoa(clientAddress.sin_addr);

	std::cout << str <<std::endl;
	return cfd;
}


void Server::closeServer() {
	close(sockfd);
}

} /* namespace Network */
