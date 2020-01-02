import Portis from "@portis/web3";
import Web3 from "web3";


const portis = new Portis("61f1e9b2-488e-4a59-a3e3-24e855799d8d","ropsten");
const web3 = new Web3(portis.provider);
		
		var ContractABI = [{
		"constant": false,
		"inputs": [
			{
				"name": "user",
				"type": "address"
			},
			{
				"name": "idno",
				"type": "string"
			},
			{
				"name": "proof",
				"type": "string"
			}
		],
		"name": "validate",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"inputs": [
			{
				"name": "own",
				"type": "address"
			}
		],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"anonymous": false,
		"inputs": [
			{
				"indexed": false,
				"name": "user",
				"type": "address"
			},
			{
				"indexed": false,
				"name": "idno",
				"type": "string"
			},
			{
				"indexed": false,
				"name": "proof",
				"type": "string"
			}
		],
		"name": "NewUser",
		"type": "event"
	},
	{
		"constant": true,
		"inputs": [
			{
				"name": "",
				"type": "uint256"
			}
		],
		"name": "users",
		"outputs": [
			{
				"name": "user",
				"type": "address"
			},
			{
				"name": "idno",
				"type": "string"
			},
			{
				"name": "proof",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function" }];

		var acc;
	//	const account1 = '0x53D8C4d0a0dDD9faC8f5D1ab33E8e1673d9481Da'
	//	const private_key = '198ccd740c0b57fc8bcb25d544683684aebb1425738fe580a4fa6e0d8ed85f79'
	web3.eth.getAccounts().then(accounts => {
		acc = accounts[0];
		console.log(acc);
	
	})
		var contract = new web3.eth.Contract(ContractABI,"0xc5eB394ED5CBf0A5b0e23956eE49674A7064f1Be");
		

	//	var Contract = new web3.eth.Contract(ContractABI, "0xc5eB394ED5CBf0A5b0e23956eE49674A7064f1Be"); 
		//var ContractInstance = Contract.at(0xc5eB394ED5CBf0A5b0e23956eE49674A7064f1Be);
	export function transact(address,idno,idProofName){
			contract.methods.validate(address, idno,idProofName).send({
				from:accounts[0]
			}).then(result=>{
				console.log(result)
			}).catch(err=> console.log(err));
		}

		

