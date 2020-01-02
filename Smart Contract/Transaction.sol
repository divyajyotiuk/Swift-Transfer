 pragma solidity ^0.5.0;

contract TransactionReceipt{
    mapping (address=>receipt) Receipts;
    struct receipt 
    {
        //address sender;
        address recipient;
        bytes32 Send_am;
        bytes32 Re_am;
        bytes4 Send_cur;
        bytes4 Re_cur;
    }
    
    event TransactionSent(address indexed sender, address indexed receiver, bytes32 amount1, 
    bytes32 amount2, bytes4  cur1, bytes4 cur2);
   
    


function transact 
    (address recipient, bytes32 amount1, bytes32  amount2 , bytes4 cur1, bytes4 cur2) 
public{
    
    Receipts[msg.sender].recipient = recipient;
    Receipts[msg.sender].Send_am = amount1;
    Receipts[msg.sender].Re_am = amount2;
    Receipts[msg.sender].Send_cur = cur1;
    Receipts[msg.sender].Re_cur = cur2;
    
    emit TransactionSent(msg.sender, recipient, amount1,  amount2 , cur1, cur2);
}

/*function get() public view returns(address, bytes32, bytes4, bytes4){
return (Receipts[msg.sender].recipient, Receipts[msg.sender].Send_am, Receipts[msg.sender].Send_cur
            ,Receipts[msg.sender].Re_cur);
}*/

}
