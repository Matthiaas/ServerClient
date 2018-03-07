/*
 * Client.cpp
 *
 *  Created on: Dec 4, 2017
 *      Author: matthias
 */

#include "Client.h"


namespace Network {



Client::Client(std::string address, int port) {
	this->port = port;
	this->adress = address;
	sock = -1;
}

Client::Client(int sock) {
	adress = "";
	port = 0;
	this->sock = sock;
	closed = false;
}

Client::~Client(){
	closed = false;
}



inline void net_error(char * str){
#if ENABLE_EXCEPTIONS
		throw std::runtime_error(str);
#endif
#if ENABLE_OUTPUT
		std::cerr << str << std::endl;
#endif
}


bool Client::setUpConnection() {
	if (sock != -1)
		return false;
	sock = socket(AF_INET, SOCK_STREAM, 0);
	if (sock == -1) {
		net_error("Could not create socket");
		return false;
	}

	server.sin_addr.s_addr = inet_addr(adress.c_str());
	server.sin_family = AF_INET;
	server.sin_port = htons(port);

	if (connect(sock, (struct sockaddr *) &server, sizeof(server)) < 0) {
		net_error("Connection Failed");
		return false;
	}

	closed = false;
	return true;
}
void Client::closeConnection(){
	if(sock!= -1)
		close(sock);
}


//Write
bool Client::writeChar(char c) {

	if (send(sock, &c, 1, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

bool Client::writeByte(int c) {
	if (send(sock, &c, 1, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

bool Client::writeCharArray(char* c, int len) {
	if (send(sock, c, len, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

bool Client::writeCharVector(std::vector<char>& v) {
	if (send(sock, &v[0], v.size(), 0) <= 0) {
		closed = true;
		net_error("Send failed");
		return false;
	}
	return true;

}

bool Client::writeString(std::string& s) {
	if (send(sock, s.c_str(), s.size() + 1, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

bool Client::writeInt(int value) {
	value = __builtin_bswap32(value);
	if (send(sock, &value, 8, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

bool Client::writeLong(long long value) {
	value = __builtin_bswap64(value);
	if (send(sock, &value, 16, 0) <= 0) {
		net_error("Send failed");
		closed = true;
		return false;
	}
	return true;
}

//Read
int Client::readByte() {
	int res = 0;
	if (recv(sock, &res, 1, 0) <= 0){ //
		net_error("Receive failed");
		closed = true;
	}
	return res;
}

char Client::readChar() {
	char res = 0;
	if (recv(sock, &res, 1, 0) <= 0) { //
		net_error("Receive failed");
		closed = true;
	}
	return res;
}

int Client::readInt() {
	int res = 0;
	for (int i = 24; i >= 0; i -= 8) {
		int c;
		if (recv(sock, &c, 1, 0) <= 0){ //
			net_error("Receive failed");
			closed = true;
		}
		res |=((int)c <<i);
	}
	return res;

}

long long Client::readLong() {
	long res = 0;
	for (int i = 56; i >= 0; i -= 8) {
		char c;
		if (recv(sock, &c, 1, 0) <= 0){ //
			net_error("Receive failed");
			closed = true;
		}
		//(& 0xFFl) because of the cast!!!
		res |= (((long long )c & 0xFFl)<<i);


	}
	return res;
}

bool Client::read(char* buf, int len, int off) {
	if (recv(sock, buf+off, len, 0) <= 0){ //
		net_error("Receive failed");
		closed = true;
	}
}

bool Client::isClosed() {
	return closed;
}

std::string Client::readString() {
	std::ostringstream os;
	char c;
	do{
		if (recv(sock, &c, 1, 0) <= 0){
			net_error("Receive failed");
			closed = true;
		}
		os << c;

	}while(c != 0);
	return os.str();
}


} /* namespace Network */

