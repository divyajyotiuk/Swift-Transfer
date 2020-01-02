pragma solidity ^0.5.0;

contract kyc {
    
    address private owner;
    
     event NewUser(address user, string idno,string proof);
    
    struct User{
        address user;
        string idno;
        string proof;
    }
    
    User [] public users;
    
    constructor(address own) public {
        owner = own;
    }
    
    
    function validate (address _user, string memory _idno, string memory _proof) internal {
         require(msg.sender==owner);
         users.push(User(_user,_idno,_proof));
         emit NewUser(_user,_idno,_proof);
        
    }
}
