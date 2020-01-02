 
import Firebase from './firebase';
import { transact } from './contract';

var idno;
var dbref = Firebase.database().ref('WUAccount');
	    dbref.orderByChild('kyc').equalTo(false).once("value", function(snapshot){

        	//console.log(snapshot.val());
            var content = '';
            snapshot.forEach(function(data){
                var val = data.val();
                var args = new Array(val.fullName.toString(), val.idProofName.toString(), val.idProofNo.toString(), data.key.toString());
                content +='<tr>';
                content += '<td>' + val.fullName + '</td>';
                content += '<td>' + val.idProofName + '</td>';
                content += '<td>' + val.idProofNo + '</td>';
                content += '<td>' + val.bankName + '</td>';
                content += '<td>' + val.country + '</td>';
                content += '<td>' + val.currency + '</td>';
                idno=val.idProofNo;
              //  content += '<td> <button id="button1" value=' + val.idProofNo + ' onclick=Verify(this.value)> Accept</button> </td>';
              content += '<td> <button id="button1"> Accept</button> </td>';
              content += '</tr>';
            });
            $('#ex-table').append(content);
        });

      function Verify(pno){
            confirm("Complete KYC");
            console.log("Verification ==> " + pno);
            
            var ver = firebase.database().ref('WUAccount');
            ver.orderByChild('idProofNo').equalTo(pno).once("value", function(snapshot){
                    
                    snapshot.forEach(function(data){
                    var val = data.val();
                    console.log(val);
                        transact("0x7D67fDDF821f7AE61B1eC9Ad75B1eaC808650aEb",val.idProofNo.toString(),val.idProofName);
                });
            });
        }

    const  butt = document.getElementById("button1");
    butt.addEventListener('click',()=>{
            Verify(idno);
    });
