#include <iostream>

#include "src/Client.h"
#include "src/Server.h"






using namespace std;

using namespace Network;

int main(){


//	Client c("127.0.0.1" , 10000);
//
//	c.setUpConnection();
//
//
//	long long l = 270031635112000162l;
//	c.writeLong(l);
//	cout << std::hex <<l  << endl;
//	c.writeByte(20);

	Server s(10556);
	s.openServerSocket();

	Client c(s.acceptNext());
	cout << endl;
	string str = string("Welcome to the car controllong service!\n\t0 to stop\n\t1 for gas\n\t2 for left\n\t3 for straigt\n\t4 for rigth\n\tPlease send data as byte!");
		c.writeString(str);
	while(true){
		int res = c.readByte();
		if(c.isClosed()) break;
		cout << res << endl;
	}



	s.closeServer();
//	c.closeConnection();

}


