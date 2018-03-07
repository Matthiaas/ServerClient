#include <iostream>

#include "src/Client.h"
#include "src/Server.h"


using namespace std;

using namespace Network;

int main(){


	//Server listening on port 10556
	Server s(10556);
	s.openServerSocket();


	//This is the Client connection
	Client c(s.acceptNext());


	cout << endl;
	string str = string("Welcome Message");
	c.writeString(str);



	//Closes the Server
	s.closeServer();


	//Closes the connection to client c
	c.closeConnection();

}
