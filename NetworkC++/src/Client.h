/*
 * Client.h
 *
 *  Created on: Dec 4, 2017
 *      Author: matthias
 */

#ifndef CLIENT_H_
#define CLIENT_H_

#include <iostream>
#include <sstream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <netdb.h>
#include <vector>

namespace Network {

class Client {
public:
	Client(std::string adress, int port);
	Client(int sock);
	virtual ~Client();

	bool setUpConnection();
	void closeConnection();

	bool isClosed();
	//Write
	bool writeChar(char c);
	bool writeByte(int c);
	bool writeCharArray(char *c, int len);
	bool wirteCharVector(std::vector<char> &v);
	bool writeString(std::string &s);
	bool writeInt(int i);
	bool writeLong(long long  l);


	//Read
	int readByte();
	char readChar();
	int readInt();
	long long  readLong();
	void read(char[] , int len , int off);
	std::string readString();


private:
	int sock;
	std::string adress;
	int port;
	struct sockaddr_in server;
	bool closed = true;
};


} /* namespace Network */
#endif /* CLIENT_H_ */
